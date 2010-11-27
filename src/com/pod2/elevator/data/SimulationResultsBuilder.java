package com.pod2.elevator.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.DeliveryStatus;
import com.pod2.elevator.core.Elevator;
import com.pod2.elevator.core.RequestInTransit;
import com.pod2.elevator.core.ResultsBuilder;
import com.pod2.elevator.core.events.Event;
import com.pod2.elevator.view.data.LogMessage;

public class SimulationResultsBuilder implements ResultsBuilder {

	private SimulationResults results;

	public SimulationResultsBuilder(String name, int templateId) {
		this.results = new SimulationResults();
		results.setName(name);
		results.setTemplateId(templateId);
	}

	public void save() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ElevatorDB", "root",
				"");

		//
		// Insert one-to-one data
		//

		String sqlQuery = "INSERT INTO `SimulationResults` (" + "`name`," + "templateId,"
				+ "startTime," + "stopTime," + "startQuantum," + "stopQuantum" + ") "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		PreparedStatement preparedStmt = conn.prepareStatement(sqlQuery);

		preparedStmt.setString(1, results.getName());
		preparedStmt.setInt(2, results.getTemplateId());
		preparedStmt.setDate(3, new java.sql.Date(results.getStartTime().getTime()));
		preparedStmt.setDate(4, new java.sql.Date(results.getStopTime().getTime()));
		preparedStmt.setLong(5, results.getStartQuantum());
		preparedStmt.setLong(6, results.getStopQuantum());

		preparedStmt.execute();

		//
		// Get results uuid
		//
		Statement s = conn.createStatement();
		s.executeQuery("SELECT * FROM `SimulationResults` ORDER BY `uuid` DESC");
		ResultSet rs = s.getResultSet();
		rs.next();
		results.setUuid(rs.getInt("uuid"));

		s.close();
		rs.close();

		//
		// Insert many-to-one attributes
		//

		//
		// Completed requests
		//
		sqlQuery = "INSERT INTO `CompletedRequest` (" + "`resultId`," + "`elevatorNumber`,"
				+ "`onloadFloor`," + "`offloadFloor`," + "`enterQuantum`," + "`onloadQuantum`,"
				+ "`offloadQuantum`," + "`timeConstraint`," + "`deliveryStatus`" + ") "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		preparedStmt = conn.prepareStatement(sqlQuery);
		Iterator<CompletedRequest> compReqs = results.getPassengerDeliveries().iterator();
		while (compReqs.hasNext()) {
			CompletedRequest compReq = compReqs.next();
			preparedStmt.setInt(1, results.getUuid());
			preparedStmt.setInt(2, compReq.getElevatorNumber());
			preparedStmt.setInt(3, compReq.getOnloadFloor());
			preparedStmt.setInt(4, compReq.getOffloadFloor());
			preparedStmt.setLong(5, compReq.getEnterQuantum());
			preparedStmt.setLong(6, compReq.getOnloadQuantum());
			preparedStmt.setLong(7, compReq.getOffloadQuantum());
			preparedStmt.setLong(8, compReq.getTimeConstraint());
			preparedStmt.setInt(9, compReq.getDeliveryStatus().ordinal());
			preparedStmt.addBatch();
		}
		preparedStmt.executeBatch();

		//
		// Elevator states
		//
		sqlQuery = "INSERT INTO `ElevatorState` (" + "`resultId`," + "`elevatorNumber`,"
				+ "`position`," + "`quantum`," + "`status`" + ") VALUES (?, ?, ?, ?, ?)";
		preparedStmt = conn.prepareStatement(sqlQuery);
		Iterator<ElevatorState[]> elevatorStates = results.getElevatorStates().iterator();
		while (elevatorStates.hasNext()) {
			ElevatorState[] states = elevatorStates.next();
			for (int i = 0; i < states.length; i++) {
				preparedStmt.setInt(1, results.getUuid());
				preparedStmt.setInt(2, i);
				preparedStmt.setDouble(3, states[i].getPosition());
				preparedStmt.setLong(4, states[i].getQuantum());
				preparedStmt.setInt(5, states[i].getStatus().ordinal());
				preparedStmt.addBatch();
			}
		}
		preparedStmt.executeBatch();

		//
		// logged events
		//
		sqlQuery = "INSERT INTO `LoggedEvent` (" + "`resultId`," + "`quantum`," + "`message`"
				+ ") VALUES (?, ?, ?)";
		preparedStmt = conn.prepareStatement(sqlQuery);
		Iterator<LoggedEvent> loggedEvents = results.getEvents().iterator();
		while (loggedEvents.hasNext()) {
			LoggedEvent loggedEvent = loggedEvents.next();
			preparedStmt.setInt(1, results.getUuid());
			preparedStmt.setLong(2, loggedEvent.getQuantum());
			preparedStmt.setString(3, loggedEvent.getMessage());
			preparedStmt.addBatch();
		}
		preparedStmt.executeBatch();

		conn.close();
	}

	public void logFinishedRequest(RequestInTransit finishedRequest) {
		CompletedRequest compReq = new CompletedRequest();
		compReq.setDeliveryStatus(finishedRequest.getDeliveryStatus());
		compReq.setElevatorNumber(finishedRequest.getElevatorNumber());
		compReq.setEnterQuantum(finishedRequest.getEnterQuantum());
		compReq.setOffloadFloor(finishedRequest.getOffloadFloor());
		compReq.setOffloadQuantum(finishedRequest.getOffloadQuantum());
		compReq.setOnloadFloor(finishedRequest.getOnloadFloor());
		compReq.setOnloadQuantum(finishedRequest.getOnloadQuantum());
		compReq.setTimeConstraint(finishedRequest.getTimeConstraint());
		results.getPassengerDeliveries().add(compReq);
	}

	public void logMessage(long timeQuantum, String message) {
		LoggedEvent logEvt = new LoggedEvent();
		logEvt.setMessage(message);
		logEvt.setQuantum(timeQuantum);
		results.getEvents().add(logEvt);
	}

	public Collection<LogMessage> getLogEntries(long quantum) {
		Collection<LogMessage> logEntries = new ArrayList<LogMessage>();
		Iterator<LoggedEvent> msgs = results.getEvents().iterator();
		while (msgs.hasNext()) {
			LoggedEvent loggedEvt = msgs.next();
			if (loggedEvt.getQuantum() == quantum) {
				logEntries.add(new LogMessage(loggedEvt.getMessage()));
			}
		}
		return logEntries;
	}

	public void logEvent(long quantum, Event event) {
		LoggedEvent logEvt = new LoggedEvent();
		logEvt.setMessage(event.toString());
		logEvt.setQuantum(quantum);
		results.getEvents().add(logEvt);
	}

	public void logRequestStateChange(long quantum, RequestInTransit request) {

		LoggedEvent event = new LoggedEvent();
		event.setMessage(request.toString());
		event.setQuantum(quantum);

		results.getEvents().add(event);

		//
		// If a passenger was delivered, add to
		// passengerDeliveries
		//
		if (request.getDeliveryStatus() == DeliveryStatus.Delivered
				|| request.getDeliveryStatus() == DeliveryStatus.Rescued) {

			CompletedRequest completedRequest = new CompletedRequest();

			completedRequest.setDeliveryStatus(request.getDeliveryStatus());
			completedRequest.setElevatorNumber(request.getElevatorNumber());
			completedRequest.setEnterQuantum(request.getEnterQuantum());
			completedRequest.setOnloadQuantum(request.getOnloadQuantum());
			completedRequest.setOffloadQuantum(request.getOffloadQuantum());
			completedRequest.setOnloadFloor(request.getOffloadFloor());
			completedRequest.setOffloadFloor(request.getOffloadFloor());
			completedRequest.setTimeConstraint(request.getTimeConstraint());

			results.getPassengerDeliveries().add(completedRequest);
		}
	}

	public void onEnd(long quantum) {
		results.setStopTime(new Date());
		results.setStopQuantum(quantum);
	}

	public void onStart() {
		results.setStartTime(new Date());
	}

	public void logCompletedQuantum(long quantum, ActiveSimulation activeSimulation) {
		//
		// log elevator states
		//
		Elevator[] elevators = activeSimulation.getElevators();
		ElevatorState[] states = new ElevatorState[elevators.length];
		for (int i = 0; i < elevators.length; i++) {
			states[i] = new ElevatorState();
			states[i].setPosition(elevators[i].getPosition());
			states[i].setQuantum(activeSimulation.getCurrentQuantum());
			states[i].setStatus(elevators[i].getServiceStatus());
		}
		results.getElevatorStates().add(states);
	}
}
