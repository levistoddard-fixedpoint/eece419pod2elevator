package com.pod2.elevator.web.views.templates;

import java.util.Date;

import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.data.SimulationTemplateDetail;
import com.pod2.elevator.data.SimulationTemplateRepository;
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
		Button createBtn = new Button("Create Template", new CreateClickHandler());
		layout.addComponent(createBtn);
	}

	private void initTemplatesTable() {
		templates = new Table();
		templates.setWidth(100, UNITS_PERCENTAGE);

		templates.addContainerProperty(TemplateFields.Name, String.class, null);
		templates.addContainerProperty(TemplateFields.CreatedDate, Date.class, null);
		templates.addContainerProperty(TemplateFields.EditDate, Date.class, null);
		templates.addContainerProperty(TemplateFields.Edit, Button.class, null);
		templates.addContainerProperty(TemplateFields.Delete, Button.class, null);

		try {
			/* populate the table table */
			for (SimulationTemplateDetail templ : SimulationTemplateRepository.getAllTemplates()) {
				addTemplateToContainer(templ);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Notification databaseError = new Notification("Error while retrieving templates.<br>",
					e.getMessage(), Notification.TYPE_ERROR_MESSAGE);
			parent.showNotification(databaseError);
		}
		layout.addComponent(templates);
	}

	private void addTemplateToContainer(SimulationTemplateDetail template) {
		int id = template.getId();
		Button edit = new Button("Edit", new CopyClickHandler(id));
		Button delete = new Button("Delete", new DeleteClickHandler(id));
		Object[] templateRow = new Object[] { template.getName(), template.getCreated(),
				template.getLastEdit(), edit, delete };
		templates.addItem(templateRow, id);
	}

	public enum TemplateFields {

		Name("Name"), CreatedDate("Created Date"), EditDate("Last Edit Date"), Edit("Edit"), Delete(
				"Delete");

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
			SimulationTemplate newTemplate = new SimulationTemplate();
			Window createWindow = new CreateTemplateWindow(ManageTemplatesView.this, parent,
					newTemplate);
			createWindow.setModal(true);
			createWindow.center();
			createWindow.setWidth(800, Sizeable.UNITS_PIXELS);
			parent.addWindow(createWindow);
		}
	}

	private class DeleteClickHandler implements ClickListener {
		private final int templateId;

		private DeleteClickHandler(int templateId) {
			this.templateId = templateId;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			// ManageTemplatesView.this.showNotification(templateId);
		}
	}

	private class CopyClickHandler implements ClickListener {

		private final int templateId;

		private CopyClickHandler(int templateId) {
			this.templateId = templateId;
		}

		@Override
		public void buttonClick(ClickEvent event) {
//			SimulationTemplateDetail detail = new SimulationTemplateDetail(templateId, "booya",
//					new Date(), new Date());
//			ManageTemplatesView.this.templateEdited(detail);
		}
	}

}
