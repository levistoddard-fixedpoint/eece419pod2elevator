package com.pod2.elevator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.pod2.elevator.core.MotionStatus;
import com.pod2.elevator.core.ServiceStatus;

public class ElevatorPanel extends JPanel implements ActionListener{
	private JScrollPane scrollPane;
	private JPanel rootPanel;
	
	private JPanel floorPanel;
	private JLabel floors[];
	
	private Elevator elevator;
	private JButton status;
	
	private int y;
	private int id;
	private int numFloors;
	private double position;
	private ServiceStatus serviceStatus;
	
	public ElevatorPanel(int id, int numFloors){
		//Init variables
		this.id = id;
		this.numFloors = numFloors;
		Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());
		
		//Display elevator number
		status = new JButton();
		status.setText("Elevator: " + Integer.toString(id));
		status.addActionListener(this);
		
		//Display elevator
		elevator = new Elevator(numFloors, 5);
		elevator.setPreferredSize(new Dimension(100,0));
		elevator.setBackground(Color.BLACK);
		
		//Display floor number
		floorPanel = new JPanel();
		floorPanel.setLayout(new BoxLayout(floorPanel, BoxLayout.Y_AXIS));
		
		//Root panel
		rootPanel = new JPanel();
		rootPanel.setLayout(new BorderLayout());
		rootPanel.setBackground(Color.BLUE);
		rootPanel.add(floorPanel, BorderLayout.EAST);
		rootPanel.add(elevator, BorderLayout.WEST);
		rootPanel.revalidate();
		
		//Scrollpane
		scrollPane = new JScrollPane(rootPanel);
		scrollPane.setPreferredSize(new Dimension(152,40*5));
		
		//Labels for floor numbers
		floors = new JLabel[numFloors];
		for(int i=numFloors - 1; i>=0; i--){
			floors[i] = new JLabel(Integer.toString(i));
			floors[i].setFont(new Font("Dialog", Font.PLAIN, 30));
			floorPanel.add(floors[i]);
		}
		
		//This Panel
		this.setBackground(Color.LIGHT_GRAY);
		this.setLayout(new VerticalLayout());
		this.add(status);
		this.add(scrollPane);
	}

	public void paint(Graphics g){
		rootPanel.revalidate();
		super.paint(g);
		
		//Set view
		if(serviceStatus != ServiceStatus.Failed){
			y = (int) (39 * (numFloors - position - 1));
		}
		scrollPane.getVerticalScrollBar().setValue(y);
		
		//Repaint elevator
		elevator.repaint();
	}
	
	protected void statusUpdate(double position, Set<Integer> floorsOffLimit, MotionStatus motionStatus, ServiceStatus serviceStatus){
		this.position = position;
		this.serviceStatus = serviceStatus;
		//Update Elevator	
		elevator.statusUpdate(position, floorsOffLimit, motionStatus, serviceStatus);
		this.repaint();
	}

	public void actionPerformed(ActionEvent e) {
		ActiveView.showElevatorStatus(id);
	}

}