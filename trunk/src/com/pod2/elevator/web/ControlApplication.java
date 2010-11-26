package com.pod2.elevator.web;

import com.pod2.elevator.main.CentralController;
import com.pod2.elevator.web.views.ControlWindow;
import com.vaadin.Application;

/**
 * OVERVIEW: An Application which acts as a web-based control interface to the
 * simulator.
 * 
 */
public class ControlApplication extends Application {

	private final CentralController controller;

	public ControlApplication(CentralController controller) {
		super();
		this.controller = controller;
	}

	@Override
	public void init() {
		setMainWindow(new ControlWindow(controller));
	}

}
