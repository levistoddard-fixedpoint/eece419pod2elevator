package com.pod2.elevator.view.analysis;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import com.pod2.elevator.data.SimulationDataRepository;
import com.pod2.elevator.data.SimulationDetail;
import com.pod2.elevator.data.SimulationResults;
import com.pod2.elevator.data.SimulationTemplate;
import com.pod2.elevator.data.SimulationTemplateRepository;
import com.pod2.elevator.view.layout.VerticalLayout;

public class AnalysisView extends JPanel implements ActionListener{
	private JTabbedPane tabPane;
	private AnalysisPanel analysisPanel;
	
	private JPanel choosePanel;
	private JButton refresh;
	private ArrayList<SimulationDetail> simulationList;
	private SimulationResults simulationResults;
	private SimulationTemplate simulationTemplate;
	private JComboBox simulationComboBox;
	
	public AnalysisView(){
		//Initialize Variables
		refresh = new JButton("Refresh");
		refresh.addActionListener(this);
		
		getSimulationList();
		
		//Add choice panel
		choosePanel = new JPanel();
		choosePanel.setLayout(new VerticalLayout());
		choosePanel.add(Box.createRigidArea(new Dimension(0,5)));
		choosePanel.add(refresh);
		choosePanel.add(simulationComboBox);
		
		//Add tabs
		tabPane = new JTabbedPane();
		tabPane.addTab("Choose Simulation", choosePanel);
		add(tabPane);
		
	}
	
	public void getSimulationList(){
		try {
			simulationList = (ArrayList<SimulationDetail>) SimulationDataRepository.getCompletedSimulations();
			String [] temp = new String[simulationList.size()];
			for(int i=0; i<simulationList.size(); i++){
				temp[i] = simulationList.get(i).getName();
			}
			simulationComboBox = new JComboBox(temp);
		} catch (SQLException s) {
			if(simulationComboBox == null){
				simulationComboBox = new JComboBox();
			}
			s.printStackTrace();
		}
	}
	
	public void paint(Graphics g){
		super.paint(g);
		Dimension size = this.getSize();
		choosePanel.setPreferredSize(size);
		choosePanel.revalidate();
		if(analysisPanel != null){
			analysisPanel.setPreferredSize(size);
			analysisPanel.revalidate();
		}
	}
	
	public void statusUpdate(){
		ArrayList<double[]> elevatorPosition = new ArrayList<double[]>();
		double[] position = {0};
		ArrayList<double[]> cumulativeDistance = new ArrayList<double[]>();
		double[] distance = {0};
		ArrayList<long[]> cumulativeServiceTime = new ArrayList<long[]>();
		long[] service = {0};
		ArrayList<int[]> passengersWaiting = new ArrayList<int[]>();
		int[] wait = {0};
		int numberPassengersDelivered = 0;
		double meanTimeToFailure = 0;
		double meanWaitTime = 0;
		
		//i = time
		//j = id
		for(int i=0; i<simulationResults.getElevatorStates().size(); i++){
			for(int j=0; j<simulationResults.getElevatorStates().get(i).length; j++){
				position = new double[simulationResults.getElevatorStates().get(i).length];
				position[j] = simulationResults.getElevatorStates().get(i)[j].getPosition();
			}
			elevatorPosition.add(i, position);
		}
		
		for(int i=0; i<simulationResults.getElevatorStates().size(); i++){
			for(int j=0; j<simulationResults.getElevatorStates().get(i).length; j++){
				distance = new double[simulationResults.getElevatorStates().get(i).length];
				distance[j] = simulationResults.getElevatorStates().get(i)[j].getCumulativeDistanceTravelled();
			}
			cumulativeDistance.add(i, distance);
		}
		
		for(int i=0; i<simulationResults.getElevatorStates().size(); i++){
			for(int j=0; j<simulationResults.getElevatorStates().get(i).length; j++){
				service = new long[simulationResults.getElevatorStates().get(i).length];
				service[j] = simulationResults.getElevatorStates().get(i)[j].getCumulativeServiceTime();
			}
			cumulativeServiceTime.add(i, service);
		}
		
		for(int i=0; i<simulationResults.getPassengersWaiting().size(); i++){
			for(int j=0; j<simulationResults.getPassengersWaiting().get(i).length; j++){
				wait = new int[simulationResults.getPassengersWaiting().get(i).length];
				wait[j] = simulationResults.getPassengersWaiting().get(i)[j];
			}
			passengersWaiting.add(i, wait);
		}
		
		numberPassengersDelivered = simulationResults.getNumberPassengersDelivered();
		meanTimeToFailure = simulationResults.getMeanTimeToFailure();
		meanWaitTime = simulationResults.getMeanWaitTime();
		
		analysisPanel.statusUpdate(elevatorPosition, cumulativeDistance, cumulativeServiceTime, passengersWaiting, numberPassengersDelivered, meanTimeToFailure, meanWaitTime);
	}

	public void actionPerformed(ActionEvent e) {
		if(refresh.equals(e.getSource())){
			getSimulationList();
		}
		if(simulationComboBox.equals(e.getSource())){
			JComboBox temp = (JComboBox)e.getSource();
			int index = (int)temp.getSelectedIndex();
			int Uuid = simulationList.get(index).getUuid();
			try {
				simulationResults = SimulationDataRepository.getSimulationResults(Uuid);
				simulationTemplate = SimulationTemplateRepository.getTemplate(simulationResults.getTemplateId());
				analysisPanel = new AnalysisPanel(simulationTemplate.getNumberFloors(), simulationTemplate.getNumberElevators());
				tabPane.add("Simulation Analysis", analysisPanel);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	
}
