/*
 * HistoryOwner.java
 *
 * 01.11.2011
 *
 * (c) O.Lieshoff
 *
 */

package archimedes.legacy.model;


/**
 * An interface which describes methods for objects which history information.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 01.11.2011 - Added.
 */

public interface HistoryOwner {

    /**
     * Returns the history of the object.
     *
     * @return The history of the object.
     *
     * @changed OLI 01.11.2011 - Added.
     */
    abstract public String getHistory();

    /**
     * Sets the passed string as new history for the object.
     *
     * @param newHistory The new history for the object.
     *
     * @changed OLI 01.11.2011 - Added.
     */
    abstract public void setHistory(String newHistory);

}