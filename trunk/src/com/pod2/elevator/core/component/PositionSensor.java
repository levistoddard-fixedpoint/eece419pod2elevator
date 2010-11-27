package com.pod2.elevator.core.component;

public class PositionSensor extends ElevatorComponent {
	/**
	 * OVERVIEW: ElevatorComponent which measures the current position of an
	 * 		Elevator (i.e distance from ground-level).
	 */

	private static final ComponentDetails details = new ComponentDetails(
			PositionSensor.class.getName(), "Position Sensor", true);

	private final PositionContext positionContext;

	public PositionSensor(PositionContext positionContext) {
		/**
		 * 	REQUIRES: positionContext != null
		 * 	MODIFIES: this
		 * 	EFFECTS: Constructor for the class.  Initializes position context.
		 */
		assert (positionContext != null);
		this.positionContext = positionContext;
	}

	public double getPosition() throws ComponentFailedException {
		/**
		 * 	EFFECTS: Return the position of the elevator.  Throw ComponentFailedException
		 * 		if position sensor has failed.
		 */
		if (isFailed()) {
			throw new ComponentFailedException("position sensor failed");
		}
		return positionContext.getCurrentPosition();
	}

	@Override
	public String getKey() {
		/**
		 * 	EFFECTS: Return the name of component from component detail
		 */
		return details.getKey();
	}

	static ComponentDetails getDetails() {
		/**
		 * 	EFFECTS: Return ComponentDetails object containing door drive component details
		 */
		return details;
	}

}
