package com.pod2.elevator.web.views.simulation;

import com.pod2.elevator.core.events.EventType;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.data.TemplateEvent;
import com.pod2.elevator.web.views.common.EventConsumer;
import com.pod2.elevator.web.views.common.LayoutUtils;
import com.pod2.elevator.web.views.common.SimulationTemplateBasicFormFieldFactory;
import com.pod2.elevator.web.views.templates.AddEventWindowFactory;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

/**
 * OVERVIEW: A CustomComponent which allows a user to stop, or update the
 * settings of a currently running simulation.
 * 
 */
public class RunningSimulationView extends CustomComponent implements EventConsumer {

	private final Window parent;
	private final SimulationSettings settings;
	private final ManageSimulationsView manageView;
	private final SimulationTemplate template;
	private final AddEventWindowFactory windowFactory;
	private final VerticalLayout layout;
	private Form settingsForm;

	public RunningSimulationView(Window parent, ManageSimulationsView manageView,
			SimulationTemplate template, SimulationSettings settings) {
		super();
		this.parent = parent;
		this.settings = settings;
		this.manageView = manageView;
		this.template = template;
		this.windowFactory = new AddEventWindowFactory(this);
		layout = new VerticalLayout();
		initStopPanel();
		layout.addComponent(LayoutUtils.createSpacer());
		initSettingsPanel();
		layout.addComponent(LayoutUtils.createSpacer());
		initRequestEventsPanel();
		layout.addComponent(LayoutUtils.createSpacer());
		initServiceEventsPanel();
		layout.addComponent(LayoutUtils.createSpacer());
		initFailureEventsPanel();
		setCompositionRoot(layout);
	}

	/**
	 * MODIFIES: manageView
	 * 
	 * EFFECTS: Inserts an event into the currently running simulation.
	 */
	public void insertEvent(TemplateEvent event) {
		manageView.insertSimulationEvent(event);
	}

	/**
	 * MODIFIES: layout
	 * 
	 * EFFECTS: Displays component panel which allows user to stop the currently
	 * running simulation.
	 */
	private void initStopPanel() {
		Panel stopPanel = new Panel("Stop Simulation");
		HorizontalLayout stopLayout = new HorizontalLayout();
		stopLayout.setWidth(100, UNITS_PERCENTAGE);
		stopLayout.setMargin(true);

		Button stopButton = new Button("Stop Current Simulation", new StopClickListener());
		stopLayout.addComponent(stopButton);
		stopLayout.setComponentAlignment(stopButton, Alignment.MIDDLE_CENTER);

		stopPanel.setContent(stopLayout);
		layout.addComponent(stopPanel);
	}

	/**
	 * MODIFIES: layout
	 * 
	 * EFFECTS: Displays component panel which allows user to update the
	 * settings of the currently running simulation.
	 */
	private void initSettingsPanel() {
		Panel settingsPanel = new Panel("Simulation Settings");
		VerticalLayout settingsLayout = new VerticalLayout();
		settingsLayout.setMargin(true);

		settingsForm = new Form();
		settingsForm.setFormFieldFactory(new SimulationTemplateBasicFormFieldFactory());
		BeanItem<SimulationSettings> settingsBean = new BeanItem<SimulationSettings>(settings);
		settingsForm.setItemDataSource(settingsBean);
		settingsForm.setVisibleItemProperties(SimulationSettings.getEditFields());
		settingsForm.setImmediate(true);
		settingsForm.setWriteThrough(true);

		settingsLayout.addComponent(settingsForm);
		settingsLayout.setComponentAlignment(settingsForm, Alignment.MIDDLE_CENTER);
		settingsLayout.addComponent(LayoutUtils.createSpacer());

		Button updateButton = new Button("Update Settings", new UpdateSettingsListener());
		settingsLayout.addComponent(updateButton);

		settingsPanel.setContent(settingsLayout);
		layout.addComponent(settingsPanel);
	}

	/**
	 * MODIFIES: layout
	 * 
	 * EFFECTS: Displays component panel which allows users to enqueue a
	 * passenger request with the currently running simulation.
	 */
	private void initRequestEventsPanel() {
		insertButtonPanel("Insert Passenger Request", new Button("Insert Passenger Request",
				new InsertEventListener("Insert Passenger Request", EventType.PassengerRequest)));
	}

	/**
	 * MODIFIES: layout
	 * 
	 * EFFECTS: Displays component panel which allows users to enqueue a service
	 * request with the currently running simulation.
	 */
	private void initServiceEventsPanel() {
		insertButtonPanel("Insert Service Request", new Button("Insert Service Request",
				new InsertEventListener("Insert Service Request", EventType.ServiceRequest)));
	}

	/**
	 * MODIFIES: layout
	 * 
	 * EFFECTS: Displays component panel which allows users to enqueue a
	 * component failure event with the currently running simulation.
	 */
	private void initFailureEventsPanel() {
		insertButtonPanel("Insert Component Failure", new Button("Insert Component Failure",
				new InsertEventListener("Insert Component Failure", EventType.ComponentFailure)));
	}

	private void insertButtonPanel(String title, Button button) {
		Panel panel = new Panel(title);
		HorizontalLayout panelLayout = new HorizontalLayout();
		panelLayout.setWidth(100, UNITS_PERCENTAGE);
		panelLayout.setMargin(true);
		panelLayout.addComponent(button);
		panel.setContent(panelLayout);
		layout.addComponent(panel);
	}

	private class StopClickListener implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			manageView.stopSimulation();
		}
	}

	private class UpdateSettingsListener implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			try {
				settingsForm.commit();
				manageView.updateSimulationSettings(settings);
				Notification successMessage = new Notification("Settings updated sucessfully.",
						null, Notification.TYPE_HUMANIZED_MESSAGE);
				parent.showNotification(successMessage);
			} catch (InvalidValueException e) {
				/* Let user update fields... */
			}
		}
	}

	private class InsertEventListener implements ClickListener {

		private final String caption;
		private final EventType type;

		public InsertEventListener(String caption, EventType type) {
			this.caption = caption;
			this.type = type;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			Window eventWindow = windowFactory.createWindow(type, template);
			eventWindow.setCaption(caption);
			eventWindow.setModal(true);
			eventWindow.setWidth(400, Sizeable.UNITS_PIXELS);
			eventWindow.center();
			parent.addWindow(eventWindow);
		}

	}

}
