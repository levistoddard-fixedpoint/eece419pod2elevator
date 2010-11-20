package com.pod2.elevator.view.analysis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pod2.elevator.data.SimulationDataRepository;
import com.pod2.elevator.data.SimulationDetail;
import com.pod2.elevator.view.layout.VerticalLayout;

public class AnalysisStatusPanel extends JPanel{
	
	private int numberPassengersDelivered;
	private JLabel numberPassengersDeliveredLabel;
	private JLabel numberPassengersDeliveredDisplay;
	
	private double meanTimeToFailure;
	private JLabel meanTimeToFailureLabel;
	private JLabel meanTimeToFailureDisplay;
	
	private double meanWaitTime;
	private JLabel meanWaitTimeLabel;
	private JLabel meanWaitTimeDisplay;
	
	public AnalysisStatusPanel(){
		this.setLayout(new VerticalLayout());
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		this.setBackground(Color.LIGHT_GRAY);
		
		numberPassengersDeliveredLabel = createLabel(0, "Passengers Delivered");
		meanTimeToFailureLabel = createLabel(0, "Mean Time to Failure");
		meanWaitTimeLabel = createLabel(0, "Average Wait Time");
		
		numberPassengersDeliveredDisplay = createLabel(1, "");
		meanTimeToFailureDisplay = createLabel(1, "");
		meanWaitTimeDisplay = createLabel(1, "");
		
		this.add(Box.createRigidArea(new Dimension(0,5)));
		
		this.add(numberPassengersDeliveredLabel);
		this.add(numberPassengersDeliveredDisplay);
		
		this.add(meanTimeToFailureLabel);
		this.add(meanTimeToFailureDisplay);
		
		this.add(meanWaitTimeLabel);
		this.add(meanWaitTimeDisplay);
	}

	private JLabel createLabel(int type, String s){
		JLabel temp = new JLabel(s);
		if(type==0){
			temp.setBorder(BorderFactory.createRaisedBevelBorder());
			temp.setBackground(Color.WHITE);
			temp.setFont(new Font("Dialog", Font.BOLD, 14));
		}else {
			temp.setBorder(BorderFactory.createLoweredBevelBorder());
			temp.setForeground(Color.GREEN);
			temp.setBackground(Color.BLACK);
			temp.setFont(new Font("Dialog", Font.PLAIN, 14));
		}
		temp.setHorizontalAlignment(JLabel.CENTER);
		temp.setPreferredSize(new Dimension(160, 30));
		temp.setOpaque(true);
		
		return temp;
	}
	
	protected void statusUpdate(int numberPassengersDelivered, double meanTimeToFailure, double meanWaitTime){
		numberPassengersDeliveredDisplay.setText(Integer.toString(numberPassengersDelivered));
		meanTimeToFailureDisplay.setText(Double.toString(meanTimeToFailure));
		meanWaitTimeDisplay.setText(Double.toString(meanWaitTime));
	}
	
}
