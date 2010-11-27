package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;

public abstract class Event {
	/**
	 * OVERVIEW: Represents any state changing event which can be applied to an
	 * 		ActiveSimulation.
	 */

	private final EventSource source;
	private final long quantum;

	Event(EventSource source, long quantum) {
		/**
		 * 	REQUIRES: source != null && quantum >= 0
		 * 	MODIFIES: this
		 * 	EFFECTS: Constructor for the class.  Initialize event source and time
		 * 		quantum.
		 */
		assert (source != null);
		assert (quantum >= 0);
		this.source = source;
		this.quantum = quantum;
	}

	public abstract void apply(ActiveSimulation simulation);
	/**
	 * 	REQUIRES: simulation != null
	 * 	MODIFIES: this
	 * 	EFFECTS: Apply event to simulation.
	 */

	public boolean canApplyNow(ActiveSimulation simulation) {
		/**
		 * 	EFFECTS: Returns true for ability to apply events.
		 */
		return true;
	}

	public EventSource getSource() {
		/**
		 * 	EFFECTS: Returns the event source.
		 */
		return source;
	}
	
	public long getQuantum() {
		/**
		 * 	EFFECTS: Returns the time quantum.
		 */
		return quantum;
	}

	@Override
	public String toString() {
		/**
		 * 	EFFECTS: Returns the string representation of the source.
		 */
		return String.format("%s", source);
	}

}
