package com.pod2.elevator.view.model;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import com.pod2.elevator.view.listener.ExitAction;

public class Toolbar extends JToolBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1125266451921017180L;

	public Toolbar() {
		ImageIcon iconExit = new ImageIcon("icons/exit.png");

		JButton exit = new JButton(iconExit);

		add(exit);

		exit.addActionListener(new ExitAction());
	}
}
