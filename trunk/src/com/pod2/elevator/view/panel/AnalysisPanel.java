package com.pod2.elevator.view.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class AnalysisPanel extends JPanel{
	static private JTextArea log;
	static private AnalysisChartPanel analysisChartPanel;
	static private AnalysisStatusPanel analysisStatusPanel;
	static private int numFloors;
	static private int numElevators;
	static private boolean dirty = false;
	
	static private AnalysisPanel analysisPanel;
	
	static public AnalysisPanel getAnalysisView(){
		if (analysisPanel == null || dirty){
			analysisPanel = new AnalysisPanel(numFloors, numElevators);
			dirty = false;
		}
		return analysisPanel;
	}
	
	private AnalysisPanel(int numFloors, int numElevators){
		//Initialize Variables
		this.numFloors = numFloors;
		this.numElevators = numElevators;
		analysisChartPanel = new AnalysisChartPanel(numFloors, numElevators);
		analysisStatusPanel = new AnalysisStatusPanel();
		log = new JTextArea("Logging goes here\n");
		
		//Set log properties
		log.setRows(5);
		log.setEditable(false);
		log.setBackground(Color.BLACK);
		log.setForeground(Color.WHITE);
		JScrollPane logScroll = new JScrollPane(log);
		
		//Set layout
		this.setLayout(new BorderLayout());
		
		//Add components
		this.add(analysisChartPanel, BorderLayout.CENTER);
		this.add(logScroll, BorderLayout.SOUTH);
		this.add(analysisStatusPanel, BorderLayout.EAST);
	}
	
	static public void init(int numFloors, int numElevators){
		AnalysisPanel.numFloors = numFloors;
		AnalysisPanel.numElevators = numElevators;
		dirty = true;
	}

	public void statusUpdate(ArrayList<double[]> elevatorPosition, ArrayList<double[]> cumulativeDistance, ArrayList<long[]> cumulativeServiceTime, ArrayList<int[]> passengersWaiting, int numberPassengersDelivered, double meanTimeToFailure, double meanWaitTime){
		analysisChartPanel.statusUpdate(elevatorPosition, cumulativeDistance, cumulativeServiceTime, passengersWaiting);
		analysisStatusPanel.statusUpdate(numberPassengersDelivered, meanTimeToFailure, meanWaitTime);
}
	
}
