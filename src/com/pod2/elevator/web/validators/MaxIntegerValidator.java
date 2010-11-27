package com.pod2.elevator.web.validators;

import com.vaadin.data.Validator;

public class MaxIntegerValidator implements Validator {

	private final int maxValue;
	private final String invalidMessage;

	public MaxIntegerValidator(int maxValue, String invalidMessage) {
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
		return Integer.parseInt(value.toString()) <= maxValue;
	}

}
