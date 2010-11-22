package com.pod2.elevator.web.views;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public abstract class EditWindow extends Window {

	private VerticalLayout layout;

	public EditWindow() {
		super();
	}

	protected final void render() {
		layout = new VerticalLayout();
		layout.setMargin(true);
		layout.addComponent(getEditControls());
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.addComponent(new Button("Save", new SaveClickListener()));
		buttons.addComponent(new Label("&nbsp;", Label.CONTENT_XHTML));
		buttons.addComponent(new Button("Cancel", new CancelClickListener()));
		layout.addComponent(buttons);
		setContent(layout);
	}

	protected abstract Component getEditControls();

	protected abstract void onSave();

	protected void onCancel() {
		close();
	}

	private class SaveClickListener implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			EditWindow.this.onSave();
		}
	}

	private class CancelClickListener implements ClickListener {
		@Override
		public void buttonClick(ClickEvent event) {
			EditWindow.this.onCancel();
		}
	}

}