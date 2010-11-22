package com.pod2.elevator.main;

import com.pod2.elevator.data.SimulationTemplateRepository;
import com.pod2.elevator.web.ControlServer;

public class CentralController {

	private final SimulationTemplateRepository templateRepository;

	public CentralController() {
		templateRepository = new SimulationTemplateRepository();
	}

	public SimulationTemplateRepository getTemplateRepository() {
		return templateRepository;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		CentralController controller = new CentralController();
		ControlServer controlServer = new ControlServer(controller);
		controlServer.start(8080);
	}

}
