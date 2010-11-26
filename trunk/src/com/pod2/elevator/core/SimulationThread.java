package com.pod2.elevator.core;

/**
 * OVERVIEW: A Thread which continually executes an ActiveSimulation.
 */
public class SimulationThread implements Runnable {

	public static long QUANTUM_MILLIS = 100;

	private ActiveSimulation simulation;
	
	// constructor
	public SimulationThread(ActiveSimulation simulation) {
		/**
		 * 	MODIFIES: this
		 * 	EFFECTS: Throw AssertionException if simulation is null.
		 * 		Initialize this.simulation with input parameter.
		 */
		assert (simulation != null);
		this.simulation = simulation;
	}

	public void run() {
		/**
		 * 	MODIFIES: this
		 * 	EFFECTS: Executes next quantum of simulation every 0.1 seconds.
		 * 		Stops when thread is interrupted.  Throws InterruptedException
		 * 		when thread is interrupted.
		 */
		while (true) {
			try {
				synchronized (simulation) {
					simulation.executeNextQuantum();
				}
				Thread.sleep(QUANTUM_MILLIS);
				if (Thread.interrupted()) {
					break;
				}
			} catch (InterruptedException e) {
				break;
			}
		}
	}

}
