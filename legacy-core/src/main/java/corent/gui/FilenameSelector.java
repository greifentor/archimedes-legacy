/*
 * FilenameSelector.java
 *
 * 07.07.2008
 *
 * (c) O.Lieshoff
 *
 */
 
package corent.gui;


import corent.base.*;
import corent.files.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;


/**
 * Diese Klasse bietet eine Komponente zur Auswahl von Dateinamen.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 01.10.2007 - Hinzugef&uuml;gt.
 *     <P>OLI 16.07.2008 - Erweiterung um den Clear-Button.
 *     <P>
 *
 */
 
public class FilenameSelector extends JPanel implements Ressourced {
    
    /* Flagge f&uuml;r die harte Abblendung. */
    private boolean strongdisabled = false;
    /* Der Button, der den FileChooser &ouml;ffnet. */
    private JButton button = null;
    /* Der Button, der den FilenameSelector zur&uuml;cksetzt. */
    private JButton buttonClear = null;
    /* Referenz auf das TextField der Komponente. */
    private JTextField textField = null;
    /* Der FileFilter, der bei der Suche im Dateisystem angeboten werden soll. */
    private javax.swing.filechooser.FileFilter filefilter = null;
    /* Die Komponenten-Factory des LineTextEditors. */
    private FilenameSelectorComponentFactory fsecf = null;
    /* Der Context zur Komponente. */
    private String context = null;
    /* Der Pfad, bei dem die Suche begonnen werden soll. */
    private String path = ".";
    /* Der in der Komponente bearbeitete Text. */
    private String text = null;
    
