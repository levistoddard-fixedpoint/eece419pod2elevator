package com.pod2.elevator.core;

public class DoorSensor extends ElevatorComponent {
	private DoorPositionContext doorPositionContext;
	
	public DoorSensor(DoorPositionContext doorPositionContext) {
		this.doorPositionContext = doorPositionContext;
	}
	
	public boolean isObstacleInWay() {
		return true;
	}
	
	public double getDistanceToClosed() {
		return 0.0;
	}
	
	public void operation() {
		
	}
}
