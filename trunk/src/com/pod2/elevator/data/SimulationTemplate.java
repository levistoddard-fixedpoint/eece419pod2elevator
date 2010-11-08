package com.pod2.elevator.data;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.pod2.elevator.scheduling.ElevatorScheduler;


public class SimulationTemplate {

	public int id;
	public String name;
	public int numberFloors;
	public int elevatorCapacity;
	public int numberElevators;
	public Set<Integer> restrictedFloors;
	public ElevatorScheduler scheduler;
	public boolean requestGenerationOn;
	public List<TemplatePassengerRequest> passengerRequests;
	public List<TemplateFailureEvent> failureEvents;
	public List<TemplateServiceEvent> serviceEvents;
	public Date created;
	public Date lastEdit;
	
}
