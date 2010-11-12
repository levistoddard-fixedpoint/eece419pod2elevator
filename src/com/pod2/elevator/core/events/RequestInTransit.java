package com.pod2.elevator.core.events;

import com.pod2.elevator.core.DeliveryStatus;

public class RequestInTransit extends PassengerRequest {

	private final long requestNumber;

	private int elevatorNumber = -1;
	private long onloadQuantum = -1;
	private long offloadQuantum = -1;
	private DeliveryStatus deliveryStatus;

	public RequestInTransit(long requestNumber,
			PassengerRequest passengerRequest) {
		super(passengerRequest);
		this.requestNumber = requestNumber;
	}

	@Override
	public boolean isLoggable() {
		return true;
	}

	public long getPassengerNumber() {
		return requestNumber;
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

	@Override
	public String toString() {
		if (DeliveryStatus.Waiting.equals(deliveryStatus)) {
			return String.format("%s: Passenger %d waiting on floor %d.",
					super.toString(), requestNumber, getOnloadFloor());
		} else if (DeliveryStatus.InElevator.equals(deliveryStatus)) {
			return String.format("%s: Passenger %d entered elevator %d",
					super.toString(), requestNumber, getElevatorNumber());
		} else if (DeliveryStatus.Rescued.equals(deliveryStatus)) {
			return String.format("%s: Passenger %d rescued from elevator %d.",
					super.toString(), requestNumber, getElevatorNumber());
		} else if (DeliveryStatus.Delivered.equals(deliveryStatus)) {
			return String.format("%s: Passenger %d delivered to floor %d.",
					super.toString(), requestNumber, getOffloadFloor());
		} else {
			throw new RuntimeException(String.format(
					"unknown delivery status: %s", deliveryStatus));
		}
	}

}
