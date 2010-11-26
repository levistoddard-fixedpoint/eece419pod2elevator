package com.pod2.elevator.data;

import java.util.Date;

public class SimulationTemplateDetail {

	private int id;
	private String name;
	private Date created;
	private Date lastEdit;

	public SimulationTemplateDetail() {
	}

	public SimulationTemplateDetail(int id, String name, Date created, Date lastEdit) {
		super();
		this.id = id;
		this.name = name;
		this.created = created;
		this.lastEdit = lastEdit;
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

	public Date getLastEdit() {
		return lastEdit;
	}

	public void setLastEdit(Date lastEdit) {
		this.lastEdit = lastEdit;
	}

	@Override
	public String toString() {
		return name;
	}

}
