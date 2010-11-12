package com.pod2.elevator.core.component;

/**
 * ElevatorComponent which moves an elevator up and down.
 * 
 */
public class DriveMechanism extends ElevatorComponent {

	private static final ComponentDetails details = new ComponentDetails(
			DriveMechanism.class.getName(), "Drive Mechanism", true);

	private final PositionContext positionContext;
	private final double maxHeight;

	public DriveMechanism(PositionContext positionContext, double maxHeight) {
		assert (positionContext != null);
		assert (maxHeight > 0);
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

	@Override
	public String getKey() {
		return details.getKey();
	}

	static ComponentDetails getDetails() {
		return details;
	}

}
