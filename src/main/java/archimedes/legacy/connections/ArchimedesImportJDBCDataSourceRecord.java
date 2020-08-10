/*
 * ArchimedesImportJDBCDataSourceRecord.java
 *
 * 17.04.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package archimedes.legacy.connections;


import corent.base.*;
import corent.db.*;
import corent.djinn.*;
import corent.files.*;


/**
 * Diese Erweiterung des JDBCDataSourceRecord dient der Konfiguration von Import eines Diagramms
 * aus einer Datenbank.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 * @deprecated OLI 15.06.2016 - Should be exchanged by a new class with same task based on class
 *         "DatabaseConnection".
 *
 */

@Deprecated public class ArchimedesImportJDBCDataSourceRecord extends JDBCDataSourceRecord {
    
    /** Eine Instanz zur Benutzung als Blueprint. */
    public static final ArchimedesImportJDBCDataSourceRecord INSTANCE = 
            new ArchimedesImportJDBCDataSourceRecord();
    
    /** Id f&uuml;r den Zugriff auf Flagge &uuml;ber den Domainimport. */
    public static final int ID_DOMAINS = 6;
    /** Id f&uuml;r den Zugriff auf Flagge &uuml;ber die Wiederherstellung der Referenzen. */
    public static final int ID_REFERENZEN = 7;

    /* 
     * Diese Flagge mu&szlig; gesetzt werden, wenn beim Import Domains aus dem DBMS 
     * &uuml;bernommen werden sollen. 
     */
    private boolean domains = false;
    /* 
     * Diese Flagge mu&szlig; gesetzt werden, wenn beim Import Referenzen ermittelt werden 
     * sollen. 
     */
    private boolean referenzen = false;
    
    /* Generiert eine Instanz mit Defaultwerten. */
    public ArchimedesImportJDBCDataSourceRecord() {
        super();
    }
    
    /* Generiert eine Instanz als Kopie der uebergebenen Instanz. */
    public ArchimedesImportJDBCDataSourceRecord(ArchimedesImportJDBCDataSourceRecord ajdbcdsr
            ) {
        super();
        for (int i = 0; i <= 7; i++) {
            this.set(i, ajdbcdsr.get(i));
        }
    }
    
    /**
     * Generiert einen JDBCDataSourceRecord anhand der &uuml;bergebenen Parameter.
     *
     * @param driver Der Name der Driverklasse, &uuml;ber die auf die Datenquelle zugegriffen
     *     werden soll.
     * @param dbname Der Name der Datenbank, auf die &uuml;ber die Datenquelle zugegriffen 
     *     werden soll.
     * @param user Der Name, unter dem der Benutzer der Datenquelle bekannt ist.
     * @param password Das Passwort des Users.
     * @param domains Diese Flagge mu&szlig; gesetzt werden, wenn beim Import die Domains des
     *     DBMS &uuml;bernommen werden sollen.
     * @param referenzen Diese Flagge mu&szlig; gesetzt werden, wenn beim Import der Versuch 
     *     unternommen werden soll, Referenzen in das Diagramm zu &uuml;bernehmen.
     */
    public ArchimedesImportJDBCDataSourceRecord(String driver, String dbname, String user, 
            String password, boolean domains, boolean referenzen) {
        super();
        this.setDriver(driver);
        this.setDBName(dbname);
        this.setUser(user);
        this.setPassword(password);
        this.setDomains(domains);
        this.setReferenzen(referenzen);
    }
    
    
    /* Accessoren und Mutatoren. */
    
