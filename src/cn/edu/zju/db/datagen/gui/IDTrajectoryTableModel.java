package cn.edu.zju.db.datagen.gui;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import javafx.event.ActionEvent;

public class IDTrajectoryTableModel extends AbstractTableModel {

	private static final String[] COLUMN_NAMES = new String[] { "Object Id", "Action", "Detail" };
	private static final Class<?>[] COLUMN_TYPES = new Class<?>[] { Integer.class, JButton.class, JButton.class };

	@Override
	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}

	@Override
	public int getRowCount() {
		return 4;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return COLUMN_NAMES[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return COLUMN_TYPES[columnIndex];
	}

	@Override public Object getValueAt(final int rowIndex, final int columnIndex) {
        /*Adding components*/
    switch (columnIndex) {
        case 0: return rowIndex;
        case 1: return "Text for "+rowIndex;
        case 2: // fall through
       /*Adding button and creating click listener*/
        case 3: final JButton button = new JButton(COLUMN_NAMES[columnIndex]);
                button.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(java.awt.event.ActionEvent arg0) {
						// TODO Auto-generated method stub
					    JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(button), 
					            "Button clicked for row "+rowIndex);						
					}
                });
                return button;
        default: return "Error";
    }
}

}
