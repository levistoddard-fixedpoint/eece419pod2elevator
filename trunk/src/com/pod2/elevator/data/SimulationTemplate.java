package com.pod2.elevator.data;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.pod2.elevator.core.component.ComponentRegistry;
import com.pod2.elevator.core.events.Event;
import com.pod2.elevator.core.events.EventFactory;
import com.pod2.elevator.core.events.EventSource;
import com.pod2.elevator.scheduling.ElevatorScheduler;

public class SimulationTemplate {

	public int id;
	public String name;
	public int numberFloors;
	public int elevatorCapacity;
	public int numberElevators;
	public double speed; /* floors per second */
	public Set<Integer> restrictedFloors;
	public ElevatorScheduler scheduler;
	public boolean requestGenerationOn;
	public List<TemplatePassengerRequest> passengerRequests;
	public List<TemplateFailureEvent> failureEvents;
	public List<TemplateServiceEvent> serviceEvents;
	public Date created;
	public Date lastEdit;

	public List<Event> getEvents() {
		List<Event> eventList = new LinkedList<Event>();
		for (TemplatePassengerRequest events : passengerRequests) {
			eventList.add(EventFactory.createPassengerRequest(
					EventSource.Template, events.quantum, events.onloadFloor,
					events.offloadFloor, events.timeConstraint));
		}
		for (TemplateFailureEvent event : failureEvents) {
			eventList.add(EventFactory.createComponentFailureEvent(
					EventSource.Template, event.quantum, event.elevatorNumber,
					ComponentRegistry.getComponentByKey(event.componentKey)));
		}
		for (TemplateServiceEvent event : serviceEvents) {
			eventList.add(EventFactory.createServiceEvent(EventSource.Template,
					event.quantum, event.elevatorNumber, event.putInService));
		}
		return eventList;
	}

	/*
	 * auto-generated below here
	 */
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

}
