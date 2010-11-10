package com.pod2.elevator.core.events;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.component.ElevatorComponent;

public class ComponentFailure extends ElevatorEvent {

	private Class<? extends ElevatorComponent> component;

	public ComponentFailure(EventSource eventSource, long timeQuantum,
			int elevatorNumber, Class<? extends ElevatorComponent> component) {
		super(eventSource, timeQuantum, elevatorNumber);
		this.component = component;
	}

	@Override
	public void apply(ActiveSimulation activeSimulation) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canApplyNow(ActiveSimulation activeSimulation) {
		return true;
	}

}
