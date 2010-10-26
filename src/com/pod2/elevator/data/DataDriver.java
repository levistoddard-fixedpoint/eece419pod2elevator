package data;

import java.sql.*;

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
		
		test.getTemplate(1);

		System.out.println("Done!");
		
		
	}

}
