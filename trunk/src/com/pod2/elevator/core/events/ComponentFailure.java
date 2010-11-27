package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.Elevator;
import com.pod2.elevator.core.component.ComponentDetails;
import com.pod2.elevator.core.component.ElevatorComponent;

public class ComponentFailure extends ElevatorEvent {
	/**
	 * OVERVIEW: An ElevatorEvent which causes one ElevatorComponent in an Elevator to
	 * 		immediately fail.
	 */

	private final ComponentDetails details;

	ComponentFailure(EventSource source, long quantum, int elevatorNumber,
			ComponentDetails details) {
		/**
		 *  REQUIRES: source != null && quantum >= 0 && elevatorNumber != null &&
		 *  	details != null
		 *  MODIFIES: this.details
		 *  EFFECTS: Constructor for the class.  Initialize the component details.
		 */
		super(source, quantum, elevatorNumber);
		assert (details != null);
		this.details = details;
	}

	@Override
	public void apply(ActiveSimulation simulation) {
		/**
		 * 	REQUIRES: simulation != null
		 * 	EFFECTS: Set component to fail.
		 */
		Elevator elevator = simulation.getElevators()[getElevatorNumber()];
		ElevatorComponent component = elevator.getComponent(details.getKey());
		component.setFailed(true);
	}

	@Override
	public String toString() {
		/**
		 * 	EFFECTS: Return the name of the failed component.
		 */
		final String format = "%s %s failed.";
		return String.format(format, super.toString(), details.getName());
	}

}
