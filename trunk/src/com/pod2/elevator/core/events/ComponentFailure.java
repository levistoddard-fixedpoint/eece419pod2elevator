package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.Elevator;
import com.pod2.elevator.core.component.ComponentDetails;
import com.pod2.elevator.core.component.ElevatorComponent;

/**
 * An ElevatorEvent which causes one ElevatorComponent in an Elevator to
 * immediately fail.
 * 
 */
public class ComponentFailure extends ElevatorEvent {

	private final ComponentDetails details;

	ComponentFailure(EventSource source, long quantum, int elevatorNumber,
			ComponentDetails details) {
		super(source, quantum, elevatorNumber);
		assert (details != null);
		this.details = details;
	}

	@Override
	public void apply(ActiveSimulation simulation) {
		Elevator elevator = simulation.getElevators()[getElevatorNumber()];
		ElevatorComponent component = elevator.getComponent(details.getKey());
		component.setFailed(true);
	}

	@Override
	public String toString() {
		final String format = "%s %s failed.";
		return String.format(format, super.toString(), details.getName());
	}

}
