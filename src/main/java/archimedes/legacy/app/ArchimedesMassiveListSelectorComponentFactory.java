/* 
 * ArchimedesMassiveListSelectorComponentFactory.java
 *
 * 03.08.2005
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.app;


import corent.gui.*;


/** 
 * Diese speziell auf Archimedes zugeschnittene Spezialisierung der 
 * DefaultMassiveListSelectorComponentFactory erweitert die Superklasse um die F&auml;higkeit
 * die Br&uuml;cke zu einer Datenbank zu schlagen.
 *
 * @author
 *     <P>ollie
 *     <P>
 *
 * @changed
 *     <P>OLI 20.02.2008 - Erweiterung um die Methode <TT>setColumnname(String)</TT>.
 *
 */
 
public class ArchimedesMassiveListSelectorComponentFactory 
        extends DefaultMassiveListSelectorComponentFactory {
            
    /* Die Klasse, &uuml;ber die mit dem DBFactoryController kommuniziert werden soll. */
    private Class cls = null;
    /* Referenz auf die ArchimedesApplication, in deren Rahmen die Factory betrieben wird. */
    private ArchimedesApplication app = null;
    /* Der Name der Tabellenspalte, &uuml;ber die referenziert wird. */
    private String columnname = null;
    /* Der Name des Feldes (bzw Attributs), das die Referenz h&auml;lt. */
    private String referencefield = null;
    
    /** 
     * Generiert eine ArchimedesMassiveListSelectorComponentFactory mit den &uuml;bergebenen 
     * Parametern.
     *
     * @param app Referenz auf die ArchimedesApplication, in deren Rahmen die Factory betrieben 
     *     wird.
     * @param cls Die Klasse, &uuml;ber die mit dem DFC kommuniziert werden soll.
     * @param cn Der Name der Tabellenspalte, &uuml;ber in der Zieltabelle referenziert wird.
     * @param rf Der Name des referenzierenden Feldes.
     */
    public ArchimedesMassiveListSelectorComponentFactory(ArchimedesApplication app, Class cls,
            String cn, String rf) {
        super();
        this.app = app;
        this.cls = cls;
        this.columnname = cn;
        this.referencefield = rf;
    }
    
    public Class getCls() {
        return this.cls;
    }
    
    public void setCls(Class cls) {
        this.cls = cls;
    }
    
    /**
     * Liefert den quailifizierten Namen der in der Zieltabelle referenzierten Spalte.
     *
     * @return Der Name der Tabellenspalte, &uuml;ber die in der Zieltabelle referenziert wird. 
     */
    public String getColumnname() {
        return this.columnname;
    }
    
    /** 
     * Liefert den Namen des Attributs, das die Referenz im Objekt h&auml;lt bzw. den 
     * unqualifizierten Namen der Tabellenspalte, die die Referenz auf die Zieltabelle 
     * h&auml;lt.
     *
     * @return Der Namen des Attributs bzw. der Tabellenspalte, die die Referenz auf die 
     *     Zieltabelle enth&auml:lt. 
     */
    public String getReferenceField() {
        return this.referencefield;
    }
    
    /** 
     * @return Eine Referenz auf die ArchimedesApplication, innerhalb derer die Factory 
     *     arbeitet. 
     */
    public ArchimedesApplication getApp() {
        return this.app;
    }
    
    /**
     * Erlaubt die Manipulation des des vorgegebenen Spaltennamens. 
     *
     * @param cn Der neue Spaltenname, &uuml;ber den der MassiveListSelector seine Daten 
     *     einlesen soll.
     *
     * @changed
     *     OLI 20.02.2008 - Hinzugef&uuml;gt.
     *
     */
    public void setColumnname(String cn) {
        this.columnname = cn;
    }

    
}
