package depricated;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.pod2.elevator.view.data.SystemSnapShot;

public class ElevatorPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6753321753497119457L;

	private int id;
	private int numFloor;
	private int numComponent;
	private Elevator elevator;

	public ElevatorPanel(int id, int numFloor, int numComponent) {
		this.id = id;
		this.numFloor = numFloor;
		this.numComponent = numComponent;

		setBackground(Color.CYAN);
		setLayout(new BorderLayout());
		elevator = new Elevator(numFloor, numComponent);
		add(elevator, BorderLayout.CENTER);
	}

	public void statusUpdate(SystemSnapShot s) {
		// TODO: Update elevator image and position (guarantee elevator cannot
		// go out of bound)
		elevator.setFloor(s.getElevatorSnapShot(id).getCurrentPosition());
		//Dimension size = this.getSize();
		//System.out.println("panel: " + size.height);
	}

}