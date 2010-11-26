package com.pod2.elevator.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pod2.elevator.core.component.ComponentFailedException;
import com.pod2.elevator.core.component.DoorDriveMechanism;
import com.pod2.elevator.core.component.DoorPositionContext;
import com.pod2.elevator.core.component.DoorSensor;
import com.pod2.elevator.core.component.DriveMechanism;
import com.pod2.elevator.core.component.ElevatorComponent;
import com.pod2.elevator.core.component.EmergencyBrake;
import com.pod2.elevator.core.component.PositionContext;
import com.pod2.elevator.core.component.PositionSensor;
import com.pod2.elevator.core.events.Event;
import com.pod2.elevator.core.events.EventFactory;
import com.pod2.elevator.core.events.EventSource;
import com.pod2.elevator.scheduling.SchedulerData;
import com.pod2.elevator.view.data.ElevatorSnapShot;

public class Elevator {
	/**
	 *  OVERVIEW: Represents a single elevator within an ActiveSimulation.
	 */
	private static final double DOOR_WIDTH = 1.0;
	private static final String SERVICE_DUE_TO_DISTANCE = "Maximum distance before servicing reached.";
	private static final String SERVICE_DUE_TO_TIME = "Maximum operation time before servicing reached.";

	private final ActiveSimulation simulation;

	private final int elevatorNumber;
	private final int elevatorCapacity;
	private final double speed; /* floors per quantum */

	private final FloorRequestPanel requestPanel;
	private final Set<Integer> floorsOffLimit;
	private final Multimap<Integer, RequestInTransit> requests;

	private final Map<String, ElevatorComponent> components;
	private final PositionContext positionContext;
	private final DoorPositionContext doorPositionContext;

	private MotionStatus motionStatus;
	private ServiceStatus serviceStatus;
	private double targetPosition;
	private SchedulerData data = null;

	private long quantumsBeforeService;
	private long cumulativeQuantumsInService;
	private double distanceBeforeService;
	private double cumulativeDistanceInService;
	private boolean isPuttingOutOfService = false;
	
	//constructor
	public Elevator(ActiveSimulation simulation, int elevatorNumber, int numberFloors,
			int elevatorCapacity, double speed, long quantumsBeforeService,
			double distanceBeforeService, Set<Integer> restrictedFloors) {
		/**
		 * 	MODIFIES: this
		 * 	EFFECTS: Constructor for this class.  Throws AssertionException if simulation or
		 * 		restrictedFloors is null.  Throws AssertionException if numberFloors or speed
		 * 		is less than or equal to 0.  Initialize simulation, elevatorNumber,
		 * 		elevatorCapacity, floorsOffLimit, quantumsBeforeService, distanceBeforeService,
		 * 		and speed.  Set cumulativeQuantumsInService and cumulativeDistanceInService to 0.
		 * 		Initialize and create each component of the elevator.  Enter each component to
		 * 		components.  Initialize requestPanel with new a FloorRequestPanel object.  Set
		 * 		elevator motionStatus to "DoorsOpen" and serviceStatus to "InService".
		 * 		targetPosition is set to 0.0.
		 */
		
		assert (simulation != null);
		assert (numberFloors > 0);
		assert (speed > 0);
		assert (restrictedFloors != null);
		
		this.simulation = simulation;
		this.elevatorNumber = elevatorNumber;
		this.elevatorCapacity = elevatorCapacity;
		this.floorsOffLimit = restrictedFloors;
		this.quantumsBeforeService = quantumsBeforeService;
		this.cumulativeQuantumsInService = 0;
		this.distanceBeforeService = distanceBeforeService;
		this.cumulativeDistanceInService = 0.0;
		this.speed = speed;

		/* initialise components */
		components = new HashMap<String, ElevatorComponent>();
		positionContext = new PositionContext(0.0);
		doorPositionContext = new DoorPositionContext(DOOR_WIDTH);
		double maxHeight = (double) numberFloors;
		Collection<ElevatorComponent> componentList = createComponents(DOOR_WIDTH, maxHeight);
		for (ElevatorComponent component : componentList) {
			components.put(component.getKey(), component);
		}

		requestPanel = new FloorRequestPanel();
		requests = HashMultimap.create();
		motionStatus = MotionStatus.DoorsOpen;
		serviceStatus = ServiceStatus.InService;
		targetPosition = 0.0;
	}

