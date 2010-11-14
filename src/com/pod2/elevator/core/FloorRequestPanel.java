package com.pod2.elevator.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents the request panel within a single Elevator, indicating which
 * destination floors have been selected.
 * 
 */
public class FloorRequestPanel {

	private final Set<Integer> requestedFloors;
	private final Map<Integer, Long> requestedTimes;

	FloorRequestPanel() {
		requestedFloors = new HashSet<Integer>();
		requestedTimes = new HashMap<Integer, Long>();
	}

	public void clearRequest(int floor) {
		requestedFloors.remove(floor);
		requestedTimes.remove(floor);
	}

	public Set<Integer> getRequestedFloors() {
		return Collections.unmodifiableSet(requestedFloors);
	}

	public boolean isRequested(int floor) {
		return requestedFloors.contains(floor);
	}

	public long getRequestedTime(int floor) {
		assert (requestedFloors.contains(floor));
		return requestedTimes.get(floor);
	}

	public void request(int floor, long currentQuantum) {
		if (!requestedFloors.contains(floor)) {
			requestedFloors.add(floor);
			requestedTimes.put(floor, currentQuantum);
		}
	}

}
