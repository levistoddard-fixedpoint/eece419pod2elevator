package com.pod2.elevator.data;

public class TemplateFailureEvent extends TemplateElevatorEvent {
	
	private String componentKey;

	public String getComponentKey() {
		return componentKey;
	}

	public void setComponentKey(String componentKey) {
		this.componentKey = componentKey;
	}
	
}
