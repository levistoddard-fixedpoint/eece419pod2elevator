package com.pod2.elevator.web.views.common;

import com.pod2.elevator.core.component.ComponentDetails;
import com.pod2.elevator.core.component.ComponentRegistry;
import com.pod2.elevator.core.events.EventType;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.data.TemplateEvent;
import com.pod2.elevator.data.TemplateFailureEvent;
import com.pod2.elevator.data.TemplatePassengerRequest;
import com.pod2.elevator.data.TemplateServiceEvent;
import com.pod2.elevator.web.validators.MaxIntegerValidator;
import com.pod2.elevator.web.validators.NonNegativeIntegerValidator;
import com.pod2.elevator.web.views.templates.FailureEventAdapter;
import com.pod2.elevator.web.views.templates.PassengerEventAdapter;
import com.pod2.elevator.web.views.templates.ServiceEventAdapter;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

/**
 * OVERVIEW: A factory class which creates Windows that the user can use to
 * enter new events (i.e. subclasses of TemplateEvent), for insertion into a
 * simulation.
 * 
 */
public class AddEventWindowFactory {

	private static final int MAX_QUANTUM = 1000000000;

	private final EventConsumer consumer;

	public AddEventWindowFactory(EventConsumer consumer) {
		assert (consumer != null);
		this.consumer = consumer;
	}

	/**
	 * OVERVIEW: Creates and returns a new Window which allows a user to input
	 * an event of the specified type.
	 * 
	 */
	public Window createWindow(EventType type, SimulationTemplate template) {
		if (EventType.PassengerRequest.equals(type)) {
			return createRequestEventWindow(template);
		} else if (EventType.ServiceRequest.equals(type)) {
			return createServiceEventWindow(template);
		} else if (EventType.ComponentFailure.equals(type)) {
			return createFailureEventWindow(template);
		}
		throw new RuntimeException("unexpected event type.");
	}

	/**
	 * OVERVIEW: A FormFieldFactory which creates Fields for the common
	 * properties between every TemplateEvent subclass.
	 * 
	 */
	private class EventFormFieldFactory implements FormFieldFactory {

		protected final int numberElevators;

		public EventFormFieldFactory(int numberElevators) {
			this.numberElevators = numberElevators;
		}

		@Override
		public Field createField(Item item, Object propertyId, Component uiContext) {
			if (propertyId.equals("quantum")) {
				TextField time = new TextField("Event Time:");
				time.setRequired(true);
				time.setRequiredError("Please enter time at which event should occur.");
				time.addValidator(new NonNegativeIntegerValidator(
						"Time must be a non-negative integer."));
				time.addValidator(new MaxIntegerValidator(MAX_QUANTUM,
						"Time must be less than or equal to " + MAX_QUANTUM + " quantums."));
				return time;
			} else if (propertyId.equals("elevatorNumber")) {
				Select elevators = new Select("Elevator Number:");
				elevators.setNullSelectionAllowed(false);
				for (int elevator = 0; elevator < numberElevators; elevator++) {
					elevators.addItem(elevator);
				}
				return elevators;
			}
			throw new RuntimeException("unexpected property: " + propertyId);
		}

	}

	/**
	 * OVERVIEW: An EventFormFieldFactory which create Fields for the properties
	 * of a TemplatePassengerRequest.
	 * 
	 */
	private class PassengerRequestFieldFactory extends EventFormFieldFactory {

		private final SimulationTemplate template;
		private final TemplatePassengerRequest request;

		public PassengerRequestFieldFactory(SimulationTemplate template,
				TemplatePassengerRequest request) {
			super(template.getNumberElevators());
			this.template = template;
			this.request = request;
		}

		@Override
		public Field createField(Item item, Object propertyId, Component uiContext) {
			if (propertyId.equals("onloadFloor")) {
				return createFloorSelectField("Onload Floor:", true);
			} else if (propertyId.equals("offloadFloor")) {
				return createFloorSelectField("Offload Floor:", false);
			} else if (propertyId.equals("timeConstraint")) {
				TextField timeConstraint = new TextField("Time Constraint:");
				timeConstraint.setWidth(LayoutUtils.getFieldWidth());
				timeConstraint.setRequired(true);
				timeConstraint.setRequiredError("Please enter a time constraint.");
				timeConstraint.addValidator(new NonNegativeIntegerValidator(
						"Time constraint must be a non-negative integer."));
				timeConstraint.addValidator(new MaxIntegerValidator(MAX_QUANTUM,
						"Time must be less than or equal to " + MAX_QUANTUM + "quantums."));
				return timeConstraint;
			}
			return super.createField(item, propertyId, uiContext);
		}

		/**
		 * OVERVIEW: A Validator which ensures that the onload and offload
		 * floors of a TemplatePassengerRequest are not equal.
		 * 
		 */
		private class SelectedFloorsValidator implements Validator {

			@Override
			public void validate(Object value) throws InvalidValueException {
				if (areOnloadAndOffloadSame()) {
					throw new InvalidValueException("Onload and offload floors must be different.");
				}
			}

			@Override
			public boolean isValid(Object value) {
				return !areOnloadAndOffloadSame();
			}

			private boolean areOnloadAndOffloadSame() {
				return request.getOnloadFloor() == request.getOffloadFloor();
			}

		}

