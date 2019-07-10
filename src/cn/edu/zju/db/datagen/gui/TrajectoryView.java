package cn.edu.zju.db.datagen.gui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

import cn.edu.zju.db.datagen.indoorobject.movingobject.MovingObj;
import java.awt.FlowLayout;

public class TrajectoryView extends JPanel {

	/**
	 * Create the panel.
	 */
	
	public TrajectoryView(MovingObj obj, String title) {
		setLayout(null);
		
		JLabel lblDomo = new JLabel("");
		lblDomo.setBounds(10, 11, 80, 84);
		lblDomo.setIcon(new ImageIcon(TrajectoryView.class.getResource("/cn/edu/zju/db/datagen/gui/route.png")));
		add(lblDomo);
		
		JButton btnDetail = new JButton("Detail");
		btnDetail.setBounds(100, 58, 89, 23);
		add(btnDetail);
		
		JLabel label = new JLabel("<< Object Id >>");
		label.setBounds(100, 33, 89, 14);
		add(label);
	}
}
