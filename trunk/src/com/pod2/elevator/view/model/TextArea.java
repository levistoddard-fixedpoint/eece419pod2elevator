package com.pod2.elevator.view.model;

import javax.swing.JTextArea;
public class TextArea extends JTextArea {
	
	public TextArea(String string) {
		super(string);
	}

	public void append(String text) {
		super.append(text);
		this.setCaretPosition(this.getCaretPosition()+text.length());
	}
	
}