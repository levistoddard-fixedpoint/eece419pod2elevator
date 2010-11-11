package com.pod2.elevator.core;

import java.util.Collection;
import java.util.Collections;

import com.pod2.elevator.core.events.PassengerRequest;

public class RequestGenerator {

	private boolean isEnabled;

	public RequestGenerator(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Collection<PassengerRequest> nextRequests(long currentQuantum) {
		if (isEnabled) {
			/* TODO Implement generation. */
		}
		return Collections.emptySet();
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

}
