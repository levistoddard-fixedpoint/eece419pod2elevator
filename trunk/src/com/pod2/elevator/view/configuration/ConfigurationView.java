package com.pod2.elevator.view.configuration;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.pod2.elevator.view.layout.VerticalLayout;

public class ConfigurationView extends JPanel implements ActionListener{
	private JButton update;
	private JLabel oldPort;
	private JFormattedTextField newPort;
	
	public ConfigurationView(){
		int port=0;
		//port = getWebInterfacePort();
		update = new JButton("Update");
		update.addActionListener(this);
		oldPort = new JLabel("Current Port: " + port);
		newPort = new JFormattedTextField(NumberFormat.getInstance());
		newPort.setHorizontalAlignment(JTextField.CENTER);
		newPort.setText("8080");
		
		this.setLayout(new VerticalLayout());
		
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(oldPort);
		this.add(newPort);
		this.add(update);
	}

	public void actionPerformed(ActionEvent e) {
		if(update.equals(e.getSource())){
			try{
				int port = Integer.parseInt(newPort.getText());
				//restartWebServer(port);
				//port = getWebInterfacePort();
			}catch(NumberFormatException n){
				n.printStackTrace();
			}
		}
	}
}
