/*
 * KeyListenerUtil.java
 *
 * 13.10.2017
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui;

import java.awt.*;

import javax.swing.*;

/**
 * Some methods to support the KeyListener logic.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 13.10.2017 - Added.
 */

public class KeyListenerUtil {

    /**
     * Generates a cycle for the input focus by the passed components.
     * <BR><PRE>
     * ENTER       -> Transfers the focus to the next component in the cycle. In case of the
     *                last component the focus will be transfered to the first one.
     * ENTER+SHIFT -> Transfers the focus to the next component in the cycle. In case of the
     *                first component the focus will be transfered to the last one.
     * ESCAPE      -> Transfers the focus to the cancel component.
     * F12         -> Transfers the focus to the submit component.
     * </PRE>
     * 
     * @param components An array of components which have to be added to the cycle.
     * @param submitComponent The component which is to activate for a submit action (usually a
     *         button).
     * @param cancelComponent The component which is to activate for a cancel action (usually a
     *         button).
     * @param coe Set this flag to perform an action if the a button is focused while pressing
     *         the enter key.
     * @param vkBack A KeyEvent code to set the focus to the next component backwards.
     * @param vkNext A KeyEvent code to set the focus to the next component.
     * @param vkNextAlt An alternate KeyEvent code to set the focus to the next component in the
     *         cycle.
     * @param vkSubmit A KeyEvent code to transfer the focus to the component which is
     *         responsible to create a submit event.
     * @param vkCancel A KeyEvent code to transfer the focus to the component which is
     *         responsible to create a cancel event.
     *
     * @changed OLI 13.10.2017 - Added (from CoreNT "KeyListenerDjinn").
     */
    public void createCycle(Component[] components, JComponent submitComponent, 
            JComponent cancelComponent, boolean coe, int vkBack, int vkNext, int vkNextAlt,
            int vkSubmit, int vkCancel) {
        JComponent c = null;
        JComponent succ = null;
        JComponent pred = null;
        for (int i = 0, len = components.length; i < len; i++) {
            if (i == 0) {
                pred = (JComponent) components[len-1];
            } else if (i > 0) {
                pred = (JComponent) components[i-1];
            }
            c = (JComponent) components[i];
            if (i == len-1) {
                succ = (JComponent) components[0];
            } else if (i < len-1) {
                succ = (JComponent) components[i+1];
            }
            c.addKeyListener(new ChainKeyAdapter(pred, c, succ, submitComponent,
                    cancelComponent, vkBack, vkNext, vkNextAlt, vkSubmit, vkCancel));
        }
    }

}