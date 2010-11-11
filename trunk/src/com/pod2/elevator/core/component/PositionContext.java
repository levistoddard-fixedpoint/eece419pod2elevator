package com.pod2.elevator.core.component;

public class PositionContext {

	private double currentPosition;

	public PositionContext(double currentPosition) {
		this.currentPosition = currentPosition;
	}

	public double getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(double currentPosition) {
		this.currentPosition = currentPosition;
	}

}
