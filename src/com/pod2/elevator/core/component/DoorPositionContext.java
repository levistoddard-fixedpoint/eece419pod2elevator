package com.pod2.elevator.core.component;

/**
 * Stores the current position of an Elevator's doors.
 * 
 */
public class DoorPositionContext {

	private double distanceUntilClosed;

	public DoorPositionContext(double distanceUntilClosed) {
		setDistanceUntilClosed(distanceUntilClosed);
	}

	public double getDistanceUntilClosed() {
		return distanceUntilClosed;
	}

	public void setDistanceUntilClosed(double distanceUntilClosed) {
		assert (distanceUntilClosed >= 0);
		this.distanceUntilClosed = distanceUntilClosed;
	}

}
