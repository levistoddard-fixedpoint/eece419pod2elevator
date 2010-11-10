package com.pod2.elevator.core;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pod2.elevator.core.events.Event;
import com.pod2.elevator.core.events.PassengerRequest;
import com.pod2.elevator.data.SimulationResultsBuilder;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.scheduling.ElevatorScheduler;
import com.pod2.elevator.view.ElevatorSnapShot;
import com.pod2.elevator.view.SimulationView;

public class ActiveSimulation {

	/* TODO move this into the template */
	private static int CAPACITY = 10;

	private long currentQuantum;
	private Elevator[] elevators;
	private FloorRequestButton[] floorRequestButtons;
	private Multimap<Integer, RequestInTransit> floorQueues;
	private Multimap<Long, Event> eventQueue;
	private ElevatorScheduler scheduler;
	private RequestGenerator requestGenerator;
	private Thread simulationThread;
	private SimulationView simulationView;
	private boolean isRunning;
	private SimulationResultsBuilder simulationResultsBuilder;
	private List<Event> delayedEventQueue;
	private SimulationTemplate template;

	public ActiveSimulation(SimulationTemplate simulationTemplate,
			SimulationResultsBuilder simulationResultsBuilder,
			SimulationView simulationView) {
		this.template = simulationTemplate;
		this.simulationResultsBuilder = simulationResultsBuilder;
		this.simulationView = simulationView;
		initializeFromTemplate(simulationTemplate);

		/* independent of input parameters. */
		currentQuantum = 0;
		simulationThread = new Thread(new SimulationThread(this));
		isRunning = false;
	}

	private void initializeFromTemplate(SimulationTemplate template) {
		this.scheduler = template.scheduler;

		/* Create the elevators. */
		elevators = new Elevator[template.getNumberElevators()];
		for (int n = 0; n < elevators.length; n++) {
			elevators[n] = new Elevator(n, CAPACITY,
					template.getRestrictedFloors());
		}

		/* Create floor-specific objects. */
		floorQueues = HashMultimap.create();
		floorRequestButtons = new FloorRequestButton[template.getNumberFloors()];
		for (int n = 0; n < floorRequestButtons.length; n++) {
			floorRequestButtons[n] = new FloorRequestButton();
		}

		/* Create and initialise event queues */
		delayedEventQueue = new LinkedList<Event>();
		eventQueue = HashMultimap.create();
		for (Event event : template.getEvents()) {
			eventQueue.put(event.getTimeQuantum(), event);
		}

		/* Initialise passenger generation */
		boolean generateEnabled = template.isRequestGenerationOn();
		requestGenerator = new RequestGenerator(generateEnabled);
	}

	/*
	 * MANAGEMENT INTERFACE
	 */
	public void start() {
		simulationView.startup(template);
		simulationResultsBuilder.setStartTime(currentQuantum, new Date());
		simulationThread.start();
		isRunning = true;
	}

	public void stop() throws InterruptedException {
		simulationThread.interrupt();
		try {
			simulationThread.join();
		} catch (InterruptedException e) {
			/* TODO determine interaction with rest of system */
		}
		simulationView.teardown();
		simulationResultsBuilder.setEndTime(currentQuantum, new Date());
		simulationResultsBuilder.save();
		isRunning = false;
	}

	public boolean getIsRunning() {
		return isRunning;
	}

	public void setScheduler(ElevatorScheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void setRequestGenerationStatus(boolean enabled) {
		requestGenerator.setEnabled(enabled);
	}

	public void enqueueEvent(Event event) {
		eventQueue.put(currentQuantum + 1, event);
	}

	public void enqueueEvent(long timeQuantum, Event event) {
		assert (timeQuantum < currentQuantum);
		eventQueue.put(timeQuantum, event);
	}

	/*
	 * SIMULATION FUNCTIONALITY
	 */
	protected void executeNextQuantum() {
		/*
		 * set-up the current cycle for execution.
		 */
		applyEvents();
		generateRequests();
		handlePassengerOffloads();
		handlePassengerOnloads();
		scheduler.schedule(this);

		/*
		 * move the elevators.
		 */
		executeQuantum();

		/*
		 * output state of simulation.
		 */
		simulationResultsBuilder.addResults(this);
		simulationView.displaySnapShot(createSnapShot());
	}

	private void applyEvents() {
		/* Apply events who couldn't apply at their appropriate time. */
		Iterator<Event> eventsItr = delayedEventQueue.iterator();
		while (eventsItr.hasNext()) {
			Event event = eventsItr.next();
			if (event.canApplyNow(this)) {
				event.apply(this);
				eventsItr.remove();
			}
		}

		/* Apply events for this quantum. */
		for (Event event : eventQueue.removeAll(currentQuantum)) {
			if (event.canApplyNow(this)) {
				event.apply(this);
			} else {
				delayedEventQueue.add(event);
			}
		}
	}

	private void generateRequests() {
		for (PassengerRequest request : requestGenerator.nextRequests()) {
			putPassengerIntoFloorQueue(request);
		}
	}

	private void putPassengerIntoFloorQueue(PassengerRequest request) {
		int onload = request.getOnloadFloor();
		int offload = request.getOffloadFloor();
		floorQueues.put(onload, new RequestInTransit(request));
		/* TODO account for doors being open for multiple cycles? */
		floorRequestButtons[onload].click(currentQuantum, offload > onload);
	}

	private void handlePassengerOffloads() {
		for (int n = 0; n < elevators.length; n++) {
			Elevator elevator = elevators[n];
			if (elevator.getMotionStatus().equals(MotionStatus.DoorsOpen)) {
				offloadPassengers(elevator);
			}
		}
	}

	private void offloadPassengers(Elevator elevator) {
		for (RequestInTransit request : elevator.offloadPassengers()) {
			request.setDeliveryStatus(DeliveryStatus.Delivered);
			request.setOffloadQuantum(currentQuantum);
			simulationResultsBuilder.logFinishedRequest(request);
		}
	}

	private void handlePassengerOnloads() {
		for (int n = 0; n < elevators.length; n++) {
			Elevator elevator = elevators[n];
			MotionStatus motionStatus = elevator.getMotionStatus();
			ServiceStatus serviceStatus = elevator.getServiceStatus();
			if (motionStatus.equals(MotionStatus.DoorsOpen)
					&& serviceStatus.equals(ServiceStatus.InService)) {
				onloadPassengers(elevator);
			}
		}
	}

	private void onloadPassengers(Elevator elevator) {
		int currFloor = (int) elevator.getPosition();
		Collection<RequestInTransit> requests = floorQueues.get(currFloor);
		Iterator<RequestInTransit> itr = requests.iterator();
		while (itr.hasNext()) {
			RequestInTransit request = itr.next();
			if (elevator.onloadPassenger(request)) {
				request.setElevatorNumber(elevator.getElevatorNumber());
				request.setOnloadQuantum(currentQuantum);
				itr.remove();
			} else {
				/*
				 * TODO somehow click the request button again for passengers
				 * who were left behind.
				 */
				break;
			}
		}
	}

	private void executeQuantum() {
		for (Elevator elevator : elevators) {
			elevator.executeQuantum();
		}
	}

	private ElevatorSnapShot createSnapShot() {
		return null;
//		ElevatorSnapShot snapshot = new ElevatorSnapShot();
//		/* TODO implement this. */
//		return snapshot;
	}

}
