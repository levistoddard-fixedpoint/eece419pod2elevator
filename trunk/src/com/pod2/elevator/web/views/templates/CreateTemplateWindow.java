package com.pod2.elevator.web.views.templates;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.pod2.elevator.core.component.ComponentDetails;
import com.pod2.elevator.core.events.EventType;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.data.SimulationTemplateRepository;
import com.pod2.elevator.data.TemplateEvent;
import com.pod2.elevator.data.TemplateFailureEvent;
import com.pod2.elevator.data.TemplatePassengerRequest;
import com.pod2.elevator.data.TemplateServiceEvent;
import com.pod2.elevator.scheduling.ElevatorScheduler;
import com.pod2.elevator.scheduling.SchedulerRegistry;
import com.pod2.elevator.web.validator.PositiveIntegerValidator;
import com.pod2.elevator.web.validator.PositiveNumberValidator;
import com.pod2.elevator.web.views.EditWindow;
import com.pod2.elevator.web.views.tables.FailureFields;
import com.pod2.elevator.web.views.tables.PassengerFields;
import com.pod2.elevator.web.views.tables.ServiceFields;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class CreateTemplateWindow extends EditWindow {

	private static final String FIELD_WIDTH = "15em";

	public static final List<String> BASIC_FIELDS = Arrays.asList("name", "numberFloors",
			"numberElevators", "scheduler", "requestGenerationOn", "speed", "elevatorCapacity",
			"quantumsBeforeService", "distanceBeforeService");

	private VerticalLayout layout;

	private final ManageTemplatesView manageView;
	private final Window parent;
	private final SimulationTemplateRepository repository;
	private final SimulationTemplate template;
	private final AddEventWindowFactory windowFactory;

	private Form basicInfo;

	private Select restrictedFloors;
	private Table requestTable;
	private Table failureTable;
	private Table serviceTable;

	/**
	 * Restricted floors multi-select binds to this container to determine
	 * presents which floors are selectable.
	 */
	private int previousNumElevators;
	private int previousNumFloors;
	private BeanItemContainer<Integer> availableFloors;

	public CreateTemplateWindow(ManageTemplatesView manageView, Window parent,
			SimulationTemplateRepository repository, SimulationTemplate template) {
		super();
		this.manageView = manageView;
		this.parent = parent;
		this.repository = repository;
		this.template = template;

		windowFactory = new AddEventWindowFactory(this);
		availableFloors = new BeanItemContainer<Integer>(Integer.class);
		previousNumFloors = template.getNumberFloors();
		for (int floor = 0; floor < template.getNumberFloors(); floor++) {
			availableFloors.addBean(floor);
		}
		previousNumElevators = template.getNumberElevators();

		setCaption("Create Template");
		super.render();
	}

	void insertEvent(TemplateEvent event) {
		if (event instanceof TemplatePassengerRequest) {
			insertEvent(requestTable, event);
		} else if (event instanceof TemplateServiceEvent) {
			insertEvent(serviceTable, event);
		} else if (event instanceof TemplateFailureEvent) {
			insertEvent(failureTable, event);
		}
	}

	@Override
	protected Component getEditControls() {
		layout = new VerticalLayout();
		initBasicInfo();
		layout.addComponent(createSpacer());
		initRestrictedFloors();
		layout.addComponent(createSpacer());
		initRequestsTable();
		layout.addComponent(createSpacer());
		initServiceRequestsTable();
		layout.addComponent(createSpacer());
		initComponentFailuresTable();
		layout.addComponent(createSpacer());
		return layout;
	}

	@Override
	protected void onSave() {
		basicInfo.commit();
		if (basicInfo.isValid()) {
			template.setLastEdit(new Date());
			template.setRestrictedFloors(getRestrictedFloors());
			template.setPassengerRequests(getRequestEvents());
			template.setServiceEvents(getServiceEvents());
			template.setFailureEvents(getFailureEvents());
			try {
				SimulationTemplateRepository.createTemplate(template);
				manageView.templateCreated(template);
				close();
			} catch (Exception e) {
				Notification databaseError = new Notification("Error creating template.<br>",
						e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
				parent.showNotification(databaseError);
			}
		}
	}

	private class NumberFloorsValueChanged implements ValueChangeListener {
		@Override
		public void valueChange(ValueChangeEvent event) {
			if (requestTable == null) {
				/* set-up before the tables */
				return;
			}
			Integer newNumFloors = (Integer) event.getProperty().getValue();
			if (newNumFloors == null) {
				newNumFloors = Integer.valueOf(0);
			}
			if (!isLargerThanAllColumnValues(requestTable, newNumFloors,
					PassengerFields.OnloadFloor, PassengerFields.OffloadFloor)) {
				Notification updateNotification = new Notification(
						"Floor in use.<br>",
						"There are passenger requests set to onload or offload "
								+ "on the removed floors.<br> Please delete these requests before lowering the "
								+ "number of floors.", Notification.TYPE_WARNING_MESSAGE);
				updateNotification.setDelayMsec(-1);
				parent.showNotification(updateNotification);
				event.getProperty().setValue(previousNumFloors);
				return;
			}
			updateAvailableFloors(newNumFloors);
		}
	}

	private class NumberElevatorsValueChanged implements ValueChangeListener {
		@Override
		public void valueChange(ValueChangeEvent event) {
			if (serviceTable == null || failureTable == null) {
				return;
			}
			Integer numElevators = (Integer) event.getProperty().getValue();
			if (numElevators == null) {
				numElevators = Integer.valueOf(0);
			}
			boolean cantUpdate = false;
			String cantUpdateMessage = "";
			if (!isLargerThanAllColumnValues(serviceTable, numElevators, ServiceFields.Elevator)) {
				cantUpdate = true;
				cantUpdateMessage = "There are service requests set to act on the removed elevators.<br>"
						+ " Please delete these requests before lowering the "
						+ "number of elevators.";
			} else if (!isLargerThanAllColumnValues(failureTable, numElevators,
					FailureFields.Elevator)) {
				cantUpdate = true;
				cantUpdateMessage = "There are failure requests set to act on the removed elevators.<br>"
						+ " Please delete these requests before lowering the "
						+ "number of elevators.";
			}
			if (cantUpdate) {
				Notification updateNotification = new Notification("Elevator in use.<br>",
						cantUpdateMessage, Notification.TYPE_WARNING_MESSAGE);
				updateNotification.setDelayMsec(-1);
				parent.showNotification(updateNotification);
				event.getProperty().setValue(previousNumElevators);
				return;
			}
			previousNumElevators = numElevators;
		}
	}

	private boolean isLargerThanAllColumnValues(Table table, Integer value, Object... colIds) {
		HashSet<Integer> values = new HashSet<Integer>();
		for (Object colId : colIds) {
			values.addAll(getIntegerColumnAsSet(table, colId));
		}
		for (Integer colValue : values) {
			if (colValue >= value) {
				return false;
			}
		}
		return true;
	}

	private HashSet<Integer> getIntegerColumnAsSet(Table table, Object colId) {
		HashSet<Integer> values = new HashSet<Integer>();
		if (table == null) {
			return values;
		}
		for (Object itemId : table.getItemIds()) {
			Item item = table.getItem(itemId);
			Property valueProperty = item.getItemProperty(colId);
			values.add((Integer) valueProperty.getValue());
		}
		return values;
	}

	private class BasicInfoFieldFactory implements FormFieldFactory {

		private static final int MAX_ELEVATORS = 10;
		private static final int MAX_FLOORS = 50;
		private static final int MAX_CAPACITY = 20;

		@Override
		public Field createField(Item item, Object propertyId, Component uiContext) {
			String pid = (String) propertyId;
			if (pid.equals("name")) {
				final int MIN_LEN = 1;
				final int MAX_LEN = 20;

				TextField name = new TextField("Name:");
				name.setWidth(FIELD_WIDTH);
				name.setRequired(true);
				name.setRequiredError("Please enter a template name.");
				name.addValidator(new StringLengthValidator("Name must be between " + MIN_LEN
						+ " and " + MAX_LEN + " characters.", MIN_LEN, MAX_LEN, false));
				return name;
			} else if (pid.equals("numberFloors")) {
				Select numberFloors = createIntegerSelect("Number of Floors:",
						"Number floors must be a positive integer.", MAX_FLOORS);
				numberFloors.addListener(new NumberFloorsValueChanged());
				return numberFloors;
			} else if (pid.equals("numberElevators")) {
				Select numberElevators = createIntegerSelect("Number of Elevators:",
						"Number elevators must be a positive integer.", MAX_ELEVATORS);
				numberElevators.addListener(new NumberElevatorsValueChanged());
				return numberElevators;
			} else if (pid.equals("scheduler")) {
				Select schedulers = new Select("Scheduling Algorithm:");
				schedulers.setWidth(FIELD_WIDTH);
				schedulers.setRequired(true);
				schedulers.setRequiredError("Please select a scheduler.");
				for (ElevatorScheduler scheduler : SchedulerRegistry.getAvailableSchedulers()) {
					schedulers.addItem(scheduler);
				}
				return schedulers;
			} else if (pid.equals("requestGenerationOn")) {
				return new CheckBox("Random request generation enabled");
			} else if (pid.equals("speed")) {
				TextField speed = new TextField("Speed (floors / quantum):");
				speed.setWidth(FIELD_WIDTH);
				speed.setRequired(true);
				speed.setRequiredError("Please enter an elevator speed.");
				speed.addValidator(new PositiveNumberValidator(
						"Elevator speed must be a positive number."));
				return speed;
			} else if (pid.equals("elevatorCapacity")) {
				return createIntegerSelect("Elevator Passenger Capacity:",
						"Capacity must be a positive integer", MAX_CAPACITY);
			} else if (pid.equals("quantumsBeforeService")) {
				TextField quantums = new TextField("Time Before Service (quantums):");
				quantums.setWidth(FIELD_WIDTH);
				quantums.setRequired(true);
				quantums.setRequiredError("Please enter quantums before required service.");
				quantums.addValidator(new PositiveIntegerValidator(
						"Time must be a positive integer."));
				return quantums;
			} else if (pid.equals("distanceBeforeService")) {
				TextField distance = new TextField("Distance Before Service (floors):");
				distance.setWidth(FIELD_WIDTH);
				distance.setRequired(true);
				distance.setRequiredError("Please enter distance before required service.");
				distance.addValidator(new PositiveNumberValidator(
						"Distance must be a positive number."));
				return distance;
			}
			throw new RuntimeException("unknown property: " + pid);
		}

		private Select createIntegerSelect(String label, String failureMessage, int maxNumber) {
			Select selectInput = new Select(label);
			selectInput.setWidth(FIELD_WIDTH);
			selectInput.setRequired(true);
			selectInput.setRequiredError(failureMessage);
			selectInput.setWriteThrough(true);
			selectInput.setReadThrough(true);
			selectInput.setImmediate(true);
			selectInput.addValidator(new PositiveIntegerValidator(failureMessage));
			for (int n = 1; n <= maxNumber; n++) {
				selectInput.addItem(n);
			}
			return selectInput;
		}

	}

	private class DeleteEventListener implements ClickListener {

		private Table events;
		private Object itemId;

		private DeleteEventListener(Table events, Object itemId) {
			this.events = events;
			this.itemId = itemId;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			events.removeItem(itemId);
		}
	}

	private class AddEventListener implements ClickListener {

		private final EventType type;
		private final String caption;

		public AddEventListener(EventType type, String caption) {
			this.type = type;
			this.caption = caption;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			showWindow(caption, windowFactory.createWindow(type, template));
		}

	}

	private void initBasicInfo() {
		basicInfo = new Form();
		basicInfo.setItemDataSource(new BeanItem<SimulationTemplate>(template));
		basicInfo.setFormFieldFactory(new BasicInfoFieldFactory());
		basicInfo.setWriteThrough(true);
		basicInfo.setVisibleItemProperties(BASIC_FIELDS);
		basicInfo.setValidationVisibleOnCommit(true);

		GridLayout formLayout = new GridLayout(3, 3);
		formLayout.setSpacing(true);
		basicInfo.setLayout(formLayout);

		layout.addComponent(basicInfo);
	}

	private void initRestrictedFloors() {
		final String RESTRICTED_FLOORS_HEIGHT = "100px";

		restrictedFloors = new Select("Restricted Floors:");
		restrictedFloors.setHeight(RESTRICTED_FLOORS_HEIGHT);
		restrictedFloors.setWidth(FIELD_WIDTH);
		restrictedFloors.setMultiSelect(true);
		restrictedFloors.setNullSelectionAllowed(true);
		restrictedFloors.setReadThrough(true);
		restrictedFloors.setWriteThrough(true);
		restrictedFloors.setImmediate(true);
		restrictedFloors.setContainerDataSource(availableFloors);
		layout.addComponent(restrictedFloors);
	}

	private void initRequestsTable() {
		requestTable = createTable();
		requestTable.addContainerProperty(PassengerFields.Quantum, Long.class, null);
		requestTable.addContainerProperty(PassengerFields.OnloadFloor, Integer.class, null);
		requestTable.addContainerProperty(PassengerFields.OffloadFloor, Integer.class, null);
		requestTable.addContainerProperty(PassengerFields.TimeConstraint, Long.class, null);
		requestTable.addContainerProperty(PassengerFields.Delete, Button.class, null);
		insertEvents(requestTable, template.getPassengerRequests());

		layout.addComponent(createButtonPanel(new Label("Passenger Request Events"), new Button(
				"Add", new AddEventListener(EventType.PassengerRequest,
						"Add Passenger Request Event"))));

		layout.addComponent(createSpacer());
		layout.addComponent(requestTable);
	}

	private void initServiceRequestsTable() {
		serviceTable = createTable();
		serviceTable.addContainerProperty(ServiceFields.Quantum, Long.class, null);
		serviceTable.addContainerProperty(ServiceFields.Elevator, Integer.class, null);
		serviceTable.addContainerProperty(ServiceFields.Request, Boolean.class, null);
		serviceTable.addContainerProperty(ServiceFields.Delete, Button.class, null);
		insertEvents(serviceTable, template.getServiceEvents());

		layout.addComponent(createButtonPanel(new Label("Forced Servicing Events"), new Button(
				"Add", new AddEventListener(EventType.ServiceRequest, "Add Forced Service Event"))));

		layout.addComponent(createSpacer());
		layout.addComponent(serviceTable);
	}

	private void initComponentFailuresTable() {
		failureTable = createTable();
		failureTable.addContainerProperty(FailureFields.Quantum, Long.class, null);
		failureTable.addContainerProperty(FailureFields.Elevator, Integer.class, null);
		failureTable.addContainerProperty(FailureFields.Component, ComponentDetails.class, null);
		failureTable.addContainerProperty(FailureFields.Delete, Button.class, null);
		insertEvents(failureTable, template.getFailureEvents());

		layout.addComponent(createButtonPanel(new Label("Forced Failure Event"), new Button("Add",
				new AddEventListener(EventType.ComponentFailure, "Add Forced Failure Event"))));

		layout.addComponent(createSpacer());
		layout.addComponent(failureTable);
	}

	private void showWindow(String caption, Window addEventWindow) {
		addEventWindow.setCaption(caption);
		addEventWindow.setModal(true);
		addEventWindow.setWidth(400, Sizeable.UNITS_PIXELS);
		addEventWindow.center();
		parent.addWindow(addEventWindow);
	}

	private void insertEvent(Table eventTable, TemplateEvent event) {
		Object[] fields = event.getFieldValues();
		Object[] item = Arrays.copyOf(fields, fields.length + 1);
		item[item.length - 1] = new Button("Delete", new DeleteEventListener(eventTable, event));
		eventTable.addItem(item, event);
	}

	private void insertEvents(Table eventTable, Collection<? extends TemplateEvent> events) {
		for (TemplateEvent event : events) {
			insertEvent(eventTable, event);
		}
	}

	private class EventTable extends Table {
		@Override
		protected String formatPropertyValue(Object rowId, Object colId, Property property) {
			if (property.getValue() != null && colId.equals(ServiceFields.Request)) {
				boolean isPutInService = ((Boolean) property.getValue()).booleanValue();
				if (isPutInService) {
					return "Put elevator in service";
				} else {
					return "Take elevator out of service";
				}
			}
			return super.formatPropertyValue(rowId, colId, property);
		}
	}

	private void updateAvailableFloors(int newNumFloors) {
		int start = Math.min(newNumFloors, previousNumFloors);
		int end = Math.max(newNumFloors, previousNumFloors);
		for (int floor = start; floor < end; floor++) {
			if (newNumFloors > previousNumFloors) {
				availableFloors.addItem(floor);
			} else {
				availableFloors.removeItem(floor);
			}
		}
		previousNumFloors = newNumFloors;
	}

	private List<TemplatePassengerRequest> getRequestEvents() {
		LinkedList<TemplatePassengerRequest> requests = new LinkedList<TemplatePassengerRequest>();
		for (Object itemId : requestTable.getItemIds()) {
			Item item = requestTable.getItem(itemId);
			TemplatePassengerRequest request = new TemplatePassengerRequest();
			request.setQuantum((Long) item.getItemProperty(PassengerFields.Quantum).getValue());
			request.setOnloadFloor((Integer) item.getItemProperty(PassengerFields.OnloadFloor)
					.getValue());
			request.setOffloadFloor((Integer) item.getItemProperty(PassengerFields.OffloadFloor)
					.getValue());
			request.setTimeConstraint((Long) item.getItemProperty(PassengerFields.TimeConstraint)
					.getValue());
			requests.add(request);
		}
		return requests;
	}

	private List<TemplateServiceEvent> getServiceEvents() {
		LinkedList<TemplateServiceEvent> services = new LinkedList<TemplateServiceEvent>();
		for (Object itemId : serviceTable.getItemIds()) {
			Item item = serviceTable.getItem(itemId);
			TemplateServiceEvent service = new TemplateServiceEvent();
			service.setQuantum((Long) item.getItemProperty(ServiceFields.Quantum).getValue());
			service.setElevatorNumber((Integer) item.getItemProperty(ServiceFields.Elevator)
					.getValue());
			service.setPutInService((Boolean) item.getItemProperty(ServiceFields.Request)
					.getValue());
			services.add(service);
		}

		return services;
	}

	private List<TemplateFailureEvent> getFailureEvents() {
		LinkedList<TemplateFailureEvent> failures = new LinkedList<TemplateFailureEvent>();
		for (Object itemId : failureTable.getItemIds()) {
			Item item = failureTable.getItem(itemId);
			TemplateFailureEvent failure = new TemplateFailureEvent();
			failure.setQuantum((Long) item.getItemProperty(FailureFields.Quantum).getValue());
			failure.setElevatorNumber((Integer) item.getItemProperty(FailureFields.Elevator)
					.getValue());
			failure.setComponent((ComponentDetails) item.getItemProperty(FailureFields.Component)
					.getValue());
			failures.add(failure);
		}
		return failures;
	}

	@SuppressWarnings("unchecked")
	private Set<Integer> getRestrictedFloors() {
		return (Set<Integer>) restrictedFloors.getValue();
	}

	private Table createTable() {
		Table table = new EventTable();
		table.setWidth(100, Sizeable.UNITS_PERCENTAGE);
		table.setPageLength(5);
		return table;
	}

	private Layout createButtonPanel(Label tableTitle, Button addButton) {
		HorizontalLayout buttonPanel = new HorizontalLayout();
		buttonPanel.addComponent(tableTitle);
		buttonPanel.addComponent(createSpacer());
		buttonPanel.addComponent(createSpacer());
		buttonPanel.addComponent(createSpacer());
		buttonPanel.addComponent(addButton);
		return buttonPanel;
	}

	private Component createSpacer() {
		return new Label("&nbsp;", Label.CONTENT_XHTML);
	}

}
