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

public class ManageSimulationsView extends CustomComponent {

	private final Window parent;
	private final CentralController controller;

	private VerticalLayout layout;

	public ManageSimulationsView(Window parent, CentralController controller) {
		super();
		this.parent = parent;
		this.controller = controller;
		initLayout();
	}

	public void initLayout() {
		layout = new VerticalLayout();
		updateView();
		setCompositionRoot(layout);
	}

	void startSimulation(String name, SimulationTemplateDetail details) {
		try {
			SimulationTemplate template = null;
			if (details != null) {
				template = SimulationTemplateRepository.getTemplate(details.getId());
			} else {
				template = new SimulationTemplate();
				template.setName("N/A");
				template.setDistanceBeforeService(-1);
				template.setQuantumsBeforeService(-1);
			}
			controller.startSimulation(name, template);
			updateView();
		} catch (Exception e) {
			Notification databaseError = new Notification("Unable to start simulation.<br>",
					e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
			parent.showNotification(databaseError);
		}
	}

	void updateSimulationSettings(SimulationSettings settings) {
		ActiveSimulation simulation = controller.getSimulation();
		synchronized (simulation) {
			simulation.setScheduler(settings.getScheduler());
			simulation.setRequestGenerationEnabled(settings.isRequestGenerationOn());
			simulation.setDistanceBeforeService(settings.getDistanceBeforeService());
			simulation.setQuantumsBeforeService(settings.getQuantumsBeforeService());
		}
	}

	void insertSimulationEvent(TemplateEvent event) {
		ActiveSimulation simulation = controller.getSimulation();
		synchronized (event) {
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
				throw new RuntimeException("unrecognized event type!");
			}
		}
	}

	void stopSimulation() {
		try {
			controller.stopSimulation();
			updateView();
		} catch (Exception e) {
			Notification databaseError = new Notification("Unable to stop simulation.<br>",
					e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
			parent.showNotification(databaseError);
		}
	}

	private void updateView() {
		layout.removeAllComponents();
		ActiveSimulation simulation = controller.getSimulation();
		if (simulation == null) {
			layout.addComponent(new StartSimulationView(parent, this));
		} else {
			SimulationSettings settings = new SimulationSettings(simulation.getScheduler(),
					simulation.isRequestGenerationEnabled(), simulation.getQuantumsBeforeService(),
					simulation.getDistanceBeforeService());
			layout.addComponent(new RunningSimulationView(this, settings));
		}
	}

}
