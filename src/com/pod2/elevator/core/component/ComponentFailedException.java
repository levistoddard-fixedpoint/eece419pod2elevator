package com.pod2.elevator.core.component;

/**
 * Thrown by ElevatorComponent sub-class operations when the component has
 * failed, or the operation will cause the component to operate outside it's
 * physical boundaries.
 * 
 */
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
