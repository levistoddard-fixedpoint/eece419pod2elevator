package com.pod2.elevator.data;

public abstract class TemplateEvent {

	private long quantum;

	public long getQuantum() {
		return quantum;
	}

	public void setQuantum(long quantum) {
		this.quantum = quantum;
	}

	public abstract Object[] getFieldValues();
	
	public abstract Object[] getFields();

}
