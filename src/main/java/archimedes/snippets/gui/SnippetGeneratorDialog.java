package archimedes.snippets.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import archimedes.legacy.model.DataModel;
import archimedes.snippets.SnippetGenerator;
import archimedes.snippets.SnippetGenerator.ParameterDescription;
import baccara.gui.GUIBundle;
import baccara.gui.generics.BaccaraEditorPanel;
import baccara.gui.generics.ComponentData;
import corent.gui.JDialogWithInifile;

/**
 * A dialog for snippet generators.
 *
 * @author Oliver.Lieshoff (14.08.2020)
 *
 */
public class SnippetGeneratorDialog extends JDialogWithInifile implements ActionListener {

	private static final String RES_DIALOG = "snippetgenerator.dialog";

	private BaccaraEditorPanel<String, Map<String, Object>> editorPanelParameters;
	private GUIBundle guiBundle;
	private JButton buttonClose;
	private JButton buttonCopyToClipboard;
	private JButton buttonGenerateCode;
	private JTextArea textAreaGeneratedCode;
	private SnippetGenerator snippetGenerator;

	public SnippetGeneratorDialog(GUIBundle guiBundle, SnippetGenerator snippetGenerator, DataModel dataModel) {
		super(guiBundle.getInifile());
		this.guiBundle = guiBundle;
		this.snippetGenerator = snippetGenerator;
		setTitle(guiBundle.getResourceText(RES_DIALOG + "." + snippetGenerator.getResourcePrefix() + ".title"));
		setContentPane(createMainPanel());
		pack();
		setVisible(true);
		if (snippetGenerator.getParameterDescriptions().isEmpty()) {
			textAreaGeneratedCode.setText(snippetGenerator.generate(new HashMap<>(), dataModel));
		}
	}

	private JPanel createMainPanel() {
		JPanel p = new JPanel(guiBundle.createBorderLayout());
		if (!snippetGenerator.getParameterDescriptions().isEmpty()) {
			p.add(createParameterInputPanel(), BorderLayout.NORTH);
		}
		// TODO: Check for parameters existing and create editor panel for parameters as well as the generate code
		// button in this case. Set this in the upper region of the dialog.
		p.add(createCodeSnippetPanel(), BorderLayout.CENTER);
		p.add(createButtonPanel(), BorderLayout.SOUTH);
		return p;
	}

	private JPanel createParameterInputPanel() {
		JPanel p = new JPanel(guiBundle.createBorderLayout());
		editorPanelParameters = new BaccaraEditorPanel<String, Map<String, Object>>(guiBundle, this,
				RES_DIALOG + "." + snippetGenerator.getResourcePrefix() + ".parameters", this,
				new HashMap<String, Object>(), getComponentData()) {

			@Override
			public void setAttribute(Map<String, Object> t, ComponentData<String> cd, Object guiValue) {
				// TODO Auto-generated method stub

			}
		};
		p.add(editorPanelParameters, BorderLayout.CENTER);
		return p;
	}

	@SuppressWarnings("unchecked")
	private ComponentData<String>[] getComponentData() {
		return snippetGenerator.getParameterDescriptions() //
				.stream() //
				.map(this::getComponentData) //
				.collect(Collectors.toList()) //
				.toArray(new ComponentData[0]) //
		;
	}

	private ComponentData<String> getComponentData(ParameterDescription pd) {
		return new ComponentData<>(pd.getName(), baccara.gui.generics.Type.STRING, "");
	}

	private JScrollPane createCodeSnippetPanel() {
		textAreaGeneratedCode = new JTextArea(25, 40);
		return new JScrollPane(textAreaGeneratedCode);
	}

	private JPanel createButtonPanel() {
		JPanel p = new JPanel(guiBundle.createFlowLayout(FlowLayout.RIGHT));
		buttonCopyToClipboard = guiBundle.createButton(RES_DIALOG + ".button.copyToClipboard", "clipboard", this, p);
		p.add(new JSeparator(JSeparator.HORIZONTAL));
		buttonClose = guiBundle.createButton(RES_DIALOG + ".button.close", "close", this, p);
		return p;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttonClose) {
			doClose();
		} else if (e.getSource() == buttonCopyToClipboard) {
			doCopyToClipboard();
		}
	}

	private void doClose() {
		setVisible(false);
		dispose();
	}

	private void doCopyToClipboard() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection selection = new StringSelection(textAreaGeneratedCode.getText());
		clipboard.setContents(selection, selection);
	}
}