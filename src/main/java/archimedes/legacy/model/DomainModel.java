/*
 * DomainModel.java
 *
 * 01.04.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package archimedes.legacy.model;

import corent.db.*;
import corent.djinn.*;


/**
 * This interface defines the necessary methods for a domain.
 * 
 * @author O.Lieshoff
 *
 * @changed OLI 01.04.2004 - Added.
 * @changed OLI 10.06.2010 - Added the method <CODE>getType(DBExecMode)</CODE>. Format revised.
 * @changed OLI 01.11.2011 - Added the extension of interface <CODE>HistoryOwner</CODE>.
 * @changed OLI 23.07.2004 - Code revised and added the methods <CODE>getParameters()</CODE> and
 *         <CODE>setParameters(String)<CODE>. Documentation changed to English.
 */
 
public interface DomainModel extends CommentOwner, Comparable, HistoryOwner, NamedObject,
        OptionListProvider, Selectable {

    /**
     * Returns the Types constant for the data type of the domain.
     *
     * @return The Types constant for the data type of the domain.
     *
     * @changed OLI 01.04.2004 - Added.
     */
    abstract public int getDataType();

    /**
     * Returns the decimal place of the domain type.
     *
     * @return The decimal place of the domain type if the domain has a decimal point. Otherwise
     *         zero is returned.
     *
     * @changed OLI 01.04.2004 - Added.
     */
    abstract public int getDecimalPlace();

    /**
     * Returns the initial value for fields of the domain.
     *
     * @return The initial value for fields of the domain.
     *
     * @changed OLI 01.04.2004 - Added.
     */
    abstract public String getInitialValue();

    /**
     * Returns the length of the data fields of the domain if a length is available for the
     * type.
     *
     * @return The length of the data fields of the domain if a length is available for the
     *         type. Otherwise zero is returned.
     *
     * @changed OLI 01.04.2004 - Added.
     */
    abstract public int getLength();

    /**
     * Returns the name of the domain.
     *
     * @return The name of the domain.
     *
     * @changed OLI 01.04.2004 - Added.
     */
    abstract public String getName();

    /**
     * Returns the parameters set for the domain.
     *
     * @return The parameters set for the domain.
     *
     * @changed OLI 23.07.2013 - Added.
     */
    abstract public String getParameters();

    /** 
     * Returns the SQL type of the domain. This will be generated from the domain data.
     *
     * @return The SQL type of the domain. This will be generated from the domain data.
     *
     * @changed OLI 01.04.2004 - Added.
     */
    abstract public String getType();

    /**
     * Returns a DBMS specific type related to the type of the domain and the passed DBMS type.
     *
     * @param dbmode The mode of the DBMS.
     * @return The DBMS specific type related to the type of the domain and the passed DBMS
     *         type.
     * @throws IllegalArgumentException In case of passing a null pointer.
     *
     * @changed OLI 10.06.2010 - Added.
     */
    abstract public String getType(DBExecMode dbmode);

    /**
     * Sets the passed <CODE>Types</CODE> constant as new data type for teh domain.
     *
     * @param type The new <CODE>Types</CODE> constant as new domain data type.
     *
     * @changed OLI 01.04.2004 - Added.
     */
    abstract public void setDataType(int type);

    /**
     * Sets the passed value as new number of decimal place.
     *
     * @param dp The new number of decimal place.
     *
     * @changed OLI 01.04.2004 - Added.
     */
    abstract public void setDecimalPlace(int dp);

    /**
     * Sets the passed value a the new initial value for fields of the domain.
     *
     * @param value The new initial value for the fields of the domain.
     *
     * @changed OLI 01.04.2004 - Added.
     */
    abstract public void setInitialValue(String value);

    /**
     * Sets the passed value as the new length of the domain.
     *
     * @param len The new length of the domain.
     *
     * @changed OLI 01.04.2004 - Added.
     */
    abstract public void setLength(int len);

    /**
     * Sets the passed name as new name for the domain.
     *
     * @param name The new name of the domain.
     *
     * @changed OLI 01.04.2004 - Added.
     */
    abstract public void setName(String name);

    /**
     * Sets the passed string as new parameter string for the domain. 
     *
     * @param parameters The new parameters for the domain.
     * @throws IllegalArgumentException Passing a null pointer as parameter string.
     *
     * @changed OLI 23.07.2013 - Added.
     */
    abstract public void setParameters(String parameters) throws IllegalArgumentException;

}