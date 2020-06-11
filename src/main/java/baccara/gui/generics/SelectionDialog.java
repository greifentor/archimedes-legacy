/*
 * SelectionDialog.java
 *
 * 23.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.generics;


import baccara.gui.*;

import corent.gui.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;


/**
 * This class provides a dialog which allows to select an object or a list of objects from a
 * list.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 23.05.2013 - Added.
 * @changed OLI 29.05.2013 - Extended by the ability to process double clicks. Hence
 *         implementation of the <CODE>MouseListener</CODE>.
 */

public class SelectionDialog extends JDialogWithInifile implements ActionListener, KeyListener,
        MouseListener {

    private JButton buttonCancel = null;
    private JButton buttonOk = null;
    private GUIBundle guiBundle = null;
    private List<Object> result = new Vector<Object>();
    private JList selectionList = null;

    /**
     * Creates a new selection dialog with the passed parameters.
     *
     * @param frame The frame which the selection is modal to.
     * @param title A title to show in the header of the dialog.
     * @param guiBundle A bundle with the GUI relevant information.
     * @param objects A list of objects where the selection is taken from.
     * @param buttonOkText The text for the ok button.
     * @param buttonCancelText The text for the cancel button.
     * @param renderer A renderer which renders the list cells. Pass <CODE>null</CODE> here if
     *         the <CODE>toString()</CODE> should be used.
     *
     * @changed OLI 23.05.2013 - Added.
     */
    public SelectionDialog(JFrame frame, String title, GUIBundle guiBundle, Object[] objects,
            String buttonOkText, String buttonCancelText, ListCellRenderer renderer) {
        super(frame, title, true, guiBundle.getInifile());
        this.guiBundle = guiBundle;
        this.selectionList = new JList(objects);
        this.selectionList.addMouseListener(this);
        this.selectionList.addKeyListener(this);
        if (renderer != null) {
            this.selectionList.setCellRenderer(renderer);
        }
        this.buttonCancel = this.createButton(buttonCancelText);
        this.buttonOk = this.createButton(buttonOkText);
        this.setContentPane(this.createMainPanel());
        this.pack();
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.addKeyListener(this);
        return button;
    }

    private JPanel createMainPanel() {
        JPanel p = new JPanel(this.guiBundle.createBorderLayout());
        p.add(new JScrollPane(this.selectionList), BorderLayout.CENTER);
        p.add(this.createButtonPanel(), BorderLayout.SOUTH);
        return p;
    }

    private JPanel createButtonPanel() {
        JPanel p = new JPanel(this.guiBundle.createFlowLayout(FlowLayout.RIGHT));
        p.add(this.buttonCancel);
        p.add(this.buttonOk);
        return p;
    }

    /**
     * @changed OLI 23.05.2013 - Added.
     */
    @Override public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.buttonCancel) {
            this.setVisible(false);
        } else if (e.getSource() == this.buttonOk) {
            this.selectAndClose();
        }
    }

    private void selectAndClose() {
        this.result = Arrays.asList(this.selectionList.getSelectedValues());
        this.setVisible(false);
    }

    /**
     * Returns a list with the selected object (or an empty list if no object is selected).
     *
     * @return A list with the selected object (or an empty list if no object is selected).
     *
     * @changed OLI 23.05.2013 - Added.
     */
    public List<Object> getResult() {
        return this.result;
    }

    /**
     * @changed OLI 18.07.2013 - Added.
     */
    @Override public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (e.getSource() == this.buttonCancel) {
                this.buttonCancel.doClick();
            } else if (e.getSource() == this.buttonOk) {
                this.buttonOk.doClick();
            } else if (e.getSource() == this.selectionList) {
                this.selectAndClose();
            }
        }
    }

    /**
     * @changed OLI 18.07.2013 - Added.
     */
    @Override public void keyReleased(KeyEvent e) {
    }

    /**
     * @changed OLI 18.07.2013 - Added.
     */
    @Override public void keyTyped(KeyEvent e) {
    }

    /**
     * @changed OLI 29.05.2013 - Added.
     */
    @Override public void mouseClicked(MouseEvent e) {
        if ((e.getClickCount() == 2) && (e.getButton() == MouseEvent.BUTTON1)) {
            this.selectAndClose();
        }
    }

    /**
     * @changed OLI 29.05.2013 - Added.
     */
    @Override public void mouseEntered(MouseEvent e) {
    }

    /**
     * @changed OLI 29.05.2013 - Added.
     */
    @Override public void mouseExited(MouseEvent e) {
    }

    /**
     * @changed OLI 29.05.2013 - Added.
     */
    @Override public void mousePressed(MouseEvent e) {
    }

    /**
     * @changed OLI 29.05.2013 - Added.
     */
    @Override public void mouseReleased(MouseEvent e) {
    }

}