package com.pod2.elevator.core;

public class RequestInTransit {
	private PassengerRequest passengerRequest;
	private int elevatorNumber;
	private long onloadQuantum;
	private long offloadQuantum;
	private DeliveryStatus deliveryStatus;
	
	public RequestInTransit(PassengerRequest passengerRequest) {
		this.passengerRequest = passengerRequest;
	}
}
