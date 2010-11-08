package com.pod2.elevator.core;

import java.util.Queue;

import com.pod2.elevator.data.SimulationResultsBuilder;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.scheduling.ElevatorScheduler;
import com.pod2.elevator.view.SimulationView;

public class ActiveSimulation {
	private long currentQuantum;
	private boolean isRunning;
	private boolean requestGenerationOn;
	private Elevator[] elevators;
	private FloorRequestButton[] floorRequestButtons;
	private Queue<RequestInTransit>[] floorRequestQueues;
	private int numberFloors;
	private int numberElevators;
	private Multimap<Event> globalEventQueue;
	private ElevatorScheduler scheduler;
	private SimulationThread simulationThread;
	private SimulationView simulationView;
	private SimulationResultsBuilder simulationResultsBuilder;
	
	public ActiveSimulation(SimulationTemplate simulationTemplate,
			SimulationResultsBuilder simulationResultsBuilder,
			SimulationView simulationView) {
		this.simulationResultsBuilder = simulationResultsBuilder;
		this.simulationView = simulationView;
		this.numberFloors = simulationTemplate.numberFloors;
		this.numberElevators = simulationTemplate.numberElevators;
	}
	
	public void start() {
		this.simulationThread.start();
	}
	
	public void stop() {
		this.simulationThread.stop();
	}
	
	public void setScheduler(ElevatorScheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public void setRequestGenerationStatus(boolean isOn) {
		this.requestGenerationOn = isOn;
	}
	
	public void enqueueEvent(Event event) {
		
	}
	
	public void enqueueEvent(long timeQuantum, Event event) {
		
	}
	
	protected void executeNextQuantum() {
		
	}
	
	public void generatePassengerRequests() {
		
	}
	
	private void updateFloorRequestQueues() {
		
	}
	
	private void handlePassengerOffloads() {
		
	}
	
	private void handlePassengerOnloads() {
		
	}
	
	private void applyEvents() {
		
	}
	
	private void executeQuantum() {
		
	}
	
	private void createSnapShot() {
		
	}
}
