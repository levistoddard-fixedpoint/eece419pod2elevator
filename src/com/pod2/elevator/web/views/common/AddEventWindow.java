package com.pod2.elevator.web.views.common;

import com.pod2.elevator.data.TemplateEvent;
import com.pod2.elevator.web.views.EditWindow;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.VerticalLayout;

/**
 * OVERVIEW: An EditWindow which allows users to input parameters for a single
 * event (i.e subclass of TemplateEvent).
 * 
 */
public class AddEventWindow<T extends TemplateEvent> extends EditWindow {

	private final EventConsumer consumer;
	private final FormFieldFactory fieldFactory;
	private final String[] fields;
	private final T event;
	private Form editForm;

	AddEventWindow(EventConsumer consumer, FormFieldFactory fieldFactory, String[] fields, T event) {
		super();
		this.consumer = consumer;
		this.fieldFactory = fieldFactory;
		this.fields = fields;
		this.event = event;
		super.render();
	}

	@Override
	protected final Component getEditControls() {
		VerticalLayout layout = new VerticalLayout();
		editForm = new Form();
		editForm.setItemDataSource(new BeanItem<T>(event));
		editForm.setFormFieldFactory(fieldFactory);
		editForm.setVisibleItemProperties(fields);
		editForm.setWriteThrough(true);
		layout.addComponent(editForm);
		return layout;
	}

	@Override
	protected final void onSave() {
		try {
			editForm.commit();
			consumer.insertEvent(event);
			close();
		} catch (InvalidValueException e) {
			/* Let user correct form... */
		}
	}

}
