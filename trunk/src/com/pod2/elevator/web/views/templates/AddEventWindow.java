package com.pod2.elevator.web.views.templates;

import com.pod2.elevator.data.TemplateEvent;
import com.pod2.elevator.web.views.EditWindow;
import com.pod2.elevator.web.views.common.EventConsumer;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.VerticalLayout;

/**
 * An EditWindow which allows users to add events (i.e subclasses of
 * TemplateEvent) to a SimulationTemplate.
 * 
 */
public class AddEventWindow<T extends TemplateEvent> extends EditWindow {

	// private final CreateTemplateWindow templateWindow;
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
		editForm = getForm();
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
			/* Let user correct form. */
		}
	}

	private Form getForm() {
		Form form = new Form();
		form.setItemDataSource(new BeanItem<T>(event));
		form.setFormFieldFactory(fieldFactory);
		form.setVisibleItemProperties(fields);
		form.setWriteThrough(true);
		return form;
	}

}
