/*
 * FrameTextViewer.java
 *
 * 17.04.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import corent.base.*;
import corent.files.*;


/**
 * Dieser Frame zeigt die in dem &uuml;bergebenen Vector gespeicherten Objekt &uuml;ber deren 
 * <TT>toString()</TT>-Methode an und erm&ouml;glicht das Editieren des so erstellten Textes. 
 * Zus&auml;tzlich gibt es einen Button &uuml;ber den der Text in eine Datei gespeichert werden
 * kann.<BR>
 * <HR>
 * 
 * @author O.Lieshoff
 *
 * @changed OLI 11.09.2014 - Einbau eines "Copy all"-Buttons.
 */

public class FrameTextViewer extends JFrameWithInifile {

    /* Das JEdtorPane, in der der Text angezeigt und editiert werden kann. */
    private JEditorPane editorPane = null;
    /* Der initiale Datenpfad zum Speichern des angezeigten Textes. */
    private String datenpfad = null;
    /* 
     * Eine Liste mit Dateinamenserweiterungen, die zum Speichern genutzt werden k&ouml;nnen 
     * sollen. 
     */
    private String[] extensions = null;

    /** 
     * Generiert einen FrameTextViewer anhand der &uuml;bergebenen Parameter.
     *
     * @param text Ein Vector mit dem anzuzeigenden Text (jedes Vector-Element ist ein Absatz).
     * @param ftvcf Die FrameTextViewerComponentFactory, &uuml;ber die sich der Frame mit den 
     *    erforderlichen Komponenten versorgt.
     * @param ini Die Inidatei, aus der der Frame sich rekonstruiert.
     * @param titel Der Fenstertitel.
     * @param datenpfad Der voreingestellte Datenpfad zum eventuellen Speichern des Textes. 
     */
    public FrameTextViewer(Vector text, FrameTextViewerComponentFactory ftvcf, Inifile ini,
            String titel, String datenpfad) {
        super(titel, ini);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                doButtonBeenden();
            }
        });
        this.datenpfad = datenpfad;
        this.editorPane = ftvcf.createEditorPane();
        this.extensions = ftvcf.getFileExtensions();
        String s = "";
        for (int i = 0, len = text.size(); i < len; i++) {
            s += text.elementAt(i).toString() + "\n";
        }
        this.editorPane.setText(s);
        this.editorPane.setCaretPosition(0);
        JButton buttonBeenden = ftvcf.createButtonBeenden();
        buttonBeenden.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doButtonBeenden();
            }
        });
        buttonBeenden.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doButtonBeenden();
                }
            }
        });
        JButton buttonCopyAll = ftvcf.createButtonCopyAll();
        buttonCopyAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                copyAllContentToClipBoard();
            }
        });
        buttonCopyAll.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    copyAllContentToClipBoard();
                }
            }
        });
        JButton buttonSpeichern = ftvcf.createButtonSpeichern();
        buttonSpeichern.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doButtonSpeichern();
            }
        });
        buttonSpeichern.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doButtonSpeichern();
                }
            }
        });
        JPanel panelCenter = new JPanel(new GridLayout(1, 1, Constants.HGAP, Constants.VGAP));
        panelCenter.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH), 
                new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP))
                );
        panelCenter.add(new JScrollPane(this.editorPane));
        JPanel panelButtons = new JPanel(new GridLayout(1, 5, Constants.HGAP, Constants.VGAP));
        panelButtons.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH), 
                new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP))
                );
        panelButtons.add(buttonCopyAll);
        panelButtons.add(new JLabel());
        panelButtons.add(new JLabel());
        panelButtons.add(buttonSpeichern);
        panelButtons.add(buttonBeenden);
        JPanel panel = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
        panel.setBorder(new CompoundBorder(new EmptyBorder(Constants.HGAP, Constants.VGAP, 
                Constants.HGAP, Constants.VGAP), new EtchedBorder(Constants.ETCH)));
        panel.setBorder(new CompoundBorder(panel.getBorder(), new EmptyBorder(Constants.HGAP, 
                Constants.VGAP, Constants.HGAP, Constants.VGAP)));
        panel.add(panelCenter, BorderLayout.CENTER);
        panel.add(panelButtons, BorderLayout.SOUTH);
        this.setContentPane(panel);
        this.pack();
        this.setVisible(true);
        this.editorPane.requestFocus();
    }

    /* Diese Methode wird ausgef&uuml;hrt, wenn der Benutzer den Beenden-Button dr&uuml;ckt. */
    private void doButtonBeenden() {
        this.doClose();
    }

    private void copyAllContentToClipBoard() {
        String content = this.editorPane.getText();
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection ssel = new StringSelection(content);
        cb.setContents(ssel, ssel);
    }

    /* 
     * Diese Methode wird ausgef&uuml;hrt, wenn der Benutzer den Speichern-Button dr&uuml;ckt. 
     */
    private void doButtonSpeichern() {
        String dn = "";
        JFileChooser fc = new JFileChooser(this.datenpfad);
        fc.setAcceptAllFileFilterUsed(false);
        ExtensionFileFilter eff = new ExtensionFileFilter(this.extensions);
        fc.setFileFilter(eff);
        fc.setCurrentDirectory(new File(this.datenpfad));
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            dn = fc.getSelectedFile().getAbsolutePath();
            try {
                FileWriter fw = new FileWriter(dn, false);
                BufferedWriter writer = new BufferedWriter(fw);
                writer.write(this.editorPane.getText());
                writer.close();
                fw.close();
                this.doClose();
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(null, "Beim Schreiben der Datei trat ein Fehler "
                        + "auf:\n" + ioe.getMessage(), "IOException!", JOptionPane.YES_OPTION);
                ioe.printStackTrace();
            }
        }
    }
    
    /* Schlie&szlig;t den Frame und gibt die Ressourcen frei. */
    private void doClose() {
        this.setVisible(false);
        this.dispose();
    }
    
}
