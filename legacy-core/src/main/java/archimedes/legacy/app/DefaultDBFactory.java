/*
 * DefaultDBFactory.java
 *
 * 21.08.2006
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.app;


import corent.db.xs.*;

import java.lang.reflect.*;
import java.sql.*;


/**
 * Diese Default-Implementierung der AbstractDBFactory.
 *
 * @author
 *     ollie
 *     <P>
 *
 * @changed 
 *     OLI 27.08.2007 - Erweiterung der Signatur des Kontruktors um eine Referenz auf den 
 *             DBFactoryController, mit dem die DBFactory-Implementierung zusammenarbeiten soll.
 *     <P>OLI 02.09.2007 - Wiedereinf&uuml;hrung des Konstruktors, der als Parameter lediglich 
 *             die Referenz auf die <TT>ArchimedesDescriptorFactory</TT> erfordert.
 *     <P>OLI 16.07.2008 - &Auml;nderungen an der <TT>write</TT>-Methode im Rahmen der 
 *             &Auml;nderung des Interfaces <TT>DBFactory</TT>.
 *     <P>OLI 18.01.2009 - Implementierung der Methode <TT>create()</TT>. Damit ist die Klasse 
 *             nicht mehr <TT>abstract</TT>. Diesen Umstand habe ich durch Wegfall des 
 *             Schl&uuml;sselwortes hergestellt.
 *     <P>
 *
 */
 
public class DefaultDBFactory<T extends ApplicationObject> extends AbstractDBFactory<T> {
    
    /** 
     * Referenz auf die ArchimedesDescriptorFactory mit der die DefaultDBFactory arbeiten soll.
     */
    protected ArchimedesDescriptorFactory adf = null;
    /** Referenz auf die Klasse von T. */
    protected Class cls = null;
    
    /**
     * Generiert eine DefaultDBFactory mit den angegebenen Parametern.
     *
     * @param adf Die ArchimedesDescriptorFactory, mit der die DefaultDBFactory arbeiten soll.
     *
     * @changed
     *     OLI 02.09.2007 - Wiedereinf&uuml;hrung dieses des Konstruktors.
     *     <P>
     *
     */
    public DefaultDBFactory(ArchimedesDescriptorFactory adf) {
        super(null);
        this.adf = adf;
        this.instance = this.create();
    }
    
    /**
     * Generiert eine DefaultDBFactory mit den angegebenen Parametern.
     *
     * @param adf Die ArchimedesDescriptorFactory, mit der die DefaultDBFactory arbeiten soll.
     * @param cls Die Klasse, auf die die Klasse typisiert werden soll. Leider ist das derzeit 
     *         die einzige M&ouml;glichkeit die <TT>create()</TT>-Methode generisch 
     *         auszuf&uuml;hren.
     *
     * @changed
     *     OLI 20.01.2009 - Wiedereinf&uuml;hrung dieses des Konstruktors.
     *     <P>
     *
     */
    public DefaultDBFactory(ArchimedesDescriptorFactory adf, Class cls) {
        super(null, false);
        this.adf = adf;
        this.cls = cls;
        this.instance = this.create();
    }
    
    /**
     * Generiert eine DefaultDBFactory mit den angegebenen Parametern.
     *
     * @param dbfc Eine Referenz auf den DBFactoryController, an den die DefaultDBFactory 
     *     gekoppelt ist.
     * @param adf Die ArchimedesDescriptorFactory, mit der die DefaultDBFactory arbeiten soll.
     *
     * @changed
     *     OLI 27.08.2007 - Erweiterung der Parameterliste um die DBFactoryController-Referenz
     *             <TT>dbfc</TT>. Bei dieser Gelegenheit: Dokumentation des Konstruktors.
     *     <P>
     *
     */
    public DefaultDBFactory(DBFactoryController dbfc, ArchimedesDescriptorFactory adf) {
        super(dbfc);
        this.adf = adf;
        this.instance = this.create();
    }
        
    /**
     * Generiert eine DefaultDBFactory mit den angegebenen Parametern.
     *
     * @param dbfc Eine Referenz auf den DBFactoryController, an den die DefaultDBFactory 
     *     gekoppelt ist.
     * @param adf Die ArchimedesDescriptorFactory, mit der die DefaultDBFactory arbeiten soll.
     * @param cls Die Klasse, auf die die Klasse typisiert werden soll. Leider ist das derzeit 
     *         die einzige M&ouml;glichkeit die <TT>create()</TT>-Methode generisch 
     *         auszuf&uuml;hren.
     *
     * @changed
     *     OLI 20.01.2009 - Wiedereinf&uuml;hrung dieses des Konstruktors.
     *     <P>
     *
     */
    public DefaultDBFactory(DBFactoryController dbfc, ArchimedesDescriptorFactory adf, 
            Class cls) {
        super(dbfc, false);
        this.adf = adf;
        this.cls = cls;
        this.instance = this.create();
    }
    
    /*
    private T create(ArchimedesDescriptorFactory adf, Class cls) {
        Constructor c = null;
        T t = null;
        try {
            c = cls.getConstructor(ArchimedesDescriptorFactory.class);
            t = (T) c.newInstance(this.adf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
    */
    
    /**
     * @throws UnsupportedOperationException falls die Instanz der Klasse nicht abgeleitet und 
     *         keine Klasse zur Nutzung in der <TT>create()</TT>-Methode angegeben worden ist.
     *
     * @changed
     *     OLI 18.01.2009 - Hinzugef&uuml;gt. Damit ist die Klasse nicht mehr <TT>abstract</TT>.
     *     <P>
     *
     */
    public T create() throws UnsupportedOperationException {
        Constructor c = null;
        T t = null;
        if (this.cls == null) {
            throw new UnsupportedOperationException("Method create() has not been overridden "
                    + "and there is no class defined for object generation.");
        }
        try {
            c = this.cls.getConstructor(ArchimedesDescriptorFactory.class);
            t = (T) c.newInstance(this.adf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
    
    public String createFilter(Object[] criteria) {
        return DefaultDBFactoryController.CreateFilter(this.adf.createFilter(
                ((ApplicationObject) this.instance).getTablename()), criteria);
    }
   
    /**
     * Diese Methode erweitert die write-Methode der Superklasse um das Feature, bei Objekten,
     * die das Interface LastModificationTracker implementieren einen aktuellen Zeitstempel in
     * an das Objekt zu &uuml;bergeben..
     *
     * @param o Das Objekt, welches geschrieben werden soll.
     * @param c Die Datenbankverbindung, &uuml;ber die das Objekt in die Datenbank 
     *     &uuml;bertragen werden soll.
     */
    public T write(T o, Connection c) throws SQLException {
        if (o instanceof LastModificationTracker) {
            ((LastModificationTracker) o).setLastModificationDate(new corent.dates.PTimestamp()
                    );
        }
        return super.write(o, c);
    }    
   
}
