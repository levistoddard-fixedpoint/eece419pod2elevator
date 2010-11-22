package com.pod2.elevator.web.views.templates;

import java.util.Date;

import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.data.SimulationTemplateDetail;
import com.pod2.elevator.data.SimulationTemplateRepository;
import com.vaadin.data.Item;
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

	private static final String CREATE_BUTTON_TEXT = "Create Template";

	// private static final String COL_NAME = "Name";
	// private static final String COL_CREATE_DATE = "Created";
	// private static final String COL_EDIT_DATE = "Last Edit";
	// private static final String COL_EDIT = "Edit";
	// private static final String COL_DELETE = "Delete";

	private final Window parent;
	private final SimulationTemplateRepository repository;

	private VerticalLayout layout;
	private Table templates;

	public ManageTemplatesView(Window parent, SimulationTemplateRepository repository) {
		super();
		this.parent = parent;
		this.repository = repository;
		initLayout();
	}

	public void initLayout() {
		layout = new VerticalLayout();
		initCreateTemplateButton();
		layout.addComponent(new Label("&nbsp;", Label.CONTENT_XHTML));
		initTemplatesTable();
		setCompositionRoot(layout);
	}

	void templateCreated(SimulationTemplateDetail template) {
		addTemplateToContainer(template);
	}

	void templateEdited(SimulationTemplateDetail template) {
		Item item = templates.getItem(template.getId());
		item.getItemProperty(TemplateFields.EditDate).setValue(template.getLastEdit());
		item.getItemProperty(TemplateFields.Name).setValue(template.getName());
	}

	private class CreateClickHandler implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			SimulationTemplate newTemplate = new SimulationTemplate();
			Window createWindow = new CreateEditTemplateWindow(parent, repository, newTemplate);
			createWindow.setModal(true);
			createWindow.center();
			createWindow.setWidth(800, Sizeable.UNITS_PIXELS);
			parent.addWindow(createWindow);
		}
	}

	private class EditClickHandler implements ClickListener {

		private final int templateId;

		private EditClickHandler(int templateId) {
			this.templateId = templateId;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			SimulationTemplateDetail detail = new SimulationTemplateDetail(templateId, "booya",
					new Date(), new Date());
			ManageTemplatesView.this.templateEdited(detail);
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

	private void initCreateTemplateButton() {
		Button createBtn = new Button(CREATE_BUTTON_TEXT, new CreateClickHandler());
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
			for (SimulationTemplateDetail templ : repository.getAllTemplates()) {
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
		Button edit = new Button("Edit", new EditClickHandler(id));
		Button delete = new Button("Delete", new DeleteClickHandler(id));
		Object[] templateRow = new Object[] { template.getName(), template.getCreated(),
				template.getLastEdit(), edit, delete };
		templates.addItem(templateRow, id);
	}

}
