package com.pod2.elevator.view;

import javax.swing.SwingUtilities;

import com.pod2.elevator.core.SimulationDisplay;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.view.SimulationWindow;
import com.pod2.elevator.view.SystemSnapShot;

public class SimulationView implements SimulationDisplay {

	private final SimulationWindow window;

	public SimulationView(SimulationWindow window) {
		this.window = window;
	}

	@Override
	public void startup(SimulationTemplate template) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(final SystemSnapShot snapshot) {
		window.statusUpdate(snapshot);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				window.statusUpdate(snapshot);
			}
		});
	}

	@Override
	public void teardown() {
		// TODO Auto-generated method stub

	}

}
