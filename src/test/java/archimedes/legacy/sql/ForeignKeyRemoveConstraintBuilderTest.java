/*
 * ForeignKeyRemoveConstraintBuilderTest.java
 *
 * 25.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Vector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.model.ColumnMetaData;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.TableMetaData;
import archimedes.legacy.scheme.Diagramm;
import archimedes.legacy.script.sql.SQLScript;
import archimedes.legacy.sql.factories.PostgreSQLScriptFactory;
import corent.files.StructuredTextFile;

/**
 * Tests of the class <CODE>ForeignKeyAddConstraintBuilder</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 25.04.2013 - Added.
 */

public class ForeignKeyRemoveConstraintBuilderTest {

	private static final String QUOTE = "'";

	private SQLScriptFactory factory = null;
	private ColumnMetaData fk = null;
	private Vector metaData = null;
	private DataModel model = null;
	private SQLScript sql = null;
	private ForeignKeyRemoveConstraintBuilder unitUnderTest = null;

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		System.setProperty("archimedes.gui.ComponentDiagramm.PAGESPERCOLUMN", "99");
		System.setProperty("archimedes.gui.ComponentDiagramm.PAGESPERROW", "99");
		System.setProperty("corent.base.StrUtil.suppress.html.note", "true");
		StructuredTextFile stf = new StructuredTextFile("src/test/resources/conf/FK-Test.ads");
		stf.setHTMLCoding(true);
		stf.load();
		this.model = new Diagramm().createDiagramm(stf);
		this.metaData = this.createMetaData();
		this.sql = new SQLScript();
		this.factory = new PostgreSQLScriptFactory(QUOTE, this.model);
		this.unitUnderTest = new ForeignKeyRemoveConstraintBuilder(this.model, this.factory, this.metaData);
	}

	private Vector createMetaData() {
		Vector v = new Vector();
		TableMetaData tmd1 = new TableMetaData("Tabelle1");
		TableMetaData tmd2 = new TableMetaData("Tabelle2");
		ColumnMetaData id1 = new ColumnMetaData("Id");
		id1.primaryKey = true;
		ColumnMetaData id2 = new ColumnMetaData("Id");
		id2.primaryKey = true;
		this.fk = new ColumnMetaData("Fk");
		tmd1.addColumn(id1);
		tmd2.addColumn(id2);
		tmd2.addColumn(this.fk);
		v.add(tmd1);
		v.add(tmd2);
		return v;
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testBuildDoesNothingIfMetaDataAndModelAreEquals() {
		this.fk.setForeignKeyConstraintName("Tabelle2_Fk_fkey");
		this.fk.setReferencedTableName("Tabelle2");
		assertFalse(this.unitUnderTest.build(sql));
		assertEquals(sql, new SQLScript());
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testBuildExtendsThePassedSQLScriptCorrectlyWhenThereIsAConstraintToAdd() {
		this.fk.setForeignKeyConstraintName("NameIsNotKnown");
		assertTrue(this.unitUnderTest.build(sql));
		assertEquals(2, sql.size());
		assertEquals("", sql.getReducingStatements()[0]);
		assertEquals("ALTER TABLE " + QUOTE + "Tabelle2" + QUOTE + " DROP CONSTRAINT " + QUOTE + "NameIsNotKnown"
				+ QUOTE + ";", sql.getReducingStatements()[1]);
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testConstructorThrowsAnExceptionPassingANullPointerAsFactory() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new ForeignKeyRemoveConstraintBuilder(this.model, null, this.metaData);
		});
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testConstructorThrowsAnExceptionPassingANullPointerAsMetaData() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new ForeignKeyRemoveConstraintBuilder(this.model, this.factory, null);
		});
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testConstructorThrowsAnExceptionPassingANullPointerAsModel() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new ForeignKeyRemoveConstraintBuilder(null, this.factory, this.metaData);
		});
	}

}