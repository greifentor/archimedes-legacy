/*
 * TimeDialog.java
 *
 * 13.04.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import corent.base.*;
import corent.dates.*;
import corent.files.*;


/**
 * Mit Hilfe dieses Dialoges kann der Benutzer eine Zeitangabe editieren.
 * 
 * @author O.Lieshoff
 *
 */

public class TimeDialog extends JDialogWithInifile implements Releaseable, TimeInputDialog {
    
    /* Diese Flagge wird auf "true" gesetzt, wenn der Dialog mit Ok beendet worden ist. */
    private boolean ok = false;
    /* Das SpinField zur Eingabe der Minuten. */
    private SpinField spinFieldMinuten = null;
    /* Das SpinField zur Eingabe der Sekunden. */
    private SpinField spinFieldSekunden = null;
    /* Das SpinField zur Eingabe der Stunden. */
    private SpinField spinFieldStunden = null;
    /* 
     * Referenz auf die TimeDialogComponentFactory, die f&uuml;r die Generierung der Komponenten
     * des Dialoges genutzt werden soll bzw. null, wenn Standardkomponenten zum Einsatz kommen 
     * sollen.
     */
    private TimeDialogComponentFactory tdcf = DefaultTimeDialogComponentFactory.INSTANCE;
    /* Der Zeitstempel, dessen Zeitangabe bearbeitet werden soll. */
    private TimestampModel tsm = null;
    
    /**
     * Generiert einen TimeDialog anhand der &uuml;bergebenen Parameter.
     *
     * @param title Der Titel zum Dialog.
     * @param ini Das Inifile, aus dem der Dialog sich rekonstruieren soll.
     * @param tsm Der zu manipulierende Timestamp.
     */
    public TimeDialog(String title, Inifile ini, TimestampModel tsm) {
        this(title, ini, tsm, null);
    }
    
    /**
     * Generiert einen TimeDialog anhand der &uuml;bergebenen Parameter.
     *
     * @param title Der Titel zum Dialog.
     * @param ini Das Inifile, aus dem der Dialog sich rekonstruieren soll.
     * @param tsm Der zu manipulierende Timestamp.
     * @param tdcf Die TimeDialogComponentFactory, aus der die Komponente des TimeDialoges 
     *     gewonnen werden sollen. 
     */
    public TimeDialog(String title, Inifile ini, TimestampModel tsm, 
            TimeDialogComponentFactory tdcf) {
        super(ini);
        this.setModal(true);
        this.setTitle(title);
        this.tsm = tsm;
        if (tdcf != null) {
            this.tdcf = tdcf;
        }
        KeyAdapter ka = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    doButtonVerwerfen();
                }
            }
        };
        this.spinFieldMinuten = new SpinField(0, 59, this.tsm.get(TimestampUnit.MINUTE));
        this.spinFieldMinuten.addKeyListener(ka);
        this.spinFieldSekunden = new SpinField(0, 59, this.tsm.get(TimestampUnit.SECOND));
        this.spinFieldSekunden.addKeyListener(ka);
        this.spinFieldStunden = new SpinField(0, 23, this.tsm.get(TimestampUnit.HOUR));
        this.spinFieldStunden.addKeyListener(ka);
        JPanel panelSpins = new JPanel(new GridLayout(1, 3, Constants.VGAP, Constants.HGAP));
        panelSpins.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH), 
                new EmptyBorder(Constants.VGAP, Constants.HGAP, Constants.VGAP, Constants.HGAP))
                );
        JPanel panelButtons = new JPanel(new GridLayout(1, 3, Constants.VGAP, Constants.HGAP));
        panelButtons.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH), 
                new EmptyBorder(Constants.VGAP, Constants.HGAP, Constants.VGAP, Constants.HGAP))
                );
        JPanel panel = new JPanel(new BorderLayout(Constants.VGAP, Constants.HGAP));
        panel.setBorder(new CompoundBorder(new EmptyBorder(Constants.VGAP, 
                Constants.HGAP, Constants.VGAP, Constants.HGAP), new EtchedBorder(
                Constants.ETCH)));
        panel.setBorder(new CompoundBorder(panel.getBorder(), new EmptyBorder(Constants.VGAP, 
                Constants.HGAP, Constants.VGAP, Constants.HGAP)));
        JButton buttonOk = this.tdcf.createButtonOk();
        buttonOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doButtonOk();
            }
        });
        buttonOk.addKeyListener(ka);
        buttonOk.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doButtonOk();
                } 
            }
        });
        JButton buttonVerwerfen = this.tdcf.createButtonAbbruch();
        buttonVerwerfen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doButtonVerwerfen();
            }
        });
        buttonVerwerfen.addKeyListener(ka);
        buttonVerwerfen.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doButtonVerwerfen();
                } 
            }
        });
        panelButtons.add(new JLabel());
        panelButtons.add(buttonOk);
        panelButtons.add(buttonVerwerfen);
        panelSpins.add(this.spinFieldStunden);
        panelSpins.add(this.spinFieldMinuten);
        panelSpins.add(this.spinFieldSekunden);
        panel.add(panelButtons, BorderLayout.SOUTH);
        panel.add(panelSpins, BorderLayout.CENTER);
        this.setContentPane(panel);
        this.pack();
        this.setVisible(true);
    }
    
    /** Diese Methode wird ausgef&uuml;ht, wenn der Benutzer den Ok-Button anclickt. */
    public void doButtonOk() {
        this.ok = true;
        this.setVisible(false);
        this.release();
        this.dispose();
    }
    
    /** Diese Methode wird ausgef&uuml;ht, wenn der Benutzer den Verwerfen-Button anclickt. */
    public void doButtonVerwerfen() {
        this.setVisible(false);
        this.release();
        this.dispose();
    }
    
    /** @return <TT>true</TT>, wenn der Dialog mit dem Ok-Button beendet worden ist. */
    public boolean isOkPressed() {
        return this.ok;
    }
    
    /** @return Der Timestamp mit den aktuellen Daten. */
    public TimestampModel getTimestamp() {
        this.tsm.set(TimestampUnit.HOUR, (int) this.spinFieldStunden.getValue());
        this.tsm.set(TimestampUnit.MINUTE, (int) this.spinFieldMinuten.getValue());
        this.tsm.set(TimestampUnit.SECOND, (int) this.spinFieldSekunden.getValue());
        return this.tsm;
    }
    
    
    /* Implementierung des Interfaces Releaseable. */
    
    public void release() {
        ReleaseUtil.ReleaseContainer(this.getContentPane());
    }
    
    
    /* Implementierung des Interfaces TimeInputDialog. */
    
    public boolean isConfirmed() {
        return this.isOkPressed();
    }
    
    public PTime getPTime() {
        return new PTime((int) this.spinFieldStunden.getValue(), 
                (int) this.spinFieldMinuten.getValue(), (int) this.spinFieldSekunden.getValue()
                );
    }
    
    public void setPTime(PTime pt) {
        this.spinFieldStunden.setValue(pt.getStunde());
        this.spinFieldMinuten.setValue(pt.getMinute());
        this.spinFieldSekunden.setValue(pt.getSekunde());
    }

}
