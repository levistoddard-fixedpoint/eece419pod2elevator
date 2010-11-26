package com.pod2.elevator.web.views;

import com.pod2.elevator.web.views.common.LayoutUtils;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * OVERVIEW: A Window with "Save", and "Cancel" buttons that allows subclasses
 * to override the actions that occur when "Save" or "Cancel" are clicked.
 * 
 */
public abstract class EditWindow extends Window {

	public EditWindow() {
		super();
	}

	/**
	 * MODIFIES: this
	 * 
	 * EFFECTS: Calls getEditControls to get the main content to display, and
	 * displays this main content along with "Save" or "Cancel" buttons. MUST be
	 * called by subclasses when ready to show the display.
	 * 
	 */
	protected final void render() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(getEditControls());
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.addComponent(new Button("Save", new SaveClickListener()));
		buttons.addComponent(LayoutUtils.createSpacer());
		buttons.addComponent(new Button("Cancel", new CancelClickListener()));
		layout.addComponent(buttons);
		setContent(layout);
	}

	/**
	 * EFFECTS: Returns the Component which should be displayed as the content
	 * of this Window (i.e. the interface which the user interacts with before
	 * clicking the "Save" or "Cancel" buttons).
	 * 
	 */
	protected abstract Component getEditControls();

	/**
	 * EFFECTS: Executes any action that should be done when the user clicks the
	 * "Save" button, and closes this window if necessary.
	 * 
	 */
	protected abstract void onSave();

	/**
	 * EFFECTS: Executes any action that should be done when the user clicks the
	 * "Cancel" button. Closes this Window by default.
	 * 
	 */
	protected void onCancel() {
		close();
	}

	/**
	 * OVERVIEW: A ClickListener which invokes the onSave callback when clicked.
	 * 
	 */
	private class SaveClickListener implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			EditWindow.this.onSave();
		}
	}

	/**
	 * OVERVIEW: A ClickListener which invokes the onCancel callback when
	 * clicked.
	 * 
	 */
	private class CancelClickListener implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			EditWindow.this.onCancel();
		}
	}

}