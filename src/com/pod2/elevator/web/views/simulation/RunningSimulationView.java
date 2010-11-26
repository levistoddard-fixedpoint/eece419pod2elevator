package com.pod2.elevator.web.views.simulation;

import com.pod2.elevator.core.events.EventType;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.data.TemplateEvent;
import com.pod2.elevator.web.views.common.AddEventWindowFactory;
import com.pod2.elevator.web.views.common.EventConsumer;
import com.pod2.elevator.web.views.common.LayoutUtils;
import com.pod2.elevator.web.views.common.SimulationTemplateBasicFormFieldFactory;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.terminal.Sizeable;
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
		initInsertEventsPanel();
		setCompositionRoot(layout);
	}

	/**
	 * MODIFIES: manageView, parent
	 * 
	 * EFFECTS: Inserts an event into the currently running simulation.
	 * Indicates to the user that the event was inserted successfully.
	 */
	public void insertEvent(TemplateEvent event) {
		manageView.insertSimulationEvent(event);
		Notification successMessage = new Notification("Event inserted successfully.", null,
				Notification.TYPE_HUMANIZED_MESSAGE);
		parent.showNotification(successMessage);
	}

	/**
	 * MODIFIES: layout
	 * 
	 * EFFECTS: Displays component which allows user to stop the currently
	 * running simulation.
	 */
	private void initStopPanel() {
		Button stopButton = new Button("Stop Current Simulation", new StopClickListener());
		insertButtonPanel("Stop Simulation", stopButton);
	}

	/**
	 * MODIFIES: layout
	 * 
	 * EFFECTS: Displays component which allows user to update the settings of
	 * the currently running simulation.
	 */
	private void initSettingsPanel() {
		Panel settingsPanel = new Panel("Update Simulation Settings");
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
		settingsLayout.addComponent(LayoutUtils.createSpacer());

		Button updateButton = new Button("Update Settings", new UpdateSettingsListener());
		settingsLayout.addComponent(updateButton);

		settingsPanel.setContent(settingsLayout);
		layout.addComponent(settingsPanel);
	}

	/**
	 * MODIFIES: layout
	 * 
	 * EFFECTS: Displays component which allows users to insert events into the
	 * currently running simulation.
	 */
	private void initInsertEventsPanel() {
		Button requestButton = new Button("Insert Passenger Request", new InsertEventListener(
				"Insert Passenger Request", EventType.PassengerRequest));
		Button serviceButton = new Button("Insert Service Request", new InsertEventListener(
				"Insert Service Request", EventType.ServiceRequest));
		Button failureButton = new Button("Insert Component Failure", new InsertEventListener(
				"Insert Component Failure", EventType.ComponentFailure));
		insertButtonPanel("Insert Events", requestButton, serviceButton, failureButton);
	}

	/**
	 * MODIFIES: layout
	 * 
	 * EFFECTS: Inserts a collection of buttons into the layout.
	 * 
	 */
	private void insertButtonPanel(String title, Button... buttons) {
		Panel panel = new Panel(title);
		HorizontalLayout panelLayout = new HorizontalLayout();
		panelLayout.setWidth(100, UNITS_PERCENTAGE);
		panelLayout.setMargin(true);
		for (Button button : buttons) {
			panelLayout.addComponent(button);
		}
		panel.setContent(panelLayout);
		layout.addComponent(panel);
	}

	/**
	 * OVERVIEW: A ClickListener which stops the currently executing simulation
	 * when clicked.
	 * 
	 */
	private class StopClickListener implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			manageView.stopSimulation();
		}
	}

	/**
	 * OVERVIEW: A ClickListener which updates the settings of the currently
	 * executing simulation when clicked.
	 * 
	 */
	private class UpdateSettingsListener implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			try {
				settingsForm.commit();
				manageView.updateSimulationSettings(settings);
				Notification successMessage = new Notification("Settings updated successfully.",
						null, Notification.TYPE_HUMANIZED_MESSAGE);
				parent.showNotification(successMessage);
			} catch (InvalidValueException e) {
				/* Let user update fields... */
			}
		}
	}

	/**
	 * OVERVIEW: A ClickListener which displays a component that allows the user
	 * to input an event of the specified type, when clicked.
	 * 
	 */
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
