package com.pod2.elevator.web.views.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pod2.elevator.core.events.EventType;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.data.SimulationTemplateRepository;
import com.pod2.elevator.data.TemplateEvent;
import com.pod2.elevator.data.TemplateFailureEvent;
import com.pod2.elevator.data.TemplatePassengerRequest;
import com.pod2.elevator.data.TemplateServiceEvent;
import com.pod2.elevator.web.views.EditWindow;
import com.pod2.elevator.web.views.common.LayoutUtils;
import com.pod2.elevator.web.views.common.SimulationTemplateBasicFormFieldFactory;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * An EditWindow which allows a user to create a new SimulationTemplate, and
 * save it to the database.
 * 
 */
public class CreateTemplateWindow extends EditWindow {

	public static final List<String> BASIC_FIELDS = Arrays.asList("name", "numberFloors",
			"numberElevators", "scheduler", "requestGenerationOn", "speed", "elevatorCapacity",
			"quantumsBeforeService", "distanceBeforeService");

	private final ManageTemplatesView manageView;
	private final Window parent;
	private final SimulationTemplate template;
	private final AddEventWindowFactory windowFactory;

	private VerticalLayout layout;

	private Form basicInfo;
	private Select restrictedFloors;
	private BeanItemContainer<PassengerEventAdapter> passengerEvents;
	private BeanItemContainer<ServiceEventAdapter> serviceEvents;
	private BeanItemContainer<FailureEventAdapter> failureEvents;

	private int previousNumElevators;
	private int previousNumFloors;

	/**
	 * Restricted floors multiselect binds to this container to determine
	 * presents which floors are selectable.
	 */
	private BeanItemContainer<Integer> availableFloors;

