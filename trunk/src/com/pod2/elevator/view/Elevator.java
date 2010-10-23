package com.pod2.elevator.view;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Elevator extends Canvas{

		/**
	 * 
	 */
	private static final long serialVersionUID = -6807421655052332523L;
	
	private JFrame elevatorStatus;
	private Image img;
	private int numComponent;
	
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	private Image elevator = toolkit.getImage("icons/elevator.png");

		public Elevator(int numComponent) {
			this.numComponent = numComponent;
			img = elevator;
		}
		
		public void paint (Graphics g)
		{
			if(img != null){
				Dimension size = this.getSize();
				g.drawImage(img, 0, 0, size.width, size.height/10, this);
			}
		}
		
		public void setImage(Image img){
			this.img = img;
		}
}
