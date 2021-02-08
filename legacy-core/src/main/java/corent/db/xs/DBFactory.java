/* 
 * DBFactory.java
 *
 * 22.02.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;


import corent.db.*;

import java.sql.*;
import java.util.*;


/**
 * Diese Interface beschreibt die notwendige Funktionalit&auml;t f&uuml;r eine DBFactory. Mit 
 * Hilfe einer solchen Factory k&ouml;nnen Objekte eines bestimmten Typs in einer Datenbank
 * persistiert werden.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed 
 *     OLI 27.08.2007 - Erweiterung um die Methoden <TT>getDBFactoryController()</TT>. Zudem
 *             sind die Methoden alphabetisch geordnet worden.
 *     <P>OLI 16.07.2008 - Ab&auml;nderung der <TT>write(T, Connection)</TT>-Methode in der 
 *             Form, da&szlig; die geschriebenen Objekte zur&uuml;ckgegeben werden. Dies ist 
 *             vorallem f&uuml;r Verbesserungen im Reconstructable-Umfeld n&ouml;tig; auch wenn
 *             es in der einen oder anderen Implementierung zu &Auml;nderungsaufwand f&uuml;hrt.
 *     <P>OLI 29.01.2009 - Erweiterung um die Signatur der Methode <TT>getSelectionView(String, 
 *             String, Connection, boolean)</TT>. Die Methode <TT>getSelectionView(String, 
 *             String, Connection)</TT> wird daf&uuml;r zur&uuml;ckgebaut.
 *     <P>
 *
 */
 
public interface DBFactory<T> {
    
    /**
     * Erzeugt ein leeres Objekt des Typs T.
     *
     * @return Das neue Objekt vom Typ T.
     */
    public T create();
    
    /**
     * Erzeugt eine geeignete Where-Klausel zur Schnell-Suche auf der mit der DBFactory 
     * verbundenen Tabelle.
     *
     * @param criteria Die in der Schnellsuche spezifizierten Teilstrings.
     */
    public String createFilter(Object[] criteria);
    
    /**
     * F&uuml;hrt eine Aktion zur angegeben Id durch und liefert ein Object mit einem 
     * eventuellen Ergebnis zur&uuml;ck. Hierdurch k&ouml;nnen Aktionen auf die Datenbank 
     * implementiert werden, die mit den obenstehenden Operationen nicht korrespondieren.
     *
     * @param c Die Connection, &uuml;ber die die Aktion ausgef&uuml;hrt werden soll. 
     * @param id Die Id der Aktion, die ausgef&uuml;hrt werden soll. Sie wird von der jeweiligen 
     *         Implementierung festgelegt.
     * @param p Eine Liste mit Parametern zur Aktion.
     * @return Ein eventuelles Ergebnis, oder <TT>null</TT>, wenn es kein solches geben sollte.
     * @throws IllegalArgumentException Falls entweder die Parameter nicht stimmig sind oder 
     *         eine ung&uuml;ltige Id angegeben wurde.
     * @throws SQLException falls beim Duplizieren des Objektes ein Fehler auftreten sollte.
     */ 
    public Object doAction(Connection c, int id, Object... p) throws IllegalArgumentException, 
            SQLException;

    /**
     * Mit Hilfe dieser Methode wird ein Objekt vom Typ T dupliziert. Prinzipiell wird das 
     * Objekt einfach mit einem neuen Schl&uuml;ssel versorgt.
     *
     * @param o Das Objekt, das Dupliziert werden soll.
     * @param c Die Connection, &uuml;ber die mit der Datenbank kommuniziert werden soll.
     * @throws SQLException falls beim Duplizieren des Objektes ein Fehler auftreten sollte.
     */
    public T duplicate(T o, Connection c) throws SQLException;
    
