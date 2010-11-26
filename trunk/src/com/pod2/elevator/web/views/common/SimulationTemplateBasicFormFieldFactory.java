package com.pod2.elevator.web.views.common;

import com.pod2.elevator.scheduling.ElevatorScheduler;
import com.pod2.elevator.scheduling.SchedulerRegistry;
import com.pod2.elevator.web.validator.PositiveIntegerValidator;
import com.pod2.elevator.web.validator.PositiveNumberValidator;
import com.vaadin.data.Item;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;

public class SimulationTemplateBasicFormFieldFactory implements FormFieldFactory {

	private static final int MIN_CAPACITY = 1;
	private static final int MAX_CAPACITY = 20;

	@Override
	public Field createField(Item item, Object propertyId, Component uiContext) {
		String pid = (String) propertyId;
		if (pid.equals("name")) {
			final int MIN_LEN = 1;
			final int MAX_LEN = 20;

			TextField name = new TextField("Name:");
			name.setWidth(LayoutUtils.getFieldWidth());
			name.setRequired(true);
			name.setRequiredError("Please enter a template name.");
			name.addValidator(new StringLengthValidator("Name must be between " + MIN_LEN + " and "
					+ MAX_LEN + " characters.", MIN_LEN, MAX_LEN, false));
			return name;
		} else if (pid.equals("scheduler")) {
			Select schedulers = new Select("Scheduling Algorithm:");
			schedulers.setWidth(LayoutUtils.getFieldWidth());
			schedulers.setRequired(true);
			schedulers.setRequiredError("Please select a scheduler.");
			schedulers.setNullSelectionAllowed(false);
			for (ElevatorScheduler scheduler : SchedulerRegistry.getAvailableSchedulers()) {
				schedulers.addItem(scheduler);
			}
			return schedulers;
		} else if (pid.equals("requestGenerationOn")) {
			return new CheckBox("Random request generation enabled");
		} else if (pid.equals("speed")) {
			TextField speed = new TextField("Speed (floors / quantum):");
			speed.setWidth(LayoutUtils.getFieldWidth());
			speed.setRequired(true);
			speed.setRequiredError("Please enter an elevator speed.");
			speed.addValidator(new PositiveNumberValidator(
					"Elevator speed must be a positive number."));
			return speed;
		} else if (pid.equals("elevatorCapacity")) {
			return createIntegerSelect("Elevator Passenger Capacity:",
					"Capacity must be a positive integer", MIN_CAPACITY, MAX_CAPACITY);
		} else if (pid.equals("quantumsBeforeService")) {
			TextField quantums = new TextField("Time Before Service (quantums):");
			quantums.setWidth(LayoutUtils.getFieldWidth());
			quantums.setRequired(true);
			quantums.setRequiredError("Please enter quantums before required service.");
			quantums.addValidator(new PositiveIntegerValidator("Time must be a positive integer."));
			return quantums;
		} else if (pid.equals("distanceBeforeService")) {
			TextField distance = new TextField("Distance Before Service (floors):");
			distance.setWidth(LayoutUtils.getFieldWidth());
			distance.setRequired(true);
			distance.setRequiredError("Please enter distance before required service.");
			distance.addValidator(new PositiveNumberValidator("Distance must be a positive number."));
			return distance;
		}
		throw new RuntimeException("unexpected property: " + pid);
	}

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
