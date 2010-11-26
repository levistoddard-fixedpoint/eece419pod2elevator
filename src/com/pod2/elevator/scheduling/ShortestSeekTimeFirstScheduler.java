package com.pod2.elevator.scheduling;

import java.util.HashSet;
import java.util.Set;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.Elevator;
import com.pod2.elevator.core.FloorRequestButton;
import com.pod2.elevator.core.MotionStatus;
import com.pod2.elevator.core.ServiceStatus;

public class ShortestSeekTimeFirstScheduler implements ElevatorScheduler {

	private Set<Integer> assignedElevator = new HashSet<Integer>();
	private Set<Double> assignedFloor = new HashSet<Double>();

	@Override
	public String getKey() {
		return ShortestSeekTimeFirstScheduler.class.getName();
	}

	@Override
	public String getName() {
		return "Shortest Seek Time First";
	}

	@Override
	public String getDescription() {
		return "Shortest distance requests will be serviced first";
	}

	@Override
	public void schedule(ActiveSimulation simulation) {
		/*
		 * Scheduler will search every floor and retrieve all elevator requests.
		 * Scheduler will first search delegate floor requests to nearest
		 * elevators. If more than one elevators has the same distance,
		 * scheduler will assign floor to an elevator with the fewest requests
		 * followed by lowest id. Only elevators that are in service will be
		 * delegated.
		 */

		// retrieve all requests
		FloorRequestButton[] requests = simulation.getRequestButtons();
		Elevator[] elevators = simulation.getElevators();

		for (int floor = 0; floor < requests.length; floor++) {
			if ((requests[floor].isUpSelected() || requests[floor].isDownSelected())
					&& !assignedFloor.contains((double) floor)) {
				// assign floors to elevators without passengers
				int nearestElevator = 0;
				for (int j = 0; j < elevators.length; j++) {
					if (elevators[j].getServiceStatus().equals(ServiceStatus.InService)) {
						if (Math.abs(elevators[j].getPosition() - floor) < Math
								.abs(elevators[nearestElevator].getPosition() - floor)) {
							nearestElevator = j;
						} else if (Math.abs(elevators[j].getPosition() - floor) == Math
								.abs(elevators[nearestElevator].getPosition() - floor)) {
							if (elevators[j].getRequestPanel().getRequestSize() < elevators[nearestElevator]
									.getRequestPanel().getRequestSize()) {
								nearestElevator = j;
							}
						}
					}
				}
				if (!assignedElevator.contains(nearestElevator)) {
					assignedElevator.add(nearestElevator);
					assignedFloor.add((double) floor);
					if (elevators[nearestElevator].getPosition() != floor) {
						elevators[nearestElevator].moveToFloor(floor);
					} else {
						elevators[nearestElevator].openDoors();
					}
				}

			}
		}

		for (Elevator elevator : simulation.getElevators()) {
			if (MotionStatus.DoorsOpen.equals(elevator.getMotionStatus())) {
				assignedFloor.remove(elevator.getPosition());
				elevator.closeDoors();
			} else if (MotionStatus.DoorsClosed.equals(elevator.getMotionStatus())) {
				assignedElevator.remove(elevator.getElevatorNumber());
				for (int i = 0; i < requests.length; i++) {
					if (elevator.getRequestPanel().isRequested(i)) {
						assignedElevator.add(elevator.getElevatorNumber());
						assignedFloor.add((double) i);
						elevator.moveToFloor(i);
					}
				}
			} else if (MotionStatus.ReachedDestinationFloor.equals(elevator.getMotionStatus())) {
				elevator.openDoors();
			}
		}

	}

}
