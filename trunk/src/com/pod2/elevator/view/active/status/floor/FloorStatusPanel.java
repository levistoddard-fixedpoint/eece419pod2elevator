package com.pod2.elevator.view.active.status.floor;

import javax.swing.JPanel;

import com.pod2.elevator.view.layout.VerticalLayout;

public class FloorStatusPanel extends JPanel {
	private FloorStatus floorStatus[];

	public FloorStatusPanel(int numFloors) {
		floorStatus = new FloorStatus[numFloors];

		for (int i = 0; i < numFloors; i++) {
			floorStatus[i] = new FloorStatus(i);
			this.add(floorStatus[i]);
		}

		this.setLayout(new VerticalLayout());
	}

	public void statusUpdate(int fid, long quantum, int passengersWaiting,
			boolean isUpSelected, boolean isDownSelected,
			long upSelectedQuantum, long downSelectedQuantum) {
		floorStatus[fid].statusUpdate(quantum, passengersWaiting, isUpSelected,
				isDownSelected, upSelectedQuantum, downSelectedQuantum);
	}
}
