package com.pod2.elevator.view;

import java.util.ArrayList;
import java.util.TreeMap;

import com.pod2.elevator.core.MotionStatus;
import com.pod2.elevator.core.ServiceStatus;

public class ElevatorSnapShot {
	public double currentPosition;
	public ArrayList<Integer> floorsOffLimit;
	public int requestCount;
	public MotionStatus motionStatus;
	public ServiceStatus serviceStatus;
	public int requestCapacity;
	public TreeMap<Class<Object>, Boolean> componentFailure;
	
	public ElevatorSnapShot(){
		currentPosition = 0;
		floorsOffLimit = new ArrayList<Integer>();
		requestCount = 0;
		motionStatus = MotionStatus.DoorsOpen;
		serviceStatus = ServiceStatus.InService;
		requestCapacity = 10;
		componentFailure = new TreeMap<Class<Object>, Boolean>();
	}
}
