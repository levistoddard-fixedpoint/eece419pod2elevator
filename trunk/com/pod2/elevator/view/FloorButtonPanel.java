package com.pod2.elevator.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class FloorButtonPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4656219862499667952L;
	private FloorButton up;
	private FloorButton down;
	private FloorButton blank;
	
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
	
	public void statusUpdate(SystemSnapShot s){
		//TODO: Update up and down button
	}
	
	public void paint (Graphics g)
	{
		Dimension size = this.getSize();
		up.setPreferredSize(new Dimension(0, size.height/2));
		down.setPreferredSize(new Dimension(0, size.height/2));
		super.paint(g);
	}

}
