package com.pod2.elevator.view.analysis;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.pod2.elevator.core.ServiceStatus;
import com.pod2.elevator.data.LoggedEvent;
import com.pod2.elevator.data.SimulationDataRepository;
import com.pod2.elevator.data.SimulationDetail;
import com.pod2.elevator.data.SimulationResults;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.data.SimulationTemplateRepository;
import com.pod2.elevator.view.layout.VerticalLayout;

public class AnalysisView extends JPanel implements ActionListener {
	private AnalysisPanel analysisPanel;

	private LinkedList<SimulationDetail> simulationList;
	private SimulationResults simulationResults;
	private SimulationTemplate simulationTemplate;

	private JButton refreshButton;
	private JComboBox simulationComboBox;
	private JButton analyzeButton;

	public AnalysisView() {
		// Initialize Variables
		refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(this);
		refreshButton.setPreferredSize(new Dimension(150, 30));
		analyzeButton = new JButton("Analyze");
		analyzeButton.addActionListener(this);
		analyzeButton.setPreferredSize(new Dimension(150, 30));
		simulationComboBox = new JComboBox();
		DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
		dlcr.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
		simulationComboBox.setRenderer(dlcr);
		simulationComboBox.setPreferredSize(new Dimension(300, 30));

		// Add components
		setLayout(new VerticalLayout());
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(refreshButton);
		add(simulationComboBox);
		add(analyzeButton);

		getSimulationList();
	}

