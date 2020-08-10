/*
 * ChangeObserver.java
 *
 * 21.12.2011
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;


/**
 * Objekte, die diese Schnittstelle implementieren, k&ouml;nnen Auskunft dar&uuml;ber geben, ob
 * sie inhaltlich ge&auml;ndert worden sind.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 21.12.2011 - Hinzugef&uuml;gt.
 */

public interface ChangeObserver {

    /**
     * Markiert das implementierende Objekt als inhaltlich ge&auml;ndert.
     *
     * @changed OLI 21.12.2011 - Hinzugef&uuml;gt.
     */
    abstract public void raiseAltered();

    /**
     * Setzt die Markierung &uuml;ber eine inhaltliche &Auml;nderung zur&uuml;ck.
     *
     * @changed OLI 21.12.2011 - Hinzugef&uuml;gt.
     */
    abstract public void clearAltered();

    /** 
     * Pr&uuml;ft, ob das implementierende Objekt inhaltlich ge&auml;ndert worden ist..
     *
     * @return <CODE>true</CODE>, falls das implementierende Objekt inhaltlich ge&auml;ndert
     *         worden ist, sonst <CODE>false</CODE>.
     */
    abstract public boolean isAltered();

}