package com.pod2.elevator.core;

/**
 * Indicates the current status of an Elevator.
 * 
 */
public enum ServiceStatus {

	/**
	 * Elevator has been put out of service, and is currently at a floor in the
	 * DoorsOpen MotionStatus state.
	 */
	OutOfService,

	/**
	 * Elevator is currently in service, and could be in any MotionStatus state.
	 */
	InService,

	/**
	 * Some ElevatorComponent on the Elevator has failed, so it cannot currently
	 * move. It could be in any MotionStatus state.
	 */
	Failed

}
