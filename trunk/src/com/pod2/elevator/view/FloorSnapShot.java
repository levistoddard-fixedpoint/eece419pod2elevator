package com.pod2.elevator.view;

import com.pod2.elevator.core.FloorRequestButton;

public class FloorSnapShot {

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
