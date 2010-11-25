package com.pod2.elevator.web.views.templates;

import com.vaadin.data.Property;
import com.vaadin.ui.Table;

public class EventTable extends Table {

	@Override
	protected String formatPropertyValue(Object rowId, Object colId, Property property) {
		if (property.getValue() != null && colId.equals("request")) {
			boolean isPutInService = ((Boolean) property.getValue()).booleanValue();
			if (isPutInService) {
				return "Put elevator in service";
			} else {
				return "Take elevator out of service";
			}
		}
		return super.formatPropertyValue(rowId, colId, property);
	}

}