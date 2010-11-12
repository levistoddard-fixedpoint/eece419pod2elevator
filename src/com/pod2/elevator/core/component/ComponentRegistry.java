package com.pod2.elevator.core.component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * Maintains a collection of the type of physical components (ElevatorComponent
 * sub-classes) which can exist inside an Elevator.
 * 
 */
public class ComponentRegistry {

	private static final Map<String, ComponentDetails> components;

	static {
		components = new HashMap<String, ComponentDetails>();
		register(DoorDriveMechanism.getDetails());
		register(DoorSensor.getDetails());
		register(DriveMechanism.getDetails());
		register(EmergencyBrake.getDetails());
		register(PositionSensor.getDetails());
	}

	public static Collection<ComponentDetails> getFailableComponents() {
		return Collections2.filter(components.values(),
				new Predicate<ComponentDetails>() {
					@Override
					public boolean apply(ComponentDetails component) {
						return component.getCanFail();
					}
				});
	}

	public static ComponentDetails getComponentByKey(String key) {
		assert (key != null);
		ComponentDetails details = components.get(key);
		if (details == null) {
			throw new RuntimeException("no component with key: " + key);
		}
		return details;
	}

	private static void register(ComponentDetails details) {
		components.put(details.getKey(), details);
	}

}
