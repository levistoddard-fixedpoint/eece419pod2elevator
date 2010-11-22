package com.pod2.elevator.web.views.templates;

import java.util.Arrays;
import java.util.List;

import com.pod2.elevator.core.component.ComponentDetails;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.data.SimulationTemplateRepository;
import com.pod2.elevator.data.TemplateServiceEvent;
import com.pod2.elevator.web.views.EditWindow;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CreateEditTemplateWindow extends EditWindow {

	private static final List<String> BASIC_FIELDS = Arrays.asList("name", "numberFloors",
			"numberElevators", "scheduler");

	private VerticalLayout layout;

	private final Window parent;
	private final SimulationTemplateRepository repository;
	private final SimulationTemplate template;

	private final ClickListener requestListener = new AddRequestClickListener();
	private final ClickListener serviceListener = new AddServiceClickListener();
	private final ClickListener failureListener = new AddFailureClickListener();

	private Form basicInfo;
	private Table requests;
	private Table componentFailures;
	private Table serviceRequests;

	public CreateEditTemplateWindow(Window parent, SimulationTemplateRepository repository,
			SimulationTemplate template) {
		super();
		this.parent = parent;
		this.repository = repository;
		this.template = template;
		setCaption(template.getId() >= 0 ? "Edit Template" : "Create Template");
		super.render();
	}

	@Override
	protected Component getEditControls() {
		layout = new VerticalLayout();
		initBasicInfo();
		layout.addComponent(getSpacer());
		initRequestsTable();
		layout.addComponent(getSpacer());
		initServiceRequestsTable();
		layout.addComponent(getSpacer());
		initComponentFailuresTable();
		layout.addComponent(getSpacer());
		return layout;
	}

	@Override
	protected void onSave() {
		System.err.println(template.toString());
	}

	private class AddRequestClickListener implements ClickListener {

		private int nextId = -1;

		@Override
		public void buttonClick(ClickEvent event) {

		}

	}

	private class AddServiceClickListener implements ClickListener {

		private int nextId = -1;

		@Override
		public void buttonClick(ClickEvent event) {

		}
	}

	private class AddFailureClickListener implements ClickListener {

		private int nextId = -1;

		@Override
		public void buttonClick(ClickEvent event) {

		}

	}

	private void initBasicInfo() {
		basicInfo = new Form();
		basicInfo.setItemDataSource(new BeanItem<SimulationTemplate>(template));
		basicInfo.setFormFieldFactory(new BasicInfoFieldFactory());
		basicInfo.setWriteThrough(true);
		basicInfo.setVisibleItemProperties(BASIC_FIELDS);

		GridLayout formLayout = new GridLayout(2, 2);
		formLayout.setSpacing(true);
		basicInfo.setLayout(formLayout);

		layout.addComponent(basicInfo);
	}

	private void initRequestsTable() {
		requests = new Table();
		requests.addContainerProperty(PassengerFields.Quantum, Long.class, null);
		requests.addContainerProperty(PassengerFields.OnloadFloor, Integer.class, null);
		requests.addContainerProperty(PassengerFields.OffloadFloor, Integer.class, null);
		requests.addContainerProperty(PassengerFields.TimeConstraint, Long.class, null);
		requests.addContainerProperty(PassengerFields.Delete, Button.class, null);

		for (TemplateServiceEvent event : template.getServiceEvents()) {
			
		}
		layout.addComponent(new Button("Add", requestListener));
		layout.addComponent(requests);
	}

	private void initServiceRequestsTable() {
		serviceRequests = new Table();
		serviceRequests.addContainerProperty(ServiceFields.Quantum, Long.class, null);
		serviceRequests.addContainerProperty(ServiceFields.Elevator, Integer.class, null);
		serviceRequests.addContainerProperty(ServiceFields.Request, Boolean.class, null);
		serviceRequests.addContainerProperty(ServiceFields.Delete, Button.class, null);

		layout.addComponent(new Button("Add", serviceListener));
		layout.addComponent(serviceRequests);
	}

	private void initComponentFailuresTable() {
		componentFailures = new Table();
		componentFailures.addContainerProperty(FailureFields.Quantum, Long.class, null);
		componentFailures.addContainerProperty(FailureFields.Elevator, Integer.class, null);
		componentFailures.addContainerProperty(FailureFields.Component, ComponentDetails.class,
				null);
		componentFailures.addContainerProperty(FailureFields.Delete, Button.class, null);

		layout.addComponent(new Button("Add", failureListener));
		layout.addComponent(componentFailures);
	}

	private Component getSpacer() {
		return new Label("&nbsp;", Label.CONTENT_XHTML);
	}

}
