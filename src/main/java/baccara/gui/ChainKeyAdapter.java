/*
 * ChainKeyAdapter.java
 *
 * 13.10.2017
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui;

import java.awt.event.*;

import javax.swing.*;

/**
 * An extension of the key adapter which manages chains of components for focus transfers. 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 13.10.2017 - Added.
 */

public class ChainKeyAdapter extends KeyAdapter {

    private JComponent cancelComponent = null;
    private JComponent component = null;
    private JComponent pred = null;
    private JComponent succ = null;
    private JComponent submitComponent = null;
    private int vkBack = KeyEvent.VK_LEFT;
    private int vkCancel = KeyEvent.VK_ESCAPE;
    private int vkNext = KeyEvent.VK_RIGHT;
    private int vkNextAlt = KeyEvent.VK_ENTER;
    private int vkSubmit = KeyEvent.VK_F12;

    /**
     * Generates a KeyListener which which manages the focus transfer between the components.
     *
     * @param pred The focus predecessor of the passed component.
     * @param component The component.
     * @param succ The focus successor of the passed component.
     * @param submitComponent The component which is to activate for a submit action (usually a
     *         button).
     * @param cancelComponent The component which is to activate for a cancel action (usually a
     *         button).
     * @param vkBack A KeyEvent code to set the focus to the next component backwards.
     * @param vkNext A KeyEvent code to set the focus to the next component.
     * @param vkNextAlt An alternate KeyEvent code to set the focus to the next component in the
     *         cycle.
     * @param vkSubmit A KeyEvent code to transfer the focus to the component which is
     *         responsible to create a submit event.
     * @param vkCancel A KeyEvent code to transfer the focus to the component which is
     *         responsible to create a cancel event.
     *
     * @changed OLI 13.10.2017 - Added (from CoreNT "ChainKeyAdapter").
     */ 
    public ChainKeyAdapter(JComponent pred, JComponent component, JComponent succ,
            JComponent submitComponent, JComponent cancelComponent, int vkBack, int vkNext, 
            int vkNextAlt, int vkSubmit, int vkCancel) {
        super();
        this.cancelComponent = cancelComponent;
        this.component = component;
        this.pred = pred;
        this.succ = succ;
        this.submitComponent = submitComponent;
        this.vkSubmit = vkSubmit;
        this.vkBack = vkCancel;
        this.vkNext = vkNext;
        this.vkNextAlt = vkNextAlt;
        this.vkBack = vkBack;
    }

    /**
     * @changed OLI 13.10.2017 - Added (from CoreNT class "ChainKeyAdapter").
     */
    @Override public void keyPressed(KeyEvent e) {   
        if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (this.component != null) 
                && (this.component instanceof JButton)) {
            ((JButton) this.component).doClick();
        } else if (((e.getKeyCode() == this.vkNext) || (e.getKeyCode() == this.vkNextAlt)) 
                && (this.succ != null)) {
            this.succ.requestFocus();
        } else if ((e.getKeyCode() == this.vkBack) && (this.pred != null)) {
            this.pred.requestFocus();
        } else if ((e.getKeyCode() == this.vkSubmit) && (this.submitComponent != null)) {
            if ((this.submitComponent instanceof JButton) && isClickOnFocused()) {
                ((JButton) this.submitComponent).doClick();
            } else {
                this.submitComponent.requestFocus();
            }
        } else if ((e.getKeyCode() == this.vkCancel) && (this.cancelComponent != null)) {
            if ((this.cancelComponent instanceof JButton) && isClickOnFocused()) {
                ((JButton) this.cancelComponent).doClick();
            } else {
                this.cancelComponent.requestFocus();
            }
        }
    }

    /**
     * Checks if an click event is to create if a button component is focused and the submit or
     * cancel key is pressed.
     * <P>By default the returned value is <CODE>false</CODE> excluding the property
     * "baccara.gui.ChainKeyAdapter.OnFocus.DoClick" is set to true.
     *
     * @return <CODE>true</CODE> if a button component is focused and the submit or cancel key
     *         is pressed.
     *
     * @changed OLI 13.10.2017 - Added.
     */
    public boolean isClickOnFocused() {
        return Boolean.getBoolean("baccara.gui.ChainKeyAdapter.OnFocus.DoClick");
    }

}