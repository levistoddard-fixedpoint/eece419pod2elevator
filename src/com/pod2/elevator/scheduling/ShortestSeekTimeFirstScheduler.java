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

public class ShortestSeekTimeFirstScheduler implements ElevatorScheduler {

	private Map<Integer, Integer> floorRequest = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> elevatorRequest = new HashMap<Integer, Integer>();

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
		int nearestElevator;

		for (int floor = 0; floor < requests.length; floor++) {
			if ((requests[floor].isUpSelected() || requests[floor]
					.isDownSelected())) {
				// assign floors to elevators without passengers
				nearestElevator = 0;
				for (int j = 0; j < elevators.length; j++) {
					if (elevators[j].getServiceStatus().equals(
							ServiceStatus.InService)) {
						if (!elevators[nearestElevator].getServiceStatus()
								.equals(ServiceStatus.InService)) {
							nearestElevator = j;
						}
						if (Math.abs(elevators[j].getPosition() - floor) < Math
								.abs(elevators[nearestElevator].getPosition()
										- floor)) {
							nearestElevator = j;
						} else if (Math.abs(elevators[j].getPosition() - floor) == Math
								.abs(elevators[nearestElevator].getPosition()
										- floor)) {
							if (elevators[j].getRequestPanel().getRequestSize() < elevators[nearestElevator]
									.getRequestPanel().getRequestSize()) {
								nearestElevator = j;
							}
						}
					} else {
						floorRequest.remove(j);
					}
				}

				if (!floorRequest.containsKey(nearestElevator)
						&& !floorRequest.containsValue(floor)
						&& elevators[nearestElevator].getServiceStatus()
								.equals(ServiceStatus.InService)) {
					floorRequest.put(nearestElevator, floor);
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
