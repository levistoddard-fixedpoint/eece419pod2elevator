package com.pod2.elevator.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu extends JMenuBar{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4942799975819603731L;

	public Menu() {
		setBorder(BorderFactory.createEtchedBorder());
		
		ImageIcon iconExit = new ImageIcon("icons/exit.png");
		
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem fileClose = new JMenuItem("Close",iconExit);
		fileClose.setMnemonic(KeyEvent.VK_C);
		fileClose.setToolTipText("Exit application");
		fileClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event){
				System.exit(0);
			}
		});
		
		file.add(fileClose);
		
		add(file);
		
	}
}
