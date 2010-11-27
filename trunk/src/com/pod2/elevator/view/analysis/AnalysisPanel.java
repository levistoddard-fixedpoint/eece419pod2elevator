package com.pod2.elevator.view.analysis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.pod2.elevator.data.LoggedEvent;
import com.pod2.elevator.view.model.TextArea;

public class AnalysisPanel extends JPanel {
	private TextArea log;
	private AnalysisChartPanel analysisChartPanel;
	private AnalysisStatusPanel analysisStatusPanel;

	public AnalysisPanel(int numFloors, int numElevators) {
		analysisChartPanel = new AnalysisChartPanel(numFloors, numElevators);
		analysisStatusPanel = new AnalysisStatusPanel();
		log = new TextArea();

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

	protected void statusUpdate(Collection<LoggedEvent> eventLog,
			ArrayList<double[]> elevatorPosition,
			ArrayList<double[]> cumulativeDistance,
			ArrayList<long[]> cumulativeServiceTime,
			ArrayList<int[]> passengersWaiting, String scheduler,
			long startQuantum, long stopQuantum, int numberPassengersDelivered,
			int rescued, double meanTimeToFailure, double meanWaitTime) {
		for (LoggedEvent e : eventLog) {
			log.append(e.getMessage() + "\n");
		}
		analysisChartPanel.statusUpdate(elevatorPosition, cumulativeDistance,
				cumulativeServiceTime, passengersWaiting);
		analysisStatusPanel.statusUpdate(scheduler, startQuantum, stopQuantum,
				numberPassengersDelivered, rescued, meanTimeToFailure,
				meanWaitTime);
	}

}
