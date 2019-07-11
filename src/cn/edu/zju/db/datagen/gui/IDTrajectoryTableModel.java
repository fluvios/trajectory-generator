package cn.edu.zju.db.datagen.gui;

import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

public class IDTrajectoryTableModel extends AbstractTableModel {

	private static final String[] COLUMN_NAMES = new String[] { "Object Id", "Action", "Detail" };
	private static final Class<?>[] COLUMN_TYPES = new Class<?>[] { Integer.class, JButton.class, JButton.class };
	
	private Vector data = new Vector();

	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return COLUMN_NAMES[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return COLUMN_TYPES[columnIndex];
	}

	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		/* Adding components */
		switch (columnIndex) {
		case 0:
			return rowIndex;
		case 1:
			final JButton actionButton = new JButton(COLUMN_NAMES[columnIndex]);
			actionButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(java.awt.event.ActionEvent arg0) {
					// TODO Auto-generated method stub
					JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(actionButton),
							"Button clicked for row " + rowIndex);
				}
			});
			return actionButton;
		case 2:
			final JButton detailButton = new JButton(COLUMN_NAMES[columnIndex]);
			detailButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(java.awt.event.ActionEvent arg0) {
					// TODO Auto-generated method stub
					JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(detailButton),
							"Button clicked for row " + rowIndex);
				}
			});
			return detailButton;
		default:
			return "Error";
		}
	}

	public void addRow(Object[] row) {
        data.add(row);
		this.fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);

	}
}
