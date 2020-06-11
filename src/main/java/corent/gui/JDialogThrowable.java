/*
 * JDialogThrowable.java
 *
 * 04.05.2009
 *
 * (c) by O.Lieshoff
 *
 */

package corent.gui;


import corent.base.*;
import corent.files.*;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;


/**
 * Dieser Dialog dient der Anzeige von Throwables mit zu&auml;tzlichen Erl&auml;uterungen zur
 * Anzeige im Frontend.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 04.05.2009 - Hinzugef&uuml;gt
 * @changed OLI 17.06.2010 - Erweiterung um einen nicht modalen Modus.
 */

public class JDialogThrowable extends JDialogWithInifile implements ActionListener {

    private static final String LS = System.getProperty("line.separator");

    private COButton buttonCancel = new COButton("$cancel",
            ".corent.gui.JDialogThrowable.button.cancel");
    private COButton buttonCopy = new COButton("$copy", 
            ".corent.gui.JDialogThrowable.button.copy");
    private Icon icon = null;
    private JLabel labelExceptionClass = new JLabel();
    private JTextArea textAreaThrowableMessage = new JTextArea(2, 60);
    private JTextArea textAreaThrowableStackTrace = new JTextArea(20, 60);
    private JTextArea textAreaUserMessage = new JTextArea(2, 60);
    private String userMessage = null;
    private Throwable thrwble= null;

    /**
     * Generiert ein JDialogThrowable-Objekt anhand der &uuml;bergebenen Parameter.
     *
     * @param thrwble Das in dem Fenster anzuzeigende Throwable.
     * @param userMessage Ein Fehlermeldungstext, mit auch ein Benutzer etwas anfangen kann.
     * @param ini Die Inidatei mit aus der das Fenster gegebenenfalls sein &Auml;&szlig;eres
     *         rekonstruieren kann.
     * @param rm Der RessourceManager, &uuml;ber den das Fenster seine Inhalte beziehen soll.
     */
    public JDialogThrowable(Throwable thrwble, String userMessage, Inifile ini, 
            RessourceManager rm) {
        this(thrwble, userMessage, ini, rm, true);
    }

