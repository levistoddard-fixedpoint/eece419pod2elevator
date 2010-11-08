package data;

import java.sql.*;
import java.util.List;
import java.util.Vector;

public class SimulationTemplateRepository {

	public void createTemplate(SimulationTemplate template) {
		
	}
	
	public void updateTemplate(SimulationTemplate template) {
		
	}
	
	public void deleteTemplate(int id) {
		
	}
	
	public List<SimulationTemplateDetail> getAllTemplates() {
		// incomplete
		return null;
	}
	
	public SimulationTemplate getTemplate(int id) throws SQLException {
		SimulationTemplate template = new SimulationTemplate();
		List<TemplatePassengerRequest> passengerRequests = new Vector<TemplatePassengerRequest>();
		
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ElevatorDB", "root", "");
		Statement s = conn.createStatement();
		s.executeQuery("SELECT * FROM `SimulationTemplate` WHERE `id` = " + id);
		ResultSet rs = s.getResultSet();
		rs.next();

		// Get one-to-one template attributes
		template.id = id;
		template.numberFloors = rs.getInt("numberFloors");
		template.elevatorCapacity = rs.getInt("elevatorCapacity");
		template.numberElevators = rs.getInt("numberElevators");
		// template.getElevatorScheduler
		template.requestGenerationOn = rs.getBoolean("requestGenerationOn");

		// Get many-to-one template attributes
		s.executeQuery("SELECT * FROM `TemplatePassengerRequest` WHERE `templateId` = " + id);
		rs = s.getResultSet();
		
		while (rs.next()) {
			TemplatePassengerRequest req = new TemplatePassengerRequest();
			req.onloadFloor = rs.getInt("onloadFloor");
			req.offloadFloor = rs.getInt("offloadFloor");
			req.timeConstraint = rs.getLong("timeConstraint");
			passengerRequests.add(req);
		}
		template.passengerRequests = passengerRequests;

		rs.close();
		s.close();
		conn.close(); 
		
		return template;

	}
}
