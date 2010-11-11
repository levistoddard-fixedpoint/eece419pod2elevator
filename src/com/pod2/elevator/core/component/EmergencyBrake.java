package com.pod2.elevator.core.component;

public class EmergencyBrake extends ElevatorComponent {

	private boolean brakeOn;

	public void brakeOn() {
		brakeOn = true;
	}

	public void brakeOff() {
		brakeOn = false;
	}

	public boolean isBrakeOn() {
		return brakeOn;
	}
	
	public boolean isFailed() {
		/* failsafe component */
		return false;
	}
	
}
