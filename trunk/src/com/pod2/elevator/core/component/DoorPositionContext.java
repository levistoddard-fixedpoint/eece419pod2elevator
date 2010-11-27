package com.pod2.elevator.core.component;

public class DoorPositionContext {
	/**
	 *  OVERVIEW: Stores the current position of an Elevator's doors.
	 */
	
	private double distanceUntilClosed;
	
	// constructor
	public DoorPositionContext(double distanceUntilClosed) {
		/**
		 * 	REQUIRES: distanceUntilClosed >= 0
		 * 	MODIFIES: this
		 * 	EFFECTS: Constructor of the class.  Initialize the distance between
		 * 		door is closed.
		 */
		setDistanceUntilClosed(distanceUntilClosed);
	}
	
	// methods
	public double getDistanceUntilClosed() {
		return distanceUntilClosed;
	}

	public void setDistanceUntilClosed(double distanceUntilClosed) {
		/**
		 * 	REQUIRES: distanceUntilClosed >= 0
		 * 	MODIFIES: this
		 * 	EFFECTS: Changes to distance between the doors.
		 */
		assert (distanceUntilClosed >= 0);
		this.distanceUntilClosed = distanceUntilClosed;
	}

}
