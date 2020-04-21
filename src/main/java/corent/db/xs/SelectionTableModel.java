/*
 * SelectionTableModel.java
 *
 * 27.08.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;


import javax.swing.table.*;


/**
 * Diese Erweiterung des TableModels dient der Nutzung innerhalb des DBFactory-Umfeldes, um 
 * Selectionsview in Tabellenform unterzubringen. Neben den anzuzeigenden Daten m&uuml;ssen hier
 * auch die Schl&uuml;ssel der mit den einzelnen Tabellenzeilen assoziierten Objekte vorgehalten
 * werden, um deren Selektion &uuml;ber das Model zu erm&ouml;glichen.
 * 
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 25.06.2008 - Erweiterung um die Methode <TT>removeKey(int)</TT>.
 *
 */
 
public interface SelectionTableModel extends TableModel {
    
    /**
     * Liefert den Schl&uuml;ssel des Objektes, das mit der angegebenen Tabellenzeile assoziiert
     * ist.
     *
     * @param row Die Tabellenzeile, zu dem der Schl&uuml;ssel des assoziierten Objektes 
     *     ermittelt werden soll.
     * @throws ArrayIndexOutOfBoundsException Wenn die eine Zeile abgefragt wird, die es nicht 
     *     gibt.
     */
    public Object getKey(int row) throws ArrayIndexOutOfBoundsException;
    
    /**
     * Liefert den Wert <TT>true</TT>, wenn die angegebenen Spalte des SelectionTableModels
     * als kodierte Spalte behandelt werden soll.
     *
     * @param n Die Nummer der Spalte, die auf Kodierung &uuml;berpr&uuml;ft werden soll.
     * @return <TT>true</TT>, falls die Spalte als kodierte Spalte behandelt werden soll.
     */
    public boolean isCoded(int n);
    
    /** 
     * @return <TT>true</TT>, wenn mehr als durch das Limit der Suchoperation definierte 
     *     Datens&auml;tze gefunden worden sind.
     */
    public boolean isMoreThanLimit();
    
    /**
     * L&ouml;scht den angegebenen Schl&uuml;ssel, aud der Liste der mit den Tabellenzeilen
     * assoziierten Schl&uuml;sselobjekten an der angegebenen Position.
     *
     * @param pos Die Position, an der der Schl&uuml;ssel eingef&uuml;gt werden soll.
     *
     * @changed
     *     OLI 25.06.2008 - Hinzugef&uuml;gt.
     *
     */
    public void removeKey(int pos);
    
    /**
     * Setzt oder L&ouml;scht die Kodierungsflagge der angegebenen Spalte des 
     * SelectionTableModels.
     *
     * @param n Die Nummer der Spalte, die auf Kodierung &uuml;berpr&uuml;ft werden soll.
     * @param b Diese Flagge sollte gesetzt werden, falls die Spalte als kodierte Spalte 
     *     behandelt werden soll.
     */
    public void setCoded(int n, boolean b);
    
    /**
     * Setzt die &uuml;bergebenen Spaltennamen als neue Namen f&uuml;r die Spalten des Models
     * ein. Es sei angeraten, die Anzahl der Spaltennamen an die Anzahl der Spalten des Models
     * anzupassen.
     *
     * @param cn Ein Array mit den Spaltennamen zum Model.
     */
    public void setColumnIdentifiers(Object[] cn);
    
    /** 
     * Setzt die Flagge zur Anzeige von mehr gefundenen als durch ein definiertes Limit 
     * erw&uuml;nschten Datens&auml;tzen auf den angegebenen Wert.
     *
     * @param b Der neu f&uuml;r die beschriebene Flagge zu setzende Wert.
     */
    public void setMoreThanLimit(boolean b);
    
}   
