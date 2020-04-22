/*
 * FocusTransferByKeyPress.java
 *
 * 15.10.2013
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.generics;


import java.awt.*;
import java.awt.event.*;


/**
 * Transfers the focus by a key press to the configured component. 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 15.10.2013 - Added.
 */

public class FocusTransferByKeyPress extends KeyAdapter {

    private int key = Integer.MIN_VALUE;
    private Component transferTarget = null;

    /**
     * Creates a new focus transfer by key press object with the passed parameters.
     *
     * @param key The key which has to be pressed to transfer the focus.
     * @param transferTarget The target of the focus transfer.
     *
     * @changed OLI 15.10.2013 - Added.
     */
    public FocusTransferByKeyPress(int key, Component transferTarget) {
        super();
        this.key = key;
        this.transferTarget = transferTarget;
    }

    /**
     * @changed OLI 15.10.2013 - Added.
     */
    @Override public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == this.key) {
            this.transferTarget.requestFocus();
        }
    }

}