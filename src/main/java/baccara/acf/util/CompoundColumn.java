/*
 * CompoundColumn.java
 *
 * 05.08.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.acf.util;

import archimedes.legacy.scheme.Tabellenspalte;
import archimedes.model.ColumnModel;

/**
 * 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 05.08.2016 - Added.
 */

public class CompoundColumn extends Tabellenspalte {

	private ColumnModel parentColumn = null;

	/**
	 * Creates a new compound column with the passed parameters.
	 *
	 * @param parentColumn The parent column of the compound column.
	 * @param linkedColumn The linked column of the compound column.
	 *
	 * @changed OLI 05.08.2016 - Added.
	 */
	public CompoundColumn(ColumnModel parentColumn, ColumnModel linkedColumn) {
		super(linkedColumn);
		this.parentColumn = parentColumn;
	}

	/*
	 * Returns the name for the compound column with a dot.
	 *
	 * @return The name for the compound column with a dot.
	 *
	 * @changed OLI 05.08.2016 - Added.
	 */
	String getCompoundName() {
		if (this.getParentColumn() instanceof CompoundColumn) {
			return ((CompoundColumn) this.getParentColumn()).getCompoundName() + "." + super.getName();
		}
		return this.getParentColumn().getName() + "." + super.getName();
	}

	/**
	 * Returns the full name for the compound column.
	 *
	 * @return The full name for the compound column.
	 *
	 * @changed OLI 05.08.2016 - Added.
	 */
	@Override
	public String getFullName() {
		return this.getTable().getName() + "." + super.getName();
	}

	/**
	 * Returns the name for the compound column.
	 *
	 * @return The name for the compound column.
	 *
	 * @changed OLI 05.08.2016 - Added.
	 */
	@Override
	public String getName() {
		return this.getCompoundName();
	}

	/**
	 * Returns the parent column of the compound column.
	 *
	 * @return The parent column of the compound column.
	 *
	 * @changed OLI 05.08.2016 - Added.
	 */
	public ColumnModel getParentColumn() {
		return this.parentColumn;
	}

	/**
	 * Returns the simple name for the compound column.
	 *
	 * @return The simple name for the compound column.
	 *
	 * @changed OLI 09.08.2016 - Added.
	 */
	public String getSimpleName() {
		return super.getName();
	}

}