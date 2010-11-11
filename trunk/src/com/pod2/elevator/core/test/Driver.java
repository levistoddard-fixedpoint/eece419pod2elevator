package com.pod2.elevator.core.test;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.SimulationDisplay;
import com.pod2.elevator.core.SimulationResultsSink;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.data.TemplateFailureEvent;
import com.pod2.elevator.data.TemplatePassengerRequest;
import com.pod2.elevator.data.TemplateServiceEvent;
import com.pod2.elevator.scheduling.SimpleScheduler;

public class Driver {

	private static List<TemplateFailureEvent> getFailureEvents() {
		return new LinkedList<TemplateFailureEvent>();
	}

	private static List<TemplateServiceEvent> getServiceEvents() {
		return new LinkedList<TemplateServiceEvent>();
	}

	private static List<TemplatePassengerRequest> getPassengerRequests() {
		LinkedList<TemplatePassengerRequest> requests = new LinkedList<TemplatePassengerRequest>();
		TemplatePassengerRequest request = new TemplatePassengerRequest();
		request.quantum = 5;
		request.onloadFloor = 1;
		request.offloadFloor = 3;
		request.timeConstraint = 1000;
		requests.add(request);
		return requests;
	}

	public static void main(String[] args) throws Exception {
		SimulationTemplate template = new SimulationTemplate();

		template.setId(1);
		template.setName("Test Driver Template");
		template.setCreated(new Date());
		template.setLastEdit(new Date());

		template.setElevatorCapacity(5);
		template.setNumberElevators(2);
		template.setNumberFloors(5);
		template.setSpeed(1.0);
		template.setRequestGenerationOn(true);
		template.setRestrictedFloors(new HashSet<Integer>());

		template.setFailureEvents(getFailureEvents());
		template.setPassengerRequests(getPassengerRequests());
		template.setServiceEvents(getServiceEvents());

		template.scheduler = new SimpleScheduler();

		SimulationDisplay display = new DummyDisplay();
		SimulationResultsSink results = new DummyResultsSink();
		ActiveSimulation simulation = new ActiveSimulation(template, results,
				display);
		simulation.start();
		while (true)
			Thread.sleep(100000000);
		//simulation.stop();
	}

}
