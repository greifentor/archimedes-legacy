/*
 * CodeFactoryMock.java
 *
 * 21.03.2009
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import java.io.File;

import archimedes.acf.checker.ModelChecker;
import archimedes.acf.event.CodeFactoryListener;
import archimedes.gui.checker.ModelCheckerMessageListFrameListener;
import archimedes.legacy.Archimedes;
import archimedes.legacy.model.DiagrammModel;
import archimedes.model.CodeFactory;
import archimedes.model.DataModel;
import baccara.gui.GUIBundle;
import corent.util.FileUtil;

/**
 * Ein Mock zum Test des Archimedes-Diagramms.
 * 
 * @author ollie
 *         <P>
 * 
 * @changed OLI 21.03.2009 - Hinzugef&uuml;gt.
 *          <P>
 * 
 */

public class CodeFactoryMock implements CodeFactory {

	private String code = null;

	/**
	 * Generiert ein CodeFactoryMock mit dem angegebenen Code.
	 * 
	 * @param code
	 *            Der Code, den das CodeFactoryMock erzeugen soll.
	 */
	public CodeFactoryMock(String code) {
		super();
		this.code = code;
	}

	/* Implementierung des Interfaces CodeFactory. */

	public boolean generate(DiagrammModel dm, String out) {
		try {
			out = FileUtil.CompletePath(out);
			new File(out).mkdirs();
			FileUtil.WriteTextToFile(out + this.code + ".tmp", false, this.code);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @changed OLI 31.03.2016 - Added.
	 */
	@Override
	public void setGUIBundle(GUIBundle guiBundle) {
	}

	/**
	 * @changed OLI 18.05.2016 - Added.
	 */
	@Override
	public ModelChecker[] getModelCheckers() {
		return new ModelChecker[0];
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Override
	public void setDataModel(DataModel dataModel) {
	}

	/**
	 * @changed OLI 09.06.2016 - Added.
	 */
	@Override
	public boolean generate(String out) {
		try {
			out = FileUtil.CompletePath(out);
			new File(out).mkdirs();
			FileUtil.WriteTextToFile(out + this.code + ".tmp", false, this.code);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @changed OLI 10.06.2016 - Added.
	 */
	@Override
	public String[] getResourceBundleNames() {
		return new String[0];
	}

	/**
	 * @changed OLI 15.06.2016 - Added.
	 */
	@Override
	public void setModelCheckerMessageListFrameListeners(ModelCheckerMessageListFrameListener... arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 08.12.2016 - Added.
	 */
	@Override
	public void addCodeFactoryListener(CodeFactoryListener arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 08.12.2016 - Added.
	 */
	@Override
	public void removeCodeFactoryListener(CodeFactoryListener arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 08.12.2016 - Added.
	 */
	@Override
	public GUIBundle getGUIBundle() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 18.04.2017 - Added.
	 */
	@Override
	public String getName() {
		return "CodeFactoryMock";
	}

	/**
	 * @changed OLI 18.04.2017 - Added.
	 */
	@Override
	public String getVersion() {
		return Archimedes.GetVersion();
	}

}