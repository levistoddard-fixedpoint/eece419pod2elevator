package com.pod2.elevator.view.analysis;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.pod2.elevator.view.layout.VerticalLayout;

public class AnalysisChartPanel extends JPanel implements ActionListener{
	private JScrollPane scrollPane;
	private JPanel rootPanel;
	
	//Line Graphs
	private int numElevators;
	
	private JFreeChart time_ElevatorPosition;
	private XYSeriesCollection time_ElevatorPositionSeriesCollection = new XYSeriesCollection();
	private XYSeries time_ElevatorPositionSeries[];
	
	private JFreeChart time_CumulativeDistance;
	private XYSeriesCollection time_CumulativeDistanceSeriesCollection = new XYSeriesCollection();;
	private XYSeries time_CumulativeDistanceSeries[];
	
	private JFreeChart time_CumulativeServiceTime;
	private XYSeriesCollection time_CumulativeServiceTimeSeriesCollection = new XYSeriesCollection();;
	private XYSeries time_CumulativeServiceTimeSeries[];
	
	private int numFloors;
	private JFreeChart time_PassengersWaiting;
	private XYSeriesCollection time_PassengersWaitingSeriesCollection = new XYSeriesCollection();;
	private XYSeries time_PassengersWaitingSeries[];
	
	private BufferedImage[] image = new BufferedImage[4];
	
	private JLabel graphLeft = new JLabel();
	private int graphLeftIndex;
	private JLabel graphRight = new JLabel();
	private int graphRightIndex;
	
	private String[] graphList = { "Time vs Elevator Position", "Time vs Cumulative Distance", "Time vs Cumulative Service Time", "Time vs Passengers Waiting" };
	private JComboBox graphComboBoxLeft = new JComboBox(graphList);
	private JComboBox graphComboBoxRight = new JComboBox(graphList);
	
	private JPanel leftPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	
	public AnalysisChartPanel(int numFloors, int numElevators){
		//Initialize Variables
		this.numFloors = numFloors;
		this.numElevators = numElevators;
		
		time_ElevatorPositionSeries = new XYSeries[numElevators];
		for(int i=0; i<time_ElevatorPositionSeries.length; i++){
			time_ElevatorPositionSeries[i] = new XYSeries(new String("Elevator " + i));
			time_ElevatorPositionSeriesCollection.addSeries(time_ElevatorPositionSeries[i]);
		}
		time_ElevatorPosition = ChartFactory.createXYLineChart("Time vs Elevator Position", "Time", "Elevator Position", time_ElevatorPositionSeriesCollection, PlotOrientation.HORIZONTAL, true, false, false);

		time_CumulativeDistanceSeries = new XYSeries[numElevators];
		for(int i=0; i<time_CumulativeDistanceSeries.length; i++){
			time_CumulativeDistanceSeries[i] = new XYSeries(new String("Elevator " + i));
			time_CumulativeDistanceSeriesCollection.addSeries(time_CumulativeDistanceSeries[i]);
		}
		time_CumulativeDistance = ChartFactory.createXYLineChart("Time vs Cumulative Distance", "Time", "Cumulative Distance", time_CumulativeDistanceSeriesCollection, PlotOrientation.HORIZONTAL, true, false, false);
		
		time_CumulativeServiceTimeSeries = new XYSeries[numElevators];
		for(int i=0; i<time_CumulativeServiceTimeSeries.length; i++){
			time_CumulativeServiceTimeSeries[i] = new XYSeries(new String("Elevator " + i));
			time_CumulativeServiceTimeSeriesCollection.addSeries(time_CumulativeServiceTimeSeries[i]);
		}
		time_CumulativeServiceTime = ChartFactory.createXYLineChart("Time vs Service Time", "Time", "Service Time", time_CumulativeServiceTimeSeriesCollection, PlotOrientation.HORIZONTAL, true, false, false);

		time_PassengersWaitingSeries = new XYSeries[numElevators];
		for(int i=0; i<time_PassengersWaitingSeries.length; i++){
			time_PassengersWaitingSeries[i] = new XYSeries(new String("Floor " + i));
			time_PassengersWaitingSeriesCollection.addSeries(time_PassengersWaitingSeries[i]);
		}
		time_PassengersWaiting = ChartFactory.createXYLineChart("Time vs Passengers Waiting", "Time", "Passengers Waiting", time_PassengersWaitingSeriesCollection, PlotOrientation.HORIZONTAL, true, false, false);
		
		graphComboBoxLeft.addActionListener(this);
		graphComboBoxLeft.setAlignmentX(CENTER_ALIGNMENT);
		graphComboBoxRight.addActionListener(this);
		graphComboBoxRight.setAlignmentX(CENTER_ALIGNMENT);
		
		leftPanel.setLayout(new VerticalLayout());
		rightPanel.setLayout(new VerticalLayout());
		
		leftPanel.add(graphComboBoxLeft);
		leftPanel.add(graphLeft);
		rightPanel.add(graphComboBoxRight);
		rightPanel.add(graphRight);
		
		add(leftPanel);
		add(rightPanel);
		
		setPreferredSize(new Dimension(640, 480));
	}
	
