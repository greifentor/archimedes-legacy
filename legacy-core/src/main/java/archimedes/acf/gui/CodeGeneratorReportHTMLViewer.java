/*
 * CodeGeneratorReportHTMLViewer.java
 *
 * 16.09.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.gui;


import archimedes.acf.report.*;

import baccara.gui.*;

import corent.gui.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;


/**
 * A dialog to show the generation process report.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 28.11.2013 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public class CodeGeneratorReportHTMLViewer extends JDialogWithInifile implements ActionListener
        {

    private GUIBundle guiBundle = null; 
    private JButton buttonClose = null;

    /**
     * Creates a new Dialog with the passed parameters.
     *
     * @param guiBundle A bundle with GUI information.
     * @param title A title for the dialog.
     * @param message The message in HTML style.
     * 
     * @changed OLI 12.01.2015 - Added.
     */
    public CodeGeneratorReportHTMLViewer(GUIBundle guiBundle, String title, String message) {
        super((JFrame) null, guiBundle.getInifile());
        this.guiBundle = guiBundle;
        this.setTitle(title);
        this.setLayout(new BorderLayout(this.guiBundle.getHGap(), this.guiBundle.getVGap()));
        JPanel main = new JPanel(new BorderLayout(this.guiBundle.getHGap(),
                this.guiBundle.getVGap()));
        main.setBorder(new EmptyBorder(this.guiBundle.getVGap(), this.guiBundle.getHGap(),
                this.guiBundle.getVGap(), this.guiBundle.getHGap()));
        main.add(this.createButtonPanel(), BorderLayout.SOUTH);
        main.add(this.createViewPanel(message), BorderLayout.CENTER);
        this.setContentPane(main);
        this.pack();
        this.setModal(true);
        this.setVisible(true);
    }

    /**
     * Creates a new Dialog with the passed parameters.
     *
     * @param guiBundle A bundle with GUI information.
     * @param report The generation process report to show in the dialog.
     * @param version The version of the code generator.
     * 
     * @changed OLI 16.10.2013 - Added.
     */
    public CodeGeneratorReportHTMLViewer(GUIBundle guiBundle, GenerationProcessReport report,
            String version) {
        this(guiBundle, guiBundle.getResourceText("generation.process.report.frame.title"
                ).replace("$VERSION$", version), report.toHTML());
    }

    private JPanel createButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, this.guiBundle.getHGap(),
                this.guiBundle.getVGap()));
        this.buttonClose = this.createButton("close");
        p.add(buttonClose);
        return p;
    }

    private JButton createButton(String name) {
        JButton button = new JButton(this.guiBundle.getResourceText("generation.process.report."
                + "frame.button." + name + ".label"));
        button.addActionListener(this);
        ImageIcon icon = this.guiBundle.getImageProvider().getImageIcon("button_" + name);
        if (icon != null) {
            button.setIcon(icon);
        }
        return button;
    }

    private JPanel createViewPanel(String message) {
        JPanel p = new JPanel(new BorderLayout(this.guiBundle.getHGap(), this.guiBundle.getVGap(
                )));
        p.add(new JScrollPane(this.createEditorPane(message)));
        return p;
    }

    private JEditorPane createEditorPane(String message) {
        JEditorPane ep = new JEditorPane();
        ep.setEditable(false);
        ep.setContentType("text/html");
        ep.setText(message);
        return ep;
    }

    /**
     * @changed OLI 16.10.2013 - Added.
     */
    @Override public void actionPerformed(ActionEvent e) {
        if (this.buttonClose == e.getSource()) {
            this.setVisible(false);
            this.dispose();
        }
    }

}