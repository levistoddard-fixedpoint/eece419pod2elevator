package com.pod2.elevator.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.pod2.elevator.core.DeliveryStatus;
import com.pod2.elevator.core.ServiceStatus;

public class SimulationDataRepository {
	static public List<SimulationDetail> getCompletedSimulations()
			throws SQLException {

		List<SimulationDetail> completedSimulations = new LinkedList<SimulationDetail>();

		Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost/ElevatorDB", "root", "");

		Statement s = conn.createStatement();
		s.executeQuery("SELECT `uuid`,`name` FROM `SimulationResults`");
		ResultSet rs = s.getResultSet();

		while (rs.next()) {
			SimulationDetail simulationDetail = new SimulationDetail(
					rs.getInt("uuid"), rs.getString("name"));
			completedSimulations.add(simulationDetail);
		}

		rs.close();
		s.close();

		//
		// Done
		//
		conn.close();

		return completedSimulations;
	}

	static public SimulationResultsBuilder getSimulationResultsBuilder(
			String name, SimulationTemplate template) {
		return new SimulationResultsBuilder(name, template.getId());
	}

	static public SimulationResults getSimulationResults(int uuid)
			throws SQLException {
		SimulationResults results = new SimulationResults();
		List<CompletedRequest> passengerDeliveries = new Vector<CompletedRequest>();
		List<ElevatorState[]> elevatorStates = new Vector<ElevatorState[]>();
		int numElevators = 0;
		List<LoggedEvent> events = new Vector<LoggedEvent>();

		Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost/ElevatorDB", "root", "");

		Statement s = conn.createStatement();
		s.executeQuery("SELECT * FROM `SimulationResults` WHERE `uuid` = "
				+ uuid);
		ResultSet rs = s.getResultSet();
		rs.next();

		//
		// One-to-one attributes
		//
		results.setUuid(uuid);
		results.setName(rs.getString("name"));
		results.setTemplateId(rs.getInt("templateId"));
		results.setStartTime(rs.getDate("startTime"));
		results.setStopTime(rs.getDate("stopTime"));
		results.setStartQuantum(rs.getLong("startQuantum"));
		results.setStopQuantum(rs.getLong("stopQuantum"));

		//
		// Many-to-one attributes
		//

		// Completed requests
		s.executeQuery("SELECT * FROM `CompletedRequest` WHERE `resultId` = "
				+ uuid);
		rs = s.getResultSet();
		while (rs.next()) {
			CompletedRequest rq = new CompletedRequest();
			rq.setElevatorNumber(rs.getInt("elevatorNumber"));
			rq.setOnloadFloor(rs.getInt("onloadFloor"));
			rq.setOffloadFloor(rs.getInt("offloadFloor"));
			rq.setEnterQuantum(rs.getLong("enterQuantum"));
			rq.setOnloadQuantum(rs.getLong("onloadQuantum"));
			rq.setOffloadQuantum(rs.getLong("offloadQuantum"));
			rq.setTimeConstraint(rs.getLong("timeConstraint"));
			rq.setDeliveryStatus(DeliveryStatus.values()[rs
					.getInt("deliveryStatus")]);
			passengerDeliveries.add(rq);
		}
		results.setPassengerDeliveries(passengerDeliveries);

		// Elevator States

		// Find Elevator Max
		s.executeQuery("SELECT MAX(elevatorNumber) as maxElevator FROM `ElevatorState` WHERE `resultId` = "
				+ uuid);
		rs = s.getResultSet();

		while (rs.next()) {
			numElevators = rs.getInt("maxElevator");
		}

		ElevatorState[] es = new ElevatorState[numElevators];
		
		// Fill array for each elevator
		for (int i = 0; i < numElevators; i++) {
			s.executeQuery("SELECT * FROM `ElevatorState` WHERE `resultId` = "
					+ uuid + " AND elevatorNumber = " + i);
			rs = s.getResultSet();

			while (rs.next()) {
				es[i] = new ElevatorState();
				es[i].setPosition(rs.getDouble("position"));
				es[i].setQuantum(rs.getLong("quantum"));
				es[i].setStatus((ServiceStatus.values()[rs.getInt("status")]));
			}
			elevatorStates.add(es);
		}
		results.setElevatorStates(elevatorStates);

		// Logged events
		s.executeQuery("SELECT * FROM `LoggedEvent` WHERE `resultId` = " + uuid);
		rs = s.getResultSet();

		while (rs.next()) {
			LoggedEvent loggedEvent = new LoggedEvent();
			loggedEvent.setQuantum(rs.getLong("quantum"));
			loggedEvent.setMessage(rs.getString("message"));
			events.add(loggedEvent);
		}
		results.setEvents(events);

		//
		// Done
		//
		conn.close();

		return results;
	}

	static public int getSimulationCountByTemplate(int templateId)
			throws SQLException {
		Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost/ElevatorDB", "root", "");
		Statement s = conn.createStatement();
		ResultSet rs = s
				.executeQuery("SELECT COUNT(*) FROM `SimulationResults` "
						+ "WHERE `templateId` = " + templateId);
		rs.next();
		int count = rs.getInt(1);

		rs.close();
		s.close();

		//
		// Done
		//
		conn.close();

		return count;
	}
}
