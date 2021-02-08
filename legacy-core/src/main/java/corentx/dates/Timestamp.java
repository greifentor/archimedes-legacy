/*
 * Timestamp.java
 *
 * 21.07.2009
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.dates;


import java.util.*;


/**
 * Dies ist das grundlegende Interface f&uuml;r die meisten Klassen des
 * <TT>corentx.dates</TT>-Package.
 *
 * <P><I><B>Hinweis:</B> Die Implementierungen des Interfaces sollten nicht ver&auml;nderlich
 * durchgef&uuml;hrt, d. h. der Zustand eines einmal instanzierten kann nachher nicht mehr
 * ge&auml;ndert werden.</I>
 *
 * @author O.Lieshoff
 *
 * @changed OLI 21.07.2009 - Hinzugef&uuml;gt
 * @changed OLI 05.08.2009 - Ab&auml;nderung der Methodenspezifikationen, soda&szlig; auch die
 *         numerischen Parameter auf den Typ R geeicht werden.
 * @changed OLI 07.08.2009 - Erweiterung um das Interface Cloneable.
 * @changed OLI 03.09.2009 - Erweiterung um die Spezifikation der Methode <TT>toDate()</TT>.
 *
 */

public interface Timestamp<T extends Timestamp> extends Cloneable, Comparable {

    /**
     * Addiert den angegebenen Wert zum Wert des Zeitobjektes zur angegebenen Zeiteinheit.
     *
     * @param tsu Die Zeiteinheit (z. B. Sekunde, Tag, Jahr) zu der der Wert des Zeitobjektes
     *         geliefert werden soll.
     * @param units Die Anzahl der Zeiteinheiten, die zum Wert des Zeitobjektes in der
     *         angegebenen Zeiteinheit addiert werden soll (ist der &uuml;bergebene Wert
     *         negativ, werden die Einheiten abgezogen).
     * @return Ein neues Zeitobjekt mit dem gegen&uuml;ber dem vorliegenden Zeitobjekt
     *         ge&auml;nderten Wert.
     * @throws IllegalArgumentException Falls die Zeiteinheit f&uuml;r die angesprochene
     *         Implementierung des Timestamps nicht g&uuml;tig ist (z. B., wenn ein Zeitobjekt
     *         nur ein Datum mit Tag, Monat und Jahr bereitstellt und versucht wird ein Wert
     *         f&uuml;r Stunden zu lesen).
     * @throws NullPointerException Falls die Zeitheit (<TT>tsu</TT>) oder die Anzahl der zu
     *         addierenden Einheiten als <TT>null</TT>-Referenz &uuml;bergeben wird.
     * 
     * @precondition tsu != <TT>null</TT>
     * @precondition units != <TT>null</TT>
     */
    public T add(TimestampUnit tsu, long units) throws IllegalArgumentException,
            NullPointerException;

    /**
     * Legt ein Duplikat nach den Regeln der <TT>Object.clone()</TT>-Methode an.
     *
     * @return Ein Duplikat des vorliegenden Objektes nach den Regeln der
     *         <TT>Object.clone()</TT>-Methode
     *
     * @changed OLI 07.08.2009 - Hinzugef&uuml;gt.
     */
    public T clone() throws CloneNotSupportedException;

    /**
     * Liefert einen formatierten String basierend auf den &uuml;bergebenen Parametern.
     *
     * @param styleDate Der <TT>DateFormatStyle</TT> f&uuml;r die Formatierung des Datums. Bei
     *         Implementierungen, in denen die Datumsangabe ber&uuml;cksichtigt wird.
     * @param styleTime Der <TT>DateFormatStyle</TT> f&uuml;r die Formatierung der Uhrzeit. Bei
     *         Implementierungen, in denen die Zeitangabe ber&uuml;cksichtigt wird.
     * @param locale Das Locale, zu dem die Formatierung stattfinden soll.
     * @throws IllegalArgumentException Falls ein Style gesetzt ist, der f&uuml;r die
     *         Timestampimplementierung nicht ausgewertet werden kann.
     * @throws NullPointerException Falls das Locale oder die notwendigen Stilangaben als
     *         <TT>null</TT>-Referenz &uuml;bergeben werden.
     *
     * @precondition locale != <TT>null</TT>
     * @precondition styleDate != <TT>null</TT>, sofern Datumsangaben in der
     *         Zeitstempelimplementierung relevant sind.
     * @precondition styleTime != <TT>null</TT>, sofern Uhrzeitangaben in der
     *         Zeitstempelimplementierung relevant sind.
     *
     * @changed OLI 03.09.2009 - Hinzugef&uuml;gt.
     */
    public String format(DateFormatStyle styleDate, DateFormatStyle styleTime, Locale locale)
            throws IllegalArgumentException, NullPointerException;

    /**
     * Liefert den Wert des Zeitobjektes zur angegebenen Zeiteinheit.
     *
     * @param tsu Die Zeiteinheit (z. B. Sekunde, Tag, Jahr) zu der der Wert des Zeitobjektes
     *         geliefert werden soll.
     * @return Der Wert des Zeitstempels zur angegebenen Zeiteinheit.
     * @throws IllegalArgumentException Falls die Zeiteinheit f&uuml;r die angesprochene
     *         Implementierung des Timestamps nicht g&uuml;tig ist (z. B., wenn ein Zeitobjekt
     *         nur ein Datum mit Tag, Monat und Jahr bereitstellt und versucht wird ein Wert
     *         f&uuml;r Stunden zu lesen).
     * @throws NullPointerException Falls die Zeitheit (<TT>tsu</TT>) als <TT>null</TT>-Referenz
     *         &uuml;bergeben wird.
     * 
     * @precondition tsu != <TT>null</TT>
     */
    public long get(TimestampUnit tsu) throws IllegalArgumentException, NullPointerException;

