package com.pod2.elevator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import com.pod2.elevator.main.CentralController;
import com.pod2.elevator.view.active.ActiveView;
import com.pod2.elevator.view.analysis.AnalysisView;
import com.pod2.elevator.view.configuration.ConfigurationView;
import com.pod2.elevator.view.data.SystemSnapShot;

public class SimulationWindow extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6664795648746130396L;
	private JTabbedPane tabPane;
	private ActiveView activeView;
	private AnalysisView analysisView;
	private ConfigurationView configurationView;
	private SystemSnapShot prevSystemSnapShot;

	public SimulationWindow() {
		// Get screen size
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();

		setSize(size);
		setTitle("Simulator");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Center on screen
		setLocation(size.width / 2 - getWidth() / 2, size.height / 2
				- getHeight() / 2);

		// Add tabs
		tabPane = new JTabbedPane();
		tabPane.setPreferredSize(new Dimension(640, 480));
		activeView = new ActiveView();
		analysisView = new AnalysisView();
		configurationView = new ConfigurationView();
		tabPane.addTab("Active Simulation", activeView);
		tabPane.addTab("Analysis", analysisView);
		tabPane.addTab("Configuration", configurationView);
		add(tabPane, BorderLayout.CENTER);

		pack();
		setVisible(true);
	}

	public void startup(int numFloors, int numElevators, String scheduler) {
		activeView = new ActiveView(numFloors, numElevators, scheduler);
		tabPane.setComponentAt(0, activeView);
	}

	public void teardown() {
		activeView = new ActiveView();
		tabPane.setComponentAt(0, activeView);
	}

	public void run() {

	}

	public void statusUpdate(SystemSnapShot systemSnapShot) {
		if (prevSystemSnapShot == null
				|| !prevSystemSnapShot.equals(systemSnapShot)) {
			activeView.statusUpdate(systemSnapShot);
		}
		prevSystemSnapShot = systemSnapShot;
	}

	public void setCentralController(CentralController centralController) {
		configurationView.setCentralController(centralController);
	}

}
