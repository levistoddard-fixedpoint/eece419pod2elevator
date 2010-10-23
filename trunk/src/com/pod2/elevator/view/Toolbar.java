package com.pod2.elevator.view;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;

public class Toolbar extends JToolBar{

	public Toolbar() {
		ImageIcon iconExit = new ImageIcon("icons/exit.png");
		
		JButton exit = new JButton(iconExit);
		
		add(exit);
		
		exit.addActionListener(new ExitAction());
	}
}
