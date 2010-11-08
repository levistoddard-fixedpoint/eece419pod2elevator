package data;

import java.util.List;
import java.util.Set;

public class SimulationTemplate {

	public int id;
	public int numberFloors;
	public int elevatorCapacity;
	public int numberElevators;
	public Set<Integer> restrictedFloors;
	//public ElevatorScheduler scheduler;
	public boolean requestGenerationOn;
	public List<TemplatePassengerRequest> passengerRequests;
	public List<TemplateFailureEvent> failureEvents;
	public List<TemplateServiceEvent> serviceEvents;
	
}
