/*
 * AttributeNotFoundException.java
 *
 * 16.02.2009
 *
 * (c) O.Lieshoff
 *
 */

package corent.base.dynamic;


/**
 * Diese Exception wird vom <TT>DynamicObject</TT> geworfen, wenn ein Zugriff auf ein nicht
 * vorhandenes Attribut stattfindet.
 * 
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 16.02.2009 - Hinzugef&uuml;gt.
 *     <P>
 *
 */

public class AttributeNotFoundException extends Exception {

    /**
     * Generiert eine AttributeNotFoundException mit den &uuml;bergebenen Parametern.
     *
     * @param attr Der Name des Attribute, das nicht existiert.
     * @param clsname Der Name der DynamicObject-Subklasse, von der aus das Attribut mit dem 
     *         unbekannten Namen angefordert worden ist.
     */
    public AttributeNotFoundException(String attr, String clsname) {
        super("Attribute called " + attr + " is not available for class " + clsname + ".");
    }

}
