package com.pod2.elevator.core;

public class Event {
	private long timeQuantum;
	private EventSource eventSource;
	
	public Event(EventSource eventSource, long timeQuantum) {
		this.eventSource = eventSource;
		this.timeQuantum = timeQuantum;
	}
}
