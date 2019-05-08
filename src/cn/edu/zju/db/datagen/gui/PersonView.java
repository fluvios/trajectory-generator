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

public class PersonView extends JPanel {

	/**
	 * Create the panel.
	 */
	
	private ImageIcon normal = new ImageIcon("D:\\Fachri\\trajectory-generator\\src\\cn\\edu\\zju\\db\\datagen\\gui\\normal.png");
	private ImageIcon wheelchair = new ImageIcon("D:\\Fachri\\trajectory-generator\\src\\cn\\edu\\zju\\db\\datagen\\gui\\wheelchair.png");
	
	public PersonView() {
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.SOUTH);
		
		JButton button = new JButton("Delete");
		splitPane.setLeftComponent(button);
		
		JButton button_1 = new JButton("Detail");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		splitPane.setRightComponent(button_1);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon("D:\\Fachri\\trajectory-generator\\src\\cn\\edu\\zju\\db\\datagen\\gui\\normal.png"));
		add(lblNewLabel, BorderLayout.CENTER);
	}

}
