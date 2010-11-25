package com.pod2.elevator.view.analysis;

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
	private JTextArea log;
	private AnalysisChartPanel analysisChartPanel;
	private AnalysisStatusPanel analysisStatusPanel;
	private int numFloors;
	private int numElevators;
	
	public AnalysisPanel(int numFloors, int numElevators){
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

	protected void statusUpdate(ArrayList<double[]> elevatorPosition, ArrayList<double[]> cumulativeDistance, ArrayList<long[]> cumulativeServiceTime, ArrayList<int[]> passengersWaiting, String simulationName, long startQuantum, long stopQuantum, int numberPassengersDelivered, double meanTimeToFailure, double meanWaitTime){
		analysisChartPanel.statusUpdate(elevatorPosition, cumulativeDistance, cumulativeServiceTime, passengersWaiting);
		analysisStatusPanel.statusUpdate(simulationName, startQuantum, stopQuantum, numberPassengersDelivered, meanTimeToFailure, meanWaitTime);
}
	
}
