package com.pod2.elevator.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.pod2.elevator.main.CentralController;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;

public class ControlServlet extends AbstractApplicationServlet {

	private final CentralController controller;

	public ControlServlet(CentralController controller) {
		this.controller = controller;
	}

	@Override
	protected Class<? extends Application> getApplicationClass() {
		return ControlApplication.class;
	}

	@Override
	protected Application getNewApplication(HttpServletRequest request)
			throws ServletException {
		return new ControlApplication(controller);
	}

}
