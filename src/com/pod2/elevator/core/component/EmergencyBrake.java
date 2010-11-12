package com.pod2.elevator.core.component;

/**
 * ElevatorComponent which stops an elevator from being able to move up or down.
 * This component cannot fail.
 * 
 */
public class EmergencyBrake extends ElevatorComponent {

	private static final ComponentDetails details = new ComponentDetails(
			EmergencyBrake.class.getName(), "Emergency Brake", false);

	private boolean isEnabled = false;

	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	@Override
	public String getKey() {
		return details.getKey();
	}

	static ComponentDetails getDetails() {
		return details;
	}

}
