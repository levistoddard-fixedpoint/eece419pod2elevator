package com.pod2.elevator.scheduling;

import com.pod2.elevator.core.ActiveSimulation;

public class FirstComeFirstServe implements ElevatorScheduler{
	
	private static long requestNum = 0;
	private int nextElevator;
	
	public void schedule(ActiveSimulation activeSimulation) {
		
//		//Search all floors from bottom to top for requests
//		for (int i = 0; i < activeSimulation.numberFloors; i++) {
//			// check if button is pressed
//			if (activeSimulation.floorRequestButtons[i].upSelectedQuantum == activeSimulation.currentQuantum || activeSimulation.floorRequestButtons[i].downSelectedQuantum == activeSimulation.currentQuantum) {
//				// assign next elevator to passenger
//				requestNum++;
//				// check to see available elevators
//				do {
//					nextElevator = requestNum % activeSimulation.numberElevators;
//					// if elevator is not in service, increment counter and use next available elevator
//					if (activeSimulation.elevators[nextElevator].serviceStatus == ServiceStatus.InService);
//						break;
//					requestNum++;
//				} while (true);
//				activeSimulation.elevators[nextElevator].moveToFloor(i);
//			}
//		}
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}