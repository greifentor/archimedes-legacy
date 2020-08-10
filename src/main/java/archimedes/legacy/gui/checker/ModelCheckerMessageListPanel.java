/*
 * ModelCheckerMessageListPanel.java
 *
 * 11.07.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.legacy.gui.checker;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import archimedes.acf.checker.*;
import archimedes.acf.checker.ModelCheckerMessage.*;
import baccara.gui.*;
import corent.gui.*;

/**
 * A panel with the logic for showing and selecting model checker messages. 
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 11.07.2016 - Added.
 */

public class ModelCheckerMessageListPanel extends JPanel implements ActionListener, 
        MouseListener {

    private static final String RES_MODEL_CHECKER_BUTTON_CLOSE_TITLE =
            "ModelChecker.button.close.title";
    private static final String RES_MODEL_CHECKER_BUTTON_GENERATE_TITLE =
            "ModelChecker.button.generate.title";

    private GUIBundle guiBundle = null;
    private JButton buttonClose = null;
    private JButton buttonGenerate = null;
    private boolean checkBeforeGenerate = false;
    private ModelCheckerMessageListEventFirer eventFirer = null;
    private ModelCheckerMessage[] mcms = null;
    private JTableWithInifile tableWarningsAndErrors = null;

    /**
     * Creates a new panel for model checker message list display and selection. 
     *
     * @param guiBundle A bundle with GUI information.
     * @param mcms The list of model checker messages to show.
     * @param checkBeforeGenerate Set this flag if the frame is called as check before
     *         generating the code.
     * @param listeners Which should observe the frame.
     * @param eventFirer The class which fires the events.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 11.07.2016 - Added.
     */
    public ModelCheckerMessageListPanel(GUIBundle guiBundle, ModelCheckerMessage[] mcms,
            boolean checkBeforeGenerate, ModelCheckerMessageListEventFirer eventFirer,
            ModelCheckerMessageListFrameListener... listeners) {
        super(guiBundle.createBorderLayout());
        this.checkBeforeGenerate = checkBeforeGenerate;
        this.eventFirer = eventFirer;
        this.guiBundle = guiBundle;
        this.mcms = mcms;
        this.add(this.createMainPanel());
    }

    private JPanel createMainPanel() {
        JPanel p = new JPanel(this.guiBundle.createBorderLayout());
        this.tableWarningsAndErrors = new JTableWithInifile(new ModelCheckerMessageTableModel(
                this.mcms, this.guiBundle), this.guiBundle.getInifile(),
                "ModelChecker.MessageTable");
        this.tableWarningsAndErrors.addMouseListener(this);
        this.tableWarningsAndErrors.restoreFromIni();
        this.tableWarningsAndErrors.setDefaultRenderer(Object.class,
                new ModelCheckerMessageTableCellRenderer());
        p.add(new JScrollPane(this.tableWarningsAndErrors), BorderLayout.CENTER);
        p.add(this.createButtonPanel(), BorderLayout.SOUTH);
        return p;
    }

    private JPanel createButtonPanel() {
        JPanel p = new JPanel(this.guiBundle.createFlowLayout(FlowLayout.RIGHT));
        if (this.checkBeforeGenerate) {
            this.buttonGenerate = this.guiBundle.createButton(
                    RES_MODEL_CHECKER_BUTTON_GENERATE_TITLE, "generate", this, p);
            p.add(new JSeparator(JSeparator.HORIZONTAL));
            if (this.hasErrors()) {
                this.buttonGenerate.setEnabled(false);
            }
        }
        this.buttonClose = this.guiBundle.createButton(
                RES_MODEL_CHECKER_BUTTON_CLOSE_TITLE, "close", this, p);
        return p;
    }

    private boolean hasErrors() {
        for (ModelCheckerMessage mcm : this.mcms) {
            if (mcm.getLevel() == Level.ERROR) {
                return true;
            }
        }
        return false;
    }

    /**
     * @changed OLI 19.05.2016 - Added.
     */
    @Override public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.buttonClose) {
            this.eventFirer.fireAction(new ModelCheckerMessageListFrameListener.Event(
                    ModelCheckerMessageListFrameListener.Event.Type.CLOSED));
            this.tableWarningsAndErrors.saveToIni();
        } else if (e.getSource() == this.buttonGenerate) {
            this.eventFirer.fireAction(new ModelCheckerMessageListFrameListener.Event(
                    ModelCheckerMessageListFrameListener.Event.Type.GENERATE));
            this.tableWarningsAndErrors.saveToIni();
        }
    }

    /**
     * @changed OLI 19.05.2016 - Added.
     */
    @Override public void mouseClicked(MouseEvent e) {
        if ((e.getClickCount() == 2) && (e.getSource() == this.tableWarningsAndErrors)) {
            ModelCheckerMessage mcm = this.mcms[this.tableWarningsAndErrors.getSelectedRow()];
            this.eventFirer.fireAction(new ModelCheckerMessageListFrameListener.Event(mcm,
                    ModelCheckerMessageListFrameListener.Event.Type.MESSAGE_SELECTED));
            this.tableWarningsAndErrors.saveToIni();
        }
    }

    /**
     * @changed OLI 19.05.2016 - Added.
     */
    @Override public void mouseEntered(MouseEvent e) {
    }

    /**
     * @changed OLI 19.05.2016 - Added.
     */
    @Override public void mouseExited(MouseEvent e) {
    }

    /**
     * @changed OLI 19.05.2016 - Added.
     */
    @Override public void mousePressed(MouseEvent e) {
    }

    /**
     * @changed OLI 19.05.2016 - Added.
     */
    @Override public void mouseReleased(MouseEvent e) {
    }

    /**
     * Updates the messages in the list panel.
     *
     * @param mcms The messages to show in the panel.
     *
     * @changed OLI 18.08.2016 - Added.
     */
    public void updateMessages(ModelCheckerMessage[] mcms) {
        this.tableWarningsAndErrors.setModel(new ModelCheckerMessageTableModel(
                mcms, this.guiBundle));
        this.tableWarningsAndErrors.restoreFromIni();
        this.mcms = mcms;
    }

}