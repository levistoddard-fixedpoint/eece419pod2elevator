package com.pod2.elevator.scheduling;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SchedulerRegistry {

	private static final Map<String, ElevatorScheduler> schedulers;

	static {
		schedulers = new HashMap<String, ElevatorScheduler>();
		register(new SabathScheduler());
	}

	private SchedulerRegistry() {
		/* Cannot create instance of this class. */
	}

	public static ElevatorScheduler getSchedulerByKey(String key) {
		if (!schedulers.containsKey(key))
			throw new RuntimeException("no such scheduler: " + key);
		return schedulers.get(key);
	}

	public static Collection<ElevatorScheduler> getAvailableSchedulers() {
		return Collections.unmodifiableCollection(schedulers.values());
	}

	private static void register(ElevatorScheduler scheduler) {
		schedulers.put(scheduler.getKey(), scheduler);
	}

}
