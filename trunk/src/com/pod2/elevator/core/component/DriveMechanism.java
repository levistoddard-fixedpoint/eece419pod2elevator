package com.pod2.elevator.core.component;

public class DriveMechanism extends ElevatorComponent {
	/**
	 *  OVERVIEW: ElevatorComponent which moves an elevator up and down.
	 */

	private static final ComponentDetails details = new ComponentDetails(
			DriveMechanism.class.getName(), "Drive Mechanism", true);

	private final PositionContext positionContext;
	private final double maxHeight;

	// constructor
	public DriveMechanism(PositionContext positionContext, double maxHeight) {
		/**
		 * 	REQUIRES: positionContext != null && maxHeight > 0
		 * 	MODIFIES: this
		 * 	EFFECTS: Constructor of the class.  Initialize position of elevator and
		 * 		max height.
		 */
		assert (positionContext != null);
		assert (maxHeight > 0);
		this.positionContext = positionContext;
		this.maxHeight = maxHeight;
	}
	
	// methods
	public void move(double distance) throws ComponentFailedException {
		/**
		 * 	REQUIRES: distance != null
		 * 	MODIFIES: positionContext
		 * 	EFFECTS: Move the position of the elevator.  Throw ComponentFailedException
		 * 		if door driver fails.
		 */
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
