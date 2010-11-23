package com.pod2.elevator.web.views.templates;

import com.pod2.elevator.data.TemplateEvent;
import com.pod2.elevator.web.views.EditWindow;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.VerticalLayout;

/**
 * A Window containing a Form which allows users to add events (i.e subclasses
 * of TemplateEvent) to a SimulationTemplate.
 * 
 */
public class AddEventWindow<T extends TemplateEvent> extends EditWindow {

	private final CreateTemplateWindow templateWindow;
	private final FormFieldFactory fieldFactory;
	private final T event;

	private Form editForm;

	public AddEventWindow(CreateTemplateWindow templateWindow, FormFieldFactory fieldFactory,
			T event) {
		super();
		this.templateWindow = templateWindow;
		this.fieldFactory = fieldFactory;
		this.event = event;
		super.render();
	}

	@Override
	protected final Component getEditControls() {
		VerticalLayout layout = new VerticalLayout();
		editForm = getForm();
		layout.addComponent(editForm);
		return layout;
	}

	@Override
	protected final void onSave() {
		editForm.validate();
		if (editForm.isValid()) {
			templateWindow.insertEvent(event);
			close();
		}
	}

	private Form getForm() {
		Form form = new Form();
		form.setItemDataSource(new BeanItem<T>(event));
		form.setFormFieldFactory(fieldFactory);
		form.setVisibleItemProperties(event.getFields());
		form.setWriteThrough(true);
		return form;
	}

}
