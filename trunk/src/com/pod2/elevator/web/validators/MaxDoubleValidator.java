package com.pod2.elevator.web.validators;

import com.vaadin.data.Validator;

public class MaxDoubleValidator implements Validator {

	private final double maxValue;
	private final String invalidMessage;

	public MaxDoubleValidator(double maxValue, String invalidMessage) {
		this.maxValue = maxValue;
		this.invalidMessage = invalidMessage;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (!isValid(value)) {
			throw new InvalidValueException(invalidMessage);
		}
	}

	@Override
	public boolean isValid(Object value) {
		return Double.parseDouble(value.toString()) <= maxValue;
	}

}
