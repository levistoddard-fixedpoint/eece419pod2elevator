package com.pod2.elevator.data;

public class InvalidDatabaseConfigurationException extends Exception {

	public InvalidDatabaseConfigurationException() {
	}

	public InvalidDatabaseConfigurationException(String message) {
		super(message);
	}

	public InvalidDatabaseConfigurationException(Throwable cause) {
		super(cause);
	}

	public InvalidDatabaseConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
