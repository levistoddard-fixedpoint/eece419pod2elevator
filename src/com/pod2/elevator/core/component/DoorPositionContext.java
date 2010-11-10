package com.pod2.elevator.core.component;

public class DoorPositionContext {
	
	private double distanceUntilClosed;

	public DoorPositionContext() {
		distanceUntilClosed = 0.0;
	}

	public double getDistanceUntilClosed() {
		return distanceUntilClosed;
	}

	public void setDistanceUntilClosed(double distanceUntilClosed) {
		this.distanceUntilClosed = distanceUntilClosed;
	}
	
}
