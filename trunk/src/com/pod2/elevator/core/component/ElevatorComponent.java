package com.pod2.elevator.core.component;

/**
 * Base class from which all physical Elevator components are derived.
 * 
 */
public abstract class ElevatorComponent {

	private boolean isFailed = false;

	public boolean isFailed() {
		return isFailed;
	}

	public void setFailed(boolean isFailed) {
		this.isFailed = isFailed;
	}

	public abstract String getKey();

}
