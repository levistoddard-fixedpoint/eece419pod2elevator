package com.pod2.elevator.core;

public class FloorRequestButton {

	private boolean isUpSelected;
	private boolean isDownSelected;
	private long upSelectedQuantum;
	private long downSelectedQuantum;

	public FloorRequestButton() {
		clearSelections();
	}

	public void click(long quantum, boolean isGoingUp) {
		if (isGoingUp && !isUpSelected) {
			upSelectedQuantum = quantum;
			isUpSelected = true;
		} else if (!isGoingUp && !isDownSelected) {
			downSelectedQuantum = quantum;
			isDownSelected = true;
		}
	}

	public void clearSelections() {
		isUpSelected = isDownSelected = false;
		upSelectedQuantum = downSelectedQuantum = -1;
	}

	public boolean isUpSelected() {
		return isUpSelected;
	}

	public boolean isDownSelected() {
		return isDownSelected;
	}

	public long getUpSelectedQuantum() {
		return upSelectedQuantum;
	}

	public long getDownSelectedQuantum() {
		return downSelectedQuantum;
	}

}
