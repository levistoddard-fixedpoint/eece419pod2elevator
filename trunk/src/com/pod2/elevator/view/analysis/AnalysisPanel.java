package com.pod2.elevator.view.analysis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class AnalysisPanel extends JPanel {
	private JTextArea log;
	private AnalysisChartPanel analysisChartPanel;
	private AnalysisStatusPanel analysisStatusPanel;

	public AnalysisPanel(int numFloors, int numElevators) {
		analysisChartPanel = new AnalysisChartPanel(numFloors, numElevators);
		analysisStatusPanel = new AnalysisStatusPanel();
		log = new JTextArea("Logging goes here\n");

		// Set log properties
		log.setRows(5);
		log.setEditable(false);
		log.setBackground(Color.BLACK);
		log.setForeground(Color.WHITE);
		JScrollPane logScroll = new JScrollPane(log);

		// Set layout
		this.setLayout(new BorderLayout());

		// Add components
		this.add(analysisChartPanel, BorderLayout.CENTER);
		this.add(logScroll, BorderLayout.SOUTH);
		this.add(analysisStatusPanel, BorderLayout.EAST);
	}

	protected void statusUpdate(ArrayList<double[]> elevatorPosition,
			ArrayList<double[]> cumulativeDistance,
			ArrayList<long[]> cumulativeServiceTime,
			ArrayList<int[]> passengersWaiting, String scheduler,
			long startQuantum, long stopQuantum, int numberPassengersDelivered,
			double meanTimeToFailure, double meanWaitTime) {
		analysisChartPanel.statusUpdate(elevatorPosition, cumulativeDistance,
				cumulativeServiceTime, passengersWaiting);
		analysisStatusPanel.statusUpdate(scheduler, startQuantum, stopQuantum,
				numberPassengersDelivered, meanTimeToFailure, meanWaitTime);
	}

}
