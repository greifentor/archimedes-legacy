/*
 * DomainStringBuilderTest.java
 *
 * 14.06.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.model.DomainShowMode;
import archimedes.model.DomainModel;

/**
 * Tests zur Klasse <TT>DomainStringBuilder</TT>.
 * 
 * @author ollie
 * 
 * @changed OLI 14.06.2011 - Hinzugef&uuml;gt.
 */

public class DomainStringBuilderTest {

	private DomainModel domain = null;
	private DomainStringBuilder unitUnderTest = null;
	private Tabellenspalte column = null;

	/**
	 * @changed OLI 14.06.2011 - Hinzugef&uuml;gt.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.domain = new Domain("IntDomain", Types.INTEGER, 0, 0);
		this.column = new Tabellenspalte("Id", this.domain);
		this.unitUnderTest = new DomainStringBuilder(this.column, DomainShowMode.ALL);
	}

	/**
	 * @changed OLI 14.06.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testBuildDomainDefaultAndAttributeDefaultUnset() {
		assertEquals("IntDomain (int)", this.unitUnderTest.build());
	}

	/**
	 * @changed OLI 14.06.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testBuildDomainDefaultAndAttributeDefaultSet() {
		this.domain.setInitialValue("4711");
		this.column.setIndividualDefaultValue("815");
		assertEquals("IntDomain (int) - 815", this.unitUnderTest.build(), "individual default value should be set.");
	}

	/**
	 * @changed OLI 14.06.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testBuildNoDomainForColumnSet() {
		this.column.setDomain(null);
		assertEquals("<null>", this.unitUnderTest.build(), "there is no domain set.");
	}

	/**
	 * @changed OLI 14.06.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testBuildOnlyAttributeDefaultSet() {
		this.column.setIndividualDefaultValue("815");
		assertEquals("IntDomain (int) - 815", this.unitUnderTest.build(), "individual default value should be set.");
	}

	/**
	 * @changed OLI 14.06.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testBuildOnlyDomainDefaultSet() {
		this.domain.setInitialValue("4711");
		assertEquals("IntDomain (int) - 4711", this.unitUnderTest.build(), "domain default value should be set.");
	}

	/**
	 * @changed OLI 14.06.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testConstructorPassingANullPointerAsColumn() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new DomainStringBuilder(null, null);
		});
	}

	@Test
	public void testBuildOnlyDomainWithDomainShowModeDOMAIN_NAME_ONLY() {
		unitUnderTest = new DomainStringBuilder(this.column, DomainShowMode.DOMAIN_NAME_ONLY);
		assertEquals("IntDomain", this.unitUnderTest.build(), "domain default value should be set.");
	}

	@Test
	public void testBuildOnlyDomainWithDomainShowModeSQL_TYPE_ONLY() {
		unitUnderTest = new DomainStringBuilder(this.column, DomainShowMode.SQL_TYPE_ONLY);
		assertEquals("INT", this.unitUnderTest.build(), "domain default value should be set.");
	}

}