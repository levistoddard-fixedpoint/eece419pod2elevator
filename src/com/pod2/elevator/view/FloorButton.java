package com.pod2.elevator.view;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

public class FloorButton extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5183331883269067220L;
	private Image img;

	public FloorButton(Image img) {
		this.img = img;
	}

	public void paint(Graphics g) {
		if (img != null) {
			Dimension size = this.getSize();
			g.drawImage(img, 0, 0, size.width, size.height, this);
		}
	}

	public void setImage(Image img) {
		this.img = img;
	}
}
