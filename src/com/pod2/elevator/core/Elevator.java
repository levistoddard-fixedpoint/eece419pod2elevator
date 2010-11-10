package com.pod2.elevator.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Multimap;
import com.pod2.elevator.core.component.DoorDriverMechanism;
import com.pod2.elevator.core.component.DoorPositionContext;
import com.pod2.elevator.core.component.DoorSensor;
import com.pod2.elevator.core.component.DriverMechanism;
import com.pod2.elevator.core.component.EmergencyBrake;
import com.pod2.elevator.core.component.PositionContext;
import com.pod2.elevator.core.component.PositionSensor;
import com.pod2.elevator.data.SimulationResultsBuilder;

public class Elevator {

	private Set<Integer> requestedFloors;
	private Set<Integer> floorsOffLimit;
	Multimap<Integer, RequestInTransit> requests;
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

	private int elevatorNumber;

	public Elevator(int elevatorNumber, int requestCapacity,
			Set<Integer> restrictedFloors) {
		this.elevatorCapacity = requestCapacity;
		this.floorsOffLimit = restrictedFloors;
		this.elevatorNumber = elevatorNumber;
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

	public int getElevatorNumber() {
		return elevatorNumber;
	}
	
	public Collection<RequestInTransit> offloadPassengers() {
		/* TODO implement this*/
		return null;
	}
	
	/*
	 * Returns true if the passenger fit into the elevator.
	 */
	public boolean onloadPassenger(RequestInTransit request) {
		/* TODO implement this */
		return true;
	}
	
}
