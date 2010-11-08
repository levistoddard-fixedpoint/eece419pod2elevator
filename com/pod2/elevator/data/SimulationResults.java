package data;

import java.sql.Date;
import java.util.List;

public class SimulationResults {
	public String uuid;
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
