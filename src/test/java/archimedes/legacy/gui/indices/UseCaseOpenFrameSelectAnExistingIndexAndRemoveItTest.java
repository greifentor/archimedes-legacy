/*
 * UseCaseOpenFrameSelectAnExistingIndexAndRemoveItTest.java
 *
 * 19.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.indices;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
 * Dieser Test &ouml;ffnet das Pflegefenster f&uuml;r komplexe Indices, selektiert einen bereits existierenden Index und
 * l&ouml;scht diesen.
 * 
 * @author ollie
 * 
 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
 */

public class UseCaseOpenFrameSelectAnExistingIndexAndRemoveItTest {

	private static final String COLUMN_NAME_A = "ColumnA";
	private static final String COLUMN_NAME_B = "ColumnB";
	private static final String COLUMN_NAME_C = "ColumnC";
	private static final String INDEX_NAME_0 = "Index0";
	private static final String INDEX_NAME_1 = "Index1";
	private static final String TABLE_NAME_A = "TableA";
	private static final String TABLE_NAME_B = "TableB";

	private AttributeMetaData columnA = null;
	private AttributeMetaData columnB = null;
	private AttributeMetaData columnC = null;
	private Diagramm diagram = null;

	/**
	 * @changed OLI 21.11.2011 - Hinzugef&uuml;gt.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.columnA = Utils.createAttributeMetaDataMock(COLUMN_NAME_A);
		this.columnB = Utils.createAttributeMetaDataMock(COLUMN_NAME_B);
		this.columnC = Utils.createAttributeMetaDataMock(COLUMN_NAME_C);
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
		l.add(Utils.createIndexMetaData(INDEX_NAME_1,
				Utils.createClassMetaDataMock(TABLE_NAME_B, this.columnB, this.columnC),
				new AttributeMetaData[] { this.columnC }));
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
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testSelectTheFirstIndexAndRemoveIt() throws Exception {
		assertEquals(2, this.diagram.getComplexIndexCount());
		ComplexIndicesAdministrationFrameUtil.selectIndex(0);
		ComplexIndicesAdministrationFrameUtil.pressRemoveButton();
		assertEquals(1, this.diagram.getComplexIndexCount());
		ComplexIndicesAdministrationFrameUtil.selectIndex(0);
		assertEquals(TABLE_NAME_B, ComplexIndicesAdministrationFrameUtil.getSelectedTableName());
	}

}