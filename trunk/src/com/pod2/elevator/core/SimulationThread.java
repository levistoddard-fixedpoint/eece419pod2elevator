package com.pod2.elevator.core;

/**
 * A Thread which continually executes an ActiveSimulation.
 * 
 */
public class SimulationThread implements Runnable {

	public static long QUANTUM_MILLIS = 100;

	private ActiveSimulation simulation;

	public SimulationThread(ActiveSimulation simulation) {
		assert (simulation != null);
		this.simulation = simulation;
	}

	public void run() {
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
