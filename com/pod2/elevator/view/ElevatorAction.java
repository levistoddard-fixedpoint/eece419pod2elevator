package com.pod2.elevator.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ElevatorAction implements MouseListener{
	private int id;
	private int numComponent;

	public ElevatorAction(int id, int numComponent){
		super();
		this.id = id;
		this.numComponent = numComponent;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		MainPanel.addElevatorStatus(new ElevatorStatus(id, numComponent));
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
