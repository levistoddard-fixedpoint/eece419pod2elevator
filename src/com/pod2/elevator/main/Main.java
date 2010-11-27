package com.pod2.elevator.main;

import java.io.FileInputStream;
import java.util.Properties;

import com.pod2.elevator.data.DatabaseUtils;
import com.pod2.elevator.data.InvalidDatabaseConfigurationException;
import com.pod2.elevator.view.SimulationView;
import com.pod2.elevator.view.SimulationWindow;

public class Main {

	private static void showUsageAndDie(String reason) {
		System.err.println("usage:\n\tjava -jar simulator.jar <path to database configuration>");
		System.err.println("reason:\n\t" + reason);
		System.exit(1);
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			showUsageAndDie("Must supply path to database configuration file.");
		}

		Properties databaseConfig = new Properties();
		try {
			databaseConfig.load(new FileInputStream(args[0]));
		} catch (Exception e) {
			showUsageAndDie(e.getMessage());
		}

		try {
			DatabaseUtils.initialize(databaseConfig);
		} catch (InvalidDatabaseConfigurationException e) {
			showUsageAndDie(e.getMessage());
		}

		try {
			SimulationWindow window = new SimulationWindow();
			SimulationView view = new SimulationView(window);
			CentralController controller = new CentralController(view);
			window.setCentralController(controller);
			controller.start();
		} catch (Exception e) {
			System.err.print("Unable to start simulator:\n\t");
			System.err.print(e.getMessage());
		}
	}
}
