package com.pod2.elevator.web.validators;

import com.vaadin.data.Validator;

/**
 * OVERVIEW: Validates that a double value is within a specified range.
 * 
 */
public class DoubleRangeValidator implements Validator {

	private final String field;
	private final double min;
	private final double max;

	public DoubleRangeValidator(String field, double min, double max) {
		this.field = field;
		this.min = min;
		this.max = max;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (!isValid(value)) {
			throw new InvalidValueException(field + " must be a numeric value between " + min
					+ " and " + max + " inclusive.");
		}
	}

	@Override
	public boolean isValid(Object value) {
		try {
			double doubleValue = Double.parseDouble(value.toString());
			return doubleValue >= min && doubleValue <= max;
		} catch (Exception e) {
			return false;
		}
	}

}
