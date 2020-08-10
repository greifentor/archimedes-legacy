/*
 * TabelleTest.java
 *
 * 25.05.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.legacy.scheme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.sql.Types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.acf.util.ParameterUtil;
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DomainModel;
import archimedes.legacy.model.OptionModel;

/**
 * Tests of the class <CODE>Tabelle</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 25.05.2016 - Added.
 */

public class TabelleTest {

	private static final String OPTION = "option";

	private Diagramm diagram = null;
	private Tabelle unitUnderTest = null;
	private View view = null;

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@BeforeEach
	public void setUp() {
		this.diagram = new Diagramm();
		this.view = new View("view", ":o", true);
		this.unitUnderTest = new Tabelle(this.view, 0, 0, this.diagram);
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testAddOptionAddsTwoOptionsCorrectly() {
		this.unitUnderTest.addOption(new Option("1"));
		this.unitUnderTest.addOption(new Option("2", "3"));
		assertEquals("1|2:3", this.getParameters());
	}

	private String getParameters() {
		String s = "";
		for (OptionModel o : this.unitUnderTest.getOptions()) {
			if (s.length() > 0) {
				s += "|";
			}
			s += o.getName()
					+ ((o.getParameter() != null) && !o.getParameter().isEmpty() ? ":" + o.getParameter() : "");
		}
		return s;
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testAddOptionDoesNotAddTheSameOptionAgainButOneWithTheSameName() {
		this.unitUnderTest.addOption(new Option("1", "2"));
		this.unitUnderTest.addOption(new Option("1", "2"));
		this.unitUnderTest.addOption(new Option("1"));
		this.unitUnderTest.addOption(new Option("1"));
		this.unitUnderTest.addOption(new Option("1", "3"));
		assertEquals("1:2|1|1:3", this.getParameters());
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testAddOptionThrowsExceptionPassingANullPointer() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.addOption(null);
		});
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testGetColumnsWithOptionReturnAnArrayWithTheFieldOwningTheOption() {
		DomainModel dm = new Domain("domain", Types.BIGINT, 0, 0);
		ColumnModel c1 = new Tabellenspalte("1", dm);
		ColumnModel c2 = new Tabellenspalte("2", dm);
		ColumnModel c3 = new Tabellenspalte("3", dm);
		c1.addOption(new Option(OPTION + 1));
		c2.addOption(new Option(OPTION));
		c3.addOption(new Option(OPTION));
		this.unitUnderTest.addColumn(c1);
		this.unitUnderTest.addColumn(c2);
		this.unitUnderTest.addColumn(c3);
		this.unitUnderTest.addColumn(new Tabellenspalte("3", dm));
		ColumnModel[] r = this.unitUnderTest.getColumnsWithOption(OPTION);
		assertEquals(2, r.length);
		assertSame(c2, r[0]);
		assertSame(c3, r[1]);
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testGetColumnsWithOptionReturnAnEmptyArrayIfNoFieldHasTheOptionSet() {
		DomainModel dm = new Domain("domain", Types.BIGINT, 0, 0);
		this.unitUnderTest.addColumn(new Tabellenspalte("1", dm));
		this.unitUnderTest.addColumn(new Tabellenspalte("2", dm));
		this.unitUnderTest.addColumn(new Tabellenspalte("3", dm));
		assertEquals(0, this.unitUnderTest.getColumnsWithOption(OPTION).length);
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testGetColumnsWithOptionReturnAnEmptyArrayPassingANullPointer() {

		assertEquals(0, this.unitUnderTest.getColumnsWithOption(null).length);
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testGetOptionAtReturnTheCorrectValueForIndex1() {
		this.setParameters("1|2:3|4:5");
		assertEquals("2", this.unitUnderTest.getOptionAt(1).getName());
		assertEquals("3", this.unitUnderTest.getOptionAt(1).getParameter());
	}

	private void setParameters(String s) {
		ParameterUtil pu = new ParameterUtil();
		for (String p : pu.getParameters(s)) {
			if (p.contains(":")) {
				String v = p.substring(p.indexOf(":") + 1).trim();
				p = p.substring(0, p.indexOf(":")).trim();
				this.unitUnderTest.addOption(new Option(p, v));
			} else {
				this.unitUnderTest.addOption(new Option(p.trim()));
			}
		}
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testGetOptionByNameReturnsNullPassingAnUnknownName() {
		this.setParameters("1|2:3|4:5");
		assertNull(this.unitUnderTest.getOptionByName("5"));
		assertNull(this.unitUnderTest.getOptionByName("6"));
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testGetOptionByNameReturnTheCorrectValue() {
		this.setParameters("1|2:3|4:5");
		assertEquals("2", this.unitUnderTest.getOptionByName("2").getName());
		assertEquals("3", this.unitUnderTest.getOptionByName("2").getParameter());
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testGetOptionsByNameReturnsNullPassingAnUnknownName() {
		this.setParameters("1|2:3|4:5");
		assertEquals(0, this.unitUnderTest.getOptionsByName("5").length);
		assertEquals(0, this.unitUnderTest.getOptionsByName("6").length);
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testGetOptionsByNameReturnTheCorrectValue() {
		this.setParameters("1|2|2:3|5:6|2:4");
		OptionModel[] r = this.unitUnderTest.getOptionsByName("2");
		assertEquals(3, r.length);
		assertEquals("2", r[0].getName());
		assertNull(r[0].getParameter());
		assertEquals("2", r[1].getName());
		assertEquals("3", r[1].getParameter());
		assertEquals("2", r[2].getName());
		assertEquals("4", r[2].getParameter());
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testGetOptionCountReturnTheCorrectValue() {
		this.setParameters("1|2:3|4:5");
		assertEquals(3, this.unitUnderTest.getOptionCount());
	}

	/**
	 * @changed OLI 24.05.2016 - Added.
	 */
	@Test
	public void testGetOptionsReturnsAnEmptyArrayIfNoOptionsAreSet() {
		this.setParameters("");
		assertEquals(0, this.unitUnderTest.getOptions().length);
	}

	/**
	 * @changed OLI 24.05.2016 - Added.
	 */
	@Test
	public void testGetOptionsReturnsTheCorrectArraysForSetParameters() {
		this.setParameters("INHERITS:Doof|UNCHANGEABLE|INCLUDE_LIST:Bloed");
		assertEquals(3, this.unitUnderTest.getOptions().length);
		assertEquals("INCLUDE_LIST", this.unitUnderTest.getOptions()[0].getName());
		assertEquals("Bloed", this.unitUnderTest.getOptions()[0].getParameter());
		assertEquals("INHERITS", this.unitUnderTest.getOptions()[1].getName());
		assertEquals("Doof", this.unitUnderTest.getOptions()[1].getParameter());
		assertEquals("UNCHANGEABLE", this.unitUnderTest.getOptions()[2].getName());
		assertNull(this.unitUnderTest.getOptions()[2].getParameter());
	}

	/**
	 * @changed OLI 24.05.2016 - Added.
	 */
	@Test
	public void testGetOptionsReturnsTheCorrectArraysForSetParametersDirtyString() {
		this.setParameters("   INHERITS : Doof | UNCHANGEABLE    | INCLUDE_LIST" + "  : Bloed   ");
		assertEquals(3, this.unitUnderTest.getOptions().length);
		assertEquals("INCLUDE_LIST", this.unitUnderTest.getOptions()[0].getName());
		assertEquals("Bloed", this.unitUnderTest.getOptions()[0].getParameter());
		assertEquals("INHERITS", this.unitUnderTest.getOptions()[1].getName());
		assertEquals("Doof", this.unitUnderTest.getOptions()[1].getParameter());
		assertEquals("UNCHANGEABLE", this.unitUnderTest.getOptions()[2].getName());
		assertNull(this.unitUnderTest.getOptions()[2].getParameter());
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testRemoveOptionDeletesTheCorrectOption() {
		this.setParameters("1|2:3|4:5");
		this.unitUnderTest.removeOption(new Option("2", "3"));
		assertEquals("1|4:5", this.getParameters());
		this.unitUnderTest.removeOption(new Option("4", "5"));
		assertEquals("1", this.getParameters());
		this.unitUnderTest.removeOption(new Option("1"));
		assertEquals("", this.getParameters());
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testRemoveOptionDoesNothingForAnUnknownOption() {
		this.setParameters("1|2:3|4:5");
		this.unitUnderTest.removeOption(new Option("3", "2"));
		assertEquals("1|2:3|4:5", this.getParameters());
		this.unitUnderTest.removeOption(new Option("42"));
		assertEquals("1|2:3|4:5", this.getParameters());
	}

}