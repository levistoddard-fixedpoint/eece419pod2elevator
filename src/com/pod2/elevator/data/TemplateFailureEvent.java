package com.pod2.elevator.data;

import com.pod2.elevator.core.component.ComponentDetails;

public class TemplateFailureEvent extends TemplateElevatorEvent {

	private ComponentDetails component;

	public ComponentDetails getComponent() {
		return component;
	}

	public void setComponent(ComponentDetails component) {
		this.component = component;
	}

}
