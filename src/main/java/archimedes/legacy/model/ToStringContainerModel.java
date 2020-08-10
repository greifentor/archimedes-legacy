/*
 * ToStringContainerModel.java
 *
 * 02.06.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;


/**
 * An interface which provides the methods for a to-string-container.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 02.06.2016 - Added.
 */

public interface ToStringContainerModel {

    /**
     * Returns the prefix of the to string position.
     *
     * @return The prefix of the to string position.
     *
     * @changed OLI 02.06.2016 - Added.
     */
    abstract public String getPrefix();

    /**
     * Returns the column which the to-string-container is created for.
     *
     * @return The column which the to-string-container is created for.
     *
     * @changed OLI 02.06.2016 - Added.
     */
    abstract public ColumnModel getColumn();

    /**
     * Returns the suffix of the to string position.
     *
     * @return The suffix of the to string position.
     *
     * @changed OLI 02.06.2016 - Added.
     */
    abstract public String getSuffix();

}