package com.pod2.elevator.data;

public class SimulationDetail {
	private int uuid;
	private String name;
	
	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SimulationDetail(int uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}
}
