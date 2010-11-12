package com.pod2.elevator.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pod2.elevator.core.events.Event;
import com.pod2.elevator.core.events.PassengerRequest;
import com.pod2.elevator.core.events.RequestInTransit;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.scheduling.ElevatorScheduler;
import com.pod2.elevator.view.ElevatorSnapShot;
import com.pod2.elevator.view.FloorSnapShot;
import com.pod2.elevator.view.LogMessage;
import com.pod2.elevator.view.SystemSnapShot;

public class ActiveSimulation {

	private long currentQuantum;
	private long nextPassengerNumber;
	private Elevator[] elevators;
	private FloorRequestButton[] floorRequestButtons;
	private Multimap<Integer, RequestInTransit> floorQueues;
	private Multimap<Long, Event> eventQueue;
	private List<Event> delayedEventQueue;
	private boolean isRunning;

	private SimulationTemplate template;
	private ResultsBuilder results;
	private SimulationDisplay display;
	private Thread simulationThread;

	private ElevatorScheduler scheduler;
	private RequestGenerator requestGenerator;

	public ActiveSimulation(SimulationTemplate template,
			ResultsBuilder results, SimulationDisplay display) {
		this.template = template;
		this.results = results;
		this.display = display;
		initializeFromTemplate(template);

		/* independent of input parameters. */
		currentQuantum = 0;
		nextPassengerNumber = 1;
		simulationThread = new Thread(new SimulationThread(this));
		isRunning = false;
	}

