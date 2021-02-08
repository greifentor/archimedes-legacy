/*
 * LineTextEditor.java
 *
 * 05.10.2004
 *
 * (c) O.Lieshoff
 *
 */
 
package corent.gui;


import corent.base.*;
import corent.files.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


/**
 * Diese Klasse bietet ein TextField, das einen mehrzeiligen Text enthalten kann, der &uuml;ber
 * einen, durch einen nebenstehenden Button aufrufbaren TextEditor ge&auml;ndert werden kann.
 * Die in dem TextField abgebildete Zeile ist die erste des Textes und kann in dem TextField 
 * ebenfalls bearbeitet werden.
 *
 * @author
 *     <P>O.Lieshoff
 *     <P>
 *
 * @changed
 *     <P>OLI 01.10.2007 - Erweiterung um die Methode <TT>initText(String)</TT>.
 *
 */
 
public class LineTextEditor extends JPanel implements Ressourced {
    
    /* Flagge f&uuml;r die harte Abblendung. */
    private boolean strongdisabled = false;
    /* Die Inidatei, aus der sich das Fenster rekonstruieren soll. */
    private Inifile ini = null;
    /* Der Button, der den TextEditor &ouml;ffnet. */
    private JButton button = null;
    /* Referenz auf das TextField der Komponente. */
    private JTextField textField = null;
    /* Die Komponenten-Factory des LineTextEditors. */
    private LineTextEditorComponentFactory ltecf = null;
    /* Der Context zur Komponente. */
    private String context = null;
    /* Der in der Komponente bearbeitete Text. */
    private String text = null;
    
    /**
     * Generiert eine LineTextEditor-Komponente anhand der &uuml;bergebenen Parameter. 
     *
     * @param text Der durch die Komponente zu bearbeitende Text.
     * @param ltecf Die Factory zur Erzeugung der in der Komponente angezeigten Controls.
     * @param ini Die Inidatei, aus der sich das Fenster rekonstruieren soll.
     * @param c Der Context zur Komponente.
     */
    public LineTextEditor(String text, LineTextEditorComponentFactory ltecf, Inifile ini, 
            String c) {
        super(new BorderLayout(Constants.HGAP, Constants.VGAP));
        this.context = c;
        this.ini = ini;
        this.ltecf = ltecf;
        this.text = text;
        this.textField = this.ltecf.createTextField(this);
        this.textField.setText(this.getLine());
        this.textField.setPreferredSize(new Dimension(123, 24));
        this.textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F4) {
                    button.doClick();
                }
            }
        });
        this.button = this.ltecf.createAendernButton(this);
        this.button.setPreferredSize(new Dimension(123, 24));
        this.button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doTextEditor();
            }
        });
        JPanel panelMain = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
        panelMain.add(this.textField);
        panelMain.add(this.button, BorderLayout.EAST);
        this.add(panelMain);
        this.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                textField.requestFocus();                
            }
        });
     }
    
    private String prefixText(String line) {
        String s = "";
        int i = this.text.indexOf("\n");
        if (i > -1) {
            if (i+1 < this.text.length()-1) {
                s = this.text.substring(i+1, this.text.length());
            }
            if (s.length() > 0) {
                s = "\n" + s;
            }
        }
        return line + s;
    }        
    
    private String getLine() {
        String s = this.text;
        int i = s.indexOf("\n");
        if (i > 0) {
            s = s.substring(0, i);
        }
        return s;
    }        
    
    /** Diese Methode wird aufgerufen, wenn der Benutzer den TextEditor aufruft. */
    public void doTextEditor() {
        TextAreaDialog tad = new TextAreaDialog(this.ltecf, this.getText(), this.isEnabled(), 
                this.ini);
        this.text = tad.getText();
        this.textField.setText(this.getLine());
    }
    
    
    /** @return Der Inhalt des LineTextEditors. */
    public String getText() {
        return this.prefixText(this.textField.getText());
    }

    /**  
     * Setzt s als neuen der ersten Zeile des Inhaltes des Editors ein.
     *
     * @param s Der neue Inhalt der ersten Zeile des Editors.
     */
    public void setText(String s) {
        this.textField.setText(s);
    }
    
    /**  
     * Setzt s als neuen Inhalt des Editors ein.
     *
     * @param s Der neue Inhalt des Editors.
     *
     * @changed
     *     OLI 01.10.2007 - Hinzugef&uuml;gt.<BR>
     */
    public void initText(String s) {
        this.text = s;
        this.textField.setText(this.getLine());
    }
    
    /** @return Referenz auf das Textfeld, in dem die Eingabe stattfindet. */
    public JTextField getTextField() {
        return this.textField;
    }
    
    /** @return Referenz auf den &Auml;ndern-Button der Komponente. */
    public JButton getButton() {
        return this.button;
    }
    
    /**
     * H&auml;ngt den angegebenen ActionListener an den Button des &Auml;ndern-LineTextEditor 
     * an.
     *
     * @param l Der anzuh&auml;ngende ActionListener.
     */
    public void addActionListener(ActionListener l) {
        this.button.addActionListener(l);
    }

    /**
     * L&ouml;st den angegebenen ActionListener vom Button des &Auml;ndern-LineTextEditor.
     *
     * @param l Der zu l&ouml;sende ActionListener.
     */
    public void removeActionListener(ActionListener l) {
        this.button.removeActionListener(l);
    }

    
    /* Ueberschreiben von Methoden der Superklasse. */ 

    public void addKeyListener(KeyListener l) {
        this.textField.addKeyListener(l);
        this.button.addKeyListener(l);
    }

    public void removeKeyListener(KeyListener l) {
        this.textField.removeKeyListener(l);
        this.button.removeKeyListener(l);
    }

    public void requestFocus() {
        super.requestFocus();
        this.textField.requestFocus();
    }
    
    public boolean hasFocus() {
        return this.textField.hasFocus() || this.button.hasFocus();
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
