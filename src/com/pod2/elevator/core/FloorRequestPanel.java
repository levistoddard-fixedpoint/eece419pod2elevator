package com.pod2.elevator.core;

import java.util.HashSet;
import java.util.Set;

public class FloorRequestPanel {

	private Set<Integer> requestedFloors;

	public FloorRequestPanel() {
		requestedFloors = new HashSet<Integer>();
	}

	public void request(int floorNumber) {
		requestedFloors.add(floorNumber);
	}

	public void clearRequest(int floorNumber) {
		requestedFloors.remove(floorNumber);
	}

	public Set<Integer> getRequestedFloors() {
		return requestedFloors;
	}

	public boolean areRequests() {
		return !requestedFloors.isEmpty();
	}

	public boolean isRequested(int floorNumber) {
		return requestedFloors.contains(floorNumber);
	}

	public void clearRequests() {
		requestedFloors.clear();
	}

}
