package com.pod2.elevator.core.component;

public class DoorSensor extends ElevatorComponent {
	/**
	 *  OVERVIEW: ElevatorComponent which measures the current position of an Elevator's doors.
	 */
	
	private static final ComponentDetails details = new ComponentDetails(
			DoorSensor.class.getName(), "Door Sensor", true);

	private final DoorPositionContext doorPositionContext;
	private final double doorWidth;
	
	//constructor
	public DoorSensor(DoorPositionContext doorPositionContext, double doorWidth) {
		/**
		 * 	REQUIRES: doorPositionContext != null ** doorWidth > 0
		 * 	MODIFIES: this
		 * 	EFFECTS: Constructor of the class.  Initialize door position context
		 * 		and door width.
		 */
		assert (doorPositionContext != null);
		assert (doorWidth > 0);
		this.doorPositionContext = doorPositionContext;
		this.doorWidth = doorWidth;
	}
	
	// methods
	public boolean areDoorsClosed() throws ComponentFailedException {
		/**
		 * 	EFFECTS: Check if doors are fully closed.  Throw ComponentFailException if
		 * 		the component has failed.
		 */
		return checkPositionEquals(0.0);
	}

	public boolean areDoorsOpen() throws ComponentFailedException {
		/**
		 * 	EFFECTS: Check if doors are fully opened.  Throw ComponentFailException if
		 * 		the component has failed.
		 */
		return checkPositionEquals(doorWidth);
	}

	private boolean checkPositionEquals(double position)
			throws ComponentFailedException {
		/**
		 * 	REQUIRES: position >= 0
		 * 	EFFECTS: Check if doors position.  Throw ComponentFailException if
		 * 		the component has failed.
		 */
		if (isFailed()) {
			throw new ComponentFailedException("door sensor failed");
		}
		return doorPositionContext.getDistanceUntilClosed() == position;
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
