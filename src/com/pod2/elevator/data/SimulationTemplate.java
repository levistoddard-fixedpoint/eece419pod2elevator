package com.pod2.elevator.data;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.pod2.elevator.core.events.Event;
import com.pod2.elevator.core.events.EventFactory;
import com.pod2.elevator.core.events.EventSource;
import com.pod2.elevator.scheduling.ElevatorScheduler;

public class SimulationTemplate extends SimulationTemplateDetail {

	private int id = -1;
	private String name = "";
	private int numberFloors = 5;
	private int elevatorCapacity = 5;
	private int numberElevators = 1;
	private double speed = 0.01; /* floors per second */
	private Set<Integer> restrictedFloors = new HashSet<Integer>();
	private ElevatorScheduler scheduler = null;
	private boolean requestGenerationOn = false;
	private List<TemplatePassengerRequest> passengerRequests = new LinkedList<TemplatePassengerRequest>();
	private List<TemplateFailureEvent> failureEvents = new LinkedList<TemplateFailureEvent>();
	private List<TemplateServiceEvent> serviceEvents = new LinkedList<TemplateServiceEvent>();
	private Date created = new Date();
	private Date lastEdit = new Date();
	private long quantumsBeforeService = 100000;
	private double distanceBeforeService = 100000.00;
	
	public List<Event> getEvents() {
		List<Event> eventList = new LinkedList<Event>();
		for (TemplatePassengerRequest events : passengerRequests) {
			eventList.add(EventFactory.createPassengerRequest(EventSource.Template,
					events.getQuantum(), events.getOnloadFloor(), events.getOffloadFloor(),
					events.getTimeConstraint()));
		}
		for (TemplateFailureEvent event : failureEvents) {
			eventList.add(EventFactory.createComponentFailureEvent(EventSource.Template,
					event.getQuantum(), event.getElevatorNumber(), event.getComponent()));
		}
		for (TemplateServiceEvent event : serviceEvents) {
			eventList.add(EventFactory.createServiceEvent(EventSource.Template, event.getQuantum(),
					event.getElevatorNumber(), event.isPutInService()));
		}
		return eventList;
	}

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

	public void setPassengerRequests(List<TemplatePassengerRequest> passengerRequests) {
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

	@Override
	public String toString() {
		return "SimulationTemplate [id=" + id + ", name=" + name + ", numberFloors=" + numberFloors
				+ ", elevatorCapacity=" + elevatorCapacity + ", numberElevators=" + numberElevators
				+ ", speed=" + speed + ", restrictedFloors=" + restrictedFloors + ", scheduler="
				+ scheduler + ", requestGenerationOn=" + requestGenerationOn
				+ ", passengerRequests=" + passengerRequests + ", failureEvents=" + failureEvents
				+ ", serviceEvents=" + serviceEvents + ", created=" + created + ", lastEdit="
				+ lastEdit + ", quantumsBeforeService=" + quantumsBeforeService
				+ ", distanceBeforeService=" + distanceBeforeService + "]";
	}

}
