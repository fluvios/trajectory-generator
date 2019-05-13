package cn.edu.zju.db.datagen.gui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

import cn.edu.zju.db.datagen.machine.Machine;

import java.awt.Font;
import java.awt.Color;

public class MachineView extends JPanel {

	/**
	 * Create the panel.
	 */
	private Machine obj;
	private JLabel title;

	public MachineView(Machine obj, String title) {
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
		lblNewLabel.setIcon(new ImageIcon(MachineView.class.getResource("/cn/edu/zju/db/datagen/gui/server.png")));
		add(lblNewLabel, BorderLayout.CENTER);
		
		this.title = new JLabel(title);
		this.title.setFont(new Font("Dialog", Font.PLAIN, 14));
		this.title.setHorizontalAlignment(SwingConstants.CENTER);
		add(this.title, BorderLayout.NORTH);
	}

}
