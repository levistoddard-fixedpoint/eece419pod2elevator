package com.pod2.elevator.view;

public class SystemSnapShot {
	public int quantum;
	public ElevatorSnapShot elevatorSnapShot[];
	public FloorSnapShot floorSnapShot;
	public LogMessage messages[];
	
	public SystemSnapShot(int numElevators, int numFloors, int numMessages){
		elevatorSnapShot = new ElevatorSnapShot[numElevators];
		for(int i=0; i<numElevators; i++){
			elevatorSnapShot[i] = new ElevatorSnapShot();
		}
		floorSnapShot = new FloorSnapShot(numFloors);
		messages = new LogMessage[numMessages];
	}
}
