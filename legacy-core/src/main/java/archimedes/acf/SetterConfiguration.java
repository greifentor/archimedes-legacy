/*
 * SetterConfiguration.java
 *
 * 02.05.2016
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.acf;

import archimedes.model.ColumnModel;
import archimedes.model.TableModel;
import gengen.util.CodeUtil;

/**
 * A configuration container for the setters.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 02.05.2016 - Added.
 */

public class SetterConfiguration {

	private boolean changeListenerLogicRequired = false;
	private CodeUtil codeUtil = null;
	private ColumnModel[] columns = null;
	private TableModel table = null;
	private boolean overriden = false;
	private boolean timestampAsReference = false;

	/**
	 * Creates a new setter configuration with the passed parameters.
	 *
	 * @param table    The table model which the setters are created for.
	 * @param codeUtil A code util e. g. for comment generation.
	 *
	 * @changed OLI 02.05.2016 - Added.
	 */
	public SetterConfiguration(TableModel table, CodeUtil codeUtil) {
		super();
		this.codeUtil = codeUtil;
		this.columns = table.getColumns();
		this.table = table;
	}

	/**
	 * Returns the code util e. g. for comment generation.
	 *
	 * @return The code util e. g. for comment generation.
	 *
	 * @changed OLI 02.05.2016 - Added.
	 */
	public CodeUtil getCodeUtil() {
		return this.codeUtil;
	}

	/**
	 * Returns the columns which are used while setter generation.
	 *
	 * @return The columns which are used while setter generation.
	 *
	 * @changed OLI 02.05.2016 - Added.
	 */
	public ColumnModel[] getColumns() {
		return this.columns;
	}

	/**
	 * Returns the table model which the setters are generated for.
	 *
	 * @return The table model which the setters are generated for.
	 *
	 * @changed OLI 02.05.2016 - Added.
	 */
	public TableModel getTable() {
		return this.table;
	}

	/**
	 * Checks for change listener logic implementation.
	 *
	 * @return <CODE>true</CODE> if the setter requires a change listener logic implementation.
	 *
	 * @changed OLI 02.05.2016 - Added.
	 */
	public boolean isChangeListenerLogicRequired() {
		return this.changeListenerLogicRequired;
	}

	/**
	 * Checks for a set overriden flag.
	 *
	 * @return <CODE>true</CODE> if the setters should override other setters or interface specifications.
	 *
	 * @changed OLI 02.05.2016 - Added.
	 */
	public boolean isOverriden() {
		return this.overriden;
	}

	/**
	 * Checks for a set timestamp as reference flag.
	 *
	 * @return <CODE>true</CODE> if the setters should handle timestamps as object references.
	 *
	 * @changed OLI 19.05.2016 - Added.
	 */
	public boolean isTimestampAsReference() {
		return this.timestampAsReference;
	}

	/**
	 * Sets the flag for change listener logic requirement.
	 *
	 * @return The modified setter configuration.
	 *
	 * @changed OLI 02.05.2016 - Added.
	 */
	public SetterConfiguration setChangeListenerLogicRequired() {
		this.changeListenerLogicRequired = true;
		return this;
	}

	/**
	 * Sets the flag for handling timestamps as references.
	 *
	 * @return The modified setter configuration.
	 *
	 * @changed OLI 19.05.2016 - Added.
	 */
	public SetterConfiguration setTimestampAsReference() {
		this.timestampAsReference = true;
		return this;
	}

}