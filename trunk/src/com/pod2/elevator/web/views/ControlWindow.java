package com.pod2.elevator.web.views;

import com.pod2.elevator.main.CentralController;
import com.pod2.elevator.web.views.simulation.ManageSimulationsView;
import com.pod2.elevator.web.views.templates.ManageTemplatesView;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ControlWindow extends Window implements SelectedTabChangeListener {

	private static final String RUN_TAB_TITLE = "Manage Simulations";
	private static final String MANAGE_TAB_TITLE = "Manage Templates";

	private final CentralController controller;

	private final TabSheet appTabs;
	private final Panel runSimulation;
	private final Panel manageTemplates;

	public ControlWindow(CentralController controller) {
		super();
		this.controller = controller;

		VerticalLayout content = new VerticalLayout();
		content.setSizeFull();

		appTabs = new TabSheet();
		appTabs.setWidth(800, Sizeable.UNITS_PIXELS);
		appTabs.addListener((SelectedTabChangeListener) this);
		
		runSimulation = new Panel();
		appTabs.addTab(runSimulation).setCaption(RUN_TAB_TITLE);
		
		manageTemplates = new Panel();
		appTabs.addTab(manageTemplates).setCaption(MANAGE_TAB_TITLE);
		
		content.addComponent(appTabs);
		content.setComponentAlignment(appTabs, Alignment.TOP_CENTER);
		setContent(content);
	}

	private Component getRunSimulationView() {
		return new ManageSimulationsView(this, controller);
	}

	private Component getManageTemplatesView() {
		return new ManageTemplatesView(this);
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		TabSheet tabs = event.getTabSheet();
		Component selectedTab = tabs.getSelectedTab();
		if (selectedTab == runSimulation) {
			runSimulation.removeAllComponents();
			runSimulation.addComponent(getRunSimulationView());
		} else if (selectedTab == manageTemplates) {
			manageTemplates.removeAllComponents();
			manageTemplates.addComponent(getManageTemplatesView());
		} else {
			throw new RuntimeException("unkown tab!");
		}
	}

}
