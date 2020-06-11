/*
 * Lockable.java
 *
 * 24.09.2007
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;



/**
 * Dieses Interface kann von Objekten implementiert werden, die &uuml;ber einen explizite
 * Methode gelockt werden sollen.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 24.09.2007 - Hinzugef&uuml;gt.
 * @changed OLI 15.01.2010 - Anpassung der Formatierung.
 */

public interface Lockable {

    /**
     * Diese Methode dient zur Pr&uuml;fung, ob das implementierende Objekt in seinem
     * gegenw&auml;rtigen Zustand &uuml;berhaupt gelockt werden kann.
     *
     * @return <TT>true</TT>, wenn das Objekt gegen andere Zugriffe gesch&uuml;tzt werden kann
     *         und/oder soll. <TT>false</TT>, wenn dies nicht notwendig oder m&ouml;glich ist.
     */
    public boolean isLockable();

    /**
     * Liefert einen String, der das Objekt eindeutig identifiziert und als Bezeichner f&uuml;r
     * das Lock dienen kann.
     *
     * @return Ein eindeutiger Bezeichner f&uuml;r das Objekt.
     */
    public String getLockString();

}
