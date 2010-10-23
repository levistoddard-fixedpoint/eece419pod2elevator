package com.pod2.elevator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class MainPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1268320896559054535L;
	private int numFloor;
	private int numElevator;
	private int numComponent;
	
	private JPanel positionPanel = new JPanel();
	private JButton positions[];
	
	private JPanel elevatorsPanel = new JPanel();
	private JPanel elevators[];
	static private JFrame elevatorStatus[];
	
	private JPanel logPanel = new JPanel();
	private JTextArea log = new JTextArea("Logging goes here");
	
	public MainPanel(int numFloor, int numElevator, int numComponent) {
		
		this.numFloor = numFloor;
		this.numElevator = numElevator;
		this.numComponent = numComponent;
		positions = new JButton[numFloor];
		elevators = new ElevatorPanel[numElevator];
		elevatorStatus = new JFrame[numElevator];
		
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		setLayout(new BorderLayout());
		
		//Add position panel
		positionPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		positionPanel.setLayout(new GridLayout(0, numElevator, 5, 5));
		
		for(int i=0; i<numElevator; i++){
			positions[i] = new JButton("5");
			positions[i].addMouseListener(new ElevatorAction(i, numComponent));
			positions[i].setPreferredSize(new Dimension(0, 30));
			positions[i].setHorizontalAlignment(JTextField.CENTER);
			positions[i].setToolTipText("Elevator Position");
			positionPanel.add(positions[i]);
			this.add(positionPanel, BorderLayout.NORTH);
		}
		
		//Add elevators panel
		elevatorsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		elevatorsPanel.setLayout(new GridLayout(0, numElevator, 5, 5));
		elevatorsPanel.setBackground(Color.BLUE);
		
		for(int i=0; i<numElevator; i++){	
			//Add elevator panels
			elevators[i] = new ElevatorPanel(numComponent);
			elevatorsPanel.add(elevators[i]);
		}
		this.add(elevatorsPanel, BorderLayout.CENTER);
		
		//Add log panel
		logPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		logPanel.setLayout(new BorderLayout());
		log.setBackground(Color.GREEN);
		log.setPreferredSize(new Dimension(0, 100));
		log.setAlignmentX(LEFT_ALIGNMENT);
		logPanel.add(log, BorderLayout.CENTER);
		this.add(logPanel, BorderLayout.SOUTH);
	}

	static public void addElevatorStatus (ElevatorStatus e){
		int i = e.getId();
		elevatorStatus[i] = e;
	}
}
