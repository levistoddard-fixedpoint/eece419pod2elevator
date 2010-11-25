package com.pod2.elevator.scheduling;

import java.util.*;

import com.pod2.elevator.core.*;

public class FCFSScheduler implements ElevatorScheduler{

	private Set<Integer> assignedElevator = new HashSet<Integer>();
	private Set<Double> assignedFloor = new HashSet<Double>();
	
	@Override
	public String getKey() {
		return FCFSScheduler.class.getName();
	}

	@Override
	public String getName() {
		return "First-Come-First-Serve Scheduler";
	}

	@Override
	public String getDescription() {
		return "First elevator request will be dealt with first by the first available elevator";
	}

	@Override
	public void schedule(ActiveSimulation simulation) {
		/*	Scheduler will search every floor and retrieve all elevator requests.  Scheduler
		 * 	will first search delegate floor requests to elevators without any passengers.
		 * 	If all elevators are currently being used, scheduler will assign floor to an
		 * 	elevator with the fewest requests.  Only elevators that are in service will be
		 * 	delegated.
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
						System.out.println("Elevator " + elevator.getElevatorNumber() + " going to floor " + i);
					}
				}
			} else if (MotionStatus.ReachedDestinationFloor.equals(elevator
					.getMotionStatus())) {
				elevator.openDoors();
			}
		}
	}

}
