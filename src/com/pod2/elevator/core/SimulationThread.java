package com.pod2.elevator.core;

public class SimulationThread implements Runnable {

	public static long QUANTUM_MILLIS = 100;

	private ActiveSimulation activeSimulation;

	public SimulationThread(ActiveSimulation activeSimulation) {
		this.activeSimulation = activeSimulation;
	}

	public void run() {
		while (true) {
			try {
				activeSimulation.executeNextQuantum();
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
