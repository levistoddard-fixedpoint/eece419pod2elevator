package com.pod2.elevator.core.component;

public class DoorDriverMechanism extends ElevatorComponent {

	private DoorPositionContext doorPositionContext;
	private double doorWidth;

	public DoorDriverMechanism(DoorPositionContext doorPositionContext,
			double doorWidth) {
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
					"door driver attempted to open door too wide");
		} else if (newPos < 0.0) {
			throw new ComponentFailedException(
					"door driver attempted to keep closing a closed door");
		}
		doorPositionContext.setDistanceUntilClosed(newPos);
	}

}
