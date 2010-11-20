package com.pod2.elevator.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import com.pod2.elevator.core.MotionStatus;
import com.pod2.elevator.core.ServiceStatus;
import com.pod2.elevator.view.layout.VerticalLayout;
import com.pod2.elevator.view.panel.ElevatorStatusPanel;
import com.pod2.elevator.view.panel.FloorStatusPanel;

public class StatusView extends JPanel implements ActionListener{
	private ElevatorStatusPanel elevatorStatus[];
	private int numElevators;
	
	private FloorStatusPanel floorStatus;
	private int numFloors;
	
	private JScrollPane scrollPane;
	private JPanel rootPanel;
	
	private JButton passengerButton;
	private JLabel quantumLabel;
	
	public StatusView(int numFloors, int numElevators){
		//Initialize Variables
		this.numElevators=numElevators;
		elevatorStatus = new ElevatorStatusPanel[numElevators];
		passengerButton = new JButton("Floor Status");
		passengerButton.addActionListener(this);
		quantumLabel = new JLabel("Quantum: 0");
		
		//Root Panel
		rootPanel = new JPanel();
		
		//ScrollPanel
		scrollPane = new JScrollPane(rootPanel);
		scrollPane.setPreferredSize(new Dimension(200, 720));
		scrollPane.setAutoscrolls(false);
		
		//Elevator Status Panel
		for(int i=0; i<numElevators; i++){
			elevatorStatus[i] = new ElevatorStatusPanel(i);
			elevatorStatus[i].setVisible(false);
			rootPanel.add(elevatorStatus[i]);
		}
		
		//Floor Status Panel
		floorStatus = new FloorStatusPanel(numFloors);
		floorStatus.setVisible(true);
		rootPanel.add(floorStatus);
		
		//Add components
		this.add(quantumLabel);
		this.add(passengerButton);
		this.add(scrollPane);
		
		this.setLayout(new VerticalLayout());
	}
	
	protected void showElevatorStatus(int id){
		floorStatus.setVisible(false);
		for(ElevatorStatusPanel s : elevatorStatus){
			s.setVisible(false);
		}
		elevatorStatus[id].setVisible(true);
	}
	
	private void showFloorStatus(){
		for(ElevatorStatusPanel s : elevatorStatus){
			s.setVisible(false);
		}
		floorStatus.setVisible(true);
	}
	
	public void paint(Graphics g){
		Dimension size = this.getSize();
		size.height -= 50;
		scrollPane.setPreferredSize(size);
		super.paint(g);
	}
	
	protected void statusUpdate(int eid, double position, Set<Integer> floorsOffLimit, int numberRequests, int requestCapacity, MotionStatus motionStatus, 
							 ServiceStatus serviceStatus, TreeMap<String, Boolean> componentFailure,
							 int fid, long quantum, int passengersWaiting, boolean isUpSelected, boolean isDownSelected, long upSelectedQuantum, long downSelectedQuantum){
		elevatorStatus[eid].statusUpdate(position, floorsOffLimit, numberRequests, requestCapacity, motionStatus, serviceStatus, componentFailure);
		floorStatus.statusUpdate(fid ,quantum, passengersWaiting, isUpSelected, isDownSelected, upSelectedQuantum, downSelectedQuantum);
		quantumLabel.setText("Quantum: " + Long.toString(quantum));
	}

	public void actionPerformed(ActionEvent e) {
		showFloorStatus();
	}

}
