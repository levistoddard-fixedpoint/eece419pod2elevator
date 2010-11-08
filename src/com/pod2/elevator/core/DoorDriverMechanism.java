package com.pod2.elevator.core;

public class DoorDriverMechanism extends ElevatorComponent {
	private DoorPositionContext doorPositionContext;
	
	public DoorDriverMechanism(DoorPositionContext doorPositionContext) {
		this.doorPositionContext = doorPositionContext;
	}
	
	public void moveClosed(double distance) {
		
	}
	
	public void moveOpen(double distance) {
		
	}
	
}