    /**
     * Setzt den Wert des Zeitobjektes zur angegebenen Zeiteinheit.
     *
     * <P><I><B>Hinweis:</B> Die Implementierung dieser Methode sollte gew&auml;hrleisten,
     * da&szlig; sich der Inhalt des vorliegenden Zeitobjektes nicht ver&auml;ndert.</I>
     *
     * @param tsu Die Zeiteinheit (z. B. Sekunde, Tag, Jahr) zu der der Wert des Zeitobjektes
     *         auf den angegebenen Wert ge&auml;ndert werden soll.
     * @param value Der neue Wert f&uuml;r die angegebene Zeiteinheit.
     * @return Ein neues Zeitobjekt mit dem gegen&uuml;ber dem vorliegenden Zeitobjekt
     *         ge&auml;nderten Wert.
     * @throws IllegalArgumentException Falls die Zeiteinheit f&uuml;r die angesprochene
     *         Implementierung des Timestamps nicht g&uuml;tig ist (z. B., wenn ein Zeitobjekt
     *         nur ein Datum mit Tag, Monat und Jahr bereitstellt und versucht wird ein Wert
     *         f&uuml;r Stunden zu lesen)<BR>ODER falls ein f&uuml;r die angebene Zeiteinheit
     *         ung&uuml;ltiger Wert angegeben wird.
     * @throws NullPointerException Falls die Zeitheit (<TT>tsu</TT>) oder der zu setzende Wert
     *         als <TT>null</TT>-Referenz &uuml;bergeben wird.
     * 
     * @precondition tsu != <TT>null</TT>
     * @precondition value != <TT>null</TT>
     */
    public T set(TimestampUnit tsu, long value) throws IllegalArgumentException,
            NullPointerException;

    /**
     * Setzt den Wert des Zeitobjektes zur angegebenen Zeiteinheit ohne Kontrolle auf
     * Validit&auml;t.
     *
     * @param tsu Die Zeiteinheit (z. B. Sekunde, Tag, Jahr) zu der der Wert des Zeitobjektes
     *         auf den angegebenen Wert ge&auml;ndert werden soll.
     * @param value Der neue Wert f&uuml;r die angegebene Zeiteinheit.
     * @return Ein neues Zeitobjekt mit dem gegen&uuml;ber dem vorliegenden Zeitobjekt
     *         ge&auml;nderten Wert.
     * @throws IllegalArgumentException Falls die Zeiteinheit f&uuml;r die angesprochene
     *         Implementierung des Timestamps nicht g&uuml;tig ist (z. B., wenn ein Zeitobjekt
     *         nur ein Datum mit Tag, Monat und Jahr bereitstellt und versucht wird ein Wert
     *         f&uuml;r Stunden zu lesen)
     * @throws NullPointerException Falls die Zeitheit (<TT>tsu</TT>) oder der zu setzende Wert
     *         als <TT>null</TT>-Referenz &uuml;bergeben wird.
     * 
     * @precondition tsu != <TT>null</TT>
     * @precondition value != <TT>null</TT>
     */
    T setUnchecked(TimestampUnit tsu, long value) throws IllegalArgumentException,
            NullPointerException;

    /**
     * Wandelt den Zeitstempel im ein Date-Objekt um.
     *
     * @return Ein Date-Objekt mit den Inhalten des Zeitstempels.
     *
     * @changed OLI 03.09.2009 - Hinzugef&uuml;gt.
     */
    public Date toDate();

    /**
     * Wandelt den Zeitstempel in einen passenden Long-Wert um. Die Zahl kann eine variierende
     * L&auml;nge haben. Bei reinen Datumsangaben z. B. JJJJMMTT, bei Zeitstempeln mit Uhrzeit
     * JJJJMMTTHHMMSS.
     *
     * @return Eine Ganzzahldarstellung zum Zeitstempel.
     */
    public long toLong();

    /**
     * Pr&uuml;ft, ob die angegebene Zahlenkolonne in dem &uuml;bergebenen Feld die Parameter
     * f&uuml;r einen validen Zeitstempel darstellen.
     *
     * @param p Ein Feld von Zahlen, die gepr&uuml;ft werden sollen, ob sie ein valides Datum
     *         ergeben (z. B. tag, monat, jahr f&uuml;r einen Zeitstempel ohne Uhrzeitangabe).
     * @return <TT>true</TT>, falls die Zahlen des Feldes in der angegebenen Reihenfolge als
     *         Grundlage f&uuml;r den Bau eines Datums herhalten k&ouml;nnen, bzw.
     *         <TT>false</TT>, wenn dies nicht der Fall ist.
     * @throws IllegalArgumentException Falls eine unpassende Zahl von Parametern &uuml;bergeben
     *         worden ist.
     * @throws NullPointerException Falls die Parameterliste als <TT>null</TT>-Referenz
     *         &uuml;bergeben wird.
     *
     * @precondition Es gibt eine passende Zahl von Parametern und die Parameterliste wird nicht
     *         als <TT>null</TT>-Referenz &uuml;bergeben.
     *
     * @changed OLI 11.08.2009 - Hinzugef&uuml;gt.
     */
    public boolean validate(long... p) throws IllegalArgumentException, NullPointerException;

}