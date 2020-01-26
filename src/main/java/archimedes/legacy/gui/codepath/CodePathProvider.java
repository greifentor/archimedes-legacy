package archimedes.legacy.gui.codepath;

import archimedes.legacy.model.DiagrammModel;
import baccara.gui.GUIBundle;

/**
 * A class which provided a code path.
 *
 * @author ollie (26.01.2020)
 */
public class CodePathProvider {

	private DiagrammModel diagram;
	private GUIBundle guiBundle;

	public CodePathProvider(GUIBundle guiBundle, DiagrammModel diagram) {
		super();
		this.diagram = diagram;
		this.guiBundle = guiBundle;
	}

	/**
	 * Returns a code path.
	 * 
	 * @return a code path.
	 */
	public String getCodePath() {
		String path = this.diagram.getCodePfad().replace("~", System.getProperty("user.home"));
		return new CodePathDialog(this.guiBundle, path).getPath();
	}

}