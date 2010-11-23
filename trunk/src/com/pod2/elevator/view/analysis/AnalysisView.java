package com.pod2.elevator.view.analysis;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.pod2.elevator.core.ServiceStatus;
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
	private LinkedList<SimulationDetail> simulationList;
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
			simulationList = (LinkedList<SimulationDetail>) SimulationDataRepository.getCompletedSimulations();
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
	
	public void statusUpdate(int numFloors, int numElevators){
		//Time vs Elevator Position
		ArrayList<double[]> elevatorPosition = new ArrayList<double[]>();
		double[] position = {0};
		
		//Time vs Cumulative Distance
		ArrayList<double[]> cumulativeDistance = new ArrayList<double[]>();
		double[] distance = new double[numElevators];
		double deltaDistance = 0;
		double prevDistance = 0;
		
		//Time vs Service Time
		ArrayList<long[]> cumulativeServiceTime = new ArrayList<long[]>();
		long[] service = new long[numElevators];
		
		//Time vs Passengers Waiting
		ArrayList<int[]> passengersWaiting = new ArrayList<int[]>();
		int[] wait = {0};
		
		//Mean Time to Failure
		double meanTimeToFailure = 0;
		int numberFailures = 0;
		
		//Total Passengers Delivered
		int numberPassengersDelivered = 0;
		
		//Mean Wait Time
		double meanWaitTime = 0;
		long totalQuantum = simulationResults.getElevatorStates().size();
		
		//i = time
		//j = id
		for(int i=0; i<simulationResults.getElevatorStates().size(); i++){
			//Initialize arrays
			position = new double[simulationResults.getElevatorStates().get(i).length];
			
			for(int j=0; j<simulationResults.getElevatorStates().get(i).length; j++){
				//Get elevator position
				position[j] = simulationResults.getElevatorStates().get(i)[j].getPosition();
				
				//Get cumulative distance
				deltaDistance = position[j] - prevDistance;
				prevDistance = position[j];
				distance[j] += deltaDistance;
				
				//Get service time
				if(simulationResults.getElevatorStates().get(i)[j].getStatus() == ServiceStatus.InService){
					service[j]++;
				}else {
					numberFailures++;
				}
				
			}
			elevatorPosition.add(i, position);
			cumulativeDistance.add(i, distance);
			cumulativeServiceTime.add(i, service);
		}
		
		for(int i=0; i<simulationResults.getPassengersWaiting().size(); i++){
			//Initialize arrays
			wait = new int[simulationResults.getPassengersWaiting().get(i).length];
			
			for(int j=0; j<simulationResults.getPassengersWaiting().get(i).length; j++){
				//Get passengers waiting
				wait[j] = simulationResults.getPassengersWaiting().get(i)[j];
			}
			passengersWaiting.add(i, wait);
		}
		
		for(int i=0; i<simulationResults.getPassengerDeliveries().size(); i++){
			//Calculate mean wait time
			meanWaitTime += simulationResults.getPassengerDeliveries().get(i).getOnloadQuantum() - simulationResults.getPassengerDeliveries().get(i).getEnterQuantum();
		}
		meanWaitTime /= simulationResults.getPassengerDeliveries().size();
		
		//Calculate mean time to failure
		for(int i=0; i<service.length; i++){
			meanTimeToFailure += service[i];
		}
		meanTimeToFailure /= numberFailures;
		
		//Calculate total passengers delivered
		numberPassengersDelivered = simulationResults.getPassengerDeliveries().size();
		
		analysisPanel.statusUpdate(elevatorPosition, cumulativeDistance, cumulativeServiceTime, passengersWaiting, numberPassengersDelivered, meanTimeToFailure, meanWaitTime);
	}

	public void actionPerformed(ActionEvent e) {
		if(refresh.equals(e.getSource())){
			getSimulationList();
		}
		if(simulationComboBox.equals(e.getSource())){
			JComboBox temp = (JComboBox)e.getSource();
			int index = (int)temp.getSelectedIndex();
			int Uuid = simulationList.get(index).getId();
			try {
				simulationResults = SimulationDataRepository.getSimulationResults(Uuid);
				simulationTemplate = SimulationTemplateRepository.getTemplate(simulationResults.getTemplateId());
				analysisPanel = new AnalysisPanel(simulationTemplate.getNumberFloors(), simulationTemplate.getNumberElevators());
				this.statusUpdate(simulationTemplate.getNumberFloors(), simulationTemplate.getNumberElevators());
				tabPane.add("Simulation Analysis", analysisPanel);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	
}