package com.pod2.elevator.core.component;

public class DoorDriveMechanism extends ElevatorComponent {
	/**
	 * 	OVERVIEW: ElevatorComponent which opens and closes the doors of an elevator.
	 */

	private static final ComponentDetails details = new ComponentDetails(
			DoorDriveMechanism.class.getName(), "Door Drive Mechanism", true);

	private final DoorPositionContext doorPositionContext;
	private final double doorWidth;
	
	//constructor
	public DoorDriveMechanism(DoorPositionContext doorPositionContext,
			double doorWidth) {
		/**
		 * 	REQUIRES: doorPositionContext != null && doorWidth > 0
		 * 	MODIFIES: this
		 * 	EFFECTS: Constructor of the class.  Initialize doorWidth and
		 * 		doorPositionContext.
		 */
		assert (doorPositionContext != null);
		assert (doorWidth > 0);
		this.doorPositionContext = doorPositionContext;
		this.doorWidth = doorWidth;
	}

	// methods
	public void move(double distance) throws ComponentFailedException {
		/**
		 * 	REQUIRES: distance != null
		 * 	MODIFIES: doorPositionContext
		 * 	EFFECTS: Move the position of the door.  Throw ComponentFailedException
		 * 		if door driver fails.
		 */
		if (isFailed()) {
			throw new ComponentFailedException("door driver failed");
		}
		double newPos = doorPositionContext.getDistanceUntilClosed() + distance;
		if (newPos > doorWidth) {
			throw new ComponentFailedException(
					"attempted to open door too wide");
		} else if (newPos < 0.0) {
			throw new ComponentFailedException(
					"attempted to keep closing a closed door");
		}
		doorPositionContext.setDistanceUntilClosed(newPos);
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
