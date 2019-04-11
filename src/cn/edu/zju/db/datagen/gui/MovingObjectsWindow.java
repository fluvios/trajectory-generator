package cn.edu.zju.db.datagen.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

public class MovingObjectsWindow {

	private JFrame frmAddMovingObject;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MovingObjectsWindow window = new MovingObjectsWindow();
					window.frmAddMovingObject.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MovingObjectsWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAddMovingObject = new JFrame();
		frmAddMovingObject.setTitle("Add Moving Object");
		frmAddMovingObject.setBounds(100, 100, 595, 554);
		frmAddMovingObject.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmAddMovingObject.getContentPane().setLayout(null);
		
		JLabel lblGender = new JLabel("Gender:");
		lblGender.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblGender.setBounds(12, 10, 95, 23);
		frmAddMovingObject.getContentPane().add(lblGender);
		
		JRadioButton rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setBounds(137, 11, 99, 23);
		frmAddMovingObject.getContentPane().add(rdbtnMale);
		
		JRadioButton rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setBounds(240, 11, 87, 23);
		frmAddMovingObject.getContentPane().add(rdbtnFemale);
		
		JLabel lblAge = new JLabel("Job:");
		lblAge.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblAge.setBounds(12, 78, 95, 23);
		frmAddMovingObject.getContentPane().add(lblAge);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(137, 79, 190, 23);
		frmAddMovingObject.getContentPane().add(comboBox);
		
		JLabel lblAge_1 = new JLabel("Age:");
		lblAge_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblAge_1.setBounds(12, 43, 95, 23);
		frmAddMovingObject.getContentPane().add(lblAge_1);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(137, 40, 190, 21);
		frmAddMovingObject.getContentPane().add(textField);
		
		JLabel lblMovement = new JLabel("Movement");
		lblMovement.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblMovement.setBounds(12, 111, 113, 23);
		frmAddMovingObject.getContentPane().add(lblMovement);
		
		JLabel lblType = new JLabel("Type:");
		lblType.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblType.setBounds(12, 132, 113, 23);
		frmAddMovingObject.getContentPane().add(lblType);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(137, 125, 190, 21);
		frmAddMovingObject.getContentPane().add(comboBox_1);
		
		JLabel lblMovingObject = new JLabel("Moving Object");
		lblMovingObject.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblMovingObject.setBounds(12, 165, 113, 23);
		frmAddMovingObject.getContentPane().add(lblMovingObject);
		
		JLabel label_1 = new JLabel("Type:");
		label_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_1.setBounds(12, 186, 113, 23);
		frmAddMovingObject.getContentPane().add(label_1);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setBounds(137, 179, 190, 21);
		frmAddMovingObject.getContentPane().add(comboBox_2);
		
		JLabel lblIntial = new JLabel("Intial");
		lblIntial.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblIntial.setBounds(12, 219, 113, 23);
		frmAddMovingObject.getContentPane().add(lblIntial);
		
		JLabel lblDistribution = new JLabel("Distribution:");
		lblDistribution.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblDistribution.setBounds(12, 240, 113, 23);
		frmAddMovingObject.getContentPane().add(lblDistribution);
		
		JComboBox comboBox_3 = new JComboBox();
		comboBox_3.setBounds(137, 233, 190, 21);
		frmAddMovingObject.getContentPane().add(comboBox_3);
		
		JLabel lblMachine = new JLabel("Moving Object");
		lblMachine.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblMachine.setBounds(12, 273, 113, 23);
		frmAddMovingObject.getContentPane().add(lblMachine);
		
		JLabel lblNumber = new JLabel("Number:");
		lblNumber.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblNumber.setBounds(12, 294, 113, 23);
		frmAddMovingObject.getContentPane().add(lblNumber);
		
		textField_1 = new JTextField();
		textField_1.setToolTipText("Maximum number for each room");
		textField_1.setColumns(10);
		textField_1.setBounds(137, 287, 190, 21);
		frmAddMovingObject.getContentPane().add(textField_1);
		
		JLabel lblMaximum = new JLabel("Maximum");
		lblMaximum.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblMaximum.setBounds(12, 327, 113, 23);
		frmAddMovingObject.getContentPane().add(lblMaximum);
		
		JLabel lblLifeSpan = new JLabel("Life Span:");
		lblLifeSpan.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblLifeSpan.setBounds(12, 348, 113, 23);
		frmAddMovingObject.getContentPane().add(lblLifeSpan);
		
		textField_2 = new JTextField();
		textField_2.setToolTipText("Maximum number for each room");
		textField_2.setColumns(10);
		textField_2.setBounds(137, 341, 190, 21);
		frmAddMovingObject.getContentPane().add(textField_2);
		
		JLabel label = new JLabel("Maximum");
		label.setFont(new Font("Dialog", Font.PLAIN, 14));
		label.setBounds(12, 381, 113, 23);
		frmAddMovingObject.getContentPane().add(label);
		
		JLabel lblSpeed = new JLabel("Speed:");
		lblSpeed.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblSpeed.setBounds(12, 402, 113, 23);
		frmAddMovingObject.getContentPane().add(lblSpeed);
		
		textField_3 = new JTextField();
		textField_3.setToolTipText("Maximum step length");
		textField_3.setColumns(10);
		textField_3.setBounds(137, 395, 87, 21);
		frmAddMovingObject.getContentPane().add(textField_3);
		
		textField_4 = new JTextField();
		textField_4.setToolTipText("Movement rate");
		textField_4.setColumns(10);
		textField_4.setBounds(240, 395, 87, 21);
		frmAddMovingObject.getContentPane().add(textField_4);
		
		JLabel lblGeneration = new JLabel("Generation");
		lblGeneration.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblGeneration.setBounds(12, 435, 113, 23);
		frmAddMovingObject.getContentPane().add(lblGeneration);
		
		JLabel lblPeriod = new JLabel("Period:");
		lblPeriod.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblPeriod.setBounds(12, 456, 113, 23);
		frmAddMovingObject.getContentPane().add(lblPeriod);
		
		textField_5 = new JTextField();
		textField_5.setToolTipText("Start date");
		textField_5.setColumns(10);
		textField_5.setBounds(137, 449, 87, 21);
		frmAddMovingObject.getContentPane().add(textField_5);
		
		textField_6 = new JTextField();
		textField_6.setToolTipText("End date");
		textField_6.setColumns(10);
		textField_6.setBounds(240, 449, 87, 21);
		frmAddMovingObject.getContentPane().add(textField_6);
		
		JButton button = new JButton("Create");
		button.setBounds(470, 481, 97, 23);
		frmAddMovingObject.getContentPane().add(button);
		
		JLabel lblRoomPreferences = new JLabel("Room Preferences:");
		lblRoomPreferences.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblRoomPreferences.setBounds(335, 10, 128, 23);
		frmAddMovingObject.getContentPane().add(lblRoomPreferences);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(335, 43, 232, 427);
		frmAddMovingObject.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setRowSelectionAllowed(false);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Room Type", "Probability", "Action"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setResizable(false);
		table.setCellSelectionEnabled(true);
		scrollPane.setViewportView(table);
	}
}
