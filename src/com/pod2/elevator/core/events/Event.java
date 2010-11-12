package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;

/**
 * Represents any state changing event which can be applied to an
 * ActiveSimulation.
 * 
 */
public abstract class Event {

	private final EventSource source;
	private final long quantum;

	Event(EventSource source, long quantum) {
		assert (source != null);
		assert (quantum >= 0);
		this.source = source;
		this.quantum = quantum;
	}

	public abstract void apply(ActiveSimulation simulation);

	public boolean canApplyNow(ActiveSimulation simulation) {
		return true;
	}

	public EventSource getSource() {
		return source;
	}
	
	public long getQuantum() {
		return quantum;
	}

	@Override
	public String toString() {
		return String.format("%s", source);
	}

}
