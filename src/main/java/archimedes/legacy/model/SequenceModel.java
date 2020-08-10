/*
 * SequenceModel.java
 *
 * 23.04.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;

import corent.djinn.*;


/**
 * An interface to describe sequences in the Archimedes model.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 23.04.2013 - Added.
 */

public interface SequenceModel extends CommentOwner, Comparable, HistoryOwner, NamedObject,
        Selectable {

    /**
     * Returns the increment value for the sequence.
     *
     * @return The increment value for the sequence.
     *
     * @changed OLI 23.04.2013 - Added.
     */
    abstract public long getIncrement();

    /**
     * Returns the name of the sequence.
     *
     * @return The name of the sequence.
     *
     * @changed OLI 23.04.2013 - Added.
     */
    abstract public String getName();

    /**
     * Returns the start value for the sequence.
     *
     * @return The start value for the sequence.
     *
     * @changed OLI 23.04.2013 - Added.
     */
    abstract public long getStartValue();

    /**
     * Sets the passed value as new increment value for the sequence.
     *
     * @param increment The new increment value for the sequence.
     * @throws IllegalArgumentException If the increment is passed lesser than one.
     *
     * @changed OLI 23.04.2013 - Added.
     */
    abstract public void setIncrement(long increment) throws IllegalArgumentException;

    /**
     * Sets the passed value as new name for the sequence.
     *
     * @param name The new name for the sequence.
     * @throws IllegalArgumentException If the name is passed as an empty or null string.
     *
     * @changed OLI 23.04.2013 - Added.
     */
    abstract public void setName(String name) throws IllegalArgumentException;

    /**
     * Sets the passed value as new start value for the sequence.
     *
     * @param startValue The new start value for the sequence.
     * @throws IllegalArgumentException If the start value is passed lesser than zero.
     *
     * @changed OLI 23.04.2013 - Added.
     */
    abstract public void setStartValue(long startValue) throws IllegalArgumentException;

}