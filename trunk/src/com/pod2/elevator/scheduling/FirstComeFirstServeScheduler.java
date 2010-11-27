package com.pod2.elevator.scheduling;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.Elevator;
import com.pod2.elevator.core.FloorRequestButton;
import com.pod2.elevator.core.MotionStatus;
import com.pod2.elevator.core.ServiceStatus;

/**
 * OVERVIEW: A first-come-first serve scheduling algorithm for elevator
 * simulator
 */
public class FirstComeFirstServeScheduler implements ElevatorScheduler {

	private Map<Integer, Integer> floorRequest = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> elevatorRequest = new HashMap<Integer, Integer>();

	/**
	 * EFFECTS: returns the name of the class
	 */
	public String getKey() {
		return FirstComeFirstServeScheduler.class.getName();
	}

	@Override
	public String getName() {
		return "First Come First Serve";
	}

	@Override
	public String getDescription() {
		return "First elevator request will be dealt with first by the first available elevator";
	}

	/**
	 * REQUIRES: simulation != null
	 * 
	 * MODIFIES: AssignedElevator,assignedFloor
	 * 
	 * EFFECTS: Scheduler will search every floor and retrieve all elevator
	 * requests. If an elevator request button is pressed in a floor, an
	 * elevator that is not occupied will be assigned to it. Each elevator will
	 * be checked of its status. If doors are opened, they will be closed. If
	 * doors are closed, scheduler will search for any floor requests made to
	 * the elevator. If such floor request is made, elevator will move towards
	 * that particular floor. If elevator has reached its designated floor,
	 * elevator doors will open.
	 */
	@Override
	public void schedule(ActiveSimulation simulation) {
	
	// retrieve all requests
	FloorRequestButton[] requests = simulation.getRequestButtons();
	Elevator[] elevators = simulation.getElevators();
	int firstElevator;

	for (int floor = 0; floor < requests.length; floor++) {
		if ((requests[floor].isUpSelected() || requests[floor]
				.isDownSelected())) {
			// assign floors to elevators without passengers
			firstElevator = 0;
			for (int j = 0; j < elevators.length; j++) {
				if (elevators[j].getServiceStatus().equals(
						ServiceStatus.InService)) {
					if (!elevators[firstElevator].getServiceStatus()
							.equals(ServiceStatus.InService)) {
						firstElevator = j;
					}
					if (elevators[j].getRequestPanel().getRequestedFloors().isEmpty()
							&& !elevatorRequest.containsKey(j)) {
						firstElevator = j;
						break;
					}
				} else {
					floorRequest.remove(j);
				}
			}

			if (!floorRequest.containsKey(firstElevator)
					&& !floorRequest.containsValue(floor)
					&& elevators[firstElevator].getServiceStatus()
							.equals(ServiceStatus.InService)) {
				floorRequest.put(firstElevator, floor);
				if (elevators[firstElevator].getPosition() != floor) {
					elevators[firstElevator].moveToFloor(floor);
				} else {
					elevators[firstElevator].openDoors();
				}
			}

		}
	}

	for (Elevator elevator : simulation.getElevators()) {
		if (MotionStatus.DoorsOpen.equals(elevator.getMotionStatus())) {
			floorRequest.remove(elevator.getElevatorNumber());
			elevatorRequest.remove(elevator.getElevatorNumber());
			elevator.closeDoors();
		} else if (MotionStatus.DoorsClosed.equals(elevator
				.getMotionStatus())) {
			floorRequest.remove(elevator.getElevatorNumber());
			elevatorRequest.remove(elevator.getElevatorNumber());
			for (int i = 0; i < requests.length; i++) {
				if (elevator.getRequestPanel().isRequested(i)) {
					elevatorRequest.put(elevator.getElevatorNumber(), i);
					elevator.moveToFloor(i);
				}
			}
		} else if (MotionStatus.ReachedDestinationFloor.equals(elevator
				.getMotionStatus())) {
			elevator.openDoors();
		}
	}

}
	
	@Override
	public String toString() {
		return getName();
	}

}
