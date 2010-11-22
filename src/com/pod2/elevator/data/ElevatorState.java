package com.pod2.elevator.data;

import com.pod2.elevator.core.ServiceStatus;

public class ElevatorState {
	
	private double position;
	private long quantum;
	private ServiceStatus status;
	
	public double getPosition() {
		return position;
	}
	public void setPosition(double position) {
		this.position = position;
	}
	public long getQuantum() {
		return quantum;
	}
	public void setQuantum(long quantum) {
		this.quantum = quantum;
	}
	public ServiceStatus getStatus() {
		return status;
	}
	public void setStatus(ServiceStatus status) {
		this.status = status;
	}
	
}
