/*
 * ConstructorConfiguration.java
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
 * A configuration class for the constructor generation.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 21.10.2013 - Added.
 */

public class ConstructorConfiguration {

    private boolean callSuperClass = false;
    private String className = null;
    private CodeUtil codeUtil = null;
    private ColumnModel[] columns = null;
    private boolean entityAnnotations = false;
    private boolean keyClass = false;
    private boolean referencesToKeyClass = false;
    private boolean simpleDataCall = false;
    private TableModel table = null;
    private boolean timestampWrapped = false;
    private boolean timestampsToWrapperConversion = false;

    /**
     * Creates a new constructor configuration with the passed parameters.
     *
     * @param className The name of the class whose instance are created by the constructor.
     * @param codeUtil A code util e. g. for comment generation.
     * @param columns The columns which are to use in the constructor (e. g. as parameters).
     * @param table The table which the constructor is to create for.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public ConstructorConfiguration(String className, CodeUtil codeUtil, ColumnModel[] columns,
            TableModel table) {
        super();
        this.className = className;
        this.codeUtil = codeUtil;
        this.columns = columns;
        this.table = table;
    }

    /**
     * Returns the class name of the class whose constructor is generated.
     *
     * @return The class name of the class whose constructor is generated.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public String getClassName() {
        return this.className;
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
     * Returns the table which the constructor is to create for.
     *
     * @return The table which the constructor is to create for.
     *
     * @changed OLI 03.08.2016 - Added.
     */
    public TableModel getTable() {
        return this.table;
    }

    /**
     * Checks if the flag for super class calls is set.
     *
     * @return <CODE>true</CODE> if the flag for super class calls is set.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public boolean isCallSuperClass() {
        return this.callSuperClass;
    }

    /**
     * Checks if the flag for entity annotations is set.
     *
     * @return <CODE>true</CODE> if the flag for entity annotations is set.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public boolean isEntityAnnotations() {
        return this.entityAnnotations;
    }

    /**
     * Checks if the flag for key class is set.
     *
     * @return <CODE>true</CODE> if the flag for key class is set.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public boolean isKeyClass() {
        return this.keyClass;
    }

    /**
     * Checks if the flag for references to key class  is set.
     *
     * @return <CODE>true</CODE> if the flag for references to key class is set.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public boolean isReferencesToKeyClass() {
        return this.referencesToKeyClass;
    }

    /**
     * Checks if the flag for simple data call is set.
     *
     * @return <CODE>true</CODE> if the flag for simple data call is set.
     *
     * @changed OLI 05.10.2015 - Added.
     */
    public boolean isSimpleDataCall() {
        return this.simpleDataCall;
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
     * Checks if the flag for timestamp to wrapper conversion is set.
     *
     * @return <CODE>true</CODE> if the flag for timestamp to wrapper conversion is set.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public boolean isTimestampToWrapperConversion() {
        return this.timestampsToWrapperConversion;
    }

    /**
     * Sets the flag for the call super class.
     *
     * @return The modified constructor configuration.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public ConstructorConfiguration setCallSuperClass() {
        this.callSuperClass = true;
        return this;
    }

    /**
     * Sets the flag for the entity annotations.
     *
     * @return The modified constructor configuration.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public ConstructorConfiguration setEntityAnnotations() {
        this.entityAnnotations = true;
        return this;
    }

    /**
     * Sets the flag for the key class.
     *
     * @return The modified constructor configuration.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public ConstructorConfiguration setKeyClass() {
        this.keyClass = true;
        return this;
    }

    /**
     * Sets the flag for the references to key class.
     *
     * @return The modified constructor configuration.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public ConstructorConfiguration setReferencesToKeyClass() {
        this.referencesToKeyClass = true;
        return this;
    }

    /**
     * Sets the flag for the simple data call wrapped.
     *
     * @return The modified constructor configuration.
     *
     * @changed OLI 05.10.2015 - Added.
     */
    public ConstructorConfiguration setSimpleDataCall() {
        this.simpleDataCall = true;
        return this;
    }

    /**
     * Sets the flag for the timestamp wrapped.
     *
     * @return The modified constructor configuration.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public ConstructorConfiguration setTimestampWrapped() {
        this.timestampWrapped = true;
        return this;
    }

    /**
     * Sets the flag for the timestamp to wrapper conversion.
     *
     * @return The modified constructor configuration.
     *
     * @changed OLI 21.10.2013 - Added.
     */
    public ConstructorConfiguration setTimestampToWrapperConversion() {
        this.timestampsToWrapperConversion = true;
        return this;
    }

}