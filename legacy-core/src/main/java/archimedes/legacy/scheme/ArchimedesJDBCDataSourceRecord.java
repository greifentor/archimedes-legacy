/*
 * ArchimedesJDBCDataSourceRecord.java
 *
 * 14.04.2004
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.scheme;


import corent.base.*;
import corent.db.*;
import corent.djinn.*;
import corent.files.*;

import java.util.*;


/**
 * Diese Erweiterung des JDBCDataSourceRecord dient der Konfiguration von Import und Abgleich 
 * des Diagramms aus bzw. mit der Datenbank.
 *
 * @author ollie
 *
 * @changed
 *     OLI 29.08.2007 - Der PostgreSQL-Modus wird nun auch innerhalb der Methoden 
 *             <TT>fromSTF(StructuredTextFile stf, String[] path)</TT> und
 *             <TT>toSTF(StructuredTextFile stf, String[] path)</TT> ber&uuml;cksichtigt.
 *
 */

public class ArchimedesJDBCDataSourceRecord extends JDBCDataSourceRecord {
    
    /** Eine Instanz zur Benutzung als Blueprint. */
    public static final ArchimedesJDBCDataSourceRecord INSTANCE = 
            new ArchimedesJDBCDataSourceRecord();
    
    /** Id f&uuml;r den Zugriff auf die Domainf&auml;higkeit des Records. */
    public static final int ID_DOMAINFAEHIG = 6;
    /** Id f&uuml;r den Zugriff auf die FkNotNullBeachten-Flagge des Records. */
    public static final int ID_FKNOTNULLBEACHTEN = 7;
    /** Id f&uuml;r den Zugriff auf die ReferenzenSetzen-Flagge des Records. */
    public static final int ID_REFERENZENSETZEN = 8;
    /** Id f&uuml;r den Zugriff auf den DBMode des Records. */
    public static final int ID_DBMODE = 9;
    /** Id f&uuml;r den Zugriff das Quote-Zeichen. */
    public static final int ID_QUOTE_CHARACTER = 10;

    /* Diese Flagge mu&szlig; gesetzt werden, wenn das betrachtete DBMS domainf&auml;hig ist. */
    private boolean domainfaehig = false;
    /* 
     * Diese Flagge mu&szlig; gesetzt werden, wenn bei Foreignkeys die NOTNULL-Klausel in das 
     * Datenschema des DBMS &uuml;bertragen werden soll. 
     */
    private boolean fknotnullbeachten = false;
    /* 
     * Diese Flagge mu&szlig; gesetzt werden, wenn Foreignkeys mit Reference-Klauseln erzeugt 
     * werden sollten.
     */
    private boolean referenzensetzen = false;
    /* Der DBMode, unter dem die beschriebene Connection betrieben werden soll. */
    private DBExecMode mode = null;
    /* Das Zeichen, mit dem Feld- und Tabellennamen geklammert werden sollen. */
    private String quoteCharacter = "";

    /* Generiert eine Instanz mit Defaultwerten. */
    protected ArchimedesJDBCDataSourceRecord() {
        super();
        switch (Integer.getInteger("archimedes.scheme.ArchimedesJDBCDataSourceRecord.DBMode", 0)
                ) {
        case 0:
            this.mode = DBExecMode.MYSQL;
            break; 
        case 1:
            this.mode = DBExecMode.MSSQL;
            break;
        case 2:
            this.mode = DBExecMode.HSQL;
            break;
        }     
    }
    
