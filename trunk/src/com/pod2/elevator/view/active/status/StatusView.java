package com.pod2.elevator.view.active.status;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.pod2.elevator.core.ActiveSimulation;
import com.pod2.elevator.core.MotionStatus;
import com.pod2.elevator.core.ServiceStatus;
import com.pod2.elevator.main.CentralController;
import com.pod2.elevator.view.active.status.elevator.ElevatorStatusPanel;
import com.pod2.elevator.view.active.status.floor.FloorStatusPanel;
import com.pod2.elevator.view.layout.VerticalLayout;

public class StatusView extends JPanel implements ActionListener {
	private ElevatorStatusPanel elevatorStatus[];
	private FloorStatusPanel floorStatus;
	private JScrollPane scrollPane;
	private JPanel rootPanel;

	private JButton floorStatusButton;
	private JLabel quantumLabel;
	private JLabel schedulerLabel;

	private CentralController centralController;

	public StatusView(int numFloors, int numElevators,
			CentralController centralController) {
		
		this.centralController = centralController;
		
		elevatorStatus = new ElevatorStatusPanel[numElevators];
		floorStatusButton = new JButton("Floor Status");
		floorStatusButton.addActionListener(this);
		quantumLabel = new JLabel("Quantum: 0");
		schedulerLabel = new JLabel();

		// Root Panel
		rootPanel = new JPanel();

		// ScrollPanel
		scrollPane = new JScrollPane(rootPanel);
		scrollPane.setPreferredSize(new Dimension(200, 720));
		scrollPane.setAutoscrolls(false);

		// Elevator Status Panel
		for (int i = 0; i < numElevators; i++) {
			elevatorStatus[i] = new ElevatorStatusPanel(i);
			elevatorStatus[i].setVisible(false);
			rootPanel.add(elevatorStatus[i]);
		}

		// Floor Status Panel
		floorStatus = new FloorStatusPanel(numFloors);
		floorStatus.setVisible(true);
		rootPanel.add(floorStatus);

		// Add components
		this.add(schedulerLabel);
		this.add(quantumLabel);
		this.add(floorStatusButton);
		this.add(scrollPane);

		this.setLayout(new VerticalLayout());
	}

	public void showElevatorStatus(int id) {
		floorStatus.setVisible(false);
		for (ElevatorStatusPanel s : elevatorStatus) {
			s.setVisible(false);
		}
		elevatorStatus[id].setVisible(true);
	}

	private void showFloorStatus() {
		for (ElevatorStatusPanel s : elevatorStatus) {
			s.setVisible(false);
		}
		floorStatus.setVisible(true);
	}

	public void paint(Graphics g) {
		Dimension size = this.getSize();
		size.height -= 50;
		scrollPane.setPreferredSize(size);
		super.paint(g);
	}

	public void statusUpdate(int eid, double position,
			Set<Integer> floorsOffLimit, int numberRequests,
			int requestCapacity, MotionStatus motionStatus,
			ServiceStatus serviceStatus,
			TreeMap<String, Boolean> componentFailure, int fid, long quantum,
			int passengersWaiting, boolean isUpSelected,
			boolean isDownSelected, long upSelectedQuantum,
			long downSelectedQuantum) {
		elevatorStatus[eid].statusUpdate(position, floorsOffLimit,
				numberRequests, requestCapacity, motionStatus, serviceStatus,
				componentFailure);
		floorStatus.statusUpdate(fid, quantum, passengersWaiting, isUpSelected,
				isDownSelected, upSelectedQuantum, downSelectedQuantum);
		quantumLabel.setText("Quantum: " + Long.toString(quantum));
		schedulerLabel.setText(centralController.getSimulation().getScheduler()
				.getName());
	}

	public void actionPerformed(ActionEvent e) {
		showFloorStatus();
	}

}
