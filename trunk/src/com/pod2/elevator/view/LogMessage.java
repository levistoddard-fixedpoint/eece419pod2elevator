package com.pod2.elevator.view;

public class LogMessage {
	public long quantum;
	public String type;
	public String description;

	public LogMessage(long q, String t, String d) {
		quantum = q;
		type = t;
		description = d;
	}

	public String toString() {
		return new String(quantum + ": " + type + " - " + description);
	}
}
