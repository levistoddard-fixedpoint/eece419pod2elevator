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
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.scheduling.ElevatorScheduler;
import com.pod2.elevator.view.data.ElevatorSnapShot;
import com.pod2.elevator.view.data.FloorSnapShot;
import com.pod2.elevator.view.data.SystemSnapShot;
import com.pod2.elevator.view.model.LogMessage;

/**
 * A discrete simulation of a building's elevator system, which can be advanced
 * one discrete time unit at at time.
 * 
 */
public class ActiveSimulation {

	private long currentQuantum;
	private long nextPassengerNumber;
	private ElevatorScheduler scheduler;
	private double distanceBeforeService;
	private long quantumsBeforeService;
	private boolean isRequestGenerationEnabled;
	private boolean isRunning;

	private final SimulationTemplate template;
	private final ResultsBuilder results;
	private final SimulationDisplay display;
	private final Thread simulationThread;

	private final Elevator[] elevators;
	private final Multimap<Integer, RequestInTransit> floorQueues;
	private final FloorRequestButton[] requestButtons;
	private final List<Event> delayedQueue;
	private final Multimap<Long, Event> eventQueue;
	private final RequestGenerator generator;

	public ActiveSimulation(SimulationTemplate template,
			ResultsBuilder results, SimulationDisplay display) {
		assert (template != null);
		assert (results != null);
		assert (display != null);

		currentQuantum = 0;
		nextPassengerNumber = 1;
		this.scheduler = template.getScheduler();
		this.distanceBeforeService = template.getDistanceBeforeService();
		this.quantumsBeforeService = template.getQuantumsBeforeService();
		isRunning = false;

		this.template = template;
		this.results = results;
		this.display = display;
		simulationThread = new Thread(new SimulationThread(this));

		/* Create the elevators. */
		elevators = new Elevator[template.getNumberElevators()];
		for (int n = 0; n < elevators.length; n++) {
			elevators[n] = new Elevator(this, n, template.getNumberFloors(),
					template.getElevatorCapacity(), template.getSpeed(),
					quantumsBeforeService, distanceBeforeService,
					template.getRestrictedFloors());
		}

		/* Create floor-specific objects. */
		floorQueues = HashMultimap.create();
		requestButtons = new FloorRequestButton[template.getNumberFloors()];
		for (int n = 0; n < requestButtons.length; n++) {
			requestButtons[n] = new FloorRequestButton();
		}

		/* Create and initialise event queues */
		delayedQueue = new LinkedList<Event>();
		eventQueue = HashMultimap.create();
		for (Event event : template.getEvents()) {
			enqueueEvent(event);
		}

		/* Initialise passenger generation */
		isRequestGenerationEnabled = template.isRequestGenerationOn();
		generator = new RequestGenerator(this);
	}

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

	public void enqueueEvent(Event event) {
		assert (event != null);
		eventQueue.put(event.getQuantum(), event);
	}

	public void enqueuePassenger(PassengerRequest request) {
		assert (request != null);
		enqueuePassenger(new RequestInTransit(nextPassengerNumber++, request));
	}

	public void setRequestGenerationEnabled(boolean isEnabled) {
		isRequestGenerationEnabled = isEnabled;
	}

