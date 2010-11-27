package com.pod2.elevator.core;

import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.view.data.SystemSnapShot;

/**
 * OVERVIEW: Graphically displays the state of an ActiveSimulation in a particular
 * 		quantum.
 */
public interface SimulationDisplay {

	public void startup(SimulationTemplate template);
	/**
	 *  REQUIRES: template != null
	 *  MODIFIES: this
	 *  EFFECTS: Initialize the simulation display with template in preparation of
	 *  	displaying a graphical representation of the time quantum of ActiveSimulation
	 */

	public void update(SystemSnapShot snapshot);
	/**
	 * 	REQUIRES: snapshot != null
	 * 	MODIFIES: this
	 * 	EFFECTS: Updates the simulation template with new snapshot for a new time
	 * 		quantum.
	 */

	public void teardown();
	/**
	 * 	MODIFIES: this
	 * 	EFFECTS: Terminates the graphical display for the state of a quantum
	 * 		in ActiveSimulation.
	 */

}