	// methods
	public void closeDoors() {
		/**
		 * 	MODIFIES: motionStatus, targetPosition
		 * 	EFFECTS: Changes motionStatus to "DoorsClosing" and targetPosition to 0.0
		 */
		motionStatus = MotionStatus.DoorsClosing;
		targetPosition = 0.0;
	}

	public void moveToFloor(int floor) {
		/**
		 * 	REQUIRES: floor != null && floor < numberFloors
		 *  MODIFIES: targetPosition, motionStatus
		 *  EFFECTS: Set targetPosition to floor.  If targetPosition is lower than
		 *  	current position, set motionStatus to "MovingDown".  Else, set
		 *  	motionStatus to "MovingUp".
		 */
		targetPosition = (double) floor;
		if (targetPosition < getPosition()) {
			motionStatus = MotionStatus.MovingDown;
		} else {
			motionStatus = MotionStatus.MovingUp;
		}
	}

	public void openDoors() {
		/**
		 * 	MODIFIES: motionStatus, targetPosition
		 * 	EFFECTS: set motionStatus to "DoorsOpening" and targetPosition to DOOR_WIDTH
		 */
		motionStatus = MotionStatus.DoorsOpening;
		targetPosition = DOOR_WIDTH;
	}

	public void putInService() {
		/**
		 * 	MODIFIES: this
		 * 	EFFECTS: For each ElevatorComponent in components, set Failed to false.  Reset
		 * 		cumulativeQuantumsInService and cumulativeDistanceInService to zero.  Disable
		 * 		emergencyBrake component.  Set serviceStatus to "InService".  Call method to
		 * 		place elevator back in service on the simulator
		 */
		for (ElevatorComponent component : components.values()) {
			component.setFailed(false);
		}
		cumulativeQuantumsInService = 0;
		cumulativeDistanceInService = 0.0;
		getEmergencyBrake().setIsEnabled(false);
		serviceStatus = ServiceStatus.InService;
		simulation.onElevatorPutInService(this);
	}

	public ElevatorComponent getComponent(String componentKey) {
		/**
		 * 	REQUIRES: componentKey != null
		 * 	EFFECTS: returns the ElevatorComponent with the specified name
		 */
		return components.get(componentKey);
	}

	public void putOutOfService() {
		/**
		 * 	MODIFIES: components, serviceStatus
		 * 	EFFECTS: Enable elevator emergency brake.  Set serviceStatus to "OutOfService".
		 */
		getEmergencyBrake().setIsEnabled(true);
		serviceStatus = ServiceStatus.OutOfService;
	}

	public void setDistanceBeforeService(double distance) {
		/**
		 * 	REQUIRES: distance != null && distance >= 0
		 * 	MODIFIES: distanceBeforeService
		 * 	EFFECTS: Change the distance before service.  Call method to check if service
		 * 		distance has been reached or not.
		 */
		this.distanceBeforeService = distance;
		checkServiceDistanceReached();
	}

	public void setQuantumsBeforeService(long quantums) {
		/**
		 * 	REQUIRES: quantums != null && quantums >= 0
		 * 	MODIFIES: quantumsBeforeService
		 *  EFFECTS: Change the number of quantum before service.  Call method to check
		 *  	if quantum for servicing has been reached or not.
		 */
		this.quantumsBeforeService = quantums;
		checkServiceQuantumsReached();
	}

	public void setSchedulerData(SchedulerData data) {
		/**
		 * 	REQUIRES: data != null
		 * 	MODIFIES: data
		 * 	EFFECTS: Changes the data of the scheduler algorithm
		 */
		this.data = data;
	}

	public int getElevatorNumber() {
		/**
		 * 	EFFECTS: returns the elevator number
		 */
		return elevatorNumber;
	}

