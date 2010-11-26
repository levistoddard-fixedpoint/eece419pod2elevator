package com.pod2.elevator.web.views.simulation;

import com.pod2.elevator.scheduling.ElevatorScheduler;

public class SimulationSettings {

	private ElevatorScheduler scheduler;
	private boolean requestGenerationOn;
	private long quantumsBeforeService;
	private double distanceBeforeService;

	public SimulationSettings(ElevatorScheduler scheduler, boolean requestGenerationOn,
			long quantumsBeforeService, double distanceBeforeService) {
		super();
		this.scheduler = scheduler;
		this.requestGenerationOn = requestGenerationOn;
		this.quantumsBeforeService = quantumsBeforeService;
		this.distanceBeforeService = distanceBeforeService;
	}

	public ElevatorScheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(ElevatorScheduler scheduler) {
		this.scheduler = scheduler;
	}

	public boolean isRequestGenerationOn() {
		return requestGenerationOn;
	}

	public void setRequestGenerationOn(boolean requestGenerationOn) {
		this.requestGenerationOn = requestGenerationOn;
	}

	public long getQuantumsBeforeService() {
		return quantumsBeforeService;
	}

	public void setQuantumsBeforeService(long quantumsBeforeService) {
		this.quantumsBeforeService = quantumsBeforeService;
	}

	public double getDistanceBeforeService() {
		return distanceBeforeService;
	}

	public void setDistanceBeforeService(double distanceBeforeService) {
		this.distanceBeforeService = distanceBeforeService;
	}

	public static String[] getEditFields() {
		return new String[] { "scheduler", "requestGenerationOn", "quantumsBeforeService",
				"distanceBeforeService" };
	}

}
