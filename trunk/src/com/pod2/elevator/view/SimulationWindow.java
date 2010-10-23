package com.pod2.elevator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

public class SimulationWindow extends JFrame implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6664795648746130396L;
	private int numFloor;
	private int numElevator;
	private int numComponent;
	private JMenuBar menubar;
	private JToolBar toolbar;
	private JTabbedPane tabPane;
	private JComponent simulator;
	
	public SimulationWindow(int numFloor, int numElevator, int numComponent) {
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
		menubar = new Menu();
		setJMenuBar(menubar);
		
		//Add toolbar
		toolbar = new Toolbar();
		add(toolbar, BorderLayout.NORTH);
		
		//Add tabs
		tabPane = new JTabbedPane();
		tabPane.setPreferredSize(new Dimension(800, 600));
		simulator = new Simulation(numFloor, numElevator, numComponent);
		JComponent test = new JPanel();
		tabPane.addTab("Simulator", simulator);
		tabPane.addTab("Test", test);
		add(tabPane, BorderLayout.CENTER);
		
		pack();
		setVisible(true);
		setSize(size);
	}

	public void run() {
		
	}
	
	public void statusUpdate(SystemSnapShot s){
		//TODO: Update simulator
	}

	public static void main(String[] args) {
		SimulationWindow sim = new SimulationWindow(10, 10, 5);
        javax.swing.SwingUtilities.invokeLater(sim);
        sim.statusUpdate(new SystemSnapShot());
    }
	
}
