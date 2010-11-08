package com.pod2.elevator.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SimulationDataRepository {
	public List<SimulationDetail>getCompletedSimulations() {
		return null;
	}
	
	public SimulationResultsBuilder getSimulationResultsBuilder(String name,
			SimulationTemplate template) {
		return null;
	}
	
	public SimulationResults getSimulationResults(int uuid) {
		return null;
	}
	
	public int getSimulationCountByTemplate(int templateId) throws SQLException {
		int count;
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ElevatorDB", "root", "");
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM `SimulationResults` " +
				"WHERE `templateId` = " + templateId);
		count = rs.getInt(1);
		
		rs.close();
		s.close();
		conn.close();
		
		return count;
	}
}
