package com.pod2.elevator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FloorStatusPanel extends JPanel{	
	private int numFloors;
	private FloorStatus floorStatus[];

	public FloorStatusPanel(int numFloors){
		this.numFloors = numFloors;
		floorStatus = new FloorStatus[numFloors];
		
		for(int i=0; i< numFloors; i++){
			floorStatus[i] = new FloorStatus(i);
			this.add(floorStatus[i]);
		}
		
		this.setLayout(new VerticalLayout());
	}
	
	protected void statusUpdate(int fid, long quantum, int passengersWaiting, boolean isUpSelected, boolean isDownSelected, long upSelectedQuantum, long downSelectedQuantum){
		floorStatus[fid].statusUpdate(quantum, passengersWaiting, isUpSelected, isDownSelected, upSelectedQuantum, downSelectedQuantum);
	}
	
	private JLabel createLabel(int type, String s){
		JLabel temp = new JLabel(s);
		if(type==0){
			temp.setBorder(BorderFactory.createRaisedBevelBorder());
			temp.setBackground(Color.WHITE);
			temp.setFont(new Font("Dialog", Font.BOLD, 16));
		}else {
			temp.setBorder(BorderFactory.createLoweredBevelBorder());
			temp.setForeground(Color.WHITE);
			temp.setBackground(Color.BLACK);
			temp.setFont(new Font("Dialog", Font.PLAIN, 16));
		}
		temp.setHorizontalAlignment(JLabel.CENTER);
		temp.setPreferredSize(new Dimension(160, 30));
		temp.setOpaque(true);
		
		return temp;
	}
}
