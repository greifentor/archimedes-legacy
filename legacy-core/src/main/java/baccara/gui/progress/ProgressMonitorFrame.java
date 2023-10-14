/*
 * ProgressMonitorDialog.java
 *
 * 25.09.2013
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.progress;


import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;


/**
 * A progress monitor dialog.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 25.09.2013 - Added.
 */

public class ProgressMonitorFrame extends JFrame implements WindowListener {

    private static final int HGAP = 3;
    private static final int VGAP = 3;

    private JProgressBar progressBar = new JProgressBar(0, 100);
    private JLabel labelComment = new JLabel();

    /**
     * Creates a progress dialog which shows a progress bar and, as option, a string above the
     * bar.
     *
     * @param caller A window which acts a caller for the progress bar. This window will not be
     *         locked but the progress bar will be kept in front of the window. Pass null here
     *         if no window is known as caller.
     * @param title A title for the progress monitor dialog.
     * @param initialComment A string which is shown above the progress bar. Pass null here if
     *         no string should be shown there.
     *
     * @changed OLI 25.09.2013 - Added.
     */
    public ProgressMonitorFrame(Window caller, String title, String initialComment) {
        super(title);
        JPanel p = this.createMainPanel(initialComment);
        this.setContentPane(p);
        this.pack();
        Rectangle r = null;
        if (caller != null) {
            r = caller.getBounds();
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
    }

    private JPanel createMainPanel(String comment) {
        JPanel p = new JPanel(new GridLayout((comment == null ? 1 : 2), 1, HGAP, VGAP));
        this.progressBar.setStringPainted(true);
        p.add(this.progressBar);
        if (comment != null) {
            p.add(this.labelComment);
            this.labelComment.setText(comment);
        }
        p.setBorder(new EmptyBorder(VGAP, HGAP, VGAP, HGAP));
        return p;
    }

    /**
     * Sets the passed value as new value for the progress bar. 
     * 
     * @param n The new value for the progress bar
     *
     * @changed OLI 25.09.2013 - Added.
     */
    public void update(int n) {
        this.progressBar.setValue(n);
        this.progressBar.setString("" + n + "%");
    }

    /**
     * Sets the passed comment as new comment for the progress bar. 
     * 
     * @param comment The new comment for the progress bar
     *
     * @changed OLI 25.09.2013 - Added.
     */
    public void update(String comment) {
        this.labelComment.setText(comment);
    }

    /**
     * @changed OLI 25.09.2013 - Added.
     */
    @Override public void windowActivated(WindowEvent e) {
    }

    /**
     * @changed OLI 25.09.2013 - Added.
     */
    @Override public void windowClosed(WindowEvent e) {
    }

    /**
     * @changed OLI 25.09.2013 - Added.
     */
    @Override public void windowClosing(WindowEvent e) {
    }

    /**
     * @changed OLI 25.09.2013 - Added.
     */
    @Override public void windowDeactivated(WindowEvent e) {
        this.toFront();
        this.requestFocus();
    }

    /**
     * @changed OLI 25.09.2013 - Added.
     */
    @Override public void windowDeiconified(WindowEvent e) {
    }

    /**
     * @changed OLI 25.09.2013 - Added.
     */
    @Override public void windowIconified(WindowEvent e) {
    }

    /**
     * @changed OLI 25.09.2013 - Added.
     */
    @Override public void windowOpened(WindowEvent e) {
    }

}