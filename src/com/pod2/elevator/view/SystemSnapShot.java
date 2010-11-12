package com.pod2.elevator.view;

import java.util.Collection;

public class SystemSnapShot {

	private long quantum;
	private ElevatorSnapShot elevatorSnapShot[];
	private FloorSnapShot floorSnapShots[];
	private Collection<LogMessage> messages;

	public SystemSnapShot(long quantum, ElevatorSnapShot elevatorSnapShots[],
			FloorSnapShot floorSnapShots[], Collection<LogMessage> messages) {
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

	public Collection<LogMessage> getMessages() {
		return messages;
	}

}
