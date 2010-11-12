package com.pod2.elevator.core.component;

/**
 * ElevatorComponent which measures the current position of an Elevator (i.e
 * distance from ground-level).
 * 
 */
public class PositionSensor extends ElevatorComponent {

	private static final ComponentDetails details = new ComponentDetails(
			PositionSensor.class.getName(), "Position Sensor", true);

	private final PositionContext positionContext;

	public PositionSensor(PositionContext positionContext) {
		assert (positionContext != null);
		this.positionContext = positionContext;
	}

	public double getPosition() throws ComponentFailedException {
		if (isFailed()) {
			throw new ComponentFailedException("position sensor failed");
		}
		return positionContext.getCurrentPosition();
	}

	@Override
	public String getKey() {
		return details.getKey();
	}

	static ComponentDetails getDetails() {
		return details;
	}

}
