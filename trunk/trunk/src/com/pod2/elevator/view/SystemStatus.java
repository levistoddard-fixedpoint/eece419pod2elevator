package com.pod2.elevator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class SystemStatus extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7488689520399741310L;

	private int numFloor;
	
	private JPanel timePanel = new JPanel();
	private JTextField time = new JTextField("Time");
	
	private JPanel statusPanel = new JPanel();
	private JPanel floorButtons[];
	private JLabel floors[];
	private JLabel floorQueues[];
	
	private JPanel cummulativePanel = new JPanel();
	private JTextArea cummulative = new JTextArea("Cummulative\nStats");

	public SystemStatus(int numFloor) {
		
		this.numFloor = numFloor;
		floorButtons = new JPanel[numFloor];
		floors = new JLabel[numFloor];
		floorQueues = new JLabel[numFloor];
		
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(100,0));
		
		//Add time
		timePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		timePanel.setLayout(new BorderLayout());
		time.setPreferredSize(new Dimension(90, 30));
		time.setHorizontalAlignment(JLabel.CENTER);
		time.setBackground(Color.GREEN);
		timePanel.add(time, BorderLayout.CENTER);
		this.add(timePanel, BorderLayout.NORTH);
		
		//Add status
		statusPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		statusPanel.setLayout(new GridLayout(0, 3, 5, 5));
		
		for(int i=0; i<this.numFloor; i++){
			//Add floor button status
			if(i==0){
				floorButtons[i] = new FloorButtonPanel(0);
			}else if(i == numFloor -1){
				floorButtons[i] = new FloorButtonPanel(1);
			}else{
				floorButtons[i] = new FloorButtonPanel(2);
			}
			statusPanel.add(floorButtons[i]);
			
			//Add label for floor queue size
			floorQueues[i] = new JLabel("2");
			floorQueues[i].setHorizontalAlignment(JLabel.CENTER);
			floorQueues[i].setToolTipText("Passengers Waiting");
			statusPanel.add(floorQueues[i]);
		
			//Add label for floor number
			floors[i] = new JLabel("1");
			floors[i].setHorizontalAlignment(JLabel.CENTER);
			floors[i].setToolTipText("Floor Number");
			statusPanel.add(floors[i]);
		}
		this.add(statusPanel, BorderLayout.CENTER);
		
		//Add cumulative stats
		cummulativePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		cummulativePanel.setLayout(new BorderLayout());
		cummulative.setPreferredSize(new Dimension(90, 100));
		cummulative.setBackground(Color.GREEN);
		cummulativePanel.add(cummulative, BorderLayout.CENTER);
		this.add(cummulativePanel, BorderLayout.SOUTH);
		
	}

}
