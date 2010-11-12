package com.pod2.elevator.core;

import com.pod2.elevator.core.events.PassengerRequest;

/**
 * Augments a PassengerRequest to add pick-up, drop-off, and delivery status
 * which resulted from the passengers trip in the ActiveSimulation.
 * 
 */
public class RequestInTransit {

	private final long requestNumber;
	private final PassengerRequest request;

	private int elevatorNumber;
	private long offloadQuantum;
	private long onloadQuantum;
	private DeliveryStatus status;

	public RequestInTransit(long requestNumber, PassengerRequest request) {
		assert (request != null);
		this.requestNumber = requestNumber;
		this.request = request;
	}

	public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.status = deliveryStatus;
	}

	public void setElevatorNumber(int elevatorNumber) {
		this.elevatorNumber = elevatorNumber;
	}

	public void setOffloadQuantum(long offloadQuantum) {
		this.offloadQuantum = offloadQuantum;
	}

	public void setOnloadQuantum(long onloadQuantum) {
		this.onloadQuantum = onloadQuantum;
	}

	public int getOnloadFloor() {
		return request.getOnloadFloor();
	}

	public int getOffloadFloor() {
		return request.getOffloadFloor();
	}

	public long getTimeConstraint() {
		return request.getTimeConstraint();
	}

	public int getElevatorNumber() {
		return elevatorNumber;
	}

	public long getOffloadQuantum() {
		return offloadQuantum;
	}

	public long getOnloadQuantum() {
		return onloadQuantum;
	}

	public DeliveryStatus getDeliveryStatus() {
		return status;
	}

	public long getPassengerNumber() {
		return requestNumber;
	}

	@Override
	public String toString() {
		if (DeliveryStatus.Waiting.equals(status)) {
			return String.format("Passenger %d waiting on floor %d.",
					requestNumber, request.getOnloadFloor());
		} else if (DeliveryStatus.InElevator.equals(status)) {
			return String.format("Passenger %d entered elevator %d",
					requestNumber, getElevatorNumber());
		} else if (DeliveryStatus.Rescued.equals(status)) {
			return String.format("Passenger %d rescued from elevator %d.",
					requestNumber, getElevatorNumber());
		} else if (DeliveryStatus.Delivered.equals(status)) {
			return String.format("Passenger %d delivered to floor %d.",
					requestNumber, request.getOffloadFloor());
		} else {
			throw new RuntimeException(String.format("unknown status: %s",
					status));
		}
	}

}
