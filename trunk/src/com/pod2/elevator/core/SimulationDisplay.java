package com.pod2.elevator.core;

import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.view.data.SystemSnapShot;

/**
 * OVERVIEW: Graphically displays the state of an ActiveSimulation in a particular
 * 		quantum.
 */
public interface SimulationDisplay {

	public void startup(SimulationTemplate template);

	public void update(SystemSnapShot snapshot);

	public void teardown();

}
