package com.pod2.elevator.core;

/**
 * Classifies a RequestInTransit by the current state it is in.
 * 
 */
public enum DeliveryStatus {

	/**
	 * Passenger has entered the ActiveSimulation, and is currently waiting to
	 * get on an Elevator.
	 */
	Waiting,

	/**
	 * Passenger has entered an Elevator, and is waiting for the Elevator to
	 * arrive it the requested floor.
	 */
	InElevator,

	/**
	 * Passenger has exited the elevator onto the requested floor.
	 */
	Delivered,

	/**
	 * Elevator the passenger was riding on failed before it reached the
	 * requested destination. Passenger was rescued on the quantum the elevator
	 * was put back into service.
	 */
	Rescued;

}
