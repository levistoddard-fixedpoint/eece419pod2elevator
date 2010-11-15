package com.pod2.elevator.data;

import java.util.Date;
import java.util.List;

public class SimulationResults {
	private int uuid;
	private String name;
	private int templateId;
	private Date startTime;
	private Date stopTime;
	private long startQuantum;
	private long stopQuantum;
	private List<ElevatorState[]> elevatorStates;
	private List<Integer[]> passengersWaiting;
	private List<CompletedRequest> passengerDeliveries;
	private List<LoggedEvent> events;
	
	public int getUuid() {
		return uuid;
	}
	public void setUuid(int uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTemplateId() {
		return templateId;
	}
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getStopTime() {
		return stopTime;
	}
	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}
	public long getStartQuantum() {
		return startQuantum;
	}
	public void setStartQuantum(long startQuantum) {
		this.startQuantum = startQuantum;
	}
	public long getStopQuantum() {
		return stopQuantum;
	}
	public void setStopQuantum(long stopQuantum) {
		this.stopQuantum = stopQuantum;
	}
	public List<ElevatorState[]> getElevatorStates() {
		return elevatorStates;
	}
	public void setElevatorStates(List<ElevatorState[]> elevatorStates) {
		this.elevatorStates = elevatorStates;
	}
	public List<Integer[]> getPassengersWaiting() {
		return passengersWaiting;
	}
	public void setPassengersWaiting(List<Integer[]> passengersWaiting) {
		this.passengersWaiting = passengersWaiting;
	}
	public List<CompletedRequest> getPassengerDeliveries() {
		return passengerDeliveries;
	}
	public void setPassengerDeliveries(List<CompletedRequest> passengerDeliveries) {
		this.passengerDeliveries = passengerDeliveries;
	}
	public List<LoggedEvent> getEvents() {
		return events;
	}
	public void setEvents(List<LoggedEvent> events) {
		this.events = events;
	}
	
}
