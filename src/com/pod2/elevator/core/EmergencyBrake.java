package com.pod2.elevator.core;

public class EmergencyBrake extends ElevatorComponent {
	private boolean isBrakeOn;
	
	public void brakeOn() {
		this.isBrakeOn = true;
	}
	
	public void brakeOff() {
		this.isBrakeOn = false;
	}
	
	public boolean isBrakeOn() {
		return this.isBrakeOn;
	}
}
