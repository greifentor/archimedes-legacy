/*
 * DefaultPersistenceDescriptor.java
 *
 * 23.04.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


import java.io.*;
import java.util.*;


/**
 * Diese Implementierung des PersistenceDescriptors kann von den meisten Standardanwendungen 
 * genutzt werden.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public class DefaultPersistenceDescriptor implements PersistenceDescriptor, Serializable {
    
    /*
     * Referenz auf das KLassenobjekt, dessen persistente Instanzen durch den Descriptor 
     * beschrieben werden.
     */
    public Class cls = null;
    /* Eine Liste mit den nach Attribut-Bezeichnern geschl&uuml;sselten ColumnRecords. */
    public Hashtable<Integer, ColumnRecord> columnrecords = 
            new Hashtable<Integer, ColumnRecord>();
    /* 
     * Die Unique-Klausel, nach der die Einzigartigkeit von Objekten gepr&uuml;ft werden soll. 
     */
    public String unique = null;
    /* Eine Liste der Spaltennamen, die beim Speichern nicht leer sein d&uuml;rfen. */
    public Vector<String> notEmptyColumnnames = new Vector<String>();
    /* 
     * Eine Liste mit ColumnRecords, die die Spalten beschreiben, die in Selektionsviews 
     * angezeigt werden sollen.
     */
    public Vector<ColumnRecord> selectionViewMembers = new Vector<ColumnRecord>();
    /* Eine Liste der f&uuml;r das Erzeugen eines Selektionsviews notwendigen Joins. */
    public Vector<JoinDescriptor> selectionJoins = new Vector<JoinDescriptor>();
    /* Eine Liste mit den Spaltennamen f&uuml;r die Selektionsansicht. */
    public Vector<String> selectionViewColumnnames = new Vector<String>();
    
    /**
     * Erzeugt einen DefaultPersistenceDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param cls Eine Klassenreferenz zur Erzeugung neuer Objekte des persistenten Typs.
     * @param columns Eine Liste mit den ColumnRecords, die dem Descriptor zugerechnet werden
     *     sollen.
     * @param selectionViewMembers Eine Liste von ColumnRecords, die die Spalten beschreiben,
     *     die in einem Selektionsview zu sehen sind.
     * @param selectionViewColumnnames Die &Uuml;berschriften f&uuml;r Spalten in der 
     *     Selektionsansicht. 
     * @param selectionJoins Eine Liste der Joins, die beim Bau eines Selektionsviews anzuwenden
     *     sind.
     * @param notEmptyColumnnames Eine Liste der Namen der Spalten, die beim Speichern nicht
     *     leer sein d&uuml;rfen.
     * @param unique Die Unique-Klausel zum Objekt.
     */
    public DefaultPersistenceDescriptor(Class cls, ColumnRecord[] columns, 
            ColumnRecord[] selectionViewMembers, String[] selectionViewColumnnames,
            JoinDescriptor[] selectionJoins, String[] notEmptyColumnnames, String unique) {
        super();
        this.cls = cls;
        for (int i = 0; i < columns.length; i++) {
            columnrecords.put(new Integer(columns[i].getAttribute()), columns[i]);
        }
        for (int i = 0; i < notEmptyColumnnames.length; i++) {
            this.notEmptyColumnnames.addElement(notEmptyColumnnames[i]);
        }
        for (int i = 0; i < selectionViewMembers.length; i++) {
            this.selectionViewMembers.addElement(selectionViewMembers[i]);
        }
        for (int i = 0; i < selectionViewColumnnames.length; i++) {
            this.selectionViewColumnnames.addElement(selectionViewColumnnames[i]);
        }
        for (int i = 0; i < selectionJoins.length; i++) {
            this.selectionJoins.addElement(selectionJoins[i]);
        }
        if ((unique != null) && (unique.length() > 0)) { 
            this.unique = unique;
        }
    }
    

    /* Statische Methoden. */
    
    public static String CreateFilter(PersistenceDescriptor pd, Object[] criteria) {
        String s = "";
        Vector vs = new Vector();
        if (criteria != null) {
            Vector<Integer> vit = pd.getAttributes();
            for (int i = 0, len = vit.size(); i < len; i++) {
                int j = vit.elementAt(i);
                ColumnRecord cr = pd.getColumn(j);
                vs.addElement(cr.getFullname());
            }
            for (int i = 0; i < criteria.length; i++) {
                if (s.length() > 0) {
                    s += " and ";
                }
                s += "(" + DBExec.Concatenation(vs, "; ") + " like '%" + criteria[i].toString() 
                        + "%')";
            }
        }
        return s;
    }
    
    
    /* Implementierung des Interfaces PersistenceDescriptor. */
    
    public Class getFactoryClass() {
        return this.cls;
    }
    
    public Vector<Integer> getAttributes() {
        Vector<Integer> vi = new Vector<Integer>();
        for (Iterator it = this.columnrecords.keySet().iterator(); it.hasNext(); ) {
            vi.addElement((Integer) it.next());
        }
        return vi;
    }

    public Vector<ColumnRecord> getKeys() {
        Vector<ColumnRecord> vcr = new Vector<ColumnRecord>();
        Vector<Integer> vit = this.getAttributes();
        for (int i = 0, len = vit.size(); i < len; i++) {
            ColumnRecord cr = this.getColumn(vit.elementAt(i));
            if (cr.isPkMember()) {
                vcr.addElement(cr);
            }
        }
        return vcr;
    }
    
    public ColumnRecord getColumn(int attr) {
        return this.columnrecords.get(new Integer(attr));
    }

    public ColumnRecord getColumn(String tsn) {
        Vector<Integer> vi = this.getAttributes();
        for (int i = 0, len = vi.size(); i < len; i++) {
            Integer it = vi.elementAt(i);
            ColumnRecord cr = this.getColumn(it.intValue());
            if (cr.getFullname().equals(tsn)) {
                return cr;
            }
        }
        return null;
    }
    
    public String createFilter(Object[] criteria) {
        return CreateFilter(this, criteria);
    }
    
    public Vector<ColumnRecord> getSelectionViewMembers() {
        return new Vector<ColumnRecord>(this.selectionViewMembers);
    }
    
    public Vector<String> getSelectionViewColumnnames() {
        return new Vector<String>(this.selectionViewColumnnames);
    }
    
    public Vector<JoinDescriptor> getSelectionJoins() {
        return new Vector<JoinDescriptor>(this.selectionJoins);
    }
    
    public Vector<String> getNotEmptyColumnnames() {
        return new Vector<String>(this.notEmptyColumnnames);
    }
    
    public String getUniqueClause() {
        return this.unique;
    }
    
}
