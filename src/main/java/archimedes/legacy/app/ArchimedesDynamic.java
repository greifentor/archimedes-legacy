/*
 * ArchimedesDynamic.java
 *
 * 08.07.2005
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.app;


import corent.base.dynamic.*;


/**
 * Mit Hilfe dieses Interfaces werden einige Grundeigenschaften von Objekten definiert, die 
 * als Archimedes-Application-Objekte fungieren sollen.<BR>
 * <HR>
 *
 * @author ollie
 *
 */
 
public interface ArchimedesDynamic extends Dynamic {
    
    /** @return Der Name der Tabelle, mit der das Object verbunden ist. */
    public String getTablename();
    
    /** @return Eine neue Instanz der Klasse des ArchimedesDynamic. */
    public Object createObject();
    
    /**
     * Setzt den &uuml;bergebenen Wert als neuen Wert f&uuml;r das angegebene Attribut ein.
     *
     * @param attr Der Name des Attributes, dessen Wert ge&auml;nder werden soll.
     * @param value Der neue Wert des Attributs zum angegebenen Namen.
     * @param change Wird diese Flagge gesetzt, wird vor dem Setzen des neuen Wertes die Methode
     *     alterValueBeforeSet(String, Object) ausgef&uuml;hrt.
     * @throws ClassCastException falls der &uuml;bergebene Wert nicht zum Typ des Attributs
     *     pa&szlig;t. 
     * @throws IllegalArgumentException falls kein Attribut mit dem angegebenen Namen existiert. 
     */
    public void set(String attr, Object value, boolean change) throws ClassCastException, 
            IllegalArgumentException;

    /**
     * Das &Uuml;berschreiben dieser Methode erlaubt eine Manipulation eines zu setzenden Wertes
     * im Rahmen der set-Methode.
     *
     * @param attr Der Name des Attributes, dessen Wert ge&auml;nder werden soll.
     * @param value Der neue Wert des Attributs zum angegebenen Namen.
     * @return Der eventuell ge&auml;nderte Wert.
     */
    public Object alterValueBeforeSet(String attr, Object value);
    
}
