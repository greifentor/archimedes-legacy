/*
 * GetterConfiguration.java
 *
 * 21.10.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf;


import archimedes.model.*;

import gengen.util.*;


/**
 * A configuration class for the getter generation.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 21.10.2013 - Added.
 */

public class GetterConfiguration {

    private boolean abstractStubOnly = false;
    private CodeUtil codeUtil = null;
    private ColumnModel[] columns = null;
    private boolean noPrimaryKey = false;
    private boolean overridden = false;
    private boolean referencesToKeyClass = false;
    private TableModel table = null;
    private boolean timestampWrapped = false;
    private boolean timestampsToWrapperConversion = false;

    /**
     * Creates a new constructor configuration with the passed parameters.
     *
     * @param table The table model which the getters are created for.
     * @param codeUtil A code util e. g. for comment generation.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public GetterConfiguration(TableModel table, CodeUtil codeUtil) {
        super();
        this.codeUtil = codeUtil;
        this.columns = table.getColumns();
        this.table = table;
    }

    /**
     * Creates a new constructor configuration with the passed parameters.
     *
     * @param table The table model which the getters are created for.
     * @param codeUtil A code util e. g. for comment generation.
     * @param columns The columns which are to use in the constructor (e. g. as parameters).
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public GetterConfiguration(TableModel table, CodeUtil codeUtil, ColumnModel[] columns) {
        super();
        this.codeUtil = codeUtil;
        this.columns = columns;
        this.table = table;
    }

    /**
     * Returns the code util e. g. for comment generation.
     *
     * @return The code util e. g. for comment generation.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public CodeUtil getCodeUtil() {
        return this.codeUtil;
    }

    /**
     * Returns the columns which are used while constructor generation.
     *
     * @return The columns which are used while constructor generation.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public ColumnModel[] getColumns() {
        return this.columns;
    }

    /**
     * Returns the table model which the getters are generated for.
     *
     * @return The table model which the getters are generated for.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public TableModel getTable() {
        return this.table;
    }

    /**
     * Checks if the flag for abstract stub only is set.
     *
     * @return <CODE>true</CODE> if the flag for abstract stub only is set.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public boolean isAbstractStubOnly() {
        return this.abstractStubOnly;
    }

    /**
     * Checks if the flag for no primary key is set.
     *
     * @return <CODE>true</CODE> if the flag for no primary key is set.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public boolean isNoPrimaryKey() {
        return this.noPrimaryKey;
    }

    /**
     * Checks if the flag for overriden is set.
     *
     * @return <CODE>true</CODE> if the flag for overriden is set.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public boolean isOverriden() {
        return this.overridden;
    }

    /**
     * Checks if the flag for references to key class is set.
     *
     * @return <CODE>true</CODE> if the flag for references to key class is set.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public boolean isReferencesToKeyClass() {
        return this.referencesToKeyClass;
    }

    /**
     * Checks if the flag for timestamp to wrapper conversion is set.
     *
     * @return <CODE>true</CODE> if the flag for timestamp to wrapper conversion  is set.
     *
     * @changed OLI 22.10.2013 - Added.
     */
    public boolean isTimestampToWrapperConversion() {
        return this.timestampsToWrapperConversion;
    }

    /**
     * Checks if the flag for timestamp wrapped is set.
     *
     * @return <CODE>true</CODE> if the flag for timestamp wrapped is set.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public boolean isTimestampWrapped() {
        return this.timestampWrapped;
    }

    /**
     * Sets the flag for abstract stub only.
     *
     * @return The modified constructor configuration.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public GetterConfiguration setAbstractStubOnly() {
        this.abstractStubOnly = true;
        return this;
    }

    /**
     * Sets the flag for no primary key.
     *
     * @return The modified constructor configuration.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public GetterConfiguration setNoPrimaryKey() {
        this.noPrimaryKey = true;
        return this;
    }

    /**
     * Sets the flag for overriden.
     *
     * @return The modified constructor configuration.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public GetterConfiguration setOverriden() {
        this.overridden = true;
        return this;
    }

    /**
     * Sets the flag for the references to key class.
     *
     * @return The modified constructor configuration.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public GetterConfiguration setReferencesToKeyClass() {
        this.referencesToKeyClass = true;
        return this;
    }

    /**
     * Sets the flag for timestamp wrapped.
     *
     * @return The modified constructor configuration.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public GetterConfiguration setTimestampWrapped() {
        this.timestampWrapped = true;
        return this;
    }

    /**
     * Sets the flag for timestamp to wrapper conversion.
     *
     * @return The modified constructor configuration.
     *
     * @changed OLI 22.10.2013 - Added.
     */
    public GetterConfiguration setTimestampToWrapperConversion() {
        this.timestampsToWrapperConversion = true;
        return this;
    }

}