package com.pod2.elevator.core;

import java.sql.SQLException;
import java.util.Collection;

import com.pod2.elevator.core.events.Event;
import com.pod2.elevator.view.data.LogMessage;

/**
 * Aggregates all data resulting from an ActiveSimulation.
 * 
 */
public interface ResultsBuilder {

	public void onStart();

	public void logCompletedQuantum(long quantum, ActiveSimulation activeSimulation);

	public void logEvent(long quantum, Event event);

	public void logRequestStateChange(long quantum, RequestInTransit request);

	public Collection<LogMessage> getLogEntries(long quantum);

	public void onEnd(long quantum);

	public void save() throws SQLException;

}
