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

/**
 * OVERVIEW: A Window which acts as a container for the top-level views of the
 * ControlApplication (i.e. Hosts the "Manage Simulations" and
 * "Manage Templates" views and allows the user to navigate to them).
 * 
 */
public class ControlWindow extends Window implements SelectedTabChangeListener {

	public static final int APP_WIDTH = 800;

	private static final String RUN_TAB_TITLE = "Manage Simulations";
	private static final String MANAGE_TAB_TITLE = "Manage Templates";

	private final CentralController controller;
	private final TabSheet appTabs;
	private final Panel runSimulation;
	private final Panel manageTemplates;

	public ControlWindow(CentralController controller) {
		super();
		assert (controller != null);
		this.controller = controller;

		VerticalLayout content = new VerticalLayout();
		content.setSizeFull();
		setContent(content);

		appTabs = new TabSheet();
		appTabs.setWidth(APP_WIDTH, Sizeable.UNITS_PIXELS);
		appTabs.addListener((SelectedTabChangeListener) this);
		content.addComponent(appTabs);
		content.setComponentAlignment(appTabs, Alignment.TOP_CENTER);

		runSimulation = new Panel();
		appTabs.addTab(runSimulation).setCaption(RUN_TAB_TITLE);

		manageTemplates = new Panel();
		appTabs.addTab(manageTemplates).setCaption(MANAGE_TAB_TITLE);
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		TabSheet tabs = event.getTabSheet();
		Component selectedTab = tabs.getSelectedTab();
		if (selectedTab == runSimulation) {
			runSimulation.removeAllComponents();
			runSimulation.addComponent(createRunSimulationView());
		} else if (selectedTab == manageTemplates) {
			manageTemplates.removeAllComponents();
			manageTemplates.addComponent(getManageTemplatesView());
		} else {
			throw new RuntimeException("unexpected tab.");
		}
	}

	/**
	 * EFFECTS: Returns a new instance of the ManageSimulationsView.
	 * 
	 */
	private Component createRunSimulationView() {
		return new ManageSimulationsView(this, controller);
	}

	/**
	 * EFFECTS: Returns a new instance of hte ManageTemplatesView.
	 * 
	 */
	private Component getManageTemplatesView() {
		return new ManageTemplatesView(this);
	}

}
