package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;

public abstract class Event {

	private long timeQuantum;
	private EventSource eventSource;

	public Event(EventSource eventSource, long timeQuantum) {
		this.eventSource = eventSource;
		this.timeQuantum = timeQuantum;
	}

	public abstract void apply(ActiveSimulation simulation);

	public abstract boolean canApplyNow(ActiveSimulation simulation);

	public long getTimeQuantum() {
		return timeQuantum;
	}

	public EventSource getEventSource() {
		return eventSource;
	}

}
