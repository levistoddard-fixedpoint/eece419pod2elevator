package com.pod2.elevator.main;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.ResultsBuilder;
import com.pod2.elevator.core.SimulationDisplay;
import com.pod2.elevator.data.DummyResultsBuilder;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.view.SimulationView;
import com.pod2.elevator.view.SimulationWindow;
import com.pod2.elevator.web.ControlServer;
import com.pod2.elevator.web.ControlServerException;

public class CentralController {

	private static final int DEFAULT_SERVER_PORT = 8080;

	private final SimulationDisplay display;

	private ActiveSimulation simulation = null;
	private ControlServer controlServer;

	private CentralController(SimulationDisplay display) {
		this.display = display;
		this.controlServer = new ControlServer(this);
	}

	public void start() throws Exception {
		controlServer.start(DEFAULT_SERVER_PORT);
	}

	public void startSimulation(String name, SimulationTemplate template)
			throws SimulationAlreadyRunningException {
		if (simulation != null) {
			throw new SimulationAlreadyRunningException("Another simulation is currently running.");
		}
		// ResultsBuilder results =
		// SimulationDataRepository.getSimulationResultsBuilder(name,
		// template);
		ResultsBuilder results = new DummyResultsBuilder();
		simulation = new ActiveSimulation(template, results, display);
		simulation.start();
	}

	public ActiveSimulation getSimulation() {
		return simulation;
	}

	public void stopSimulation() throws UnableToStopSimulationException {
		if (simulation == null) {
			/* No simulation was running. */
			return;
		}
		try {
			simulation.stop();
			simulation = null;
		} catch (Exception e) {
			throw new UnableToStopSimulationException(e);
		}
	}

	public void restartWebServer(int port) throws ControlServerException {
		controlServer.stop();
		controlServer.start(port);
	}

	public static void main(String[] args) throws Exception {
		try {
			SimulationWindow window = new SimulationWindow(5, 5);
			SimulationView view = new SimulationView(window);
			CentralController controller = new CentralController(view);
			controller.start();
		} catch (Exception e) {
			System.err.print("Unable to start simulator:\n\t");
			System.err.print(e.getMessage());
		}
	}

}
