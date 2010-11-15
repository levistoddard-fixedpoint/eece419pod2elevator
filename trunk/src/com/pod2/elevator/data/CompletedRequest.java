package com.pod2.elevator.data;

import com.pod2.elevator.core.DeliveryStatus;

public class CompletedRequest {
	private int elevatorNumber;
	private int onloadFloor;
	private int offloadFloor;
	private long enterQuantum;
	private long onloadQuantum;
	private long offloadQuantum;
	private long timeConstraint;
	private DeliveryStatus deliveryStatus;
	
	public int getElevatorNumber() {
		return elevatorNumber;
	}
	public void setElevatorNumber(int elevatorNumber) {
		this.elevatorNumber = elevatorNumber;
	}
	public int getOnloadFloor() {
		return onloadFloor;
	}
	public void setOnloadFloor(int onloadFloor) {
		this.onloadFloor = onloadFloor;
	}
	public int getOffloadFloor() {
		return offloadFloor;
	}
	public void setOffloadFloor(int offloadFloor) {
		this.offloadFloor = offloadFloor;
	}
	public long getEnterQuantum() {
		return enterQuantum;
	}
	public void setEnterQuantum(long enterQuantum) {
		this.enterQuantum = enterQuantum;
	}
	public long getOnloadQuantum() {
		return onloadQuantum;
	}
	public void setOnloadQuantum(long onloadQuantum) {
		this.onloadQuantum = onloadQuantum;
	}
	public long getOffloadQuantum() {
		return offloadQuantum;
	}
	public void setOffloadQuantum(long offloadQuantum) {
		this.offloadQuantum = offloadQuantum;
	}
	public long getTimeConstraint() {
		return timeConstraint;
	}
	public void setTimeConstraint(long timeConstraint) {
		this.timeConstraint = timeConstraint;
	}
	public DeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
}
