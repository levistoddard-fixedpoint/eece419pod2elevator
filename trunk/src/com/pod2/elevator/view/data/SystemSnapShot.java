package com.pod2.elevator.view.data;

import java.util.Collection;

import com.pod2.elevator.view.model.LogMessage;

public class SystemSnapShot {

	private long quantum;
	private ElevatorSnapShot elevatorSnapShots[];
	private FloorSnapShot floorSnapShots[];
	private Collection<LogMessage> messages;

	public SystemSnapShot(long quantum, ElevatorSnapShot elevatorSnapShots[],
			FloorSnapShot floorSnapShots[], Collection<LogMessage> messages) {
		this.quantum = quantum;
		this.elevatorSnapShots = elevatorSnapShots;
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
		return elevatorSnapShots[elevatorNumber];
	}

	public Collection<LogMessage> getMessages() {
		return messages;
	}

}
