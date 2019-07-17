package com.gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TrajectoryView extends JPanel {

	/**
	 * Create the panel.
	 */
	
	public TrajectoryView(String objectId) {
		setLayout(null);
		
		JLabel lblDomo = new JLabel("");
		lblDomo.setBounds(10, 11, 80, 84);
		lblDomo.setIcon(new ImageIcon(TrajectoryView.class.getResource("/cn/edu/zju/db/datagen/gui/route.png")));
		add(lblDomo);
		
		JButton btnDetail = new JButton("Detail");
		btnDetail.setBounds(100, 58, 89, 23);
		add(btnDetail);
		
		JLabel label = new JLabel();
		label.setText(objectId);
		label.setBounds(100, 33, 89, 14);
		add(label);
	}
}
