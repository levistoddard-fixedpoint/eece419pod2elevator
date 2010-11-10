package com.pod2.elevator.core.component;

public class DoorDriverMechanism extends ElevatorComponent {

	private DoorPositionContext doorPositionContext;
	private double doorWidth;

	public DoorDriverMechanism(DoorPositionContext doorPositionContext,
			double doorWidth) {
		this.doorPositionContext = doorPositionContext;
		this.doorWidth = doorWidth;
	}

	public void moveClosed(double distance) throws ComponentFailedException {
		if (isFailed()) {
			throw new ComponentFailedException("door driver failed");
		}
		double newPos = doorPositionContext.getDistanceUntilClosed() - distance;
		doorPositionContext.setDistanceUntilClosed(Math.max(0.0, newPos));
	}

	public void moveOpen(double distance) throws ComponentFailedException {
		if (isFailed()) {
			throw new ComponentFailedException("door driver failed");
		}
		double newPos = doorPositionContext.getDistanceUntilClosed() + distance;
		doorPositionContext.setDistanceUntilClosed(Math.min(doorWidth, newPos));
	}

}
