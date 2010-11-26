package com.pod2.elevator.web.views.templates;

import com.pod2.elevator.web.views.common.LayoutUtils;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * OVERVIEW: A Window which allows the user to choose whether to confirm or
 * cancel the deletion of a template, given that there are existing simulations
 * which were run with that template.
 * 
 */
public class ConfirmDeleteWindow extends Window {

	private final ManageTemplatesView manageView;
	private final int templateId;

	public ConfirmDeleteWindow(ManageTemplatesView manageView, int templateId, int simulationCount) {
		super();

		this.manageView = manageView;
		this.templateId = templateId;

		setCaption("Confirm Delete Tempalate");

		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		setContent(mainLayout);

		Label confirmMessage = new Label("This template has " + simulationCount + " associated "
				+ (simulationCount == 1 ? "simulation" : "simulations")
				+ ". If you delete this template, all associated simulations will be deleted."
				+ " Are you sure you want to delete?");
		mainLayout.addComponent(confirmMessage);

		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.addComponent(new Button("Confirm", new ConfirmClickListener()));
		buttonsLayout.addComponent(LayoutUtils.createSpacer());
		buttonsLayout.addComponent(new Button("Cancel", new CancelClickListener()));
		mainLayout.addComponent(buttonsLayout);
	}

	/**
	 * OVERVIEW: A ClickListener which deletes the template with templateId and
	 * closes this Window, when clicked.
	 * 
	 */
	private class ConfirmClickListener implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			manageView.deleteTemplate(templateId);
			ConfirmDeleteWindow.this.close();
		}
	}

	/**
	 * OVERVIEW: A ClickListener which closes this Window when clicked.
	 * 
	 */
	private class CancelClickListener implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			ConfirmDeleteWindow.this.close();
		}
	}

}
