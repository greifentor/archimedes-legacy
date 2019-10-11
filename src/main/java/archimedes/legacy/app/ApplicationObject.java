/*
 * ApplicationObject.java
 *
 * 06.07.2005
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.app;


import corent.base.*;
import corent.base.dynamic.*;
import corent.db.*;
import corent.db.xs.*;
import corent.djinn.*;

import java.util.*;


/**
 * Diese Klasse kann als Basis f&uuml;r Applikationen genutzt werden, die deren Datenmodell mit
 * dem Archimedes-Programm erstellt worden ist. Sie bietet Grundfunktionen zum dynamischen 
 * Generieren von Descriptoren und Attributen aus dem Datenmodell.
 * Die Klasse ist abstrakt, weil die Methode createObject aus dem Interface ArchimedesDynamic
 * nicht implementiert worden ist. Dies mu&szlig; in den Subklassen passieren.
 *
 * <TABLE BORDER=1>
 *     <TR VALIGN=TOP>
 *         <TH>Property</TH>
 *         <TH>Default</TH>
 *         <TH>Typ</TH>
 *         <TH>Beschreibung</TH>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>archimedes.app.ApplicationObject.deleted.text</TD>
 *         <TD>&nbsp;(removed)</TD>
 *         <TD>String</TD>
 *         <TD>
 *             Der hier angegebene String wird als gel&ouml:scht gekennzeichneten 
 *             Deactivatable-Implementierungen als Suffix in der <TT>toString()</TT>-Methode
 *             angef&uuml;gt.
 *         </TD>
 *     </TR>
 * </TABLE>
 *
 *
 * @author
 *     ollie
 *     <P>
 *
 * @changed 
 *     OLI 06.10.2007 - Erweiterung der Fehlermeldung in der Methode 
 *             <TT>getEditorDescriptorList()</TT>.
 *     <P>OLI 11.02.2008 - Erweiterung um die Logik, als gel&ouml;scht gekennzeichnete Daten 
 *             auch in der <TT>toString()</TT>-Methode entsprechend zu kennzeichnen.
 *     <P>OLI 11.05.2008 - Erweiterung um die vorbereitende Implementierung der Methode
 *             <TT>isTabEnabled(int)</TT> aus dem Interface <TT>TabbedPane</TT>.
 *
 */

