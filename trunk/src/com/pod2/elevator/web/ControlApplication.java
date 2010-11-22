package com.pod2.elevator.web;

import com.pod2.elevator.main.CentralController;
import com.pod2.elevator.web.views.ControlWindow;
import com.vaadin.Application;
import com.vaadin.ui.Window;

public class ControlApplication extends Application {

	private final CentralController controller;

	public ControlApplication(CentralController controller) {
		super();
		this.controller = controller;
	}

	@Override
	public void init() {
		Window main = new ControlWindow(controller);
		setMainWindow(main);
	}

}
