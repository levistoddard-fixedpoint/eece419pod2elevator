package com.pod2.elevator.view;

import com.pod2.elevator.core.*;

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
	private JComponent simulation;
	
	public SimulationWindow(int numFloor, int numElevator, int numComponent) {
		this.numFloor = numFloor;
		this.numElevator = numElevator;
		this.numComponent = numComponent;
		
		//Get screen size
		Toolkit toolkit = getToolkit();
		Dimension size = toolkit.getScreenSize();
		
		setSize(size);
		setTitle("Simulator");
		setLayout(new BorderLayout());
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
		tabPane.setPreferredSize(new Dimension(800,600));
		simulation = new Simulation(numFloor, numElevator, numComponent);
		JComponent test = new JPanel();
		tabPane.addTab("Simulator", simulation);
		tabPane.addTab("Test", test);
		add(tabPane, BorderLayout.CENTER);
		
		pack();
		setVisible(true);
		setSize(size);
	}

	public void run() {
		
	}
	
	public void statusUpdate(SystemSnapShot s){
		((Simulation) simulation).statusUpdate(s);
	}

	public static void main(String[] args) {
		SimulationWindow sim = new SimulationWindow(10, 10, 5);
        javax.swing.SwingUtilities.invokeLater(sim);
        SystemSnapShot test = new SystemSnapShot(10, 10, 5);
        test.quantum = 10;

        for(int i=0; i<10; i++){

        	test.elevatorSnapShot[i].currentPosition = 4;
        	test.elevatorSnapShot[i].requestCount = 5;
        	test.elevatorSnapShot[i].motionStatus = MotionStatus.MovingUp;
        	test.elevatorSnapShot[i].serviceStatus = ServiceStatus.InService;
        	test.elevatorSnapShot[i].requestCapacity = 10;

        	test.floorSnapShot.floorQueues[i] = 6;
        	test.floorSnapShot.floorRequestButtons[i] = new FloorRequestButton(true, false, 5, 5);
        }
        
        for(int i=0; i<5; i++){
        	test.messages[i] = new LogMessage(2, "Test", "Test Log Message");
        }
        sim.statusUpdate(test);
    }
	
}