    /* Generiert eine Instanz als Kopie der &uuml;bergebenen Instanz. */
    protected ArchimedesJDBCDataSourceRecord(ArchimedesJDBCDataSourceRecord ajdbcdsr) {
        this();
        for (int i = 0; i <= 8; i++) {
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
     * @param quoteCharacter Ein Zeichen zum Klammern der Feld- und Tabellennamen.
     */
    public ArchimedesJDBCDataSourceRecord(String driver, String dbname, String user, 
            String password, String quoteCharacter) {
        this();
        this.setDriver(driver);
        this.setDBName(dbname);
        this.setUser(user);
        this.setPassword(password);
        this.setQuoteCharacter(quoteCharacter);
    }


    /* Accessoren und Mutatoren. */

    /**
     * Accessor f&uuml;r die Eigenschaft Domainfaehig.
     * 
     * @return Der Wert der Eigenschaft Domainfaehig.
     */
    public boolean hasDomains() {
        return this.domainfaehig;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft Domainfaehig.
     *
     * @param domainfaehig Der neue Wert der Eigenschaft Domainfaehig.
     */
    public void setDomains(boolean domainfaehig) {
        this.domainfaehig = domainfaehig;
    }
            
    /**
     * Accessor f&uuml;r die Eigenschaft FkNotNullBeachten.
     * 
     * @return Der Wert der Eigenschaft FkNotNullBeachten.
     */
    public boolean isFkNotNullBeachten() {
        return this.fknotnullbeachten;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft FkNotNullBeachten.
     *
     * @param fknotnullbeachten Der neue Wert der Eigenschaft FkNotNullBeachten.
     */
    public void setFkNotNullBeachten(boolean fknotnullbeachten) {
        this.fknotnullbeachten = fknotnullbeachten;
    }
        
    /**
     * Accessor f&uuml;r die Eigenschaft ReferenzenSetzen.
     * 
     * @return Der Wert der Eigenschaft ReferenzenSetzen.
     */
    public boolean isReferenzenSetzen() {
        return this.referenzensetzen;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft ReferenzenSetzen.
     *
     * @param referenzensetzen Der neue Wert der Eigenschaft ReferenzenSetzen.
     */
    public void setReferenzenSetzen(boolean referenzensetzen) {
        this.referenzensetzen = referenzensetzen;
    }

    /**
     * Accessor f&uuml;r die Eigenschaft Mode.
     * 
     * @return Der Wert der Eigenschaft Mode.
     */
    public DBExecMode getMode() {
        return this.mode;
    }

    /**
     * Liefert das Zeichen, mit dem Feld- und Tabellennamen eingeklammert werden sollen.
     * 
     * @return Das Zeichen, mit dem Feld- und Tabellennamen eingeklammert werden sollen.
     */
    public String getQuoteCharacter() {
        return this.quoteCharacter;
    }

    /**
     * Mutator f&uuml;r die Eigenschaft Mode.
     *
     * @param mode Der neue Wert der Eigenschaft Mode.
     */
    public void setMode(DBExecMode mode) {
        this.mode = mode;
    }

    /**
     * Setzt eine neues Zeichen als Klammer f&uuml;r Feld- und Tabellennamen.
     *
     * @param newQuoteCharacter Das neue Klammerzeichen f&uuml;r Feld- und Tabellennamen.
     */
    public void setQuoteCharacter(String newQuoteCharacter) {
        this.quoteCharacter = newQuoteCharacter;
    }


    /* Ueberschreiben der Methoden der Superklasse. */
    
    public boolean equals(Object o) {
        if (!(o instanceof ArchimedesJDBCDataSourceRecord)) {
            return false;
        }
        ArchimedesJDBCDataSourceRecord dsr = (ArchimedesJDBCDataSourceRecord) o;
        return super.equals(dsr) && (this.hasDomains() == dsr.hasDomains()) 
                && (this.isFkNotNullBeachten() == dsr.isFkNotNullBeachten())
                && (this.isReferenzenSetzen() == dsr.isReferenzenSetzen())
                && (this.getMode() == dsr.getMode());
    }
    
    public int hashCode() {
        int result = super.hashCode();
        result = 37 * result + (this.hasDomains() ? 1 : 0);
        result = 37 * result + (this.isFkNotNullBeachten() ? 1 : 0);
        result = 37 * result + (this.isReferenzenSetzen() ? 1 : 0);
        return result;
    }
    
        
    /* Implementierung des Interfaces Attributed. */
    
    public Object get(int id) throws IllegalArgumentException {
        switch (id) {
        case ID_DOMAINFAEHIG:
            return new Boolean(this.hasDomains());
        case ID_FKNOTNULLBEACHTEN:
            return new Boolean(this.isFkNotNullBeachten());
        case ID_REFERENZENSETZEN:
            return new Boolean(this.isReferenzenSetzen());
        case ID_DBMODE:
            return this.getMode();
        case ID_QUOTE_CHARACTER:
            return this.getQuoteCharacter();
        }
        return super.get(id);
    }
   
    public void set(int id, Object value) throws ClassCastException, IllegalArgumentException {
        switch (id) {
        case ID_DOMAINFAEHIG:
            this.setDomains(((Boolean) value).booleanValue());
            return;
        case ID_FKNOTNULLBEACHTEN:
            this.setFkNotNullBeachten(((Boolean) value).booleanValue());
            return;
        case ID_REFERENZENSETZEN:
            this.setReferenzenSetzen(((Boolean) value).booleanValue());
            return;
        case ID_DBMODE:
            this.setMode((DBExecMode) value);
            return;
        case ID_QUOTE_CHARACTER:
            this.setQuoteCharacter(String.valueOf(value));
            return;
        }
        super.set(id, value);
    }
    
    
    /* Implementierung des Interfaces Editable. */
    
    public EditorDescriptorList getEditorDescriptorList() {
        DefaultComponentFactory dcf = DefaultComponentFactory.INSTANZ;
        DefaultEditorDescriptorList dedl = (DefaultEditorDescriptorList) 
                super.getEditorDescriptorList();
        Vector v = new Vector();
        v.addElement(DBExecMode.MYSQL);
        v.addElement(DBExecMode.MSSQL);
        v.addElement(DBExecMode.HSQL);
        v.addElement(DBExecMode.POSTGRESQL);
        DefaultComponentFactory dcfmodes = new DefaultComponentFactory(v);
        DefaultLabelFactory dlf = DefaultLabelFactory.INSTANZ;
        dedl.addElement(new DefaultEditorDescriptor(0, this, ID_DBMODE, dlf, dcfmodes, 
                "DB-Mode", 'B', null, StrUtil.FromHTML("Hier k&ouml;nnen Sie die Art der "
                + "Datenbank einstellen.")));
        dedl.addElement(new DefaultEditorDescriptor(0, this, ID_DOMAINFAEHIG, dlf, dcf, 
                "Domains", 'M', null, StrUtil.FromHTML("Mu&szlig; gesetzt sein, wenn das DBMS "
                + "mit Domains arbeiten kann und soll!")));
        dedl.addElement(new DefaultEditorDescriptor(0, this, ID_FKNOTNULLBEACHTEN, dlf, dcf, 
                "Not-Null-Umsetzen", 'O', null, StrUtil.FromHTML("Mu&szlig; gesetzt sein, wenn "
                + "das DBMS die Not-Null-Klauseln f&uuml;r Foreignkeys umsetzen soll.")));
        dedl.addElement(new DefaultEditorDescriptor(0, this, ID_REFERENZENSETZEN, dlf, dcf, 
                "Referenzklauseln setzen", 'R', null, StrUtil.FromHTML("Mu&szlig; gesetzt sein,"
                + " wenn das DBMS die Referenz-Klauseln f&uuml;r Foreignkeys umsetzen soll.")));
        dedl.addElement(new DefaultEditorDescriptor(0, this, ID_QUOTE_CHARACTER, dlf, dcf, 
                "Quote-Zeichenfolge", 'Q', null, StrUtil.FromHTML("Hier kann ein Zeichen "
                + "gesetzt werden, das zur Klammerung von Feld- und Tabellennamen dient.")));
        return dedl;
    }
    
    public Object createObject() {
        ArchimedesJDBCDataSourceRecord jdbcdsr = new ArchimedesJDBCDataSourceRecord();
        jdbcdsr.setDBName("");
        jdbcdsr.setDriver("");
        jdbcdsr.setUser("");
        jdbcdsr.setPassword("");
        return jdbcdsr;
    }
    
    public Object createObject(Object blueprint) throws ClassCastException {
        return new ArchimedesJDBCDataSourceRecord((ArchimedesJDBCDataSourceRecord) blueprint);
    }
    
    public String getXMLAttributes() {
        StringBuffer sb = new StringBuffer().append(super.toXMLAttributes());
        sb.append("hasdomains=\"").append(StrUtil.ToHTML("" + this.hasDomains())).append("\" ");
        sb.append("fknotnull=\"").append(StrUtil.ToHTML("" + this.isFkNotNullBeachten())
                ).append("\" ");
        sb.append("setreferences=\"").append(StrUtil.ToHTML("" + this.isReferenzenSetzen())
                ).append("\" ");
        sb.append("dbmode=\"").append(StrUtil.ToHTML(this.getMode().toToken())).append("\" ");
        return sb.toString();
    }


    /* Implementierung des Interfaces STFStoreable. */

    /**
     * @changed
     *     OLI 29.08.2007 - Der PostgreSQL-Modus wird nun auch beim Schreiben in ein
     *             StructuredTextFile ber&uuml;cksichtigt.
     *
     */
    public void toSTF(StructuredTextFile stf, String[] path) {
        super.toSTF(stf, path);
        stf.writeStr(StructuredTextFile.AddPath(path, "DomainFaehig"), new Boolean(
                this.hasDomains()).toString());
        stf.writeStr(StructuredTextFile.AddPath(path, "FkNotNullBeachten"), new Boolean(
                this.isFkNotNullBeachten()).toString());
        stf.writeStr(StructuredTextFile.AddPath(path, "Quote"), this.getQuoteCharacter());
        stf.writeStr(StructuredTextFile.AddPath(path, "ReferenzenSetzen"), new Boolean(
                this.isReferenzenSetzen()).toString());
        String dbm = "MYSQL";
        if (this.getMode() == DBExecMode.MSSQL) {
            dbm = "MSSQL";
        } else if (this.getMode() == DBExecMode.HSQL) {
            dbm = "HSQL";
        } else if (this.getMode() == DBExecMode.POSTGRESQL) {
            dbm = "POSTGRESQL";
        }
        stf.writeStr(StructuredTextFile.AddPath(path, "DBMode"), dbm);
    }

    /**
     * @changed
     *     OLI 29.08.2007 - Der PostgreSQL-Modus wird nun auch beim Lesen aus einem
     *             StructuredTextFile ber&uuml;cksichtigt.
     *
     */
    public void fromSTF(StructuredTextFile stf, String[] path) {
        super.fromSTF(stf, path);
        this.setDomains(new Boolean(stf.readStr(StructuredTextFile.AddPath(path, "DomainFaehig"
                ), "FALSE")).booleanValue());
        this.setFkNotNullBeachten(new Boolean(stf.readStr(StructuredTextFile.AddPath(path, 
                "FkNotNullBeachten"), "FALSE")).booleanValue());
        this.setReferenzenSetzen(new Boolean(stf.readStr(StructuredTextFile.AddPath(path, 
                "ReferenzenSetzen"), "FALSE")).booleanValue());
        this.setQuoteCharacter(stf.readStr(StructuredTextFile.AddPath(path, "Quote"), ""));
        String dbm = stf.readStr(StructuredTextFile.AddPath(path, "DBMode"), "FALSE");
        if (dbm.equalsIgnoreCase("MSSQL")) {
            this.setMode(DBExecMode.MSSQL);
        } else if (dbm.equalsIgnoreCase("HSQL")) {
            this.setMode(DBExecMode.HSQL);
        } else if (dbm.equalsIgnoreCase("POSTGRESQL")) {
            this.setMode(DBExecMode.POSTGRESQL);
        }
    }
                
}
