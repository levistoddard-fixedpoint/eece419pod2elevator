package com.pod2.elevator.core.component;

/**
 * Stores the current position of an Elevator. Measured from ground-level, which
 * corresponds to position 0.0.
 * 
 */
public class PositionContext {

	private double currentPosition;

	public PositionContext(double currentPosition) {
		assert (currentPosition >= 0);
		this.currentPosition = currentPosition;
	}

	public double getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(double currentPosition) {
		this.currentPosition = currentPosition;
	}

}