    /**
     * Mit Hilfe dieser Methode wird ein neues Objekt vom Typ T generiert.
     *
     * @param c Die Connection, &uuml;ber die mit der Datenbank kommuniziert werden soll.
     * @throws SQLException falls beim Erzeugen des Objektes ein Fehler auftreten sollte.
     */
    public T generate(Connection c) throws SQLException;
    
    /**
     * Liefert eine Referenz auf den DBFactoryController, zu dem die DBFactory geh&ouml;rt.
     *
     * @return Der DBFactoryController, zu dem die DBFactory geh&ouml;rt.
     */
    public DBFactoryController getDBFactoryController();

    @Deprecated    
    /**
     * Liefert ein SelectionTableModel mit einem View, &uuml;ber den Objekte des angegebenen 
     * Typs selektiert werden k&ouml;nnen.
     *
     * @param w Eine Where-Klausel, mit der der SelectionView eingeschr&auml;nkt werden kann.
     * @param aj Zus&auml;tzliche Join-Klausel zum View.
     * @param c Die Connection, &uuml;ber die mit der Datenbank kommuniziert werden soll.
     * @throws SQLException falls beim Duplizieren des Objektes ein Fehler auftreten sollte.
     *
     * @deprecated
     *     OLI 29.01.2009 - Zugunsten der um den Parameter "suppressFilling" Funktion 
     *             herausgenommen.
     *     <P>
     */
    public SelectionTableModel getSelectionView(String w, String aj, Connection c) 
            throws SQLException;
    
    /**
     * Liefert ein SelectionTableModel mit einem View, &uuml;ber den Objekte des angegebenen 
     * Typs selektiert werden k&ouml;nnen.
     *
     * @param w Eine Where-Klausel, mit der der SelectionView eingeschr&auml;nkt werden kann.
     * @param aj Zus&auml;tzliche Join-Klausel zum View.
     * @param c Die Connection, &uuml;ber die mit der Datenbank kommuniziert werden soll.
     * @param suppressFilling Diese Flagge mu&szlig; gesetzt werden, wenn die Methode ein leeres
     *         <TT>SelectionTableModel</TT> ohne Datenzugriff auf die Datenbank liefern soll.
     *         Dies wird, gerade bei Tabellen mit gro&szlig;en Zahlen von Datens&auml;tzen,
     *         n&ouml;tig sein.
     * @throws SQLException falls beim Duplizieren des Objektes ein Fehler auftreten sollte.
     *
     * @changed
     *     OLI 29.01.2009 - Auf Basis der Methode <TT>getSelectionView(String, String, 
     *             Connection)</TT> hinzugef&uuml;gt.
     *     <P>
     *
     */
    public SelectionTableModel getSelectionView(String w, String aj, Connection c, 
            boolean suppressFilling) throws SQLException;
    
    /**
     * Pr&uuml;ft, ob das &uuml;bergebene Objekt in seiner gegenw&auml;rtigen Konstellation
     * in der Datenbank einzigartig ist.
     *
     * @param o Das Objekt, das aus der Datenbank, welches auf Einzigartigkeit gepr&uuml;ft 
     *         werden soll.
     * @param c Die Connection mit der Datenquelle, &uuml;ber die die Pr&uuml;fung 
     *         durchgef&uuml;hrt werden soll.
     * @return <TT>true</TT>, falls das Objekt einzigartig ist, andernfalls <TT>false</TT>.
     * @throws SQLException falls bei der Pr&uuml;fung des Objekts ein Fehler auftreten sollte.
     */
    public boolean isUnique(T o, Connection c) throws SQLException;
    