    /**
     * Generiert eine FilenameSelector-Komponente anhand der &uuml;bergebenen Parameter. 
     *
     * @param text Der durch die Komponente zu bearbeitende Text.
     * @param fsecf Die Factory zur Erzeugung der in der Komponente angezeigten Controls.
     * @param ini Die Inidatei, aus der sich das Fenster rekonstruieren soll.
     * @param c Der Context zur Komponente.
     */
    public FilenameSelector(String text, FilenameSelectorComponentFactory fsecf, Inifile ini, 
            String c) {
        super(new BorderLayout(Constants.HGAP, Constants.VGAP));
        this.context = c;
        this.fsecf = fsecf;
        this.text = text;
        this.textField = this.fsecf.createTextField(this);
        this.textField.setText(this.getLine());
        this.textField.setPreferredSize(new Dimension(123, 24));
        this.textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F4) {
                    button.doClick();
                }
            }
        });
        this.button = this.fsecf.createSelectButton(this);
        this.button.setPreferredSize(new Dimension(123, 24));
        this.button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doFilenameSelection();
            }
        });
        this.buttonClear = this.fsecf.createClearButton(this);
        this.buttonClear.setPreferredSize(new Dimension(123, 24));
        this.buttonClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doClear();
            }
        });
        JPanel panelMain = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
        panelMain.add(this.textField);
        JPanel panelButtons = new JPanel(new GridLayout(1, 2, Constants.HGAP, Constants.VGAP));
        panelButtons.add(this.buttonClear);
        panelButtons.add(this.button);
        panelMain.add(panelButtons, BorderLayout.EAST);
        this.add(panelMain);
        this.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                textField.requestFocus();                
            }
        });
    }
    
    /**
     * H&auml;ngt den angegebenen ActionListener an den Button des &Auml;ndern-LineTextEditor 
     * an.
     *
     * @param l Der anzuh&auml;ngende ActionListener.
     */
    public void addActionListener(ActionListener l) {
        this.button.addActionListener(l);
        this.buttonClear.addActionListener(l);
    }

    /** 
     * Diese Methode wird aufgerufen, wenn der Benutzer den FilenameSelector zur&uuml;cksetzeń
     * will.
     */
    public void doClear() {
        this.textField.setText("");
    }
    
    /** Diese Methode wird aufgerufen, wenn der Benutzer den FileChooser aufruft. */
    public void doFilenameSelection() {
        int returnVal = -1;
        JFileChooser fc = new JFileChooser(this.path);
        String dn = null;
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(this.getFileFilter());
        fc.setCurrentDirectory(new File(this.path)); 
        returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            dn = fc.getSelectedFile().getAbsolutePath();
            this.setText(dn);
        }
    }
    
    /**
     * Liefert den Basispfad, auf dem eine eventuell Suche beginnen soll.
     *
     * @return Der Basispfad an dem eine eventuelle Suche beginnen soll.
     */
    public String getBasePath() {
        return this.path;
    }
    
    /** @return Referenz auf den &Auml;ndern-Button der Komponente. */
    public JButton getButton() {
        return this.button;
    }
    
    /**
     * Liefert den FileFilter, der die Suche des FileChooser einschr&auml;nken soll.
     *
     * @return Der aktuelle FileFilter der Komponente.
     */
    public javax.swing.filechooser.FileFilter getFileFilter() {
        return this.filefilter;
    }
    
    private String getLine() {
        String s = this.text;
        int i = s.indexOf("\n");
        if (i > 0) {
            s = s.substring(0, i);
        }
        return s;
    }        
    
    /** @return Der Inhalt des FilenameSelectors. */
    public String getText() {
        return this.textField.getText();
    }
    
    /** @return Referenz auf das Textfeld, in dem die Eingabe stattfindet. */
    public JTextField getTextField() {
        return this.textField;
    }
    
    /**
     * L&ouml;st den angegebenen ActionListener vom Button des &Auml;ndern-LineTextEditor.
     *
     * @param l Der zu l&ouml;sende ActionListener.
     */
    public void removeActionListener(ActionListener l) {
        this.button.removeActionListener(l);
        this.buttonClear.removeActionListener(l);
    }

    /**  
     * Setzt s als neuen Basispfad f&uuml;r eine eventuelle Suche ein.
     *
     * @param s Der neue Basispfad f&uuml;r eine eventuelle Dateisuche.
     */
    public void setBasePath(String s) {
        this.path = s;
    }

    /**
     * Setzt den &uuml;bergebenen FileFilter als neuen FileFilter ein, der die Suche des 
     * FileChooser einschr&auml;nken soll.
     *
     * @param ff Der neue FileFilter zur Komponente.
     */
    public void setFileFilter(javax.swing.filechooser.FileFilter ff) {
        this.filefilter = ff;
    }
    
    /**  
     * Setzt s als neuen der ersten Zeile des Inhaltes des Editors ein.
     *
     * @param s Der neue Inhalt der ersten Zeile des Editors.
     */
    public void setText(String s) {
        this.textField.setText(s);
    }

    
    /* Ueberschreiben von Methoden der Superklasse. */ 

    public void addKeyListener(KeyListener l) {
        this.textField.addKeyListener(l);
        this.button.addKeyListener(l);
        this.buttonClear.addKeyListener(l);
    }

    public void removeKeyListener(KeyListener l) {
        this.textField.removeKeyListener(l);
        this.button.removeKeyListener(l);
        this.buttonClear.removeKeyListener(l);
    }

    public void requestFocus() {
        super.requestFocus();
        this.textField.requestFocus();
    }
    
    public boolean hasFocus() {
        return this.textField.hasFocus() || this.button.hasFocus() || this.buttonClear.hasFocus(
                );
    }
    
    public void setEnabled(boolean b) {       
        if (b && this.strongdisabled) {
            return;
        }
        super.setEnabled(b);
        this.textField.setEnabled(b);
    }

    
    /* Implementierung des Interfaces Ressourced. */
    
    public String getContext() {
        return this.context;
    }
    
    public boolean isStrongDisabled() {
        return this.strongdisabled;
    }
    
    public void setContext(String n) {
        this.context = n;
    }
    
    public synchronized void setStrongDisabled(boolean b) {
        this.setEnabled(false);
        this.strongdisabled = b;
    }

    public void update(RessourceManager rm) {
        String c = COUtil.GetFullContext(this);
        String s = rm.getToolTipText(c);
        if (s.length() > 0) {
            this.setToolTipText(s);
        }
        Color color = rm.getBackground(c);
        if (color != null) {
            this.setBackground(color);
        }
        color = rm.getForeground(c);
        if (color != null) {
            this.setForeground(color);
        }
    }
    
}
