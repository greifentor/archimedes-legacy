/*
 * TabellenspalteTest.java
 *
 * 14.06.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.model.DomainModel;
import archimedes.legacy.model.OptionModel;
import archimedes.legacy.model.ReferenceWeight;
import corent.base.Direction;

/**
 * Tests of the class <CODE>Tabellenspalte</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 14.06.2011 - Added.
 * @changed OLI 25.04.2011 - Changed to English.
 */

public class TabellenspalteTest {

	private static final String ID = "Id";
	private static final String VALUE = ":o)";

	private Diagramm diagram = null;
	private DomainModel domain = null;
	private Panel panel = null;
	private Sequence sequence = null;
	private Tabelle table = null;
	private Tabellenspalte unitUnderTest = null;
	private View view = null;

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@BeforeEach
	public void setUp() {
		this.diagram = new Diagramm();
		this.domain = new Domain("int", Types.INTEGER, 0, 0);
		this.view = new View("view", ":o", true);
		this.panel = new Panel();
		this.panel.setTabTitle("dataPanel");
		this.sequence = new Sequence("SEQ", 7, 42);
		this.table = new Tabelle(this.view, 0, 0, this.diagram);
		this.table.addPanel(this.panel);
		this.unitUnderTest = new Tabellenspalte(ID, this.domain);
		this.table.addColumn(this.unitUnderTest);
		this.diagram.addTabelle(this.table);
	}

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@AfterEach
	public void tearDown() {
		System.clearProperty("archimedes.scheme.Tabellenspalte.diagramm.writeable.prefix");
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testAddOptionAddsTwoOptionsCorrectly() {
		this.unitUnderTest.addOption(new Option("1"));
		this.unitUnderTest.addOption(new Option("2", "3"));
		assertEquals("1|2:3", this.unitUnderTest.getParameters());
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
		assertEquals("1:2|1|1:3", this.unitUnderTest.getParameters());
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
	 * @changed OLI 05.08.2016 - Added.
	 */
	@Test
	public void ConstructorToCopyAnObjectReturnsNotTheSameButAnEqualObject() {
		this.unitUnderTest.setCanBeReferenced(true);
		this.unitUnderTest.setComment(";op");
		this.unitUnderTest.setDeprecated(true);
		this.unitUnderTest.setEditorAlterInBatchView(true);
		this.unitUnderTest.setEditorDisabled(true);
		this.unitUnderTest.setEditorLabelText(":op");
		this.unitUnderTest.setEditorMaxLength(42);
		this.unitUnderTest.setEditorMember(true);
		this.unitUnderTest.setEditorMnemonic("&uuml;");
		this.unitUnderTest.setEditorPosition(4711);
		this.unitUnderTest.setEditorReferenceWeight(ReferenceWeight.MASSIVE);
		this.unitUnderTest.setEditorResourceId("bla.bla.bla");
		this.unitUnderTest.setEditorToolTipText("tool.tip.text");
		this.unitUnderTest.setEncrypted(true);
		this.unitUnderTest.setHideReference(true);
		this.unitUnderTest.setHistory("Urbem Romam a principio reges habuere ...");
		this.unitUnderTest.setIndex(true);
		this.unitUnderTest.setIndexSearchMember(true);
		this.unitUnderTest.setIndividualDefaultValue("der fault ;o)");
		this.unitUnderTest.setLastModificationField(true);
		this.unitUnderTest.setListItemField(true);
		this.unitUnderTest.setNotNull(true);
		this.unitUnderTest.setPanel(this.panel);
		this.unitUnderTest.setParameters("parameters :o)");
		this.unitUnderTest.setPrimaryKey(true);
		this.unitUnderTest.setRelation(new Relation(this.view, this.unitUnderTest, Direction.DOWN, 25,
				this.unitUnderTest, Direction.DOWN, 50));
		this.unitUnderTest.setRemovedStateField(true);
		this.unitUnderTest.setRequired(true);
		this.unitUnderTest.setSequenceForKeyGeneration(this.sequence);
		this.unitUnderTest.setSuppressForeignKeyConstraint(true);
		this.unitUnderTest.setSynchronized(true);
		this.unitUnderTest.setSynchronizeId(true);
		this.unitUnderTest.setTechnicalField(true);
		this.unitUnderTest.setTransient(true);
		this.unitUnderTest.setUnique(true);
		Tabellenspalte ts = new Tabellenspalte(this.unitUnderTest);
		assertNotSame(ts, this.unitUnderTest);
		assertEquals(this.unitUnderTest.canBeReferenced(), ts.canBeReferenced());
		assertEquals(this.unitUnderTest.getComment(), ts.getComment());
		assertEquals(this.unitUnderTest.isDeprecated(), ts.isDeprecated());
		assertEquals(this.unitUnderTest.getDomain(), ts.getDomain());
		assertEquals(this.unitUnderTest.isEditorAlterInBatchView(), ts.isEditorAlterInBatchView());
		assertEquals(this.unitUnderTest.isEditorDisabled(), ts.isEditorDisabled());
		assertEquals(this.unitUnderTest.getEditorLabelText(), ts.getEditorLabelText());
		assertEquals(this.unitUnderTest.getEditorMaxLength(), ts.getEditorMaxLength());
		assertEquals(this.unitUnderTest.isEditorMember(), ts.isEditorMember());
		assertEquals(this.unitUnderTest.getEditorMnemonic(), ts.getEditorMnemonic());
		assertEquals(this.unitUnderTest.getEditorPosition(), ts.getEditorPosition());
		assertEquals(this.unitUnderTest.getEditorReferenceWeight(), ts.getEditorReferenceWeight());
		assertEquals(this.unitUnderTest.getEditorResourceId(), ts.getEditorResourceId());
		assertEquals(this.unitUnderTest.getEditorToolTipText(), ts.getEditorToolTipText());
		assertEquals(this.unitUnderTest.isEncrypted(), ts.isEncrypted());
		assertEquals(this.unitUnderTest.isReferenceHidden(), ts.isReferenceHidden());
		assertEquals(this.unitUnderTest.getHistory(), ts.getHistory());
		assertEquals(this.unitUnderTest.hasIndex(), ts.hasIndex());
		assertEquals(this.unitUnderTest.isIndexSearchMember(), ts.isIndexSearchMember());
		assertEquals(this.unitUnderTest.getIndividualDefaultValue(), ts.getIndividualDefaultValue());
		assertEquals(this.unitUnderTest.isLastModificationField(), ts.isLastModificationField());
		assertEquals(this.unitUnderTest.isListItemField(), ts.isListItemField());
		assertEquals(this.unitUnderTest.isNotNull(), ts.isNotNull());
		assertEquals(this.unitUnderTest.getPanel(), ts.getPanel());
		assertEquals(this.unitUnderTest.getParameters(), ts.getParameters());
		assertEquals(this.unitUnderTest.isPrimaryKey(), ts.isPrimaryKey());
		assertEquals(this.unitUnderTest.getRelation(), ts.getRelation());
		assertEquals(this.unitUnderTest.isRemovedStateField(), ts.isRemovedStateField());
		assertEquals(this.unitUnderTest.isRequired(), ts.isRequired());
		assertEquals(this.unitUnderTest.getSequenceForKeyGeneration(), ts.getSequenceForKeyGeneration());
		assertEquals(this.unitUnderTest.isSuppressForeignKeyConstraint(), ts.isSuppressForeignKeyConstraint());
		assertEquals(this.unitUnderTest.isSynchronized(), ts.isSynchronized());
		assertEquals(this.unitUnderTest.isSynchronizeId(), ts.isSynchronizeId());
		assertEquals(this.unitUnderTest.getTable(), ts.getTable());
		assertEquals(this.unitUnderTest.isTechnicalField(), ts.isTechnicalField());
		assertEquals(this.unitUnderTest.isTransient(), ts.isTransient());
		assertEquals(this.unitUnderTest.isUnique(), ts.isUnique());
		assertTrue(EqualsBuilder.reflectionEquals(ts, this.unitUnderTest));
	}

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@Test
	public void testGetDefaultValueDomainDefaultAndAttributeDefaultSet() {
		this.domain.setInitialValue("4711");
		this.unitUnderTest.setIndividualDefaultValue("815");
		assertEquals("815", this.unitUnderTest.getDefaultValue(), "individual default value should be set.");
	}

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@Test
	public void testGetDefaultValueDomainDefaultAndAttributeDefaultUnset() {
		assertNull(this.unitUnderTest.getDefaultValue());
	}

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@Test
	public void testGetDefaultValueOnlyAttributeDefaultSet() {
		this.unitUnderTest.setIndividualDefaultValue("815");
		assertEquals("815", this.unitUnderTest.getDefaultValue(), "individual default value should be set.");
	}

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@Test
	public void testGetDefaultValueOnlyDomainDefaultSet() {
		this.domain.setInitialValue("4711");
		assertEquals("4711", this.unitUnderTest.getDefaultValue(), "domain default value should be set.");
	}

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@Test
	public void testGetIndividualDefaultValueByDefault() {
		assertEquals("", this.unitUnderTest.getIndividualDefaultValue());
	}

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@Test
	public void testGetIndividualDefaultValuePassingANullAsIndividualDefaultValueContainer() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.setIndividualDefaultValue(null);
		});
	}

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@Test
	public void testGetIndividualDefaultValueWithANullSetValue() {
		this.unitUnderTest.setIndividualDefaultValue("NULL");
		assertNotNull(this.unitUnderTest.getIndividualDefaultValue());
		assertEquals("NULL", this.unitUnderTest.getIndividualDefaultValue());
	}

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@Test
	public void testGetIndividualDefaultValueWithASetValue() {
		this.unitUnderTest.setIndividualDefaultValue(VALUE);
		assertNotNull(this.unitUnderTest.getIndividualDefaultValue());
		assertEquals(VALUE, this.unitUnderTest.getIndividualDefaultValue());
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testGetOptionAtReturnTheCorrectValueForIndex1() {
		this.unitUnderTest.setParameters("1|2:3|4:5");
		assertEquals("2", this.unitUnderTest.getOptionAt(1).getName());
		assertEquals("3", this.unitUnderTest.getOptionAt(1).getParameter());
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testGetOptionByNameReturnsNullPassingAnUnknownName() {
		this.unitUnderTest.setParameters("1|2:3|4:5");
		assertNull(this.unitUnderTest.getOptionByName("5"));
		assertNull(this.unitUnderTest.getOptionByName("6"));
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testGetOptionByNameReturnTheCorrectValue() {
		this.unitUnderTest.setParameters("1|2:3|4:5");
		assertEquals("2", this.unitUnderTest.getOptionByName("2").getName());
		assertEquals("3", this.unitUnderTest.getOptionByName("2").getParameter());
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testGetOptionsByNameReturnsNullPassingAnUnknownName() {
		this.unitUnderTest.setParameters("1|2:3|4:5");
		assertEquals(0, this.unitUnderTest.getOptionsByName("5").length);
		assertEquals(0, this.unitUnderTest.getOptionsByName("6").length);
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testGetOptionsByNameReturnTheCorrectValue() {
		this.unitUnderTest.setParameters("1|2|2:3|5:6|2:4");
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
		this.unitUnderTest.setParameters("1|2:3|4:5");
		assertEquals(3, this.unitUnderTest.getOptionCount());
	}

	/**
	 * @changed OLI 24.05.2016 - Added.
	 */
	@Test
	public void testGetOptionsReturnsAnEmptyArrayIfNoOptionsAreSet() {
		this.unitUnderTest.setParameters("");
		assertEquals(0, this.unitUnderTest.getOptions().length);
	}

	/**
	 * @changed OLI 24.05.2016 - Added.
	 */
	@Test
	public void testGetOptionsReturnsTheCorrectArraysForSetParameters() {
		this.unitUnderTest.setParameters("INHERITS:Doof|UNCHANGEABLE|INCLUDE_LIST:Bloed");
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
		this.unitUnderTest.setParameters("   INHERITS : Doof | UNCHANGEABLE    | INCLUDE_LIST" + "  : Bloed   ");
		assertEquals(3, this.unitUnderTest.getOptions().length);
		assertEquals("INCLUDE_LIST", this.unitUnderTest.getOptions()[0].getName());
		assertEquals("Bloed", this.unitUnderTest.getOptions()[0].getParameter());
		assertEquals("INHERITS", this.unitUnderTest.getOptions()[1].getName());
		assertEquals("Doof", this.unitUnderTest.getOptions()[1].getParameter());
		assertEquals("UNCHANGEABLE", this.unitUnderTest.getOptions()[2].getName());
		assertNull(this.unitUnderTest.getOptions()[2].getParameter());
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testGetTableReturnsAReferenceToTheTableOfTheColumn() {
		assertSame(this.table, this.unitUnderTest.getTable());
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testRemoveOptionDeletesTheCorrectOption() {
		this.unitUnderTest.setParameters("1|2:3|4:5");
		this.unitUnderTest.removeOption(new Option("2", "3"));
		assertEquals("1|4:5", this.unitUnderTest.getParameters());
		this.unitUnderTest.removeOption(new Option("4", "5"));
		assertEquals("1", this.unitUnderTest.getParameters());
		this.unitUnderTest.removeOption(new Option("1"));
		assertEquals("", this.unitUnderTest.getParameters());
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Test
	public void testRemoveOptionDoesNothingForAnUnknownOption() {
		this.unitUnderTest.setParameters("1|2:3|4:5");
		this.unitUnderTest.removeOption(new Option("3", "2"));
		assertEquals("1|2:3|4:5", this.unitUnderTest.getParameters());
		this.unitUnderTest.removeOption(new Option("42"));
		assertEquals("1|2:3|4:5", this.unitUnderTest.getParameters());
	}

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@Test
	public void testToDiagrammStringRegularRun() {
		assertEquals("Id :int (int)", this.unitUnderTest.toDiagrammString());
	}

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@Test
	public void testToDiagrammStringRegularRunAttributeDefaultValueSet() {
		this.unitUnderTest.setIndividualDefaultValue("4711");
		assertEquals("Id :int (int) - 4711", this.unitUnderTest.toDiagrammString());
	}

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@Test
	public void testToDiagrammStringRegularRunDomainDefaultValueSet() {
		this.domain.setInitialValue("4711");
		assertEquals("Id :int (int) - 4711", this.unitUnderTest.toDiagrammString());
	}

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@Test
	public void testToDiagrammStringRegularRunListMemberFlagSet() {
		this.unitUnderTest.setListItemField(true);
		assertEquals("(#) Id :int (int)", this.unitUnderTest.toDiagrammString());
	}

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@Test
	public void testToDiagrammStringRegularRunPrimaryKeyFlagSet() {
		this.unitUnderTest.setPrimaryKey(true);
		assertEquals("Id :int (int) (PK)", this.unitUnderTest.toDiagrammString());
	}

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@Test
	public void testToDiagrammStringRegularRunWriteableMemberFlagSet() {
		this.diagram.setMarkUpRequiredFieldNames(true);
		System.setProperty("archimedes.scheme.Tabellenspalte.diagramm.writeable.prefix", "W-");
		this.unitUnderTest.setRequired(true);
		assertEquals("W-Id* :int (int)", this.unitUnderTest.toDiagrammString());
	}

}