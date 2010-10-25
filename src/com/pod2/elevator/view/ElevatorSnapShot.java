package com.pod2.elevator.view;

import java.util.AbstractMap;
import java.util.ArrayList;

import com.pod2.elevator.core.MotionStatus;
import com.pod2.elevator.core.ServiceStatus;

public class ElevatorSnapShot {
	public double currentPosition;
	public ArrayList<Integer> floorsOffLimit;
	public int requestCount;
	public MotionStatus motionStatus;
	public ServiceStatus serviceStatus;
	public int requestCapacity;
	public AbstractMap<Class<Object>, Boolean> componentFailure;
}
