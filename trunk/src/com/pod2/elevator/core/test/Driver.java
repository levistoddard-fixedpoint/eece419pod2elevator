package com.pod2.elevator.core.test;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.ResultsBuilder;
import com.pod2.elevator.core.SimulationDisplay;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.data.TemplateFailureEvent;
import com.pod2.elevator.data.TemplatePassengerRequest;
import com.pod2.elevator.data.TemplateServiceEvent;
import com.pod2.elevator.scheduling.*;
import com.pod2.elevator.view.SimulationWindow;

import depricated.SimulationView;

public class Driver {

	private static List<TemplateFailureEvent> getFailureEvents() {

		LinkedList<TemplateFailureEvent> events = new LinkedList<TemplateFailureEvent>();

		// TemplateFailureEvent e1 = new TemplateFailureEvent();
		// e1.quantum = 100;
		// e1.componentKey = DriveMechanism.class.getName();
		// e1.elevatorNumber = 1;
		// events.add(e1);

		return events;
	}

	private static List<TemplateServiceEvent> getServiceEvents() {
		LinkedList<TemplateServiceEvent> events = new LinkedList<TemplateServiceEvent>();

		// TemplateServiceEvent e2 = new TemplateServiceEvent();
		// e2.elevatorNumber = 1;
		// e2.putInService = true;
		// e2.quantum = 200;
		// events.add(e2);
		//
		// TemplateServiceEvent e3 = new TemplateServiceEvent();
		// e3.elevatorNumber = 0;
		// e3.putInService = false;
		// e3.quantum = 200;
		// events.add(e3);

		return events;
	}

	private static List<TemplatePassengerRequest> getPassengerRequests() {
		LinkedList<TemplatePassengerRequest> requests = new LinkedList<TemplatePassengerRequest>();
		/*
		TemplatePassengerRequest request = new TemplatePassengerRequest();
		request.setQuantum(20);
		request.setOnloadFloor(1);
		request.setOffloadFloor(3);
		request.setTimeConstraint(1000);
		requests.add(request);
		
		
		request = new TemplatePassengerRequest();
		request.setQuantum(20);
		request.setOnloadFloor(4);
		request.setOffloadFloor(2);
		request.setTimeConstraint(1000);
		requests.add(request);
		*/
		return requests;
	}

	public static void main(String[] args) throws Exception {
		SimulationTemplate template = new SimulationTemplate();

		template.setId(1);
		template.setName("Test Driver Template");
		template.setCreated(new Date());
		template.setLastEdit(new Date());

		template.setElevatorCapacity(5);
		template.setNumberElevators(5);
		template.setNumberFloors(5);
		template.setSpeed(1.0);
		template.setRequestGenerationOn(true);
		template.setRestrictedFloors(new HashSet<Integer>());

		template.setQuantumsBeforeService(-1);
		template.setDistanceBeforeService(-1.0);

		template.setFailureEvents(getFailureEvents());
		template.setPassengerRequests(getPassengerRequests());
		template.setServiceEvents(getServiceEvents());

		template.setScheduler(new FCFSScheduler());

		// SimulationDisplay display = new DummyDisplay();

		SimulationWindow window = new SimulationWindow(
				template.getNumberFloors(), template.getNumberElevators());
		SwingUtilities.invokeLater(window);
		SimulationDisplay display = new SimulationView(window);
		ResultsBuilder results = new DummyResultsBuilder();
		ActiveSimulation simulation = new ActiveSimulation(template, results,
				display);
		simulation.start();
		while (true)
			Thread.sleep(100000000);
		// simulation.stop();
	}

}