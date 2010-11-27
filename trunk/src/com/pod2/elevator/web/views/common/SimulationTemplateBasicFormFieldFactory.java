package com.pod2.elevator.web.views.common;

import com.pod2.elevator.scheduling.ElevatorScheduler;
import com.pod2.elevator.scheduling.SchedulerRegistry;
import com.pod2.elevator.web.validators.MaxDoubleValidator;
import com.pod2.elevator.web.validators.MaxIntegerValidator;
import com.pod2.elevator.web.validators.PositiveIntegerValidator;
import com.pod2.elevator.web.validators.PositiveNumberValidator;
import com.vaadin.data.Item;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;

/**
 * OVERVIEW: A FormFieldFactory which generates Field components for the name,
 * scheduler, request generation enabled, capacity, and quantums/distance before
 * service fields of a SimulationTemplate.
 * 
 */
public class SimulationTemplateBasicFormFieldFactory implements FormFieldFactory {

	@Override
	public Field createField(Item item, Object propertyId, Component uiContext) {
		String pid = (String) propertyId;
		if (pid.equals("name")) {
			final int MIN_LEN = 1;
			final int MAX_LEN = 20;

			TextField name = new TextField("Name:");
			name.setWidth(LayoutUtils.getFieldWidth());
			name.setRequired(true);
			name.setRequiredError("Please enter a name.");
			String message = "Name must be between " + MIN_LEN + " and " + MAX_LEN + " characters.";
			name.addValidator(new StringLengthValidator(message, MIN_LEN, MAX_LEN, false));
			return name;
		} else if (pid.equals("scheduler")) {
			Select schedulers = new Select("Scheduling Algorithm:");
			schedulers.setWidth(LayoutUtils.getFieldWidth());
			schedulers.setRequired(true);
			schedulers.setRequiredError("Please select a scheduling algorithm.");
			schedulers.setNullSelectionAllowed(false);
			for (ElevatorScheduler scheduler : SchedulerRegistry.getAvailableSchedulers()) {
				schedulers.addItem(scheduler);
			}
			return schedulers;
		} else if (pid.equals("requestGenerationOn")) {
			return new CheckBox("Random request generation enabled");
		} else if (pid.equals("speed")) {
			final double MAX_SPEED = 2.0;

			TextField speed = new TextField("Speed (floors / quantum):");
			speed.setWidth(LayoutUtils.getFieldWidth());
			speed.setRequired(true);
			speed.setRequiredError("Please enter an elevator speed.");
			speed.addValidator(new PositiveNumberValidator(
					"Elevator speed must be a positive number."));
			speed.addValidator(new MaxDoubleValidator(MAX_SPEED,
					"Speed must be less than or equal to " + MAX_SPEED + " floors/quantum"));
			return speed;
		} else if (pid.equals("elevatorCapacity")) {
			final int MIN_CAPACITY = 1;
			final int MAX_CAPACITY = 20;

			return createIntegerSelect("Elevator Passenger Capacity:",
					"Capacity must be a positive integer.", MIN_CAPACITY, MAX_CAPACITY);
		} else if (pid.equals("quantumsBeforeService")) {
			final int MAX_QUANTUMS_BEFORE_SERVICE = 5000;

			TextField quantums = new TextField("Time Before Service (quantums):");
			quantums.setWidth(LayoutUtils.getFieldWidth());
			quantums.setRequired(true);
			quantums.setRequiredError("Please enter quantums before required service.");
			quantums.addValidator(new PositiveIntegerValidator("Time must be a positive integer."));
			quantums.addValidator(new MaxIntegerValidator(5000,
					"Time must be less than or equal to " + MAX_QUANTUMS_BEFORE_SERVICE
							+ " quantums."));
			return quantums;
		} else if (pid.equals("distanceBeforeService")) {
			final double MAX_DISTANCE_BEFORE_SERVICE = 5000.0;

			TextField distance = new TextField("Distance Before Service (floors):");
			distance.setWidth(LayoutUtils.getFieldWidth());
			distance.setRequired(true);
			distance.setRequiredError("Please enter distance before required service.");
			distance.addValidator(new PositiveNumberValidator("Distance must be a positive number."));
			distance.addValidator(new MaxDoubleValidator(MAX_DISTANCE_BEFORE_SERVICE,
					"Distance must be less than or equal to " + MAX_DISTANCE_BEFORE_SERVICE
							+ " floors."));
			return distance;
		}
		throw new RuntimeException("unexpected property: " + pid);
	}

	/**
	 * EFFECTS: Returns a new Select field which allows a user to select an
	 * integer value between min and max.
	 * 
	 */
	protected Select createIntegerSelect(String label, String failureMessage, int min, int max) {
		Select selectInput = new Select(label);
		selectInput.setWidth(LayoutUtils.getFieldWidth());
		selectInput.setRequired(true);
		selectInput.setRequiredError(failureMessage);
		selectInput.setNullSelectionAllowed(false);
		selectInput.setWriteThrough(true);
		selectInput.setReadThrough(true);
		selectInput.setImmediate(true);
		selectInput.addValidator(new PositiveIntegerValidator(failureMessage));
		for (int n = min; n <= max; n++) {
			selectInput.addItem(n);
		}
		return selectInput;
	}

}
