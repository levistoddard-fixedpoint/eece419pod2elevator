package com.pod2.elevator.web.validator;

import com.vaadin.data.Validator;

public class PositiveIntegerValidator implements Validator {

	private final String failureMessage;

	public PositiveIntegerValidator(String failureMessage) {
		this.failureMessage = failureMessage;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (!isValid(value)) {
			throw new InvalidValueException(failureMessage);
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