	private void initializeFromTemplate(SimulationTemplate template) {
		this.scheduler = template.getScheduler();

		/* Create the elevators. */
		elevators = new Elevator[template.getNumberElevators()];
		for (int n = 0; n < elevators.length; n++) {
			elevators[n] = new Elevator(this, n, template.getNumberFloors(),
					template.getElevatorCapacity(), template.getSpeed(),
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
			enqueueEvent(event);
		}

		/* Initialise passenger generation */
		boolean generateEnabled = template.isRequestGenerationOn();
		requestGenerator = new RequestGenerator(generateEnabled);
	}

	/*
	 * MANAGEMENT INTERFACE
	 */
	public void start() {
		display.startup(template);
		results.onStart();
		simulationThread.start();
		isRunning = true;
	}

	public void stop() throws InterruptedException {
		simulationThread.interrupt();
		simulationThread.join();
		display.teardown();
		results.onEnd(currentQuantum);
		results.save();
		isRunning = false;
	}

	/*
	 * STATE INTERFACE
	 */
	public long getQuantum() {
		return currentQuantum;
	}

	public Elevator[] getElevators() {
		return Arrays.copyOf(elevators, elevators.length);
	}

	public FloorRequestButton[] getRequestButtons() {
		return Arrays.copyOf(floorRequestButtons, floorRequestButtons.length);
	}

	public int getNumberFloors() {
		return template.getNumberFloors();
	}

	public boolean getIsRunning() {
		return isRunning;
	}

	public void setScheduler(ElevatorScheduler scheduler) {
		this.scheduler = scheduler;
	}

	public ElevatorScheduler getScheduler() {
		return scheduler;
	}

	public void setRequestGenerationStatus(boolean enabled) {
		requestGenerator.setEnabled(enabled);
	}

	public boolean getRequestGenerationStatus() {
		return requestGenerator.isEnabled();
	}

	public void enqueueEvent(Event event) {
		eventQueue.put(event.getTimeQuantum(), event);
	}

	public void enqueuePassenger(PassengerRequest request) {
		enqueuePassenger(new RequestInTransit(nextPassengerNumber++, request));
	}

	private void enqueuePassenger(RequestInTransit request) {
		int onload = request.getOnloadFloor();
		int offload = request.getOffloadFloor();
		request.setDeliveryStatus(DeliveryStatus.Waiting);
		floorQueues.put(onload, request);
		floorRequestButtons[onload].click(currentQuantum, offload > onload);
		results.logEvent(currentQuantum, request);
	}

	public int getPassengersWaiting(int floorNumber) {
		return floorQueues.get(floorNumber).size();
	}

	/*
	 * SIMULATION FUNCTIONALITY
	 */
	void executeNextQuantum() {
		/*
		 * set-up the current quantum for execution.
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
		 * export state of simulation.
		 */
		results.onQuantumComplete(this);
		display.update(createSnapShot());

		/*
		 * move onto next quantum.
		 */
		incrementQuantum();
	}

	void onElevatorPutInService(Elevator elevator) {
		/*
		 * Rescue stranded passengers.
		 */
		offloadPassengers(elevator, DeliveryStatus.Rescued);
	}

	void onElevatorDoorsClosed(Elevator elevator) {
		/*
		 * Click the button for any passengers still waiting.
		 */
		int currFloor = (int) elevator.getPosition();
		for (RequestInTransit request : floorQueues.get(currFloor)) {
			int onload = request.getOnloadFloor();
			int offload = request.getOffloadFloor();
			FloorRequestButton button = floorRequestButtons[currFloor];
			button.click(currentQuantum, offload > onload);
		}
	}

	private void applyEvents() {
		/* Apply events who couldn't apply at their appropriate time. */
		Iterator<Event> itr = delayedEventQueue.iterator();
		while (itr.hasNext()) {
			Event event = itr.next();
			if (event.canApplyNow(this)) {
				event.apply(this);
				if (event.isLoggable()) {
					results.logEvent(currentQuantum, event);
				}
				itr.remove();
			}
		}

		/* Apply events for this quantum. */
		for (Event event : eventQueue.removeAll(currentQuantum)) {
			if (event.canApplyNow(this)) {
				event.apply(this);
				if (event.isLoggable()) {
					results.logEvent(currentQuantum, event);
				}
			} else {
				delayedEventQueue.add(event);
			}
		}

	}

	private void generateRequests() {
		for (PassengerRequest request : requestGenerator
				.nextRequests(currentQuantum)) {
			enqueuePassenger(request);
		}
	}

	private void handlePassengerOffloads() {
		for (int n = 0; n < elevators.length; n++) {
			Elevator elevator = elevators[n];
			if (elevator.getMotionStatus().equals(MotionStatus.DoorsOpen)) {
				offloadPassengers(elevator, DeliveryStatus.Delivered);
			}
		}
	}

	private void offloadPassengers(Elevator elevator, DeliveryStatus status) {
		for (RequestInTransit request : elevator.offloadPassengers()) {
			request.setDeliveryStatus(status);
			request.setOffloadQuantum(currentQuantum);
			results.logEvent(currentQuantum, request);
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
		Collection<RequestInTransit> reqs = floorQueues.removeAll(currFloor);
		reqs = new LinkedList<RequestInTransit>(reqs); // previously
														// unmodifiable
		Iterator<RequestInTransit> itr = reqs.iterator();
		while (itr.hasNext()) {
			RequestInTransit request = itr.next();
			if (elevator.onloadPassenger(request)) {
				request.setDeliveryStatus(DeliveryStatus.InElevator);
				request.setElevatorNumber(elevator.getElevatorNumber());
				request.setOnloadQuantum(currentQuantum);
				results.logEvent(currentQuantum, request);
				itr.remove();
			} else {
				/* elevator capacity filled up. */
				break;
			}
		}
	}

	private void executeQuantum() {
		for (Elevator elevator : elevators) {
			elevator.executeQuantum();
		}
	}

	private void incrementQuantum() {
		currentQuantum++;
	}

	private SystemSnapShot createSnapShot() {

		int numberFloors = template.getNumberFloors();
		FloorSnapShot[] floorSnapshots = new FloorSnapShot[numberFloors];
		for (int n = 0; n < numberFloors; n++) {
			floorSnapshots[n] = new FloorSnapShot(floorRequestButtons[n],
					getPassengersWaiting(n));
		}

		int numberElevators = template.getNumberElevators();
		ElevatorSnapShot[] elevatorSnapshots = new ElevatorSnapShot[numberElevators];
		for (int n = 0; n < numberElevators; n++) {
			elevatorSnapshots[n] = elevators[n].createSnapshot();
		}

		LogMessage[] messages = results.getLoggedEvents(currentQuantum);

		return new SystemSnapShot(currentQuantum, elevatorSnapshots,
				floorSnapshots, messages);
	}

}
