/*
 * TableModelColumns.java
 *
 * 19.10.2017
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.table;

import java.util.List;

import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.OptionModel;
import baccara.gui.BasicTableModel;
import baccara.gui.GUIBundle;
import corentx.util.SortedVector;

/**
 * A table model to manage the columns of a data table.
 * 
 * @author ollie
 * 
 * @changed OLI 19.10.2017 - Added.
 */

public class TableModelColumns extends BasicTableModel {

	private static final String RES_PREFIX = "TableModelColumns.Column.";

	private GUIBundle guiBundle = null;
	private List<ColumnModel> columns = new SortedVector<ColumnModel>();

	/**
	 * Creates a new table model for columns with the passed parameters.
	 * 
	 * @param columns
	 *            The columns to edit.
	 * @param guiBundle
	 *            A bundle with GUI information.
	 * 
	 * @changed OLI 19.10.2017 - Added.
	 */
	public TableModelColumns(List<ColumnModel> columns, GUIBundle guiBundle) {
		super();
		this.guiBundle = guiBundle;
		for (ColumnModel c : columns) {
			this.columns.add(c);
		}
	}

	/**
	 * @changed OLI 19.10.2017 - Added.
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		Class<?>[] cls = new Class[] { String.class, String.class, Boolean.class, Boolean.class, Boolean.class,
				Boolean.class, Boolean.class, Boolean.class, String.class, String.class, String.class, String.class };
		return cls[columnIndex];
	}

	/**
	 * @changed OLI 19.10.2017 - Added.
	 */
	@Override
	public int getColumnCount() {
		return 12;
	}

	/**
	 * @changed OLI 19.10.2017 - Added.
	 */
	@Override
	public String getColumnName(int columnIndex) {
		String[] res = new String[] { RES_PREFIX + "Name", RES_PREFIX + "DomainName", RES_PREFIX + "PrimaryKey",
				RES_PREFIX + "ForeignKey", RES_PREFIX + "Index", RES_PREFIX + "NotNull", RES_PREFIX + "Unique",
				RES_PREFIX + "Transient", RES_PREFIX + "DefaultValue", RES_PREFIX + "Constraints",
				RES_PREFIX + "EditorParameter", RES_PREFIX + "Options" };
		return this.guiBundle.getResourceText(res[columnIndex] + ".text");
	}

	/**
	 * @changed OLI 19.10.2017 - Added.
	 */
	@Override
	public int getRowCount() {
		return this.columns.size();
	}

	/**
	 * @changed OLI 19.10.2017 - Added.
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ColumnModel c = this.columns.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return c.getName();
		case 1:
			return c.getDomain().getName();
		case 2:
			return c.isPrimaryKey();
		case 3:
			return c.getReferencedTable() != null;
		case 4:
			return c.isIndexSearchMember();
		case 5:
			return c.isNotNull();
		case 6:
			return c.isUnique();
		case 7:
			return c.isTransient();
		case 8:
			return c.getIndividualDefaultValue();
		case 9:
			return "";
		case 10:
			return c.getEditorPosition() + ", " + c.getEditorLabelText();
		case 11:
			String s = "";
			for (OptionModel o : c.getOptions()) {
				if (s.length() > 0) {
					s += " | ";
				}
				s += o.getName()
						+ ((o.getParameter() != null) && !o.getParameter().isEmpty() ? ":" + o.getParameter() : "");
			}
			return s;
		}
		return null;
	}

	/**
	 * @changed OLI 19.10.2017 - Added.
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	/**
	 * @changed OLI 19.10.2017 - Added.
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

}