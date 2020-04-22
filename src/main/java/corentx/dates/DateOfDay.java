/*
 * DateOfDay.java
 *
 * 08.11.2012
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.dates;

import java.util.*;


/**
 * Ein Interface zur Markierung von Timestamps, die ein Tagesdatum enthalten.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.11.2012 - Hinzugef&uuml;gt.
 */

public interface DateOfDay {

    /**
     * Wandelt den Zeitstempel im ein Date-Objekt um.
     *
     * @return Ein Date-Objekt mit den Inhalten des Zeitstempels.
     *
     * @changed OLI 03.09.2009 - Hinzugef&uuml;gt.
     */
    public Date toDate();

}