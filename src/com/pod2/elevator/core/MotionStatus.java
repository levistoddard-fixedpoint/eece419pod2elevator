package com.pod2.elevator.core;


public enum MotionStatus {
	/**
	 * OVERVIEW: Indicates the current motion of an Elevator.
	 */
	
	/**
	 * Elevator is currently travelling to a lower floor number.
	 */
	MovingDown,

	/**
	 * Elevator is currently travelling to a higher floor number.
	 */
	MovingUp,

	/**
	 * Elevator is currently at a floor with it's doors open.
	 */
	DoorsOpen,

	/**
	 * Elevator is currently at a floor with it's doors closed.
	 */
	DoorsClosed,

	/**
	 * Elevator is currently at a floor with it's doors opening.
	 */
	DoorsOpening,

	/**
	 * Elevator is currently at a floor with it's doors closing.
	 */
	DoorsClosing,

	/**
	 * Elevator is currently at a floor, after transitioning out of the MovingUp
	 * or MovingDown state. Elevator still has it's doors closed.
	 */
	ReachedDestinationFloor

}
