package com.pod2.elevator.view;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Elevator extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6807421655052332523L;

	private int numFloor;
	private int numComponent;

	private int x = 0;
	private int y = 0;

	private double floor = 0;

	private Image img;

	public Elevator(int numFloor, int numComponent) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image elevator = toolkit.getImage("icons/elevator.png");

		this.numFloor = numFloor;
		this.numComponent = numComponent;
		img = elevator;

		Dimension size = this.getSize();
		x = 0;
		y = size.height - size.height / numFloor;
	}

	public void paint(Graphics g) {
		if (img != null) {
			Dimension size = this.getSize();
			y = (int) (size.height - size.height / numFloor - floor
					* size.height / numFloor);
			g.drawImage(img, x, y, size.width, size.height / numFloor, this);
		}
	}

	public void setImage(Image img) {
		this.img = img;
	}

	public void setFloor(double currentPosition) {
		floor = currentPosition;
	}
}
