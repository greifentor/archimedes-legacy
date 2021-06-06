package archimedes.legacy.gui.codepath;

import java.awt.Container;
import java.awt.event.ActionListener;

import baccara.gui.GUIBundle;
import baccara.gui.generics.BaccaraEditorPanel;
import baccara.gui.generics.ComponentData;
import baccara.gui.generics.Type;

/**
 * An editor panel for code path editing
 *
 * @author ollie (26.01.2020)
 */
public class CodePathEditorPanel extends BaccaraEditorPanel<String, CodePath> { // NOSONAR OLI: Was soll man machen ?!?

	@SuppressWarnings("unchecked")
	public CodePathEditorPanel(GUIBundle guiBundle, Container parent, ActionListener actionListener,
			CodePath codePath) {
		super(guiBundle, parent, "CodePathDialog", actionListener, codePath, new ComponentData[] { // NOSONAR
				new ComponentData<>("path", Type.FILE_NAME, codePath.getPath()) //
		});
	}

	@Override
	public void setAttribute(CodePath codePath, ComponentData<String> component, Object obj) {
		codePath.setPath((String) obj);
	}

}