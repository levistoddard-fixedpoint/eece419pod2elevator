package com.pod2.elevator.view;

import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

import com.pod2.elevator.core.MotionStatus;
import com.pod2.elevator.core.ServiceStatus;
import com.pod2.elevator.core.component.ElevatorComponent;

public class ElevatorSnapShot {

	private double currentPosition;
	private Set<Integer> floorsOffLimit;
	private int numberRequests;
	private int requestCapacity;
	private MotionStatus motionStatus;
	private ServiceStatus serviceStatus;
	private TreeMap<String, Boolean> componentFailure;

	public ElevatorSnapShot(double currentPosition,
			Set<Integer> floorsOffLimit, int numberRequests,
			int requestCapacity, MotionStatus motionStatus,
			ServiceStatus serviceStatus,
			Collection<ElevatorComponent> components) {
		super();
		this.currentPosition = currentPosition;
		this.floorsOffLimit = floorsOffLimit;
		this.numberRequests = numberRequests;
		this.requestCapacity = requestCapacity;
		this.motionStatus = motionStatus;
		this.serviceStatus = serviceStatus;

		this.componentFailure = new TreeMap<String, Boolean>();
		for (ElevatorComponent component : components) {
			componentFailure.put(component.getClass().getName(),
					Boolean.valueOf(component.isFailed()));
		}
	}

	public double getCurrentPosition() {
		return currentPosition;
	}

	public Set<Integer> getFloorsOffLimit() {
		return floorsOffLimit;
	}

	public int getNumberRequests() {
		return numberRequests;
	}

	public int getRequestCapacity() {
		return requestCapacity;
	}

	public MotionStatus getMotionStatus() {
		return motionStatus;
	}

	public ServiceStatus getServiceStatus() {
		return serviceStatus;
	}

	public Boolean getFailureStatus(String component) {
		return componentFailure.get(component);
	}

}
