package com.pod2.elevator.view;

import javax.swing.SwingUtilities;

import com.pod2.elevator.core.SimulationDisplay;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.view.SimulationWindow;
import com.pod2.elevator.view.data.SystemSnapShot;

public class SimulationView implements SimulationDisplay {

	private final SimulationWindow window;

	public SimulationView(SimulationWindow window) {
		this.window = window;
	}

	public void startup(SimulationTemplate template) {
		window.startup(template.getNumberFloors(),
				template.getNumberElevators());
	}

	public void update(final SystemSnapShot snapshot) {
		window.statusUpdate(snapshot);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				window.statusUpdate(snapshot);
			}
		});
	}

	public void teardown() {
		window.teardown();
	}

}
