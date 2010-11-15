package com.pod2.elevator.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

public class SimulationDataRepository {
	public List<SimulationDetail>getCompletedSimulations() throws SQLException {
			
		List<SimulationDetail> completedSimulations = new Vector<SimulationDetail>();
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ElevatorDB", "root", "");
		
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
	
	public SimulationResultsBuilder getSimulationResultsBuilder(String name,
			SimulationTemplate template) {
		return null;
	}
	
	public SimulationResults getSimulationResults(int uuid) throws SQLException {
		SimulationResults results = new SimulationResults();
		List<CompletedRequest> passengerDeliveries = new Vector<CompletedRequest>();
		List<LoggedEvent> events = new Vector<LoggedEvent>();
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ElevatorDB", "root", "");
		
		Statement s = conn.createStatement();
		s.executeQuery("SELECT * FROM `SimulationTemplate` WHERE `uuid` = " + uuid);
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
			//rq.setDeliveryStatus(rs.getInt("deliveryStatus"));
			passengerDeliveries.add(rq);
		}
		results.setPassengerDeliveries(passengerDeliveries);
		
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
	
	public int getSimulationCountByTemplate(int templateId) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ElevatorDB", "root", "");
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM `SimulationResults` " +
				"WHERE `templateId` = " + templateId);
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
