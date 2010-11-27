package com.pod2.elevator.view.analysis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.pod2.elevator.view.layout.VerticalLayout;

public class AnalysisStatusPanel extends JPanel {

	private JLabel schedulerLabel;
	private JLabel schedulerDisplay;

	private JLabel startLabel;
	private JLabel startDisplay;

	private JLabel stopLabel;
	private JLabel stopDisplay;

	private JLabel numberPassengersDeliveredLabel;
	private JLabel numberPassengersDeliveredDisplay;

	private JLabel meanTimeToFailureLabel;
	private JLabel meanTimeToFailureDisplay;

	private JLabel meanWaitTimeLabel;
	private JLabel meanWaitTimeDisplay;

	public AnalysisStatusPanel() {
		this.setLayout(new VerticalLayout());
		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(),
				BorderFactory.createLoweredBevelBorder()));
		this.setBackground(Color.LIGHT_GRAY);

		schedulerLabel = createLabel(0, "Simulation");
		startLabel = createLabel(0, "Start Quantum");
		stopLabel = createLabel(0, "Stop Quantum");
		numberPassengersDeliveredLabel = createLabel(0, "Passengers Delivered");
		meanTimeToFailureLabel = createLabel(0, "Mean Time to Failure");
		meanWaitTimeLabel = createLabel(0, "Average Wait Time");

		schedulerDisplay = createLabel(1, "");
		startDisplay = createLabel(1, "");
		stopDisplay = createLabel(1, "");
		numberPassengersDeliveredDisplay = createLabel(1, "");
		meanTimeToFailureDisplay = createLabel(1, "");
		meanWaitTimeDisplay = createLabel(1, "");

		this.add(Box.createRigidArea(new Dimension(0, 5)));

		this.add(schedulerLabel);
		this.add(schedulerDisplay);

		this.add(startLabel);
		this.add(startDisplay);

		this.add(stopLabel);
		this.add(stopDisplay);

		this.add(numberPassengersDeliveredLabel);
		this.add(numberPassengersDeliveredDisplay);

		this.add(meanTimeToFailureLabel);
		this.add(meanTimeToFailureDisplay);

		this.add(meanWaitTimeLabel);
		this.add(meanWaitTimeDisplay);
	}

	private JLabel createLabel(int type, String s) {
		JLabel temp = new JLabel(s);
		if (type == 0) {
			temp.setBorder(BorderFactory.createRaisedBevelBorder());
			temp.setBackground(Color.WHITE);
			temp.setFont(new Font("Dialog", Font.BOLD, 14));
		} else {
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

	protected void statusUpdate(String scheduler, long startQuantum,
			long stopQuantum, int numberPassengersDelivered,
			double meanTimeToFailure, double meanWaitTime) {
		schedulerDisplay.setText(scheduler);
		startDisplay.setText(Long.toString(startQuantum));
		stopDisplay.setText(Long.toString(stopQuantum));
		numberPassengersDeliveredDisplay.setText(Integer
				.toString(numberPassengersDelivered));
		if(meanTimeToFailure >= 0){
			meanTimeToFailureDisplay.setText(Double.toString(meanTimeToFailure));
		}else {
			meanTimeToFailureDisplay.setText("No Failures");
		}
		if(meanWaitTime >= 0){
			meanWaitTimeDisplay.setText(Double.toString(meanWaitTime));
		}else {
			meanWaitTimeDisplay.setText("No Passengers");
		}
	}
}
