/*
 * ArchimedesApplication.java
 *
 * 06.07.2005
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.app;


import corent.db.xs.*;
import corent.security.*;

import java.util.*;

import javax.swing.*;


/**
 * Dieses Interface definiert das Verhalten von Applikationen, die mit dynamischen 
 * Archimedes-Applikationsobjekten zusammenarbeiten sollen.
 *
 * @author
 *     ollie
 *     <P>
 *
 * @changed
 *     OLI 10.01.2009 - Erweiterung um die Spezifikation der Methode 
 *             <TT>createFactories(ArchimedesDescriptorFactory)</TT>.
 *     <P> 
 *
 */
 
public interface ArchimedesApplication {
    
    /**
     * Liefert eine Hashtable mit den DBFactories, die in den DBFactoryController der 
     * Applikation eingebunden werden sollen. Diese Methode wird f&uuml;r den Start der 
     * Applikation genutzt. Es handelt sich also nicht unbedingt um eine Referenz auf die 
     * tats&auml;chlich im DBFactoryController genutzten DBFactory-Instanzen.
     *
     * @param adf Die ArchimedesDescriptorFactory, aus der die Applikation ihre Strukturen 
     *     generiert.
     *
     * @return Eine Hashtable (Class, DBFactory) mit den DBFactory-Instanzen zu den einzelnen
     *     Klassen.
     *
     * @changed
     *     OLI 10.01.2009 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public Hashtable<Class, DBFactory> createFactories(ArchimedesDescriptorFactory adf);
    
    /** 
     * Liefert eine Referenz auf die ArchimedesDescriptorFactory der Applikation.
     *
     * @return Referenz auf die ArchimedesDescriptorFactory der Applikation.
     */ 
    public ArchimedesDescriptorFactory getADF();
    
    /**
     * Liefert eine Referenz auf das DesktopPanel des Applikationshauptfensters.
     *
     * @return Referenz auf das DesktopPanel des Applikationshauptfensters.
     */
    public JDesktopPane getDesktop();
    
    /**
     * Liefert eine Referenz auf den DBFactoryController, &uuml;ber den die Applikation mit der
     * Persistenzschicht kommuniziert.
     *
     * @return Referenz auf den DBFactoryController der Applikation. 
     */
    public DBFactoryController getDFC();
    
    /** 
     * Liefert eine Referenz auf den JFrame, in dem das Hauptfenster der Applikation abgebildet
     * werden soll.
     *
     * @return Referenz auf das Applikationshauptfenster.
     */
    public JFrame getFrame();
    
    /** 
     * Liefert den Pfad, unter dem die Images der Applikation zu finden sind.
     *
     * @return Der Pfad, unter dem die Images zu finden sind.
     */
    public String getImagePath();
    
    /**
     * Liefert den Namen des Druckers, an den die Ausdrucke weitergeleitet werden sollen.
     *
     * @return Der Name des Printers, zu dem die Ausdrucke geleitet werden sollen. 
     */
    public String getPrintername();
    
    /**
     * Liefert eine Referenz auf den f&uuml;r die Applikation zust&auml;ndigen 
     * SecurityController.
     *
     * @return Der SecurityController, der die Zugriffsrechte f&uuml;r die Applikationen 
     *         regelt. 
     */
    public SecurityController getSecurityController();
    
    /**
     * Liefert eine Referenz auf den an der Applilation angemeldeten Benutzer. 
     *
     * @return Referenz auf den an der Applilation angemeldeten Benutzer.
     */
    public Object getUser();
        
}    
