/*
 * ArchimedesDescriptorFactory.java
 *
 * 05.07.2005
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.app;


import archimedes.legacy.model.*;

import corent.base.*;
import corent.base.dynamic.*;
import corent.db.*;
import corent.djinn.*;

import java.util.*;


/**
 * Dieses Interface definiert das Verhalten einer Descriptor-Factory f&uuml;r dynamische 
 * Objekte, deren Descriptoren direkt aus einem Archimedes-Datenmodell gewonnen werden sollen.
 *
 * @author
 *     ollie
 *     <P>
 *
 * @changed
 *     OLI 01.05.2009 - Alphabetische Anordnung der Methodenspezifikationen.
 *     <P>
 *
 */

public interface ArchimedesDescriptorFactory {

    /**
     * Generiert ein Filter-Feld zum Einsatz in der DBFactory.
     *
     * @param tn Der Name der Tabelle zu der der Filter generiert werden soll.
     */
    public String[] createFilter(String tn);

    /**
     * Pr&uuml;ft zwei ArchimedesDynamic-Objekte auf inhaltliche Gleichheit.
     *
     * @param ad0 Eines der Objekte, die auf Inhaltsgleichheit gepr&uuml;ft werden sollen.
     * @param ad1 Das andere der Objekte, die auf Inhaltsgleichheit gepr&uuml;ft werden sollen.
     * @return <TT>true</TT>, falls die Inhalte der beiden Objekte gleich sind.  
     */
    public boolean equalsTo(ArchimedesDynamic ad0, ArchimedesDynamic ad1);

    /**
     * Generiert einen String aus den Angaben der Tabellenbeschreibung.
     *
     * @param ad Das Object, zu dem der String erzeugt werden soll.
     * @return Eine Stringrepr&auml;sentation des &uuml;bergebenen Objektes, die anhand der 
     *     Angaben im Archimedesmodell erstellt wird.  
     */
    public String generateString(ArchimedesDynamic ad);

    /**
     * Liefert eine Referenz auf das Archimedes.Application-Objekt, mit dem die Factory arbeiten
     * soll.
     *
     * @return ArchimedesApplication, innerhalb derer die Factory arbeiten soll.
     */
    public ArchimedesApplication getApplication();

    /**
     * Liefert einen Vector &uuml;ber den die Ordnungszahlen der Attribute ermittelt werden 
     * k&ouml;nnen.
     *
     * @param tn Der Name der Tabelle, zu der die Attributnamen ermittelt werden sollen.
     * @return Der Vector mit den Attributnamen.
     */
    public Vector<String> getAttributenames(String tn); 

    /**
     * Liefert das DiagrammModel, das der ArchimedesDescriptorFactory zugrundeliegt.
     *
     * @return Das DiagrammModel, auf dem die Daten der ArchimedesDescriptorFactory beruht.
     */
    public DiagrammModel getDiagrammModel();

    /**
     * Liefert zum angegebenen Tabellennamen einen DynamicDescriptor.
     *
     * @param tn Der Name der Tabelle des Archimedes-Modells, zu dem der Descriptor geliefert 
     *     werden soll.
     * @return Der DynamicDescriptor zur angegebenen Tabelle.
     */
    public Hashtable<String, AttributeDescriptor> getDynamicDescriptor(String tn);

    /**
     * Liefert zum angegebenen Tabellennamen eine EditorDescriptorList.
     *
     * @param attr Das Attributed-Object, das durch die EditorDescriptorList innerhalb eines
     *     EditorDjinns bearbeitet werden soll.
     * @param tn Der Name der Tabelle des Archimedes-Modells, zu dem der Descriptor geliefert 
     *     werden soll.
     * @return Die EditorDescriptorList zur angegebenen Tabelle.
     */
    public EditorDescriptorList getEditorDescriptor(Attributed attr, String tn);

    /**
     * Generiert einen OrderByDescriptor zur angegebenen Tabelle
     *
     * @param pd Der PersistenceDescriptor, aus dem die Daten f&uuml;r die Tabellenspalten
     *     gewonnen werden sollen.
     * @param tn Der Name der Tabelle, zu dem der Descriptor generiert werden soll.
     * @return Der OrderBydescriptor zur Tabelle.
     */
    public OrderByDescriptor getOrderByDescriptor(PersistenceDescriptor pd, String tn);

    /**
     * Liefert zum angegebenen Tabellennamen einen PersistenceDescriptor.
     *
     * @param cls Die Klasse der Objekte, die durch den PersistenceDescriptor erzeugt werden 
     *     sollen. 
     * @param tn Der Name der Tabelle des Archimedes-Modells, zu dem der Descriptor geliefert 
     *     werden soll.
     * @return Der PersistenceDescriptor zur angegebenen Tabelle.
     */
    public PersistenceDescriptor getPersistenceDescriptor(Class cls, String tn);

    /** 
     * Generiert einen TabbedDescriptor zur angegebenen Tabelle.
     *
     * @param tn Der Name der Tabelle, zu der der TabbedDescriptor generiert werden soll.
     * @return Der TabbedDescriptor zur angegebenen Tabelle, bzw. <TT>null</TT>, wenn die 
     *     Tabelle nicht mehr als ein Panel zur Manipulation ihrer Datens&auml;tze 
     *     ben&ouml;tigt.
     */
    public TabbedPaneFactory getTabbedPaneFactory(String tn);

    /**
     * Setzt die &uuml;bergebene ArchimedesApplication als neuen Application f&uuml;r die 
     * Factory ein.
     *
     * @param app Die ArchimedesApplication, innerhalb derer die Factory arbeiten soll.
     */
    public void setApplication(ArchimedesApplication app);

}
