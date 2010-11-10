package com.pod2.elevator.view;

import com.pod2.elevator.core.FloorRequestButton;

public class FloorSnapShot {

//	public FloorRequestButton floorRequestButtons[];
//	public int floorQueues[];
//
//	public FloorSnapShot(FloorRequestButton floorRequestButtons[],
//			int floorQueues[]) {
//		this.floorRequestButtons = floorRequestButtons;
//		this.floorQueues = floorQueues;
//	}
//
//	public FloorSnapShot(int numFloors) {
//		floorQueues = new int[numFloors];
//		floorRequestButtons = new FloorRequestButton[numFloors];
//	}
	
	private FloorRequestButton floorRequestButton;
	private int passengersWaiting;
	
	public FloorSnapShot(FloorRequestButton floorRequestButton,
			int passengersWaiting) {
		super();
		this.floorRequestButton = floorRequestButton;
		this.passengersWaiting = passengersWaiting;
	}
	
	public FloorRequestButton getFloorRequestButton() {
		return floorRequestButton;
	}
	
	public int getPassengersWaiting() {
		return passengersWaiting;
	}
	
}
