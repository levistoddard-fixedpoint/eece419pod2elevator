package com.pod2.elevator.core.component;

public class EmergencyBrake extends ElevatorComponent {
	/**
	 * OVERVIEW: ElevatorComponent which stops an elevator from being able to move
	 * 		up or down. This component cannot fail.
	 */

	private static final ComponentDetails details = new ComponentDetails(
			EmergencyBrake.class.getName(), "Emergency Brake", false);

	private boolean isEnabled = false;
	
	public void setIsEnabled(boolean isEnabled) {
		/**
		 * 	REQUIRES: isEnabled != null
		 * 	MODIFIES: this.isEnabled
		 * 	EFFECTS: Set emergency brakes on or off
		 */
		this.isEnabled = isEnabled;
	}

	public boolean isEnabled() {
		/**
		 * 	EFFECTS: Returns true if emergency brakes are on.  False otherwise.
		 */
		return isEnabled;
	}

	@Override
	public String getKey() {
		/**
		 * 	EFFECTS: Return the name of component from component detail
		 */
		return details.getKey();
	}

	static ComponentDetails getDetails() {
		/**
		 * 	EFFECTS: Return ComponentDetails object containing door drive component details
		 */
		return details;
	}

}
