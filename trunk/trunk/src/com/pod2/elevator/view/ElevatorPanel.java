package com.pod2.elevator.view;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JPanel;

public class ElevatorPanel extends JPanel{
	
	private Canvas elevator;
	
	public ElevatorPanel(int numComponent) {
		setBackground(Color.CYAN);
		setLayout(new BorderLayout());
		elevator = new Elevator(numComponent);
		add(elevator, BorderLayout.CENTER);
	}

}
