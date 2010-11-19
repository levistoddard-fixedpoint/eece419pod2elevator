package com.pod2.elevator.view;

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
import com.pod2.elevator.core.component.DoorDriveMechanism;
import com.pod2.elevator.core.component.DoorPositionContext;
import com.pod2.elevator.core.component.DoorSensor;
import com.pod2.elevator.core.component.DriveMechanism;
import com.pod2.elevator.core.component.ElevatorComponent;
import com.pod2.elevator.core.component.EmergencyBrake;
import com.pod2.elevator.core.component.PositionContext;
import com.pod2.elevator.core.component.PositionSensor;

public class ActiveView extends JPanel{
	static private ElevatorView elevatorView;
	static private StatusView statusView;
	static private JTextArea log;
	
	static private int numFloors=1;
	static private int numElevators=1;
	static private int numComponents=1;
	
	static private ActiveView activeView;
	
	static public ActiveView getActiveView(){
		if (activeView == null){
			activeView = new ActiveView(numFloors, numElevators, numComponents);
		}
		return activeView;
	}
	
	private ActiveView(int numFloors, int numElevators, int numComponents){
		elevatorView = new ElevatorView(numFloors, numElevators);
		statusView = new StatusView(numFloors, numElevators);
		log = new JTextArea("Logging goes here\n");
		log.setRows(5);
		log.setEditable(false);
		log.setBackground(Color.BLACK);
		log.setForeground(Color.WHITE);
		JScrollPane logScroll = new JScrollPane(log);
		
		this.setLayout(new BorderLayout());
		
		this.add(elevatorView, BorderLayout.CENTER);
		this.add(statusView, BorderLayout.EAST);
		this.add(logScroll, BorderLayout.SOUTH);
	}
	
	static public void init(int numFloors, int numElevators, int numComponents){
		ActiveView.numFloors = numFloors;
		ActiveView.numElevators = numElevators;
		ActiveView.numComponents = numComponents;
	}
	
	protected static void showElevatorStatus(int id){
		statusView.showElevatorStatus(id);
	}

	protected void statusUpdate(SystemSnapShot systemSnapShot){
		int eid;
		double position = 0;
		Set<Integer> floorsOffLimit = null;
		int numberRequests = 0;
		int requestCapacity = 0;
		MotionStatus motionStatus = null; 
		ServiceStatus serviceStatus = null;
		TreeMap<String, Boolean> componentFailure = new TreeMap<String, Boolean>();
		
		PositionContext positionContext = new PositionContext(0.0);
		DoorPositionContext doorPositionContext = new DoorPositionContext(0);
		DriveMechanism drive = new DriveMechanism(positionContext, 0);
		PositionSensor sensor = new PositionSensor(positionContext);
		DoorDriveMechanism doorDrive = new DoorDriveMechanism(
				doorPositionContext, 0);
		DoorSensor doorSensor = new DoorSensor(doorPositionContext, 0);
		EmergencyBrake ebrake = new EmergencyBrake();
		Collection<ElevatorComponent> components = Arrays.asList(drive, sensor, doorDrive, doorSensor, ebrake);
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
			for (ElevatorComponent component : components) {
				key = component.getClass().getName();
				value = systemSnapShot.getElevatorSnapShot(i).getFailureStatus(component.getClass());
				componentFailure.put(key, value);
			}
			
			elevatorView.statusUpdate(eid, position, floorsOffLimit, motionStatus, serviceStatus);
		}
		
		for(int i=0; i< numFloors; i++){
			fid = i;
			passengersWaiting = systemSnapShot.getFloorSnapShot(i).getPassengersWaiting();
			isUpSelected = systemSnapShot.getFloorSnapShot(i).getFloorRequestButton().isUpSelected();
			isDownSelected = systemSnapShot.getFloorSnapShot(i).getFloorRequestButton().isDownSelected();
			upSelectedQuantum = systemSnapShot.getFloorSnapShot(i).getFloorRequestButton().getUpSelectedQuantum();
			downSelectedQuantum = systemSnapShot.getFloorSnapShot(i).getFloorRequestButton().getDownSelectedQuantum();
			
			statusView.statusUpdate(fid, position, floorsOffLimit, numberRequests, requestCapacity, motionStatus, serviceStatus, componentFailure, fid, quantum, passengersWaiting, isUpSelected, isDownSelected, upSelectedQuantum, downSelectedQuantum);
				
		}
		
	}

}
