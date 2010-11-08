package com.pod2.elevator.core;

public class PositionSensor extends ElevatorComponent {
	
	private PositionContext positionContext;
	
	public PositionSensor(PositionContext positionContext) {
		this.positionContext = positionContext;
	}
	
	public double getPosition() {
		return this.positionContext.currentPosition;
	}
	
}
