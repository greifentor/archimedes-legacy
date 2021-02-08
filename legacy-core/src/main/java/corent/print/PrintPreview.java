/*
 * PrintPreview.java
 *
 * 10.08.2003
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.print;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import corent.base.*;
import corent.files.*;
import corent.gui.*;


/**
 * Diese Klasse bietet ein Fenster an, in dem ein Druckvorschau realisiert werden kann.
 * <BR><HR>
 *
 * @author O.Lieshoff
 *
 */

public class PrintPreview extends JDialogWithInifile {
    
    /** 
     * Referenz auf den abgebildeten PrintJob (so es einen gibt, sonst null) <I>(Default null)
     * </I>. 
     */
    protected CorePrintJob cpj = null;
    /** 
     * Flagge, die gesetzt wird, wenn der Dialog &uuml;ber den Abbruch-Button beendet wird 
     * <I>(Default false).
     */
    protected boolean canceled = true;
    /** Referenz auf den Abbruch-Button <I>(Default null)</I>. */
    protected JButton buttonAbbruch = null;
    /** Referenz auf den Ok-Button <I>(Default null)</I>. */
    protected JButton buttonOk = null;
    /** Referenz auf den Page+-Button <I>(Default null)</I>. */
    protected JButton buttonPagePlus = null;
    /** Referenz auf den Page--Button <I>(Default null)</I>. */
    protected JButton buttonPageMinus = null;
    /** Referenz auf eine eventuell vorhandene PreviewComponent <I>(Default null)</I>. */
    protected PreviewComponent pvc = null; 
    /** Der Sicherheits- und Ressourcenkontext zum Preview <I>(Default null)</I>. */
    protected String kontext = null;
    
    
    /**
     * Generiert und &ouml;ffnet einen JDialog zur Anzeige des &uuml;bergebenen Objektes in 
     * einer Druckvorschau.
     *
     * @param pv Das Previewable, das angezeigt werden soll,
     * @param ini Die Inidatei, aus der das Fenster rekonstruiert werden soll.
     * @param dim Die Ausdehnung der anzuzeigenden Seite.
     */
    public PrintPreview(Previewable pv, Inifile ini, Dimension dim) {
        super(ini);
        this.construct(dim, pv, null);
    }
    
    /**
     * Generiert und &ouml;ffnet einen JDialog zur Anzeige des &uuml;bergebenen Objektes in 
     * einer Druckvorschau.
     *
     * @param pv Das Previewable, das angezeigt werden soll,
     * @param ini Die Inidatei, aus der das Fenster rekonstruiert werden soll.
     * @param dim Die Ausdehnung der anzuzeigenden Seite.
     * @param cpj Der CorePrintJob, der in dem Preview dargestellt werden soll.
     */
    public PrintPreview(Previewable pv, Inifile ini, Dimension dim, CorePrintJob cpj) {
        super(ini);
        this.construct(dim, pv, cpj);
    }
    
    /**
     * Generiert und &ouml;ffnet einen JDialog zur Anzeige des &uuml;bergebenen Objektes in 
     * einer Druckvorschau.
     *
     * @param img Ein ImageIcon, das angezeigt werden soll.
     * @param ini Die Inidatei, aus der das Fenster rekonstruiert werden soll.
     */
    public PrintPreview(ImageIcon img, Inifile ini) {
        super(ini);
        this.construct(null, img, null);
    }
    
    /**
     * Generiert und &ouml;ffnet einen JDialog zur Anzeige des &uuml;bergebenen Objektes in 
     * einer Druckvorschau.
     *
     * @param img Ein ImageIcon, das angezeigt werden soll.
     * @param ini Die Inidatei, aus der das Fenster rekonstruiert werden soll.
     * @param cpj Der CorePrintJob, der in dem Preview dargestellt werden soll.
     */
    public PrintPreview(ImageIcon img, Inifile ini, CorePrintJob cpj) {
        super(ini);
        this.construct(null, img, cpj);
    }
    
    /**
     * Generiert und &ouml;ffnet einen JDialog zur Anzeige des &uuml;bergebenen Objektes in 
     * einer Druckvorschau.
     *
     * @param pv Das Previewable, das angezeigt werden soll,
     * @param ini Die Inidatei, aus der das Fenster rekonstruiert werden soll.
     * @param dim Die Ausdehnung der anzuzeigenden Seite.
     * @param kontext Der Kontext f&uuml;r den Sicherheits- und Ressourcenmanager.
     */
    public PrintPreview(Previewable pv, Inifile ini, Dimension dim, String kontext) {
        super(ini);
        this.construct(dim, pv, null, kontext);
    }
    
