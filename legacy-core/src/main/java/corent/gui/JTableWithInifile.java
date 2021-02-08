/*
 * JTableWithIniFile.java
 *
 * 25.10.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import corent.files.*;

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;


/**
 * Diese Spezialisierung der JTable ist in der Lage ihre Konfiguration (zun&auml;chst nur die
 * Spaltenbreite, mittelfristig aber auch die Spaltenreihenfolge) in eine Inidatei zu speichern
 * und diese Einstellungen bei Bedarf daraus wieder herzustellen.
 *
 * @author O.Lieshoff
 *
 */

public class JTableWithInifile extends JTable implements ContextOwner {
    
    /* Diese Flagge wird nach dem Abschlu&szlig; der Initialisierung des Objektes gesetzt. */
    private boolean initialized = false;
    /* Flagge f&uuml;r die harte Abblendung. */
    private boolean strongdisabled = false;
    /* Die Inidatei, in die die Konfiguration des JTables gespeichert werden soll. */
    private Inifile ini = null;
    /* Ein Name, unter dem die Konfiguration zur JTable in der Inidatei abgelegt werden kann. */
    private String identifier = null;
    /* Der Context zur Komponente. */
    private String context = null;

    
    public JTableWithInifile(Inifile ini, String ident) {
        super();
        this.ini = ini;
        this.identifier = ident;
        // this.restoreFromIni();
    }
    
    public JTableWithInifile(int numRows, int numColumns, Inifile ini, String ident) {
        super(numRows, numColumns);
        this.ini = ini;
        this.identifier = ident;
        // this.restoreFromIni();
    }
    
    public JTableWithInifile(Object[][] rowData, Object[] columnNames, Inifile ini, String ident
            ) {
        super(rowData, columnNames);
        this.ini = ini;
        this.identifier = ident;
        // this.restoreFromIni();
    }
    
    public JTableWithInifile(TableModel dm, Inifile ini, String ident) {
        super(dm);
        this.ini = ini;        
        this.identifier = ident;
        // this.restoreFromIni();
    }
    
    public JTableWithInifile(TableModel dm, TableColumnModel cm, Inifile ini, String ident) {
        super(dm, cm);
        this.ini = ini;
        this.identifier = ident;
        // this.restoreFromIni();
    }
    
    public JTableWithInifile(TableModel dm, TableColumnModel cm, ListSelectionModel sm, 
            Inifile ini, String ident) {
        super(dm, cm, sm);
        this.ini = ini;
        this.identifier = ident;
        // this.restoreFromIni();
    }
    
    public JTableWithInifile(Vector rowData, Vector columnNames, Inifile ini, String ident) {
        super(rowData, columnNames);
        this.ini = ini;
        this.identifier = ident;
        // this.restoreFromIni();
    }
    
    public void setEnabled(boolean b) {
        if (b && this.strongdisabled) {
            return;
        }
        super.setEnabled(b);
    }

    /**
     * Setzt den &uuml;bergebenen Wert als neuen Namen, unter dem die Daten der Komponente in
     * der IniDatei gespeichert werden.
     *
     * @param identifier Der neue Name, unter dem die Daten der Komponente in der IniDatei
     *     gespeichert werden sollen.
     */
    public void setIdentifier(String identifier) {
        if (identifier == null) {
            this.identifier = null;
        } else {
            this.identifier = identifier;
        }
    }

    /**
     * @return Der Name, unter dem die Daten der Komponente in der IniDatei gespeichert werden.
     *     Ist kein expliziter Name gesetzt, wird der Klassenname der Componente
     *     zur&uuml;ckgeliefert.
     */
    public String getIdentifier() {
        if (this.identifier == null) {
            return this.getClass().getName();
        }
        return this.identifier;
    }
    
    /** Liest die Konfiguration des JTables aus der Inidatei. */
    public void restoreFromIni() {
        this.initialized = false;
        if (this.ini != null) {
            TableColumnModel tcm = this.getColumnModel();
            for (int i = 0, len = tcm.getColumnCount(); i < len; i++) {
                TableColumn tc = tcm.getColumn(i);
                int w = this.ini.readInt(this.getIdentifier(), "Width" + i, 
                        tc.getPreferredWidth());
                tc.setPreferredWidth(w);
                tc.setWidth(w);
            }
        }
        this.initialized = true;
    }
    
    /** Schreibt die Konfiguration der JTable in die Inidatei. */
    public void saveToIni() {
        if (this.ini != null) {
            TableColumnModel tcm = this.getColumnModel();
            for (int i = 0, len = tcm.getColumnCount(); i < len; i++) {
                TableColumn tc = tcm.getColumn(i);
                try {
                    this.ini.writeInt(this.getIdentifier(), "Width" + i, tc.getWidth());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
    

    /* Ueberschreiben von Methoden der Superklasse. */
    
    public void columnMarginChanged(ChangeEvent e) {
        super.columnMarginChanged(e);
        if (this.initialized) {
            this.saveToIni();
        }
    }
    
    
    /* Implementierung des Interfaces ContextOwner. */

    public String getContext() {
        if (this.context != null) {
            return this.context;
        }
        return this.getIdentifier();
    }
    
    public boolean isStrongDisabled() {
        return this.strongdisabled;
    }
    
    public void setContext(String c) {
        this.context = c;
    }

    public synchronized void setStrongDisabled(boolean b) {
        this.setEnabled(false);
        this.strongdisabled = b;
    }

}
