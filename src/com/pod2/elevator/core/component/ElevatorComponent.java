package com.pod2.elevator.core.component;

public abstract class ElevatorComponent {
	/**
	 * OVERVIEW: Base class from which all physical Elevator components are derived.
	 */
	
	private boolean isFailed = false;

	public boolean isFailed() {
		/**
		 * 	EFFECTS: Return true if component has failed.  False otherwise.
		 */
		
		return isFailed;
	}

	public void setFailed(boolean isFailed) {
		/**
		 * 	REQUIRES: isFailed != null
		 * 	MODIFIES: this
		 * 	EFFECTS: Set the fail state of the component.
		 */
		this.isFailed = isFailed;
	}

	public abstract String getKey();
	/**
	 * 	EFFECTS: Returns the name of the component.
	 */

}
