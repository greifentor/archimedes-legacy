package archimedes.legacy.gui.codepath;

import java.io.IOException;

import archimedes.legacy.model.DiagrammModel;
import baccara.gui.GUIBundle;

/**
 * A class which provided a code path.
 *
 * @author ollie (26.01.2020)
 */
public class CodePathProvider {

	private static final String INI_GROUP_NAME = "CodeFactoryPathes";

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
		String path = this.guiBundle.getInifile().readStr(INI_GROUP_NAME, this.getFieldNameForDiagram(), "");
		if (path.isEmpty()) {
			path = this.diagram.getCodePfad().replace("~", System.getProperty("user.home"));
		}
		return new CodePathDialog(this.guiBundle, path).getPath();
	}

	private String getFieldNameForDiagram() {
		return this.diagram.getName().replace(" ", "_");
	}

	/**
	 * Stores the passed path name into the ini file.
	 * 
	 * @param path The path to store for the current diagram model.
	 * @throws IOException If an error occurs while setting the path.
	 */
	public void storePath(String path) throws IOException {
		this.guiBundle.getInifile().writeStr(INI_GROUP_NAME, this.getFieldNameForDiagram(), path);
	}

}