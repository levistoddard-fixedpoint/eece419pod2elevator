package com.pod2.elevator.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.pod2.elevator.core.DeliveryStatus;
import com.pod2.elevator.core.ServiceStatus;

public class SimulationDataRepository {
	static public List<SimulationDetail> getCompletedSimulations() throws SQLException {

		List<SimulationDetail> completedSimulations = new LinkedList<SimulationDetail>();

		Connection conn = DatabaseUtils.getConnection();

		Statement s = conn.createStatement();
		s.executeQuery("SELECT `uuid`,`name` FROM `SimulationResults`");
		ResultSet rs = s.getResultSet();

		while (rs.next()) {
			SimulationDetail simulationDetail = new SimulationDetail(rs.getInt("uuid"),
					rs.getString("name"));
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

	static public SimulationResultsBuilder getSimulationResultsBuilder(String name,
			SimulationTemplate template) {
		return new SimulationResultsBuilder(name, template.getId());
	}

	static public SimulationResults getSimulationResults(int uuid) throws SQLException {
		SimulationResults results = new SimulationResults();
		List<CompletedRequest> passengerDeliveries = new Vector<CompletedRequest>();
		List<ElevatorState[]> elevatorStates = new Vector<ElevatorState[]>();
		int numElevators = 0;
		List<LoggedEvent> events = new Vector<LoggedEvent>();

		Connection conn = DatabaseUtils.getConnection();

		Statement s = conn.createStatement();
		s.executeQuery("SELECT * FROM `SimulationResults` WHERE `uuid` = " + uuid);
		ResultSet rs = s.getResultSet();
		rs.next();

		//
		// One-to-one attributes
		//
		results.setUuid(uuid);
		results.setName(rs.getString("name"));
		results.setTemplateId(rs.getInt("templateId"));
		results.setStartTime(rs.getTimestamp("startTime"));
		results.setStopTime(rs.getTimestamp("stopTime"));
		results.setStartQuantum(rs.getLong("startQuantum"));
		results.setStopQuantum(rs.getLong("stopQuantum"));

		//
		// Many-to-one attributes
		//

		// Completed requests
		s.executeQuery("SELECT * FROM `CompletedRequest` WHERE `resultId` = " + uuid);
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
			rq.setDeliveryStatus(DeliveryStatus.values()[rs.getInt("deliveryStatus")]);
			passengerDeliveries.add(rq);
		}
		results.setPassengerDeliveries(passengerDeliveries);

		// Elevator States

		// Find Elevator Max
		s.executeQuery("SELECT MAX(elevatorNumber) AS maxElevator FROM `ElevatorState` WHERE `resultId` = "
				+ uuid);
		rs = s.getResultSet();

		while (rs.next()) {
			numElevators = rs.getInt("maxElevator");
		}
		
		ArrayList<ElevatorState> esArrayList;
		ElevatorState[] esArray = new ElevatorState[0];

		// Fill array for each elevator
		for (int i = 0; i <= numElevators; i++) {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM `ElevatorState` WHERE `resultId` = ?"
					+ " AND elevatorNumber = ?");
			ps.setInt(1, uuid);
			ps.setInt(2, i);
			ps.execute();
			rs = ps.getResultSet();

			esArrayList = new ArrayList<ElevatorState>();
			while (rs.next()) {
				ElevatorState es = new ElevatorState();
				es.setPosition(rs.getDouble("position"));
				es.setQuantum(rs.getLong("quantum"));
				es.setStatus((ServiceStatus.values()[rs.getInt("status")]));
				esArrayList.add(es);
			}
			elevatorStates.add(esArrayList.toArray(esArray));
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

	static public int getSimulationCountByTemplate(int templateId) throws SQLException {
		Connection conn = DatabaseUtils.getConnection();
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM `SimulationResults` "
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