		/**
		 * EFFECTS: Returns a Select field with an option for every floor in
		 * this template. Restricted floors are included in the options if
		 * includeRestructed is true, they are excluded otherwise.
		 * 
		 */
		private Field createFloorSelectField(String caption, boolean includeRestricted) {
			Select floors = new Select(caption);
			floors.setWidth(LayoutUtils.getFieldWidth());
			floors.setRequired(true);
			floors.setRequiredError("Please select a floor.");
			floors.setNullSelectionAllowed(false);
			floors.setNullSelectionItemId(-1);
			floors.setInvalidCommitted(true);
			floors.addValidator(new SelectedFloorsValidator());
			floors.addValidator(new NonNegativeIntegerValidator(
					"Floor must be a non-negative integer."));
			for (int floor = 0; floor < template.getNumberFloors(); floor++) {
				if (!includeRestricted && template.getRestrictedFloors().contains(floor)) {
					continue;
				}
				floors.addItem(floor);
			}
			return floors;
		}

	}

	/**
	 * OVERVIEW: Returns a new Window in which the user can enter the parameters
	 * for a TemplatePassengerRequest event.
	 * 
	 */
	private Window createRequestEventWindow(SimulationTemplate template) {
		final long DEFAULT_QUANTUM = 100;
		final int DEFAULT_ONLOAD_FLOOR = 0;
		final int DEFAULT_OFFLOAD_FLOOR = 1;
		final long DEFAULT_CONSTRAINT = 1000;

		TemplatePassengerRequest request = new TemplatePassengerRequest();
		request.setQuantum(DEFAULT_QUANTUM);
		/*
		 * Find a floor which is not off limits. Set the selected floor to the
		 * null floor if this is not possible.
		 */
		request.setOnloadFloor(DEFAULT_ONLOAD_FLOOR);
		int floor = DEFAULT_OFFLOAD_FLOOR;
		for (; floor < template.getNumberFloors(); floor++) {
			if (!template.getRestrictedFloors().contains(floor)) {
				break;
			}
		}
		if (floor == template.getNumberFloors()) {
			floor = -1;
		}
		request.setOffloadFloor(floor);
		request.setTimeConstraint(DEFAULT_CONSTRAINT);
		FormFieldFactory fieldFactory = new PassengerRequestFieldFactory(template, request);
		return new AddEventWindow<TemplatePassengerRequest>(consumer, fieldFactory,
				PassengerEventAdapter.getEditableFields(), request);
	}

	/**
	 * OVERVIEW: An EventFormFieldFactory which create Fields for the properties
	 * of a TemplateServiceEvent.
	 * 
	 */
	private class ServiceEventFieldFactory extends EventFormFieldFactory {

		public ServiceEventFieldFactory(int numberFloors) {
			super(numberFloors);
		}

		@Override
		public Field createField(Item item, Object propertyId, Component uiContext) {
			if (propertyId.equals("putInService")) {
				return new CheckBox("Should put elevator in service.");
			}
			return super.createField(item, propertyId, uiContext);
		}

	}

	/**
	 * OVERVIEW: Returns a new Window in which the user can enter the parameters
	 * for a TemplateServiceEvent event.
	 * 
	 */
	private Window createServiceEventWindow(SimulationTemplate template) {
		final long DEFAULT_QUANTUM = 100;
		final int DEFAULT_ELEVATOR = 0;
		final boolean DEFAULT_PUT_IN_SERVICE = false;

		TemplateServiceEvent service = new TemplateServiceEvent();
		service.setQuantum(DEFAULT_QUANTUM);
		service.setElevatorNumber(DEFAULT_ELEVATOR);
		service.setPutInService(DEFAULT_PUT_IN_SERVICE);
		FormFieldFactory fieldFactory = new ServiceEventFieldFactory(template.getNumberElevators());
		return new AddEventWindow<TemplateEvent>(consumer, fieldFactory,
				ServiceEventAdapter.getEditableFields(), service);
	}

	/**
	 * OVERVIEW: An EventFormFieldFactory which create Fields for the properties
	 * of a TemplateFailureEvent.
	 * 
	 */
	private class FailureRequestFieldFactory extends EventFormFieldFactory {

		public FailureRequestFieldFactory(int numberFloors) {
			super(numberFloors);
		}

		@Override
		public Field createField(Item item, Object propertyId, Component uiContext) {
			if (propertyId.equals("component")) {
				Select components = new Select("Elevator Component:");
				for (ComponentDetails component : ComponentRegistry.getFailableComponents()) {
					components.addItem(component);
				}
				components.setRequired(true);
				components.setRequiredError("Please select the component which should fail.");
				components.setNullSelectionAllowed(false);
				return components;
			}
			return super.createField(item, propertyId, uiContext);
		}

	}

	/**
	 * OVERVIEW: Returns a new Window in which the user can enter the parameters
	 * for a TemplateFailureEvent event.
	 * 
	 */
	private Window createFailureEventWindow(SimulationTemplate template) {
		final long DEFAULT_QUANTUM = 100;
		final int DEFAULT_ELEVATOR = 0;
		final ComponentDetails DEFAULT_COMPONENT = ComponentRegistry.getFailableComponents()
				.iterator().next();

		TemplateFailureEvent failure = new TemplateFailureEvent();
		failure.setQuantum(DEFAULT_QUANTUM);
		failure.setElevatorNumber(DEFAULT_ELEVATOR);
		failure.setComponent(DEFAULT_COMPONENT);
		FormFieldFactory fieldFactory = new FailureRequestFieldFactory(
				template.getNumberElevators());
		return new AddEventWindow<TemplateFailureEvent>(consumer, fieldFactory,
				FailureEventAdapter.getEditableFields(), failure);
	}

}
