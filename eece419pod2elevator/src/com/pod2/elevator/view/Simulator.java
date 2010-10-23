package com.pod2.elevator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class Simulator extends JFrame implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4222236768731267728L;
	private int numFloor;
	private int numElevator;
	private int numComponent;

	public Simulator(int numFloor, int numElevator, int numComponent) {
		this.numFloor = numFloor;
		this.numElevator = numElevator;
		this.numComponent = numComponent;
		
		//Get screen size
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		
		setSize(size);
		setTitle("Simulator");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//Center on screen
		setLocation(size.width/2 - getWidth()/2, size.height/2 - getHeight()/2);
		
		//Add menu
		JMenuBar menubar = new Menu();
		setJMenuBar(menubar);
		
		//Add toolbar
		JToolBar toolbar = new Toolbar();
		add(toolbar, BorderLayout.NORTH);
		
		//Add main panel
		JPanel mainPanel = new MainPanel(numFloor, numElevator, numComponent);
		add(mainPanel, BorderLayout.CENTER);
		
		//Add system status
		JPanel systemStatus = new SystemStatus(numFloor);
		add(systemStatus, BorderLayout.EAST);
		
		pack();
		setVisible(true);
		setSize(size);
	}

	public void run() {
		//setSize(size);
	}

	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Simulator(10, 10, 5));
    }

}
