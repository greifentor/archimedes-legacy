/*
 * SequenceScriptBuilderTest.java
 *
 * 23.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Vector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.metadata.SequenceMetaData;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.scheme.Sequence;
import archimedes.legacy.script.sql.SQLScript;
import archimedes.legacy.sql.factories.PostgreSQLScriptFactory;
import archimedes.model.SequenceModel;

/**
 * Tests of the class <CODE>SequenceScriptBuilder</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 23.04.2013 - Added.
 */

public class SequenceScriptBuilderTest {

	private static final long INCREMENT_2 = 100;
	private static final long INCREMENT_3 = 200;
	private static final String QUOTE = "\"";
	private static final String SEQ_NAME_1 = "sequence1";
	private static final String SEQ_NAME_2 = "sequence2";
	private static final String SEQ_NAME_3 = "sequence3";
	private static final long START_VALUE_2 = 1;
	private static final long START_VALUE_3 = 2;

	private DiagrammModel model = null;
	private SequenceMetaData[] sequences = null;
	private SequenceScriptBuilder unitUnderTest = null;
	private SQLScriptFactory factory = null;

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@BeforeEach
	public void setUp() {
		this.sequences = new SequenceMetaData[] { new SequenceMetaData(SEQ_NAME_1), new SequenceMetaData(SEQ_NAME_2) };
		this.model = mock(DiagrammModel.class);
		this.factory = new PostgreSQLScriptFactory(QUOTE, this.model);
		Vector<SequenceModel> v = new Vector();
		v.add(new Sequence(SEQ_NAME_2, START_VALUE_2, INCREMENT_2));
		v.add(new Sequence(SEQ_NAME_3, START_VALUE_3, INCREMENT_3));
		when(this.model.getSequences()).thenReturn(v.toArray(new SequenceModel[0]));
		this.unitUnderTest = new SequenceScriptBuilder(this.model, this.factory, this.sequences);
	}

	/**
	 * @changed OLI 24.04.2013 - Added.
	 */
	@Test
	public void testBuildReturnsFalseIfNoChangesDetected() {
		SQLScript sql = new SQLScript();
		Vector html = new Vector();
		this.sequences = new SequenceMetaData[] { new SequenceMetaData(SEQ_NAME_2), new SequenceMetaData(SEQ_NAME_3) };
		this.unitUnderTest = new SequenceScriptBuilder(this.model, this.factory, this.sequences);
		assertFalse(this.unitUnderTest.build(sql, html));
		assertEquals(sql, new SQLScript());
		assertEquals(html, new Vector());
	}

	/**
	 * @changed OLI 24.04.2013 - Added.
	 */
	@Test
	public void testBuildReturnsTrueAnFillsTheChangeReportCorrectly() {
		SQLScript sql = new SQLScript();
		Vector html = new Vector();
		Vector htmlexp = new Vector();
		htmlexp.add("        <h2>Sequenzen</h2>");
		htmlexp.add("        <hr SIZE=1 WIDTH=\"100%\">");
		htmlexp.add("        Sequenz <B>" + SEQ_NAME_3 + "</B> mit Increment " + INCREMENT_3 + " und Startwert "
				+ START_VALUE_3 + " hinzugef&uuml;gt.<BR>");
		htmlexp.add("        <BR>");
		htmlexp.add("        Sequenz <B>" + SEQ_NAME_1 + "</B> gel&ouml;scht.<BR>");
		assertTrue(this.unitUnderTest.build(sql, html));
		assertEquals(htmlexp.toString(), html.toString());
	}

	/**
	 * @changed OLI 24.04.2013 - Added.
	 */
	@Test
	public void testBuildReturnsTrueAnFillsTheChangeScriptCorrectly() {
		SQLScript sql = new SQLScript();
		Vector sqlexp = new Vector();
		Vector html = new Vector();
		sqlexp.add("CREATE SEQUENCE " + QUOTE + SEQ_NAME_3 + QUOTE + " INCREMENT BY " + INCREMENT_3 + " START WITH "
				+ START_VALUE_3 + ";");
		sqlexp.add("");
		sqlexp.add("DROP SEQUENCE IF EXISTS " + QUOTE + SEQ_NAME_1 + QUOTE + ";");
		assertTrue(this.unitUnderTest.build(sql, html));
		assertEquals(sqlexp.toString(), sql.toString());
	}

	/**
	 * @changed OLI 24.04.2013 - Added.
	 */
	@Test
	public void testBuildReturnsTrueAnFillsTheChangeScriptCorrectlyIfThereIsAnAddOnly() {
		SQLScript sql = new SQLScript();
		Vector sqlexp = new Vector();
		Vector html = new Vector();
		this.sequences = new SequenceMetaData[] { new SequenceMetaData(SEQ_NAME_2) };
		sqlexp.add("CREATE SEQUENCE " + QUOTE + SEQ_NAME_3 + QUOTE + " INCREMENT BY " + INCREMENT_3 + " START WITH "
				+ START_VALUE_3 + ";");
		sqlexp.add("");
		this.unitUnderTest = new SequenceScriptBuilder(this.model, this.factory, this.sequences);
		assertTrue(this.unitUnderTest.build(sql, html));
		assertEquals(sqlexp.toString(), sql.toString());
	}

	/**
	 * @changed OLI 24.04.2013 - Added.
	 */
	@Test
	public void testBuildReturnsTrueAnFillsTheChangeScriptCorrectlyIfThereIsADropOnly() {
		SQLScript sql = new SQLScript();
		Vector sqlexp = new Vector();
		Vector html = new Vector();
		this.sequences = new SequenceMetaData[] { new SequenceMetaData(SEQ_NAME_1), new SequenceMetaData(SEQ_NAME_2),
				new SequenceMetaData(SEQ_NAME_3) };
		sqlexp.add("DROP SEQUENCE IF EXISTS " + QUOTE + SEQ_NAME_1 + QUOTE + ";");
		this.unitUnderTest = new SequenceScriptBuilder(this.model, this.factory, this.sequences);
		assertTrue(this.unitUnderTest.build(sql, html));
		assertEquals(sqlexp.toString(), sql.toString());
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Test
	public void testBuildThrowsAnExceptionPassingANullPointerAsHTMLDescription() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.build(new SQLScript(), null);
		});
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Test
	public void testBuildThrowsAnExceptionPassingANullPointerAsSQLScript() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.build(null, new Vector<String>());
		});
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Test
	public void testConstructorThrowsAnExceptionPassingANullPointerAsModel() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new SequenceScriptBuilder(null, this.factory, this.sequences);
		});
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Test
	public void testConstructorThrowsAnExceptionPassingANullPointerAsSequenceArray() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new SequenceScriptBuilder(this.model, this.factory, null);
		});
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Test
	public void testConstructorThrowsAnExceptionPassingANullPointerAsSQLScripFactory() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new SequenceScriptBuilder(this.model, null, this.sequences);
		});
	}

}