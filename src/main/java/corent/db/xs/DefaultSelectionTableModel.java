/* 
 * DefaultSelectionTableModel.java
 *
 * 27.08.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;


import corent.dates.*;

import java.util.*;

import javax.swing.table.*;


/**
 * Mithilfe dieser Erweiterung des DefaultTableModels, l&auml;&szlig;t sich auf einfache Weise
 * ein SelectionTableModel implementieren. Hier&uuml;ber wird jede Anzeigezeile mit einem 
 * Schl&uuml;ssel versehen, der explizit abgefragt werden kann, aber in der Anzeige des 
 * TableModels nicht erscheint.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 25.06.2008 - Erweiterung um die Methode <TT>removeKey(int)</TT>.
 *
 */
 
public class DefaultSelectionTableModel extends DefaultTableModel 
        implements SelectionTableModel {
          
    /* 
     * Diese Flagge mu&szlig; gesetzt werden, wenn die Menge der gefundenen Datens&auml;tze 
     * g&ouml;&szlig;er als ein vorgegebenes Limit ist.
     */
    private boolean morethanlimit = false;
    /* Die Liste mit den zu den Zeilen assoziierten Objekten. */
    private Vector keys = new Vector();
    /* Die Liste mit alternativen Klassen f&uuml;r die Anzeige in der Tabelle. */
    private Hashtable<Integer, Class> alternativeClasses = new Hashtable<Integer, Class>();
    /* Eine Liste mit den als kodiert zu behandelnden Tabellenspalten. */
    private Vector<Integer> coded = new Vector<Integer>();

    /** Erzeugt ein DefaultSelectionTableModel mit Defaultwerten. */
    public DefaultSelectionTableModel() {
        super();
    }
    
    /**
     * Erzeugt ein DefaultSelectionTableModel mit den angegebenen Parametern. Die Zellen der 
     * hierdurch erzeugten Tabelle sind leer, der Wert ihres Inhaltes ist <TT>null</TT>.
     *
     * @param rowCount Die Anzahl der zu erzeugenden Zeilen.
     * @param columnCount Die Anzahl der zu erzeugenden Spalten.
     */
    public DefaultSelectionTableModel(int rowCount, int columnCount) {
        super(rowCount, columnCount);
        this.keys = new Vector(rowCount);
    }
    
    /**
     * Erzeugt ein DefaultSelectionTableModel mit den angegebenen Parametern.
     *
     * @param keys Ein Feld mit Key-Objekten zu den Datenzeilen.
     * @param data Die an die Tabellenstruktur zu &uuml;bergebenden Daten. Der erste Index ist
     *     Zeile.
     * @param columnNames Titel f&uuml;r die Spalten.
     * @throws ArrayIndexOutOfBoundsException Wenn die Anzahl der Key-Objekte ungleich der 
     *     Zeilenzahl ist.
     */
    public DefaultSelectionTableModel(Object[] keys, Object[][] data, Object[] columnNames) 
            throws ArrayIndexOutOfBoundsException {
        super(data, columnNames);
        if (keys.length != data.length) {
            throw new ArrayIndexOutOfBoundsException("Number of keys and number of data records"
                    + " doesn't match!");
        }
        for (int i = 0; i < keys.length; i++) {
            this.keys.addElement(keys[i]);
        }
    }

    /**    
     * Erzeugt ein DefaultSelectionTableModel mit den angegebenen Parametern. Die Zellen der 
     * hierdurch erzeugten Tabelle sind leer, der Wert ihres Inhaltes ist <TT>null</TT>.
     *
     * @param columnNames Titel f&uuml;r die Spalten.
     * @param rowCount Die Anzahl der zu erzeugenden Zeilen.
     */
    public DefaultSelectionTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }
    
    /**    
     * Erzeugt ein DefaultSelectionTableModel mit den angegebenen Parametern. Die Zellen der 
     * hierdurch erzeugten Tabelle sind leer, der Wert ihres Inhaltes ist <TT>null</TT>.
     *
     * @param columnNames Titel f&uuml;r die Spalten.
     * @param rowCount Die Anzahl der zu erzeugenden Zeilen.
     */
    public DefaultSelectionTableModel(Vector columnNames, int rowCount) {
        super(columnNames, rowCount);
    }
    
    /**
     * Erzeugt ein DefaultSelectionTableModel mit den angegebenen Parametern.
     *
     * @param keys Ein Feld mit Key-Objekten zu den Datenzeilen.
     * @param data Die an die Tabellenstruktur zu &uuml;bergebenden Daten. Der erste Index ist
     *     Zeile.
     * @param columnNames Titel f&uuml;r die Spalten.
     * @throws ArrayIndexOutOfBoundsException Wenn die Anzahl der Key-Objekte ungleich der 
     *     Zeilenzahl ist.
     */
    public DefaultSelectionTableModel(Vector keys, Vector data, Vector columnNames) 
            throws ArrayIndexOutOfBoundsException {
        super(data, columnNames);
        if (keys.size() != data.size()) {
            throw new ArrayIndexOutOfBoundsException("Number of keys and data records doesn't "
                    + "match!");
        }
        this.keys = new Vector(keys);
    }
    
    /**
     * Erzeugt ein DefaultSelectionTableModel mit den angegebenen Parametern.
     *
     * @param rows Vector mit SelectionTableRow-Objekten.
     * @param columnNames Titel f&uuml;r die Spalten.
     */
    public DefaultSelectionTableModel(Vector rows, Vector columnNames) {
        super();
        this.load(rows);
    }
    
    /** 
     * Bel&auml;dt das Model mit den Daten der SelectionTableRows.
     *
     * @param rows Vector mit SelectionTableRow-Objekten.
     */
    public void load(Vector rows) {
        for (int i = this.getRowCount()-1; i >= 0; i--) {
            this.removeRow(i);
        }
        this.keys = new Vector();
        for (int i = 0, len = rows.size(); i < len; i++) {
            SelectionTableRow str = (SelectionTableRow) rows.elementAt(i);
            this.addKey(str.getKeys());
            this.addRow(str.getData());
        }
    }
    
    /**
     * H&auml;ngt den angegebenen Schl&uuml;ssel an die Liste der mit den Tabellenzeilen 
     * assoziierten Schl&uuml;sselobjekte an.
     *
     * @param key Der anzuh&auml;ngende Schl&uuml;ssel.
     */
    public void addKey(Object key) {
        this.keys.addElement(key);
    }
    
    /**
     * F&uuml;gt den angegebenen Schl&uuml;ssel in die Liste der mit den Tabellenzeilen 
     * assoziierten Schl&uuml;sselobjekte an der &uuml;bergebenen Position ein.
     *
     * @param key Der einzuf&uuml;gende Schl&uuml;ssel.
     * @param pos Die Position, an der der Schl&uuml;ssel eingef&uuml;gt werden soll.
     */
    public void insertKeyAt(Object key, int pos) {
        this.keys.insertElementAt(key, pos);
    }
    
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
    public void removeKey(int pos) {
        this.keys.removeElementAt(pos);
    }
    
    /**
     * Setzt den angegebenen Schl&uuml;ssel in der Liste der mit den Tabellenzeilen 
     * assoziierten Schl&uuml;sselobjekte an der &uuml;bergebenen Position ein.
     *
     * @param key Der einzuf&uuml;gende Schl&uuml;ssel.
     * @param pos Die Position, an der der Schl&uuml;ssel eingef&uuml;gt werden soll.
     */
    public void setKeyAt(Object key, int pos) {
        this.keys.setElementAt(key, pos);
    }
    
    /**
     * Setzt die &uuml;bergebene Klasse als alternativ anzuwendende Klasse f&uuml;r die Anzeige
     * der anegegebenen Spalte in der Tabelle ein.
     *
     * @param col Die Nummer der Spalte, f&uuml;r die die alternative Klasse genutzt werden 
     *     soll.
     * @param cls Die Klasse, die als Alternative f&uuml;r die angegebene Spalte genutzt werden
     *     soll.
     */
    public void setClassForColumn(int col, Class cls) {
        this.alternativeClasses.put(col, cls);
    }
    
    
    /* Ueberschreiben von Methoden der Superklasse. */
    
    public Class getColumnClass(int col) {
        Class alt = this.alternativeClasses.get(col);
        if (alt != null) {
            return alt;
        }
        return "".getClass();
    }
    
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
    public Object getValueAt(int row, int col) {
        Object o = super.getValueAt(row, col);
        Class alt = this.alternativeClasses.get(col);
        if (alt != null) {
            try {
                if (alt.getName().equals("java.lang.Boolean")) {
                    Object o0 = new Boolean(o.toString().equalsIgnoreCase("true"));
                    if (!o0.equals(Boolean.TRUE)) {
                        try {
                            int i0 = Integer.parseInt(o.toString());
                            if (i0 != 0) {
                                o0 = Boolean.TRUE;
                            }
                        } catch (Exception e0) {
                        }
                    }
                    return o0;
                } else if (alt.getName().equals("corent.dates.PDate")) {
                    try {
                        int i0 = Integer.parseInt(o.toString());
                        if (i0 == -1) {
                            return PDate.UNDEFINIERT;
                        }
                        return new PDate(i0);
                    } catch (Exception e0) {
                    }
                } else if (alt.getName().equals("corent.dates.PTime")) {
                    try {
                        int i0 = Integer.parseInt(o.toString());
                        return new PTime(i0);
                    } catch (Exception e0) {
                    }
                } else if (alt.getName().equals("corent.dates.PTimestamp")) {
                    try {
                        long l = Long.parseLong(o.toString());
                        if (l == -1) {
                            return PTimestamp.NULL;
                        }
                        return new PTimestamp(l);
                    } catch (Exception e0) {
                    }
                } else if (alt.getName().equals("corent.dates.LongPTimestamp")) {
                    try {
                        long l = Long.parseLong(o.toString());
                        if (l == -1) {
                            return LongPTimestamp.NULL;
                        }
                        return new LongPTimestamp(l);
                    } catch (Exception e0) {
                    }
                }
            } catch (Exception e) {
            }
            return o;
        }
        return o;
    }
    
    
    /* Implementierung des Interfaces SelectionTableModel. */
    
    public Object getKey(int row) throws ArrayIndexOutOfBoundsException{
        if ((row < 0) || (row >= this.keys.size())) {
            throw new ArrayIndexOutOfBoundsException("Key does not exists for line " + row + "!"
                    );
        }
        return this.keys.elementAt(row);
    }
    
    public boolean isMoreThanLimit() {
        return this.morethanlimit;
    }
    
    public void setMoreThanLimit(boolean b) {
        this.morethanlimit = b;
    }
        
    public boolean isCoded(int n) {
        return this.coded.contains(n);
    }
    
    public void setCoded(int n, boolean b) {
        if (b) {
            if (!this.coded.contains(n)) {
                this.coded.addElement(n);
            }
        } else {
            if (this.coded.contains(n)) {
                this.coded.removeElement(n);
            }
        }
    }
    
    public void setColumnIdentifiers(Object[] cn) {
        super.setColumnIdentifiers(cn);
    }
    
    
}
