/*
 * SpinField.java
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


/**
 * Diese Komponente bildet ein Spin-Feld ab, in dem ein nummerischer Wert mit Hilfe von Buttons
 * innerhalb definierter Grenzen erh&ouml;ht bzw. verringert werden kann.
 *
 * @author O.Lieshoff
 *
 */
 
public class SpinField extends JPanel {
    
    /* Der Erh&ouml;hen-Button. */
    private JButton buttonErhoehen = null;
    /* Der Verringern-Button. */
    private JButton buttonVerringern = null;
    /* Das Anzeigefeld des Spins. */
    private JTextField anzeige = null;
    /* Das Maximum f&uuml;r den im SpinField angezeigten Wert. */
    private long maximum = 1;
    /* Das Minimum f&uuml;r den im SpinField angezeigten Wert. */
    private long minimum = 0;
    /* Der im SpinField angezeigte Wert. */
    private long wert = 0;
    
    /* Die Daten f&uuml;r das Icon des Pfeil-Oben-Buttons. */
    byte[] bOben = new byte[] {71, 73, 70, 56, 57, 97, 9, 0, 8, 0, -9, 0, 0, 0, 0,
             0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, 33, -7, 4, 1, 0, 0, 1, 0, 44, 0, 0, 0, 0, 9, 0, 8, 0, 64
            , 8, 26, 0, 3, 8, 28, 72, 48, 0, -128, -125, 7, 11, 34, 36, -120, -80, -95, -62,
             -126, 2, 23, 50, 116, 8, -111, 96, 64, 0, 59};
    /* Die Daten f&uuml;r das Icon des Pfeil-Unten-Buttons. */
    byte[] bUnten = new byte[] {71, 73, 70, 56, 57, 97, 9, 0, 8, 0, -9, 0, 0, 0, 0,
             0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, 33, -7, 4, 1, 0, 0, 1, 0, 44, 0, 0, 0, 0, 9, 0, 8, 0, 64
            , 8, 27, 0, 3, 8, 28, 72, 80, 32, -128, -125, 5, 15, 42, 44, 24, 0, 64, 66, -123
            , 11, 13, 66, 124, -24, -112, 97, -63, -128, 0, 59};

    
    /**
     * Generiert ein SpinField mit den angegebenen Parametern. 
     *
     * @param min Das Minimum f&uuml;r den Wert im SpinField.
     * @param max Das Maximum f&uuml;r den Wert im SpinField.
     * @param value Der aktuelle Wert im SpinField.
     */
    public SpinField(long min, long max, long value) {
        this(min, max, value, null);
    }
    
    /**
     * Generiert ein SpinField mit den angegebenen Parametern. 
     *
     * @param min Das Minimum f&uuml;r den Wert im SpinField.
     * @param max Das Maximum f&uuml;r den Wert im SpinField.
     * @param value Der aktuelle Wert im SpinField.
     * @param icons Array[2] mit den ImageIcons f&uuml;r die Spin-Buttons.
     */
    public SpinField(long min, long max, long value, ImageIcon[] icons) {
        super(new BorderLayout(1, 1));
        this.maximum = max;
        this.minimum = min;
        this.wert = value;
        this.anzeige = new JTextField(2) {
            public boolean isEditable() {
                return false;
            }
            public boolean isEnabled() {
                return false;
            }
        };
        this.anzeige.setEditable(false);
        this.anzeige.setEnabled(false);
        this.anzeige.setForeground(Color.black);
        this.buttonErhoehen = new JButton();
        if ((icons != null) && (icons[0] != null)) {
            this.buttonErhoehen.setIcon(icons[0]);
        } else {
            this.buttonErhoehen.setIcon(new ImageIcon(bOben));
        }
        this.buttonErhoehen.setFont(new Font("sansserif", Font.BOLD, 8));
        this.buttonErhoehen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                increment();
            }
        });
        this.buttonErhoehen.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
                    wert += (maximum - wert) / 2;
                    updateAnzeige();
                }
            }
        });
        this.buttonErhoehen.setPreferredSize(new Dimension(18, 10));
        this.buttonVerringern = new JButton();
        if ((icons != null) && (icons[1] != null)) {
            this.buttonVerringern.setIcon(icons[1]);
        } else {
            this.buttonVerringern.setIcon(new ImageIcon(bUnten));
        }
        this.buttonVerringern.setFont(new Font("sansserif", Font.BOLD, 8));
        this.buttonVerringern.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                decrement();
            }
        });
        this.buttonVerringern.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
                    wert -= (wert - minimum) / 2;
                    updateAnzeige();
                }
            }
        });
        this.buttonVerringern.setPreferredSize(new Dimension(18, 10));
        KeyAdapter ka = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    increment();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    decrement();
                }
            }
        };
        this.anzeige.addKeyListener(ka);
        this.buttonErhoehen.addKeyListener(ka);
        this.buttonVerringern.addKeyListener(ka);
        JPanel panelButtons = new JPanel(new GridLayout(2, 1, 1, 1));
        panelButtons.add(this.buttonErhoehen);
        panelButtons.add(this.buttonVerringern);
        this.add(this.anzeige, BorderLayout.CENTER);
        this.add(panelButtons, BorderLayout.EAST);
        this.updateAnzeige();
        // this.pack();
    }
    
    /** @return Der aktuelle Wert des SpinField. */
    public long getValue() {
        return this.wert;
    }
    
    /** 
     * Setzt den Inhalt des SpinFields auf den angegebenen Wert.
     *
     * @param value Der neue Wert f&uuml;r das SpinField.
     */
    public void setValue(long value) {
        this.wert = value;
        this.updateAnzeige();
    }
    
    private void updateAnzeige() {
        this.anzeige.setText("" + this.wert);
    }
    
    private void decrement() {
        if (wert > minimum) {
            wert--;
            this.updateAnzeige();
        }
    }
    
    private void increment() {
        if (wert < maximum) {
            wert++;
            this.updateAnzeige();
        }
    }
    
    
    /* Ueberschreiben von Methoden der Superklasse. */
    
    public void addKeyListener(KeyListener l) {
        this.anzeige.addKeyListener(l);
        this.buttonErhoehen.addKeyListener(l);
        this.buttonVerringern.addKeyListener(l);
    }
    
    public void removeKeyListener(KeyListener l) {
        this.anzeige.removeKeyListener(l);
        this.buttonErhoehen.removeKeyListener(l);
        this.buttonVerringern.removeKeyListener(l);
    }
    
}