	public MotionStatus getMotionStatus() {
		/**
		 * 	EFFECTS: returns the motion status of elevator
		 */
		return motionStatus;
	}

	public double getPosition() {
		/**
		 * 	EFFECTS: returns the current position of the elevator
		 */
		return positionContext.getCurrentPosition();
	}

	public FloorRequestPanel getRequestPanel() {
		/**
		 * 	EFFECTS: returns the FloorRequestPanel of the elevator
		 */
		return requestPanel;
	}

	public SchedulerData getSchedulerData() {
		/**
		 * 	EFFECTS: returns the data of scheduling algorithm
		 */
		return data;
	}

	public ServiceStatus getServiceStatus() {
		/**
		 * 	EFFECTS: returns the service status of elevator
		 */
		return serviceStatus;
	}

	ElevatorSnapShot createSnapshot() {
		/**
		 * 	EFFECTS: returns an ElevatorSnapShot object with current position of
		 * 		elevator, off limit floors, passengers in elevator, elevator capacity
		 * 		motion status, service status, and components in elevator
		 */
		double currentPosition = positionContext.getCurrentPosition();
		int requestsInElevator = requests.values().size();
		return new ElevatorSnapShot(currentPosition, floorsOffLimit, requestsInElevator,
				elevatorCapacity, motionStatus, serviceStatus, components.values());
	}

	void executeQuantum() {
		/**
		 * 	MODIFIES: this
		 * 	EFFECTS: Execute the current time quantum events.  If service status is
		 * 		"OutOfService", then do nothing and return.  If elevator service status is
		 * 		"InService", increment cumulativeQuantumsInService by 1.  If motion status
		 * 		is "MovingDown" or "MovingUp", move elevator with driveMechanism component.
		 * 		If motionStatus is "DoorsClosing", change motionStatus to "DoorsClosed".
		 * 		If motionStatus is "DoorsOpening", clear the request for the current floor
		 * 		in elevator and change motionStatus to "DoorsOpen".  Throw
		 * 		ComponentFailedException if any component has failed and apply emergency
		 * 		brake.
		 */
		if (!serviceStatus.equals(ServiceStatus.InService))
			return;

		/* see if elevator has reached it's service time limit. */
		cumulativeQuantumsInService += 1;
		checkServiceQuantumsReached();

		try {
			PositionSensor sensor = getPositionSensor();
			double position = sensor.getPosition();
			if (motionStatus == MotionStatus.MovingDown || motionStatus == MotionStatus.MovingUp) {
				/* move elevator to it's next position. */
				double velocity = targetPosition < position ? -speed : speed;
				if (position == targetPosition) {
					motionStatus = MotionStatus.ReachedDestinationFloor;
				} else if (Math.abs(targetPosition - position) < speed) {
					getDriveMechanism().move(targetPosition - position);
				} else {
					getDriveMechanism().move(velocity);
				}

				/* see if elevator has reached it's service distance limit. */
				double newPosition = sensor.getPosition();
				cumulativeDistanceInService += Math.abs(newPosition - position);
				checkServiceDistanceReached();

			} else if (motionStatus == MotionStatus.DoorsClosing) {
				if (getDoorSensor().areDoorsClosed()) {
					motionStatus = MotionStatus.DoorsClosed;
				} else {
					simulation.onElevatorDoorsClosing(this);
					getDoorDriver().move(-DOOR_WIDTH);
				}
			} else if (motionStatus == MotionStatus.DoorsOpening) {
				if (getDoorSensor().areDoorsOpen()) {
					requestPanel.clearRequest((int) getPosition());
					motionStatus = MotionStatus.DoorsOpen;
				} else {
					getDoorDriver().move(DOOR_WIDTH);
				}
			}
		} catch (ComponentFailedException e) {
			serviceStatus = ServiceStatus.Failed;
			getEmergencyBrake().setIsEnabled(true);
		}
	}

	Collection<RequestInTransit> offloadPassengers() {
		/**
		 * 	MODIFIES: requests
		 * 	EFFECTS: returns passengers in current position and removes them from requests.
		 */
		return requests.removeAll((int) getPosition());
	}

