package com.pod2.elevator.scheduling;

import java.util.*;

import com.pod2.elevator.core.*;

public class FCFSScheduler implements ElevatorScheduler{
	/**
	 * 	OVERVIEW:	A first-come-first serve scheduling algorithm for elevator simulator
	 */

	private Set<Integer> assignedElevator = new HashSet<Integer>();
	private Set<Double> assignedFloor = new HashSet<Double>();
	
	// methods
	public String getKey() {
	/**
	 * 	EFFECTS:	returns the name of the class 
	 */
		return FCFSScheduler.class.getName();
	}

	@Override
	public String getName() {
	/**
	* 	EFFECTS:	returns the name of the scheduling algorithm
	*/
		return "First-Come-First-Serve Scheduler";
	}

	@Override
	public String getDescription() {
	/**
	 * 	EFFECTS:	returns the description of the scheduling algorithm
	 */
		return "First elevator request will be dealt with first by the first available elevator";
	}

	@Override
	public void schedule(ActiveSimulation simulation) {
	/**
	 *	REQUIRES:	simulation != null
	 *	MODIFIES:	AssignedElevator,assignedFloor
	 *	EFFECTS:	Scheduler will search every floor and retrieve all elevator requests.
	 * 				If an elevator request button is pressed in a floor, an elevator that
	 * 				is not occupied will be assigned to it.  Each elevator will be checked
	 * 				of its status.  If doors are opened, they will be closed.  If doors are
	 * 				closed, scheduler will search for any floor requests made to the
	 * 				elevator.  If such floor request is made, elevator will move towards
	 * 				that particular floor.  If elevator has reached its designated floor,
	 * 				elevator doors will open.
	 */
		
		// retrieve all requests
		FloorRequestButton[] requests = simulation.getRequestButtons();
		Elevator[] elevators = simulation.getElevators();
		
		for (int floor = 0; floor < requests.length; floor++) {
			if ((requests[floor].isUpSelected()
					|| requests[floor].isDownSelected()) && !assignedFloor.contains((double)floor)) {
				// assign floors to elevators without passengers
				for (int j = 0; j < elevators.length; j++) {
					if (elevators[j].getServiceStatus().equals(
							ServiceStatus.InService)) {
						if (elevators[j].getRequestPanel().getRequestedFloors().isEmpty() && !assignedElevator.contains(j)){
							assignedElevator.add(j);
							assignedFloor.add((double)floor);
							elevators[j].moveToFloor(floor);
							break;
						}
					}
				}

			}
		}
		
		for (Elevator elevator : simulation.getElevators()) {
			if (MotionStatus.DoorsOpen.equals(elevator.getMotionStatus())) {
				assignedFloor.remove(elevator.getPosition());
				elevator.closeDoors();
			} else if (MotionStatus.DoorsClosed.equals(elevator.getMotionStatus())){
				assignedElevator.remove(elevator.getElevatorNumber());
				for (int i = 0; i < requests.length; i++){
					if (elevator.getRequestPanel().isRequested(i)){
						assignedElevator.add(elevator.getElevatorNumber());
						assignedFloor.add((double)i);
						elevator.moveToFloor(i);
					}
				}
			} else if (MotionStatus.ReachedDestinationFloor.equals(elevator
					.getMotionStatus())) {
				elevator.openDoors();
			}
		}
	}

}
