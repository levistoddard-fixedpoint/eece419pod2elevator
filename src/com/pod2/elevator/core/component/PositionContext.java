package com.pod2.elevator.core.component;

public class PositionContext {

	public double currentPosition;

	public PositionContext() {
		currentPosition = 0.0;
	}

	public double getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(double currentPosition) {
		this.currentPosition = currentPosition;
	}

}