    /**
     * Generiert und &ouml;ffnet einen JDialog zur Anzeige des &uuml;bergebenen Objektes in 
     * einer Druckvorschau.
     *
     * @param pv Das Previewable, das angezeigt werden soll,
     * @param ini Die Inidatei, aus der das Fenster rekonstruiert werden soll.
     * @param dim Die Ausdehnung der anzuzeigenden Seite.
     * @param cpj Der CorePrintJob, der in dem Preview dargestellt werden soll.
     * @param kontext Der Kontext f&uuml;r den Sicherheits- und Ressourcenmanager.
     */
    public PrintPreview(Previewable pv, Inifile ini, Dimension dim, CorePrintJob cpj,
            String kontext) {
        super(ini);
        this.construct(dim, pv, cpj, kontext);
    }
    
    /**
     * Generiert und &ouml;ffnet einen JDialog zur Anzeige des &uuml;bergebenen Objektes in 
     * einer Druckvorschau.
     *
     * @param img Ein ImageIcon, das angezeigt werden soll.
     * @param ini Die Inidatei, aus der das Fenster rekonstruiert werden soll.
     * @param kontext Der Kontext f&uuml;r den Sicherheits- und Ressourcenmanager.
     */
    public PrintPreview(ImageIcon img, Inifile ini, String kontext) {
        super(ini);
        this.construct(null, img, null, kontext);
    }
    
    /**
     * Generiert und &ouml;ffnet einen JDialog zur Anzeige des &uuml;bergebenen Objektes in 
     * einer Druckvorschau.
     *
     * @param img Ein ImageIcon, das angezeigt werden soll.
     * @param ini Die Inidatei, aus der das Fenster rekonstruiert werden soll.
     * @param cpj Der CorePrintJob, der in dem Preview dargestellt werden soll.
     * @param kontext Der Kontext f&uuml;r den Sicherheits- und Ressourcenmanager.
     */
    public PrintPreview(ImageIcon img, Inifile ini, CorePrintJob cpj, String kontext) {
        super(ini);
        this.construct(null, img, cpj, kontext);
    }
    
    /** 
     * Generiert den eigentlichen Dialog und macht ihn sichtbar.
     *
     * @param dim Die Gr&ouml;&szlig;, falls es sich bei dem zweiten Parameter um eine 
     *     PreviewComponent handelt.
     * @param c Die Componente, die zur Anzeige gebracht werden soll.
     * @param cpj Der CorePrintJob, der in dem Preview dargestellt werden soll.
     */
    private void construct(Dimension dim, Object c, CorePrintJob cpj) {
        this.construct(dim, c, cpj, null);
    }
    
