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
import javax.swing.JFrame;
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
	private AnalysisPanel analysisPanel;
	
	private LinkedList<SimulationDetail> simulationList;
	private SimulationResults simulationResults;
	private SimulationTemplate simulationTemplate;
	
	private JButton refreshButton;
	private JComboBox simulationComboBox;
	private JButton analyzeButton;
	
	public AnalysisView(){
		//Initialize Variables
		refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(this);
		refreshButton.setPreferredSize(new Dimension(150, 30));
		analyzeButton = new JButton("Analyze");
		analyzeButton.addActionListener(this);
		analyzeButton.setPreferredSize(new Dimension(150, 30));
		simulationComboBox = new JComboBox();
		simulationComboBox.setPreferredSize(new Dimension(150, 30));
		
		//Add components
		setLayout(new VerticalLayout());
		add(Box.createRigidArea(new Dimension(0,5)));
		add(refreshButton);
		add(simulationComboBox);
		add(analyzeButton);
		
		getSimulationList();
	}
	
	public void getSimulationList(){
		simulationComboBox.removeAllItems();
		try {
			simulationList = (LinkedList<SimulationDetail>) SimulationDataRepository.getCompletedSimulations();
			for(int i=0; i<simulationList.size(); i++){
				simulationComboBox.addItem(simulationList.get(i).getName());
			}
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
		setPreferredSize(size);
		revalidate();
		if(analysisPanel != null){
			analysisPanel.setPreferredSize(size);
			analysisPanel.revalidate();
		}
	}
	
	public void statusUpdate(int numFloors, int numElevators, String scheduler){
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
		
		//Simulation Name
		String simulationName = simulationResults.getName();
		//Start Quantum
		long startQuantum = simulationResults.getStartQuantum();
		//Stop Quantum
		long stopQuantum = simulationResults.getStopQuantum();
		
		analysisPanel.statusUpdate(elevatorPosition, cumulativeDistance, cumulativeServiceTime, passengersWaiting, simulationName, startQuantum, stopQuantum, numberPassengersDelivered, meanTimeToFailure, meanWaitTime);
		
		/* Test Display
		ArrayList<double[]> elevatorPosition = new ArrayList<double[]>();
		ArrayList<double[]> cumulativeDistance = new ArrayList<double[]>();
		ArrayList<long[]> cumulativeServiceTime = new ArrayList<long[]>();
		ArrayList<int[]> passengersWaiting = new ArrayList<int[]>();

		double[] temp0 = new double[100];
		double[] temp1 = new double[100];
		long[] temp2 = new long[100];
		int[] temp3 = new int[100];
		for(int i=0; i<100; i++){
			temp0[i] = i;
		}
		for(int i=0; i<100; i++){
			temp1[i] = 100-i;
		}
		for(int i=0; i<100; i++){
			temp2[i] = 2*i;
		}
		for(int i=0; i<100; i++){
			temp3[i] = i/2;
		}
		elevatorPosition.add(temp0);
		elevatorPosition.add(temp1);
		
		cumulativeDistance.add(temp1);
		cumulativeDistance.add(temp0);
		
		cumulativeServiceTime.add(temp2);
		
		passengersWaiting.add(temp3);
		
		String simulationName = new String("Test Simulation");
		
		long startQuantum = 0;
		long stopQuantum = 100;
		
		int numberPassengersDelivered = 152;
		double meanTimeToFailure = 13.2;
		double meanWaitTime = 5.6;
		
		analysisPanel.statusUpdate(elevatorPosition, cumulativeDistance, cumulativeServiceTime, passengersWaiting, scheduler, startQuantum, stopQuantum, numberPassengersDelivered, meanTimeToFailure, meanWaitTime);
		*/
	}

	public void actionPerformed(ActionEvent e) {
		if(refreshButton.equals(e.getSource())){
			getSimulationList();
		}
		if(analyzeButton.equals(e.getSource())){
			/* Test Display
			analysisPanel = new AnalysisPanel(10, 10);
			this.statusUpdate(10, 10, "FCFS");
			JFrame temp = new JFrame("Test Simulation Analysis");
			temp.add(analysisPanel);
			temp.pack();
			temp.setVisible(true);
			*/
			int index = (int)simulationComboBox.getSelectedIndex();
			int Uuid = simulationList.get(index).getId();
			try {
				simulationResults = SimulationDataRepository.getSimulationResults(Uuid);
				simulationTemplate = SimulationTemplateRepository.getTemplate(simulationResults.getTemplateId());
				analysisPanel = new AnalysisPanel(simulationTemplate.getNumberFloors(), simulationTemplate.getNumberElevators());
				this.statusUpdate(simulationTemplate.getNumberFloors(), simulationTemplate.getNumberElevators(), simulationTemplate.getScheduler().getName());
				JFrame temp = new JFrame(simulationResults.getName());
				temp.add(analysisPanel);
				temp.pack();
				temp.setVisible(true);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		}
	}
	
	
}
