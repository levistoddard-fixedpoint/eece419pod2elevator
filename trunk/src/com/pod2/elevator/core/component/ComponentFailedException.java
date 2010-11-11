package com.pod2.elevator.core.component;

public class ComponentFailedException extends Exception {

	public ComponentFailedException() {
	}

	public ComponentFailedException(String message) {
		super(message);
	}

	public ComponentFailedException(Throwable cause) {
		super(cause);
	}

	public ComponentFailedException(String message, Throwable cause) {
		super(message, cause);
	}

}
