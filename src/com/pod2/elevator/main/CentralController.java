package com.pod2.elevator.main;

import com.pod2.elevator.scheduling.ElevatorScheduler;
import com.pod2.elevator.scheduling.SchedulerRegistry;

public class CentralController {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(ElevatorScheduler scheduler: SchedulerRegistry.getAvailableSchedulers()) {
			System.err.println(scheduler.getKey());
		}
	}

}
