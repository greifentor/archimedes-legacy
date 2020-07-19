/*
 * AttributeBlockConfiguration.java
 *
 * 22.10.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf;

import archimedes.model.*;
import gengen.util.*;


/**
 * A configuration class for the attribute block generation.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 22.10.2013 - Added.
 */

public class AttributeBlockConfiguration {

    private CodeUtil codeUtil = null;
    private ColumnModel[] columns = null;
    private boolean forJPA = false;
    private ListAttributeData lad = null;
    private boolean primaryKeysOnly = false;
    private boolean referencesToKeyClass = false;
    private TableModel table = null;
    private boolean timestampWrapped = false;

    /**
     * Creates a new attribute block  configuration with the passed parameters.
     *
     * @param table The table model whose attribute block is to created.
     * @param codeUtil A code util e. g. for comment generation.
     *
     * @changed OLI 22.10.2013 - Added.
     */
    public AttributeBlockConfiguration(TableModel table, CodeUtil codeUtil) {
        super();
        this.codeUtil = codeUtil;
        this.columns = table.getColumns();
        this.table = table;
    }

    /**
     * Creates a new constructor configuration with the passed parameters.
     *
     * @param table The table model whose attribute block is to created.
     * @param columns The columns which are to use in the constructor (e. g. as parameters).
     * @param codeUtil A code util e. g. for comment generation.
     *
     * @changed OLI 22.10.2013 - Added.
     */
    public AttributeBlockConfiguration(TableModel table, ColumnModel[] columns,
            CodeUtil codeUtil) {
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
     * @changed OLI 22.10.2013 - Added.
     */
    public CodeUtil getCodeUtil() {
        return this.codeUtil;
    }

    /**
     * Returns the columns which are used while constructor generation.
     *
     * @return The columns which are used while constructor generation.
     *
     * @changed OLI 22.10.2013 - Added.
     */
    public ColumnModel[] getColumns() {
        return this.columns;
    }

    /**
     * Returns the table which the attribute block is to create for.
     *
     * @return The table which the attribute block is to create for.
     *
     * @changed OLI 03.08.2016 - Added.
     */
    public TableModel getTable() {
        return this.table;
    }

    /**
     * Returns the list attributes if there are some.
     *
     * @return The list attributes if there are some. Otherwise an empty array.
     *
     * @changed OLI 21.03.2016 - Added.
     */
    public ListAttribute[] getListAttributes() {
        if (this.lad != null) {
            return this.lad.getListAttributes();
        }
        return new ListAttribute[0];
    }

    /**
     * Checks if the flag for JPA is set.
     *
     * @return <CODE>true</CODE> if the flag for JPA is set.
     *
     * @changed OLI 22.10.2013 - Added.
     */
    public boolean isForJPA() {
        return this.forJPA;
    }

    /**
     * Checks if the flag for primary key only is set.
     *
     * @return <CODE>true</CODE> if the flag for primary key only is set.
     *
     * @changed OLI 22.10.2013 - Added.
     */
    public boolean isPrimaryKeysOnly() {
        return this.primaryKeysOnly;
    }

    /**
     * Checks if the flag for references to key class is set.
     *
     * @return <CODE>true</CODE> if the flag for references to key class is set.
     *
     * @changed OLI 22.10.2013 - Added.
     */
    public boolean isReferencesToKeyClass() {
        return this.referencesToKeyClass;
    }

    /**
     * Checks if the flag for timestamp wrapped is set.
     *
     * @return <CODE>true</CODE> if the flag for timestamp wrapped is set.
     *
     * @changed OLI 22.10.2013 - Added.
     */
    public boolean isTimestampWrapped() {
        return this.timestampWrapped;
    }

    /**
     * Sets the flag for JPA.
     *
     * @return The modified constructor configuration.
     *
     * @changed OLI 22.10.2013 - Added.
     */
    public AttributeBlockConfiguration setForJPA() {
        this.forJPA = true;
        return this;
    }

    /**
     * Sets the list attribute data.
     *
     * @param lad The list attribute data to set.
     * @return The modified constructor configuration.
     *
     * @changed OLI 21.03.2016 - Added.
     */
    public AttributeBlockConfiguration setListAttributeData(ListAttributeData lad) {
        this.lad = lad;
        return this;
    }

    /**
     * Sets the flag for primary keys only.
     *
     * @return The modified constructor configuration.
     *
     * @changed OLI 22.10.2013 - Added.
     */
    public AttributeBlockConfiguration setPrimaryKeysOnly() {
        this.primaryKeysOnly = true;
        return this;
    }

    /**
     * Sets the flag for the references to key class.
     *
     * @return The modified constructor configuration.
     *
     * @changed OLI 22.10.2013 - Added.
     */
    public AttributeBlockConfiguration setReferencesToKeyClass() {
        this.referencesToKeyClass = true;
        return this;
    }

    /**
     * Sets the flag for timestamp wrapped.
     *
     * @return The modified constructor configuration.
     *
     * @changed OLI 22.10.2013 - Added.
     */
    public AttributeBlockConfiguration setTimestampWrapped() {
        this.timestampWrapped = true;
        return this;
    }

}