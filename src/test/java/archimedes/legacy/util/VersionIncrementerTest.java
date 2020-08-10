/*
 * VersionIncrementerTest.java
 *
 * 28.04.2014
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.model.DataModel;
import corent.dates.PDate;

/**
 * Tests of the class <CODE>VersionIncrementer</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 28.04.2014 - Added.
 */

public class VersionIncrementerTest {

	private DataModel model = null;
	private VersionIncrementer unitUnderTest = null;

	/**
	 * @changed OLI 28.04.2014 - Added.
	 */
	@BeforeEach
	public void setUp() {
		this.model = mock(DataModel.class);
		this.unitUnderTest = new VersionIncrementer();
	}

	/**
	 * @changed OLI 28.04.2014 - Added.
	 */
	@Test
	public void testElementCountOfState() {
		assertEquals(2, VersionIncrementer.State.values().length);
	}

	/**
	 * @changed OLI 28.04.2014 - Added.
	 */
	@Test
	public void testIncrementReturnsStateSCRIPTS_CLEANED_ONLYIfModelIsUpdatedCorrectly() {
		when(this.model.getVersion()).thenReturn("1.1");
		assertEquals(VersionIncrementer.State.SCRIPTS_CLEANED_ONLY, this.unitUnderTest.increment(this.model));
		verify(this.model, times(0)).setVersion(any(String.class));
		verify(this.model, times(1)).setAdditionalSQLCodePostChangingCode("");
		verify(this.model, times(1)).setAdditionalSQLCodePostReducingCode("");
		verify(this.model, times(1)).setAdditionalSQLCodePreChangingCode("");
		verify(this.model, times(1)).setAdditionalSQLCodePreExtendingCode("");
		verify(this.model, times(1)).setVersionDate(new PDate());
	}

	/**
	 * @changed OLI 28.04.2014 - Added.
	 */
	@Test
	public void testIncrementReturnsStateSCRIPTS_CLEANED_ONLYIfVersionIsNull() {
		when(this.model.getVersion()).thenReturn(null);
		assertEquals(VersionIncrementer.State.SCRIPTS_CLEANED_ONLY, this.unitUnderTest.increment(this.model));
		verify(this.model, times(0)).setVersion(any(String.class));
		verify(this.model, times(1)).setAdditionalSQLCodePostChangingCode("");
		verify(this.model, times(1)).setAdditionalSQLCodePostReducingCode("");
		verify(this.model, times(1)).setAdditionalSQLCodePreChangingCode("");
		verify(this.model, times(1)).setAdditionalSQLCodePreExtendingCode("");
		verify(this.model, times(1)).setVersionDate(new PDate());
	}

	/**
	 * @changed OLI 28.04.2014 - Added.
	 */
	@Test
	public void testIncrementReturnsStateSUCCESSIfModelIsUpdatedCorrectly() {
		when(this.model.getVersion()).thenReturn("1");
		assertEquals(VersionIncrementer.State.SUCCESS, this.unitUnderTest.increment(this.model));
		verify(this.model, times(1)).setVersion("2");
		verify(this.model, times(1)).setAdditionalSQLCodePostChangingCode("");
		verify(this.model, times(1)).setAdditionalSQLCodePostReducingCode("");
		verify(this.model, times(1)).setAdditionalSQLCodePreChangingCode("");
		verify(this.model, times(1)).setAdditionalSQLCodePreExtendingCode("");
		verify(this.model, times(1)).setVersionDate(new PDate());
	}

	/**
	 * @changed OLI 28.04.2014 - Added.
	 */
	@Test
	public void testIncrementThrowsAnExceptionPassingANullPointerAsModel() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			this.unitUnderTest.increment(null);
		});
	}

	/**
	 * @changed OLI 28.04.2014 - Added.
	 */
	@Test
	public void testValueOfState() {
		assertEquals(VersionIncrementer.State.SCRIPTS_CLEANED_ONLY,
				VersionIncrementer.State.valueOf("SCRIPTS_CLEANED_ONLY"));
		assertEquals(VersionIncrementer.State.SUCCESS, VersionIncrementer.State.valueOf("SUCCESS"));
	}

}