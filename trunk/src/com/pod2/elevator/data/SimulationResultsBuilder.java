package com.pod2.elevator.data;

import java.util.Date;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.events.RequestInTransit;
import com.pod2.elevator.view.LogMessage;

public class SimulationResultsBuilder {

	public void addResults(ActiveSimulation activeSimulation) {

	}

	public void save() {

	}

	public void logFinishedRequest(RequestInTransit finishedRequest) {

	}

	public void logMessage(long timeQuantum, String message) {

	}

	public void setStartTime(long timeQuantum, Date systemTime) {

	}

	public void setEndTime(long timeQuantum, Date systemTime) {

	}

	public LogMessage[] getLoggedMessages(long timeQuantum) {
		/* TODO implement this */
		return null;
	}
	
}
