package com.pod2.elevator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import com.pod2.elevator.core.FloorRequestButton;
import com.pod2.elevator.core.MotionStatus;
import com.pod2.elevator.core.ServiceStatus;
import com.pod2.elevator.core.component.DoorDriveMechanism;
import com.pod2.elevator.core.component.DoorSensor;
import com.pod2.elevator.core.component.DriveMechanism;
import com.pod2.elevator.core.component.ElevatorComponent;
import com.pod2.elevator.core.component.PositionSensor;
import com.pod2.elevator.view.active.ActiveView;
import com.pod2.elevator.view.analysis.AnalysisView;
import com.pod2.elevator.view.configuration.ConfigurationView;
import com.pod2.elevator.view.data.ElevatorSnapShot;
import com.pod2.elevator.view.data.FloorSnapShot;
import com.pod2.elevator.view.data.LogMessage;
import com.pod2.elevator.view.data.SystemSnapShot;
import com.pod2.elevator.view.model.Menu;
import com.pod2.elevator.view.model.Toolbar;

public class SimulationWindow extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6664795648746130396L;
	private int numFloors;
	private int numElevators;
	private JMenuBar menubar;
	private JToolBar toolbar;
	private JTabbedPane tabPane;
	private ActiveView activeView;
	private AnalysisView analysisView;
	private ConfigurationView configurationView;
	private SystemSnapShot prevSystemSnapShot;

	public SimulationWindow(int numFloors, int numElevators) {
		this.numFloors = numFloors;
		this.numElevators = numElevators;

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

		// Add menu
		menubar = new Menu();
		setJMenuBar(menubar);

		// Add toolbar
		toolbar = new Toolbar();
		add(toolbar, BorderLayout.NORTH);

		// Add tabs
		tabPane = new JTabbedPane();
		tabPane.setPreferredSize(new Dimension(640, 480));
		activeView = new ActiveView(numFloors, numElevators);
		analysisView = new AnalysisView();
		configurationView = new ConfigurationView();
		tabPane.addTab("Active Simulation", activeView);
		tabPane.addTab("Analysis", analysisView);
		tabPane.addTab("Configuration", configurationView);
		add(tabPane, BorderLayout.CENTER);

		pack();
		setVisible(true);
	}

	public void run() {

	}

	public void statusUpdate(SystemSnapShot systemSnapShot) {
		if(prevSystemSnapShot == null || !prevSystemSnapShot.equals(systemSnapShot)){
			activeView.statusUpdate(systemSnapShot);
		}
		prevSystemSnapShot = systemSnapShot;
	}

	public static void main(String[] args) {
		SimulationWindow sim = new SimulationWindow(5, 4);
		javax.swing.SwingUtilities.invokeLater(sim);

		FloorSnapShot[] floors = new FloorSnapShot[5];
		for (int n = 0; n < 5; n++) {
			FloorRequestButton button = new FloorRequestButton();
			floors[n] = new FloorSnapShot(button, n);
		}

		ElevatorSnapShot[] elevators = new ElevatorSnapShot[4];
		for (int n = 0; n < 4; n++) {
			List<ElevatorComponent> components = new LinkedList<ElevatorComponent>();
			components.add(new DoorSensor(null, 4.0));
			components.add(new DoorDriveMechanism(null, 4.0));
			components.add(new PositionSensor(null));
			components.add(new DriveMechanism(null, 4.0));
			elevators[n] = new ElevatorSnapShot((double) n,
					new HashSet<Integer>(), n + 2, n + 4,
					MotionStatus.DoorsOpen, ServiceStatus.InService, components);
		}

		LogMessage[] messages = new LogMessage[5];
		for (int n = 0; n < 5; n++) {
			messages[n] = new LogMessage("A test message");
		}

		SystemSnapShot snapshot = new SystemSnapShot(100, elevators, floors,
				Arrays.asList(messages));
		sim.statusUpdate(snapshot);

	}

}
