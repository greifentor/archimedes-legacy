/*
 * SQLGeneratorUtilTest.java
 *
 * 26.07.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.model.ColumnModel;
import archimedes.model.DomainModel;
import corent.db.DBExecMode;

/**
 * Tests of the class <CODE>SQLGeneratorUtil</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 26.07.2013 - Added.
 */

public class SQLGeneratorUtilTest {

	private ColumnModel column = null;
	private DomainModel domain = null;

	/**
	 * @changed OLI 26.07.2013 - Added.
	 */
	@BeforeEach
	public void setUp() {
		this.column = mock(ColumnModel.class);
		this.domain = mock(DomainModel.class);
		when(this.column.getDomain()).thenReturn(this.domain);
	}

	/**
	 * @changed OLI 26.07.2013 - Added.
	 */
	@Test
	public void testGetDefaultValueReturnANullPointerNeitherDomainNorColumnInitValueSet() {
		when(this.column.getDefaultValue()).thenReturn("NULL");
		assertEquals("NULL", SQLGeneratorUtil.getDefaultValue(this.column, DBExecMode.MYSQL));
	}

	/**
	 * @changed OLI 26.07.2013 - Added.
	 */
	@Test
	public void testGetDefaultValueReturnCorrectValueForPostGreAndDomainCalledBoolean0() {
		when(this.column.getDefaultValue()).thenReturn("0");
		when(this.domain.getName()).thenReturn("Boolean");
		assertEquals("false", SQLGeneratorUtil.getDefaultValue(this.column, DBExecMode.POSTGRESQL));
	}

	/**
	 * @changed OLI 26.07.2013 - Added.
	 */
	@Test
	public void testGetDefaultValueReturnCorrectValueForPostGreAndDomainCalledBoolean1() {
		when(this.column.getDefaultValue()).thenReturn("1");
		when(this.domain.getName()).thenReturn("Boolean");
		assertEquals("true", SQLGeneratorUtil.getDefaultValue(this.column, DBExecMode.POSTGRESQL));
	}

	/**
	 * @changed OLI 26.07.2013 - Added.
	 */
	@Test
	public void testGetDefaultValueThrowsAnExceptionPassingANullPointerAsColumn() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SQLGeneratorUtil.getDefaultValue(null, DBExecMode.MYSQL);
		});
	}

	/**
	 * @changed OLI 26.07.2013 - Added.
	 */
	@Test
	public void testGetDefaultValueThrowsAnExceptionPassingANullPointerAsDBMode() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SQLGeneratorUtil.getDefaultValue(this.column, null);
		});
	}

	/**
	 * @changed OLI 26.07.2013 - Added.
	 */
	@Test
	public void testGetTypeStringReturnsBooleanIfNoDomainsIsPostGreAndDomainCalledBoolean() {
		when(this.domain.getName()).thenReturn("boolean");
		assertEquals("BOOLEAN", SQLGeneratorUtil.getTypeString(false, this.domain, DBExecMode.POSTGRESQL));
	}

	/**
	 * @changed OLI 26.07.2013 - Added.
	 */
	@Test
	public void testGetTypeStringReturnsTheDomainNameIfModelHasDomains() {
		when(this.domain.getName()).thenReturn("Domain");
		assertEquals("Domain", SQLGeneratorUtil.getTypeString(true, this.domain, DBExecMode.MYSQL));
	}

	/**
	 * @changed OLI 26.07.2013 - Added.
	 */
	@Test
	public void testGetTypeStringThrowsAnExceptionPassingANullPointerAsDBMode() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SQLGeneratorUtil.getTypeString(false, this.domain, null);
		});
	}

	/**
	 * @changed OLI 26.07.2013 - Added.
	 */
	@Test
	public void testGetTypeStringThrowsAnExceptionPassingANullPointerAsDomain() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			SQLGeneratorUtil.getTypeString(false, null, DBExecMode.MYSQL);
		});
	}

}