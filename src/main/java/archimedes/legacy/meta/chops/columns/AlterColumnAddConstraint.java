/*
 * AlterColumnAddConstraint.java
 *
 * 11.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops.columns;

import archimedes.legacy.meta.MetaDataColumn;
import archimedes.legacy.meta.MetaDataTable;
import archimedes.legacy.meta.chops.AbstractChangeOperation;
import archimedes.legacy.meta.chops.ScriptSectionType;;

/**
 * A representation of a constraint addition.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

public class AlterColumnAddConstraint extends AbstractColumnConstraintChangeOperation {

	/**
	 * Creates a new change operation to add a constraint to a column.
	 *
	 * @param table          The information about the table which the column to add belongs to.
	 * @param column         The information about the column which is the constraint is to add to.
	 * @param constraintType The type of the constraint which is to add to the column.
	 *
	 * @changed OLI 11.12.2015 - Added.
	 */
	public AlterColumnAddConstraint(MetaDataTable table, MetaDataColumn column, ColumnConstraintType constraintType) {
		this(table, column, constraintType, ScriptSectionType.ADD_COLUMNS_AND_DROP_CONSTRAINTS);
	}

	/**
	 * Creates a new change operation to add a constraint to a column.
	 *
	 * @param table          The information about the table which the column to add belongs to.
	 * @param column         The information about the column which is the constraint is to add to.
	 * @param constraintType The type of the constraint which is to add to the column.
	 * @param section        The type of the section which the change operation is related to.
	 *
	 * @changed OLI 11.12.2015 - Added.
	 */
	public AlterColumnAddConstraint(MetaDataTable table, MetaDataColumn column, ColumnConstraintType constraintType,
			ScriptSectionType section) {
		super(table, column, constraintType, section);
	}

	/**
	 * @changed OLI 18.12.2015 - Added.
	 */
	@Override
	public int compareTo(AbstractChangeOperation o) {
		if (o instanceof AlterColumnAddConstraint) {
			AlterColumnAddConstraint acac = (AlterColumnAddConstraint) o;
			if (this.getSection() == acac.getSection()) {
				return this.getConstraintType().compareTo(acac.getConstraintType());
			}
		}
		return super.compareTo(o);
	}

	/**
	 * @changed OLI 11.12.2015 - Added.
	 */
	@Override
	public String toString() {
		return "AlterColumnAddConstraint(table=\"" + this.getTable().getName() + "\", column=\""
				+ this.getColumn().getName() + "\", constraintType=" + this.getConstraintType() + ")";
	}

}