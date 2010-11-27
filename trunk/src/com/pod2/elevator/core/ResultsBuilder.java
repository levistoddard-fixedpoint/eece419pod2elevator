package com.pod2.elevator.core;

import java.sql.SQLException;
import java.util.Collection;

import com.pod2.elevator.core.events.Event;
import com.pod2.elevator.view.data.LogMessage;

/**
 *  OVERVIEW: Aggregates all data resulting from an ActiveSimulation.
 */
public interface ResultsBuilder {
	
	public void onStart();
	/**
	 * 	MODIFIES: this
	 * 	EFFECTS: Sets the quantum when the ResultBuilder is started.
	 */

	public void logCompletedQuantum(long quantum, ActiveSimulation activeSimulation);
	/**
	 * 	REQUIRES: quantum != null && activeSimulation != null
	 * 	MODIFIES: this
	 * 	EFFECTS: Record the quantum that has been completed and the activeSimulation
	 * 		to the ResultBuilder log.
	 */

	public void logEvent(long quantum, Event event);
	/**
	 * 	REQUIRES: quantum != null && event != null
	 * 	MODIFIES: this
	 * 	EFFECTS: Record the event that has taken place and quantum to the
	 * 		ResultBuilder log.
	 */

	public void logRequestStateChange(long quantum, RequestInTransit request);
	/**
	 * 	REQUIRES: quantum != null && request != null
	 * 	MODIFIES: this
	 * 	EFFECTS: Record the change of state for request and quantum taken place to
	 * 		the ResultBuilder log.
	 */

	public Collection<LogMessage> getLogEntries(long quantum);
	/**
	 * 	EFFECTS: Returns all entries log of the quantum specified in a Collection of
	 * 		LogMessage.
	 */
	
	public void onEnd(long quantum);
	/**
	 *  REQUIRES: quantum != null
	 *  MODIFIES: this
	 *  EFFECTS: Sets the time quantum when the ResultBuilder ends
	 */

	public void save() throws SQLException;
	/**
	 *  EFFECTS: Save the ResultBuilder logs to the SQL database.  Throw SQLException
	 *  	if any SQL related errors occur.
	 */

}
