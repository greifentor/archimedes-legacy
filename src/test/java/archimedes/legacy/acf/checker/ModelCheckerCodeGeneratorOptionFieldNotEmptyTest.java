/*
 * ModelCheckerCodeGeneratorOptionFieldNotEmptyTest.java
 *
 * 27.05.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.legacy.acf.checker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.acf.checker.ModelCheckerCodeGeneratorOptionFieldNotEmpty;
import archimedes.acf.checker.ModelCheckerMessage;
import archimedes.legacy.scheme.Diagramm;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import baccara.gui.FileImageProvider;
import baccara.gui.GUIBundle;
import baccara.gui.PropertyResourceManager;
import corent.files.Inifile;
import corent.files.StructuredTextFile;

/**
 * Tests of the class <CODE>ModelCheckerCodeGeneratorOptionFieldNotEmpty</CODE>
 * 
 * @author ollie
 * 
 * @changed OLI 27.05.2016 - Added.
 */

public class ModelCheckerCodeGeneratorOptionFieldNotEmptyTest {

	private DataModel model = null;
	private ModelCheckerCodeGeneratorOptionFieldNotEmpty unitUnderTest = null;

	/**
	 * @changed OLI 06.08.2013 - Added.
	 */
	@BeforeEach
	public void setUpDataModelAndPreferences() throws Exception {
		System.setProperty("archimedes.gui.ComponentDiagramm.PAGESPERCOLUMN", "99");
		System.setProperty("archimedes.gui.ComponentDiagramm.PAGESPERROW", "99");
		System.setProperty("corent.base.StrUtil.suppress.html.note", "true");
		System.setProperty("corent.files.StructuredTextFile.suppress.FileNotFound", "true");
		FileInputStream in = new FileInputStream(new File("src/test/resources/dm/TestModel2.ads"));
		StructuredTextFile stf = new StructuredTextFile(in);
		Diagramm dm = null;
		try {
			stf.load();
			dm = new Diagramm();
			dm = (Diagramm) dm.createDiagramm(stf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.model = dm;
		System.setProperty("archimedes.gui.ArchimedesCommandProcessor.suppress.gui", "true");
	}

	/**
	 * @changed OLI 27.05.2016 - Added.
	 */
	@BeforeEach
	public void setUp() {
		GUIBundle guiBundle = new GUIBundle(new Inifile("unittests/conf/tst.ini"),
				new PropertyResourceManager(new Properties()), new FileImageProvider(new HashMap<String, String>()), 3,
				3);
		this.unitUnderTest = new ModelCheckerCodeGeneratorOptionFieldNotEmpty(guiBundle);
	}

	/**
	 * @changed OLI 27.05.2016 - Added.
	 */
	@Test
	public void testCheckReturnsAMessageIfDEPENDENTIsSetButNotREF() {
		TableModel tm = this.model.getTableByName("Table01");
		tm.setGenerateCodeOptions(";op");
		ModelCheckerMessage[] m = this.unitUnderTest.check(this.model);
		assertEquals(1, m.length);
		assertEquals(ModelCheckerCodeGeneratorOptionFieldNotEmpty.RES_MODEL_CHECKER_GENERATE_CODE_OPTIONS_FIELD_IS_SET,
				m[0].getMessage());
		assertEquals(ModelCheckerMessage.Level.WARNING, m[0].getLevel());
		assertSame(tm, m[0].getObject());

	}

}