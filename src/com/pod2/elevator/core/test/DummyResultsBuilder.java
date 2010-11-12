package com.pod2.elevator.core.test;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.RequestInTransit;
import com.pod2.elevator.core.ResultsBuilder;
import com.pod2.elevator.core.events.Event;
import com.pod2.elevator.view.LogMessage;

public class DummyResultsBuilder implements ResultsBuilder {

	@Override
	public void onStart() {
		System.err.println("starting simulation...");
	}

	@Override
	public void onQuantumComplete(ActiveSimulation activeSimulation) {
		// System.err.println("completed quantum...");
	}

	@Override
	public void logEvent(long quantum, Event event) {
		System.err.println(String.format("%10d %s", quantum, event));
	}

	@Override
	public void logRequestStateChange(long quantum, RequestInTransit request) {
		System.err.println(String.format("%10d %s", quantum, request));
	}

	@Override
	public LogMessage[] getLoggedEvents(long quantum) {
		return new LogMessage[0];
	}

	@Override
	public void onEnd(long timeQuantum) {
		System.err.println("ending simulation at " + timeQuantum + "...");
	}

	@Override
	public void save() {
		System.err.println("saving simulation...");
	}

}
