package com.pod2.elevator.scheduling;

import com.pod2.elevator.core.ActiveSimulation;

/**
 * Used by the ActiveSimulation to make all Elevator scheduling policy
 * decisions.
 */
public interface ElevatorScheduler {

	public String getKey();

	public String getName();

	public String getDescription();

	public void schedule(ActiveSimulation simulation);

}
