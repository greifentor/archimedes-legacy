/*
 * ComplexIndexWriterTest.java
 *
 * 16.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.model.ComplexIndicesToSTFWriter;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.IndexListCleaner;
import archimedes.legacy.model.IndexMetaData;
import corent.files.StructuredTextFile;

/**
 * Tests zur Klasse <CODE>ComplexIndexWriter</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 16.12.2011 - Hinzugef&uuml;gt.
 */

public class ComplexIndexWriterTest {

	private ComplexIndicesToSTFWriter writer = null;
	private ComplexIndexWriter unitUnderTest = null;
	private DiagrammModel diagram = null;
	private IndexListCleaner cleaner = null;
	private IndexMetaData[] indices = null;
	private StructuredTextFile stf = null;

	/**
	 * @changed OLI 21.11.2011 - Hinzugef&uuml;gt.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.indices = new IndexMetaData[0];
		this.diagram = this.createDiagramMock();
		this.cleaner = this.createIndexListCleaner();
		this.stf = new StructuredTextFile("tmp");
		this.writer = this.createComplexIndicesToSTFWriterMock();
		this.unitUnderTest = new ComplexIndexWriter(this.cleaner, this.writer);
	}

	private DiagrammModel createDiagramMock() {
		DiagrammModel d = EasyMock.createMock(DiagrammModel.class);
		EasyMock.expect(d.getComplexIndices()).andReturn(this.indices);
		EasyMock.replay(d);
		return d;
	}

	private IndexListCleaner createIndexListCleaner() {
		IndexListCleaner ilc = EasyMock.createMock(IndexListCleaner.class);
		ilc.clean(this.diagram);
		EasyMock.replay(ilc);
		return ilc;
	}

	private ComplexIndicesToSTFWriter createComplexIndicesToSTFWriterMock() {
		ComplexIndicesToSTFWriter ciw = EasyMock.createMock(ComplexIndicesToSTFWriter.class);
		ciw.write(this.stf, this.indices);
		EasyMock.replay(ciw);
		return ciw;
	}

	/**
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testConstructorPassingANullPointerAsComplexIndicesToSTFWriter() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new ComplexIndexWriter(this.cleaner, null);
		});
	}

	/**
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testConstructorPassingANullPointerAsIndexListCleaner() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new ComplexIndexWriter(null, this.writer);
		});
	}

	/**
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testWritePassingANullPointerAsStrcuturedTextFile() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.store(null, this.diagram);
		});
	}

	/**
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testWritePassingANullPointerAsDiagrammModel() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.store(this.stf, null);
		});
	}

	/**
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testWriteAfterARegularRunCleanerAndWriterShouldCalled() throws Exception {
		this.unitUnderTest.store(this.stf, this.diagram);
		EasyMock.verify(this.cleaner);
		EasyMock.verify(this.diagram);
		EasyMock.verify(this.writer);
	}

}