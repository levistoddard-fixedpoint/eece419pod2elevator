package com.pod2.elevator.core.events;

/**
 * Classifies an event by type of event that is requested
 */
public enum EventType {

	/**
	 * 	Event is for passenger requests
	 */
	PassengerRequest,
	
	/**
	 *  Event is for changing service status
	 */
	ServiceRequest,
	
	/**
	 * 	Event is for creating component failures
	 */
	ComponentFailure;

}
