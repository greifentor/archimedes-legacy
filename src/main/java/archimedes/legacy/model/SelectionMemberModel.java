/*
 * SelectionMemberModel.java
 *
 * 20.08.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;


/**
 * An interface to describe selection members.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 20.08.2013 - Added.
 * @changed OLI 02.08.2016 - Extended by the "printExpression".
 */

public interface SelectionMemberModel {

    /**
     * Returns the attribute which is assigned to the column.
     *
     * @return The attribute which is assigned to the column.
     *
     * @changed OLI 20.08.2013 - Added.
     */
    abstract public SelectionAttribute getAttribute();

    /**
     * Returns the column which is a selection member.
     *
     * @return The column which is a selection member.
     *
     * @changed OLI 20.08.2013 - Added.
     */
    abstract public ColumnModel getColumn();

    /**
     * Returns the print expression for the selection member.
     *
     * @return The print expression for the selection member.
     *
     * @changed OLI 02.08.2016 - Added.
     */
    abstract public String getPrintExpression();

    /**
     * Sets the passed attribute as new attribute for the selection member.
     *
     * @param attribute The new attribute for the selection member.
     *
     * @changed OLI 20.08.2013 - Added.
     */
    abstract public void setAttribute(SelectionAttribute attribute);

    /**
     * Sets the passed column as new column for the selection member.
     *
     * @param column The new column to be set as selection member.
     *
     * @changed OLI 20.08.2013 - Added.
     */
    abstract public void setColumn(ColumnModel column);

    /**
     * Sets the passed print expression as new print expression for the selection member.
     *
     * @param printExpression The new print expression as new print expression to be set as
     *         selection member.
     *
     * @changed OLI 02.08.2016 - Added.
     */
    abstract public void setPrintExpression(String printExpression);

}