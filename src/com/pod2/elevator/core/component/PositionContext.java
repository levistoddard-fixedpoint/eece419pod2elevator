package com.pod2.elevator.core.component;

public class PositionContext {
	/**
	 * OVERVIEW: Stores the current position of an Elevator. Measured from ground-level,
	 * 		which corresponds to position 0.0.
	 */
	
	private double currentPosition;
	
	public PositionContext(double currentPosition) {
		/**
		 * 	REQUIRES: currentPosition >= 0
		 * 	MODIFIES: this.currentPosition
		 *  EFFECTS: Constructor for this class. Initialize the current
		 *  	position of the elevator.
		 */
		assert (currentPosition >= 0);
		this.currentPosition = currentPosition;
	}

	public double getCurrentPosition() {
		/**
		 * 	EFFECTS: Return the current position of the elevator
		 */
		return currentPosition;
	}

	public void setCurrentPosition(double currentPosition) {
		/**
		 * 	REQUIRES: currentPosition >= 0
		 * 	MODIFIES: this
		 * 	EFFECTS: Change the position of the elevator
		 */
		this.currentPosition = currentPosition;
	}

}
