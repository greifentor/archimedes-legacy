/*
 * DBExecEventType.java
 *
 * 27.04.2011
 *
 * (c) by O.Lieshoff
 *
 */

package corent.db;


/**
 * Bezeichner zum Typisieren von <TT>DBExecEvents</TT>.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 27.04.2011 - Hinzugef&uuml;gt.
 */

public enum DBExecEventType {

    /**
     * Bezeichner f&uuml;r Events, die durch Aufruf der Query-Methode entstanden sind.
     *
     * @changed OLI 27.04.2011 - Hinzugef&uuml;gt.
     */
    QUERY,
    /**
     * Bezeichner f&uuml;r Events, die durch Aufruf der Update-Methode entstanden sind.
     *
     * @changed OLI 27.04.2011 - Hinzugef&uuml;gt.
     */
    UPDATE;

}