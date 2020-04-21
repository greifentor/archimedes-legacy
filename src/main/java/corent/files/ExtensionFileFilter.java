/*
 * ExtensionFileFilter.java
 *
 * 11.01.2004
 *
 */

package corent.files;


import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

import javax.swing.filechooser.*;


/**
 * Diese Klasse konkretisiert einen FileFilter in der Form, da&szlig; er nur Files mit 
 * bestimmten Extensions akzeptiert.
 *
 * @author O.Lieshoff
 *
 * @changed 
 *     OLI 05.09.2007 - &Auml;nderung wegen <I>deprecation</I> der Klasse <TT>
 *             corent.files.FileUtil</TT> in der Methode <TT>accept(File)</TT>.<BR>
 *
 */

public class ExtensionFileFilter extends FileFilter implements FilenameFilter {

    /* Liste mit den zu akzeptierenden Extensions <I>(Default new Vector())</I>. */
    private Vector extensions = new Vector();
    /* Ein erl&auml;uternder Text zum FileFilter <I>(Default "")</I>. */
    private String description = "";

    /**
     * Generiert einen FileFilter auf Basis der in dem &uuml;bergebenen Vector vorgefundenen
     * Extensions.
     *
     * @param extensions Die Extensions, die von dem FileFilter akzeptiert werden sollen.
     */
    public ExtensionFileFilter(Vector extensions) {
        this(extensions, "");
    }

    /**
     * Generiert einen FileFilter auf Basis der in dem &uuml;bergebenen Vector vorgefundenen
     * Extensions.
     *
     * @param extensions Die Extensions, die von dem FileFilter akzeptiert werden sollen.
     * @param description Eine Beschreibung zum Filter.
     */
    public ExtensionFileFilter(Vector extensions, String description) {
        super();
        this.extensions = new Vector(extensions);
        this.description = description;
    }

    /**
     * Generiert einen FileFilter auf Basis der in dem &uuml;bergebenen Array vorgefundenen
     * Extensions.
     *
     * @param extensions Die Extensions, die von dem FileFilter akzeptiert werden sollen.
     */
    public ExtensionFileFilter(String[] extensions) {
        this(extensions, "");
    }

    /**
     * Generiert einen FileFilter auf Basis der in dem &uuml;bergebenen Array vorgefundenen
     * Extensions.
     *
     * @param extensions Die Extensions, die von dem FileFilter akzeptiert werden sollen.
     * @param description Eine Beschreibung zum Filter.
     */
    public ExtensionFileFilter(String[] extensions, String description) {
        super();
        for (int i = 0; i < extensions.length; i++) {
            if (extensions[i] != null) {
                this.extensions.addElement(extensions[i]);
            }
        }
        this.description = description;
    }

    
    /* Implementierung des Interfaces FileFilter. */
    
    /**
     * @changed 
     *     OLI 05.09.2007 - &Auml;nderung des Aufrufs der Methode <TT>GetExtension(File)</TT> 
     *             auf die Methode der Klasse <TT>corent.util.FileUtil</TT>. Die Klasse dieses
     *             Packages (corent.files.FileUtil) wurde auf deprecated gesetzt.<BR>
     */
    public boolean accept(File f) {
        if (f.isDirectory()) {
           return true;
        }
        String extension = corent.util.FileUtil.GetExtension(f);
        if (extension != null) {
            for (int i = 0; i < this.extensions.size(); i++) {
                String ext = (String) this.extensions.elementAt(i);
                if (ext != null) {
                    if (extension.toLowerCase().equals(ext)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String getDescription() {
        String s = new String();
        if (this.description != null) {
            s = this.description;
        }
        s = s.concat(" (");
        for (int i = 0; i < this.extensions.size(); i++) {
            String h = (String) this.extensions.elementAt(i);
            if (h != null) {
                s = s.concat("*." + h);
            }
        }
        s = s.concat(")");
        return s;
    }
    
    
    /* Implementierung des Interfaces FilenameFilter. */

    public boolean accept(File dir, String name) {
        if (name != null) {
            name = name.toLowerCase();
            for (int i = 0, len = this.extensions.size(); i < len; i++) {
                String h = (String) this.extensions.elementAt(i);
                if (name.endsWith(h.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }
    
}