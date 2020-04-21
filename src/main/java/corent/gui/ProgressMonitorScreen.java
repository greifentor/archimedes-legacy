/*
 * ProgressMonitorScreen.java
 *
 * 21.03.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import java.awt.*;
import java.util.*; 

import javax.swing.*;
import javax.swing.border.*;

import corent.base.*;


/**
 * Diese Klasse bietet einen ProgressMonitor im Rahmen eines JDialogs an. Der Monitor ist zwar 
 * nicht modal, h&auml;t sich jedoch hartn&auml;ckig im Vordergrund.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
abstract public class ProgressMonitorScreen extends JDialog {
    
    /* Der ProgressBar des Dialogs. */
    private JProgressBar progressBar = new JProgressBar(0, 100);
    
    /**
     * Generiert einen ProgressMonitor anhand der &uuml;bergebenen Parameter.
     *
     * @param caller Das aufrufende Fenster.
     * @param title Die Titelzeile f&uuml;r den ProgressMonitor.
     * @param parameter Eine Hashtable mit eventuellen Parametern f&uuml;r den ProgressMonitor. 
     * @param bild Eine Referenz auf ein Bild, das in dem Monitor oberhalb des Progressbars 
     *     angezeigt werden soll.
     */
    public ProgressMonitorScreen(JFrame caller, String title, Hashtable parameter, 
            ImageIcon bild) {
        super(caller, (title != null ? title : "Progressmonitor"), false);
        /*
        final Hashtable p = parameter;
        this.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                progress(p);
            }
        });
        */
        this.progressBar.setStringPainted(true);
        JPanel panel = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
        panel.setBorder(new CompoundBorder(new EmptyBorder(Constants.HGAP, Constants.VGAP, 
                Constants.HGAP, Constants.VGAP), new EtchedBorder(Constants.ETCH)));
        panel.setBorder(new CompoundBorder(panel.getBorder(), new EmptyBorder(Constants.HGAP, 
                Constants.VGAP, Constants.HGAP, Constants.VGAP)));
        if (bild != null) {
            panel.add(new JLabel(bild));
        }
        panel.add(this.progressBar, BorderLayout.SOUTH);
        this.progressBar.setString("0%");
        this.setContentPane(panel);
        this.pack();
        Rectangle r = null;
        if (this.getOwner() != null) {
            r = this.getOwner().getBounds();
        } else {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            r = new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight());
        }
        Dimension d = this.getPreferredSize();
        d.setSize(d.getWidth() * 2, d.getHeight());
        this.setSize(d);
        this.setBounds((r.width / 2) - (this.getWidth() / 2) + r.x, (r.height / 2) -
                (this.getHeight() / 2) + r.y, this.getWidth(), this.getHeight());
        this.setVisible(true);
        this.progress(parameter);
    }
    
    /**
     * Setzt den neuen Wert in den ProgressMonitor ein.
     * 
     * @param n Der neue Wert.
     */
    public void update(int n) {
        this.progressBar.setValue(n);
        this.progressBar.setString("" + n + "%");
        this.update(this.getGraphics());
    }
    
    /** Schlie&szlig;t den ProgressMonitor. */
    public void close() {
        this.dispose();
    }
    
    
    /* Abstrakte Methode des Monitors. */
    
    abstract public void progress(Hashtable parameter);
    
}
