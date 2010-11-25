package com.pod2.elevator.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.pod2.elevator.core.component.ComponentRegistry;
import com.pod2.elevator.scheduling.SchedulerRegistry;

public class SimulationTemplateRepository {

	static public void createTemplate(SimulationTemplate template) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ElevatorDB", "root",
				"");

		//
		// One-to-one attributes
		//
		String sqlQuery = "INSERT INTO `SimulationTemplate` (" + "`numberFloors`,"
				+ "`elevatorCapacity`," + "`numberElevators`," + "`scheduler`,"
				+ "`requestGenerationOn`," + "`name`," + "`created`," + "`lastEdit`" + ") "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement preparedStmt = conn.prepareStatement(sqlQuery);

		preparedStmt.setInt(1, template.getNumberFloors());
		preparedStmt.setInt(2, template.getElevatorCapacity());
		preparedStmt.setInt(3, template.getNumberElevators());
		preparedStmt.setString(4, template.getScheduler().getKey());
		preparedStmt.setBoolean(5, template.isRequestGenerationOn());
		preparedStmt.setString(6, template.getName());
		preparedStmt.setDate(7, new Date(template.getCreated().getTime()));
		preparedStmt.setDate(8, new Date(template.getLastEdit().getTime()));

		preparedStmt.execute();

		//
		// Get template id
		//
		Statement s = conn.createStatement();
		s.executeQuery("SELECT * FROM `SimulationTemplate` ORDER BY `id` DESC");
		ResultSet rs = s.getResultSet();
		rs.next();
		template.setId(rs.getInt("id"));

		s.close();
		rs.close();

		//
		// Many-to-one attributes
		//

		//
		// Restricted Floors
		//

		Iterator<Integer> rf = template.getRestrictedFloors().iterator();
		while (rf.hasNext()) {
			sqlQuery = "INSERT INTO `RestrictedFloors` (`templateId`,`restrictedFloor`)"
					+ "VALUES (?, ?)";
			preparedStmt = conn.prepareStatement(sqlQuery);
			preparedStmt.setInt(1, template.getId());
			preparedStmt.setInt(2, rf.next());
			preparedStmt.execute();
		}

		//
		// Passenger Requests
		//

		Iterator<TemplatePassengerRequest> rq = template.getPassengerRequests().iterator();
		while (rq.hasNext()) {
			TemplatePassengerRequest request = rq.next();
			sqlQuery = "INSERT INTO `TemplatePassengerRequest` (`templateId`, `onloadFloor`,"
					+ "`offloadFloor`,`timeConstraint`,`quantum`) " + "VALUES (?, ?, ?, ?, ?)";
			preparedStmt = conn.prepareStatement(sqlQuery);
			preparedStmt.setInt(1, template.getId());
			preparedStmt.setInt(2, request.getOnloadFloor());
			preparedStmt.setInt(3, request.getOffloadFloor());
			preparedStmt.setLong(4, request.getTimeConstraint());
			preparedStmt.setLong(5, request.getQuantum());
			preparedStmt.execute();
		}

		//
		// Failure Events
		//

		Iterator<TemplateFailureEvent> fe = template.getFailureEvents().iterator();
		while (fe.hasNext()) {
			TemplateFailureEvent event = fe.next();
			sqlQuery = "INSERT INTO `TemplateFailureEvent` (`templateId`, `component`,"
					+ "`elevatorNumber`,`quantum`) " + "VALUES (?, ?, ?, ?)";
			preparedStmt = conn.prepareStatement(sqlQuery);
			preparedStmt.setInt(1, template.getId());
			preparedStmt.setString(2, event.getComponent().getKey());
			preparedStmt.setInt(3, event.getElevatorNumber());
			preparedStmt.setLong(4, event.getQuantum());
			preparedStmt.execute();
		}

		//
		// Service Events
		//

		Iterator<TemplateServiceEvent> se = template.getServiceEvents().iterator();
		while (se.hasNext()) {
			TemplateServiceEvent event = se.next();
			sqlQuery = "INSERT INTO `TemplateServiceEvent` (`templateId`, `putInService`,"
					+ "`elevatorNumber`,`quantum`) " + "VALUES (?, ?, ?, ?)";
			preparedStmt = conn.prepareStatement(sqlQuery);
			preparedStmt.setInt(1, template.getId());
			preparedStmt.setBoolean(2, event.isPutInService());
			preparedStmt.setInt(3, event.getElevatorNumber());
			preparedStmt.setLong(4, event.getQuantum());
			preparedStmt.execute();
		}

		//
		// Done
		//

		conn.close();
	}

	static public void updateTemplate(SimulationTemplate template) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ElevatorDB", "root",
				"");

		//
		// Update one-to-one template attributes
		//
		String sqlQuery = "UPDATE `SimulationTemplate` SET " + "`numberFloors` = ?,"
				+ "`elevatorCapacity` = ?," + "`numberElevators` = ?," + "`scheduler` = ?,"
				+ "`requestGenerationOn` = ?," + "`name` = ?," + "`created` = ?,"
				+ "`lastEdit` = ?" + " WHERE `id` = ?";

		PreparedStatement preparedStmt = conn.prepareStatement(sqlQuery);
		preparedStmt.setInt(1, template.getNumberFloors());
		preparedStmt.setInt(2, template.getElevatorCapacity());
		preparedStmt.setInt(3, template.getNumberElevators());
		preparedStmt.setString(4, template.getScheduler().getKey());
		preparedStmt.setBoolean(5, template.isRequestGenerationOn());
		preparedStmt.setString(6, template.getName());
		preparedStmt.setDate(7, new Date(template.getCreated().getTime()));
		preparedStmt.setDate(8, new Date(template.getLastEdit().getTime()));
		preparedStmt.setInt(9, template.getId());

		preparedStmt.executeUpdate();

		//
		// Update many-to-one attributes
		//

		//
		// Restricted Floors
		//

		// Delete old entries
		sqlQuery = "DELETE FROM `RestrictedFloors` WHERE `templateId` = ?";
		preparedStmt = conn.prepareStatement(sqlQuery);
		preparedStmt.setInt(1, template.getId());
		preparedStmt.execute();

		// Insert new entries
		Iterator<Integer> rf = template.getRestrictedFloors().iterator();
		while (rf.hasNext()) {
			sqlQuery = "INSERT INTO `RestrictedFloors` (`templateId`,`restrictedFloor`)"
					+ "VALUES (?, ?)";
			preparedStmt = conn.prepareStatement(sqlQuery);
			preparedStmt.setInt(1, template.getId());
			preparedStmt.setInt(2, rf.next());
			preparedStmt.execute();
		}

		//
		// Passenger Requests
		//

		// Delete old entries
		sqlQuery = "DELETE FROM `TemplatePassengerRequest` WHERE `templateId` = ?";
		preparedStmt = conn.prepareStatement(sqlQuery);
		preparedStmt.setInt(1, template.getId());
		preparedStmt.execute();

		// Insert new entries
		Iterator<TemplatePassengerRequest> rq = template.getPassengerRequests().iterator();
		while (rq.hasNext()) {
			TemplatePassengerRequest request = rq.next();
			sqlQuery = "INSERT INTO `TemplatePassengerRequest` (`templateId`, `onloadFloor`,"
					+ "`offloadFloor`,`timeConstraint`,`quantum`) " + "VALUES (?, ?, ?, ?, ?)";
			preparedStmt = conn.prepareStatement(sqlQuery);
			preparedStmt.setInt(1, template.getId());
			preparedStmt.setInt(2, request.getOnloadFloor());
			preparedStmt.setInt(3, request.getOffloadFloor());
			preparedStmt.setLong(4, request.getTimeConstraint());
			preparedStmt.setLong(5, request.getQuantum());
			preparedStmt.execute();
		}

		//
		// Failure Events
		//

		// Delete old entries
		sqlQuery = "DELETE FROM `TemplateFailureEvent` WHERE `templateId` = ?";
		preparedStmt = conn.prepareStatement(sqlQuery);
		preparedStmt.setInt(1, template.getId());
		preparedStmt.execute();

		// Insert new entries
		Iterator<TemplateFailureEvent> fe = template.getFailureEvents().iterator();
		while (fe.hasNext()) {
			TemplateFailureEvent event = fe.next();
			sqlQuery = "INSERT INTO `TemplateFailureEvent` (`templateId`, `component`,"
					+ "`elevatorNumber`,`quantum`) " + "VALUES (?, ?, ?, ?)";
			preparedStmt = conn.prepareStatement(sqlQuery);
			preparedStmt.setInt(1, template.getId());
			preparedStmt.setString(2, event.getComponent().getKey());
			preparedStmt.setInt(3, event.getElevatorNumber());
			preparedStmt.setLong(4, event.getQuantum());
			preparedStmt.execute();
		}

		//
		// Service Events
		//

		// Delete old entries
		sqlQuery = "DELETE FROM `TemplateServiceEvent` WHERE `templateId` = ?";
		preparedStmt = conn.prepareStatement(sqlQuery);
		preparedStmt.setInt(1, template.getId());
		preparedStmt.execute();

		// Insert new entries
		Iterator<TemplateServiceEvent> se = template.getServiceEvents().iterator();
		while (se.hasNext()) {
			TemplateServiceEvent event = se.next();
			sqlQuery = "INSERT INTO `TemplateServiceEvent` (`templateId`, `putInService`,"
					+ "`elevatorNumber`,`quantum`) " + "VALUES (?, ?, ?, ?)";
			preparedStmt = conn.prepareStatement(sqlQuery);
			preparedStmt.setInt(1, template.getId());
			preparedStmt.setBoolean(2, event.isPutInService());
			preparedStmt.setInt(3, event.getElevatorNumber());
			preparedStmt.setLong(4, event.getQuantum());
			preparedStmt.execute();
		}

		//
		// Done
		//
		conn.close();
	}

	static public void deleteTemplate(int id) throws SQLException {

		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ElevatorDB", "root",
				"");

		String sqlQuery = "DELETE FROM `SimulationTemplate` WHERE `id` = ?";
		PreparedStatement preparedStmt = conn.prepareStatement(sqlQuery);
		preparedStmt.setInt(1, id);
		preparedStmt.execute();

		//
		// Done
		//
		conn.close();

	}

	static public List<SimulationTemplateDetail> getAllTemplates() throws SQLException {

		List<SimulationTemplateDetail> allTemplates = new Vector<SimulationTemplateDetail>();

		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ElevatorDB", "root",
				"");

		Statement s = conn.createStatement();
		s.executeQuery("SELECT * FROM `SimulationTemplate`");
		ResultSet rs = s.getResultSet();

		while (rs.next()) {
			SimulationTemplateDetail templateDetail = new SimulationTemplateDetail(rs.getInt("id"),
					rs.getString("name"), rs.getDate("created"), rs.getDate("lastEdit"));
			allTemplates.add(templateDetail);
		}

		rs.close();
		s.close();

		//
		// Done
		//
		conn.close();

		return allTemplates;
	}

	static public SimulationTemplate getTemplate(int id) throws SQLException {

		SimulationTemplate template = new SimulationTemplate();
		Set<Integer> restrictedFloors = new HashSet<Integer>();
		List<TemplatePassengerRequest> passengerRequests = new Vector<TemplatePassengerRequest>();
		List<TemplateFailureEvent> failureEvents = new Vector<TemplateFailureEvent>();
		List<TemplateServiceEvent> serviceEvents = new Vector<TemplateServiceEvent>();

		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ElevatorDB", "root",
				"");
		Statement s = conn.createStatement();
		s.executeQuery("SELECT * FROM `SimulationTemplate` WHERE `id` = " + id);
		ResultSet rs = s.getResultSet();
		rs.next();

		//
		// Get one-to-one template attributes
		//
		template.setId(id);
		template.setName(rs.getString("name"));
		template.setNumberFloors(rs.getInt("numberFloors"));
		template.setElevatorCapacity(rs.getInt("elevatorCapacity"));
		template.setNumberElevators(rs.getInt("numberElevators"));
		template.setScheduler(SchedulerRegistry.getSchedulerByKey(rs.getString("scheduler")));
		template.setRequestGenerationOn(rs.getBoolean("requestGenerationOn"));
		template.setCreated(rs.getDate("created"));
		template.setLastEdit(rs.getDate("lastEdit"));

		//
		// Get many-to-one template attributes
		//

		// Restricted floors
		s.executeQuery("SELECT * FROM `RestrictedFloors` WHERE `templateId` = " + id);
		rs = s.getResultSet();

		while (rs.next()) {
			restrictedFloors.add(rs.getInt("restrictedFloor"));
		}
		template.setRestrictedFloors(restrictedFloors);

		// TemplatePassengerRequest
		s.executeQuery("SELECT * FROM `TemplatePassengerRequest` WHERE `templateId` = " + id);
		rs = s.getResultSet();

		while (rs.next()) {
			TemplatePassengerRequest req = new TemplatePassengerRequest();
			req.setOnloadFloor(rs.getInt("onloadFloor"));
			req.setOffloadFloor(rs.getInt("offloadFloor"));
			req.setTimeConstraint(rs.getLong("timeConstraint"));
			passengerRequests.add(req);
		}
		template.setPassengerRequests(passengerRequests);

		// TemplateFailureEvent
		s.executeQuery("SELECT * FROM `TemplateFailureEvent` WHERE `templateId` = " + id);
		rs = s.getResultSet();

		while (rs.next()) {
			TemplateFailureEvent failEvent = new TemplateFailureEvent();
			failEvent.setComponent(ComponentRegistry.getComponentByKey(rs.getString("component")));
			failEvent.setElevatorNumber(rs.getInt("elevatorNumber"));
			failEvent.setQuantum(rs.getLong("quantum"));
			failureEvents.add(failEvent);
		}
		template.setFailureEvents(failureEvents);

		// TemplateServiceEvent
		s.executeQuery("SELECT * FROM `TemplateServiceEvent` WHERE `templateId` = " + id);
		rs = s.getResultSet();

		while (rs.next()) {
			TemplateServiceEvent servEvent = new TemplateServiceEvent();
			servEvent.setElevatorNumber(rs.getInt("elevatorNumber"));
			servEvent.setQuantum(rs.getLong("quantum"));
			serviceEvents.add(servEvent);
		}
		template.setServiceEvents(serviceEvents);

		rs.close();
		s.close();

		//
		// Done
		//
		conn.close();

		return template;

	}
}
