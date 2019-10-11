/*
 * UseCaseOpenFrameCreateANewComplexIndexAndStoreItTest.java
 *
 * 15.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.indices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.scheme.Diagramm;
import archimedes.model.IndexMetaData;

/**
 * Dieser Test &Ouml;ffnet das Fenster zur Wartung komplexer Indices, erzeugt einen neuen komplexen Index und speichert
 * diesen.
 * 
 * @author ollie
 * 
 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
 */

public class UseCaseOpenFrameCreateANewComplexIndexAndStoreItTest {

	private Diagramm diagram = null;

	/**
	 * @changed OLI 21.11.2011 - Hinzugef&uuml;gt.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.diagram = new Diagramm();
		ComplexIndicesAdministrationFrameUtil.openComplexIndicesAdministrationFrame(this.diagram);
	}

	/**
	 * @changed OLI 21.11.2011 - Hinzugef&uuml;gt.
	 */
	@AfterEach
	public void tearDown() throws Exception {
		ComplexIndicesAdministrationFrameUtil.disposeComplexIndicesAdministrationFrame();
	}

	/**
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testCreateNewComplexIndexAndStoreIt() throws Exception {
		assertEquals(0, this.diagram.getComplexIndexCount());
		this.createANewIndex("NewIndex");
		assertEquals(1, this.diagram.getComplexIndexCount());
		assertIndexInListIs("NewIndex", 0);
	}

	private void createANewIndex(String indexName) throws Exception {
		ComplexIndicesAdministrationFrameUtil.pressNewButton();
		ComplexIndicesAdministrationFrameUtil.setNameTo(indexName);
		ComplexIndicesAdministrationFrameUtil.selectTable(0);
		ComplexIndicesAdministrationFrameUtil.selectColumn(0);
		ComplexIndicesAdministrationFrameUtil.selectColumn(1);
		ComplexIndicesAdministrationFrameUtil.pressStoreButton();
	}

	private static void assertIndexInListIs(String indexName, int index) {
		IndexMetaData imd = ComplexIndicesAdministrationFrameUtil.getItemOfList(index);
		assertNotNull(imd);
		assertEquals(indexName, imd.getName());
		assertEquals("TableA", imd.getTable().getName());
		assertEquals("ColumnA", imd.getColumns()[0].getName());
		assertEquals("ColumnB", imd.getColumns()[1].getName());
	}

	/**
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testCreateNewComplexIndexAndStoreItAndDoThisAgainWithANewIndexName() throws Exception {
		assertEquals(0, this.diagram.getComplexIndexCount());
		this.createANewIndex("NewIndex1");
		this.createANewIndex("NewIndex2");
		assertEquals(2, this.diagram.getComplexIndexCount());
		assertIndexInListIs("NewIndex1", 0);
		assertIndexInListIs("NewIndex2", 1);
	}

}