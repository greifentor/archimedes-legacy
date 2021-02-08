/*
 * Attributed.java
 *
 * 17.12.2003
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.base;


/**
 * Mit Hilfe dieses Interfaces k&ouml;nnen Attribute eines Objektes &uuml;ber festgelegte Namen
 * abgerufen oder gesetzt werden. Haupts&auml;chlich dient das Interface der Umgehung der 
 * Reflection-Klassen und der mit deren Benutzung entstehenden Fallen und Fallstricken.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface Attributed {
    
    /**
     * Diese Methode erm&ouml;glicht den Zugriff auf ein durch den angegebenen Namen 
     * spezifiziertes Attribut.
     *
     * @param id Die Id des Attributes.
     * @return Der Wert des Attributs.
     * @throws IllegalArgumentException Falls zu der angegebenen Id kein Attributzugriff 
     *     definiert ist.
     */
    public Object get(int id) throws IllegalArgumentException;
    
    /**
     * Setzt den angegebenen Wert zum angegebenen Namen.
     *
     * @param id Die Id des Attributes, das zu setzen ist.
     * @param value Der Wert, der zum Attribut gesetzt werden soll.
     * @throws ClassCastException Falls der angegebene Wert f&uuml;r das Attribut unpassend ist.
     * @throws IllegalArgumentException Falls zu der angegebenen Id kein Attributzugriff 
     *     definiert ist.
     */
    public void set(int id, Object value) throws ClassCastException, IllegalArgumentException;
            
}
