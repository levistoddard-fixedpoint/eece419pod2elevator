package com.pod2.elevator.data;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class DataDriver {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		SimulationTemplateRepository test = new SimulationTemplateRepository();
		SimulationTemplate template = new SimulationTemplate();
		SimulationTemplate fetchedTemplate = new SimulationTemplate();
		
		// Get current date and format for SQL
		java.util.Date date = new java.util.Date();
		Date curDate = new Date(date.getTime());
		
		//
		// Set template parameters
		//
		template.setName("Test Template");
		template.setCreated(curDate);
		template.setLastEdit(curDate);
		template.setElevatorCapacity(8);
		template.setNumberElevators(2);
		template.setNumberFloors(12);
		template.setRequestGenerationOn(false);
		
		List<TemplatePassengerRequest> passengerRequests = new Vector<TemplatePassengerRequest>();
		template.setPassengerRequests(passengerRequests);
		TemplatePassengerRequest req = new TemplatePassengerRequest();
		req.offloadFloor = 1;
		req.onloadFloor = 2;
		req.quantum = 1000;
		req.timeConstraint = 500;
		template.getPassengerRequests().add(req);
		
		List<TemplateFailureEvent> failureEvents = new Vector<TemplateFailureEvent>();
		template.setFailureEvents(failureEvents);
		TemplateFailureEvent ev = new TemplateFailureEvent();
		ev.componentKey = "Door";
		ev.elevatorNumber = 1;
		ev.quantum = 10000;
		template.getFailureEvents().add(ev);
		
		List<TemplateServiceEvent> serviceEvents = new Vector<TemplateServiceEvent>();
		template.setServiceEvents(serviceEvents);
		TemplateServiceEvent serv = new TemplateServiceEvent();
		serv.elevatorNumber = 1;
		serv.putInService = false;
		serv.quantum = 10000;
		template.getServiceEvents().add(serv);
		
		Set<Integer> restrictedFloors = new HashSet<Integer>();
		restrictedFloors.add(10);
		restrictedFloors.add(11);
		restrictedFloors.add(12);
		template.setRestrictedFloors(restrictedFloors);
		
		
		//
		// insert template into database
		//
		test.createTemplate(template); 
		
		template.setName("Modified Template");
		test.updateTemplate(template);

		//
		// Get list of all templates
		//
		List<SimulationTemplateDetail> templateList = test.getAllTemplates();
		
		fetchedTemplate = test.getTemplate(templateList.iterator().next().id);
				
		System.out.println("Details for template id # " + fetchedTemplate.getId() + ":\n" +
				"Name: " + fetchedTemplate.getName() + "\n" +
				"Created: " + fetchedTemplate.getCreated() + "\n" +
				"Last Edit: " + fetchedTemplate.getLastEdit() + "\n" +
				"Elevator Capacity: " + fetchedTemplate.getElevatorCapacity() + "\n" +
				"Number of Elevators: " + fetchedTemplate.getNumberElevators() + "\n" +
				"Number of Floors: " + fetchedTemplate.getNumberFloors() + "\n" +
				"Request Generation On: " + fetchedTemplate.isRequestGenerationOn() + "\n");
		
		//
		// Simulation Data
		//
		
		SimulationDataRepository sdr = new SimulationDataRepository();
		int simcount = sdr.getSimulationCountByTemplate(1);
		System.out.println("Simcount: " + simcount + "\n");
		
		

		System.out.println("Done!");
		
		
	}

}
