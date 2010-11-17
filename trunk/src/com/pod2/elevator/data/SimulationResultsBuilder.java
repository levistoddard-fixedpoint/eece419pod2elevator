package com.pod2.elevator.data;

import java.util.Collection;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.ResultsBuilder;
import com.pod2.elevator.core.events.Event;
import com.pod2.elevator.core.RequestInTransit;
import com.pod2.elevator.view.LogMessage;

public class SimulationResultsBuilder implements ResultsBuilder {
	public void addResults(ActiveSimulation activeSimulation) {
		
	}
	
	public void save() {
		
	}
	
	public void logFinishedRequest(RequestInTransit finishedRequest) {
		
	}
	
	public void logMessage(long timeQuantum, String message) {
		
	}
	
	public void setStartTime(long timeQuantum, long systemTime) {
		
	}
	
	public void setEndTime(long timeQuantum, long systemTime) {
		
	}

	public Collection<LogMessage> getLogEntries(long quantum) {
		// TODO Auto-generated method stub
		return null;
	}

	public void logCompletedQuantum(ActiveSimulation activeSimulation) {
		// TODO Auto-generated method stub
		
	}

	public void logEvent(long quantum, Event event) {
		// TODO Auto-generated method stub
		
	}

	public void logRequestStateChange(long quantum,
			RequestInTransit request) {
		// TODO Auto-generated method stub
		
	}

	public void onEnd(long quantum) {
		// TODO Auto-generated method stub
		
	}

	public void onStart() {
		// TODO Auto-generated method stub
		
	}
}
