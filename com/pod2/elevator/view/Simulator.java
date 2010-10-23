package com.pod2.elevator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;

public class Simulator extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4222236768731267728L;
	private int numFloor;
	private int numElevator;
	private int numComponent;

	private MainPanel mainPanel;
	private SystemStatus systemStatus;

	public Simulator(int numFloor, int numElevator, int numComponent) {
		this.numFloor = numFloor;
		this.numElevator = numElevator;
		this.numComponent = numComponent;
		
		setBackground(Color.BLUE);
		setLayout(new BorderLayout());
		
		//Add main panel
		mainPanel = new MainPanel(numFloor, numElevator, numComponent);
		add(mainPanel, BorderLayout.CENTER);
		
		//Add system status
		systemStatus = new SystemStatus(numFloor);
		add(systemStatus, BorderLayout.EAST);
	}
	
	public void statusUpdate(SystemSnapShot s){
		//TODO: Update main panel, system status
	}

}