	public CreateTemplateWindow(ManageTemplatesView manageView, Window parent,
			SimulationTemplate template) {
		super();
		this.manageView = manageView;
		this.parent = parent;
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

	@SuppressWarnings("unchecked")
	Set<Integer> getRestrictedFloors() {
		return new HashSet<Integer>((Set<Integer>) restrictedFloors.getValue());
	}

	void insertEvent(TemplateEvent event) {
		if (event instanceof TemplatePassengerRequest) {
			TemplatePassengerRequest request = (TemplatePassengerRequest) event;
			final PassengerEventAdapter adapter = new PassengerEventAdapter(request);
			adapter.setDeleteButton(createDelete(passengerEvents, adapter));
			passengerEvents.addBean(adapter);
		} else if (event instanceof TemplateServiceEvent) {
			TemplateServiceEvent service = (TemplateServiceEvent) event;
			final ServiceEventAdapter adapter = new ServiceEventAdapter(service);
			adapter.setDeleteButton(createDelete(serviceEvents, adapter));
			serviceEvents.addBean(adapter);
		} else if (event instanceof TemplateFailureEvent) {
			TemplateFailureEvent failure = (TemplateFailureEvent) event;
			final FailureEventAdapter adapter = new FailureEventAdapter(failure);
			adapter.setDeleteButton(createDelete(failureEvents, adapter));
			failureEvents.addBean(adapter);
		}
	}

	@Override
	protected Component getEditControls() {
		layout = new VerticalLayout();
		initBasicInfo();
		layout.addComponent(LayoutUtils.createSpacer());
		initRestrictedFloors();
		layout.addComponent(LayoutUtils.createSpacer());
		initRequestEventsTable();
		layout.addComponent(LayoutUtils.createSpacer());
		initServiceEventsTable();
		layout.addComponent(LayoutUtils.createSpacer());
		initFailureEventsTable();
		layout.addComponent(LayoutUtils.createSpacer());
		return layout;
	}

	@Override
	protected void onSave() {
		try {
			basicInfo.commit();
			template.setLastEdit(new Date());
			template.setRestrictedFloors(getRestrictedFloors());
			template.setPassengerRequests(getPassengerEvents());
			template.setServiceEvents(getServiceEvents());
			template.setFailureEvents(getFailureEvents());
			try {
				SimulationTemplateRepository.createTemplate(template);
				manageView.templateCreated(template);
				close();
			} catch (Exception e) {
				Notification databaseError = new Notification("Unable to create template.<br>",
						e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
				parent.showNotification(databaseError);
			}
		} catch (InvalidValueException e) {
			/* Allow user to correct their errors. */
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
		restrictedFloors.setWidth(LayoutUtils.getFieldWidth());
		restrictedFloors.setMultiSelect(true);
		restrictedFloors.setNullSelectionAllowed(true);
		restrictedFloors.setReadThrough(true);
		restrictedFloors.setWriteThrough(true);
		restrictedFloors.setImmediate(true);
		restrictedFloors.setContainerDataSource(availableFloors);
		for (Integer floor : template.getRestrictedFloors()) {
			restrictedFloors.select(floor);
		}
		restrictedFloors.addListener(new RestrictedFloorsChangeListener());
		layout.addComponent(restrictedFloors);
	}

	private void initRequestEventsTable() {
		passengerEvents = new BeanItemContainer<PassengerEventAdapter>(PassengerEventAdapter.class);
		insertEvents(template.getPassengerRequests());

		Table requestTable = createTable();
		requestTable.setContainerDataSource(passengerEvents);
		requestTable.setVisibleColumns(PassengerEventAdapter.getVisibleColumns());
		requestTable.setColumnHeaders(PassengerEventAdapter.getColumnHeaders());

		layout.addComponent(createButtonPanel(new Label("Passenger Request Events"), new Button(
				"Add", new AddEventListener(EventType.PassengerRequest,
						"Add Passenger Request Event"))));
		layout.addComponent(LayoutUtils.createSpacer());
		layout.addComponent(requestTable);
	}

	private void initServiceEventsTable() {
		serviceEvents = new BeanItemContainer<ServiceEventAdapter>(ServiceEventAdapter.class);
		insertEvents(template.getServiceEvents());

		Table serviceTable = createTable();
		serviceTable.setContainerDataSource(serviceEvents);
		serviceTable.setVisibleColumns(ServiceEventAdapter.getVisibleColumns());
		serviceTable.setColumnHeaders(ServiceEventAdapter.getColumnHeaders());

		layout.addComponent(createButtonPanel(new Label("Forced Servicing Events"), new Button(
				"Add", new AddEventListener(EventType.ServiceRequest, "Add Forced Service Event"))));
		layout.addComponent(LayoutUtils.createSpacer());
		layout.addComponent(serviceTable);
	}

	private void initFailureEventsTable() {
		failureEvents = new BeanItemContainer<FailureEventAdapter>(FailureEventAdapter.class);
		insertEvents(template.getFailureEvents());

		Table failureTable = createTable();
		failureTable.setContainerDataSource(failureEvents);
		failureTable.setVisibleColumns(FailureEventAdapter.getVisibleColumns());
		failureTable.setColumnHeaders(FailureEventAdapter.getColumnHeaders());

		layout.addComponent(createButtonPanel(new Label("Forced Failure Event"), new Button("Add",
				new AddEventListener(EventType.ComponentFailure, "Add Forced Failure Event"))));

		layout.addComponent(LayoutUtils.createSpacer());
		layout.addComponent(failureTable);
	}

	private List<TemplatePassengerRequest> getPassengerEvents() {
		return new ArrayList<TemplatePassengerRequest>(passengerEvents.getItemIds());
	}

	private List<TemplateServiceEvent> getServiceEvents() {
		return new ArrayList<TemplateServiceEvent>(serviceEvents.getItemIds());
	}

	private List<TemplateFailureEvent> getFailureEvents() {
		return new ArrayList<TemplateFailureEvent>(failureEvents.getItemIds());
	}

	private void showWindow(String caption, Window addEventWindow) {
		addEventWindow.setCaption(caption);
		addEventWindow.setModal(true);
		addEventWindow.setWidth(400, Sizeable.UNITS_PIXELS);
		addEventWindow.center();
		parent.addWindow(addEventWindow);
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

	private Table createTable() {
		Table table = new EventTable();
		table.setWidth(100, Sizeable.UNITS_PERCENTAGE);
		table.setPageLength(5);
		table.setImmediate(true);
		table.setReadThrough(true);
		return table;
	}

	private Layout createButtonPanel(Label tableTitle, Button addButton) {
		HorizontalLayout buttonPanel = new HorizontalLayout();
		buttonPanel.addComponent(tableTitle);
		buttonPanel.addComponent(LayoutUtils.createSpacer());
		buttonPanel.addComponent(LayoutUtils.createSpacer());
		buttonPanel.addComponent(LayoutUtils.createSpacer());
		buttonPanel.addComponent(addButton);
		return buttonPanel;
	}

	private void insertEvents(Collection<? extends TemplateEvent> events) {
		for (TemplateEvent event : events) {
			insertEvent(event);
		}
	}

	private Button createDelete(final BeanItemContainer<? extends Object> container,
			final Object item) {
		return new Button("Delete", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				container.removeItem(item);
			}
		});
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

	private class BasicInfoFieldFactory extends SimulationTemplateBasicFormFieldFactory {

		private static final int MIN_ELEVATORS = 1;
		private static final int MAX_ELEVATORS = 10;

		private static final int MIN_FLOORS = 2;
		private static final int MAX_FLOORS = 50;

		@Override
		public Field createField(Item item, Object propertyId, Component uiContext) {
			String pid = (String) propertyId;
			if (pid.equals("numberFloors")) {
				Select numberFloors = createIntegerSelect("Number of Floors:",
						"Number floors must be a positive integer.", MIN_FLOORS, MAX_FLOORS);
				numberFloors.setNullSelectionAllowed(false);
				numberFloors.addListener(new NumberFloorsValueChanged());
				return numberFloors;
			} else if (pid.equals("numberElevators")) {
				Select numberElevators = createIntegerSelect("Number of Elevators:",
						"Number elevators must be a positive integer.", MIN_ELEVATORS,
						MAX_ELEVATORS);
				numberElevators.setNullSelectionAllowed(false);
				numberElevators.addListener(new NumberElevatorsValueChanged());
				return numberElevators;
			}
			return super.createField(item, propertyId, uiContext);
		}

	}

	public class EventTable extends Table {

		@Override
		protected String formatPropertyValue(Object rowId, Object colId, Property property) {
			if (property.getValue() != null && colId.equals("putInService")) {
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

	private class NumberElevatorsValueChanged implements ValueChangeListener {
		@Override
		public void valueChange(ValueChangeEvent event) {
			if (serviceEvents == null || failureEvents == null) {
				return;
			}
			Integer newNumElevators = (Integer) event.getProperty().getValue();
			if (newNumElevators == null) {
				newNumElevators = Integer.valueOf(0);
			}

			boolean cantUpdate = false;
			String cantUpdateMessage = "";
			for (TemplateServiceEvent serviceEvent : getServiceEvents()) {
				if (serviceEvent.getElevatorNumber() >= newNumElevators) {
					cantUpdate = true;
					cantUpdateMessage = "There are service requests set to act on the removed elevators.<br>"
							+ " Please delete these requests before lowering the "
							+ "number of elevators.";
				}
			}
			for (TemplateFailureEvent failureEvent : getFailureEvents()) {
				if (failureEvent.getElevatorNumber() >= newNumElevators) {
					cantUpdate = true;
					cantUpdateMessage = "There are failure requests set to act on the removed elevators.<br>"
							+ " Please delete these requests before lowering the "
							+ "number of elevators.";
				}
			}
			if (cantUpdate) {
				Notification updateNotification = new Notification("Elevator in use.<br>",
						cantUpdateMessage, Notification.TYPE_WARNING_MESSAGE);
				updateNotification.setDelayMsec(-1);
				parent.showNotification(updateNotification);
				event.getProperty().setValue(previousNumElevators);
				return;
			}
			previousNumElevators = newNumElevators;
		}
	}

	private class NumberFloorsValueChanged implements ValueChangeListener {
		@Override
		public void valueChange(ValueChangeEvent event) {
			if (passengerEvents == null) {
				return;
			}
			Integer newNumFloors = (Integer) event.getProperty().getValue();
			if (newNumFloors == null) {
				newNumFloors = Integer.valueOf(0);
			}
			for (TemplatePassengerRequest request : getPassengerEvents()) {
				boolean isOnloadHigher = request.getOnloadFloor() >= newNumFloors;
				boolean isOffloadHigher = request.getOffloadFloor() >= newNumFloors;
				if (isOnloadHigher || isOffloadHigher) {
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
			}
			updateAvailableFloors(newNumFloors);
		}
	}

	private class RestrictedFloorsChangeListener implements ValueChangeListener {

		@Override
		public void valueChange(ValueChangeEvent event) {
			boolean inUse = false;
			@SuppressWarnings("unchecked")
			Set<Integer> restrictedSet = (Set<Integer>) event.getProperty().getValue();

			for (TemplatePassengerRequest request : getPassengerEvents()) {
				if (restrictedSet.contains(request.getOffloadFloor())) {
					inUse = true;
					restrictedFloors.unselect(request.getOffloadFloor());
				}
			}
			if (inUse) {
				Notification updateNotification = new Notification("Floor in use.<br>",
						"There are passenger requests set to offload "
								+ "on the restricted floor(s).<br> Please delete these "
								+ "requests before restricting floors.",
						Notification.TYPE_WARNING_MESSAGE);
				updateNotification.setDelayMsec(-1);
				parent.showNotification(updateNotification);
			}

		}
	}

}
