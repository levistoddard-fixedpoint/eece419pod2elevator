package com.pod2.elevator.view.active;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.pod2.elevator.core.MotionStatus;
import com.pod2.elevator.core.ServiceStatus;
import com.pod2.elevator.core.component.ComponentDetails;
import com.pod2.elevator.core.component.ComponentRegistry;
import com.pod2.elevator.core.component.DoorDriveMechanism;
import com.pod2.elevator.core.component.DoorPositionContext;
import com.pod2.elevator.core.component.DoorSensor;
import com.pod2.elevator.core.component.DriveMechanism;
import com.pod2.elevator.core.component.ElevatorComponent;
import com.pod2.elevator.core.component.EmergencyBrake;
import com.pod2.elevator.core.component.PositionContext;
import com.pod2.elevator.core.component.PositionSensor;
import com.pod2.elevator.view.active.elevator.ElevatorView;
import com.pod2.elevator.view.active.status.StatusView;
import com.pod2.elevator.view.data.LogMessage;
import com.pod2.elevator.view.data.SystemSnapShot;

public class ActiveView extends JPanel{
	private ElevatorView elevatorView;
	private StatusView statusView;
	private JTextArea log;
	private Collection<ComponentDetails> components;
	
	private int numFloors=1;
	private int numElevators=1;
	
	public ActiveView(int numFloors, int numElevators){
		//Initialize Variables
		this.numFloors = numFloors;
		this.numElevators = numElevators;
		statusView = new StatusView(numFloors, numElevators);
		elevatorView = new ElevatorView(numFloors, numElevators, statusView);
		log = new JTextArea("Logging goes here\n");
		components = ComponentRegistry.getFailableComponents();
		
		//Set log properties
		log.setRows(5);
		log.setEditable(false);
		log.setBackground(Color.BLACK);
		log.setForeground(Color.WHITE);
		JScrollPane logScroll = new JScrollPane(log);
		
		//Set layout
		this.setLayout(new BorderLayout());
		
		//Add components
		this.add(elevatorView, BorderLayout.CENTER);
		this.add(statusView, BorderLayout.EAST);
		this.add(logScroll, BorderLayout.SOUTH);
	}

	public void statusUpdate(SystemSnapShot systemSnapShot){
		int eid = 0;
		double position = 0;
		Set<Integer> floorsOffLimit = null;
		int numberRequests = 0;
		int requestCapacity = 0;
		MotionStatus motionStatus = null; 
		ServiceStatus serviceStatus = null;
		TreeMap<String, Boolean> componentFailure = new TreeMap<String, Boolean>();
		String key;
		Boolean value;
		
		int fid;
		long quantum = systemSnapShot.getQuantum();
		int passengersWaiting;
		boolean isUpSelected;
		boolean isDownSelected;
		long upSelectedQuantum;
		long downSelectedQuantum;
		
		for(int i=0; i< numElevators; i++){
			eid = i;
			position = systemSnapShot.getElevatorSnapShot(i).getCurrentPosition();
			floorsOffLimit = systemSnapShot.getElevatorSnapShot(i).getFloorsOffLimit();
			numberRequests = systemSnapShot.getElevatorSnapShot(i).getNumberRequests();
			requestCapacity = systemSnapShot.getElevatorSnapShot(i).getRequestCapacity();
			motionStatus = systemSnapShot.getElevatorSnapShot(i).getMotionStatus();
			serviceStatus = systemSnapShot.getElevatorSnapShot(i).getServiceStatus();
			for (ComponentDetails component : components) {
				key = component.getKey();
				value = systemSnapShot.getElevatorSnapShot(i).getFailureStatus(key);
				componentFailure.put(key, value);
			}
			
			for(int j=0; j< numFloors; j++){
				fid = j;
				passengersWaiting = systemSnapShot.getFloorSnapShot(j).getPassengersWaiting();
				isUpSelected = systemSnapShot.getFloorSnapShot(j).getFloorRequestButton().isUpSelected();
				isDownSelected = systemSnapShot.getFloorSnapShot(j).getFloorRequestButton().isDownSelected();
				upSelectedQuantum = systemSnapShot.getFloorSnapShot(j).getFloorRequestButton().getUpSelectedQuantum();
				downSelectedQuantum = systemSnapShot.getFloorSnapShot(j).getFloorRequestButton().getDownSelectedQuantum();
				
				statusView.statusUpdate(eid, position, floorsOffLimit, numberRequests, requestCapacity, motionStatus, serviceStatus, componentFailure, fid, quantum, passengersWaiting, isUpSelected, isDownSelected, upSelectedQuantum, downSelectedQuantum);
			}
			
			elevatorView.statusUpdate(eid, position, floorsOffLimit, motionStatus, serviceStatus);
		}
		
		for(LogMessage l : systemSnapShot.getMessages()){
			log.append(l.toString()+"\n");
		}
		
	}

}
