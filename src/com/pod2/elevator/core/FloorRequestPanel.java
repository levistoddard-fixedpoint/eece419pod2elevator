package com.pod2.elevator.core;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the request panel within a single Elevator, indicating which
 * destination floors have been selected.
 * 
 */
public class FloorRequestPanel {

	private final Set<Integer> requestedFloors;

	public FloorRequestPanel() {
		requestedFloors = new HashSet<Integer>();
	}

	public void clearRequest(int floorNumber) {
		requestedFloors.remove(floorNumber);
	}

	public Set<Integer> getRequestedFloors() {
		return requestedFloors;
	}

	public boolean isRequested(int floorNumber) {
		return requestedFloors.contains(floorNumber);
	}

	public void request(int floorNumber) {
		requestedFloors.add(floorNumber);
	}

}
