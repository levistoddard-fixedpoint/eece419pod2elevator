package com.pod2.elevator.scheduling;

import com.pod2.elevator.core.ActiveSimulation;

public interface ElevatorScheduler {

	public String getKey();

	public String getName();

	public String getDescription();

	public void schedule(ActiveSimulation simulation);
	
}
