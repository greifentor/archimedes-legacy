/*
 * Core.java
 *
 * AUTOMATISCH VON ARCHIMEDES GENERIERT!!!
 *
 * 18.10.2023
 *
 * (c) by <null>
 *
 */

package ;


import archimedes.legacy.app.*;

import corent.db.xs.*;
import corent.djinn.*;
import corent.files.*;
import corent.gui.*;

import java.util.*;

import javax.swing.*;


/**
 * Abstrakte Basisklasse f&uuml;r die Applikation.
 *
 * @author
 *     <null>
 *     <P>
 *
 * @changed
 *     ?? 18.10.2023 - Hinzugef&uuml;gt.
 *     <P>
 *
 */

public abstract class Core extends JFrameWithInifile implements ArchimedesApplication, MenuSummoner {

    /**
     * Erzeugt eine Instanz der -Basisklasse.
     *
     * @param title Der Titel des Hauptfensters.
     * @param ini Die Inidatei, aus der die Anwendung ihre Voreinstellungen beziehen soll.
     */
    public Core(String title, Inifile ini) {
        super(title, ini);
    }

    /**
     * Erzeugt eine Hashtable mit allen DBFactories zur Anwendung.
     *
     * @param adf Die ArchimedesDescriptorFactory der Anwendung.
     */
    public Hashtable<Class, DBFactory> createFactories(ArchimedesDescriptorFactory adf) {
        Hashtable<Class, DBFactory> factories = new Hashtable<Class, DBFactory>();
        return factories;
    }

    /**
     * Diese Methode wird ausgef&uuml;hrt, wenn der Benutzer eine der Stammdatenwartungspunkte
     * aufruft.
     *
     * @param cls Die Class, deren Objekte stammgewartet werden sollen.
     * @param headline Die &Uuml;berschrift zum Auswahldialog der Wartung.
     */
    public abstract void doWartungStammdaten(Class cls, String headline);


    /* Implementierung des Interfaces MenuSummoner. */

    public void processMenuEvent(String context, String[] params) {
    }

}
