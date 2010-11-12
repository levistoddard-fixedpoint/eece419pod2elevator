package com.pod2.elevator.core;

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
import com.pod2.elevator.core.events.RequestInTransit;
import com.pod2.elevator.scheduling.SchedulerData;
import com.pod2.elevator.view.ElevatorSnapShot;

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
		this.simulation = simulation;
		this.elevatorNumber = elevatorNumber;
		this.elevatorCapacity = elevatorCapacity;
		this.floorsOffLimit = restrictedFloors;
		this.speed = ((double) SimulationThread.QUANTUM_MILLIS / 1000) * speed;

		/* initialise components */
		components = new HashMap<String, ElevatorComponent>();
		positionContext = new PositionContext(0.0);
		double maxHeight = (double) numberFloors;
		DriveMechanism driver = new DriveMechanism(positionContext, maxHeight);
		PositionSensor position = new PositionSensor(positionContext);
		components.put(DriveMechanism.class.getName(), driver);
		components.put(PositionSensor.class.getName(), position);
		doorPositionContext = new DoorPositionContext(DOOR_WIDTH);
		DoorDriveMechanism doorDriver = new DoorDriveMechanism(
				doorPositionContext, DOOR_WIDTH);
		DoorSensor doorSensor = new DoorSensor(doorPositionContext, DOOR_WIDTH);
		components.put(DoorDriveMechanism.class.getName(), doorDriver);
		components.put(DoorSensor.class.getName(), doorSensor);
		components.put(EmergencyBrake.class.getName(), new EmergencyBrake());

		requestPanel = new FloorRequestPanel();
		requests = HashMultimap.create();
		motionStatus = MotionStatus.DoorsOpen;
		serviceStatus = ServiceStatus.InService;
		targetPosition = 0.0;
	}

	public ElevatorComponent getComponent(String componentKey) {
		return components.get(componentKey);
	}

	public void putInService() {
		for (ElevatorComponent component : components.values()) {
			component.setFailed(false);
		}
		getEmergencyBrake().setIsEnabled(false);
		serviceStatus = ServiceStatus.InService;
		simulation.onElevatorPutInService(this);
	}

	public void putOutOfService() {
		getEmergencyBrake().setIsEnabled(true);
		serviceStatus = ServiceStatus.OutOfService;
	}

	public int getElevatorNumber() {
		return elevatorNumber;
	}

	public MotionStatus getMotionStatus() {
		return motionStatus;
	}

	public ServiceStatus getServiceStatus() {
		return serviceStatus;
	}

	public double getPosition() {
		return positionContext.getCurrentPosition();
	}

	public FloorRequestPanel getRequestPanel() {
		return requestPanel;
	}

	public void setSchedulerData(SchedulerData data) {
		this.data = data;
	}

	public SchedulerData getSchedulerData() {
		return data;
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

	public void closeDoors() {
		motionStatus = MotionStatus.DoorsClosing;
		targetPosition = 0.0;
	}

	void executeQuantum() {
		if (!serviceStatus.equals(ServiceStatus.InService))
			return;
		try {
			if (motionStatus.equals(MotionStatus.MovingDown)
					|| motionStatus.equals(MotionStatus.MovingUp)) {
				double currPos = getPositionSensor().getPosition();
				double velocity = targetPosition < currPos ? -speed : speed;
				if (currPos == targetPosition) {
					motionStatus = MotionStatus.ReachedDestinationFloor;
				} else if (Math.abs(targetPosition - currPos) < speed) {
					getDriveMechanism().move(targetPosition - currPos);
				} else {
					getDriveMechanism().move(velocity);
				}
			} else if (motionStatus.equals(MotionStatus.DoorsClosing)) {
				if (getDoorSensor().areDoorsClosed()) {
					motionStatus = MotionStatus.DoorsClosed;
					simulation.onElevatorDoorsClosed(this);
				} else {
					getDoorDriver().move(-DOOR_WIDTH);
				}
			} else if (motionStatus.equals(MotionStatus.DoorsOpening)) {
				if (getDoorSensor().areDoorsOpen()) {
					motionStatus = MotionStatus.DoorsOpen;
					requestPanel.clearRequest((int) getPosition());
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

	ElevatorSnapShot createSnapshot() {
		double currentPosition = positionContext.getCurrentPosition();
		int requestsInElevator = requests.values().size();
		return new ElevatorSnapShot(currentPosition, floorsOffLimit,
				requestsInElevator, elevatorCapacity, motionStatus,
				serviceStatus, components.values());
	}

	private DoorDriveMechanism getDoorDriver() {
		return (DoorDriveMechanism) components.get(DoorDriveMechanism.class
				.getName());
	}

	private DriveMechanism getDriveMechanism() {
		return (DriveMechanism) components.get(DriveMechanism.class.getName());
	}

	private PositionSensor getPositionSensor() {
		return (PositionSensor) components.get(PositionSensor.class.getName());
	}

	private DoorSensor getDoorSensor() {
		return (DoorSensor) components.get(DoorSensor.class.getName());
	}

	private EmergencyBrake getEmergencyBrake() {
		return (EmergencyBrake) components.get(EmergencyBrake.class.getName());
	}

}
