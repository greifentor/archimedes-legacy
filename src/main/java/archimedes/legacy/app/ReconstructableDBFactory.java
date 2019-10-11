/*
 * HistoricDBFactory.java
 *
 * 23.05.2007
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.app;


import corent.base.*;
import corent.dates.*;
import corent.db.*;
import corent.db.xs.*;

import java.sql.*;
import java.util.*;


/**
 * Eine DBFactory-Implementierung, die die Grundlage f&uuml;r historische Tabellen bilden, 
 * welche rekonstruierbare Datens&auml;tze enthalten.
 *
 * <P>&Uuml;ber das Setzen der Properties <I>archimedes.Archimedes.debug</I> 
 * und <I>archimedes.app.debug</I> kann zus&auml;tzlicher Debug-Ouput zugeschaltet werden.
 *
 * @author 
 *     ollie
 *     <P>
 *
 * @changed 
 *     OLI 27.08.2007 - Erweiterung der Signatur des Kontruktors um eine Referenz auf den 
 *             DBFactoryController, mit dem die DBFactory-Implementierung zusammenarbeiten soll.
 *     <P>OLI 21.01.2008 - Debugging des L&ouml;schens f&uuml;r Reconstructables in der Methode
 *             <TT>remove(T, boolean, Connection)</TT>.
 *     <P>OLI 11.02.2008 - Erweiterungen um Rahmen der Einf&uuml;hrung von Historienanzeigen. 
 *     <P>OLI 15.02.2008 - Erweiterung um das Umsetzen der Active-Flagge in der 
 *             Vorg&auml;ngerrevision des Datensatzes in der Methode <TT>write(T, Connection, 
 *             LongPTimestamp)</TT>.
 *     <P>OLI 02.06.2008 - Beheben des Bugs beim Duplizieren von Datens&auml;tzen in der Methode
 *             <TT>duplicate(T, Connection)</TT>.
 *     <P>OLI 04.06.2008 - Erweiterung um konfigurierbare Debug-Outputs in der Methode 
 *             <TT>read(String, Connection, OrderByDescriptor, boolean)</TT>
 *     <P>OLI 16.07.2008 - &Auml;nderungen an der <TT>write</TT>-Methode im Rahmen der 
 *             &Auml;nderung des Interfaces <TT>DBFactory</TT>
 *     <P>OLI 15.10.2008 - Ausblenden der Aktiv-Flaggenzur&uuml;cksetzung, wenn der Name
 *             der Aktivflaggenspalte gleich <TT>null</TT> ist (Methode <TT>write(T, Connection,
 *             LongPTimestamp)</TT>).
 *     <P>OLI 29.01.2009 - Erweiterung um die Methode <TT>getSelectionView(String, String, 
 *             Connection, boolean)</TT>. Zur&uuml;cksetzung der Methode 
 *             <TT>getSelectionView(String, String, Connection)</TT> auf den Status 
 *             "deprecated".
 *     <P>
 *
 */

