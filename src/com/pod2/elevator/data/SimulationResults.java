package com.pod2.elevator.data;

import java.util.Date;
import java.util.List;

public class SimulationResults {
	public int uuid;
	public String name;
	public int templateId;
	public Date startTime;
	public Date stopTime;
	public long startQuantum;
	public long stopQuantum;
	public List<ElevatorState[]> elevatorStates;
	public List<> passengersWaiting;
	public List<CompletedRequest> passengerDeliveries;
	public List<LoggedEvent> events;
}
