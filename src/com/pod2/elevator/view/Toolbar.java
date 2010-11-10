package com.pod2.elevator.view;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

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
