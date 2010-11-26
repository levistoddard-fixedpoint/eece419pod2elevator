package com.pod2.elevator.web;

/**
 * OVERVIEW: Thrown by ControlServer instances when the web service cannot be
 * started or stopped for any reason.
 * 
 */
public class ControlServerException extends Exception {

	public ControlServerException() {
	}

	public ControlServerException(String arg0) {
		super(arg0);
	}

	public ControlServerException(Throwable arg0) {
		super(arg0);
	}

	public ControlServerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
