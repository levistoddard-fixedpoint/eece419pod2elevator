package com.pod2.elevator.web.views.simulation;

import com.pod2.elevator.main.CentralController;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class RunSimulationView extends CustomComponent {

	private final CentralController controller;
	
	public RunSimulationView(CentralController controller) {
		super();
		this.controller = controller;
		initLayout();
	}

	public void initLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(new Label("run simulation"));
		setCompositionRoot(layout);
	}

}
