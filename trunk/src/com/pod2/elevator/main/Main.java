package com.pod2.elevator.main;

import com.pod2.elevator.view.SimulationView;
import com.pod2.elevator.view.SimulationWindow;

public class Main {

	public static void main(String[] args) {
		try {
			SimulationWindow window = new SimulationWindow();
			SimulationView view = new SimulationView(window);
			CentralController controller = new CentralController(view);
			window.setCentralController(controller);
			controller.start();
		} catch (Exception e) {
			System.err.print("Unable to start simulator:\n\t");
			System.err.print(e.getMessage());
		}
	}

}
