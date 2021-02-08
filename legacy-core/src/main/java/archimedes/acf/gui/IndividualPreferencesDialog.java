/*
 * IndividualPreferencesDialog.java
 *
 * 10.07.2014
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.gui;


import baccara.gui.*;
import baccara.gui.generics.*;

import corent.gui.*;

import corentx.io.*;
import corentx.util.*;

import gengen.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.*;


/**
 * A dialog to edit the individual preferences.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 10.07.2014 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public class IndividualPreferencesDialog extends JDialogWithInifile implements ActionListener,
        FocusListener, KeyListener {

    private List<AlternatePathContainer> alternatePathes = null;
    private JButton buttonCancel = null;
    private JButton buttonOk = null;
    private JButton buttonSave = null;
    private IndividualPreferences ip = null;
    private GUIBundle guiBundle = null;
    private Map<Component, String> componentsByComponent = new HashMap<Component, String>();
    private Map<String, Component> componentsByName = new HashMap<String, Component>();
    private String configFilePath = null;
    private JTable tableAlternatePathes = null;

    /**
     * Creates a new Dialog with the passed parameters.
     *
     * @param parent The parent dialog of this dialog.
     * @param guiBundle A bundle with GUI information.
     * @param ip Individual preferences for code generation.
     * @param configFilePath The path of the file with the individual preferences.
     *
     * @changed OLI 10.07.2014 - Added.
     */
    public IndividualPreferencesDialog(JDialog parent, GUIBundle guiBundle,
            IndividualPreferences ip, String configFilePath) {
        super(parent, guiBundle.getInifile());
        this.configFilePath = configFilePath;
        this.guiBundle = guiBundle;
        this.ip = ip;
        this.setContentPane(this.createMainPanel());
        this.pack();
        this.setModal(true);
        this.setAlwaysOnTop(true);
        this.setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel p = new JPanel(new BorderLayout(this.guiBundle.getHGap(), this.guiBundle.getVGap(
                )));
        p.add(this.createEditorPanel(), BorderLayout.NORTH);
        p.add(this.createTablePanel(), BorderLayout.CENTER);
        p.add(this.createButtonPanel(), BorderLayout.SOUTH);
        return p;
    }

    private JPanel createEditorPanel() {
        return new EditorPanelCreator(this.guiBundle, this, this).create(
                "code.generators.configuration.individual.preferences",
                this.componentsByComponent, this.componentsByName, new ComponentData[] {
                new ComponentData("company.name", baccara.gui.generics.Type.STRING,
                        this.ip.getCompanyName()),
                new ComponentData("user.name", baccara.gui.generics.Type.STRING,
                        this.ip.getUserName()),
                new ComponentData("user.token", baccara.gui.generics.Type.STRING,
                        this.ip.getUserToken()),
                new ComponentData("base.code.path", baccara.gui.generics.Type.STRING,
                        this.ip.getBaseCodePath())
                });
    }

    private JPanel createTablePanel() {
        JPanel p = new JPanel(new BorderLayout(this.guiBundle.getHGap(), this.guiBundle.getVGap(
                )));
        this.alternatePathes = this.getAlternatePathContainers(this.ip);
        this.tableAlternatePathes = new JTableWithInifile(new AlternatePathesTableModel(
                this.guiBundle, this.alternatePathes), this.guiBundle.getInifile(),
                "AlternatePathesTable");
        this.tableAlternatePathes.addFocusListener(this);
        this.tableAlternatePathes.addKeyListener(this);
        p.add(new JScrollPane(this.tableAlternatePathes), BorderLayout.CENTER);
        return p;
    }

    private List<AlternatePathContainer> getAlternatePathContainers(IndividualPreferences ip) {
        List<AlternatePathContainer> l = new SortedVector<AlternatePathContainer>();
        for (String token : ip.getBaseCodePathAlternateTokens()) {
            l.add(new AlternatePathContainer(token, ip.getBaseCodePath(token)));
        }
        return l;
    }

    private JPanel createButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, this.guiBundle.getHGap(),
                this.guiBundle.getVGap()));
        this.buttonCancel = this.createButton("cancel");
        this.buttonOk = this.createButton("ok");
        this.buttonSave = this.createButton("save");
        p.add(this.buttonOk);
        p.add(this.buttonSave);
        p.add(this.buttonCancel);
        return p;
    }

    private JButton createButton(String name) {
        JButton button = new JButton(this.guiBundle.getResourceText("code.generators."
                + "configuration.individual.preferences.button." + name + ".label"));
        button.addActionListener(this);
        ImageIcon icon = this.guiBundle.getImageProvider().getImageIcon("button_" + name);
        if (icon != null) {
            button.setIcon(icon);
        }
        return button;
    }

    /**
     * @changed OLI 10.07.2014 - Added.
     */
    @Override public void actionPerformed(ActionEvent e) {
        if (this.buttonCancel == e.getSource()) {
            this.setVisible(false);
            this.dispose();
        } else if (this.buttonOk == e.getSource()) {
            this.transferData();
            this.setVisible(false);
            this.dispose();
        } else if (this.buttonSave == e.getSource()) {
            this.transferData();
            String cf = "";
            cf += "base.code.path=" + this.ip.getBaseCodePath() + "\n";
            for (String token : this.ip.getBaseCodePathAlternateTokens()) {
                cf += "base.code.path." + token + "=" + this.ip.getBaseCodePath(token) + "\n";
            }
            cf += "company.name=" + this.ip.getCompanyName() + "\n";
            cf += "user.name=" + this.ip.getUserName() + "\n";
            cf += "user.token=" + this.ip.getUserToken() + "\n";
            try {
                FileUtil.writeTextToFile(this.configFilePath, false, cf);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            this.setVisible(false);
            this.dispose();
        }
    }

    private void transferData() {
        this.ip.setBaseCodePath(((JTextField) this.componentsByName.get("base.code.path")
                ).getText());
        this.ip.setCompanyName(((JTextField) this.componentsByName.get("company.name")
                ).getText());
        this.ip.setUserName(((JTextField) this.componentsByName.get("user.name")).getText()
                );
        this.ip.setUserToken(((JTextField) this.componentsByName.get("user.token")).getText(
                ));
        this.ip.clearAlternateBaseCodePathes();
        for (AlternatePathContainer c : this.alternatePathes) {
            this.ip.addAlternateBaseCodePath(c.getToken(), c.getAlternatePath());
        }
    }

    /**
     * @changed OLI 13.07.2014 - Added.
     */
    @Override public void focusGained(FocusEvent e) {
    }

    /**
     * @changed OLI 13.07.2014 - Added.
     */
    @Override public void focusLost(FocusEvent e) {
        this.tableAlternatePathes.repaint();
    }

    /**
     * @changed OLI 13.07.2014 - Added.
     */
    @Override public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_TAB) || (e.getKeyCode() == KeyEvent.VK_ENTER)) {
            this.tableAlternatePathes.repaint();
        }
    }

    /**
     * @changed OLI 13.07.2014 - Added.
     */
    @Override public void keyReleased(KeyEvent e) {
    }

    /**
     * @changed OLI 13.07.2014 - Added.
     */
    @Override public void keyTyped(KeyEvent e) {
    }

}