package com.pod2.elevator.web.views.simulation;

import com.pod2.elevator.web.views.common.LayoutUtils;
import com.pod2.elevator.web.views.common.SimulationTemplateBasicFormFieldFactory;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class RunningSimulationView extends CustomComponent {

	private final SimulationSettings settings;
	private final ManageSimulationsView manageView;
	private final VerticalLayout layout;

	private Form settingsForm;

	public RunningSimulationView(ManageSimulationsView manageView, SimulationSettings settings) {
		super();
		this.settings = settings;
		this.manageView = manageView;
		layout = new VerticalLayout();
		initStopPanel();
		layout.addComponent(LayoutUtils.createSpacer());
		initSettingsPanel();
		layout.addComponent(LayoutUtils.createSpacer());
		initEventsPanel();
		setCompositionRoot(layout);
	}

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

	private void initSettingsPanel() {
		Panel settingsPanel = new Panel("Simulation Settings");
		VerticalLayout settingsLayout = new VerticalLayout();
		settingsLayout.setMargin(true);

		settingsForm = new Form();
		BeanItem<SimulationSettings> settingsBean = new BeanItem<SimulationSettings>(settings);
		settingsForm.setItemDataSource(settingsBean);
		settingsForm.setVisibleItemProperties(SimulationSettings.getEditFields());
		settingsForm.setFormFieldFactory(new SimulationTemplateBasicFormFieldFactory());
		settingsForm.setImmediate(true);
		settingsForm.setWriteThrough(true);
		settingsLayout.addComponent(settingsForm);
		settingsLayout.addComponent(LayoutUtils.createSpacer());

		Button updateSettings = new Button("Update Settings", new UpdateSettingsListener());
		settingsLayout.addComponent(updateSettings);

		settingsPanel.setContent(settingsLayout);
		layout.addComponent(settingsPanel);
	}

	private void initEventsPanel() {
		Panel eventsPanel = new Panel("Simulation Events");

		layout.addComponent(eventsPanel);
	}

	private class UpdateSettingsListener implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			try {
				settingsForm.commit();
				manageView.updateSimulationSettings(settings);
			} catch (InvalidValueException e) {
				/* Let user update fields... */
			}
		}
	}

	private class StopClickListener implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			manageView.stopSimulation();
		}
	}

}