	boolean onloadPassenger(RequestInTransit request) {
		/**
		 * 	MODIFIES: requests, requestPanel
		 * 	EFFECTS: Insert a passenger (request) into elevator unless elevator is full.
		 * 		If full, return false.  Return true when passenger has been added to the list
		 * 		of requests.
		 */
		if (requests.values().size() < elevatorCapacity) {
			int offloadFloor = request.getOffloadFloor();
			requests.put(offloadFloor, request);
			requestPanel.request(offloadFloor, simulation.getCurrentQuantum());
			return true;
		}
		return false;
	}

	private Collection<ElevatorComponent> createComponents(double doorWidth, double maxHeight) {
		/**
		 * 	REQUIRES: doorWidth != null && maxHeight != null
		 *  EFFECTS: returns all elevator components with specified doorWidth and maxHeight.
		 */
		DriveMechanism drive = new DriveMechanism(positionContext, maxHeight);
		PositionSensor sensor = new PositionSensor(positionContext);
		DoorDriveMechanism doorDrive = new DoorDriveMechanism(doorPositionContext, doorWidth);
		DoorSensor doorSensor = new DoorSensor(doorPositionContext, doorWidth);
		EmergencyBrake ebrake = new EmergencyBrake();
		return Arrays.asList(drive, sensor, doorDrive, doorSensor, ebrake);
	}

	private DoorDriveMechanism getDoorDriver() {
		/**
		 * 	EFFECTS: returns the door drive mechanism component
		 */
		String key = DoorDriveMechanism.class.getName();
		return (DoorDriveMechanism) components.get(key);
	}

	private DriveMechanism getDriveMechanism() {
		/**
		 * 	EFFECTS: returns the drive mechanism of elevator
		 */
		String key = DriveMechanism.class.getName();
		return (DriveMechanism) components.get(key);
	}

	private PositionSensor getPositionSensor() {
		/**
		 * 	EFFECTS: returns the position sensor component of elevator
		 */
		String key = PositionSensor.class.getName();
		return (PositionSensor) components.get(key);
	}

	private DoorSensor getDoorSensor() {
		/**
		 * 	EFFECTS: returns the door sensors of elevator
		 */
		String key = DoorSensor.class.getName();
		return (DoorSensor) components.get(key);
	}

	private EmergencyBrake getEmergencyBrake() {
		/**
		 * 	EFFECTS: returns the emergency brake component of elevator
		 */
		String key = EmergencyBrake.class.getName();
		return (EmergencyBrake) components.get(key);
	}

	private void checkServiceDistanceReached() {
		/**
		 * 	EFFECTS: Call method to put elevator out of service once service distance has been reached.
		 */
		checkPutOutOfService(distanceBeforeService >= 0
				&& cumulativeDistanceInService >= distanceBeforeService, SERVICE_DUE_TO_DISTANCE);
	}

	private void checkServiceQuantumsReached() {
		/**
		 * 	EFFECTS: Call method to put elevator out of service once service quantum has been reached
		 */
		checkPutOutOfService(quantumsBeforeService >= 0
				&& cumulativeQuantumsInService >= quantumsBeforeService, SERVICE_DUE_TO_TIME);
	}

	private void checkPutOutOfService(boolean condition, String reason) {
		/**
		 * 	REQUIRES: condition != null && reason != null
		 * 	MODIFIES: simulation, isPuttingOutOfService
		 * 	EFFECTS: If condition has been met to put elevator out of service, create an out of service
		 * 		event with reason.  Insert the event to simulation and set out of service flag to true.
		 * 		If condition not met, return without changes.
		 */
		if (!isPuttingOutOfService && condition) {
			long quantum = simulation.getCurrentQuantum() + 1;
			Event serviceEvent = EventFactory.createServiceEvent(EventSource.Generated, quantum,
					elevatorNumber, false, reason);
			simulation.enqueueEvent(serviceEvent);
			isPuttingOutOfService = true;
		}
	}

}
