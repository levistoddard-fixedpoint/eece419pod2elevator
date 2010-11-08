package com.pod2.elevator.view;

import com.pod2.elevator.core.FloorRequestButton;

public class FloorSnapShot {
	public FloorRequestButton floorRequestButtons[];
	public int floorQueues[];
	
	public FloorSnapShot(int numFloors) {
		floorQueues = new int[numFloors];
		floorRequestButtons = new FloorRequestButton[numFloors];
	}
}
