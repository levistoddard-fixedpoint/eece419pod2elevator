package com.pod2.elevator.web.views.simulation;

import com.pod2.elevator.data.SimulationTemplateDetail;
import com.pod2.elevator.data.SimulationTemplateRepository;
import com.pod2.elevator.web.views.common.LayoutUtils;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

/**
 * OVERVIEW: A CustomComponent that allows a user to start a new simulation.
 * 
 */
public class StartSimulationView extends CustomComponent {

	private static final int MIN_NAME_LENGTH = 1;
	private static final int MAX_NAME_LENGTH = 20;

	private final Window window;
	private final ManageSimulationsView manageView;
	private final Panel layout;
	private BeanItemContainer<SimulationTemplateDetail> templates;
	private Form startForm;
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

	/**
	 * MODIFIES: layout
	 * 
	 * EFFECTS: Displays the input fields necessary for starting a new
	 * simulation.
	 */
	private void initLayout() {
		startForm = new Form();
		VerticalLayout formLayout = new VerticalLayout();
		formLayout.setSpacing(true);
		startForm.setLayout(formLayout);

		/*
		 * Create field to enter simulation name.
		 */
		name = new TextField("Name:");
		name.setValidationVisible(true);
		name.setWidth(LayoutUtils.getFieldWidth());
		name.setRequired(true);
		name.setRequiredError("Please enter a simulation name.");
		name.addValidator(new StringLengthValidator("Please enter a simulation name.",
				MIN_NAME_LENGTH, MAX_NAME_LENGTH, false));
		startForm.addField("name", name);

		/*
		 * Create field to select template.
		 */
		templates = new BeanItemContainer<SimulationTemplateDetail>(SimulationTemplateDetail.class);
		template = new Select("Template:");
		template.setValidationVisible(true);
		template.setWidth(LayoutUtils.getFieldWidth());
		template.setReadThrough(true);
		template.setContainerDataSource(templates);
		template.setRequired(true);
		template.setRequiredError("Please select a simulation template.");
		template.setNullSelectionAllowed(false);
		startForm.addField("template", template);

		/*
		 * Create button to start the simulation.
		 */
		startForm.addField("start", new Button("Start", new StartClickListener()));

		VerticalLayout formWrapper = new VerticalLayout();
		formWrapper.setMargin(true);
		formWrapper.addComponent(startForm);
		layout.setContent(formWrapper);
	}

	/**
	 * REQUIRES: templates references a BeanItemContainer.
	 * 
	 * MODIFIES: templates.
	 * 
	 * EFFECTS: Populates templates with available simulation templates from the
	 * database. Displays an error message if the information cannot be
	 * retrieved from the database for any reason.
	 */
	private void populateTemplates() {
		try {
			for (SimulationTemplateDetail detail : SimulationTemplateRepository.getAllTemplates()) {
				templates.addBean(detail);
			}
			if(templates.size() > 0) {
				template.setValue(templates.firstItemId());
				
			}
		} catch (Exception e) {
			Notification retrieveError = new Notification("Unable to retrieve templates.<br>",
					e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
			window.showNotification(retrieveError);
		}
	}

	private class StartClickListener implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			try {
				startForm.commit();
				manageView.startSimulation((String) name.getValue(),
						(SimulationTemplateDetail) template.getValue());
			} catch (InvalidValueException e) {
				/* let user update fields... */
			}
		}
	}

}
