/*
 * JDBCDataSourceRecord.java
 *
 * 01.03.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


import corent.base.*;
import corent.djinn.*;
import corent.files.*;


/**
 * Diese Implementierung des DataSourceRecord-Interfaces bezieht sich auf die zur Nutzung einer
 * JDBC-Datenquelle notwendigen Daten.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */

public class JDBCDataSourceRecord implements Attributed, DataSourceRecord, Editable, 
        STFStoreable {
    
    /** Eine Instanz zur Benutzung als Blueprint. */
    public static final JDBCDataSourceRecord INSTANCE = new JDBCDataSourceRecord();
    
    /** Id f&uuml;r den Zugriff auf den Namen der Datenbank des Records. */
    public static final int ID_DBNAME = 0;
    /** Id f&uuml;r den Zugriff auf die Beschreibung zum Records. */
    public static final int ID_DESCRIPTION = 1;
    /** Id f&uuml;r den Zugriff auf den Namen des Records. */
    public static final int ID_NAME = 2;
    /** Id f&uuml;r den Zugriff auf den Klassennamen des JDBC-Drivers zur Vebindung. */
    public static final int ID_DRIVER = 3;
    /** Id f&uuml;r den Zugriff auf den Namen des Users der Datenbank. */
    public static final int ID_USER = 4;
    /** Id f&uuml;r den Zugriff auf Password des Users. */
    public static final int ID_PASSWORD = 5;

    /* Der Name der physischen Datenquelle. */
    private String dbname = null;
    /* Die Beschreibung zur Datenquelle. */
    private String description = "no description";
    /* Der Name der Treiberklasse, &uuml;ber die auf die Datenquelle zugegriffen werden soll. */
    private String driver = null;
    /* Der Name zur Datenquelle. */
    private String name = "unknown";
    /* Das Passwort, mit dem der User auf die Datenquelle zugreift. */
    private String password = null;
    /* Der Name, unter dem der User auf die Datenquelle zugreift. */
    private String user = null;
    
    /* Generiert eine Instanz mit Defaultwerten. */
    protected JDBCDataSourceRecord() {
        super();
    }
    
    /* Generiert eine Instanz als Kopie der &uuml;bergebenen Instanz. */
    protected JDBCDataSourceRecord(JDBCDataSourceRecord jdbcdsr) {
        super();
        for (int i = 0; i <= 5; i++) {
            this.set(i, jdbcdsr.get(i));
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
     */
    public JDBCDataSourceRecord(String driver, String dbname, String user, String password) {
        super();
        this.setDriver(driver);
        this.setDBName(dbname);
        this.setUser(user);
        this.setPassword(password);
    }
    
    
    /* Accessoren und Mutatoren. */
    
    /**
     * Accessor f&uuml;r die Eigenschaft DBName.
     * 
     * @return Der Wert der Eigenschaft DBName.
     */
    public String getDBName() {
        return this.dbname;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft DBName.
     *
     * @param dbname Der neue Wert der Eigenschaft DBName.
     */
    public void setDBName(String dbname) {
        this.dbname = dbname;
    }
    
    /**
     * Accessor f&uuml;r die Eigenschaft Driver.
     * 
     * @return Der Wert der Eigenschaft Driver.
     */
    public String getDriver() {
        return this.driver;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft Driver.
     *
     * @param driver Der neue Wert der Eigenschaft Driver.
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }
    
    /**
     * Accessor f&uuml;r die Eigenschaft Password.
     * 
     * @return Der Wert der Eigenschaft Password.
     */
    public String getPassword() {
        return this.password;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft Password.
     *
     * @param password Der neue Wert der Eigenschaft Password.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Accessor f&uuml;r die Eigenschaft User.
     * 
     * @return Der Wert der Eigenschaft User.
     */
    public String getUser() {
        return this.user;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft User.
     *
     * @param user Der neue Wert der Eigenschaft User.
     */
    public void setUser(String user) {
        this.user = user;
    }
    
    /** 
     * Liefert eine Liste mit XML-Attributen mit den Inhalten des Objekts.
     *
     * @return Der Inhalt des Objekts als XML-Attributliste.
     */
    public String toXMLAttributes() {
        StringBuffer sb = new StringBuffer();
        sb.append("dbname=\"").append(StrUtil.ToHTML(this.getDBName())).append("\" ");
        sb.append("driver=\"").append(StrUtil.ToHTML(this.getDriver())).append("\" ");
        sb.append("user=\"").append(StrUtil.ToHTML(this.getUser())).append("\" ");
        return sb.toString();
    }
    
    
    /* Ueberschreiben der Methoden der Superklasse. */
    
    public boolean equals(Object o) {
        if (!(o instanceof JDBCDataSourceRecord)) {
            return false;
        }
        JDBCDataSourceRecord dsr = (JDBCDataSourceRecord) o;
        return (this.getName().equals(dsr.getName()) && this.getDescription().equals(
                dsr.getDescription()) && this.getDriver().equals(dsr.getDriver()) &&
                this.getDBName().equals(dsr.getDBName()) && this.getUser().equals(dsr.getUser())
                && this.getPassword().equals(dsr.getPassword()));
    }
    
    public int hashCode() {
        int result = 17;
        result = 37 * result + this.getDBName().hashCode();
        result = 37 * result + this.getDescription().hashCode();
        result = 37 * result + this.getDriver().hashCode();
        result = 37 * result + this.getName().hashCode();
        result = 37 * result + this.getPassword().hashCode();
        result = 37 * result + this.getUser().hashCode();
        return result;
    }
    
    public String toString() {
        return this.getName();
    }
        
        
    /* Implementierung des Interfaces Attributed. */
    
    public Object get(int id) throws IllegalArgumentException {
        switch (id) {
        case ID_DBNAME:
            return this.getDBName(); 
        case ID_DESCRIPTION:
            return this.getDescription(); 
        case ID_DRIVER:
            return this.getDriver();
        case ID_NAME:
            return this.getName();
        case ID_PASSWORD:
            return this.getPassword();
        case ID_USER:
            return this.getUser();
        }
        throw new IllegalArgumentException("id (" + id + ") not valid for instance of "
                + "JDBCDataSourceRecord"); 
    }
   
    public void set(int id, Object value) throws ClassCastException, IllegalArgumentException {
        switch (id) {
        case ID_DBNAME:
            this.setDBName((String) value);
            break;
        case ID_DESCRIPTION:
            this.setDescription((String) value); 
            break;
        case ID_DRIVER:
            this.setDriver((String) value);
            break;
        case ID_NAME:
            this.setName((String) value);
            break;
        case ID_PASSWORD:
            this.setPassword((String) value);
            break;
        case ID_USER:
            this.setUser((String) value);
            break;
        default:
            throw new IllegalArgumentException("id (" + id + ") not valid for instance of "
                    + "JDBCDataSourceRecord"); 
        }
    }
    
    
    /* Implementierung des Interfaces DataSourceRecord. */ 
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    /* Implementierung des Interfaces Editable. */
    
    public EditorDescriptorList getEditorDescriptorList() {
        DefaultComponentFactory dcf = DefaultComponentFactory.INSTANZ;
        DefaultEditorDescriptorList dedl = new DefaultEditorDescriptorList();
        DefaultLabelFactory dlf = DefaultLabelFactory.INSTANZ;
        dedl.addElement(new DefaultEditorDescriptor(0, this, ID_DRIVER, dlf, dcf, "Driver", 'D',
                null, "Der Klassenname des Driver, unter dem die Connection erzeugt werden soll"
                ));
        dedl.addElement(new DefaultEditorDescriptor(0, this, ID_DBNAME, dlf, dcf, "Name", 'N', 
                null, "Der Name (URL) der Datenbank"));
        dedl.addElement(new DefaultEditorDescriptor(0, this, ID_USER, dlf, dcf, "Benutzer", 'B',
                null, "Der Benutzername, unter dem auf die Datenbank zugegriffen werden soll"));
        dedl.addElement(new DefaultEditorDescriptor(0, this, ID_PASSWORD, dlf, dcf, "Password", 
                'P', null, "Das Password zum Benutzernamen"));
        return dedl;
    }
    
    public Object createObject() {
        JDBCDataSourceRecord jdbcdsr = new JDBCDataSourceRecord();
        jdbcdsr.setDBName("");
        jdbcdsr.setDriver("");
        jdbcdsr.setUser("");
        jdbcdsr.setPassword("");
        return jdbcdsr;
    }
    
    public Object createObject(Object blueprint) throws ClassCastException {
        return new JDBCDataSourceRecord((JDBCDataSourceRecord) blueprint);
    }
    
    
    /* Implementierung des Interfaces STFStoreable. */
    
    public void toSTF(StructuredTextFile stf, String[] path) {
        stf.writeStr(StructuredTextFile.AddPath(path, "DBName"), StrUtil.ToHTML(this.getDBName()
                ));
        stf.writeStr(StructuredTextFile.AddPath(path, "Description"), StrUtil.ToHTML(
                this.getDescription()));
        stf.writeStr(StructuredTextFile.AddPath(path, "Driver"), StrUtil.ToHTML(this.getDriver()
                ));
        stf.writeStr(StructuredTextFile.AddPath(path, "Name"), StrUtil.ToHTML(this.getName()));
        stf.writeStr(StructuredTextFile.AddPath(path, "User"), StrUtil.ToHTML(this.getUser())
                );
    }
    
    public void fromSTF(StructuredTextFile stf, String[] path) {
        this.setDBName(StrUtil.FromHTML(stf.readStr(StructuredTextFile.AddPath(path, "DBName"), 
                "")));
        this.setDescription(StrUtil.FromHTML(stf.readStr(StructuredTextFile.AddPath(path, 
                "Description"), "")));
        this.setDriver(StrUtil.FromHTML(stf.readStr(StructuredTextFile.AddPath(path, "Driver"), 
                "")));
        this.setName(StrUtil.FromHTML(stf.readStr(StructuredTextFile.AddPath(path, "Name"), 
                "")));
        this.setUser(StrUtil.FromHTML(stf.readStr(StructuredTextFile.AddPath(path, "User"), 
                "")));
        this.setPassword("");
    }
        
}
