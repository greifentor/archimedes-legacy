/*
 * ArtifactGeneratorListConfigurationDialog.java
 *
 * 16.09.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.gui;


import archimedes.acf.*;
import archimedes.model.*;

import baccara.gui.*;

import corent.gui.*;

import gengen.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;


/**
 * A modal dialog to configure the code generators.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 16.10.2013 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public class CodeGeneratorListConfigurationDialog extends JDialogWithInifile
        implements ActionListener {

    public enum ClosingState {CANCEL, GENERATE};

    private IndividualPreferences ip = null;
    private JButton buttonCancel = null;
    private JButton buttonConfig = null;
    private JButton buttonGenerate = null;
    private JButton buttonSelectGeneratorsAll = null;
    private JButton buttonSelectGeneratorsNone = null;
    private JButton buttonSelectTablesAll = null;
    private JButton buttonSelectTablesNone = null;
    private ClosingState closingState = null;
    private CodeGenerator[] codeGenerators = null;
    private String configFilePath = null;
    private GUIBundle guiBundle = null;
    private JLabel labelPath = null;
    private TableModel[] tables = null; 

    /**
     * Creates a new Dialog with the passed parameters.
     *
     * @param guiBundle A bundle with GUI information.
     * @param codeGenerator A list of code generators which are configured by the dialog.
     * @param ip Individual preferences for code generation.
     * @param tables The list of tables to select those tables which the code is to create for.
     * @param configFilePath The path of the file with the individual preferences.
     * @param codeFactoryVersion The version number of the code factory.
     *
     * @changed OLI 16.10.2013 - Added.
     */
    public CodeGeneratorListConfigurationDialog(GUIBundle guiBundle,
            CodeGenerator[] codeGenerators, IndividualPreferences ip, TableModel[] tables,
            String configFilePath, String codeFactoryVersion) {
        super((JFrame) null, guiBundle.getInifile());
        this.codeGenerators = codeGenerators;
        this.configFilePath = configFilePath;
        this.ip = ip;
        this.tables = tables;
        this.guiBundle = guiBundle;
        this.setTitle(this.guiBundle.getResourceText("code.generators.configuration.title"
                ).replace("$VERSION$", codeFactoryVersion));
        this.setLayout(new BorderLayout(this.guiBundle.getHGap(), this.guiBundle.getVGap()));
        JPanel main = new JPanel(new BorderLayout(this.guiBundle.getHGap(),
                this.guiBundle.getVGap()));
        main.setBorder(new EmptyBorder(this.guiBundle.getVGap(), this.guiBundle.getHGap(),
                this.guiBundle.getVGap(), this.guiBundle.getHGap()));
        main.add(this.createPathLabelPanel(ip.getBaseCodePathes()), BorderLayout.NORTH);
        main.add(this.createButtonPanel(), BorderLayout.SOUTH);
        main.add(this.createTablePanel(), BorderLayout.CENTER);
        this.setContentPane(main);
        this.pack();
        this.setModal(true);
        this.setVisible(true);
    }

    private JPanel createPathLabelPanel(String basePath) {
        JLabel label = new JLabel(this.guiBundle.getResourceText("code.generators.configuration"
                + ".label.path.label"));
        this.labelPath = new JLabel(basePath);
        this.labelPath.setForeground(new Color(0, 0, 160));
        this.labelPath.setFont(new Font("SansSerif", Font.BOLD, 12));
        this.buttonConfig = this.createButton("config");
        JPanel p = new JPanel(new BorderLayout(this.guiBundle.getHGap(), this.guiBundle.getVGap(
                )));
        p.add(label, BorderLayout.WEST);
        p.add(this.labelPath, BorderLayout.CENTER);
        p.add(this.buttonConfig, BorderLayout.EAST);
        return p;
    }

    private JPanel createButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, this.guiBundle.getHGap(),
                this.guiBundle.getVGap()));
        this.buttonCancel = this.createButton("cancel");
        this.buttonGenerate = this.createButton("generate");
        p.add(buttonGenerate);
        p.add(new JLabel("    "));
        p.add(buttonCancel);
        return p;
    }

    private JButton createButton(String name) {
        JButton button = new JButton(this.guiBundle.getResourceText("code.generators."
                + "configuration.button." + name + ".label"));
        button.addActionListener(this);
        ImageIcon icon = this.guiBundle.getImageProvider().getImageIcon("button_" + name);
        if (icon != null) {
            button.setIcon(icon);
        }
        return button;
    }

    private JPanel createTablePanel() {
        JPanel p = new JPanel(new GridLayout(1, 2, this.guiBundle.getHGap(),
                this.guiBundle.getVGap()));
        p.add(this.createTablesTablePanel());
        p.add(this.createGeneratorsTablePanel());
        return p;
    }

    private JPanel createTablesTablePanel() {
        JTable tableTables = new JTableWithInifile(new TablesTableModel(this.tables,
                this.guiBundle), this.guiBundle.getInifile(), "TablesConfigurationTable"
                );
        JPanel panelTables = new JPanel(new BorderLayout(this.guiBundle.getHGap(),
                this.guiBundle.getVGap()));
        panelTables.setBorder(new LineBorder(Color.darkGray, 1));
        panelTables.add(new JScrollPane(tableTables), BorderLayout.CENTER);
        panelTables.add(this.createTablesButtonPanel(), BorderLayout.SOUTH);
        return panelTables;
    }

    private JPanel createGeneratorsTablePanel() {
        JTable tableGenerators = new JTableWithInifile(new CodeGeneratorListTableModel(
                this.codeGenerators, this.guiBundle), this.guiBundle.getInifile(),
                "CodeGeneratorConfigurationTable");
        tableGenerators.setDefaultRenderer(String.class, new TableGeneratorsCellRenderer(
                this.codeGenerators));
        JPanel panelGenerators = new JPanel(new BorderLayout(this.guiBundle.getHGap(),
                this.guiBundle.getVGap()));
        panelGenerators.setBorder(new LineBorder(Color.darkGray, 1));
        panelGenerators.add(new JScrollPane(tableGenerators), BorderLayout.CENTER);
        panelGenerators.add(this.createGeneratorButtonPanel(), BorderLayout.SOUTH);
        return panelGenerators;
    }

    private JPanel createTablesButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, this.guiBundle.getHGap(),
                this.guiBundle.getVGap()));
        this.buttonSelectTablesAll = this.createButton("selectAll");
        this.buttonSelectTablesNone = this.createButton("selectNothing");
        p.add(buttonSelectTablesAll);
        p.add(buttonSelectTablesNone);
        return p;
    }

    private JPanel createGeneratorButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, this.guiBundle.getHGap(),
                this.guiBundle.getVGap()));
        this.buttonSelectGeneratorsAll = this.createButton("selectAll");
        this.buttonSelectGeneratorsNone = this.createButton("selectNothing");
        p.add(buttonSelectGeneratorsAll);
        p.add(buttonSelectGeneratorsNone);
        return p;
    }

    /**
     * @changed OLI 16.10.2013 - Added.
     */
    @Override public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.buttonCancel) {
            this.closeDialog(ClosingState.CANCEL);
        } else if (e.getSource() == this.buttonConfig) {
            new IndividualPreferencesDialog(this, this.guiBundle, this.ip, this.configFilePath);
            this.updateView();
        } else if (e.getSource() == this.buttonGenerate) {
            this.closeDialog(ClosingState.GENERATE);
        } else if ((e.getSource() == this.buttonSelectGeneratorsAll)
                || (e.getSource() == this.buttonSelectGeneratorsNone)) {
            for (CodeGenerator cg : this.codeGenerators) {
                cg.setTemporarilySuspended(e.getSource() == this.buttonSelectGeneratorsNone);
            }
            this.repaint();
        } else if ((e.getSource() == this.buttonSelectTablesAll)
                || (e.getSource() == this.buttonSelectTablesNone)) {
            for (TableModel tm : this.tables) {
                tm.setGenerateCode(e.getSource() == this.buttonSelectTablesAll);
            }
            this.repaint();
        }
    }

    private void closeDialog(ClosingState closingState) {
        this.closingState = closingState;
        this.setVisible(false);
        this.dispose();
    }

    /**
     * Returns the closing state of the dialog.
     *
     * @return The closing state of the dialog.
     *
     * @changed OLI 16.10.2013 - Added.
     */
    public ClosingState getClosingState() {
        return this.closingState;
    }

    /**
     * Updates the view.
     *
     * @changed OLI 10.07.2014 - Added.
     */
    public void updateView() {
        this.labelPath.setText(this.ip.getBaseCodePathes());
    }

}