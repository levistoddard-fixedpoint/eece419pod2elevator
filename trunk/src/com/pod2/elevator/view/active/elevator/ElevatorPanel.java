package com.pod2.elevator.view.active.elevator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.pod2.elevator.core.MotionStatus;
import com.pod2.elevator.core.ServiceStatus;
import com.pod2.elevator.view.active.status.StatusView;
import com.pod2.elevator.view.layout.VerticalLayout;

public class ElevatorPanel extends JPanel implements ActionListener {
	private JScrollPane scrollPane;
	private JPanel rootPanel;

	private JPanel floorPanel;
	private JLabel floors[];

	private Elevator elevator;
	private JButton status;

	private int y;
	private int id;
	private int numFloors;
	private double position;
	private ServiceStatus serviceStatus;

	private StatusView statusView;

	public ElevatorPanel(int id, int numFloors, StatusView statusView) {
		// Init variables
		this.id = id;
		this.numFloors = numFloors;
		this.statusView = statusView;

		// Display elevator number
		status = new JButton();
		status.setText("Elevator: " + Integer.toString(id));
		status.addActionListener(this);

		// Display elevator
		elevator = new Elevator(numFloors);
		elevator.setPreferredSize(new Dimension(100, 0));
		elevator.setBackground(Color.BLACK);

		// Display floor number
		floorPanel = new JPanel();
		floorPanel.setLayout(new BoxLayout(floorPanel, BoxLayout.Y_AXIS));

		// Root panel
		rootPanel = new JPanel();
		rootPanel.setLayout(new BorderLayout());
		rootPanel.setBackground(Color.BLUE);
		rootPanel.add(floorPanel, BorderLayout.EAST);
		rootPanel.add(elevator, BorderLayout.WEST);
		rootPanel.revalidate();

		// Scrollpane
		scrollPane = new JScrollPane(rootPanel);
		scrollPane.setPreferredSize(new Dimension(152, 40 * 5));

		// Labels for floor numbers
		floors = new JLabel[numFloors];
		for (int i = numFloors - 1; i >= 0; i--) {
			floors[i] = new JLabel(Integer.toString(i));
			floors[i].setFont(new Font("Dialog", Font.PLAIN, 30));
			floorPanel.add(floors[i]);
		}

		// This Panel
		this.setBackground(Color.LIGHT_GRAY);
		this.setLayout(new VerticalLayout());
		this.add(status);
		this.add(scrollPane);
	}

	public void paint(Graphics g) {
		rootPanel.revalidate();
		super.paint(g);

		// Set view
		if (serviceStatus != ServiceStatus.Failed) {
			y = (int) (39 * (numFloors - position - 1));
		}
		scrollPane.getVerticalScrollBar().setValue(y);

		// Repaint elevator
		elevator.repaint();
	}

	protected void statusUpdate(double position, Set<Integer> floorsOffLimit,
			MotionStatus motionStatus, ServiceStatus serviceStatus) {
		this.position = position;
		this.serviceStatus = serviceStatus;
		// Update Elevator
		elevator.statusUpdate(position, floorsOffLimit, motionStatus,
				serviceStatus);
		this.repaint();
	}

	public void actionPerformed(ActionEvent e) {
		statusView.showElevatorStatus(id);
	}

}
