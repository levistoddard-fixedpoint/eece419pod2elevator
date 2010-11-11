package com.pod2.elevator.view;

public class SystemSnapShot {

	private long quantum;
	private ElevatorSnapShot elevatorSnapShot[];
	private FloorSnapShot floorSnapShots[];
	private LogMessage messages[];

	public SystemSnapShot(long quantum, ElevatorSnapShot elevatorSnapShots[],
			FloorSnapShot floorSnapShots[], LogMessage messages[]) {
		this.quantum = quantum;
		this.elevatorSnapShot = elevatorSnapShots;
		this.floorSnapShots = floorSnapShots;
		this.messages = messages;
	}

	public long getQuantum() {
		return quantum;
	}

	public FloorSnapShot getFloorSnapShot(int floorNumber) {
		return floorSnapShots[floorNumber];
	}

	public ElevatorSnapShot getElevatorSnapShot(int elevatorNumber) {
		return elevatorSnapShot[elevatorNumber];
	}

	public LogMessage[] getMessages() {
		return messages;
	}

}
