/*
 * ArchimedesImportProgressMonitor.java
 *
 * 21.07.2008
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.gui;


import corent.gui.*;

import java.util.*;

import javax.swing.*;


/**
 * Eine Ableitung des ProgressMonitorScreen zur grafischen Aufbereitung des Datenmodellimports
 * im Archimedes-System.
 *
 * @author
 *     ollie
 *     <P>
 *
 * @changed
 *     OLI 21.07.2008 - Hinzugef&uuml;gt.
 *     <P>
 *
 */
 
public class ArchimedesImportProgressMonitor extends ProgressMonitorScreen {
 
    public ArchimedesImportProgressMonitor (JFrame caller, String title, Hashtable parameter, 
            ImageIcon bild) {
        super(caller, title, parameter, bild);
    }
    
    
    /* Implementierung der abstrakten Methoden der Superklasse. */
    
    public void progress(Hashtable parameter) {
        int max = (Integer) parameter.get("max");
        int now = (Integer) parameter.get("now");
        int p = (int) (((double) now / (double) max) * 100.0);
        this.update(p);
    }
    
}