    /**
     * Generiert ein JDialogThrowable-Objekt anhand der &uuml;bergebenen Parameter.
     *
     * @param thrwble Das in dem Fenster anzuzeigende Throwable.
     * @param userMessage Ein Fehlermeldungstext, mit auch ein Benutzer etwas anfangen kann.
     * @param ini Die Inidatei mit aus der das Fenster gegebenenfalls sein &Auml;&szlig;eres
     *         rekonstruieren kann.
     * @param rm Der RessourceManager, &uuml;ber den das Fenster seine Inhalte beziehen soll.
     * @param modal Diese Flagge ist zu setzen, wenn der Dialog Modal genutzt werden soll.
     */
    public JDialogThrowable(Throwable thrwble, String userMessage, Inifile ini, 
            RessourceManager rm, boolean modal) {
        super(ini);
        int i = 0;
        int leni = 0;
        JPanel pButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, Constants.HGAP, 
                Constants.VGAP));
        JPanel pHeader = this.buildPanel();
        JPanel pHeaderSub = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP)); 
        JPanel pMain = this.buildPanel();
        JPanel pMessages = new JPanel(new GridLayout(2, 1, Constants.HGAP, Constants.VGAP));
        JPanel pStackTrace = this.buildPanel();
        StackTraceElement[] ste = thrwble.getStackTrace();
        StringBuffer t = new StringBuffer();
        this.thrwble = thrwble;
        this.userMessage = (userMessage != null ? userMessage : Utl.GetProperty(
                ".corent.gui.JDialogThrowable.user.message.null", "n/a"));
        this.setModal(modal);
        this.setTitle(Utl.GetProperty(".corent.gui.JDialogThrowable.title.prefix", 
                "Exception") + (thrwble.getMessage() != null ? ":&nbsp;" + thrwble.getMessage()
                : ""));
        for (i = 0, leni = ste.length; i < leni; i++) {
            t.append(ste[i].toString()).append(LS);
        }
        pButtons.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH), new EmptyBorder(
                Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP)));
        pMain.setBorder(new CompoundBorder(new EmptyBorder(Constants.HGAP, Constants.VGAP, 
                Constants.HGAP, Constants.VGAP), pMain.getBorder()));
        this.buttonCancel.addActionListener(this);
        this.buttonCancel.update(rm);
        this.buttonCopy.addActionListener(this);
        this.buttonCopy.update(rm);
        this.icon = rm.getIcon("corent.gui.JDialogThrowable.image");
        this.labelExceptionClass.setText(thrwble.getClass().getName());
        this.labelExceptionClass.setFont(new Font(Utl.GetProperty(
                ".corent.gui.JDialogThrowable.throwable.class.font", "sansserif"), Font.BOLD, 18
                ));
        this.textAreaThrowableMessage.setText(thrwble.getMessage());
        this.textAreaThrowableMessage.setEditable(false);
        this.textAreaThrowableStackTrace.setText(t.toString());
        this.textAreaThrowableStackTrace.setEditable(false);
        this.textAreaUserMessage.setText(this.userMessage);
        this.textAreaUserMessage.setEditable(false);
        this.textAreaUserMessage.setFont(new Font(Utl.GetProperty(
                ".corent.gui.JDialogThrowable.user.message.font", "sansserif"), Font.BOLD, 14));
        pButtons.add(this.buttonCopy);
        pButtons.add(this.buttonCancel);
        pHeaderSub.add(this.labelExceptionClass, BorderLayout.NORTH);
        pMessages.add(new JScrollPane(this.textAreaUserMessage));
        pMessages.add(new JScrollPane(this.textAreaThrowableMessage));
        pHeaderSub.add(pMessages, BorderLayout.CENTER);
        pHeader.add(pHeaderSub, BorderLayout.CENTER);
        if (this.icon != null) {
            pHeader.add(new JLabel(this.icon), BorderLayout.WEST);
        }
        pStackTrace.add(new JScrollPane(this.textAreaThrowableStackTrace), BorderLayout.CENTER);
        pMain.add(pHeader, BorderLayout.NORTH);
        pMain.add(pStackTrace, BorderLayout.CENTER);
        pMain.add(pButtons, BorderLayout.SOUTH);
        this.setContentPane(pMain);
        this.pack();
        this.setVisible(true);
    }

    private JPanel buildPanel() {
        JPanel p = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
        p.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH), new EmptyBorder(
                Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP)));
        return p;
    }

    /** Diese Methode wird ausgef&uuml;hrt, wenn der Benutzer den Cancel-Button dr&uuml;ckt. */
    public void cancel() {
        this.setVisible(false);
    }

    /** Diese Methode wird ausgef&uuml;hrt, wenn der Benutzer den Copy-Button dr&uuml;ckt. */
    public void copy() {
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        int i = 0;
        int leni = 0;
        StackTraceElement[] ste = thrwble.getStackTrace();
        StringBuffer throwabletext = new StringBuffer();
        StringSelection ssel = null;
        throwabletext.append(Utl.GetProperty(
                ".corent.gui.JDialogThrowable.report.prefix.message", "exception:&nbsp;")
                ).append(this.thrwble.getMessage()).append(LS).append(LS);
        throwabletext.append(Utl.GetProperty(
                ".corent.gui.JDialogThrowable.report.prefix.user.message", "message:&nbsp;")
                ).append(this.userMessage).append(LS).append(LS);
        throwabletext.append(Utl.GetProperty(
                ".corent.gui.JDialogThrowable.report.prefix.class", "class:&nbsp;")
                ).append(this.thrwble.getClass().getName()).append(LS).append(LS);
        throwabletext.append(Utl.GetProperty(
                ".corent.gui.JDialogThrowable.report.prefix.stacktrace", "stacktrace:"));
        for (i = 0, leni = ste.length; i < leni; i++) {
            throwabletext.append(LS).append(ste[i].toString());
        }
        ssel = new StringSelection(throwabletext.toString());
        cb.setContents(ssel, ssel);
    }


    /* Implementierung des Interfaces ActionListener. */

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == this.buttonCancel) {
            this.cancel();
        } else if (source == this.buttonCopy) {
            this.copy();
        }
    }

}
