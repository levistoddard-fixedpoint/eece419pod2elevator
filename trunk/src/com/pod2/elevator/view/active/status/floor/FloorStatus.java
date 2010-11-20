package com.pod2.elevator.view.active.status.floor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pod2.elevator.view.layout.VerticalLayout;

public class FloorStatus extends JPanel{
	private JLabel idLabel;
	
	private boolean isUpSelected;
	private JLabel upLabel;
	private JLabel upDisplay;
	
	private int passengersWaiting;
	private JLabel passengersWaitingLabel;
	private JLabel passengersWaitingDisplay;
	
	private long upSelectedQuantum;
	private JLabel upQuantumLabel;
	private JLabel upQuantumDisplay;
	
	private boolean isDownSelected;
	private JLabel downLabel;
	private JLabel downDisplay;
	
	private long downSelectedQuantum;
	private JLabel downQuantumLabel;
	private JLabel downQuantumDisplay;
	
	private int id;
	private long quantum;
	private long maxWaitTime = 1;
	private int maxPassengersWaiting = 30;

	public FloorStatus(int id){
		this.id = id;
		
		idLabel = createLabel(0, new String("Floor " + id));
		idLabel.setForeground(Color.BLUE);
		
		passengersWaitingLabel = createLabel(0, "Passengers Waiting");
		passengersWaitingDisplay = createLabel(1, "");
		
		upLabel = createLabel(0, "Up Request");
		upDisplay = createLabel(1, "");
		
		upQuantumLabel = createLabel(0, "Time Requested");
		upQuantumDisplay = createLabel(1, "");
		
		downLabel = createLabel(0, "Down Request");
		downDisplay = createLabel(1, "");
		
		downQuantumLabel = createLabel(0, "Time Requested");
		downQuantumDisplay = createLabel(1, "");
		
		this.add(idLabel);
		this.add(passengersWaitingLabel);
		this.add(passengersWaitingDisplay);
		this.add(upLabel);
		this.add(upDisplay);
		this.add(upQuantumLabel);
		this.add(upQuantumDisplay);
		this.add(downLabel);
		this.add(downDisplay);
		this.add(downQuantumLabel);
		this.add(downQuantumDisplay);
		
		this.setLayout(new VerticalLayout());
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
	}
	
	protected void statusUpdate(long quantum, int passengersWaiting, boolean isUpSelected, boolean isDownSelected, long upSelectedQuantum, long downSelectedQuantum){
		this.quantum = quantum;
		this.passengersWaiting = passengersWaiting;
		this.isUpSelected = isUpSelected;
		this.isDownSelected = isDownSelected;
		this.upSelectedQuantum = upSelectedQuantum;
		this.downSelectedQuantum = downSelectedQuantum;
		
		if(passengersWaiting > maxPassengersWaiting){
			passengersWaitingDisplay.setForeground(Color.RED);
		}else {
			passengersWaitingDisplay.setForeground(Color.GREEN);
		}
		passengersWaitingDisplay.setText(Integer.toString(passengersWaiting));
		if(isUpSelected){
			upDisplay.setForeground(Color.GREEN);
			upDisplay.setText("Requested");
		}else {
			upDisplay.setForeground(Color.WHITE);
			upDisplay.setText("-");
		}
		if(isDownSelected){
			downDisplay.setForeground(Color.GREEN);
			downDisplay.setText("Requested");
		}else {
			downDisplay.setForeground(Color.WHITE);
			downDisplay.setText("-");
		}
		if(quantum > upSelectedQuantum + maxWaitTime){
			upQuantumDisplay.setForeground(Color.RED);
		}else {
			upQuantumDisplay.setForeground(Color.GREEN);
		}
		upQuantumDisplay.setText(Long.toString(upSelectedQuantum));
		if(quantum > downSelectedQuantum + maxWaitTime){
			downQuantumDisplay.setForeground(Color.RED);
		}else {
			downQuantumDisplay.setForeground(Color.GREEN);
		}
		downQuantumDisplay.setText(Long.toString(downSelectedQuantum));
	}
	
	private JLabel createLabel(int type, String s){
		JLabel temp = new JLabel(s);
		if(type==0){
			temp.setBorder(BorderFactory.createRaisedBevelBorder());
			temp.setBackground(Color.WHITE);
			temp.setFont(new Font("Dialog", Font.BOLD, 16));
		}else {
			temp.setBorder(BorderFactory.createLoweredBevelBorder());
			temp.setForeground(Color.WHITE);
			temp.setBackground(Color.BLACK);
			temp.setFont(new Font("Dialog", Font.PLAIN, 16));
		}
		temp.setHorizontalAlignment(JLabel.CENTER);
		temp.setPreferredSize(new Dimension(160, 30));
		temp.setOpaque(true);
		
		return temp;
	}
}
