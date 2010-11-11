package com.pod2.elevator.core.test;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.RequestInTransit;
import com.pod2.elevator.core.SimulationResultsSink;
import com.pod2.elevator.view.LogMessage;

public class DummyResultsSink implements SimulationResultsSink {

	@Override
	public void onStart() {
		System.err.println("starting simulation...");
	}

	@Override
	public void onEnd(long timeQuantum) {
		System.err.println("ending simulation at " + timeQuantum + "...");
	}

	@Override
	public void onQuantumComplete(ActiveSimulation activeSimulation) {
		//System.err.println("completed quantum...");
	}

	@Override
	public void logFinishedRequest(RequestInTransit request) {
		System.err.println("finnished request");
	}

	@Override
	public void logMessage(long timeQuantum, Object message) {
		System.err.println("logging message");
	}

	@Override
	public LogMessage[] getLoggedMessages(long timeQuantum) {
		return new LogMessage[0];
	}

	@Override
	public void save() {
		System.err.println("saving simulation");
	}

}