    /**
     * Liest eine Liste von Objekte des angegebenen Typs aus der Datenquelle.
     *
     * @param w Eine Where-Klausel, die f&uuml;r eine Selektion der Liste sorgt, bzw. <TT>null
     *     </TT>, falls die alle Objekte aus der Datenquelle gelesen werden sollen.
     * @param c Die Datenquelle, aus der die Objekte gelesen werden sollen.
     * @param o Ein alternativer OrderByDescriptor, nach dem die Ergebnisliste sortiert werden
     *         soll, oder <TT>null</TT>, wenn die Defaultsortierung benutzt werden soll.
     * @param includeRemoved Wird diese Flagge gesetzt, werden auch gel&ouml;schte 
     *         Datens&auml;tze ber&uuml;cksichtigt, falls es sich bei der angegebenen Klasse um 
     *         ein Deactivatable handelt.
     * @throws SQLException falls beim Lesen der Objekte ein Fehler auftritt.
     */
    public Vector<T> read(String w, Connection c, OrderByDescriptor o, boolean includeRemoved) 
            throws SQLException; 
    
    /**
     * L&ouml;scht ein Objekt aus der Datenbank bzw. markiert es als gel&ouml;scht.
     *
     * @param o Das Objekt, das aus der Datenbank gel&ouml;scht werden soll.
     * @param forced Diese Flagge ist zu setzen, wenn das Objekt in jedem Fall physisch aus der
     *         der Datenbank entfernt werden soll (auch wenn es Deactivatable implementiert).
     * @param c Die Connection mit der Datenquelle, aus der das Objekt entfernt werden soll.
     * @throws SQLException falls beim L&ouml;schen des Objekts ein Fehler auftreten sollte.
     */
    public void remove(T o, boolean forced, Connection c) throws SQLException;
    
    /**
     * L&ouml;scht eine Menge von Objekte aus der Datenbank bzw. markiert sie als gel&ouml;scht.
     *
     * @param k Die Schl&uuml;ssel der Datens&auml;tze, die gel&ouml;scht bzw. entsprechend 
     *         markiert werden sollen.
     * @param forced Diese Flagge ist zu setzen, wenn die Objekte in jedem Fall physisch aus der
     *         der Datenbank entfernt werden sollen (auch wenn es Deactivatable implementiert).
     * @param c Die Connection mit der Datenquelle, aus der das Objekt entfernt werden soll.
     * @throws SQLException falls beim L&ouml;schen des Objekts ein Fehler auftreten sollte.
     */
    public void removeBatch(Vector k, boolean forced, Connection c) throws SQLException;
    
    /**
     * Diese Methode schreibt den Inhalt des &uuml;bergebenen Objektes in die Datenbank.
     *
     * @param o Das Objekt, das in die Datenbank geschrieben werden soll.
     * @param c Die Connection mit der Datenquelle, in die das Objekt &uuml;bertragen werden
     *         soll.
     * @return Das geschriebene Objekt (im Normalfall o).
     * @throws SQLException falls beim &Uuml;bertragen der Objekt-Inhalte ein Fehler auftreten 
     *         sollte.
     *
     * @changed
     *     OLI 16.08.2008 - &Auml;nderung des R&uuml;ckgabewertes auf das geschriebene Objekt.
     *             Diese &Auml;nderung ist besonders im Umfeld der Reconstructables 
     *             gewinnbringend.
     *
     */
    public T write(T o, Connection c) throws SQLException;

    /**
     * Diese Methode aktualisiert die Datens&auml;tze mit den angegebenen Schl&uuml;sseln.
     *
     * @param k Die Schl&uuml;ssel der Datens&auml;tze, die aktualisiert werden sollen.
     * @param ta Die Felder, die bei der mit der DBFactory verbundenen Tabelle zu mit den 
     *         dazugeh&ouml;rigen Werten aktualisiert werden sollen (Hashtable&lt;Integer, 
     *         Object&gt; - AttributeId &amp; Wert). 
     * @param c Die Connection mit der Datenquelle, in die das Objekt &uuml;bertragen werden
     *         soll.
     * @throws SQLException falls beim &Uuml;bertragen der Objekt-Inhalte ein Fehler auftreten 
     *         sollte.
     */
    public void writeBatch(Vector k, Hashtable<Integer, Object> ta, Connection c) 
            throws SQLException;

}
