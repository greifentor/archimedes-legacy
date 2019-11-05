package archimedes.legacy.scripting;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import archimedes.legacy.app.ApplicationUtil;
import archimedes.legacy.model.DiagrammModel;
import corent.base.Constants;
import corent.files.ExtensionFileFilter;
import corent.files.Inifile;
import corent.gui.COButton;
import corent.gui.COLabel;
import corent.gui.JDialogThrowable;
import corent.gui.JDialogWithInifile;
import corent.gui.PropertyRessourceManager;
import corent.gui.RessourceManager;
import corentx.io.FileUtil;
import corentx.script.JScriptRunner;
import corentx.util.Str;

/**
 * A dialog for edit and run JavaScripts on the Archimedes database model.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.08.2011 - created.
 * @changed OLI 05.11.2019 - reincarnated.
 */

public class DialogScriptExecuter extends JDialogWithInifile implements ActionListener, KeyListener {

	private static final Logger LOG = Logger.getLogger(DialogScriptExecuter.class);
	private static final String CONTEXTNAME = ".archimedes.legacy.scripting.DialogScriptExecuter";

	private COButton buttonClose = new COButton("close", CONTEXTNAME + ".buttonClose");
	private COButton buttonExecute = new COButton("execute", CONTEXTNAME + ".buttonExecute");
	private COButton buttonLoad = new COButton("load", CONTEXTNAME + ".buttonLoad");
	private COLabel labelScriptField = new COLabel("script", CONTEXTNAME + ".labelScriptField");
	private DiagrammModel dm = null;
	private JTextArea textAreaScript = new JTextArea(40, 1);
	private RessourceManager rm = new PropertyRessourceManager();

	public DialogScriptExecuter(Inifile ini, JFrame parent, DiagrammModel dm, String defaultTitle) {
		super(parent, corent.base.Utl.GetProperty(CONTEXTNAME + ".title", defaultTitle), true, ini);
		this.dm = dm;
		JPanel mainPanel = ApplicationUtil.BuildBorderedPanel();
		mainPanel.add(this.createScriptPanel(), BorderLayout.CENTER);
		mainPanel.add(this.createButtonsPanel(), BorderLayout.SOUTH);
		this.setContentPane(mainPanel);
		this.updateComponents();
		this.buttonClose.addActionListener(this);
		this.buttonExecute.addActionListener(this);
		this.buttonLoad.addActionListener(this);
		this.setVisible(true);
	}

	private JPanel createButtonsPanel() {
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, Constants.HGAP, Constants.VGAP));
		buttonsPanel.setBorder(ApplicationUtil.BuildBorder());
		buttonsPanel.add(this.buttonExecute);
		buttonsPanel.add(new JLabel(""));
		buttonsPanel.add(this.buttonLoad);
		buttonsPanel.add(new JLabel(""));
		buttonsPanel.add(this.buttonClose);
		return buttonsPanel;
	}

	private JPanel createScriptPanel() {
		JPanel contentPanel = ApplicationUtil.BuildFullBorderedPanel();
		contentPanel.add(this.labelScriptField, BorderLayout.NORTH);
		contentPanel.add(new JScrollPane(this.textAreaScript), BorderLayout.CENTER);
		return contentPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == this.buttonClose) {
			this.closeDialog();
		} else if (source == this.buttonExecute) {
			this.executeScript();
		} else if (source == this.buttonLoad) {
			this.loadScript();
		}
	}

	public void closeDialog() {
		this.setVisible(false);
	}

	public void executeScript() {
		try {
			LOG.info("executing script");
			Map<String, Object> params = new Hashtable<String, Object>();
			params.put("model", this.dm);
			new JScriptRunner(this.textAreaScript.getText(), params, null).exec();
		} catch (Exception e) {
			new JDialogThrowable(e, Str.fromHTML("Fehler beim Ausf&uuml;hren des Scripts."), this.getInifile(),
					new PropertyRessourceManager());
		}
	}

	public void loadScript() {
		try {
			LOG.info("loading script");
			String dn = "";
			JFileChooser fc = new JFileChooser(".");
			fc.setAcceptAllFileFilterUsed(false);
			ExtensionFileFilter eff = new ExtensionFileFilter(new String[] { "js" });
			fc.setFileFilter(eff);
			fc.setCurrentDirectory(new File("."));
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				dn = fc.getSelectedFile().getAbsolutePath();
				this.textAreaScript.setText(FileUtil.readTextFromFile(dn));
			}
		} catch (Exception e) {
			new JDialogThrowable(e, Str.fromHTML("Fehler beim Laden des Scripts."), this.getInifile(),
					new PropertyRessourceManager());
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	private void updateComponents() {
		this.buttonClose.update(this.rm);
		this.buttonExecute.update(this.rm);
		this.buttonLoad.update(this.rm);
		this.labelScriptField.update(this.rm);
	}

}