	public void clear(){
		for(XYSeries x : time_ElevatorPositionSeries){
			x.clear();
		}
		for(XYSeries x : time_CumulativeDistanceSeries){
			x.clear();
		}
		for(XYSeries x : time_CumulativeServiceTimeSeries){
			x.clear();
		}
		for(XYSeries x : time_PassengersWaitingSeries){
			x.clear();
		}
	}
	
	public void paint(Graphics g){
		super.paint(g);
		
		Dimension size = this.getSize();
		image[0] = time_ElevatorPosition.createBufferedImage(size.width/2-10, size.height-40);
		image[1] = time_CumulativeDistance.createBufferedImage(size.width/2-10, size.height-40);
		image[2] = time_CumulativeServiceTime.createBufferedImage(size.width/2-10, size.height-40);
		image[3] = time_PassengersWaiting.createBufferedImage(size.width/2-10, size.height-40);
		
		graphLeft.setIcon(new ImageIcon(image[graphLeftIndex]));
		graphRight.setIcon(new ImageIcon(image[graphRightIndex]));
	}
	
	protected void statusUpdate(ArrayList<double[]> elevatorPosition, ArrayList<double[]> cumulativeDistance, ArrayList<long[]> cumulativeServiceTime, ArrayList<int[]> passengersWaiting){
		this.clear();	
		
		for(int i=0; i<elevatorPosition.size(); i++){
			for(int j=0; j<elevatorPosition.get(i).length; j++){
				time_ElevatorPositionSeries[i].add(j, elevatorPosition.get(i)[j]);
			}
		}
		for(int i=0; i<cumulativeDistance.size(); i++){
			for(int j=0; j<cumulativeDistance.get(i).length; j++){
				time_CumulativeDistanceSeries[i].add(j, cumulativeDistance.get(i)[j]);
			}
		}
		for(int i=0; i<cumulativeServiceTime.size(); i++){
			for(int j=0; j<cumulativeServiceTime.get(i).length; j++){
				time_CumulativeServiceTimeSeries[i].add(j, cumulativeServiceTime.get(i)[j]);
			}
		}
		for(int i=0; i<passengersWaiting.size(); i++){
			for(int j=0; j<passengersWaiting.get(i).length; j++){
				time_PassengersWaitingSeries[i].add(j, passengersWaiting.get(i)[j]);
			}
		}
		
		this.repaint();
	}

	public void actionPerformed(ActionEvent e) {
		JComboBox temp = (JComboBox)e.getSource();
		int index = (int)temp.getSelectedIndex();
		if(temp.equals(graphComboBoxLeft)){
			graphLeftIndex = index;
		}else if (temp.equals(graphComboBoxRight)){
			graphRightIndex = index;
		}
	}
}
