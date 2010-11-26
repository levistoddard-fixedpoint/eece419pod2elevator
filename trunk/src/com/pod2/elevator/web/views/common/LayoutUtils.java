package com.pod2.elevator.web.views.common;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class LayoutUtils {
	
	public static Component createSpacer() {
		return new Label("&nbsp;", Label.CONTENT_XHTML);
	}

	public static String getFieldWidth() {
		return "15em";
	}

}
