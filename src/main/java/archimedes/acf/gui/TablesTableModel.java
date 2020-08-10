/*
 * TablesTableModel.java
 *
 * 16.09.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.gui;

import javax.swing.table.AbstractTableModel;

import archimedes.acf.param.TableParamIds;
import archimedes.legacy.model.events.TableChangedEvent;
import baccara.gui.GUIBundle;

/**
 * A table model to represent the Archimedes table models in a swing table.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 23.10.2013 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public class TablesTableModel extends AbstractTableModel {

	private static final String[] COLUMN_HEADER_RESOURCE_NAMES = new String[] {
			"code.tables.configuration.table.column.name", "code.tables.configuration.table.column.generate.code" };
	private archimedes.legacy.model.TableModel[] tables = null;
	private GUIBundle guiBundle = null;

	/**
	 * Creates a new table model for the passed code table model array.
	 *
	 * @param tables    The tables which are represented by the table model.
	 * @param guiBundle A bundle with GUI information.
	 *
	 * @changed OLI 23.10.2013 - Added.
	 */
	public TablesTableModel(archimedes.legacy.model.TableModel[] tables, GUIBundle guiBundle) {
		super();
		this.tables = tables;
		this.guiBundle = guiBundle;
		for (archimedes.legacy.model.TableModel table : tables) {
			if (table.isGenerateCode() && !isTableUnselectedByStereoType(table)
					&& !isCodeGenerationSuppressedByOption(table)) {
				table.setGenerateCode(true);
			}
		}
	}

	private boolean isTableUnselectedByStereoType(archimedes.legacy.model.TableModel table) {
		return false;
	}

	private boolean isCodeGenerationSuppressedByOption(archimedes.legacy.model.TableModel table) {
		return !table.isOptionSet(TableParamIds.NO_CODE_GENERATION);
	}

	/**
	 * @changed OLI 23.10.2013 - Added.
	 */
	@Override
	public Class<?> getColumnClass(int column) {
		if (column == 1) {
			return Boolean.class;
		}
		return String.class;
	}

	/**
	 * @changed OLI 23.10.2013 - Added.
	 */
	@Override
	public int getColumnCount() {
		return COLUMN_HEADER_RESOURCE_NAMES.length;
	}

	/**
	 * @changed OLI 23.10.2013 - Added.
	 */
	@Override
	public String getColumnName(int column) {
		return this.guiBundle.getResourceText(COLUMN_HEADER_RESOURCE_NAMES[column]);
	}

	/**
	 * @changed OLI 23.10.2013 - Added.
	 */
	@Override
	public int getRowCount() {
		return this.tables.length;
	}

	/**
	 * @changed OLI 23.10.2013 - Added.
	 */
	@Override
	public Object getValueAt(int row, int column) {
		if (column == 1) {
			return this.tables[row].isGenerateCode();
		}
		return this.tables[row].getName();
	}

	/**
	 * @changed OLI 23.10.2013 - Added.
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		return (column == 1);
	}

	/**
	 * @changed OLI 23.10.2013 - Added.
	 */
	@Override
	public void setValueAt(Object o, int row, int column) {
		if (column == 1) {
			this.tables[row].setGenerateCode(((Boolean) o).booleanValue());
			this.tables[row].getDataModel().fireDataModelEvent(new TableChangedEvent(this.tables[row]));
		}
	}

}