    /**
     * Accessor f&uuml;r die Eigenschaft Domains.
     * 
     * @return Der Wert der Eigenschaft Domains.
     */
    public boolean isDomains() {
        return this.domains;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft Domains.
     *
     * @param domains Der neue Wert der Eigenschaft Domains.
     */
    public void setDomains(boolean domains) {
        this.domains = domains;
    }
        
    /**
     * Accessor f&uuml;r die Eigenschaft Referenzen.
     * 
     * @return Der Wert der Eigenschaft Referenzen.
     */
    public boolean isReferenzen() {
        return this.referenzen;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft Referenzen.
     *
     * @param referenzen Der neue Wert der Eigenschaft Referenzen.
     */
    public void setReferenzen(boolean referenzen) {
        this.referenzen = referenzen;
    }
        
    public String getXMLAttributes() {
        StringBuffer sb = new StringBuffer().append(super.toXMLAttributes());
        sb.append("hasdomains=\"").append(StrUtil.ToHTML("" + this.isDomains())).append("\" ");
        sb.append("setreferences=\"").append(StrUtil.ToHTML("" + this.isReferenzen())).append(
                "\" ");
        return sb.toString();
    }
    
    
    /* Ueberschreiben der Methoden der Superklasse. */
    
    public boolean equals(Object o) {
        if (!(o instanceof ArchimedesImportJDBCDataSourceRecord)) {
            return false;
        }
        ArchimedesImportJDBCDataSourceRecord dsr = (ArchimedesImportJDBCDataSourceRecord) o;
        return super.equals(dsr) && (this.isDomains() == dsr.isDomains()) && (this.isReferenzen(
                ) == dsr.isReferenzen());
    }
    
    public int hashCode() {
        int result = super.hashCode();
        result = 37 * result + (this.isDomains() ? 1 : 0);
        result = 37 * result + (this.isReferenzen() ? 1 : 0);
        return result;
    }
    
        
    /* Implementierung des Interfaces Attributed. */
    
    public Object get(int id) throws IllegalArgumentException {
        switch (id) {
        case ID_DOMAINS:
            return new Boolean(this.isDomains());
        case ID_REFERENZEN:
            return new Boolean(this.isReferenzen());
        }
        return super.get(id);
    }
   
    public void set(int id, Object value) throws ClassCastException, IllegalArgumentException {
        switch (id) {
        case ID_DOMAINS:
            this.setDomains(((Boolean) value).booleanValue());
            return;
        case ID_REFERENZEN:
            this.setReferenzen(((Boolean) value).booleanValue());
            return;
        }
        super.set(id, value);
    }
    
    
    /* Implementierung des Interfaces Editable. */
    
    public EditorDescriptorList getEditorDescriptorList() {
        DefaultComponentFactory dcf = DefaultComponentFactory.INSTANZ;
        DefaultEditorDescriptorList dedl = 
                (DefaultEditorDescriptorList) super.getEditorDescriptorList();
        DefaultLabelFactory dlf = DefaultLabelFactory.INSTANZ;
        dedl.addElement(new DefaultEditorDescriptor(0, this, ID_DOMAINS, dlf, dcf, 
                StrUtil.FromHTML("Domains &uuml;bernehmen"), 'O', null, StrUtil.FromHTML(
                "Mu&szlig; gesetzt sein, wenn die Domains aus dem DBMS &uuml;bernommen werden "
                + "sollen.")));
        dedl.addElement(new DefaultEditorDescriptor(0, this, ID_REFERENZEN, dlf, dcf, 
                StrUtil.FromHTML("Referenzen &uuml;ber Namen herstellen"), 'R', null, 
                StrUtil.FromHTML("Mu&szlig; gesetzt sein, wenn versucht werden soll, die "
                + "Referenzen nicht aus dem Datenschema, sondern aus Namenskonventionen zu "
                + "&uuml;bernehmen.")));
        return dedl;
    }
    
    public Object createObject() {
        ArchimedesImportJDBCDataSourceRecord jdbcdsr = new ArchimedesImportJDBCDataSourceRecord(
                );
        jdbcdsr.setDBName("");
        jdbcdsr.setDriver("");
        jdbcdsr.setUser("");
        jdbcdsr.setPassword("");
        return jdbcdsr;
    }
    
    public Object createObject(Object blueprint) throws ClassCastException {
        return new ArchimedesImportJDBCDataSourceRecord((ArchimedesImportJDBCDataSourceRecord) 
                blueprint);
    }

        
    /* Implementierung des Interfaces STFStoreable. */
    
    public void toSTF(StructuredTextFile stf, String[] path) {
        super.toSTF(stf, path);
        stf.writeStr(StructuredTextFile.AddPath(path, "Domains"), new Boolean(
                this.isDomains()).toString());
        stf.writeStr(StructuredTextFile.AddPath(path, "Referenzen"), new Boolean(
                this.isReferenzen()).toString());
    }
    
    public void fromSTF(StructuredTextFile stf, String[] path) {
        super.fromSTF(stf, path);
        this.setDomains(new Boolean(stf.readStr(StructuredTextFile.AddPath(path, "Domains"
                ), "FALSE")).booleanValue());
        this.setReferenzen(new Boolean(stf.readStr(StructuredTextFile.AddPath(path, "Referenzen"
                ), "FALSE")).booleanValue());
    }
                
}
