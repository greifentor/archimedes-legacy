/*
 * TestDiagramm.java
 *
 * 08.03.2009
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JDialogOperator;

import archimedes.legacy.Archimedes;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.TabellenModel;
import archimedes.model.CodeFactory;
import corent.base.SortedVector;
import corent.base.StrUtil;
import corent.db.ConnectionManager;
import corent.db.DBExec;
import corent.db.JDBCDataSourceRecord;
import corent.files.StructuredTextFile;

/**
 * Testf&auml;lle f&uuml;r die Diagramm-Klasse.
 * 
 * @author ollie
 * 
 * @changed OLI 08.03.2009 - Hinzugef&uuml;gt.
 */

public class TestDiagram {

	private CodeFactory/* Mock */ dcfm; // = new
	// CodeFactoryMock("CodeDefault");
	private Diagramm dm2 = null;
	private DiagrammModel dm = null;
	private JDBCDataSourceRecord jdbcdsr2 =
			new JDBCDataSourceRecord("org.hsqldb.jdbcDriver", "jdbc:hsqldb:target/test2", "sa", "");

	@BeforeEach
	public void setUp() throws Exception {
		this.dcfm = mock(CodeFactory.class);
		System.setProperty("corent.base.StrUtil.suppress.html.note", "true");
		System.setProperty("archimedes.gui.ComponentDiagramm.PAGESPERCOLUMN", "99");
		System.setProperty("archimedes.gui.ComponentDiagramm.PAGESPERROW", "99");
		Connection c = null;
		InputStream in = Archimedes.Factory.getClass().getClassLoader().getResourceAsStream("dm/TestModel.ads");
		int i = 0;
		int leni = 0;
		StructuredTextFile stf = new StructuredTextFile(in);
		TabellenModel tm = null;
		Vector vtm = null;
		try {
			stf.load();
			this.dm = new Diagramm();
			this.dm = (Diagramm) dm.createDiagramm(stf);
		} catch (Exception e) {
			e.printStackTrace();
			fail("failure while initializing test diagram!");
		}
		in = Archimedes.Factory.getClass().getClassLoader().getResourceAsStream("dm/TestModel2.ads");
		stf = new StructuredTextFile(in);
		try {
			stf.load();
			this.dm2 = new Diagramm();
			this.dm2 = (Diagramm) dm2.createDiagramm(stf);
		} catch (Exception e) {
			e.printStackTrace();
			fail("failure while initializing test diagram 2!");
		}
		System.setProperty("archimedes.gui.ArchimedesCommandProcessor.suppress.gui", "true");
		try {
			c = ConnectionManager.GetConnection(jdbcdsr2);
			vtm = this.dm2.getTabellen();
			for (i = 0, leni = vtm.size(); i < leni; i++) {
				tm = (Tabelle) vtm.get(i);
				DBExec.Update(c, "drop table " + tm.getName() + " if exists");
				DBExec.Update(c, tm.makeCreateStatement(false, false, false));
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("failure while initializing test database 2!");
		}
	}

	private void cleanFiles() {
		new File("./Code01.tmp").delete();
		new File("./Code02.tmp").delete();
		new File("./CodeDefault.tmp").delete();
	}

	/** Test der Methode generateCode(CodeFactory, String). */
	@Test
	public void testGenerateCodeNoFactoryNameDefinedUsesPassedOne() {
		boolean ok = false;
		// Default-Factory.
		this.dcfm = mock(CodeFactory.class);
		when(this.dcfm.generate("./")).thenReturn(true);
		this.cleanFiles();
		this.dm.setCodeFactoryClassName("");
		ok = this.dm.generateCode(this.dcfm, "./", null);
		assertTrue(ok);
		/*
		 * assertTrue(!new File("./Code01.tmp").exists() && !new File("./Code02.tmp").exists() && new
		 * File("./CodeDefault.tmp").exists());
		 */
	}

	/**
	 * @changed OLI 21.06.2016 - Added.
	 */
	@Test
	public void testGenerateCodePassesNullForDefaultFactory() {
		this.cleanFiles();
		this.dm.setCodeFactoryClassName("DefaultCode");
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					JDialogOperator dialog = new JDialogOperator(StrUtil.FromHTML("Codegenerator"));
					JButtonOperator button = new JButtonOperator(dialog, "OK");
					button.doClick();
				} catch (Exception e) {
				}
			}
		});
		th.start();
		boolean ok = this.dm.generateCode(null, "./", null);
		assertTrue(ok);
		assertTrue(
				!new File("./Code01.tmp").exists() && !new File("./Code02.tmp").exists()
						&& !new File("./CodeDefault.tmp").exists());
	}

	/**
	 * @changed OLI 21.06.2016 - Added.
	 */
	@Test
	public void testGenerateCodeUsesANotExistingFantasyFactory() {
		this.dcfm = mock(CodeFactory.class);
		when(this.dcfm.generate("./")).thenReturn(true);
		this.cleanFiles();
		this.dm.setCodeFactoryClassName("archimedes.scheme.CodeFactoryMockFantasy");
		boolean ok = this.dm.generateCode(this.dcfm, "./", null);
		assertTrue(ok);
		assertTrue(
				!new File("./Code01.tmp").exists() && !new File("./Code02.tmp").exists()
						&& !new File("./CodeDefault.tmp").exists());
		// Default-Factory auf null und nicht existente Phantasie-Factory.
		/*
		 * das laeuft richtig. Allerdings bekommt man dadurch erzeugte Directory "testmodel" nicht mehr weg. Deshalb ist
		 * es auskommentiert.
		 * 
		 * this.cleanFiles();this.dm.setCodeFactoryClassName( "archimedes.scheme.CodeFactoryMockFantasy"); ok =
		 * this.dm.generateCode(null, "./"); assertTrue(ok); assertTrue(!new File("./Code01.tmp").exists() && !new
		 * File("./Code02.tmp").exists() && !new File("./CodeDefault.tmp").exists()); //
		 */
		// Nochmal aufraeumen.
		this.cleanFiles();
	}

	/** Test der Methode getCodegeneratorOptionsListTag(String, String). */
	@Test
	public void testGetCodegeneratorOptionsListTag() {
		List<String> ls = null;
		Map<String, List<String>> m = null;
		m = this.dm.getCodegeneratorOptionsListTag("Test", ",");
		assertTrue((m.size() == 0));
		m = this.dm.getCodegeneratorOptionsListTag("TestTag", ",");
		assertTrue((m.size() == 1));
		if (m.size() == 1) {
			ls = m.get("TableScheme");
			assertNotNull(ls);
			if (ls != null) {
				assertTrue(ls.size() == 2);
				if (ls.size() == 2) {
					assertEquals(ls.get(0).toString(), "DataScheme");
					assertEquals(ls.get(1).toString(), "ColumnScheme");
				}
			}
		}
		m = this.dm.getCodegeneratorOptionsListTag("TestTag", "$");
		assertTrue((m.size() == 1));
		if (m.size() == 1) {
			ls = m.get("TableScheme");
			assertNotNull(ls);
			if (ls != null) {
				assertTrue(ls.size() == 1);
				if (ls.size() == 1) {
					assertEquals(ls.get(0).toString(), "DataScheme, ColumnScheme");
				}
			}
		}
	}

	/** Test der Methode getKeyColumns(). */
	@Test
	public void testGetKeyColumns() {
		SortedVector sv = new SortedVector();
		SortedVector sv0 = null;
		TabellenModel tm = this.dm2.getTabelle("Table01");
		sv.add("Table02.Table01");
		sv.add("Table02.Table03");
		sv.add("Table03.Table03");
		sv.add("Table04.Table04");
		sv0 = this.dm2.getKeyColumns(tm);
		assertEquals(sv.toString(), sv0.toString());
	}

	/** Test der Methode getKeycolumns(). */
	@Test
	public void testGetKeycolumns() {
		SortedVector sv = new SortedVector();
		SortedVector sv0 = null;
		TabellenModel tm = this.dm2.getTabelle("Table01");
		sv.add("Table02.Table01");
		sv.add("Table02.Table03");
		sv.add("Table03.Table03");
		sv.add("Table04.Table04");
		sv0 = this.dm2.getKeycolumns(tm);
		assertEquals(sv.toString(), sv0.toString());
	}

	/**
	 * Test der Methode getProjectToken().
	 * 
	 * @changed OLI 30.09.2009 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testGetProjectToken() {
		assertEquals("TestModel", this.dm.getProjectToken());
		((Diagramm) this.dm).setApplicationName("Ein Kleiner.Test-Fall !");
		assertEquals("EinKleinerTestFall", this.dm.getProjectToken());
	}

}