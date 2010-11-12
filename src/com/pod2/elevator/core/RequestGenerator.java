package com.pod2.elevator.core;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import com.pod2.elevator.core.events.Event;
import com.pod2.elevator.core.events.EventFactory;
import com.pod2.elevator.core.events.EventSource;

/**
 * Generates random PassengerRequests for insertion into an ActiveSimulation.
 * 
 */
public class RequestGenerator {

	private static double GENERATE_PROBABILITY = 0.01;

	private final ActiveSimulation simulation;
	private final Random random;

	RequestGenerator(ActiveSimulation simulation) {
		this.simulation = simulation;
		this.random = new Random();
	}

	public Collection<Event> nextRequests() {
		if (random.nextDouble() < GENERATE_PROBABILITY) {
			int max = simulation.getNumberFloors();
			int onload = random.nextInt(max);
			int offload = random.nextInt(max);
			while (onload == offload) {
				offload = random.nextInt(max);
			}
			long constraint = random.nextLong();
			Event request = EventFactory.createPassengerRequest(
					EventSource.Generated, simulation.getCurrentQuantum(),
					onload, offload, constraint);
			return Collections.singletonList(request);
		}
		return Collections.emptySet();
	}

}
