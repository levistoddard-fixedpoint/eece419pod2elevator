package com.pod2.elevator.data;

import java.util.Date;

public class SimulationTemplateDetail {

	private int id = -1;
	private String name = "";
	private Date created = new Date();

	public SimulationTemplateDetail() {
	}

	public SimulationTemplateDetail(int id, String name, Date created) {
		super();
		this.id = id;
		this.name = name;
		this.created = created;
	}

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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return name;
	}

}
