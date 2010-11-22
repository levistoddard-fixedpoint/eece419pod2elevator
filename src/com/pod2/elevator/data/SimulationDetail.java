package com.pod2.elevator.data;

public class SimulationDetail {
	private int id;
	private String name;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SimulationDetail(int id, String name) {
		this.id = id;
		this.name = name;
	}
}
