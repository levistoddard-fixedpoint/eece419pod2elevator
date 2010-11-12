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
import com.pod2.elevator.scheduling.SchedulerData;
import com.pod2.elevator.view.ElevatorSnapShot;

/**
 * Represents a single elevator within an ActiveSimulation.
 * 
 */
public class Elevator {

	private static final double DOOR_WIDTH = 1.0;

	private final ActiveSimulation simulation;

	private final int elevatorNumber;
	private final int elevatorCapacity;
	private final double speed;

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

	public Elevator(ActiveSimulation simulation, int elevatorNumber,
			int numberFloors, int elevatorCapacity, double speed,
			Set<Integer> restrictedFloors) {
		assert (simulation != null);
		assert (numberFloors > 0);
		assert (speed > 0);
		assert (restrictedFloors != null);

		this.simulation = simulation;
		this.elevatorNumber = elevatorNumber;
		this.elevatorCapacity = elevatorCapacity;
		this.floorsOffLimit = restrictedFloors;
		this.speed = ((double) SimulationThread.QUANTUM_MILLIS / 1000) * speed;

		/* initialise components */
		components = new HashMap<String, ElevatorComponent>();
		positionContext = new PositionContext(0.0);
		doorPositionContext = new DoorPositionContext(DOOR_WIDTH);
		double maxHeight = (double) numberFloors;
		Collection<ElevatorComponent> componentList = createComponents(
				DOOR_WIDTH, maxHeight);
		for (ElevatorComponent component : componentList) {
			components.put(component.getKey(), component);
		}

		requestPanel = new FloorRequestPanel();
		requests = HashMultimap.create();
		motionStatus = MotionStatus.DoorsOpen;
		serviceStatus = ServiceStatus.InService;
		targetPosition = 0.0;
	}

	public void closeDoors() {
		motionStatus = MotionStatus.DoorsClosing;
		targetPosition = 0.0;
	}

	public void moveToFloor(int floor) {
		targetPosition = (double) floor;
		if (targetPosition < getPosition()) {
			motionStatus = MotionStatus.MovingDown;
		} else {
			motionStatus = MotionStatus.MovingUp;
		}
	}

	public void openDoors() {
		motionStatus = MotionStatus.DoorsOpening;
		targetPosition = DOOR_WIDTH;
	}

	public void putInService() {
		for (ElevatorComponent component : components.values()) {
			component.setFailed(false);
		}
		getEmergencyBrake().setIsEnabled(false);
		serviceStatus = ServiceStatus.InService;
		simulation.onElevatorPutInService(this);
	}

	public ElevatorComponent getComponent(String componentKey) {
		return components.get(componentKey);
	}

	public void putOutOfService() {
		getEmergencyBrake().setIsEnabled(true);
		serviceStatus = ServiceStatus.OutOfService;
	}

	public void setSchedulerData(SchedulerData data) {
		this.data = data;
	}

	public int getElevatorNumber() {
		return elevatorNumber;
	}

	public MotionStatus getMotionStatus() {
		return motionStatus;
	}

	public double getPosition() {
		return positionContext.getCurrentPosition();
	}

	public FloorRequestPanel getRequestPanel() {
		return requestPanel;
	}

	public SchedulerData getSchedulerData() {
		return data;
	}

	public ServiceStatus getServiceStatus() {
		return serviceStatus;
	}

	ElevatorSnapShot createSnapshot() {
		double currentPosition = positionContext.getCurrentPosition();
		int requestsInElevator = requests.values().size();
		return new ElevatorSnapShot(currentPosition, floorsOffLimit,
				requestsInElevator, elevatorCapacity, motionStatus,
				serviceStatus, components.values());
	}

	void executeQuantum() {
		if (!serviceStatus.equals(ServiceStatus.InService))
			return;
		try {
			double position = getPositionSensor().getPosition();
			if (motionStatus == MotionStatus.MovingDown
					|| motionStatus == MotionStatus.MovingUp) {
				double velocity = targetPosition < position ? -speed : speed;
				if (position == targetPosition) {
					motionStatus = MotionStatus.ReachedDestinationFloor;
				} else if (Math.abs(targetPosition - position) < speed) {
					getDriveMechanism().move(targetPosition - position);
				} else {
					getDriveMechanism().move(velocity);
				}
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
		return requests.removeAll((int) getPosition());
	}

	boolean onloadPassenger(RequestInTransit request) {
		if (requests.values().size() < elevatorCapacity) {
			int offloadFloor = request.getOffloadFloor();
			requests.put(offloadFloor, request);
			requestPanel.request(offloadFloor);
			return true;
		}
		return false;
	}

	private Collection<ElevatorComponent> createComponents(double doorWidth,
			double maxHeight) {
		DriveMechanism drive = new DriveMechanism(positionContext, maxHeight);
		PositionSensor sensor = new PositionSensor(positionContext);
		DoorDriveMechanism doorDrive = new DoorDriveMechanism(
				doorPositionContext, doorWidth);
		DoorSensor doorSensor = new DoorSensor(doorPositionContext, doorWidth);
		EmergencyBrake ebrake = new EmergencyBrake();
		return Arrays.asList(drive, sensor, doorDrive, doorSensor, ebrake);
	}

	private DoorDriveMechanism getDoorDriver() {
		String key = DoorDriveMechanism.class.getName();
		return (DoorDriveMechanism) components.get(key);
	}

	private DriveMechanism getDriveMechanism() {
		String key = DriveMechanism.class.getName();
		return (DriveMechanism) components.get(key);
	}

	private PositionSensor getPositionSensor() {
		String key = PositionSensor.class.getName();
		return (PositionSensor) components.get(key);
	}

	private DoorSensor getDoorSensor() {
		String key = DoorSensor.class.getName();
		return (DoorSensor) components.get(key);
	}

	private EmergencyBrake getEmergencyBrake() {
		String key = EmergencyBrake.class.getName();
		return (EmergencyBrake) components.get(key);
	}

}
