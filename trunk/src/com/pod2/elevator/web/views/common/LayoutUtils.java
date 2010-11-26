package com.pod2.elevator.web.views.common;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

/**
 * OVERVIEW: A collection of routines to generate common Components and sizes
 * used throughout the web control application.
 * 
 */
public class LayoutUtils {

	/**
	 * EFFECTS: Returns a Component which can be used as a spacer between other
	 * pairs of components. Creates a space of one character wide when used
	 * between Components in a horizontal layout. Creates a space of one line
	 * when used between Components in a vertical layout.
	 */
	public static Component createSpacer() {
		return new Label("&nbsp;", Label.CONTENT_XHTML);
	}

	/**
	 * EFFECTS: Returns the field width that all input Components should be.
	 * 
	 */
	public static String getFieldWidth() {
		return "15em";
	}

}
