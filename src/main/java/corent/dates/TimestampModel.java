/*
 * TimestampModel.java
 *
 * 06.04.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.dates;


/**
 * Dieses Interface definiert die f&uuml;r die Arbeit mit Timestamps n&ouml;tige 
 * Funktionalit&auml;t.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 14.08.2008 - Erweiterung um die Spezifikation der Methode <TT>getMillis()</TT>.
 *     <P>OLI 02.11.2008 - Erweiterung um die Methode <TT>toDate()</TT>.
 *     <P>
 *
 */
 
public interface TimestampModel {
    
    /** 
     * Addiert den angegebenen Wert zur ebenfalls angebene TimestampUnit. 
     *
     * @param tsu Die TimestampUnit, zu der der angegebene Wert addiert werden soll.
     * @param value Der zu addierende Wert.
     * @return Eine Kopie des PTimestamps mit den neuen Werten.
     */
    public TimestampModel add(TimestampUnit tsu, int value);
    
    /** 
     * Liest den Wert des Timestamps zur angebenen TimestampUnit. 
     *
     * @param tsu Die TimestampUnit, zu der der angegebene Wert gesetzt werden soll.
     * @return Der Wert zur angegebenen TimestampUnit. 
     */
    public int get(TimestampUnit tsu);
    
    /**
     * Liefert die Anzahl der Millisekunden seit dem 01.01.1970 (Unix-Time).
     * Die Implementierung wurde anhand einer Vorarbeit von Volodymyr Medvid durchgef&uuml;hrt.
     *
     * @return Die Anzahl der Millisekunden seit dem 01.01.1970 (Unix-Time).
     *
     * @changed
     *     <P>OLI 14.08.2008 - Hinzugef&uuml;gt.
     *
     */
    public long getMillis();
    
    /** 
     * Setzt den angegebenen Wert f&uuml;r die ebenfalls angebene TimestampUnit. 
     *
     * @param tsu Die TimestampUnit, zu der der angegebene Wert gesetzt werden soll.
     * @param value Der zusetzende Wert.
     * @throws IllegalArgumentException Falls der zusetzende Wert einen ung&uuml;tigen Zustand
     *     im Timestamp hervorrufen w&uuml;rde.
     */
    public void set(TimestampUnit tsu, int value) throws IllegalArgumentException;
    
    /**
     * Liefert ein java.util.Date-Objekt mit dem Wert des TimestampModels.
     *
     * @return Ein java.util.Date-Objekt mit dem Wert des TimestampModels.
     *
     * @changed
     *     OLI 02.11.2008 - Hinzugef&uuml;gt.
     *     <P>
     */
    public java.util.Date toDate();
    
    /** @return Wandelt den PTimestamp in eine Ganzzahl (long) um (Format JJJJMMDDHHMMSS). */
    public long toLong();
    
}
