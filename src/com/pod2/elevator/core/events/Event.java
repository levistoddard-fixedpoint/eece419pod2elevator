package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;

public abstract class Event {

	protected long timeQuantum;
	protected EventSource eventSource;

	public Event(EventSource eventSource, long timeQuantum) {
		this.eventSource = eventSource;
		this.timeQuantum = timeQuantum;
	}

	public long getTimeQuantum() {
		return timeQuantum;
	}

	public EventSource getEventSource() {
		return eventSource;
	}

	public abstract void apply(ActiveSimulation activeSimulation);

	public abstract boolean canApplyNow(ActiveSimulation activeSimulation);
	
}
