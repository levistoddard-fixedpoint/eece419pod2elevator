package com.pod2.elevator.core.component;

/**
 * ElevatorComponent which opens and closes the doors of an elevator.
 * 
 */
public class DoorDriveMechanism extends ElevatorComponent {

	private static final ComponentDetails details = new ComponentDetails(
			DoorDriveMechanism.class.getName(), "Door Drive Mechanism", true);

	private final DoorPositionContext doorPositionContext;
	private final double doorWidth;

	public DoorDriveMechanism(DoorPositionContext doorPositionContext,
			double doorWidth) {
		assert (doorPositionContext != null);
		assert (doorWidth > 0);
		this.doorPositionContext = doorPositionContext;
		this.doorWidth = doorWidth;
	}

	public void move(double distance) throws ComponentFailedException {
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
		return details.getKey();
	}

	static ComponentDetails getDetails() {
		return details;
	}

}
