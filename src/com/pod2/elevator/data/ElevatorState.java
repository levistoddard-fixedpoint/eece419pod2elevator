package com.pod2.elevator.data;

import com.pod2.elevator.core.ServiceStatus;

public class ElevatorState {
	
	private double position;
	private ServiceStatus status;
	private double cumulativeDistanceTravelled;
	private long cumulativeServiceTime;
	
	public double getPosition() {
		return position;
	}
	public void setPosition(double position) {
		this.position = position;
	}
	public ServiceStatus getStatus() {
		return status;
	}
	public void setStatus(ServiceStatus status) {
		this.status = status;
	}
	public void setCumulativeDistanceTravelled(double cumulativeDistanceTravelled) {
		this.cumulativeDistanceTravelled = cumulativeDistanceTravelled;
	}
	public double getCumulativeDistanceTravelled() {
		return cumulativeDistanceTravelled;
	}
	public void setCumulativeServiceTime(long cumulativeServiceTime) {
		this.cumulativeServiceTime = cumulativeServiceTime;
	}
	public long getCumulativeServiceTime() {
		return cumulativeServiceTime;
	}
	
}
