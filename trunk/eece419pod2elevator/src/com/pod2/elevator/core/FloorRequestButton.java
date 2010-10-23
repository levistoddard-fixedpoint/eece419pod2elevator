package com.pod2.elevator.core;

public class FloorRequestButton {
	public boolean isUpSelected;
	public boolean isDownSelected;
	public long quantumUpWasSelected;
	public long quantumDownWasSelected;
	
	public FloorRequestButton(boolean b1, boolean b2, long l1, long l2){
		isUpSelected = b1;
		isDownSelected = b2;
		quantumUpWasSelected = l1;
		quantumDownWasSelected = l2;
	}
}
