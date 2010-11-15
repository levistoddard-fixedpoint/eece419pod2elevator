package com.pod2.elevator.data;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import com.pod2.elevator.scheduling.ElevatorScheduler;


public class SimulationTemplate {

	private int id;
	private String name;
	private int numberFloors;
	private int elevatorCapacity;
	private int numberElevators;
	private double speed; /* floors per second */
	private Set<Integer> restrictedFloors;
	private ElevatorScheduler scheduler;
	private boolean requestGenerationOn;
	private List<TemplatePassengerRequest> passengerRequests;
	private List<TemplateFailureEvent> failureEvents;
	private List<TemplateServiceEvent> serviceEvents;
	private Date created;
	private Date lastEdit;
	private long quantumsBeforeService;
	private double distanceBeforeService;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNumberFloors() {
		return numberFloors;
	}
	public void setNumberFloors(int numberFloors) {
		this.numberFloors = numberFloors;
	}
	public int getElevatorCapacity() {
		return elevatorCapacity;
	}
	public void setElevatorCapacity(int elevatorCapacity) {
		this.elevatorCapacity = elevatorCapacity;
	}
	public int getNumberElevators() {
		return numberElevators;
	}
	public void setNumberElevators(int numberElevators) {
		this.numberElevators = numberElevators;
	}
	public Set<Integer> getRestrictedFloors() {
		return restrictedFloors;
	}
	public void setRestrictedFloors(Set<Integer> restrictedFloors) {
		this.restrictedFloors = restrictedFloors;
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
	public List<TemplatePassengerRequest> getPassengerRequests() {
		return passengerRequests;
	}
	public void setPassengerRequests(
			List<TemplatePassengerRequest> passengerRequests) {
		this.passengerRequests = passengerRequests;
	}
	public List<TemplateFailureEvent> getFailureEvents() {
		return failureEvents;
	}
	public void setFailureEvents(List<TemplateFailureEvent> failureEvents) {
		this.failureEvents = failureEvents;
	}
	public List<TemplateServiceEvent> getServiceEvents() {
		return serviceEvents;
	}
	public void setServiceEvents(List<TemplateServiceEvent> serviceEvents) {
		this.serviceEvents = serviceEvents;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getLastEdit() {
		return lastEdit;
	}
	public void setLastEdit(Date lastEdit) {
		this.lastEdit = lastEdit;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
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
	
}
