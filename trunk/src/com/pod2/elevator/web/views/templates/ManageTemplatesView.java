package com.pod2.elevator.web.views.templates;

import java.util.Date;

import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.data.SimulationTemplateDetail;
import com.pod2.elevator.data.SimulationTemplateRepository;
import com.pod2.elevator.web.views.ControlWindow;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

public class ManageTemplatesView extends CustomComponent {

	private final Window parent;

	private VerticalLayout layout;
	private Table templates;

	public ManageTemplatesView(Window parent) {
		super();
		this.parent = parent;
		initLayout();
	}

	void templateCreated(SimulationTemplateDetail template) {
		addTemplateToContainer(template);
	}

	private void initLayout() {
		layout = new VerticalLayout();
		initCreateTemplateButton();
		layout.addComponent(new Label("&nbsp;", Label.CONTENT_XHTML));
		initTemplatesTable();
		setCompositionRoot(layout);
	}

	private void initCreateTemplateButton() {
		layout.addComponent(new Button("Create Template", new CreateClickHandler()));
	}

	private void initTemplatesTable() {
		templates = new Table();
		templates.setWidth(100, UNITS_PERCENTAGE);

		templates.addContainerProperty(TemplateFields.Name, String.class, null);
		templates.addContainerProperty(TemplateFields.CreatedDate, Date.class, null);
		templates.addContainerProperty(TemplateFields.EditDate, Date.class, null);
		templates.addContainerProperty(TemplateFields.Copy, Button.class, null);
		templates.addContainerProperty(TemplateFields.Delete, Button.class, null);

		try {
			/* populate the table table */
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

	private void addTemplateToContainer(SimulationTemplateDetail template) {
		int id = template.getId();
		Button copyButton = new Button("Copy", new CopyClickHandler(id));
		Button deleteButton = new Button("Delete", new DeleteClickHandler(id));
		Object[] templateRow = new Object[] { template.getName(), template.getCreated(),
				template.getLastEdit(), copyButton, deleteButton };
		templates.addItem(templateRow, id);
	}

	private void showCreateWindow(SimulationTemplate template) {
		Window createWindow = new CreateTemplateWindow(this, parent, template);
		createWindow.setModal(true);
		createWindow.center();
		createWindow.setWidth(ControlWindow.APP_WIDTH, Sizeable.UNITS_PIXELS);
		parent.addWindow(createWindow);
	}

	public enum TemplateFields {

		Name("Name"), CreatedDate("Created"), EditDate("Last Edit"), Copy("Copy"), Delete("Delete");

		private String title;

		private TemplateFields(String title) {
			this.title = title;
		}

		public String toString() {
			return title;
		}

	}

	private class CreateClickHandler implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			showCreateWindow(new SimulationTemplate());
		}
	}

	private class DeleteClickHandler implements ClickListener {
		private final int templateId;

		private DeleteClickHandler(int templateId) {
			this.templateId = templateId;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			try {
				SimulationTemplateRepository.deleteTemplate(templateId);
				templates.removeItem(templateId);
			} catch (Exception e) {
				Notification databaseError = new Notification("Unable to delete template.<br>",
						e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
				parent.showNotification(databaseError);
			}
		}
	}

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
