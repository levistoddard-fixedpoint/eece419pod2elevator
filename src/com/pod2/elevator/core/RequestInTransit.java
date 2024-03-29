package com.pod2.elevator.core;

import com.pod2.elevator.core.events.PassengerRequest;

public class RequestInTransit {
	/**
	 * OVERVIEW: Augments a PassengerRequest to add pick-up, drop-off, and
	 * delivery status which resulted from the passengers trip in the
	 * ActiveSimulation.
	 */

	private final long requestNumber;
	private final PassengerRequest request;

	private int elevatorNumber;
	private long offloadQuantum;
	private long onloadQuantum;
	private DeliveryStatus status;

	// constructor
	public RequestInTransit(long requestNumber, PassengerRequest request) {
		/**
		 * 	REQUIRES: request != null && requestNumber != null
		 * 	MODIFIES: requestNumber, request
		 * 	EFFECTS: Constructor of the class. Initialize variables for use
		 * 		in this class.
		 */
		assert (request != null);
		this.requestNumber = requestNumber;
		this.request = request;
	}

	public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
		/**
		 * REQUIRES: deliveryStatus != null
		 * MODIFIES: status EFFECTS: Set the
		 * status of the passenger.
		 */
		this.status = deliveryStatus;
	}

	public void setElevatorNumber(int elevatorNumber) {
		/**
		 * REQUIRES: elevatorNumber != null
		 * MODIFIES: this.elevatorNumber
		 * EFFECTS: Set the elevator number that passenger is in.
		 */
		this.elevatorNumber = elevatorNumber;
	}

	public void setOffloadQuantum(long offloadQuantum) {
		/**
		 * REQUIRES: offloadQuantum != null && offloadQuantum >= 0
		 * MODIFIES: this.offloadQuantum
		 * EFFECTS: Set the quantum that passenger gets out of elevator.
		 */
		this.offloadQuantum = offloadQuantum;
	}

	public void setOnloadQuantum(long onloadQuantum) {
		/**
		 * REQUIRES: onloadQuantum != null && onloadQuantum >= 0
		 * MODIFIES:
		 * this.onloadQuantum
		 * EFFECTS: Set the quantum that passenger gets into elevator.
		 */
		this.onloadQuantum = onloadQuantum;
	}

	public int getOnloadFloor() {
		/**
		 * EFFECTS: Returns floor number that passenger got on elevator.
		 */
		return request.getOnloadFloor();
	}

	public int getOffloadFloor() {
		/**
		 * EFFECTS: Returns floor number that passenger got off elevator.
		 */
		return request.getOffloadFloor();
	}

	public long getTimeConstraint() {
		/**
		 * EFFECTS: Returns time constraint of passenger.
		 */
		return request.getTimeConstraint();
	}

	public long getEnterQuantum() {
		/**
		 * EFFECTS: Return the time quantum of the request.
		 */
		return request.getQuantum();
	}

	public int getElevatorNumber() {
		/**
		 * EFFECTS: Returns elevator number of the elevator passenger is in.
		 */
		return elevatorNumber;
	}

	public long getOffloadQuantum() {
		/**
		 * EFFECTS: Returns the quantum which passenger got off elevator
		 */
		return offloadQuantum;
	}

	public long getOnloadQuantum() {
		/**
		 * EFFECTS: Returns the quantum which passenger got on elevator
		 */
		return onloadQuantum;
	}

	public DeliveryStatus getDeliveryStatus() {
		/**
		 * EFFECTS: Returns the delivery status of passenger
		 */
		return status;
	}

	public long getPassengerNumber() {
		/**
		 * EFFECTS: Returns passenger number.
		 */
		return requestNumber;
	}

	@Override
	public String toString() {
		/**
		 * 	EFFECTS: Returns a string based on the DelvieryStatus of the passenger.
		 * 		The string should include information specific to the status.  (Ex.
		 * 		if DelvieryStatus is "Waiting", string should return the passenger
		 * 		number and the floor the passenger is waiting at").
		 */
		if (DeliveryStatus.Waiting.equals(status)) {
			return String.format("Passenger %d waiting on floor %d.", requestNumber,
					request.getOnloadFloor());
		} else if (DeliveryStatus.InElevator.equals(status)) {
			return String.format("Passenger %d entered elevator %d", requestNumber,
					getElevatorNumber());
		} else if (DeliveryStatus.Rescued.equals(status)) {
			return String.format("Passenger %d rescued from elevator %d.", requestNumber,
					getElevatorNumber());
		} else if (DeliveryStatus.Delivered.equals(status)) {
			return String.format("Passenger %d delivered to floor %d.", requestNumber,
					request.getOffloadFloor());
		} else {
			throw new RuntimeException(String.format("unknown status: %s", status));
		}
	}

}