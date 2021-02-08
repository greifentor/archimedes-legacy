/*
 * SequenceMetaDataTest.java
 *
 * 23.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests of the class <CODE>SequenceMetaData</CODE>.
 *
 * @author ollie
 *
 * @changed OLI 23.04.2013 - Added.
 */

public class SequenceMetaDataTest {

	private static final String NAME = "name";

	private SequenceMetaData unitUnderTest = null;

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@BeforeEach
	public void setUp() {
		this.unitUnderTest = new SequenceMetaData(NAME);
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Test
	public void testConstructorSetsTheCorrectNameForTheSequence() {
		assertEquals(NAME, this.unitUnderTest.getName());
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Test
	public void testConstructorThrowsAnExceptionPassingAnEmptyStringAsName() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new SequenceMetaData("");
		});
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Test
	public void testConstructorThrowsAnExceptionPassingANullPointerAsName() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new SequenceMetaData(null);
		});
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Test
	public void testEqualsReturnsFalsePassingADifferentNames() {
		assertFalse(this.unitUnderTest.equals(new SequenceMetaData(NAME + 1)));
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Test
	public void testEqualsReturnsFalsePassingAnotherClassType() {
		assertFalse(this.unitUnderTest.equals(":op")); // NOSONAR OLI: Should so ...
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Test
	public void testEqualsReturnsFalsePassingANullPointer() {
		assertFalse(this.unitUnderTest.equals(null));
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Test
	public void testEqualsReturnsTruePassingAnEqualSequenceMetaData() {
		assertTrue(this.unitUnderTest.equals(new SequenceMetaData(NAME)));
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Test
	public void testEqualsReturnsTruePassingTheSameSequenceMetaData() {
		assertTrue(this.unitUnderTest.equals(this.unitUnderTest));
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Test
	public void testHashCodeReturnsEqualValuesPassingAnEqualSequenceMetaData() {
		assertEquals(this.unitUnderTest.hashCode(), new SequenceMetaData(NAME).hashCode());
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Test
	public void testHashCodeReturnsEqualValuesPassingSameSequenceMetaData() {
		assertEquals(this.unitUnderTest.hashCode(), this.unitUnderTest.hashCode());
	}

	/**
	 * @changed OLI 23.04.2013 - Added.
	 */
	@Test
	public void testToStringReturnsTheCorrectString() throws Exception {
		assertEquals("Name=" + NAME, this.unitUnderTest.toString());
	}

}