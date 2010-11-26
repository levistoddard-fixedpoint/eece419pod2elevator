package com.pod2.elevator.view.configuration;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.pod2.elevator.main.CentralController;
import com.pod2.elevator.view.layout.VerticalLayout;
import com.pod2.elevator.web.ControlServerException;

public class ConfigurationView extends JPanel implements ActionListener{
	private JButton update;
	private JLabel oldPort;
	private JFormattedTextField newPort;
	private CentralController centralController;
	private int port = 8080;
	
	public ConfigurationView(){
		//port = getWebInterfacePort();
		update = new JButton("Update");
		update.addActionListener(this);
		oldPort = new JLabel("Current Port: " + port);
		newPort = new JFormattedTextField(NumberFormat.getIntegerInstance());
		newPort.setHorizontalAlignment(JTextField.CENTER);
		newPort.setText("8080");
		newPort.setColumns(6);
		
		this.setLayout(new VerticalLayout());
		
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(oldPort);
		this.add(newPort);
		this.add(update);
	}
	
	public void setCentralController(CentralController centralController) {
		this.centralController = centralController;
	}

	public void actionPerformed(ActionEvent e) {
		if(update.equals(e.getSource())){
			try{
				long p = (Long) NumberFormat.getIntegerInstance().parse(newPort.getText());
				if(p > 65535 || p < 1024){
					JOptionPane.showMessageDialog(this, "Port should be between 1024 to 65535.  Please enter a new port number.");
				}else {
					port = (int) p;
					try {
						centralController.restartWebServer(port);
						oldPort.setText("Current Port: " + port);
					} catch (ControlServerException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}catch(NumberFormatException n){
				n.printStackTrace();
			} catch (ParseException n) {
				// TODO Auto-generated catch block
				n.printStackTrace();
			}
		}
	}
}