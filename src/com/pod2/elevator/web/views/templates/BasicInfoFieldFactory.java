package com.pod2.elevator.web.views.templates;

import com.pod2.elevator.scheduling.ElevatorScheduler;
import com.pod2.elevator.scheduling.SchedulerRegistry;
import com.pod2.elevator.web.views.PositiveIntegerValidator;
import com.vaadin.data.Item;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;

public class BasicInfoFieldFactory implements FormFieldFactory {

	private static final int MAX_ELEVATORS = 10;
	private static final int MAX_FLOORS = 50;

	@Override
	public Field createField(Item item, Object propertyId, Component uiContext) {
		String pid = (String) propertyId;
		if (pid.equals("name")) {
			// Setting Template Name field:
			TextField name = new TextField("Name:");
			name.setRequired(true);
			name.setRequiredError("Please enter a valid template name.");
			name.setWidth("15em");
			name.addValidator(new StringLengthValidator(
					"Template name must be shorter than 20 characters", 1, 20,
					false));
			return name;
		} else if (pid.equals("numberFloors")) {
			return createIntegerSelect("Number of Floors:", MAX_FLOORS);
		} else if (pid.equals("numberElevators")) {
			return createIntegerSelect("Number of Elevators:", MAX_ELEVATORS);
		} else if (pid.equals("scheduler")) {
			// Setting Scheduling Algorithm field:
			Select schedulers = new Select("Scheduling Algorithm:");
			for (ElevatorScheduler scheduler : SchedulerRegistry
					.getAvailableSchedulers()) {
				schedulers.addItem(scheduler);
			}
			schedulers.setRequired(true);
			schedulers.setRequiredError("Please select a scheduler.");
			schedulers.setWidth("20em");
			return schedulers;
		} else if (pid.equals("requestGenerationOn")) {
			return new CheckBox("Random request generation enabled");
		}
		// If input does not match any of the specified fields:
		return null;
	}

	private Field createIntegerSelect(String label, int maxNumber) {
		// Setting Number of Elevators field:
		Select selectInput = new Select(label);
		for (int n = 1; n <= maxNumber; n++) {
			selectInput.addItem(n);
		}
		selectInput.setRequired(true);
		selectInput.setWidth("15em");
		selectInput.addValidator(new PositiveIntegerValidator());
		return selectInput;
	}

}
