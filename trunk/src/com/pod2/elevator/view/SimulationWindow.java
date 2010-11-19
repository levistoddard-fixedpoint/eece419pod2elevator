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

public class SimulationWindow extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6664795648746130396L;
	private int numFloors;
	private int numElevators;
	private int numComponents;
	private JMenuBar menubar;
	private JToolBar toolbar;
	private JTabbedPane tabPane;
	private ActiveView activeView;

	public SimulationWindow(int numFloors, int numElevators, int numComponents) {
		this.numFloors = numFloors;
		this.numElevators = numElevators;
		this.numComponents = numComponents;

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
		tabPane.setPreferredSize(new Dimension(800, 600));
		ActiveView.init(numFloors, numElevators, numComponents);
		activeView = ActiveView.getActiveView();
		JComponent test = new JPanel();
		tabPane.addTab("Simulator", activeView);
		tabPane.addTab("Test", test);
		add(tabPane, BorderLayout.CENTER);

		pack();
		setVisible(true);
		setSize(size);
	}

	public void run() {

	}

	public void statusUpdate(SystemSnapShot s) {
		activeView.statusUpdate(s);
	}

	public static void main(String[] args) {
		SimulationWindow sim = new SimulationWindow(5, 4, 4);
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
