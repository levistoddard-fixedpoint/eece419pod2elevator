package com.pod2.elevator.core.component;

public class PositionSensor extends ElevatorComponent {

	private PositionContext positionContext;

	public PositionSensor(PositionContext positionContext) {
		this.positionContext = positionContext;
	}

	public double getPosition() throws ComponentFailedException {
		if (isFailed()) {
			throw new ComponentFailedException("position sensor failed");
		}
		return positionContext.currentPosition;
	}

}
