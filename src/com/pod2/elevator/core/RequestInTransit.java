package com.pod2.elevator.core;

import com.pod2.elevator.core.events.PassengerRequest;

public class RequestInTransit extends PassengerRequest {

	private int elevatorNumber;
	private long onloadQuantum;
	private long offloadQuantum;
	private DeliveryStatus deliveryStatus;

	public RequestInTransit(PassengerRequest passengerRequest) {
		super(passengerRequest);
	}

	public int getElevatorNumber() {
		return elevatorNumber;
	}

	public void setElevatorNumber(int elevatorNumber) {
		this.elevatorNumber = elevatorNumber;
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

	public DeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

}