abstract public class ReconstructableDBFactory<T extends ApplicationObject & 
        corent.db.xs.Reconstructable> extends DefaultDBFactory<T> implements GenerateExpander {

    /**
     * Generiert eine Instanz der Klasse mit Defaultwerten.
     *
     * @param dbfc Eine Referenz auf den DBFactoryController, an den die DefaultDBFactory 
     *     gekoppelt ist.
     * @param adf Die ArchimedesDescriptorFactory, aus der die Instanz ihre Konfiguration 
     *     beziehen soll.
     *
     * @changed
     *     OLI 27.08.2007 - Erweiterung der Parameterliste um die DBFactoryController-Referenz
     *             <TT>dbfc</TT>.
     *
     */
    public ReconstructableDBFactory(DBFactoryController dbfc, ArchimedesDescriptorFactory adf) {
        super(dbfc, adf);
    }
    
    public Object doAction(Connection c, int id, Object... p) throws IllegalArgumentException,
            SQLException {
        if (id == 0) {
            SortedVector vlpts = new SortedVector();
            ResultSet rs = DBExec.Query(c, "select " + this.getTimestampColumnname() + " from " 
                    + this.getTablename() + " where " + this.getReferenceColumnname() + "=" 
                    + p[0].toString());
            while (rs.next()) {
                long l = rs.getLong(1);
                if (!rs.wasNull() && (l > 0)) {
                    try {
                        vlpts.addElement(new LongPTimestamp(l));
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
            DBExec.CloseQuery(rs);
            return vlpts;
        }
        throw new IllegalArgumentException("id " + id + " is not valid for "
                + "ReconstructableDBFactory.doAction(Connection, int, Object...).");
    }
    
    /**
     * @changed
     *     OLI 02.06.2008 - Einbau des Setzens der ObjectNumber auf den Wert -1. Hierdurch wird
     *             erreicht, das diese beim Speichern erzeugt wird. Anders geht das nicht, weil
     *             sonst Objekte mit falschen ObjectNumbers produziert werden. 
     *
     */
	public T duplicate(T o, Connection c) throws SQLException {
        T o0 = super.duplicate(o, c);
        // OLI 02.06.2008 - Das hier ist extrem wichtig, sonst gibt's Chaos beim Duplizieren.
        o0.setObjectnumber(-1);
        if (!Boolean.getBoolean("corent.db.xs.mode.central") && (o0 instanceof CentrallyHeld)) {
            ((CentrallyHeld) o0).setGLI(-1);
        }
        return o0;
    }
    
    public T generate(Connection c) throws java.sql.SQLException {
        T o = this.create();
        if (!Boolean.getBoolean("corent.db.xs.mode.central") && (o instanceof CentrallyHeld)) {
            ((CentrallyHeld) o).setGLI(-1);
        }
        return o;
    }

    public String getAdditionalPreselection() {
        return "";
    }
    
    public Vector getHistorySelectionList(Connection c, long objno, String addfieldlist, 
            String[] addfieldprefices) throws SQLException {
        String addinfo = "";
        Vector vlpts = new Vector();
        ResultSet rs = DBExec.Query(c, "select " + this.getTimestampColumnname() +
                (addfieldlist != null && addfieldlist.length() > 0 ? ", " + addfieldlist : "") 
                + " from " + this.getTablename() + " where " + this.getReferenceColumnname() 
                + "=" + objno + " order by Timestamp desc");
        int columncount = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            long l = rs.getLong(1);
            addinfo = "";
            for (int i = 2; i <= columncount; i++) {
                if (addinfo.length() > 0) {
                    addinfo = addinfo.concat("; ");
                }
                addinfo = addinfo.concat((addfieldprefices.length >= i-2 ? addfieldprefices[i-2]
                        : "")).concat(rs.getString(i)); 
            }
            if (!rs.wasNull() && (l > 0)) {
                try {
                    vlpts.addElement(new LongPTimestampSelection(l, addinfo));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        DBExec.CloseQuery(rs);
        return vlpts;
    }
    
    public String getActiveColumnname() {
        return System.getProperty("archimedes.app.ReconstructableDBFactory.column.active", 
                "Aktiv");
    }
    
    public String getIdColumnname() {
        return this.getTablename();
    }

    public String getModifiedAtColumnname() {
        return System.getProperty("archimedes.app.ReconstructableDBFactory.column.modifiedat", 
                "GeaendertAm");
    }

    public String getReferenceColumnname() {
        return System.getProperty("archimedes.app.ReconstructableDBFactory.column."
                + "referencenumber", "Referenznummer");
    }
    
    @Deprecated
    /**
     * @deprecated
     *     OLI 29.01.2009 - Zugunsten der Methode <TT>getSelectionView(String, String, 
     *              Connection, boolean)</TT>.
     *
     */
    public SelectionTableModel getSelectionView(String w, String aj, Connection c) 
            throws java.sql.SQLException {
        return this.getSelectionView(w, aj, c, false);
    }
    
    /**
     * @changed
     *     OLI 29.01.2009 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public SelectionTableModel getSelectionView(String w, String aj, Connection c, 
            boolean suppressFilling) throws java.sql.SQLException {
        if (w.endsWith("$")) {
            w = w.substring(0, w.length()-1);
        } else {
            String presel = "(" + this.getTablename() + "." + this.getModifiedAtColumnname() 
                    + "=-1 or " + this.getTablename() + "." + this.getModifiedAtColumnname()
                    + " is null)";
            if (this.getAdditionalPreselection().length() > 0) {
                presel = "(".concat(presel).concat(" and ").concat(
                        this.getAdditionalPreselection()).concat(")");
            }
            if ((w != null) && (w.length() > 0)) {
                w = w.concat(" and ").concat(presel);
            } else {
                w = presel;
            }
        }
        return super.getSelectionView(w, aj, c, suppressFilling);
    }
    
    public String getDeletedColumnname() {
        return this.getTablename() + "." + System.getProperty(
                "archimedes.app.ReconstructableDBFactory.column.deleted", "Geloescht");
    }
    
    abstract public String getTablename();
    
    public String getTimestampColumnname() {
        return "ZeitpunktBearbeitung";
    }
    
    public boolean isUnique(T o, Connection c) throws SQLException {
        String u = o.getPersistenceDescriptor().getUniqueClause();
        if (u != null) {
            try {
                String us = DBFactoryUtil.UniqueStatement(o) + " and (" 
                        + this.getTablename() + "." + this.getModifiedAtColumnname() + "=-1 or "
                        + this.getTablename() + "." + this.getModifiedAtColumnname() 
                        + " is null)";
                ResultSet rs = DBExec.Query(c, us);
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count == 0) {
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }    

    public Vector<T> read(String w, Connection c, OrderByDescriptor o, boolean ir) 
            throws SQLException {
        boolean debug = Boolean.getBoolean("archimedes.Archimedes.debug") 
                || Boolean.getBoolean("archimedes.app.debug"); 
        if (debug) {
            System.out.println("ReconstructableDBFactory.read: " + w + ", " + ir);
        }
        if ((w != null) && (w.endsWith("$"))) {
            w = w.substring(0, w.length()-1);
        } else {
            T instance = this.create(); 
            String presel = "(" + this.getTablename() + "." + this.getModifiedAtColumnname() 
                    + "=-1 or " + this.getTablename() + "." + this.getModifiedAtColumnname()
                    + " is null)";
            if (this.getAdditionalPreselection().length() > 0) {
                presel = "(".concat(presel).concat(" and ").concat(
                        this.getAdditionalPreselection()).concat(")");
            }
            if ((w != null) && (w.length() > 0)) {
                w = w.concat(" and ").concat(presel);
            } else {
                w = presel;
            }
            if (!ir) {
                if (debug) {
                    System.out.print("    extended for taking out removed records to: ");
                }
                if ((w != null) && (w.length() > 0)) {
                    w = "(".concat(w).concat(") and ");
                }
                w = w.concat(this.getDeletedColumnname()).concat("=").concat(instance instanceof
                        Deactivatable ? (((Deactivatable) instance).getActivatedValue()
                        ).toString() : "0");
                if (debug) {
                    System.out.println(w);
                }
            }
        }
        return super.read(w, c, o, ir);
    }
    
    public Vector<T> read(String w, Connection c, OrderByDescriptor o) 
            throws SQLException {
        return this.read(w, c, o, false);
    }
    
    public Vector<T> read(String w, Connection c) throws SQLException {
        return this.read(w, c, null, false);
    }
    
    public T write(T o, Connection c) throws SQLException {
        return this.write(o, c, null);
    }
    
    /**
     * @changed
     *     OLI 15.02.2008 - Erweiterung um das Umsetzen der Active-Flagge in der 
     *             Vorg&auml;ngerrevision des Datensatzes.
     *     <P>OLI 15.10.2008 - Ausblenden der Aktiv-Flaggenzur&uuml;cksetzung, wenn der Name
     *             der Aktivflaggenspalte gleich <TT>null</TT> ist.
     *     <P>
     */
    public T write(T o, Connection c, LongPTimestamp lpts) throws SQLException {
        long id = (long) o.getId();
        if (id < 1) {
            o.setTimestamp(new LongPTimestamp());
            o.setModificationOf(o.getTimestamp());
            DBFactoryUtil.Generate(o, c, this);
            return o;
        }
        if (lpts == null) {  
            lpts = new LongPTimestamp();
        }
        if (o.getTimestamp().toLong() != LongPTimestamp.NULL.toLong()) {
            DBExec.Update(c, "update " + this.getTablename() + " set " 
                    + this.getModifiedAtColumnname() + "=" + lpts.toLong() + 
                    (this.getActiveColumnname() != null ? ", " + this.getActiveColumnname() 
                    + "=0" : "") + " where " + this.getIdColumnname() + "=" + id);
        }
        DBFactoryUtil.Generate(o, c, this);
        if (o.getModificationOf().toLong() == LongPTimestamp.NULL.toLong()) {
            o.setModificationOf(o.getTimestamp());
        }
        o.setModifiedAt(LongPTimestamp.NULL);
        o.setTimestamp(lpts);
        return super.write(o, c);
    }
    
    /**
     * @changed
     *     OLI 21.01.2008 - Debugging des L&ouml;schens f&uuml;r Reconstructables. Vorher sind 
     *             die Datens&auml;tze immer physisch aus der Datenbank entfernt worden.
     */
    public void remove(T o, boolean forced, Connection c) throws SQLException {
        if (!forced) {
            if (o instanceof Deletable) {
                ((Deletable) o).setDeleted(true);
            }
            this.write(o, c);
        } else {
            super.remove(o, forced, c);
        }
    }
    
    
    /* Implementierung des Interfaces GenerateExpander. */
    
    public Object doChangeKeys(long id) {
        return new Long(id);
    }

    public Object doGenerateExpansion(Object obj) {
        T t = (T) obj;
        if (t.getObjectnumber() < 1) {
            t.setObjectnumber(t.getId());
        }
        return t;
    }
    
}
