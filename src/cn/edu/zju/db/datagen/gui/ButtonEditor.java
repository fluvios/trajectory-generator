package cn.edu.zju.db.datagen.gui;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.alee.graphics.image.gif.RepaintListener;

import cn.edu.zju.db.datagen.trajectory.Trajectory;

public class ButtonEditor extends DefaultCellEditor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JButton button;
	private String label;
	private boolean isPushed;
	private Map<Integer, ArrayList<Trajectory>> idTrajectories = new HashMap<Integer, ArrayList<Trajectory>>();
	private Home gui = new Home();
	

	public ButtonEditor(JCheckBox checkBox, JTable table,
			Map<Integer, ArrayList<Trajectory>> idTrajectories) {
		super(checkBox);
		this.idTrajectories = idTrajectories;
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				int row = table.convertRowIndexToModel( table.getEditingRow() );
//				System.out.println(idTrajectories.get(table.getValueAt(row, 0)));
				gui.idTrajectory = idTrajectories.get(table.getValueAt(row, 0));
				gui.isDisplay = true;
				gui.revalidate();
				gui.repaint();
				fireEditingStopped();
			}
		});
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (isSelected) {
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setForeground(table.getForeground());
			button.setBackground(table.getBackground());
		}
		label = (value == null) ? "" : value.toString();
		button.setText(label);
		isPushed = true;
		return button;
	}

	public Object getCellEditorValue() {
		if (isPushed) {
			//
			//
			// JOptionPane.showMessageDialog(button ,label + ": Ouch!");
			// System.out.println(label + ": Ouch!");

		}
		isPushed = false;
		return new String(label);
	}

	public boolean stopCellEditing() {
		isPushed = false;
		return super.stopCellEditing();
	}

	protected void fireEditingStopped() {
		super.fireEditingStopped();
	}
}