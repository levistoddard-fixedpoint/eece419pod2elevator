package com.pod2.elevator.core.component;

public class DoorPositionContext {

	private double distanceUntilClosed;

	public DoorPositionContext(double distanceUntilClosed) {
		this.distanceUntilClosed = distanceUntilClosed;
	}

	public double getDistanceUntilClosed() {
		return distanceUntilClosed;
	}

	public void setDistanceUntilClosed(double distanceUntilClosed) {
		this.distanceUntilClosed = distanceUntilClosed;
	}

}
