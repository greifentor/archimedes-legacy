/*
 * ToXMLTag.java
 *
 * 03.04.2007
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.xml;


/**
 * Dieses Interface stellt sicher, da&szlig; ein Objekt in ein XML-Tag umwandeln 
 * l&auml;&szlig;t.
 *
 * @author O.Lieshoff
 *
 */
 
public interface ToXMLTag {
    
    /**
     * Wandelt den Inhalt des Objektes in einen String mit einem g&uuml;ltigen XML-Tag um.
     *
     * @return Der String mit dem XML-Tag zum Objekt.
     */
    public String toXMLTag();
    
}