	public void setScheduler(ElevatorScheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void setDistanceBeforeService(double distance) {
		this.distanceBeforeService = distance;
		for (Elevator elevator : elevators) {
			elevator.setDistanceBeforeService(distance);
		}
	}

	public void setQuantumsBeforeService(long quantums) {
		this.quantumsBeforeService = quantums;
		for (Elevator elevator : elevators) {
			elevator.setQuantumsBeforeService(quantums);
		}
	}

	public long getCurrentQuantum() {
		return currentQuantum;
	}

	public boolean getIsRunning() {
		return isRunning;
	}
	
	public Elevator[] getElevators() {
		return Arrays.copyOf(elevators, elevators.length);
	}

	public double getDistanceBeforeService() {
		return distanceBeforeService;
	}

	public long getQuantumsBeforeService() {
		return quantumsBeforeService;
	}

	public int getNumberFloors() {
		return template.getNumberFloors();
	}

	public int getPassengersWaiting(int floorNumber) {
		return floorQueues.get(floorNumber).size();
	}

	public FloorRequestButton[] getRequestButtons() {
		return Arrays.copyOf(requestButtons, requestButtons.length);
	}

	public ElevatorScheduler getScheduler() {
		return scheduler;
	}

	public boolean isRequestGenerationEnabled() {
		return isRequestGenerationEnabled;
	}

	void executeNextQuantum() {
		/*
		 * set-up the current quantum for execution.
		 */
		generateRequests();
		applyEvents();
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
		display.update(createSnapShot());
		results.logCompletedQuantum(this);

		/*
		 * move onto next quantum.
		 */
		incrementQuantum();
	}

	void onElevatorDoorsClosing(Elevator elevator) {
		/*
		 * Click the button for any passengers still waiting.
		 */
		int floor = (int) elevator.getPosition();
		requestButtons[floor].clearSelections();
		for (RequestInTransit request : floorQueues.get(floor)) {
			requestElevator(request);
		}
	}

	void onElevatorPutInService(Elevator elevator) {
		/* Rescue stranded passengers. */
		offloadPassengers(elevator, DeliveryStatus.Rescued);
	}

	private void applyEvents() {
		/* Apply events who couldn't apply at their appropriate time. */
		Iterator<Event> itr = delayedQueue.iterator();
		while (itr.hasNext()) {
			Event event = itr.next();
			if (event.canApplyNow(this)) {
				event.apply(this);
				results.logEvent(currentQuantum, event);
				itr.remove();
			}
		}

		/* Apply events for this quantum. */
		for (Event event : eventQueue.removeAll(currentQuantum)) {
			if (event.canApplyNow(this)) {
				event.apply(this);
				results.logEvent(currentQuantum, event);
			} else {
				delayedQueue.add(event);
			}
		}
	}

	private SystemSnapShot createSnapShot() {
		int numberFloors = template.getNumberFloors();
		FloorSnapShot[] floorSnapshots = new FloorSnapShot[numberFloors];
		for (int n = 0; n < numberFloors; n++) {
			floorSnapshots[n] = new FloorSnapShot(requestButtons[n],
					getPassengersWaiting(n));
		}

		int numberElevators = template.getNumberElevators();
		ElevatorSnapShot[] elevatorSnapshots = new ElevatorSnapShot[numberElevators];
		for (int n = 0; n < numberElevators; n++) {
			elevatorSnapshots[n] = elevators[n].createSnapshot();
		}

		Collection<LogMessage> messages = results.getLogEntries(currentQuantum);

		return new SystemSnapShot(currentQuantum, elevatorSnapshots,
				floorSnapshots, messages);
	}

	private void requestElevator(RequestInTransit request) {
		int onload = request.getOnloadFloor();
		int offload = request.getOffloadFloor();
		requestButtons[onload].click(currentQuantum, offload > onload);
	}

	private void enqueuePassenger(RequestInTransit request) {
		floorQueues.put(request.getOnloadFloor(), request);
		requestElevator(request);
		request.setDeliveryStatus(DeliveryStatus.Waiting);
		results.logRequestStateChange(currentQuantum, request);
	}

	private void executeQuantum() {
		for (Elevator elevator : elevators) {
			elevator.executeQuantum();
		}
	}

	private void generateRequests() {
		if (isRequestGenerationEnabled) {
			eventQueue.putAll(currentQuantum, generator.nextRequests());
		}
	}

	private void handlePassengerOffloads() {
		for (int n = 0; n < elevators.length; n++) {
			Elevator elevator = elevators[n];
			if (MotionStatus.DoorsOpen == elevator.getMotionStatus()) {
				offloadPassengers(elevator, DeliveryStatus.Delivered);
			}
		}
	}

	private void handlePassengerOnloads() {
		for (int n = 0; n < elevators.length; n++) {
			Elevator elevator = elevators[n];
			MotionStatus motionStatus = elevator.getMotionStatus();
			ServiceStatus serviceStatus = elevator.getServiceStatus();
			if (MotionStatus.DoorsOpen == motionStatus
					&& ServiceStatus.InService == serviceStatus) {
				onloadPassengers(elevator);
			}
		}
	}

	private void incrementQuantum() {
		currentQuantum++;
	}

	private void offloadPassengers(Elevator elevator, DeliveryStatus status) {
		for (RequestInTransit request : elevator.offloadPassengers()) {
			request.setOffloadQuantum(currentQuantum);
			request.setDeliveryStatus(status);
			results.logRequestStateChange(currentQuantum, request);
		}
	}

	private void onloadPassengers(Elevator elevator) {
		int floor = (int) elevator.getPosition();
		Collection<RequestInTransit> requests = floorQueues.removeAll(floor);
		requests = new LinkedList<RequestInTransit>(requests);
		Iterator<RequestInTransit> itr = requests.iterator();
		while (itr.hasNext()) {
			RequestInTransit request = itr.next();
			if (elevator.onloadPassenger(request)) {
				request.setDeliveryStatus(DeliveryStatus.InElevator);
				request.setElevatorNumber(elevator.getElevatorNumber());
				request.setOnloadQuantum(currentQuantum);
				results.logRequestStateChange(currentQuantum, request);
				itr.remove();
			} else {
				/* elevator capacity filled up. */
				floorQueues.putAll(floor, requests);
				break;
			}
		}
	}

}
