/*
 * TimestampField.java
 *
 * 13.04.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corent.gui;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import corent.base.*;
import corent.dates.*;


/**
 * Das TimestampField bietet die M&ouml;glichkeit eine TimestampModel anzuzeigen und 
 * anwenderfreundlich zu manipulieren.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 27.03.2008 - Umziehen des Clear-Buttons an die erste Position der Buttonleiste, um 
 *             eine einheitliche Positionierung mit dem MassiveListSelector zu erreichen.
 *     <P>OLI 24.02.2009 - Erweiterung um die beiden Buttons zum einfachen Erh&ouml;hen bzw.
 *             Heruntersetzen des Tagesdatums um einen Tag.
 *     <P>OLI 30.03.2009 - Korrektur des Panelaufbaus bei Timestamps.
 *     <P>
 * 
 */

public class TimestampField extends JPanel {

    /** Die Komponente, in der das TimestampField abgebildet wird <I>(Default null)</I>. */
    protected Container container = null;
    /** Die Beschriftung des TimestampFields <I>(Default null)</I>. */
    protected JLabel label = null;
    /** Die textuelle Anzeige des TimestampFields <I>(Default null)</I>. */
    protected JTextField anzeige = null;
    /** Der Button, &uuml;ber den der Datumsdialog aufgerufen wird <I>(Default null)</I>. */
    protected JButton buttonDatum = null;
    /** Der Button, &uuml;ber den der Uhrzeitdialog aufgerufen wird <I>(Default null)</I>. */
    protected JButton buttonUhrzeit = null;
    /** Button zum L&ouml;schen des Timestamps <I>(Default null)</I>. */
    protected JButton buttonLoeschen = null;
    /** Button zum Vorschalten des Timestamps um einen Tag<I>(Default null)</I>. */
    protected JButton buttonTagPlus = null;
    /** Button zum Zur&uuml;ckschalten des Timestamps um einen Tag<I>(Default null)</I>. */
    protected JButton buttonTagMinus = null;
    
    /* Die TimestampFieldListener, die das TimestampField abh&ouml;ren. */
    private Vector listener = new Vector();
    
    /* Referenz auf die TimestampFieldComponentFactory zum TimestampField. */
    private TimestampFieldComponentFactory tfcf = 
            DefaultTimestampFieldComponentFactory.INSTANCE;
    /* Referenz auf den bearbeiteten Timestamp. */
    private TimestampModel timestamp = null;

