/*
 * TestModelCore.java
 *
 * AUTOMATISCH VON ARCHIMEDES GENERIERT!!!
 *
 * 12.03.2021
 *
 * (c) by ollie
 *
 */

package testmodel;


import archimedes.legacy.app.*;

import corent.db.xs.*;
import corent.djinn.*;
import corent.files.*;
import corent.gui.*;

import java.util.*;

import javax.swing.*;

import platon.scheme.*;
import platon.scheme.db.*;


/**
 * Abstrakte Basisklasse f&uuml;r die Applikation.
 *
 * @author
 *     ollie
 *     <P>
 *
 * @changed
 *     ?? 12.03.2021 - Hinzugef&uuml;gt.
 *     <P>
 *
 */

public abstract class TestModelCore extends JFrameWithInifile implements ArchimedesApplication, MenuSummoner {

    /**
     * Erzeugt eine Instanz der TestModel-Basisklasse.
     *
     * @param title Der Titel des Hauptfensters.
     * @param ini Die Inidatei, aus der die Anwendung ihre Voreinstellungen beziehen soll.
     */
    public TestModelCore(String title, Inifile ini) {
        super(title, ini);
    }

    /**
     * Erzeugt eine Hashtable mit allen DBFactories zur Anwendung.
     *
     * @param adf Die ArchimedesDescriptorFactory der Anwendung.
     */
    public Hashtable<Class, DBFactory> createFactories(ArchimedesDescriptorFactory adf) {
        Hashtable<Class, DBFactory> factories = new Hashtable<Class, DBFactory>();
        factories.put(ColumnScheme.class, new DBFactoryColumnScheme(adf));
        factories.put(DataScheme.class, new DBFactoryDataScheme(adf));
        factories.put(DomainScheme.class, new DBFactoryDomainScheme(adf));
        factories.put(StereotypeScheme.class, new DBFactoryStereotypeScheme(adf));
        factories.put(StereotypeSchemeTableScheme.class, new DBFactoryStereotypeSchemeTableScheme(adf));
        factories.put(TableScheme.class, new DBFactoryTableScheme(adf));
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
        if (context.equals("wartung/ColumnScheme")) {
            this.doWartungStammdaten(ColumnScheme.class, "ColumnScheme");
        } else if (context.equals("wartung/DataScheme")) {
            this.doWartungStammdaten(DataScheme.class, "DataScheme");
        } else if (context.equals("wartung/DomainScheme")) {
            this.doWartungStammdaten(DomainScheme.class, "DomainScheme");
        } else if (context.equals("wartung/StereotypeScheme")) {
            this.doWartungStammdaten(StereotypeScheme.class, "StereotypeScheme");
        } else if (context.equals("wartung/StereotypeSchemeTableScheme")) {
            this.doWartungStammdaten(StereotypeSchemeTableScheme.class, "StereotypeSchemeTableScheme");
        } else if (context.equals("wartung/TableScheme")) {
            this.doWartungStammdaten(TableScheme.class, "TableScheme");
        }
    }

}
