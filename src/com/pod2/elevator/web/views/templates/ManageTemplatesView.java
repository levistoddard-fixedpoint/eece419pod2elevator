package com.pod2.elevator.web.views.templates;

import java.util.Date;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.data.SimulationDataRepository;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.data.SimulationTemplateDetail;
import com.pod2.elevator.data.SimulationTemplateRepository;
import com.pod2.elevator.main.CentralController;
import com.pod2.elevator.web.views.ControlWindow;
import com.pod2.elevator.web.views.common.LayoutUtils;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

/**
 * OVERVIEW: A Component which allows a user to view all SimulationTemplates
 * which exist in the system, and to copy or delete these templates.
 * 
 */
public class ManageTemplatesView extends CustomComponent {

	private final Window parent;
	private final CentralController controller;
	private VerticalLayout layout;
	private Table templates;

	public ManageTemplatesView(Window parent, CentralController controller) {
		super();
		this.parent = parent;
		this.controller = controller;
		layout = new VerticalLayout();
		initCreateTemplateButton();
		layout.addComponent(LayoutUtils.createSpacer());
		initTemplatesTable();
		setCompositionRoot(layout);
	}

	/**
	 * MODIFIES: templates
	 * 
	 * EFFECTS: Adds template to the collection of currently displayed
	 * templates.
	 */
	void templateCreated(SimulationTemplateDetail template) {
		addTemplateToContainer(template);
	}

	/**
	 * MODIFIES: templates
	 * 
	 * EFFECTS: Deletes template with templateId from the
	 * SimulationTemplateRepository. Displays notification to user if template
	 * cannot be deleted. Template is removed from templates if delete is
	 * successful.
	 */
	void deleteTemplate(int templateId) {
		try {
			ActiveSimulation simulation = controller.getSimulation();
			if (simulation != null && simulation.getTemplate().getId() == templateId) {
				Notification currentlyRunningMessage = new Notification("Template in use.<br>",
						"The currently running simulation is using this template. You must stop the"
								+ " simulation before deleting the template.",
						Notification.TYPE_WARNING_MESSAGE);
				parent.showNotification(currentlyRunningMessage);
			} else {
				SimulationTemplateRepository.deleteTemplate(templateId);
				templates.removeItem(templateId);
			}
		} catch (Exception e) {
			Notification databaseError = new Notification("Unable to delete template.<br>",
					e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
			parent.showNotification(databaseError);
		}
	}

	/**
	 * MODIFIES: layout
	 * 
	 * EFFECTS: Adds a component to this layout which allows the user to create
	 * a new template.
	 * 
	 */
	private void initCreateTemplateButton() {
		layout.addComponent(new Button("Create Template", new CreateClickHandler()));
	}

	/**
	 * MODIFIES: layout
	 * 
	 * EFFECTS: Adds a component to this layout which allows the user to view,
	 * copy, and delete any template in the system.
	 */
	private void initTemplatesTable() {
		templates = new Table();
		templates.setWidth(100, UNITS_PERCENTAGE);

		templates.addContainerProperty(TemplateFields.Name, String.class, null);
		templates.addContainerProperty(TemplateFields.CreatedDate, Date.class, null);
		templates.addContainerProperty(TemplateFields.Copy, Button.class, null);
		templates.addContainerProperty(TemplateFields.Delete, Button.class, null);

		try {
			for (SimulationTemplateDetail templ : SimulationTemplateRepository.getAllTemplates()) {
				addTemplateToContainer(templ);
			}
		} catch (Exception e) {
			Notification databaseError = new Notification("Unable to retrieve templates.<br>",
					e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
			parent.showNotification(databaseError);
		}
		layout.addComponent(templates);
	}

	/**
	 * MODIFIES: templates
	 * 
	 * EFFECTS: Adds template to the collection of displayed templates.
	 */
	private void addTemplateToContainer(SimulationTemplateDetail template) {
		int id = template.getId();
		Button copyButton = new Button("Copy", new CopyClickHandler(id));
		Button deleteButton = new Button("Delete", new DeleteClickHandler(id));
		Object[] templateRow = new Object[] { template.getName(), template.getCreated(),
				copyButton, deleteButton };
		templates.addItem(templateRow, id);
	}

	/**
	 * MODIFIES: parent
	 * 
	 * OVERVIEW: Displays a Window for creating a new template. Fields of the
	 * template are initialized from the provided template.
	 * 
	 */
	private void showCreateWindow(SimulationTemplate template) {
		Window createWindow = new CreateTemplateWindow(this, parent, template);
		createWindow.setModal(true);
		createWindow.center();
		createWindow.setWidth(ControlWindow.APP_WIDTH, Sizeable.UNITS_PIXELS);
		parent.addWindow(createWindow);
	}

	/**
	 * OVERVIEW: An enumeration of the fields which should be visible when
	 * viewing an existing template.
	 * 
	 */
	public enum TemplateFields {

		Name("Name"), CreatedDate("Created"), Copy("Copy"), Delete("Delete");

		private String title;

		private TemplateFields(String title) {
			this.title = title;
		}

		public String toString() {
			return title;
		}

	}

	/**
	 * OVERVIEW: A ClickListener which displays a component that allows the user
	 * to create a new template, when clicked.
	 * 
	 */
	private class CreateClickHandler implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			showCreateWindow(new SimulationTemplate());
		}
	}

	/**
	 * OVERVIEW: A ClickListener which deletes a template when clicked.
	 * 
	 */
	private class DeleteClickHandler implements ClickListener {
		private final int templateId;

		private DeleteClickHandler(int templateId) {
			this.templateId = templateId;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			try {
				int count = SimulationDataRepository.getSimulationCountByTemplate(templateId);
				if (count > 0) {
					Window confirmDelete = new ConfirmDeleteWindow(ManageTemplatesView.this,
							templateId, count);
					confirmDelete.setModal(true);
					confirmDelete.setWidth(400, UNITS_PIXELS);
					confirmDelete.center();
					parent.addWindow(confirmDelete);
				} else {
					deleteTemplate(templateId);
				}
			} catch (Exception e) {
				Notification databaseError = new Notification(
						"Unable to retrieve associated simulations.<br>", e.getMessage(),
						Notification.TYPE_ERROR_MESSAGE);
				parent.showNotification(databaseError);
			}
		}
	}

	/**
	 * OVERVIEW: A ClickListener which displays a component that allows the user
	 * to create a modified copy of a template, when clicked.
	 * 
	 */
	private class CopyClickHandler implements ClickListener {

		private final int templateId;

		private CopyClickHandler(int templateId) {
			this.templateId = templateId;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			try {
				SimulationTemplate template = SimulationTemplateRepository.getTemplate(templateId);
				template.setName("Copy of " + template.getName());
				showCreateWindow(template);
			} catch (Exception e) {
				Notification databaseError = new Notification("Unable to retrieve template.<br>",
						e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
				parent.showNotification(databaseError);
			}
		}
	}

}
