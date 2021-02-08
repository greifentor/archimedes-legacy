/*
 * GenericDBFactory.java
 *
 * 09.08.2005
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.app;


import corent.db.xs.*;


/**
 * Diese DBFactory erzeigt ApplicationObjects. Dadurch l&auml;&szlig;t sie sich generisch 
 * nutzen. Allerdings liefert sie keine getypten Ausgaben, sondern eben nur ApplicationObjects.
 *
 * @author ollie
 *
 * @changed 
 *     OLI 27.08.2007 - Erweiterung der Signatur des Kontruktors um eine Referenz auf den 
 *             DBFactoryController, mit dem die DBFactory-Implementierung zusammenarbeiten soll.
 *             <BR>
 *
 */
 
public class GenericDBFactory extends AbstractDBFactory {
    
    /* 
     * Referenz auf die ArchimedesDescriptorFactory, aus der die Instanzen der Klasse ihre
     * Descriptoren beziehen sollen.
     */
    private ArchimedesDescriptorFactory adf = null;
    /* Der Name der Tabelle, auf den sich die Factory beziehen soll. */
    private String tablename = null;
    
    /**
     * Erzeugt eine GenericDBFactory anhand der &uuml;bergebenen Parameter.
     *
     * @param dbfc Eine Referenz auf den DBFactoryController, an den die DefaultDBFactory 
     *     gekoppelt ist.
     * @param adf Die ArchimedesDescriptorFactory zur Erzeugung der Descriptoren der Objekte.
     * @param tn Der Name der Tabelle, auf die sich die Factory beziehen soll.
     *
     * @changed
     *     OLI 27.08.2007 - Erweiterung der Parameterliste um die DBFactoryController-Referenz
     *             <TT>dbfc</TT>.
     *
     */
    public GenericDBFactory(DBFactoryController dbfc, ArchimedesDescriptorFactory adf, 
            String tn) {
        super(dbfc);
        this.adf = adf;
        this.tablename = tn;
        this.instance = this.create();
    }
    
    
    /* Implementierung der abstrakten Methoden der Superklasse. */
    
    public ApplicationObject create() {
        return new GenericApplicationObject(this.adf, this.tablename);
    }
    
    public String createFilter(Object[] criteria) {
        return DefaultDBFactoryController.CreateFilter(this.adf.createFilter(
                ((ApplicationObject) this.instance).getTablename()), criteria);
    }

}
