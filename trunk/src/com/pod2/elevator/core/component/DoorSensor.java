package com.pod2.elevator.core.component;

/**
 * ElevatorComponent which measures the current position of an Elevator's doors.
 * 
 */
public class DoorSensor extends ElevatorComponent {

	private static final ComponentDetails details = new ComponentDetails(
			DoorSensor.class.getName(), "Door Sensor", true);

	private final DoorPositionContext doorPositionContext;
	private final double doorWidth;

	public DoorSensor(DoorPositionContext doorPositionContext, double doorWidth) {
		assert (doorPositionContext != null);
		assert (doorWidth > 0);
		this.doorPositionContext = doorPositionContext;
		this.doorWidth = doorWidth;
	}

	public boolean areDoorsClosed() throws ComponentFailedException {
		return checkPositionEquals(0.0);
	}

	public boolean areDoorsOpen() throws ComponentFailedException {
		return checkPositionEquals(doorWidth);
	}

	private boolean checkPositionEquals(double position)
			throws ComponentFailedException {
		if (isFailed()) {
			throw new ComponentFailedException("door sensor failed");
		}
		return doorPositionContext.getDistanceUntilClosed() == position;
	}

	@Override
	public String getKey() {
		return details.getKey();
	}

	static ComponentDetails getDetails() {
		return details;
	}

}
