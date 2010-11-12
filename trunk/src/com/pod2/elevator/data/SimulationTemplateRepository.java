package com.pod2.elevator.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

public class SimulationTemplateRepository {

	public void createTemplate(SimulationTemplate template) throws SQLException {
		Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost/ElevatorDB", "root", "");
		Statement s = conn.createStatement();
		s.executeQuery("INSERT INTO `SimulationTemplate` (" + "`numberFloors`,"
				+ "`elevatorCapacity`," + "`numberElevators`," + "`scheduler`,"
				+ "`requestGenerationOn`," + "`name`," + "`created`,"
				+ "`lastEdit`" + ") " + "VALUES (" + template.numberFloors
				+ "," + template.elevatorCapacity + ","
				+ template.numberElevators + "," + template.scheduler.getKey()
				+ "," + template.requestGenerationOn + "," + template.name
				+ "," + template.created + "," + template.lastEdit + ")");

		s.close();
		conn.close();
	}

	public void updateTemplate(SimulationTemplate template) throws SQLException {
		Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost/ElevatorDB", "root", "");
		Statement s = conn.createStatement();
		s.executeQuery("UPDATE `SimulationTemplate` SET " + "`numberFloors` = "
				+ template.numberFloors + "," + "`elevatorCapacity` = "
				+ template.elevatorCapacity + "," + "`numberElevators` = "
				+ template.numberElevators + "," + "`scheduler` = "
				+ template.scheduler.getKey() + ","
				+ "`requestGenerationOn` = " + template.requestGenerationOn
				+ "," + "`name`, = " + template.name + "," + "`created` = "
				+ template.created + "," + "`lastEdit` = " + template.lastEdit
				+ " WHERE `id` = " + template.id);

		s.close();
		conn.close();
	}

	public void deleteTemplate(int id) throws SQLException {

		Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost/ElevatorDB", "root", "");
		Statement s = conn.createStatement();
		s.executeQuery("DELETE FROM `SimulationTemplate` WHERE `id` = " + id);

		s.close();
		conn.close();

	}

	public List<SimulationTemplateDetail> getAllTemplates() throws SQLException {

		List<SimulationTemplateDetail> allTemplates = new Vector<SimulationTemplateDetail>();

		Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost/ElevatorDB", "root", "");
		Statement s = conn.createStatement();
		s.executeQuery("SELECT * FROM `SimulationTemplate`");
		ResultSet rs = s.getResultSet();

		while (rs.next()) {
			SimulationTemplateDetail templateDetail = new SimulationTemplateDetail();
			templateDetail.id = rs.getInt("id");
			templateDetail.name = rs.getString("name");
			templateDetail.created = rs.getDate("created");
			templateDetail.lastEdit = rs.getDate("lastEdit");
			allTemplates.add(templateDetail);
		}

		rs.close();
		s.close();
		conn.close();

		return allTemplates;
	}

	public SimulationTemplate getTemplate(int id) throws SQLException,
			ClassNotFoundException {

		SimulationTemplate template = new SimulationTemplate();
		List<TemplatePassengerRequest> passengerRequests = new Vector<TemplatePassengerRequest>();
		List<TemplateFailureEvent> failureEvents = new Vector<TemplateFailureEvent>();
		List<TemplateServiceEvent> serviceEvents = new Vector<TemplateServiceEvent>();

		Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost/ElevatorDB", "root", "");
		Statement s = conn.createStatement();
		s.executeQuery("SELECT * FROM `SimulationTemplate` WHERE `id` = " + id);
		ResultSet rs = s.getResultSet();
		rs.next();

		//
		// Get one-to-one template attributes
		//
		template.id = id;
		template.numberFloors = rs.getInt("numberFloors");
		template.elevatorCapacity = rs.getInt("elevatorCapacity");
		template.numberElevators = rs.getInt("numberElevators");
		// template.getElevatorScheduler
		template.requestGenerationOn = rs.getBoolean("requestGenerationOn");

		//
		// Get many-to-one template attributes
		//

		// TemplatePassengerRequest
		s.executeQuery("SELECT * FROM `TemplatePassengerRequest` WHERE `templateId` = "
				+ id);
		rs = s.getResultSet();

		while (rs.next()) {
			TemplatePassengerRequest req = new TemplatePassengerRequest();
			req.onloadFloor = rs.getInt("onloadFloor");
			req.offloadFloor = rs.getInt("offloadFloor");
			req.timeConstraint = rs.getLong("timeConstraint");
			passengerRequests.add(req);
		}
		template.passengerRequests = passengerRequests;

		// TemplateFailureEvent
		s.executeQuery("SELECT * FROM `TemplateFailureEvent` WHERE `templateId` = "
				+ id);
		rs = s.getResultSet();

		while (rs.next()) {
			TemplateFailureEvent failEvent = new TemplateFailureEvent();
			failEvent.componentKey = rs.getString("component");
			failEvent.elevatorNumber = rs.getInt("elevatorNumber");
			failEvent.quantum = rs.getLong("quantum");
			failureEvents.add(failEvent);
		}
		template.failureEvents = failureEvents;

		// TemplateServiceEvent
		s.executeQuery("SELECT * FROM `TemplateServiceEvent` WHERE `templateId` = "
				+ id);
		rs = s.getResultSet();

		while (rs.next()) {
			TemplateServiceEvent servEvent = new TemplateServiceEvent();
			servEvent.elevatorNumber = rs.getInt("elevatorNumber");
			servEvent.quantum = rs.getLong("quantum");
			serviceEvents.add(servEvent);
		}
		template.serviceEvents = serviceEvents;

		rs.close();
		s.close();
		conn.close();

		return template;

	}
}
