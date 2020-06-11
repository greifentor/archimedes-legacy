/*
 * ToXMLAttributes.java
 *
 * 03.04.2007
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.xml;


/**
 * Dieses Interface stellt sicher, da&szlig; ein Objekt in eine Liste von XML-Attributen 
 * umgewandelt werden kann.
 *
 * @author O.Lieshoff
 *
 */
 
public interface ToXMLAttributes {
    
    /**
     * Wandelt den Inhalt des Objektes in einen String mit einer Liste von XML-Attibuten um.
     *
     * @return Der String mit den XML-Attributen zum Objekt.
     */
    public String toXMLAttributes();
    
}
