package com.pod2.elevator.core;

import com.pod2.elevator.core.events.PassengerRequest;

public class RequestInTransit {

	private final long requestNumber;
	private final PassengerRequest request;

	private int elevatorNumber;
	private long onloadQuantum;
	private long offloadQuantum;
	private DeliveryStatus status;

	public RequestInTransit(long requestNumber, PassengerRequest request) {
		assert (request != null);
		this.requestNumber = requestNumber;
		this.request = request;
	}

	public int getOnloadFloor() {
		return request.getOnloadFloor();
	}

	public int getOffloadFloor() {
		return request.getOffloadFloor();
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
		return status;
	}

	public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.status = deliveryStatus;
	}

	@Override
	public String toString() {
		if (DeliveryStatus.Waiting.equals(status)) {
			return String.format("%s: Passenger %d waiting on floor %d.",
					super.toString(), requestNumber, request.getOnloadFloor());
		} else if (DeliveryStatus.InElevator.equals(status)) {
			return String.format("%s: Passenger %d entered elevator %d",
					super.toString(), requestNumber, getElevatorNumber());
		} else if (DeliveryStatus.Rescued.equals(status)) {
			return String.format("%s: Passenger %d rescued from elevator %d.",
					super.toString(), requestNumber, getElevatorNumber());
		} else if (DeliveryStatus.Delivered.equals(status)) {
			return String.format("%s: Passenger %d delivered to floor %d.",
					super.toString(), requestNumber, request.getOffloadFloor());
		} else {
			throw new RuntimeException(String.format(
					"unknown delivery status: %s", status));
		}
	}

}
