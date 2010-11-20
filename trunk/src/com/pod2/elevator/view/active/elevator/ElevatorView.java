package com.pod2.elevator.view.active.elevator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.pod2.elevator.core.MotionStatus;
import com.pod2.elevator.core.ServiceStatus;

public class ElevatorView extends JPanel{
	private JScrollPane scrollPane;
	private JPanel rootPanel;
	private GridLayout gridLayout;
	
	private ElevatorPanel elevators[];
	
	private int i;
	private int numElevators;
	private int numFloors;
	
	public ElevatorView(int numFloors, int numElevators){
		//Initialize variables
		this.numFloors = numFloors;
		this.numElevators = numElevators;
		elevators = new ElevatorPanel[numElevators];
		rootPanel = new JPanel();
		gridLayout = new GridLayout(2,5,5,5);
		
		//Root panel
		rootPanel = new JPanel();
		rootPanel.setPreferredSize(new Dimension(800,600));
		rootPanel.setLayout(gridLayout);
		rootPanel.setBackground(Color.LIGHT_GRAY);
		
		//Scrollpane
		scrollPane = new JScrollPane(rootPanel);
		scrollPane.setPreferredSize(new Dimension(800,600));
		
		//Elevator Panels
		for(i=0; i<numElevators; i++){
			elevators[i] = new ElevatorPanel(i, numFloors);
			rootPanel.add(elevators[i]);
		}
		
		//Add Scroll Pane
		this.add(scrollPane);
	}
	
	public void paint(Graphics g){
		//Get current size
		Dimension size = this.getSize();
		
		//Update scroll pane to fit in current size
		scrollPane.setPreferredSize(size);
		
		//Calculate number of elevator panels fit in a row
		int col = (int) Math.floor(this.getSize().getWidth()/135 - 1);
		//Ensure no divide by 0 error
		if(col==0)col=1;
		//Calculate how many columns are required
		int row = (int) Math.ceil(numElevators / col + 1);
		
		//Set layout to match resolution
		gridLayout.setColumns(col);
		gridLayout.setRows(row);
		
		//Scale root panel to reflect change in row
		int x = (int) (0);
		int y = (int) (243 * row);
		rootPanel.setPreferredSize(new Dimension(x, y));
		rootPanel.revalidate();
		
		super.paint(g);
		for(ElevatorPanel e : elevators){
			e.repaint();
		}
	}
	
	public void statusUpdate(int eid, double position, Set<Integer> floorsOffLimit, MotionStatus motionStatus, ServiceStatus serviceStatus){
		elevators[eid].statusUpdate(position, floorsOffLimit, motionStatus, serviceStatus);
	}
	
}