abstract public class ApplicationObject extends DynamicObject implements ArchimedesDynamic, 
        Attributed, Comparable, /* Deactivatable, */ HasKey, PersistentEditable, RemoteDBMember
        {
            
    /** Referenz auf die standardm&auml;&szlig;ige ArchimedesApplication. */  
    public static ArchimedesApplication STANDARDAPP = null;
            
    /** 
     * Die Referenz auf die ArchimedesDescriptorFactory mit der das Objekt zusammenarbeiten 
     * soll. 
     */
    transient protected ArchimedesDescriptorFactory adf = null;
    
    /* Der Name der Tabelle, auf den sich die Klasse im Datenmodell beziehen soll. */
    private String tablename = null;

    /** 
     * Generiert eine Instanz der Klasse anhand der &uuml;bergebenen Parameter.
     *
     * @param adf Die ArchimedesDescriptorFactory, aus der das Objekt seine Descriptoren 
     *     gewinnen soll.
     * @param tn Der Name der Tabelle, auf den sich die Klasse im Datenmodell beziehen soll.
     */
    public ApplicationObject(ArchimedesDescriptorFactory adf, String tn) {
        super();
        this.adf = adf;
        this.tablename = tn;
        if (this.adf != null) {
            this.buildAttributes(this.adf.getDynamicDescriptor(tn));
        }
    }
    
    /**
     * Liefert einen String zur Kennzeichnung von deaktivierten Objekten, sofern diese das 
     * Interface <TT>Deactivatable</TT> implementieren.
     *
     * @return Ein String zur Kennzeichnung von deaktivierten Objekten, der aus der Property
     *     <TT>archimedes.app.ApplicationObject.deleted.text</TT>, falls das Objekt eine 
     *     Implementierung des Interfaces <TT>Deactivatable</TT> ist und als gel&ouml;scht 
     *     gekennzeichnet ist. Sonst wird ein leerer String zur&uuml;ckgeliefert.
     */
    public String removedString() {
        return ((this instanceof Deactivatable) && ((Deactivatable) this).isRemoved() ? 
                Utl.GetProperty("archimedes.app.ApplicationObject.deleted.text", " (removed)"
                ) : "");
    }

    /** 
     * Setzt den &uuml;bergebenen Namen als neuen Namen f&uuml;r die Tabelle ein, auf die sich
     * das ApplicationObject bezieht.
     *
     * @param tn Der neue Tabellenname zum ApplicationObject.
     */
    protected void setTablename(String tn) {
        this.tablename = tn;
    }
    
    /** 
     * Setzt die &uuml;bergebene ArchimedesDescriptorFactory als neue 
     * ArchimedesDescriptorFactory ein.
     *
     * @param adf Die neue ArchimedesDescriptorFactory zum Objekt.
     */
    public void setADF(ArchimedesDescriptorFactory adf) {
        this.adf = adf;
    }
    
    /** @return Der Inhalt der Attribute des Objektes als String. */
    public String getContentToString() {
        List<String> lan = this.getAttributenames();
        SortedVector sv = new SortedVector();
        StringBuffer sb = new StringBuffer();
        for (int i = 0, len = lan.size(); i < len; i++) {
            sv.addElement(lan.get(i));
        }
        for (int i = 0, len = lan.size(); i < len; i++) {
            String s = (String) sv.elementAt(i);
            Object o = this.get(s);
            if (sb.length() > 0) {
                sb.append(", ");
            }
            if (o instanceof List) {
                List l = (List) o;
                StringBuffer sb0 = new StringBuffer();
                for (int j = 0, lenj = l.size(); j < lenj; j++) {
                    Object o0 = l.get(j);
                    if (sb0.length() > 0) {
                        sb0.append(", ");
                    }
                    sb0.append("(").append((o0 instanceof ApplicationObject 
                            ? ((ApplicationObject) o0).getContentToString() : o0.toString())
                            ).append(")");
                }
                o = "{".concat(sb0.toString()).concat("}");
            }
            sb.append(s).append("=").append((o == null ? "null" : o.toString()));
        }
        return sb.toString();
        // return this.attribute.toString();
    }
    

    /* &Uuml;berschreiben von Methoden der Superklasse (Pflichtteil). */

    public boolean equals(Object o) {
        if (!(o instanceof ApplicationObject)) {
            return false;
        }
        if (this.adf == null) {
            throw new NullPointerException("\nERRROR: adf is null. Maybe you haven't "
                    + "overwritten method restoreApplication() while using lists in user "
                    + "classes!");  
        }
        return this.adf.equalsTo(this, (ApplicationObject) o);
    }

    /*
    public int hashCode() {
        int result = 17;
        result = 37 * result + this.getStation();
        return result;
    }
    */

    public String toString() {
        return this.adf.generateString(this) + this.removedString();
    }

    
    /* &Uuml;berschreiben von Methoden der Superklasse (K&uuml;r). */

    public Object alterValueBeforeSet(String attr, Object value) {
        AttributeDescriptor ad = this.getHAD().get(attr);
        if ((value != null) && (ad != null) && ad.isReference()) {
            if (value instanceof HasKey) {
                value = ((HasKey) value).getKey();
            }
        }
        return value;
    }

    /* Implementierung des Interfaces ArchimedesDynamic. */

    public String getTablename() {
        return this.tablename;
    }

    
    /* Implementierung des Interfaces Attributed. */

    public Object get(int id) throws IllegalArgumentException {
        if ((id >= 0) && (id < this.adf.getAttributenames(this.getTablename()).size())) {
            return this.get(this.adf.getAttributenames(this.getTablename()).elementAt(id));
        }
        throw new IllegalArgumentException("Class " + this.getClass().getName() + " hasn't an "
                + "attribute no. " + id + " (get)!");
    }

    public void set(int id, Object value) throws ClassCastException, IllegalArgumentException {
        if ((id >= 0) && (id < this.adf.getAttributenames(this.getTablename()).size())) {
            this.set(this.adf.getAttributenames(this.getTablename()).elementAt(id), value);
            return;
        }
        throw new IllegalArgumentException("Class " + this.getClass().getName() + " hasn't an "
                + "attribute no. " + id + " (set)!");
    }
    
    
    /* Vorbereitende Implementierung zum Interface ColumnViewable. */
    
    public Class getColumnclass(int col) {
        return "".getClass();
    }


    /* Implementierung des Interfaces Comparable. */
    
    public int compareTo(Object o) {
        return 0;
    }
    
    
    /* Vorbereitende Implementierung des Interfaces Deactivatable. */
    
    public ColumnRecord getStatusColumn() {
        return this.getPersistenceDescriptor().getColumn(this.getTablename() + "." +
                System.getProperty("application.Status.columnname", "Geloescht"));
    }
    
    public Object getDeactivatedValue() {
        return Integer.getInteger("application.Status.Deactivated", 1);
    }
    
    public Object getActivatedValue() {
        return Integer.getInteger("application.Status.Activated", 0);
    }
    
    public void activateObject() {
    }
    
    
    /* Implementierung des Interfaces HasKey. */
    
    public Object getKey() {
        PersistenceDescriptor pd = this.getPersistenceDescriptor();
        Vector<ColumnRecord> vpk = pd.getKeys();
        if (vpk.size() > 0) {
            ColumnRecord cr = vpk.elementAt(0); 
            return this.get(cr.getColumnname());
        }
        return null;
    }
    
    public void setKey(Object k) {
        PersistenceDescriptor pd = this.getPersistenceDescriptor();
        Vector<ColumnRecord> vpk = pd.getKeys();
        if (vpk.size() > 0) {
            ColumnRecord cr = vpk.elementAt(0); 
            this.set(cr.getColumnname(), k);
        }
    }
    
    
    /* Implementierung des Interfaces Editable. */

    public PersistenceDescriptor getPersistenceDescriptor() {
        try {
            return this.adf.getPersistenceDescriptor(this.getClass(), this.getTablename());
        } catch (NullPointerException npe) {
            throw new NullPointerException("Maybe you have not initialized the "
                    + "ArchimedesDescriptorFactory object of class " + this.getClass() + ". " 
                    + "try to override method restoreApplication() at parentclass! By: "
                    + npe.getMessage());
        }
    }
    
    /**
     * @changed 
     *     OLI 06.10.2007 - Erweiterung der Fehlermeldung um Hinweis auf eine nicht 
     *             initialisierte Archimedes-Applikation.
     *
     */
    public EditorDescriptorList getEditorDescriptorList() {
        try {
            return this.adf.getEditorDescriptor(this, this.getTablename());
        } catch (NullPointerException npe) {
            npe.printStackTrace();            
            throw new NullPointerException("Maybe you have not initialized the "
                    + "ArchimedesDescriptorFactory object of " + this.getClass() + ". " 
                    + "try to override method restoreApplication() at parentclass! By: "
                    + npe.getMessage() + "\nMaybe you have not initialized an "
                    + "ArchimedesApplication instance otherwise.");
        }
    }

    public Object createObject(Object blueprint) throws ClassCastException {
        if (!this.getClass().isInstance(blueprint)) {
            throw new ClassCastException("Blueprint has not right class. Required:" 
                    + this.getClass().getName() + ". Found:" + (blueprint != null ? 
                    blueprint.getClass().getName() : "null"));
        }
        Vector<String> attributenames = this.adf.getAttributenames(this.getTablename());
        Dynamic d = (Dynamic) this.createObject();
        Dynamic bp = (Dynamic) blueprint;
        for (int i = 0, len = attributenames.size(); i < len; i++) {
            d.set(attributenames.elementAt(i), bp.get(attributenames.elementAt(i)));
        }
        return d;
    }
    
    
    /* Vorbereitende Implementierung des Interfaces Ordered. */
    
    public OrderByDescriptor getOrderByDescriptor() {
        return this.adf.getOrderByDescriptor(this.getPersistenceDescriptor(), this.getTablename(
                ));
    }
    
    
    /* Implementierung des Interfaces RemoteDBMember. */
    
    protected void restoreApplication() {
        this.setADF(STANDARDAPP.getADF());
        this.setHAD(this.adf.getDynamicDescriptor(this.tablename));
    }        
    
    public void objectCreated() {
        this.restoreApplication();
    }
    
    public void objectDuplicated() {
        this.restoreApplication();
    }
    
    public void objectGenerated() {
        this.restoreApplication();
    }
    
    public void objectRed() {
        this.restoreApplication();
    }
    
    public void objectBeforeDuplicate() {
        this.restoreApplication();
    }
    
    public void objectBeforeRemove() {
        this.restoreApplication();
    }
    
    public void objectBeforeWrite() {
        this.restoreApplication();
    }
    
    public void objectBeforeIsUnique() {
        this.restoreApplication();
    }
    
    
    /* Vorbereitende Implementierung des Interfaces TabbedEditable. */
    
    public TabbedPaneFactory getTabbedPaneFactory() {
        return this.adf.getTabbedPaneFactory(this.getTablename());
    }
    
    public boolean isTabEnabled(int no) {
        return true;
    }
    
    
    /* Vorbereitende Implementierung des Interfaces Traceable. */
    
    public void traceChanged() {
        if (this.adf.getApplication() instanceof ArchimedesTracingApplication) {
            ((ArchimedesTracingApplication) this.adf.getApplication()).traceEvent(this, 
                    ArchimedesTracingApplication.TraceType.CHANGED);
        }
    }
    
    public void traceCreated() {
        if (this.adf.getApplication() instanceof ArchimedesTracingApplication) {
            ((ArchimedesTracingApplication) this.adf.getApplication()).traceEvent(this, 
                    ArchimedesTracingApplication.TraceType.CREATED);
        }
    }
    
    public void traceDeleted() {
        if (this.adf.getApplication() instanceof ArchimedesTracingApplication) {
            ((ArchimedesTracingApplication) this.adf.getApplication()).traceEvent(this, 
                    ArchimedesTracingApplication.TraceType.DELETED);
        }
    }
    
    public void traceDuplicated() {
        if (this.adf.getApplication() instanceof ArchimedesTracingApplication) {
            ((ArchimedesTracingApplication) this.adf.getApplication()).traceEvent(this, 
                    ArchimedesTracingApplication.TraceType.DUPLICATED);
        }
    }

}
