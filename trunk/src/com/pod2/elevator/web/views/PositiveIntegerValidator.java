package com.pod2.elevator.web.views;

import com.vaadin.data.Validator;

public class PositiveIntegerValidator implements Validator {

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (!isValid(value)) {
			throw new InvalidValueException("Must be a positive value.");
		}
	}

	@Override
	public boolean isValid(Object value) {
		try {
			return Integer.valueOf(value.toString()) > 0;
		} catch (Exception e) {
			return false;
		}
	}

}
