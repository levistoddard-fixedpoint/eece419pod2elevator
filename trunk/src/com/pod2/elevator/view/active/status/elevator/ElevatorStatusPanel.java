package com.pod2.elevator.view.active.status.elevator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.pod2.elevator.core.MotionStatus;
import com.pod2.elevator.core.ServiceStatus;
import com.pod2.elevator.view.layout.VerticalLayout;

public class ElevatorStatusPanel extends JPanel{
	private int id;
	
	private JLabel idLabel;
	
	private double currentPosition;
	private JLabel currentPositionLabel;
	private JLabel currentPositionDisplay;
	
	private Set<Integer> floorsOffLimit;
	private JLabel floorsOffLimitLabel;
	private JTextArea floorsOffLimitDisplay;
	private JScrollPane floorsOffLimitScroll;
	
	private int numberRequests;
	private JLabel numberRequestLabel;
	private JLabel numberRequestDisplay;
	
	private int requestCapacity;
	private JLabel requestCapacityLabel;
	private JLabel requestCapacityDisplay;
	
	private MotionStatus motionStatus;
	private JLabel motionStatusLabel;
	private JLabel motionStatusDisplay;
	
	private ServiceStatus serviceStatus;
	private JLabel serviceStatusLabel;
	private JLabel serviceStatusDisplay;
	
	private TreeMap<String, Boolean> componentFailure;
	private JLabel componentFailureLabel;
	private JTextArea componentFailureDisplay;
	private JScrollPane componentFailureScroll;
	
	public ElevatorStatusPanel(int id){
		this.id = id;
		
		this.setLayout(new VerticalLayout());
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		this.setBackground(Color.LIGHT_GRAY);
		
		idLabel = createLabel(0, new String("Elevator " + id));
		
		currentPositionLabel = createLabel(0, "Current Position");
		floorsOffLimitLabel = createLabel(0, "Floors Offlimit");
		numberRequestLabel = createLabel(0, "Requests");
		requestCapacityLabel = createLabel(0, "Request Capacity");
		motionStatusLabel = createLabel(0, "Motion Status");
		serviceStatusLabel = createLabel(0, "Service Status");
		componentFailureLabel = createLabel(0, "Component Status");
		
		currentPositionDisplay = createLabel(1, "");
		floorsOffLimitDisplay = createTextArea();
		numberRequestDisplay = createLabel(1, "");
		requestCapacityDisplay = createLabel(1, "");
		motionStatusDisplay = createLabel(1, "");
		serviceStatusDisplay = createLabel(1, "");
		componentFailureDisplay = createTextArea();
		
		floorsOffLimitScroll = new JScrollPane(floorsOffLimitDisplay);
		componentFailureScroll = new JScrollPane(componentFailureDisplay);
		
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(idLabel);
		
		this.add(currentPositionLabel);
		this.add(currentPositionDisplay);

		this.add(floorsOffLimitLabel);
		this.add(floorsOffLimitScroll);
		
		this.add(numberRequestLabel);
		this.add(numberRequestDisplay);
		
		this.add(requestCapacityLabel);
		this.add(requestCapacityDisplay);
		
		this.add(motionStatusLabel);
		this.add(motionStatusDisplay);
		
		this.add(serviceStatusLabel);
		this.add(serviceStatusDisplay);
		
		this.add(componentFailureLabel);
		this.add(componentFailureScroll);

	}
	
	private JLabel createLabel(int type, String s){
		JLabel temp = new JLabel(s);
		if(type==0){
			temp.setBorder(BorderFactory.createRaisedBevelBorder());
			temp.setBackground(Color.WHITE);
			temp.setFont(new Font("Dialog", Font.BOLD, 14));
		}else {
			temp.setBorder(BorderFactory.createLoweredBevelBorder());
			temp.setForeground(Color.GREEN);
			temp.setBackground(Color.BLACK);
			temp.setFont(new Font("Dialog", Font.PLAIN, 14));
		}
		temp.setHorizontalAlignment(JLabel.CENTER);
		temp.setPreferredSize(new Dimension(160, 30));
		temp.setOpaque(true);
		
		return temp;
	}
	
	private JTextArea createTextArea(){
		JTextArea temp = new JTextArea();
		temp.setBorder(BorderFactory.createLoweredBevelBorder());
		temp.setFont(new Font("Dialog", Font.PLAIN, 14));
		temp.setPreferredSize(new Dimension(160, 80));
		temp.setBackground(Color.BLACK);
		temp.setForeground(Color.GREEN);
		temp.setRows(5);
		temp.setLineWrap(true);
		temp.setEditable(false);
		return temp;
	}
	
	public void statusUpdate(double position, Set<Integer> floorsOffLimit, int numberRequests, int requestCapacity, MotionStatus motionStatus, ServiceStatus serviceStatus, TreeMap<String, Boolean> componentFailure){
		this.currentPosition = position;
		this.floorsOffLimit = floorsOffLimit;
		this.numberRequests = numberRequests;
		this.requestCapacity = requestCapacity;
		this.motionStatus = motionStatus;
		this.serviceStatus = serviceStatus;
		this.componentFailure = componentFailure;
		
		currentPositionDisplay.setText(Double.toString(position));
		
		floorsOffLimitDisplay.setText("");
		floorsOffLimitDisplay.setPreferredSize(new Dimension(160, floorsOffLimit.size()*21+3));
		for(Integer i : floorsOffLimit){
			floorsOffLimitDisplay.append(i.toString() + " \n");
		}
		if(numberRequests > requestCapacity){
			numberRequestDisplay.setForeground(Color.RED);
		}else {
			numberRequestDisplay.setForeground(Color.GREEN);
		}
		numberRequestDisplay.setText(Integer.toString(numberRequests));
		requestCapacityDisplay.setText(Integer.toString(requestCapacity));
		
		if(motionStatus == MotionStatus.MovingDown || motionStatus == MotionStatus.MovingUp){
			motionStatusDisplay.setForeground(Color.GREEN);
			currentPositionDisplay.setForeground(Color.GREEN);
		}else {
			motionStatusDisplay.setForeground(Color.YELLOW);
			currentPositionDisplay.setForeground(Color.YELLOW);
		}
		if(motionStatus == MotionStatus.ReachedDestinationFloor){
			motionStatusDisplay.setText("Arrived");
		}else {
			motionStatusDisplay.setText(motionStatus.toString());
		}
		
		if(serviceStatus == ServiceStatus.Failed || serviceStatus == ServiceStatus.OutOfService){
			serviceStatusDisplay.setForeground(Color.RED);
		}else {
			serviceStatusDisplay.setForeground(Color.GREEN);
		}
		serviceStatusDisplay.setText(serviceStatus.toString());
		
		componentFailureDisplay.setText("");
		componentFailureDisplay.setPreferredSize(new Dimension(142, componentFailure.size()*2*21+3));
		componentFailureDisplay.setForeground(Color.GREEN);
		for(Map.Entry<String, Boolean> entry : componentFailure.entrySet()){
			String key = entry.getKey();
			String[] keys = key.split("com.pod2.elevator.core.component.");
			if(keys.length > 0){
				key = keys[keys.length-1];
			}
			Boolean value = entry.getValue();
			if(value){
				componentFailureDisplay.setForeground(Color.RED);
				componentFailureDisplay.append(key.toString() + "\nFAILED" + "\n");
			}else {
				componentFailureDisplay.append(key.toString() + "\nOK" + "\n");
			}
		}
	}
	
}
