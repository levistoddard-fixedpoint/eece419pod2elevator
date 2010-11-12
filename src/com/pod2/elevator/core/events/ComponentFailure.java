package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.Elevator;
import com.pod2.elevator.core.component.ElevatorComponent;

public class ComponentFailure extends ElevatorEvent {

	private String componentKey;

	public ComponentFailure(EventSource eventSource, long timeQuantum,
			int elevatorNumber, String componentKey) {
		super(eventSource, timeQuantum, elevatorNumber);
		this.componentKey = componentKey;
	}

	@Override
	public void apply(ActiveSimulation simulation) {
		Elevator elevator = simulation.getElevators()[getElevatorNumber()];
		ElevatorComponent component = elevator.getComponent(componentKey);
		component.setFailed(true);
	}

	@Override
	public boolean canApplyNow(ActiveSimulation activeSimulation) {
		return true;
	}

	@Override
	public String toString() {
		return String.format("%s: Failed %s component.", super.toString(),
				componentKey);
	}

}
