package com.pod2.elevator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;

public class ElevatorPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6753321753497119457L;
	
	private int numFloor;
	private int numComponent;
	private Elevator elevator;
	
	public ElevatorPanel(int numFloor, int numComponent) {
		this.numFloor = numFloor;
		this.numComponent = numComponent;
		
		setBackground(Color.CYAN);
		setLayout(new BorderLayout());
		elevator = new Elevator(numFloor, numComponent);
		add(elevator, BorderLayout.CENTER);
	}
	
	public void statusUpdate(SystemSnapShot s){
		//TODO: Update elevator image and position (guarantee elevator cannot go out of bound)
	}

}
