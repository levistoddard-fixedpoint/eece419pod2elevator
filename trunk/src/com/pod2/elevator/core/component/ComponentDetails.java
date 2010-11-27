package com.pod2.elevator.core.component;

public class ComponentDetails {
	/**
	 *  OVERVIEW: Represents a type of ElevatorComponent in the ComponentRegistry.
	 */
	
	private final String key;
	private final String name;
	private final boolean canFail;
	
	// constructor
	ComponentDetails(String key, String name, boolean canFail) {
		/**
		 * 	REQUIRES: key != null && canFail != null
		 * 	MODIFIES: this
		 * 	EFFECTS: Constructor of the class.  Initialize variables used in this class.
		 */
		super();
		assert (key != null);
		assert (name != null);
		this.key = key;
		this.name = name;
		this.canFail = canFail;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public boolean getCanFail() {
		return canFail;
	}

	@Override
	public String toString() {
		/**
		 * 	EFFECTS: Return the name of the component.
		 */
		return getName();
	}

}
