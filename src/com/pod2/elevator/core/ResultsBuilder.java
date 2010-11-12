package com.pod2.elevator.core;

import com.pod2.elevator.core.events.Event;
import com.pod2.elevator.view.LogMessage;

public interface ResultsBuilder {

	public void onStart();

	public void onQuantumComplete(ActiveSimulation activeSimulation);

	public void logEvent(long quantum, Event event);

	public void logRequestStateChange(long quantum, RequestInTransit request);

	public LogMessage[] getLoggedEvents(long quantum);

	public void onEnd(long quantum);

	public void save();

}
