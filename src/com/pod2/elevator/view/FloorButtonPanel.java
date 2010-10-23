package com.pod2.elevator.view;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class FloorButtonPanel extends JPanel{
	
	private Canvas up;
	private Canvas down;
	private Canvas blank;
	
	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	private Image iconUp = toolkit.getImage("icons/floorup.png");
	private Image iconDown = toolkit.getImage("icons/floordown.png");
	private Image iconBlank = toolkit.getImage("icons/blank.png");
	private Image iconUpOn = toolkit.getImage("icons/floorupon.png");
	private Image iconDownOn = toolkit.getImage("icons/floordownon.png");
	
	public FloorButtonPanel(int Type){
		
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new GridLayout(2, 1, 0, 0));
		
		up = new FloorButton(iconUp);
		down = new FloorButton(iconDown);
		blank = new FloorButton(iconBlank);
		
		switch(Type){
		case 0:
			add(blank);
			add(down);
			break;
		case 1:
			add(up);
			add(blank);
			break;
		case 2:
			add(up);
			add(down);
			break;
		default:
			break;
		}
		
	}
	
	public void paint (Graphics g)
	{
		Dimension size = this.getSize();
		up.setPreferredSize(new Dimension(0, size.height/2));
		down.setPreferredSize(new Dimension(0, size.height/2));
		super.paint(g);
	}

}
