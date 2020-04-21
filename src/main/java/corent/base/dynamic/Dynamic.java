/*
 * Dynamic.java
 *
 * 05.04.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.base.dynamic;


/**
 * Dieses Interface definiert das Verhalten eines dynamischen Objektes. Seine Attribute
 * m&uuml;ssen nicht fest im Quelltext definiert werden, sondern k&ouml;nnen auch extern
 * konfigurierbar gemacht werden.
 * <P>Dies erm&ouml;glicht es, inperformante und zudem stilistisch unsch&ouml;ne Zugriffe
 * &uuml;ber Reflections zur vermeiden.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed OLI 05.04.2005 - Hinzugef&uuml;gt.
 * @changed OLI 09.06.2009 - Formatanpassungen.
 *
 */
 
public interface Dynamic {

    /**
     * Mit Hilfe dieser Methode wird das Attribut mit dem angegebenen Namen gelesen.
     *
     * @param attr Der Name des Attributes, der gelesen werden soll.
     * @return Der Wert des Attributs zum angegebenen Namen.
     * @throws IllegalArgumentException Falls kein Attribut mit dem angegebenen Namen existiert.
     * @throws NullPointerException Falls der Name des Attributs als Null-Referenz
     *         &uuml;bergeben wird.
     *
     * @precondition attr != null
     *
     */
    public Object get(String attr) throws IllegalArgumentException;

    /**
     * Liefert die Klasse des Java-Typs, unter dem das angegebene Attribut gespeichert wird.
     *
     * @param attr Der Name des Attributes, zu dem die Typ-Klasse geliefert werden soll.
     * @return Die Klasse, unter der das Attribut in dem dynamischen Object gespeichert wird.
     * @throws ClassCastException Falls der &uuml;bergebene Wert nicht zum Typ des Attributs
     *         pa&szlig;t.
     * @throws IllegalArgumentException Falls kein Attribut mit dem angegebenen Namen existiert.
     * @throws NullPointerException Falls der Name des Attributs als Null-Referenz
     *         &uuml;bergeben wird.
     *
     * @precondition attr != null
     *
     */
    public Class getType(String attr) throws ClassCastException, IllegalArgumentException;

    /**
     * Setzt den &uuml;bergebenen Wert als neuen Wert f&uuml;r das angegebene Attribut ein.
     *
     * @param attr Der Name des Attributes, dessen Wert ge&auml;nder werden soll.
     * @param value Der neue Wert des Attributs zum angegebenen Namen.
     * @throws ClassCastException Falls der &uuml;bergebene Wert nicht zum Typ des Attributs
     *         pa&szlig;t.
     * @throws IllegalArgumentException Falls kein Attribut mit dem angegebenen Namen existiert.
     * @throws NullPointerException Falls der Name des Attributs als Null-Referenz
     *         &uuml;bergeben wird.
     *
     * @precondition attr != null
     *
     */
    public void set(String attr, Object value) throws ClassCastException,
            IllegalArgumentException;

}
