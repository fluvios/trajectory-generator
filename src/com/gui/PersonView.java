package com.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import com.indoorobject.movingobject.MovingObj;

public class PersonView extends JPanel {

	/**
	 * Create the panel.
	 */
	
	private MovingObj obj;
	private JLabel title;
	
	public PersonView(MovingObj obj, String title) {		
		setLayout(new BorderLayout(0, 0));
		this.obj = obj;
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.SOUTH);
		
		JButton button = new JButton("Delete");
		button.setBackground(Color.WHITE);
		button.setFont(new Font("Dialog", Font.PLAIN, 11));
		splitPane.setLeftComponent(button);
		
		JButton button_1 = new JButton("Detail");
		button_1.setBackground(Color.WHITE);
		button_1.setFont(new Font("Dialog", Font.PLAIN, 11));
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		splitPane.setRightComponent(button_1);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 11));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		if(obj.getMovementType().equals("normal")) {
			lblNewLabel.setIcon(new ImageIcon(MachineView.class.getResource("/cn/edu/zju/db/datagen/gui/normal.png")));			
		} else {
			lblNewLabel.setIcon(new ImageIcon(MachineView.class.getResource("/cn/edu/zju/db/datagen/gui/wheelchair.png")));
		}
		add(lblNewLabel, BorderLayout.CENTER);
		
		this.title = new JLabel(title);
		this.title.setFont(new Font("Dialog", Font.PLAIN, 14));
		this.title.setHorizontalAlignment(SwingConstants.CENTER);
		add(this.title, BorderLayout.NORTH);
	}

}
