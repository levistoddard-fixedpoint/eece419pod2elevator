package com.pod2.elevator.core.component;

public class DriverMechanism extends ElevatorComponent {

	private PositionContext positionContext;
	private double maxHeight;

	public DriverMechanism(PositionContext positionContext, double maxHeight) {
		this.positionContext = positionContext;
		this.maxHeight = maxHeight;
	}

	public void moveUp(double distance) throws ComponentFailedException {
		if (isFailed()) {
			throw new ComponentFailedException("elevator driver failed");
		}
		double newPos = positionContext.getCurrentPosition() + distance;
		if (newPos > maxHeight) {
			throw new ComponentFailedException(
					"elevator driver reached height limit");
		}
		positionContext.setCurrentPosition(newPos);
	}

	public void moveDown(double distance) throws ComponentFailedException {
		if (isFailed()) {
			throw new ComponentFailedException("elevator driver failed");
		}
		double newPos = positionContext.getCurrentPosition() - distance;
		if (newPos < 0.0) {
			throw new ComponentFailedException(
					"elevator driver reached ground level limit");
		}
		positionContext.setCurrentPosition(newPos);
	}
}