    /**
     * Generiert ein TimestampField anhand der &uuml;bergebenen Parameter.
     *
     * @param container Die Komponente, in dem das TimestampField abgebildet wird.
     * @param tfcf Die TimestampFieldComponentFactory, aus der das TimestampField seine 
     *     Komponenten beziehen soll. 
     * @param timestamp Der anzuzeigende Timestamp.
     *
     * @changed
     *     OLI 27.03.2008 - Umziehen des Clear-Buttons an die erste Position der Buttonleiste, 
     *             um eine einheitliche Positionierung mit dem MassiveListSelector zu erreichen. 
     *     <P>OLI 24.02.2009 - Erweiterung um die beiden Buttons zum einfachen Erh&ouml;hen bzw.
     *             Heruntersetzen des Tagesdatums um einen Tag.
     *     <P>OLI 30.03.2009 - Debugging: Die Panelanordnung bei Timestamps hat die Buttons 
     *             f&uuml;r's L&ouml;schen und Verschieben geschluckt.
     *     <P>
     *
     */
    public TimestampField(Container container, TimestampFieldComponentFactory tfcf, 
            TimestampModel timestamp) {
        super(new BorderLayout(Constants.HGAP, Constants.VGAP));
        this.container = container;
        this.timestamp = timestamp;
        if (tfcf != null) {
            this.tfcf = tfcf;
        }
        this.label = this.tfcf.createLabel();
        this.anzeige = this.tfcf.createTextField();
        this.anzeige.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                doAnzeigeFocusLost();
            }
        });
        this.anzeige.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doAnzeigeEnter();
                } else if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_Z)) {
                    updateAnzeige();
                }
            }
        });
        char mnemonic = this.tfcf.getMnemonic();
        if (mnemonic != '\0') {
            this.label.setDisplayedMnemonic(mnemonic);
            this.label.setLabelFor(anzeige);
        }
        this.updateAnzeige();
        if (this.tfcf.isDateEnabled()) {
            this.buttonDatum = this.tfcf.createButtonDatum();
            this.buttonDatum.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doDatumAendern();
                }
            });
            this.buttonTagMinus = this.tfcf.createButtonTagMinus(); 
            this.buttonTagMinus.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doDatumMinusTag();
                }
            });
            this.buttonTagPlus = this.tfcf.createButtonTagPlus(); 
            this.buttonTagPlus.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doDatumPlusTag();
                }
            });
        }
        if (this.tfcf.isTimeEnabled()) {
            this.buttonUhrzeit = this.tfcf.createButtonUhrzeit();
            this.buttonUhrzeit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doUhrzeitAendern();
                }
            });
        }
        this.buttonLoeschen = this.tfcf.createButtonClear();
        this.buttonLoeschen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doLoeschen();
            }
        });
        if (this.label.getText().length() > 0) {
            this.add(this.label, BorderLayout.WEST);
        } else if (this.label instanceof Releaseable) {
            ((Releaseable) this.label).release();
        }
        this.add(this.anzeige, BorderLayout.CENTER);
        int compcount = 1;
        if (this.tfcf.isDateEnabled()) {
            //compcount = compcount+2;
            compcount++;
        }
        if (this.tfcf.isTimeEnabled()) {
            compcount++;
        }
        JPanel panelButtons = new JPanel(new GridLayout(1, compcount, Constants.HGAP, 
                Constants.VGAP));
        if (this.tfcf.isDateEnabled()) {
            JPanel panelPlusMinus = new JPanel(new GridLayout(1, 3, Constants.HGAP, 
                Constants.VGAP));
            panelButtons = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
            panelPlusMinus.add(this.buttonLoeschen);
            panelPlusMinus.add(this.buttonTagMinus);
            panelPlusMinus.add(this.buttonTagPlus);
            panelButtons.add(panelPlusMinus, BorderLayout.WEST);
            panelButtons.add(this.buttonDatum, BorderLayout.EAST);
        } else {
            panelButtons.add(this.buttonLoeschen);
        }
        if (this.tfcf.isTimeEnabled()) {
            panelButtons.add(this.buttonUhrzeit);
        }
        this.add(panelButtons, BorderLayout.EAST);
    }

    /** @return Der in dem TimestampField angezeigte Timestamp. */
    public TimestampModel getTimestamp() {
        return this.timestamp;
    }
    
    /** @return Der in dem TimestampField angezeigte Timestamp als PDate (also ohne Urzeit). */
    public PDate getPDate() {
        if (this.timestamp.toLong() > 0) {
            return new PDate(this.timestamp.get(TimestampUnit.DAY), this.timestamp.get(
                    TimestampUnit.MONTH), this.timestamp.get(TimestampUnit.YEAR));
        }
        return PDate.UNDEFINIERT;
    }
    
    /** @return Der in dem TimestampField angezeigte Timestamp als PTime (also ohne Datum). */
    public PTime getPTime() {
        if (this.timestamp.toLong() > 0) {
            return new PTime(this.timestamp.get(TimestampUnit.HOUR), this.timestamp.get(
                    TimestampUnit.MINUTE), this.timestamp.get(TimestampUnit.SECOND));
        }
        return PTime.UNDEFINIERT;
    }
    
    /**
     * Setzt den &uuml;bergebenen Timestamp als neuen Timestamp f&uuml;r das TimestampField ein.
     *
     * @param tsm Der neueinzusetzende Timestamp.
     */
    public void setDate(TimestampModel tsm) {
        this.timestamp = tsm;
        this.updateAnzeige();
    }

    /** Aktualisiert die Anzeige des TimestampFields. */
    public void updateAnzeige() {
        this.anzeige.setBackground(Color.white);
        if (!this.tfcf.isTimeEnabled()) {
            this.anzeige.setText(this.getPDate().toString());
        } else if (!this.tfcf.isDateEnabled()) {
            this.anzeige.setText(this.getPTime().toString());
        } else {
            this.anzeige.setText(this.timestamp.toString());
        }
    }

    /**
     * Diese Methode kommt zur Ausf&uuml;hrung, wenn der Eingabe-Focus das Anzeigefeld
     * verl&auml;&szlig;. In dieser Standardimplementierung wird bei einem Focus-Lost-Event die
     * Methode doAnzeigeEnter() aufgerufen.
     */
    public void doAnzeigeFocusLost() {
    }

    /**
     * Diese Methode wird aufgerufen, wenn der Benutzer die Entertaste gedr&uuml;ckt hat. Der
     * Inhalt des Eingabefeldes wird ausgewertet und in das angezeigte PTimestamp-Objekt 
     * &uuml;bernommen. Ist der Inhalt ung&uuml;ltig, so wird ein Beep ausgegeben und der Fokus
     * in das Feld zur&uuml;ckgef&uuml;hrt.
     */
    public void doAnzeigeEnter() {
        try {
            if (this.timestamp == PTimestamp.NULL) {
                this.timestamp = new PTimestamp(10101000000l);
            }
            if (!this.tfcf.isTimeEnabled()) {
                PDate pd = PDate.valueOf(this.anzeige.getText().trim());
                this.timestamp = new PTimestamp(this.timestamp);
                this.timestamp.set(TimestampUnit.DAY, 1);
                this.timestamp.set(TimestampUnit.YEAR, pd.getJahr());
                this.timestamp.set(TimestampUnit.MONTH, pd.getMonat());
                this.timestamp.set(TimestampUnit.DAY, pd.getTag());
            } else if (!this.tfcf.isDateEnabled()) {
                PTime pt = PTime.valueOf(this.anzeige.getText().trim());
                this.timestamp = new PTimestamp(this.timestamp);
                this.timestamp.set(TimestampUnit.HOUR, pt.getStunde());
                this.timestamp.set(TimestampUnit.MINUTE, pt.getMinute());
                this.timestamp.set(TimestampUnit.SECOND, pt.getSekunde());
                this.fireTimeChanged(this.timestamp);
            } else {
                this.timestamp = new PTimestamp(this.anzeige.getText().trim());
            }
            this.updateAnzeige();
        } catch (Exception e) {         
            Toolkit.getDefaultToolkit().beep();
            this.anzeige.setBackground(Color.red);
            this.anzeige.requestFocus();
        }
    }

    /**
     * Diese Methode wird ausgef&uuml;hrt, wenn der Benutzer den Button zum &Auml;ndern des 
     * Datums dr&uuml;ckt.
     */
    public void doDatumAendern() {
        if (this.timestamp.equals(PTimestamp.NULL)) {
            this.timestamp = new PTimestamp();
            this.timestamp.set(TimestampUnit.HOUR, 0);
            this.timestamp.set(TimestampUnit.MINUTE, 0);
            this.timestamp.set(TimestampUnit.SECOND, 0);
        }
        DateDialog pdd = new DateDialog(Utl.GetProperty(
                "corent.gui.DefaultTimestampFieldComponentFactory.datedialog.title", 
                "&Auml;ndern Datum"), new PDate((int) (this.timestamp.toLong()
                / 1000000l)));
        if (pdd.getDate() != null) {
            this.timestamp = new PTimestamp(this.timestamp);
            this.timestamp.set(TimestampUnit.DAY, 1);
            this.timestamp.set(TimestampUnit.YEAR, pdd.getDate().getJahr());
            this.timestamp.set(TimestampUnit.MONTH, pdd.getDate().getMonat());
            this.timestamp.set(TimestampUnit.DAY, pdd.getDate().getTag());
            this.fireDateChanged(this.timestamp);
            this.updateAnzeige();
        }
        pdd.release();
    }
    
    /** 
     * Verschiebt das aktuelle Datum um einen Tag nach hinten (zu&uuml;ck).
     *
     * @changed
     *     OLI 24.02.2009 - Hinzugef&uuml;gt
     *     <P>
     *
     */
    public void doDatumMinusTag() {
        this.timestamp = this.timestamp.add(TimestampUnit.DAY, -1);
        this.fireDateChanged(this.timestamp);
        this.updateAnzeige();
    }

    /** 
     * Verschiebt das aktuelle Datum um einen Tag nach vorn.
     *
     * @changed
     *     OLI 24.02.2009 - Hinzugef&uuml;gt
     *     <P>
     *
     */
    public void doDatumPlusTag() {
        this.timestamp = this.timestamp.add(TimestampUnit.DAY, 1);
        this.fireDateChanged(this.timestamp);
        this.updateAnzeige();
    }

    /**
     * Diese Methode wird ausgef&uuml;hrt, wenn der Benutzer den Button zum &Auml;ndern der 
     * Uhrzeit dr&uuml;ckt.
     */
    public void doUhrzeitAendern() {
        if (this.timestamp.equals(PTimestamp.NULL)) {
            this.timestamp = new PTimestamp();
        }
        TimeInputDialog td = this.tfcf.createTimeInputDialog(this.timestamp);
        /*        
        TimeInputDialog td = null;
        String tidcn = System.getProperty("corent.gui.TimestampField.alternate.timedialog", "");
        if (tidcn.length() > 0) {
            try {
                Class cls = Class.forName(tidcn);
                td = (TimeInputDialog) cls.newInstance(
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("\nError: TimeDialog could'nt be build from class:" + tidcn);
            }
        if (td == null) {
            TimeInputDialog td = new TimeDialog("Zeit ändern", null, (this.timestamp == 
                    PTimestamp.NULL ? new PTimestamp() : this.timestamp), 
                    this.tfcf.createTimeDialogComponentFactory());
        }
        */
        if (td.isConfirmed()) {
            this.timestamp = new PTimestamp(this.timestamp);
            this.timestamp.set(TimestampUnit.HOUR, td.getPTime().getStunde());
            this.timestamp.set(TimestampUnit.MINUTE, td.getPTime().getMinute());
            this.timestamp.set(TimestampUnit.SECOND, td.getPTime().getSekunde());
            this.fireTimeChanged(this.timestamp);
            this.updateAnzeige();
        }
    }

    /** 
     * Diese Methode wird ausgef&uuml;hrt, wenn der Benutzer den L&ouml;schen-Button 
     * dr&uuml;ckt.
     */
    public void doLoeschen() {
        this.timestamp = PTimestamp.NULL;
        this.fireDateChanged(this.timestamp);
        this.fireTimeChanged(this.timestamp);
        this.updateAnzeige();
    }

    /**
     * F&uuml;gt den &uuml;bergebenen Listener der Liste der das TimestampField abh&ouml;renden
     * TimestampFieldListener hinzu.
     *
     * @param l Der TimestampFieldListener, der der Liste der TimestampFieldListener 
     *     hinzugef&uuml;gt werden soll, die das TimestampField abh&ouml;ren.
     */
    public void addTimestampFieldListener(TimestampFieldListener l) {
        if (!this.listener.contains(l)) {
            this.listener.addElement(l);
        }
    }
    
    /**
     * L&ouml;scht den &uuml;bergebenen Listener aus der Liste der das TimestampField 
     * abh&ouml;renden TimestampFieldListener.
     *
     * @param l Der TimestampFieldListener, der aus der Liste der TimestampFieldListener 
     *     gel&ouml;scht werden soll, die das TimestampField abh&ouml;ren.
     */
    public void removeTimestampFieldListener(TimestampFieldListener l) {
        if (!this.listener.contains(l)) {
            this.listener.removeElement(l);
        }
    }
    
    /**
     * Benachrichtigt die das TimestampField abh&ouml;renden Listener &uuml;ber eine 
     * &Auml;nderung des Datums.
     *
     * @param ts Der durch die &Auml;nderung erzeugte Timestamp.
     */
    public void fireDateChanged(TimestampModel ts) {
        for (int i = 0, len = this.listener.size(); i < len; i++) {
            ((TimestampFieldListener) this.listener.elementAt(i)).dateChanged(ts);
        }
    }
    
    /**
     * Benachrichtigt die das TimestampField abh&ouml;renden Listener &uuml;ber eine 
     * &Auml;nderung der Uhrzeit.
     *
     * @param ts Der durch die &Auml;nderung erzeugte Timestamp.
     */
    public void fireTimeChanged(TimestampModel ts) {
        for (int i = 0, len = this.listener.size(); i < len; i++) {
            ((TimestampFieldListener) this.listener.elementAt(i)).timeChanged(ts);
        }
    }
    
    
    /* Ueberschreiben von Methoden der Superklasse. */ 

    public void addActionListener(ActionListener l) {
        if (this.buttonDatum != null) {
            this.buttonDatum.addActionListener(l);
        }
        if (this.buttonUhrzeit != null) {
            this.buttonUhrzeit.addActionListener(l);
        }
        this.buttonLoeschen.addActionListener(l);
    }

    public void removeActionListener(ActionListener l) {
        if (this.buttonDatum != null) {
            this.buttonDatum.removeActionListener(l);
        }
        if (this.buttonUhrzeit != null) {
            this.buttonUhrzeit.removeActionListener(l);
        }
        this.buttonLoeschen.removeActionListener(l);
    }

    public void addKeyListener(KeyListener l) {
        this.anzeige.addKeyListener(l);
        if (this.buttonDatum != null) {
            this.buttonDatum.addKeyListener(l);
        }
        if (this.buttonUhrzeit != null) {
            this.buttonUhrzeit.addKeyListener(l);
        }
        this.buttonLoeschen.addKeyListener(l);
    }

    public void removeKeyListener(KeyListener l) {
        this.anzeige.removeKeyListener(l);
        if (this.buttonDatum != null) {
            this.buttonDatum.removeKeyListener(l);
        }
        if (this.buttonUhrzeit != null) {
            this.buttonUhrzeit.removeKeyListener(l);
        }
        this.buttonLoeschen.removeKeyListener(l);
    }

    public void requestFocus() {
        if (this.isEnabled()) {
            super.requestFocus();
            this.anzeige.requestFocus();
        }
    }
    
    public boolean hasFocus() {
        return this.anzeige.hasFocus() || (this.buttonDatum == null ? false : 
                this.buttonDatum.hasFocus())  || this.buttonLoeschen.hasFocus() || 
                (this.buttonUhrzeit == null ? false : this.buttonUhrzeit.hasFocus());
    }
    
    public void setEnabled(boolean b) {
        this.anzeige.setEnabled(b);
        if (this.buttonDatum != null) {
            this.buttonDatum.setEnabled(b);
            this.buttonTagMinus.setEnabled(b);
            this.buttonTagPlus.setEnabled(b);
        }
        this.buttonLoeschen.setEnabled(b);
        if (this.buttonUhrzeit != null) {
            this.buttonUhrzeit.setEnabled(b);
        }
    }
    
    public boolean isEnabled() {
        return this.anzeige.isEnabled() || (this.buttonDatum == null ? false : 
                this.buttonDatum.isEnabled())  || this.buttonLoeschen.isEnabled() || 
                (this.buttonUhrzeit == null ? false : this.buttonUhrzeit.isEnabled());
    }    
    
}