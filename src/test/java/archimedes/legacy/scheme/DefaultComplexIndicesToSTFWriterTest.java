/*
 * DefaultComplexIndicesToSTFWriterTest.java
 *
 * 19.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Vector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.Utils;
import archimedes.legacy.model.IndexMetaData;
import corent.files.StructuredTextFile;
import gengen.metadata.AttributeMetaData;
import gengen.metadata.ClassMetaData;

/**
 * Tests zur Klasse <CODE>DefaultComplexIndicesToSTFWriter</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
 */

public class DefaultComplexIndicesToSTFWriterTest {

	private static final String INDEX_NAME_0 = "Index0";
	private static final String INDEX_NAME_1 = "Index1";
	private static final String COLUMN_NAME_X = "ColumnX";
	private static final String COLUMN_NAME_Y = "ColumnY";
	private static final String COLUMN_NAME_Z = "ColumnZ";
	private static final String TABLE_NAME_X = "TableX";
	private static final String TABLE_NAME_Y = "TableY";

	private AttributeMetaData columnX = null;
	private AttributeMetaData columnY = null;
	private AttributeMetaData columnZ = null;
	private ClassMetaData tableX = null;
	private ClassMetaData tableY = null;
	private DefaultComplexIndicesToSTFWriter unitUnderTest = null;
	private IndexMetaData[] indices = null;
	private StructuredTextFile stf = null;

	/**
	 * @changed OLI 21.11.2011 - Hinzugef&uuml;gt.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.columnX = Utils.createAttributeMetaDataMock(COLUMN_NAME_X);
		this.columnY = Utils.createAttributeMetaDataMock(COLUMN_NAME_Y);
		this.columnZ = Utils.createAttributeMetaDataMock(COLUMN_NAME_Z);
		this.stf = new StructuredTextFile("tmp");
		this.tableX = Utils.createClassMetaDataMock(TABLE_NAME_X, this.columnX, this.columnY);
		this.tableY = Utils.createClassMetaDataMock(TABLE_NAME_Y, this.columnZ);
		this.indices = this.createIndexList();
		this.unitUnderTest = new DefaultComplexIndicesToSTFWriter();
	}

	private IndexMetaData[] createIndexList() {
		List<IndexMetaData> l = new Vector<IndexMetaData>();
		l.add(Utils.createIndexMetaData(INDEX_NAME_0, this.tableX, this.columnY));
		l.add(Utils.createIndexMetaData(INDEX_NAME_1, this.tableY, this.columnZ));
		return l.toArray(new IndexMetaData[0]);
	}

	/**
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testWritePassingANullPointerAsIndexList() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.write(this.stf, null);
		});
	}

	/**
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testWritePassingANullPointerAsStructuredTextFile() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.write(null, this.indices);
		});
	}

	/**
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testWriteToAnEmptyFile() throws Exception {
		assertEquals(0, this.stf.getPathes().size());
		this.unitUnderTest.write(this.stf, this.indices);
		assertEquals(17, this.stf.getPathes().size());
	}

	/**
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testWriteToAFilledFile() throws Exception {
		this.stf.writeStr(new String[] { "Diagramm", "Others", "Name" }, ":o)");
		this.stf.writeLong(new String[] { "Diagramm", "ComplexIndices", "IndexCount" }, 2);
		this.stf.writeStr(params(0, "Name"), "Index0");
		this.stf.writeStr(params(0, "Table", "Name"), "TableA");
		this.stf.writeLong(params(0, "ColumnCount"), 2);
		this.stf.writeStr(params(0, "Column0", "Name"), "ColumnA");
		this.stf.writeStr(params(0, "Column1", "Name"), "ColumnB");
		this.stf.writeStr(params(1, "Name"), "Index1");
		this.stf.writeStr(params(1, "Table", "Name"), "TableB");
		this.stf.writeLong(params(1, "ColumnCount"), 1);
		this.stf.writeStr(params(1, "Column0", "Name"), "ColumnC");
		this.stf.writeStr(params(2, "Name"), "Index2");
		this.stf.writeStr(params(2, "Table", "Name"), "TableC");
		this.stf.writeLong(params(2, "ColumnCount"), 3);
		this.stf.writeStr(params(2, "Column0", "Name"), "ColumnD");
		this.stf.writeStr(params(2, "Column1", "Name"), "ColumnE");
		this.stf.writeStr(params(2, "Column2", "Name"), "ColumnF");
		assertEquals(32, this.stf.getPathes().size());
		this.unitUnderTest.write(this.stf, this.indices);
		assertEquals(19, this.stf.getPathes().size());
		assertEquals(":o)", this.stf.readStr(new String[] { "Diagramm", "Others", "Name" }, ""));
	}

	private static String[] params(int pos, String... s) {
		String[] result = new String[s.length + 3];
		result[0] = "Diagramm";
		result[1] = "ComplexIndices";
		result[2] = "Index" + pos;
		for (int i = 0, leni = s.length; i < leni; i++) {
			result[i + 3] = s[i];
		}
		return result;
	}

}