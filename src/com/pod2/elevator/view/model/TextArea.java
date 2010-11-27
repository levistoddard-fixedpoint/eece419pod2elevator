package com.pod2.elevator.view.model;

import javax.swing.JTextArea;

public class TextArea extends JTextArea {

	public TextArea() {
		super();
	}

	public void append(String text) {
		super.append(text);
		try {
			this.setCaretPosition(this.getCaretPosition() + text.length());
		} catch (IllegalArgumentException e) {

		}
	}

}