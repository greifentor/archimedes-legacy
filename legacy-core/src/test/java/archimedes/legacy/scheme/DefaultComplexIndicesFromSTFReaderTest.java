/*
 * DefaultComplexIndicesFromSTFReaderTest.java
 *
 * 20.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static archimedes.legacy.scheme.ComplexIndicesUtil.indexCount;
import static archimedes.legacy.scheme.ComplexIndicesUtil.params;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Vector;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.Utils;
import archimedes.model.IndexMetaData;
import corent.files.StructuredTextFile;
import gengen.metadata.AttributeMetaData;
import gengen.metadata.ClassMetaData;
import gengen.metadata.ModelMetaData;

/**
 * Tests zur Klasse <CODE>DefaultComplexIndicesFromSTFReader</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
 */

public class DefaultComplexIndicesFromSTFReaderTest {

	private static final String INDEX_NAME_0 = "Index0";
	private static final String INDEX_NAME_1 = "Index1";
	private static final String COLUMN_NAME_X = "ColumnX";
	private static final String COLUMN_NAME_Y = "ColumnY";
	private static final String COLUMN_NAME_Z = "ColumnZ";
	private static final String TABLE_NAME_X = "TableX";
	private static final String TABLE_NAME_Y = "TableY";

	private AttributeMetaData columnX = null;
	private AttributeMetaData columnY = null;
	private ClassMetaData tableX = null;
	private ClassMetaData tableY = null;
	private DefaultComplexIndicesFromSTFReader unitUnderTest = null;
	private List<IndexMetaData> indices = null;
	private ModelMetaData model = null;
	private StructuredTextFile stf = null;

	/**
	 * @changed OLI 21.11.2011 - Hinzugef&uuml;gt.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.columnX = Utils.createAttributeMetaDataMock(COLUMN_NAME_X);
		this.columnY = Utils.createAttributeMetaDataMock(COLUMN_NAME_Y);
		this.stf = this.createStructuredTextFile();
		this.tableX = this.createClassMetaData(TABLE_NAME_X);
		this.indices = new Vector<IndexMetaData>();
		this.model = this.createModel();
		this.unitUnderTest = new DefaultComplexIndicesFromSTFReader();
	}

	private StructuredTextFile createStructuredTextFile() {
		StructuredTextFile stf = new StructuredTextFile("tmp");
		stf.writeLong(indexCount(), 2);
		this.createTableWithUnexistingColumnReference(0, stf, INDEX_NAME_0);
		this.createUnexistingTable(1, stf, INDEX_NAME_1);
		return stf;
	}

	private void createTableWithUnexistingColumnReference(int pos, StructuredTextFile stf, String name) {
		stf.writeStr(params(pos, "Name"), name);
		stf.writeStr(params(pos, "Table", "Name"), TABLE_NAME_X);
		stf.writeLong(params(pos, "ColumnCount"), 2);
		stf.writeStr(params(pos, "Column0", "Name"), COLUMN_NAME_X);
		stf.writeStr(params(pos, "Column1", "Name"), COLUMN_NAME_Z);
	}

	private void createUnexistingTable(int pos, StructuredTextFile stf, String name) {
		stf.writeStr(params(pos, "Name"), name);
		stf.writeStr(params(pos, "Table", "Name"), TABLE_NAME_Y);
		stf.writeLong(params(pos, "ColumnCount"), 1);
		stf.writeStr(params(pos, "Column0", "Name"), COLUMN_NAME_Y);
	}

	private ClassMetaData createClassMetaData(String name) {
		ClassMetaData cmd = EasyMock.createMock(ClassMetaData.class);
		EasyMock.expect(cmd.getName()).andReturn(name);
		EasyMock.expect(cmd.getAttribute(COLUMN_NAME_X)).andReturn(this.columnX);
		EasyMock.expect(cmd.getAttribute(COLUMN_NAME_Y)).andReturn(this.columnY);
		EasyMock.expect(cmd.getAttribute(COLUMN_NAME_Z)).andReturn(null);
		EasyMock.replay(cmd);
		return cmd;
	}

	private ModelMetaData createModel() {
		ModelMetaData mmd = EasyMock.createMock(ModelMetaData.class);
		EasyMock.expect(mmd.getClass(0)).andReturn(this.tableX);
		EasyMock.expect(mmd.getClass(TABLE_NAME_X)).andReturn(this.tableX);
		EasyMock.expect(mmd.getClass(TABLE_NAME_Y)).andReturn(this.tableY);
		EasyMock.replay(mmd);
		return mmd;
	}

	private List<IndexMetaData> createIndexList() {
		List<IndexMetaData> l = new Vector<IndexMetaData>();
		l.add(Utils.createIndexMetaData(INDEX_NAME_0, this.tableX, this.columnY));
		return l;
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testReadThrowingAnExceptionPassingANullPointerAsDataModel() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.read(this.stf, this.indices, null);
		});
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testReadThrowingAnExceptionPassingANullPointerAsIndexList() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.read(this.stf, null, this.model);
		});
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testReadThrowingAnExceptionPassingANullPointerAsStructuredTextFile() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.read(null, this.indices, this.model);
		});
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testReadIndexListContainsIndexWithReferenceToColumnX() throws Exception {
		this.unitUnderTest.read(this.stf, this.indices, this.model);
		assertEquals(1, this.indices.size());
		assertEquals(INDEX_NAME_0, this.indices.get(0).getName());
		assertEquals(TABLE_NAME_X, this.indices.get(0).getTable().getName());
		assertEquals(1, this.indices.get(0).getColumns().length);
		assertEquals(COLUMN_NAME_X, this.indices.get(0).getColumns()[0].getName());
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testReadCleaningTheIndexListFirst() throws Exception {
		this.indices = this.createIndexList();
		this.unitUnderTest.read(this.stf, this.indices, this.model);
		assertEquals(1, this.indices.size());
		assertEquals(INDEX_NAME_0, this.indices.get(0).getName());
		assertEquals(TABLE_NAME_X, this.indices.get(0).getTable().getName());
		assertEquals(1, this.indices.get(0).getColumns().length);
		assertEquals(COLUMN_NAME_X, this.indices.get(0).getColumns()[0].getName());
	}

}