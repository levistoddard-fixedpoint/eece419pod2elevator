package com.pod2.elevator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;

public class Simulation extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4222236768731267728L;
	private int numFloor;
	private int numElevator;
	private int numComponent;

	private MainPanel mainPanel;
	private SystemStatus systemStatus;

	public Simulation(int numFloor, int numElevator, int numComponent) {
		this.numFloor = numFloor;
		this.numElevator = numElevator;
		this.numComponent = numComponent;

		setBackground(Color.BLUE);
		setLayout(new BorderLayout());

		// Add main panel
		mainPanel = new MainPanel(numFloor, numElevator, numComponent);
		add(mainPanel, BorderLayout.CENTER);

		// Add system status
		systemStatus = new SystemStatus(numFloor);
		add(systemStatus, BorderLayout.EAST);
	}

	public void statusUpdate(SystemSnapShot s) {
		mainPanel.statusUpdate(s);
		systemStatus.statusUpdate(s);
	}

}
