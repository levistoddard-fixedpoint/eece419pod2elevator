package com.pod2.elevator.web.views.simulation;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.events.EventFactory;
import com.pod2.elevator.core.events.EventSource;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.data.SimulationTemplateDetail;
import com.pod2.elevator.data.SimulationTemplateRepository;
import com.pod2.elevator.data.TemplateEvent;
import com.pod2.elevator.data.TemplateFailureEvent;
import com.pod2.elevator.data.TemplatePassengerRequest;
import com.pod2.elevator.data.TemplateServiceEvent;
import com.pod2.elevator.main.CentralController;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

/**
 * OVERVIEW: A CustomComponent which allows the user to start a new simulation
 * if none are currently running, or stop/update the curently running
 * simulation.
 * 
 */
public class ManageSimulationsView extends CustomComponent {

	private final Window parent;
	private final CentralController controller;
	private final VerticalLayout layout;

	public ManageSimulationsView(Window parent, CentralController controller) {
		super();
		assert (parent != null);
		assert (controller != null);
		this.parent = parent;
		this.controller = controller;

		layout = new VerticalLayout();
		updateView();
		setCompositionRoot(layout);
	}

	/**
	 * REQUIRES: This is currently displaying the StartSimulationView.
	 * 
	 * MODIFIES: controller, layout
	 * 
	 * EFFECTS: Starts a new simulation with the given name and template. If the
	 * simulation could be started, the RunningSimulationView is shown in this.
	 * If the simulation could not be started, an error message is displayed,
	 * and the current view is not changed.
	 * 
	 */
	void startSimulation(String name, SimulationTemplateDetail details) {
		try {
			SimulationTemplate template = SimulationTemplateRepository.getTemplate(details.getId());
			controller.startSimulation(name, template);
			updateView();
		} catch (Exception e) {
			Notification startError = new Notification("Unable to start simulation.<br>",
					e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
			parent.showNotification(startError);
		}
	}

	/**
	 * REQUIRES: This is currently displaying the RunSimulationView.
	 * 
	 * MODIFIES: controller.getSimulation
	 * 
	 * EFFECTS: Updates the settings of the currently running simulation from
	 * the input settings.
	 */
	void updateSimulationSettings(SimulationSettings settings) {
		ActiveSimulation simulation = controller.getSimulation();
		synchronized (simulation) {
			simulation.setScheduler(settings.getScheduler());
			simulation.setRequestGenerationEnabled(settings.isRequestGenerationOn());
			simulation.setDistanceBeforeService(settings.getDistanceBeforeService());
			simulation.setQuantumsBeforeService(settings.getQuantumsBeforeService());
		}
	}

	/**
	 * REQUIRES: This is currently displaying the RunSimulationView.
	 * 
	 * MODIFIES: controller.getSimulation
	 * 
	 * EFFECTS: Enqueues input event with the currently running simulation.
	 */
	void insertSimulationEvent(TemplateEvent event) {
		ActiveSimulation simulation = controller.getSimulation();
		synchronized (simulation) {
			if (event instanceof TemplatePassengerRequest) {
				TemplatePassengerRequest request = (TemplatePassengerRequest) event;
				simulation.enqueueEvent(EventFactory.createPassengerRequest(EventSource.Inserted,
						request.getQuantum(), request.getOnloadFloor(), request.getOffloadFloor(),
						request.getTimeConstraint()));
			} else if (event instanceof TemplateServiceEvent) {
				TemplateServiceEvent service = (TemplateServiceEvent) event;
				simulation
						.enqueueEvent(EventFactory.createServiceEvent(EventSource.Inserted,
								service.getQuantum(), service.getElevatorNumber(),
								service.isPutInService()));
			} else if (event instanceof TemplateFailureEvent) {
				TemplateFailureEvent failure = (TemplateFailureEvent) event;
				simulation.enqueueEvent(EventFactory.createComponentFailureEvent(
						EventSource.Inserted, failure.getQuantum(), failure.getElevatorNumber(),
						failure.getComponent()));
			} else {
				throw new RuntimeException("unexpected event type!");
			}
		}
	}

	/**
	 * REQUIRES: This is currently displaying the RunSimulationView.
	 * 
	 * MODIFIES: controller, layout
	 * 
	 * EFFECTS: Stops the currently running simulation. If the simulation could
	 * be stopped, layout is updated to display the StartSimulationView. If the
	 * simulation could not be stopped, an error message is displayed to the
	 * user, and no changes are made to the layout.
	 * 
	 */
	void stopSimulation() {
		try {
			controller.stopSimulation();
			updateView();
		} catch (Exception e) {
			Notification stopError = new Notification("Unable to stop simulation.<br>",
					e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
			parent.showNotification(stopError);
		}
	}

	/**
	 * EFFECTS: Displays the StartSimulationView if no simulation is currently
	 * running. Displays the RunningSimulationView if a simulation is currently
	 * running.
	 */
	private void updateView() {
		layout.removeAllComponents();
		ActiveSimulation simulation = controller.getSimulation();
		if (simulation == null) {
			layout.addComponent(new StartSimulationView(parent, this));
		} else {
			SimulationSettings settings = new SimulationSettings(simulation.getScheduler(),
					simulation.isRequestGenerationEnabled(), simulation.getQuantumsBeforeService(),
					simulation.getDistanceBeforeService());
			SimulationTemplate template = simulation.getTemplate();
			layout.addComponent(new RunningSimulationView(parent, this, template, settings));
		}
	}

}
