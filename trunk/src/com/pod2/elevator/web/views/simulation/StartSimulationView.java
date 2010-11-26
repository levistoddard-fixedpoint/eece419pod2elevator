package com.pod2.elevator.web.views.simulation;

import com.pod2.elevator.data.SimulationTemplateDetail;
import com.pod2.elevator.data.SimulationTemplateRepository;
import com.pod2.elevator.web.views.common.LayoutUtils;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

public class StartSimulationView extends CustomComponent {

	private static final String FIELD_WIDTH = "15em";

	private final ManageSimulationsView manageView;
	private final Window window;
	private final Panel layout;

	private BeanItemContainer<SimulationTemplateDetail> templates;

	private TextField name;
	private Select template;

	public StartSimulationView(Window window, ManageSimulationsView manageView) {
		super();
		this.window = window;
		this.manageView = manageView;
		layout = new Panel("Start Simulation");
		initLayout();
		populateTemplates();
		setCompositionRoot(layout);
	}

	private void initLayout() {
		VerticalLayout formLayout = new VerticalLayout();
		formLayout.setMargin(true);

		/*
		 * Create field to enter simulation name.
		 */
		name = new TextField("Name:");
		name.setWidth(FIELD_WIDTH);
		name.setMaxLength(20);
		name.setRequired(true);
		name.setRequiredError("Please enter a simulation name.");
		formLayout.addComponent(name);
		formLayout.addComponent(LayoutUtils.createSpacer());

		/*
		 * Create field to select template.
		 */
		templates = new BeanItemContainer<SimulationTemplateDetail>(SimulationTemplateDetail.class);
		template = new Select("Template:");
		template.setWidth(FIELD_WIDTH);
		template.setReadThrough(true);
		template.setContainerDataSource(templates);
		formLayout.addComponent(template);
		formLayout.addComponent(LayoutUtils.createSpacer());

		/*
		 * Create button to start the simulation.
		 */
		formLayout.addComponent(new Button("Start", new StartClickListener()));
		formLayout.addComponent(LayoutUtils.createSpacer());

		layout.setContent(formLayout);
	}

	private void populateTemplates() {
		try {
			for (SimulationTemplateDetail detail : SimulationTemplateRepository.getAllTemplates()) {
				templates.addBean(detail);
			}
		} catch (Exception e) {
			Notification databaseError = new Notification("Unable to retrieve templates.<br>",
					e.getMessage(), Notification.TYPE_WARNING_MESSAGE);
			window.showNotification(databaseError);
		}
	}

	private class StartClickListener implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			manageView.startSimulation((String) name.getValue(),
					(SimulationTemplateDetail) template.getValue());
		}
	}

}
