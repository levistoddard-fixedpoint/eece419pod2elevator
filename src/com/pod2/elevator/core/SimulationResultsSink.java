package com.pod2.elevator.core;

import com.pod2.elevator.view.LogMessage;

public interface SimulationResultsSink {

	public void onStart();

	public void onEnd(long timeQuantum);

	public void onQuantumComplete(ActiveSimulation activeSimulation);

	public void logFinishedRequest(RequestInTransit request);

	public void logMessage(long timeQuantum, Object message);

	public LogMessage[] getLoggedMessages(long timeQuantum);

	public void save();

}
