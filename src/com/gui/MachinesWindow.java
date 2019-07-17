package com.gui;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MachinesWindow {

	private JFrame frmAddMachine;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MachinesWindow window = new MachinesWindow();
					window.frmAddMachine.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MachinesWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAddMachine = new JFrame();
		frmAddMachine.setTitle("Add Machine\r\n");
		frmAddMachine.setBounds(100, 100, 355, 190);
		frmAddMachine.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmAddMachine.getContentPane().setLayout(null);
		
		JLabel lblMachine_1 = new JLabel("Machine");
		lblMachine_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblMachine_1.setBounds(12, 64, 95, 23);
		frmAddMachine.getContentPane().add(lblMachine_1);
		
		JLabel lblMachine = new JLabel("Machine");
		lblMachine.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblMachine.setBounds(12, 10, 95, 23);
		frmAddMachine.getContentPane().add(lblMachine);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblName.setBounds(12, 31, 95, 23);
		frmAddMachine.getContentPane().add(lblName);
		
		JLabel lblAddress = new JLabel("Address:");
		lblAddress.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblAddress.setBounds(12, 85, 95, 23);
		frmAddMachine.getContentPane().add(lblAddress);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(115, 78, 212, 21);
		frmAddMachine.getContentPane().add(textField);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(115, 24, 212, 21);
		frmAddMachine.getContentPane().add(textField_1);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(230, 118, 97, 23);
		frmAddMachine.getContentPane().add(btnCreate);
	}

}
