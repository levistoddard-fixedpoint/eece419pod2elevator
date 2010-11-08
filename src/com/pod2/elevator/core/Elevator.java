package com.pod2.elevator.core;

import java.util.Iterator;
import java.util.Set;

import com.pod2.elevator.data.SimulationResultsBuilder;

public class Elevator {
	private Set<Integer> requestedFloors;
	private Set<Integer> floorsOffLimit;
	private Multimap<RequestInTransit> requests;
	private int elevatorCapacity;
	private DriverMechanism driverMechanism;
	private EmergencyBrake emergencyBrake;
	private PositionSensor positionSensor;
	private DoorDriverMechanism doorDriverMechanism;
	private DoorSensor doorSensor;
	private PositionContext positionContext;
	private DoorPositionContext doorPositionContext;
	private SimulationResultsBuilder simulationResultsBuilder;
	private MotionStatus motionStatus;
	private ServiceStatus serviceStatus;
	private Iterator nextPositions;
	
	public Elevator(int requestCapacity, Set<Integer> restrictedFloors) {
		this.elevatorCapacity = requestCapacity;
		this.floorsOffLimit = restrictedFloors;
	}
	
	public double getPosition() {
		return this.positionContext.currentPosition;
	}
	
	public void moveToFloor(int floor) {
		this.positionContext.currentPosition = floor;
	}
	
	public void closeDoors() {
		
	}
	
	public void openDoors() {
		
	}
	
	protected void executeQuantum() {
		
	}
	
	protected MotionStatus getMotionStatus() {
		return this.motionStatus;
	}
	
	protected ServiceStatus getServiceStatus() {
		return this.serviceStatus;
	}
}