	public void getSimulationList() {
		simulationComboBox.removeAllItems();
		try {
			simulationList = (LinkedList<SimulationDetail>) SimulationDataRepository
					.getCompletedSimulations();
			int Uuid;
			SimulationResults tempResults;
			for (int i = 0; i < simulationList.size(); i++) {
				Uuid = simulationList.get(i).getId();
				tempResults = SimulationDataRepository
						.getSimulationResults(Uuid);
				simulationComboBox.addItem(simulationList.get(i).getName()
						+ " : " + "[" + tempResults.getStartTime() + "]");
			}
		} catch (SQLException s) {
			// JOptionPane.showMessageDialog(this,
			// "Cannnot connect to database: Press refresh to try again");
			int i = JOptionPane.showConfirmDialog(this,
					"Cannnot connect to database: Try again?", "Error",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			switch (i) {
			case 0:
				getSimulationList();
				break;
			case 1:
				System.exit(0);
				break;
			case 2:
				break;
			}
		}

	}

	public void paint(Graphics g) {
		super.paint(g);
		Dimension size = this.getSize();
		setPreferredSize(size);
		revalidate();
		if (analysisPanel != null) {
			analysisPanel.setPreferredSize(size);
			analysisPanel.revalidate();
		}
	}

	public void statusUpdate(int numFloors, int numElevators, String scheduler) {
		// Time vs Elevator Position
		ArrayList<double[]> elevatorPosition = new ArrayList<double[]>();
		double[] position;

		// Time vs Cumulative Distance
		ArrayList<double[]> cumulativeDistance = new ArrayList<double[]>();
		double[] distance;
		double deltaDistance = 0;
		double prevDistance = 0;

		// Time vs Service Time
		ArrayList<long[]> cumulativeServiceTime = new ArrayList<long[]>();
		long[] service = null;
		long prevTime = 0;
		long totalTime = 0;

		// Time vs Passengers Waiting
		ArrayList<int[]> passengersWaiting = new ArrayList<int[]>();
		int[] wait = { 0 };

		// Mean Time to Failure
		double meanTimeToFailure = 0;
		int numberFailures = 0;

		// Total Passengers Delivered
		int numberPassengersDelivered = 0;

		// Mean Wait Time
		double meanWaitTime = 0;

		// i = # elevator
		// j = time
		for (int i = 0; i < simulationResults.getElevatorStates().size(); i++) {
			// Reset variables
			position = new double[simulationResults.getElevatorStates().get(i).length];
			distance = new double[simulationResults.getElevatorStates().get(i).length];
			deltaDistance = 0;
			prevDistance = 0;
			service = new long[simulationResults.getElevatorStates().get(i).length];
			prevTime = 0;

			for (int j = 0; j < simulationResults.getElevatorStates().get(i).length; j++) {
				// Get elevator position
				position[j] = simulationResults.getElevatorStates().get(i)[j]
						.getPosition();

				// Get cumulative distance
				deltaDistance = Math.abs(position[j] - prevDistance);
				if (j > 0) {
					prevDistance = distance[j - 1];
				}
				distance[j] = prevDistance + deltaDistance;
				prevDistance = position[j];

				// Get service time
				if (j > 0) {
					prevTime = service[j - 1];
				}
				if (simulationResults.getElevatorStates().get(i)[j].getStatus() == ServiceStatus.InService) {
					service[j] = prevTime + 1;
				} else {
					service[j] = prevTime;
					numberFailures++;
				}
				// Get MTTF
				totalTime += service[j];

			}

			elevatorPosition.add(i, position);
			cumulativeDistance.add(i, distance);
			cumulativeServiceTime.add(i, service);
		}

		// i = # floor
		// j = time
		for (int i = 0; i < simulationTemplate.getNumberFloors(); i++) {
			// Initialize Array
			wait = new int[(int) simulationResults.getStopQuantum()];
			for (int j = 0; j < simulationResults.getPassengerDeliveries()
					.size(); j++) {
				// Check if there is a passenger waiting on this floor
				if (j >= simulationResults.getPassengerDeliveries().get(j)
						.getEnterQuantum()
						&& j <= simulationResults.getPassengerDeliveries()
								.get(j).getOnloadQuantum()
						&& i == simulationResults.getPassengerDeliveries()
								.get(j).getOnloadFloor()) {
					wait[i]++;
				}
			}
			passengersWaiting.add(i, wait);
		}

		for (int i = 0; i < simulationResults.getPassengerDeliveries().size(); i++) {
			// Calculate mean wait time
			meanWaitTime += (simulationResults.getPassengerDeliveries().get(i)
					.getOnloadQuantum() - simulationResults
					.getPassengerDeliveries().get(i).getEnterQuantum());
		}
		if (simulationResults.getPassengerDeliveries().size() > 0) {
			meanWaitTime /= simulationResults.getPassengerDeliveries().size();
		} else {
			meanWaitTime = -1;
		}

		// Calculate mean time to failure
		if (numberFailures > 0) {
			meanTimeToFailure = totalTime / numberFailures;
		} else {
			meanTimeToFailure = -1;
		}

		// Calculate total passengers delivered
		numberPassengersDelivered = simulationResults.getPassengerDeliveries()
				.size();

		// Simulation Name
		String simulationName = simulationResults.getName();
		// Start Quantum
		long startQuantum = simulationResults.getStartQuantum();
		// Stop Quantum
		long stopQuantum = simulationResults.getStopQuantum();

		// Event Log
		Collection<LoggedEvent> eventLog = simulationResults.getEvents();

		analysisPanel.statusUpdate(eventLog, elevatorPosition,
				cumulativeDistance, cumulativeServiceTime, passengersWaiting,
				simulationName, startQuantum, stopQuantum,
				numberPassengersDelivered, meanTimeToFailure, meanWaitTime);
	}

	public void actionPerformed(ActionEvent e) {
		if (refreshButton.equals(e.getSource())) {
			getSimulationList();
		}
		if (analyzeButton.equals(e.getSource())) {
			int index = (int) simulationComboBox.getSelectedIndex();
			int Uuid;
			if (simulationList == null) {
				JOptionPane.showMessageDialog(this,
						"No Simulation Available: Press refresh to try again");
			} else {
				try {
					Uuid = simulationList.get(index).getId();
					simulationResults = SimulationDataRepository
							.getSimulationResults(Uuid);
					simulationTemplate = SimulationTemplateRepository
							.getTemplate(simulationResults.getTemplateId());
					analysisPanel = new AnalysisPanel(
							simulationTemplate.getNumberFloors(),
							simulationTemplate.getNumberElevators());
					this.statusUpdate(simulationTemplate.getNumberFloors(),
							simulationTemplate.getNumberElevators(),
							simulationTemplate.getScheduler().getName());
					JFrame temp = new JFrame(simulationResults.getName());
					temp.add(analysisPanel);
					temp.pack();
					temp.setVisible(true);
				} catch (SQLException e1) {
					JOptionPane
							.showMessageDialog(this,
									"Cannnot connect to database: Press refresh to try again");
				} catch (IndexOutOfBoundsException e1) {
					JOptionPane.showMessageDialog(this,
							"No Simulation Selected");
				}
			}

		}
	}

}
