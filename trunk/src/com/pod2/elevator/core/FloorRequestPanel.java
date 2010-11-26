package com.pod2.elevator.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FloorRequestPanel {
	/**
	 * OVERVIEW: Represents the request panel within a single Elevator, indicating which
	 * 		destination floors have been selected.
	 */

	private final Set<Integer> requestedFloors;
	private final Map<Integer, Long> requestedTimes;

	// constructor
	FloorRequestPanel() {
		/**
		 *  MODIFIES: requestedFloors, requestedTimes
		 *  EFFECTS: Constructor of the class.  Initializes requestedFloors and requestedTimes.
		 */
		requestedFloors = new HashSet<Integer>();
		requestedTimes = new HashMap<Integer, Long>();
	}

	public void clearRequest(int floor) {
		/**
		 * 	REQUIRES: floor != null
		 *  MODIFIES: requestedFloors, requestedTimes
		 *  EFFECTS: remove the element floor from requestedFloors and requestedTimes.
		 */
		requestedFloors.remove(floor);
		requestedTimes.remove(floor);
	}

	public Set<Integer> getRequestedFloors() {
		/**
		 * 	EFFECTS: returns the set of floors requested
		 */
		return Collections.unmodifiableSet(requestedFloors);
	}

	public boolean isRequested(int floor) {
		/**
		 * 	REQUIRES: floor != null
		 * 	EFFECTS: returns true if the floor has been requested.  False otherwise.
		 */
		return requestedFloors.contains(floor);
	}
	
	public int getRequestSize() {
		/**
		 * 	EFFECTS: returns the number of requests.
		 */
		return requestedFloors.size();
	}

	public long getRequestedTime(int floor) {
		/**
		 *  EFFECTS: returns the quantums of requests
		 */
		assert (requestedFloors.contains(floor));
		return requestedTimes.get(floor);
	}

	public void request(int floor, long currentQuantum) {
		/**
		 *	REQUIRES: currentQuantum != null && currentQuantum >= 0
		 *  MODIFIES: requestedFloors, requestedTimes
		 *  EFFECTS: Add request to requestedFloor and requestedTime if requestedFloor does
		 *  	not contain floor.
		 */
		if (!requestedFloors.contains(floor)) {
			requestedFloors.add(floor);
			requestedTimes.put(floor, currentQuantum);
		}
	}

}
