package com.pod2.elevator.core.component;

public class DriverMechanism extends ElevatorComponent {

	private PositionContext positionContext;
	private double maxHeight;

	public DriverMechanism(PositionContext positionContext, double maxHeight) {
		this.positionContext = positionContext;
		this.maxHeight = maxHeight;
	}

	public void move(double distance) throws ComponentFailedException {
		if (isFailed()) {
			throw new ComponentFailedException("elevator driver failed");
		}
		double newPos = positionContext.getCurrentPosition() + distance;
		if (newPos > maxHeight) {
			throw new ComponentFailedException(
					"attempted to drive elevator too high");
		} else if (newPos < 0.0) {
			throw new ComponentFailedException(
					"attempted to drive elevator too low");
		}
		positionContext.setCurrentPosition(newPos);
	}

}
