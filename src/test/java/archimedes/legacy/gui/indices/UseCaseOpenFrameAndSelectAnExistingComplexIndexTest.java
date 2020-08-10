/*
 * UseCaseOpenFrameAndSelectAnExistingComplexIndexTest.java
 *
 * 15.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.indices;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Vector;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.Utils;
import archimedes.legacy.model.IndexMetaData;
import archimedes.legacy.scheme.Diagramm;
import gengen.metadata.AttributeMetaData;

/**
 * Hier werden Tests mit bereits bestehenden IndexMetaData-Objekten angestellt.
 * 
 * @author ollie
 * 
 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
 */

public class UseCaseOpenFrameAndSelectAnExistingComplexIndexTest {

	private static final String COLUMN_NAME_A = "ColumnA";
	private static final String COLUMN_NAME_B = "ColumnB";
	private static final String COLUMN_NAME_C = "ColumnC";
	private static final String INDEX_NAME_0 = "Index0";
	private static final String TABLE_NAME_A = "TableA";
	private static final String TABLE_NAME_B = "TableB";

	private AttributeMetaData columnA = null;
	private AttributeMetaData columnB = null;
	private Diagramm diagram = null;

	/**
	 * @changed OLI 21.11.2011 - Hinzugef&uuml;gt.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.columnA = Utils.createAttributeMetaDataMock(COLUMN_NAME_A);
		this.columnB = Utils.createAttributeMetaDataMock(COLUMN_NAME_B);
		this.diagram = new Diagramm();
		for (IndexMetaData imd : this.createIndexMetaDataList()) {
			this.diagram.addComplexIndex(imd);
		}
		ComplexIndicesAdministrationFrameUtil.openComplexIndicesAdministrationFrame(this.diagram);
	}

	private List<IndexMetaData> createIndexMetaDataList() {
		List<IndexMetaData> l = new Vector<IndexMetaData>();
		l.add(Utils.createIndexMetaData(INDEX_NAME_0,
				Utils.createClassMetaDataMock(TABLE_NAME_A, this.columnA, this.columnB),
				new AttributeMetaData[] { this.columnA }));
		return l;
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
	public void testSelectAnExistingIndexAndCheckIfShownInViewCorrectly() throws Exception {
		ComplexIndicesAdministrationFrameUtil.selectIndex(0);
		assertCheckValuesForIndex0Set();
	}

	private static void assertCheckValuesForIndex0Set() {
		assertCheckValuesForIndex0Set(true);
	}

	private static void assertCheckValuesForIndex0Set(boolean columnASelected) {
		assertCheckValuesForIndex0Set(TABLE_NAME_A);
		assertEquals(columnASelected, ComplexIndicesAdministrationFrameUtil.isColumnSelected(COLUMN_NAME_A));
		assertEquals(Boolean.FALSE, ComplexIndicesAdministrationFrameUtil.isColumnSelected(COLUMN_NAME_B));
		assertNull(ComplexIndicesAdministrationFrameUtil.isColumnSelected(COLUMN_NAME_C));
	}

	private static void assertCheckValuesForIndex0Set(String tableName) {
		assertEquals(INDEX_NAME_0, ComplexIndicesAdministrationFrameUtil.getIndexNameInputFieldContent());
		assertEquals(tableName, ComplexIndicesAdministrationFrameUtil.getSelectedTableName());
	}

	/**
	 * @changed OLI 16.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testSelectAnExistingIndexModifyButNotStoreItAndSelectItAgain() throws Exception {
		ComplexIndicesAdministrationFrameUtil.selectIndex(0);
		ComplexIndicesAdministrationFrameUtil.setNameTo(":o)");
		ComplexIndicesAdministrationFrameUtil.pressNewButton();
		ComplexIndicesAdministrationFrameUtil.setNameTo("Index1");
		ComplexIndicesAdministrationFrameUtil.pressStoreButton();
		ComplexIndicesAdministrationFrameUtil.selectIndex(0);
		assertCheckValuesForIndex0Set();
	}

	/**
	 * @changed OLI 16.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testSelectAnExistingIndexDeselectFirstColumnAndStoreIt() throws Exception {
		ComplexIndicesAdministrationFrameUtil.selectIndex(0);
		ComplexIndicesAdministrationFrameUtil.selectColumn(0);
		ComplexIndicesAdministrationFrameUtil.pressStoreButton();
		ComplexIndicesAdministrationFrameUtil.selectIndex(0);
		assertCheckValuesForIndex0Set(false);
	}

	/**
	 * @changed OLI 16.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testSelectAnExistingIndexSelectAnotherTableStoreIt() throws Exception {
		ComplexIndicesAdministrationFrameUtil.selectIndex(0);
		ComplexIndicesAdministrationFrameUtil.selectTable(1);
		ComplexIndicesAdministrationFrameUtil.pressStoreButton();
		ComplexIndicesAdministrationFrameUtil.selectIndex(0);
		assertCheckValuesForIndex0Set(TABLE_NAME_B);
	}

}