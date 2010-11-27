package com.pod2.elevator.core;

public class FloorRequestButton {
	/**
	 * OVERVIEW: Represents the up/down buttons used to request an Elevator to service a
	 * 		particular floor.
	 */
	private boolean isUpSelected;
	private boolean isDownSelected;
	private long upSelectedQuantum;
	private long downSelectedQuantum;

	// constructor
	public FloorRequestButton() {
		/**
		 * 	EFFECTS: Constructor of this class.  Reset all elevator requests.
		 */
		clearSelections();
	}

	public void click(long quantum, boolean isGoingUp) {
		/**
		 * 	REQUIRES: quantum != null && quantum > 0 && isGoingUp != null
		 * 	MODIFIES: this
		 * 	EFFECTS: Set the buttons for going up or down.  Only set the
		 * 		buttons if it has not been set before.  Once set, record the
		 * 		quantum when the button is set.
		 */
		if (isGoingUp && !isUpSelected) {
			upSelectedQuantum = quantum;
			isUpSelected = true;
		} else if (!isGoingUp && !isDownSelected) {
			downSelectedQuantum = quantum;
			isDownSelected = true;
		}
	}

	public void clearSelections() {
		/**
		 * 	MODIFIES: this
		 * 	EFFECTS: Reset isUpSelected and isDownSelected buttons to false.  Change
		 * 		upSelectedQuantum and downSelectedQuantum to -1.
		 */
		isUpSelected = isDownSelected = false;
		upSelectedQuantum = downSelectedQuantum = -1;
	}

	public boolean isUpSelected() {
		/**
		 *  EFFECTS: return true if Up button is set.  False otherwise.
		 */
		return isUpSelected;
	}

	public boolean isDownSelected() {
		/**
		 *  EFFECTS: return true if Down button is set.  False otherwise.
		 */
		return isDownSelected;
	}

	public long getUpSelectedQuantum() {
		/**
		 * 	EFFECTS: return the quantum which the Up button was pressed.
		 */
		return upSelectedQuantum;
	}

	public long getDownSelectedQuantum() {
		/**
		 * 	EFFECTS: return the quantum which the Down button was pressed.
		 */
		return downSelectedQuantum;
	}

}