    /** 
     * Generiert den eigentlichen Dialog und macht ihn sichtbar.
     *
     * @param dim Die Gr&ouml;&szlig;, falls es sich bei dem zweiten Parameter um eine 
     *     PreviewComponent handelt.
     * @param c Die Componente, die zur Anzeige gebracht werden soll.
     * @param cpj Der CorePrintJob, der in dem Preview dargestellt werden soll.
     * @param kontext Der Kontext f&uuml;r den Sicherheits- und Ressourcenmanager.
     */
    private void construct(Dimension dim, Object c, CorePrintJob cpj, String kontext) {
        boolean zoomable = (c instanceof Zoomable);
        this.cpj = cpj;
        this.kontext = kontext;
        this.setModal(true);
        this.setTitle(this.createTitle());
        JPanel panelMain = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
        panelMain.setBorder(new CompoundBorder(new EmptyBorder(Constants.VGAP, Constants.HGAP, 
                Constants.VGAP, Constants.HGAP), new EtchedBorder(Constants.ETCH)));
        panelMain.setBorder(new CompoundBorder(panelMain.getBorder(), new EmptyBorder(
                Constants.VGAP, Constants.HGAP, Constants.VGAP, Constants.HGAP)));
        JPanel panel = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
        panel.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH), new EmptyBorder( 
                Constants.VGAP, Constants.HGAP, Constants.VGAP, Constants.HGAP)));
        if (c instanceof Previewable) {
            this.pvc = new PreviewComponent((Previewable) c, dim);
            panel.add(new JScrollPane(this.pvc), BorderLayout.CENTER);
        } else if (c instanceof ImageIcon) {
            panel.add(new JScrollPane(new JLabel((ImageIcon) c)), BorderLayout.CENTER);
        } else {
            panel.add(new JScrollPane(new JLabel(StrUtil.FromHTML("Anzeige wird nicht "
                    + "unterst&uuml;tzt"))), BorderLayout.CENTER);
        }
        panel.setPreferredSize(new Dimension (750, 500));
        panel.setMaximumSize(new Dimension (750, 500));
        JPanel panelButtons = new JPanel(new GridLayout(1, 4, Constants.HGAP, Constants.VGAP));
        panelButtons.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH), 
                new EmptyBorder(Constants.VGAP, Constants.HGAP, Constants.VGAP, Constants.HGAP))
                );
        JButton zoomplus = this.createButtonZoomPlus();
        JButton zoomminus = this.createButtonZoomMinus();
        JPanel zooms = new JPanel(new GridLayout(1, 2, Constants.HGAP, Constants.VGAP));
        if (zoomable) {
            zoomplus.addActionListener(new ActionZoomPlus((Zoomable) c));
            zoomminus.addActionListener(new ActionZoomMinus((Zoomable) c));
            zooms.add(zoomplus);    
            zooms.add(zoomminus);    
        }
        JPanel pages = new JPanel(new GridLayout(1, 3, Constants.HGAP, Constants.VGAP));
        if ((c instanceof Previewable) && (this.cpj != null) && (this.cpj.ja.getFromPage() 
                < this.cpj.ja.getToPage())) {
            this.buttonPagePlus = this.createButtonPagePlus();
            this.buttonPageMinus = this.createButtonPageMinus();
            this.buttonPageMinus.setEnabled(false);
            this.buttonPagePlus.addActionListener(new ActionPagePlus((Previewable) c));
            this.buttonPageMinus.addActionListener(new ActionPageMinus((Previewable) c));
            pages.add(this.buttonPageMinus);    
            pages.add(this.buttonPagePlus);
        } else {
            pages.add(new JLabel(""));    
            pages.add(new JLabel(""));
        }
        this.buttonAbbruch = this.createButtonAbbruch();
        this.buttonAbbruch.addActionListener(new ActionAbbruch());
        this.buttonOk = this.createButtonOk();
        this.buttonOk.addActionListener(new ActionOk());
        new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F12) {
                    doOk();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    doAbbruch();
                }
            }
        };
        if (zoomable) {
            panelButtons.add(zooms);
        } else {
            panelButtons.add(new JLabel(""));
        }
        panelButtons.add(pages);
        panelButtons.add(this.buttonOk);
        panelButtons.add(this.buttonAbbruch);
        panelMain.add(panel, BorderLayout.CENTER);
        panelMain.add(panelButtons, BorderLayout.SOUTH);
        this.setContentPane(panelMain);
        this.pack();
        this.repaint();
        this.setVisible(true);
    }
    
    /** 
     * @return Generiert einen Button, der w&auml;hrend der Dialogerstellung als Abbruch-Button 
     *     eingef&uuml;gt wird.
     */
    protected JButton createButtonAbbruch() {
        return new JButton("Abbruch");
    }
    
    /** 
     * @return Generiert einen Button, der w&auml;hrend der Dialogerstellung als Ok-Button 
     *     eingef&uuml;gt wird.
     */
    protected JButton createButtonOk() {
        return new JButton("Drucken");
    }
    
    /** 
     * @return Generiert einen Button, der w&auml;hrend der Dialogerstellung als 'Page-'-Button 
     *     eingef&uuml;gt wird.
     */
    protected JButton createButtonPageMinus() {
        return new JButton("Page -");
    }
    
    /** 
     * @return Generiert einen Button, der w&auml;hrend der Dialogerstellung als 'Page+'-Button 
     *     eingef&uuml;gt wird.
     */
    protected JButton createButtonPagePlus() {
        return new JButton("Page +");
    }
    
    /** 
     * @return Generiert einen Button, der w&auml;hrend der Dialogerstellung als 'Zoom-'-Button 
     *     eingef&uuml;gt wird.
     */
    protected JButton createButtonZoomMinus() {
        return new JButton("Zoom -");
    }
    
    /** 
     * @return Generiert einen Button, der w&auml;hrend der Dialogerstellung als 'Zoom+'-Button 
     *     eingef&uuml;gt wird.
     */
    protected JButton createButtonZoomPlus() {
        return new JButton("Zoom +");
    }
    
    /** @return Liefert einen String, der als Fenstertitel eingesetzt wird. */
    protected String createTitle() {
        return "Druckvorschau";
    }
    
    /** Diese Methode wird aufgerufen, wenn der Benutzer den Abbruch-Button dr&uuml;ckt. */
    public void doAbbruch() {
        this.canceled = true;
        this.setVisible(false);
    }
    
    /** Diese Methode wird aufgerufen, wenn der Benutzer den Ok-Button dr&uuml;ckt. */
    public void doOk() {
        if (Checker.IsActive(this.buttonOk)) {
            this.canceled = false;
            this.setVisible(false);
        }
    }
    
    /** 
     * Diese Methode wird aufgerufen, wenn der Benutzer den 'Page-'-Button dr&uuml;ckt.
     *
     * @param pv Das Previewable, dessen Inhalt angezeigt werden soll.
     */
    public void doPageMinus(Previewable pv) {
        if (this.pvc.getPage() > this.cpj.getMinPage()) {
            this.pvc.setPage(this.pvc.getPage()-1);
            if (this.pvc.getPage() == this.cpj.getMinPage()) {
                this.buttonPageMinus.setEnabled(false);
            }
            this.buttonPagePlus.setEnabled(true);
            this.repaint();
        }
    }
    
    /** 
     * Diese Methode wird aufgerufen, wenn der Benutzer den 'Page+'-Button dr&uuml;ckt. 
     *
     * @param pv Das Previewable, dessen Inhalt angezeigt werden soll.
     */
    public void doPagePlus(Previewable pv) {
        if (this.pvc.getPage() < this.cpj.getMaxPage()) {
            this.pvc.setPage(this.pvc.getPage()+1);
            if (this.pvc.getPage() == this.cpj.getMaxPage()) {
                this.buttonPagePlus.setEnabled(false);
            }
            this.buttonPageMinus.setEnabled(true);
            this.repaint();
        }
    }
    
    /** 
     * Diese Methode wird aufgerufen, wenn der Benutzer den 'Zoom-'-Button dr&uuml;ckt.
     *
     * @param z Das zoombare Objekt zur Anzeige.
     */
    public void doZoomMinus(Zoomable z) {
        z.setDPI(z.getDPI() - (z.getDPI() / 4));
        this.repaint();
    }
    
    /** 
     * Diese Methode wird aufgerufen, wenn der Benutzer den 'Zoom+'-Button dr&uuml;ckt.
     *
     * @param z Das zoombare Objekt zur Anzeige.
     */
    public void doZoomPlus(Zoomable z) {
        z.setDPI(z.getDPI() + (z.getDPI() / 4));
        this.repaint();
    }
    
    /** @return <TT>true</TT>, wenn der Preview abgebrochen wurde,<BR><TT>false</TT> sonst. */
    public boolean isCanceled() {
        return this.canceled;
    }

    class ActionAbbruch extends DefaultAction {
        protected ActionAbbruch() {
            super("ActionAbbruch");
        }
        public void actionPerformed(ActionEvent e) {
            doAbbruch();
        }
    }
    
    class ActionOk extends DefaultAction {
        protected ActionOk() {
            super("ActionOk");
        }
        public void actionPerformed(ActionEvent e) {
            doOk();
        }
    }
    
    class ActionPageMinus extends DefaultAction {
        private Previewable pv = null;
        protected ActionPageMinus(Previewable pv) {
            super("ActionPageMinus");
            this.pv = pv;
        }
        public void actionPerformed(ActionEvent e) {
            doPageMinus(this.pv);
        }
    }
    
    class ActionPagePlus extends DefaultAction {
        private Previewable pv = null;
        protected ActionPagePlus(Previewable pv) {
            super("ActionPagePlus");
            this.pv = pv;
        }
        public void actionPerformed(ActionEvent e) {
            doPagePlus(this.pv);
        }
    }
    
    class ActionZoomMinus extends DefaultAction {
        private Zoomable z = null;
        protected ActionZoomMinus(Zoomable z) {
            super("ActionZoomMinus");
            this.z = z;
        }
        public void actionPerformed(ActionEvent e) {
            doZoomMinus(this.z);
        }
    }
    
    class ActionZoomPlus extends DefaultAction {
        private Zoomable z = null;
        protected ActionZoomPlus(Zoomable z) {
            super("ActionZoomPlus");
            this.z = z;
        }
        public void actionPerformed(ActionEvent e) {
            doZoomPlus(this.z);
        }
    }
    
}
