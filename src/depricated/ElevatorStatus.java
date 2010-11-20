package depricated;

import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import com.pod2.elevator.core.component.DoorDriveMechanism;
import com.pod2.elevator.core.component.DoorSensor;
import com.pod2.elevator.core.component.DriveMechanism;
import com.pod2.elevator.core.component.PositionSensor;
import com.pod2.elevator.view.data.ElevatorSnapShot;
import com.pod2.elevator.view.data.SystemSnapShot;

public class ElevatorStatus extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6807421655052332523L;

	private int id;
	private int numComponent;

	private JPanel thisPanel = new JPanel();

	private JPanel bottomPanel = new JPanel();

	private JPanel passengerPanel = new JPanel();
	private JLabel passengerNumber = new JLabel("10");

	private JPanel statusPanel = new JPanel();
	private JLabel motionStatus = new JLabel("Up");

	private JPanel componentPanel = new JPanel();
	private JLabel componentStatus[];

	public ElevatorStatus(int id, int numComponent) {

		setSize(200, 100);

		add(thisPanel);

		this.id = id;
		this.numComponent = numComponent;
		this.setResizable(false);

		componentStatus = new JLabel[numComponent];

		setTitle("Elevator " + id + " Status");

		thisPanel.setToolTipText("Elevator " + id);
		thisPanel
				.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		thisPanel.setLayout(new BoxLayout(thisPanel, BoxLayout.Y_AXIS));

		// add component panel
		componentPanel.setLayout(new GridLayout(0, 5, 5, 5));
		for (int i = 0; i < numComponent; i++) {
			componentStatus[i] = new JLabel("Ok");
			componentStatus[i].setBorder(BorderFactory
					.createBevelBorder(BevelBorder.RAISED));
			componentStatus[i].setHorizontalAlignment(JLabel.CENTER);
			componentStatus[i].setVerticalAlignment(JLabel.CENTER);
			componentPanel.add(componentStatus[i]);
		}
		thisPanel.add(componentPanel);

		// add bottom panel
		bottomPanel.setLayout(new GridLayout(0, 2, 5, 5));
		thisPanel.add(bottomPanel);

		// add passenger panel
		passengerPanel.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.RAISED));
		passengerPanel.add(passengerNumber);
		bottomPanel.add(passengerPanel);

		// add status panel
		statusPanel.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.RAISED));
		statusPanel.add(motionStatus);
		bottomPanel.add(statusPanel);

		// pack();
		setVisible(true);
	}

	public int getId() {
		return id;
	}

	public void statusUpdate(SystemSnapShot s) {
		/*
		// TODO: Update passenger number, motion status, component status
		ElevatorSnapShot elevator = s.getElevatorSnapShot(id);
		passengerNumber.setText(Integer.toString(elevator.getNumberRequests()));
		motionStatus.setText(elevator.getMotionStatus().toString());
		componentStatus[0].setText(elevator.getFailureStatus(DoorSensor.class)
				.toString());
		componentStatus[0].setText(elevator.getFailureStatus(
				DoorDriveMechanism.class).toString());
		componentStatus[0].setText(elevator.getFailureStatus(
				PositionSensor.class).toString());
		componentStatus[0].setText(elevator.getFailureStatus(
				DriveMechanism.class).toString());
		*/
	}
}
