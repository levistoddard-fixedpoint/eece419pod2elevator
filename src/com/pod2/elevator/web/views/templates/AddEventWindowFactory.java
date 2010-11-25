package com.pod2.elevator.web.views.templates;

import com.pod2.elevator.core.component.ComponentDetails;
import com.pod2.elevator.core.component.ComponentRegistry;
import com.pod2.elevator.core.events.EventType;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.data.TemplateEvent;
import com.pod2.elevator.data.TemplateFailureEvent;
import com.pod2.elevator.data.TemplatePassengerRequest;
import com.pod2.elevator.data.TemplateServiceEvent;
import com.pod2.elevator.web.validator.NonNegativeIntegerValidator;
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
 * An factory class to create Windows which the user can use to create new
 * events (i.e. subclasses of TemplateEvent) for insertion into a simulation
 * template.
 * 
 */
public class AddEventWindowFactory {

	private static final String FIELD_WIDTH = "15em";

	private final CreateTemplateWindow templateWindow;

	public AddEventWindowFactory(CreateTemplateWindow templateWindow) {
		this.templateWindow = templateWindow;
	}

	public Window createWindow(EventType type, SimulationTemplate template) {
		if (EventType.PassengerRequest.equals(type)) {
			return createRequestEventWindow(type, template);
		} else if (EventType.ServiceRequest.equals(type)) {
			return createServiceEventWindow(type, template);
		} else if (EventType.ComponentFailure.equals(type)) {
			return createFailureEventWindow(type, template);
		}
		throw new RuntimeException("Unknown event type:" + type);
	}

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
						"Time must be a positive integer."));
				return time;
			} else if (propertyId.equals("elevatorNumber")) {
				Select elevators = new Select("Elevator Number:");
				elevators.setRequired(true);
				elevators.setRequiredError("Please select elevator that event should occur on.");
				elevators.addValidator(new NonNegativeIntegerValidator(
						"Elevator must be a positive integer."));
				for (int elevator = 0; elevator < numberElevators; elevator++) {
					elevators.addItem(elevator);
				}
				return elevators;
			}
			throw new RuntimeException("Unknown property: " + propertyId);
		}

	}

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
				return createFloorSelectField("Onload Floor:");
			} else if (propertyId.equals("offloadFloor")) {
				return createFloorSelectField("Offload Floor:");
			} else if (propertyId.equals("timeConstraint")) {
				TextField timeConstraint = new TextField("Time Constraint:");
				timeConstraint.setWidth(FIELD_WIDTH);
				timeConstraint.setRequired(true);
				timeConstraint.setRequiredError("Please enter a time constraint.");
				timeConstraint.addValidator(new NonNegativeIntegerValidator(
						"Time constraint must be a positive integer."));
				return timeConstraint;
			}
			return super.createField(item, propertyId, uiContext);
		}

		private class SelectedFloorsValidator implements Validator {

			@Override
			public void validate(Object value) throws InvalidValueException {
				if (areOnloadAndOffloadSame()) {
					throw new InvalidValueException("Onload and offload floors must be different.");
				}
				if (isFloorOffLimits(value)) {
					throw new InvalidValueException("Floor is off limits.");
				}
			}

			@Override
			public boolean isValid(Object value) {
				return !areOnloadAndOffloadSame() && !isFloorOffLimits(value);
			}

			private boolean areOnloadAndOffloadSame() {
				return request.getOnloadFloor() == request.getOffloadFloor();
			}

			private boolean isFloorOffLimits(Object value) {
				return template.getRestrictedFloors().contains(Integer.valueOf(value.toString()));
			}

		}

		private Field createFloorSelectField(String caption) {
			Select floors = new Select(caption);

			floors.setWidth(FIELD_WIDTH);
			floors.setRequired(true);
			floors.setRequiredError("Please select a floor.");
			floors.addValidator(new NonNegativeIntegerValidator("Floor must be a positive integer."));
			floors.addValidator(new SelectedFloorsValidator());
			floors.setInvalidCommitted(true);
			for (int floor = 0; floor < template.getNumberFloors(); floor++) {
				floors.addItem(floor);
			}
			return floors;
		}

	}

	private Window createRequestEventWindow(EventType type, SimulationTemplate template) {
		final long DEFAULT_QUANTUM = 100;
		final int DEFAULT_ONLOAD_FLOOR = 1;
		final int DEFAULT_OFFLOAD_FLOOR = 2;
		final long DEFAULT_CONSTRAINT = 1000;

		TemplatePassengerRequest request = new TemplatePassengerRequest();
		request.setQuantum(DEFAULT_QUANTUM);
		request.setOnloadFloor(DEFAULT_ONLOAD_FLOOR);
		request.setOffloadFloor(DEFAULT_OFFLOAD_FLOOR);
		request.setTimeConstraint(DEFAULT_CONSTRAINT);
		FormFieldFactory fieldFactory = new PassengerRequestFieldFactory(template, request);
		return new AddEventWindow<TemplatePassengerRequest>(templateWindow, fieldFactory,
				PassengerEventAdapter.getEditableFields(), request);
	}

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

	private Window createServiceEventWindow(EventType type, SimulationTemplate template) {
		final long DEFAULT_QUANTUM = 100;
		final int DEFAULT_ELEVATOR = 0;
		final boolean DEFAULT_PUT_IN_SERVICE = false;

		TemplateServiceEvent service = new TemplateServiceEvent();
		service.setQuantum(DEFAULT_QUANTUM);
		service.setElevatorNumber(DEFAULT_ELEVATOR);
		service.setPutInService(DEFAULT_PUT_IN_SERVICE);
		FormFieldFactory fieldFactory = new ServiceEventFieldFactory(template.getNumberElevators());
		return new AddEventWindow<TemplateEvent>(templateWindow, fieldFactory,
				ServiceEventAdapter.getEditableFields(), service);
	}

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
				return components;
			}
			return super.createField(item, propertyId, uiContext);
		}

	}

	private Window createFailureEventWindow(EventType type, SimulationTemplate template) {
		final long DEFAULT_QUANTUM = 100;
		final int DEFAULT_ELEVATOR = 1;
		final ComponentDetails DEFAULT_COMPONENT = ComponentRegistry.getFailableComponents()
				.iterator().next();

		TemplateFailureEvent failure = new TemplateFailureEvent();
		failure.setQuantum(DEFAULT_QUANTUM);
		failure.setElevatorNumber(DEFAULT_ELEVATOR);
		failure.setComponent(DEFAULT_COMPONENT);
		FormFieldFactory fieldFactory = new FailureRequestFieldFactory(
				template.getNumberElevators());
		return new AddEventWindow<TemplateFailureEvent>(templateWindow, fieldFactory,
				FailureEventAdapter.getEditableFields(), failure);
	}

}
