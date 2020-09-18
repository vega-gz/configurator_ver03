package ReadWriteExcel;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.beans.*;
import java.util.Vector;

/*
 *  This class listens for changes made to the data in the table via the
 *  TableCellEditor. When editing is started, the value of the cell is saved
 *  When editing is stopped the new value is saved. When the oold and new
 *  values are different, then the provided Action is invoked.
 *
 *  The source of the Action is a TableCellListener instance.
 */
public class TableCellListener implements PropertyChangeListener, Runnable
{
	private JTable table;
	private Action action;

	private Vector<Vector> oldData;
	private Vector<Vector> newData;

	/**
	 *  Create a TableCellListener.
	 *
	 *  @param table   the table to be monitored for data changes
	 *  @param action  the Action to invoke when cell data is changed
	 */
	public TableCellListener(JTable table, Action action)
	{
		this.table = table;
		this.action = action;
		this.table.addPropertyChangeListener( this );
	}

	/**
	 *  Create a TableCellListener with a copy of all the data relevant to
	 *  the change of data for a given cell.
	 *
	 *  @param oldData  the old data of the changed model
	 *  @param newData  the new data of the changed model
	 */
	private TableCellListener(JTable table, Vector<Vector> oldData, Vector<Vector> newData)
	{
		this.table = table;
		this.oldData = oldData;
		this.newData = newData;
	}

	private Vector<Vector> getNewData()
	{
		return newData;
	}

	/**
	 *  Get the old data
	 *
	 *  @return the old data of the model
	 */
	public Vector<Vector> getOldData()
	{
		return oldData;
	}

	/**
	 *  Get the table of the cell that was changed
	 *
	 *  @return the table of the cell that was changed
	 */
	public JTable getTable()
	{
		return table;
	}
//
//  Implement the PropertyChangeListener interface
//
	@Override
	public void propertyChange(PropertyChangeEvent e)
	{
		//  A cell has started/stopped editing

		if ("tableCellEditor".equals(e.getPropertyName()))
		{
			if (table.isEditing())
				processEditingStarted();
			else
				processEditingStopped();
		}
	}

	/*
	 *  Save information of the cell about to be edited
	 */
	private void processEditingStarted()
	{
		

		SwingUtilities.invokeLater( this );
	}
	/*
	 *  See above.
	 */
	@Override
	public void run()
	{
		fixOldData();
	}

	public void fixOldData() {
		TableModel model = table.getModel();
		oldData = new Vector<>(model.getRowCount());
		for (int i = 0; i < model.getRowCount(); ++i) {
			oldData.add(new Vector<>());
			for (int j = 0; j < model.getColumnCount(); ++j)
				oldData.get(i).add(model.getValueAt(i, j));
		}
	}

	public void tableChanged() {
		TableCellListener tcl = new TableCellListener(table, getOldData(), getNewData());
		ActionEvent event = new ActionEvent(
				tcl,
				ActionEvent.ACTION_PERFORMED,
				"");
		action.actionPerformed(event);
	}

	/*
	 *	Update the Cell history when necessary
	 */
	private void processEditingStopped()
	{
		newData = ((DefaultTableModel) table.getModel()).getDataVector();

		//  The data has changed, invoke the supplied Action
		for (int i = 0; i < newData.size(); i++) {
			for (int j = 0; j < newData.get(0).size(); j++) {
				if (!newData.get(i).get(j).equals(oldData.get(i).get(j))) {
					tableChanged();
					return;
				}
			}
		}
	}
}
