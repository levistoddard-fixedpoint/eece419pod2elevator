package com.pod2.elevator.data;
import java.util.Collection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.RequestInTransit;
import com.pod2.elevator.core.ResultsBuilder;
import com.pod2.elevator.core.events.Event;
import com.pod2.elevator.view.data.LogMessage;

public class DummyResultsBuilder implements ResultsBuilder {

	Multimap<Long, LogMessage> messages = ArrayListMultimap.create();

	@Override
	public void onStart() {
		// System.err.println("starting simulation...");
	}

	@Override
	public void logCompletedQuantum(long quantum, ActiveSimulation activeSimulation) {
		// System.err.println("completed quantum...");
	}

	@Override
	public void logEvent(long quantum, Event event) {
		String message = String.format("%10d %s", quantum, event);
		messages.put(quantum, new LogMessage(message));
	}

	@Override
	public void logRequestStateChange(long quantum, RequestInTransit request) {
		String message = String.format("%10d %s", quantum, request);
		messages.put(quantum, new LogMessage(message));
	}

	@Override
	public Collection<LogMessage> getLogEntries(long quantum) {
		return messages.get(quantum);
	}

	@Override
	public void onEnd(long timeQuantum) {
		// System.err.println("ending simulation at " + timeQuantum + "...");
	}

	@Override
	public void save() {
		// System.err.println("saving simulation...");
	}

}