package com.pod2.elevator.web.validators;

import com.vaadin.data.Validator;

/**
 * OVERVIEW: A Validator that ensures a value is an integer, and is greater than
 * or equal to zero.
 * 
 */
public class NonNegativeIntegerValidator implements Validator {

	private final String failureMessage;

	public NonNegativeIntegerValidator(String failureMessage) {
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
			return Integer.valueOf(value.toString()) >= 0;
		} catch (Exception e) {
			return false;
		}
	}

}
