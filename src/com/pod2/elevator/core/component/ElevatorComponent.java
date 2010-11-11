package com.pod2.elevator.core.component;

public abstract class ElevatorComponent {

	private boolean isFailed;

	public ElevatorComponent() {
		isFailed = false;
	}

	public boolean isFailed() {
		return isFailed;
	}

	public void setFailed(boolean isFailed) {
		this.isFailed = isFailed;
	}
		
}
