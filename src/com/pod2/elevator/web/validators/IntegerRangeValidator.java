package com.pod2.elevator.web.validators;

import com.vaadin.data.Validator;

/**
 * OVERVIEW: Validates that an integer value is within a specified range.
 * 
 */
public class IntegerRangeValidator implements Validator {

	private final String field;
	private final int min;
	private final int max;

	public IntegerRangeValidator(String field, int min, int max) {
		this.field = field;
		this.min = min;
		this.max = max;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (!isValid(value)) {
			throw new InvalidValueException(field + " must be an integer value between " + min
					+ " and " + max + " inclusive.");
		}
	}

	@Override
	public boolean isValid(Object value) {
		try {
			int intValue = Integer.parseInt(value.toString());
			return intValue >= min && intValue <= max;
		} catch (Exception e) {
			return false;
		}
	}

}
