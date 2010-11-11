package com.pod2.elevator.core.component;

public class DoorSensor extends ElevatorComponent {

	private DoorPositionContext doorPositionContext;
	private double doorWidth;

	public DoorSensor(DoorPositionContext doorPositionContext, double doorWidth) {
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

}
