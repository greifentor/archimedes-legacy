/*
 * BorderToRedWrapper.java
 *
 * 13.10.2017
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

/**
 * A decorator for components which wraps the component 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 13.10.2017 - Added.
 */

public class BorderToRedWrapper extends JPanel implements FocusListener {

    /**
     * Creates a new border to red wrapper for the passes component.
     *
     * @param c The component to wrap.
     *
     * @changed OLI 13.10.2017 - Added.
     */
    public BorderToRedWrapper(Component c) {
        super(new GridLayout(1, 1));
        this.setBorder(new LineBorder(this.getBackground(), 1));
        this.add(c);
        c.addFocusListener(this);
    }

    /**
     * @changed OLI 13.10.2017 - Added.
     */
    @Override public void focusGained(FocusEvent e) {
        this.setBorder(new LineBorder(Color.RED, 1));
    }

    /**
     * @changed OLI 13.10.2017 - Added.
     */
    @Override public void focusLost(FocusEvent e) {
        this.setBorder(new LineBorder(this.getBackground(), 1));
    }

}