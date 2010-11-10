package com.pod2.elevator.core;

public class SimulationThread implements Runnable {

	private long sleepMillis = 1000;
	private ActiveSimulation activeSimulation;

	public SimulationThread(ActiveSimulation activeSimulation) {
		this.activeSimulation = activeSimulation;
	}

	public void run() {
		while (true) {
			try {
				activeSimulation.executeNextQuantum();
				Thread.sleep(sleepMillis);
				if (Thread.interrupted()) {
					break;
				}
			} catch (InterruptedException e) {
				break;
			}
		}
	}

}
