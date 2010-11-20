package com.pod2.elevator.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.pod2.elevator.view.layout.VerticalLayout;

public class ConfigurationView extends JPanel implements KeyListener, ActionListener{
	private JButton update;
	private JLabel oldPort;
	private JTextField newPort;
	
	public ConfigurationView(){
		int port=0;
		//port = getWebInterfacePort();
		update = new JButton("Update");
		update.addActionListener(this);
		oldPort = new JLabel("Current Port: " + port);
		newPort = new JTextField("Enter Port Number");
		newPort.addKeyListener(this);
		newPort.setHorizontalAlignment(JTextField.CENTER);
		
		this.setLayout(new VerticalLayout());
		
		this.add(Box.createRigidArea(new Dimension(0,5)));
		this.add(oldPort);
		this.add(newPort);
		this.add(update);
	}

	public void keyTyped(KeyEvent e) {
		if(newPort.equals(e.getSource())){
			if(newPort.getText().equalsIgnoreCase(new String("Enter Port Number"))){
				newPort.setText("");
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		if(newPort.equals(e.getSource())){
			if(newPort.getText().equalsIgnoreCase(new String("Enter Port Number"))){
				newPort.setText("");
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		if(newPort.equals(e.getSource())){
			if(newPort.getText().equalsIgnoreCase(new String("Enter Port Number"))){
				newPort.setText("");
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(update.equals(e.getSource())){
			try{
				int port = Integer.parseInt(newPort.getText());
				//restartWebServer(port);
			}catch(NumberFormatException n){
				newPort.setText("Enter Port Number");
				n.printStackTrace();
			}
		}
	}
}
