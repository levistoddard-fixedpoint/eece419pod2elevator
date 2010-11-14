package com.pod2.elevator.core.component;

/**
 * Represents a type of ElevatorComponent in the ComponentRegistry.
 * 
 */
public class ComponentDetails {

	private final String key;
	private final String name;
	private final boolean canFail;

	ComponentDetails(String key, String name, boolean canFail) {
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
		return "ComponentDetails [key=" + key + ", name=" + name + ", canFail="
				+ canFail + "]";
	}

}
