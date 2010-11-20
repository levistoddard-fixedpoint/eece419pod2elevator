package com.pod2.elevator.view.active.elevator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.pod2.elevator.core.MotionStatus;
import com.pod2.elevator.core.ServiceStatus;

public class Elevator extends JPanel implements ActionListener{
	
	private int id;
	private int numFloors;
	
	private int x = 0;
	private int y = 0;
	
	private double position = 0;
	private int openWidth;
	private Set<Integer> floorsOffLimit = new TreeSet<Integer>();
	
	private MotionStatus motionStatus = MotionStatus.DoorsOpening;
	private ServiceStatus serviceStatus = ServiceStatus.InService;
	
	private Timer timer;

	public Elevator(int id, int numFloors) {
		this.id = id;
		this.numFloors = numFloors;
		openWidth=1;
		timer = new Timer(1, this);
	}
	
	public void paint (Graphics g)
	{
		super.paint(g);
		Dimension size = this.getSize();
		if(serviceStatus != ServiceStatus.Failed){
			y = (int) (39*(numFloors - position - 1));
		}
		if(serviceStatus == ServiceStatus.InService){
			g.setColor(Color.GREEN);
		}else {
			g.setColor(Color.GRAY);
		}
		g.fillRect(0, y, size.width/2+1-openWidth, 39);
		g.fillRect(size.width/2+1+openWidth, y, size.width-1, 39);
		for(Integer i : floorsOffLimit){
			g.setColor(Color.RED);
			int height = (39*(numFloors - i - 1));
			g.fillRect(0, height, size.width, 39);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		switch(motionStatus){
		case DoorsOpening:
			openWidth = (int) (this.getSize().getWidth()/2 - 5);
			break;
		case DoorsClosing:
			openWidth = 1;
			break;
		}
		
		this.repaint();
	}
	
	protected void statusUpdate(double position, Set<Integer> floorsOffLimit, MotionStatus motionStatus, ServiceStatus serviceStatus){
		this.motionStatus = motionStatus;
		this.serviceStatus = serviceStatus;
		switch(motionStatus){
		case DoorsOpening:
		case DoorsClosing:
			if(serviceStatus == ServiceStatus.InService){
				timer.start();
			}else{
				timer.stop();
			}
			break;
		default:
			timer.stop();
			break;
		}
		this.position = position;
		this.floorsOffLimit = floorsOffLimit;
		this.repaint();
	}
}
