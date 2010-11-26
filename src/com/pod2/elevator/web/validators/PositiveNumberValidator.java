package com.pod2.elevator.web.validators;

import com.vaadin.data.Validator;

/**
 * OVERVIEW: A Validator that ensures a value can be parsed as numeric, and is
 * greater than zero.
 * 
 */
public class PositiveNumberValidator implements Validator {

	private final String failureMessage;

	public PositiveNumberValidator(String failureMessage) {
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
			return Double.valueOf(value.toString()) > 0.0;
		} catch (Exception e) {
			return false;
		}
	}

}
