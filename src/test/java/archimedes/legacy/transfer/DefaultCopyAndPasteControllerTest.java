/*
 * DefaultCopyAndPasteControllerTest.java
 *
 * 08.12.2016
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.transfer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.xml.ModelXMLReader;

/**
 * Test of the class <CODE>DefaultCopyAndPasteController</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 08.12.2016 - Added.
 */

public class DefaultCopyAndPasteControllerTest {

	private DefaultCopyAndPasteController unitUnderTest = null;
	private DataModel dm = null;

	/**
	 * @changed OLI 08.12.2016 - Added.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.unitUnderTest = new DefaultCopyAndPasteController();
		this.loadDiagramIfNecessary();
	}

	private void loadDiagramIfNecessary() throws Exception {
		if (this.dm == null) {
			this.dm = new ModelXMLReader(new ArchimedesObjectFactory()).read("src/test/resources/dm/XMLTestModel.xml");
		}
	}

	/**
	 * @changed OLI 08.12.2016 - Added.
	 */
	@Disabled("Does not work any longer ???")
	@Test
	public void testTableToTransferableStringReturnsACorrectXMLString() {
		TableModel tm = this.dm.getTableByName("Building");
		assertEquals("<Tables>\n"
				+ "    <Table activeInApplication=\"true\" additionalCreateConstraint=\"Constraint\" codeFolder=\"pack\" codeGeneratorOptions=\"OPT\" colorBackground=\"goldgelb\" colorForeground=\"schwarz\" comment=\"Comment\" contextName=\"Context\" deprecated=\"false\" draft=\"false\" dynamicCode=\"true\" externalTable=\"false\" firstGenerationDone=\"true\" generateCode=\"true\" history=\"@changed OLI - Added.\" inherited=\"true\" name=\"Building\" nmRelation=\"false\" uniqueFormula=\"Id &amp;amp; Name\">\n"
				+ "        <Column canBeReferenced=\"true\" comment=\"bla bla$BR$laber$BR$s&amp;uuml;lz\" deprecated=\"true\" domain=\"Ident\" editorAlterInBatchView=\"true\" editorDisabled=\"true\" editorLabelText=\"Id\" editorMaxLength=\"42\" editorMember=\"true\" editorMnemonic=\"I\" editorPosition=\"1\" editorReferenceWeight=\"SMALL\" editorResourceId=\"A\" editorToolTipText=\"The id\" encrypted=\"true\" global=\"true\" globalId=\"true\" hasIndex=\"true\" hideReference=\"true\" history=\"@changed OLI - Added.\" indexSearchMember=\"true\" individualDefaultValue=\"4711\" lastModificationField=\"true\" listItemField=\"true\" name=\"Id\" notNull=\"true\" panelNumber=\"0\" parameter=\"B\" primaryKey=\"true\" removedStateField=\"true\" required=\"true\" sequenceForKeyGeneration=\"BuildingSeq\" suppressForeignKeyConstraints=\"true\" technicalField=\"true\" transient=\"true\" unique=\"true\" />\n"
				+ "        <Column canBeReferenced=\"false\" comment=\"\" deprecated=\"false\" domain=\"Description\" editorAlterInBatchView=\"false\" editorDisabled=\"false\" editorLabelText=\"\" editorMaxLength=\"0\" editorMember=\"false\" editorMnemonic=\"\" editorPosition=\"0\" editorReferenceWeight=\"NONE\" editorResourceId=\"\" editorToolTipText=\"\" encrypted=\"false\" global=\"false\" globalId=\"false\" hasIndex=\"false\" hideReference=\"false\" history=\"@changed OLI - Added.\" indexSearchMember=\"false\" individualDefaultValue=\"\" lastModificationField=\"false\" listItemField=\"false\" name=\"Name\" notNull=\"true\" panelNumber=\"0\" parameter=\"\" primaryKey=\"false\" removedStateField=\"false\" required=\"false\" sequenceForKeyGeneration=\"\" suppressForeignKeyConstraints=\"false\" technicalField=\"false\" transient=\"false\" unique=\"false\" />\n"
				+ "        <ComboStringMember columnName=\"Name\" prefix=\"&amp;lt;\" suffix=\"&amp;gt;\" tableName=\"Building\" />\n"
				+ "        <CompareToMember columnName=\"Name\" tableName=\"Building\" />\n"
				+ "        <EqualsMember columnName=\"Id\" tableName=\"Building\" />\n"
				+ "        <EqualsMember columnName=\"Name\" tableName=\"Building\" />\n"
				+ "        <HashCodeMember columnName=\"Name\" tableName=\"Building\" />\n"
				+ "        <HashCodeMember columnName=\"Id\" tableName=\"Building\" />\n"
				+ "        <NReference alterable=\"true\" columnName=\"Building\" deleteConfirmationRequired=\"true\" extensible=\"true\" id=\"1\" nReferencePanelType=\"STANDARD\" panelNumber=\"1\" permitCreate=\"true\" tableName=\"Room\" />\n"
				+ "        <Options>\n" + "            <Option name=\"OPT1\" parameter=\"ena\" />\n"
				+ "            <Option name=\"OPT2\" parameter=\"dyo\" />\n"
				+ "            <Option name=\"OPT3\" parameter=\"tres\" />\n" + "        </Options>\n"
				+ "        <Option name=\"OPT1\" parameter=\"ena\" />\n"
				+ "        <Option name=\"OPT2\" parameter=\"dyo\" />\n"
				+ "        <Option name=\"OPT3\" parameter=\"tres\" />\n"
				+ "        <Panel panelClass=\"FORM\" panelNumber=\"0\" tabMnemonic=\"1\" tabTitle=\"data\" tabToolTipText=\"Hier k&amp;ouml;nnen Sie die Daten des Objekt warten\" />\n"
				+ "        <Panel panelClass=\"LIST\" panelNumber=\"1\" tabMnemonic=\"A\" tabTitle=\"rooms\" tabToolTipText=\"The rooms\" />\n"
				+ "        <SelectionMember columnName=\"Id\" printExpression=\"\" selectionAttributeName=\"OPTIONAL\" tableName=\"Building\" />\n"
				+ "        <SelectionMember columnName=\"Name\" printExpression=\"\" selectionAttributeName=\"IMPORTANT\" tableName=\"Building\" />\n"
				+ "        <SelectionOrderMember columnName=\"Name\" direction=\"ASC\" tableName=\"Building\" />\n"
				+ "        <SelectionOrderMember columnName=\"Id\" direction=\"DESC\" tableName=\"Building\" />\n"
				+ "        <Stereotype name=\"Anchor\" />\n"
				+ "        <ToStringMember columnName=\"Id\" prefix=\"Id=\" suffix=\", \" tableName=\"Building\" />\n"
				+ "        <ToStringMember columnName=\"Name\" prefix=\"Name=\" suffix=\"\" tableName=\"Building\" />\n"
				+ "        <View name=\"Main\" x=\"400\" y=\"125\" />\n" + "    </Table>\n" + "</Tables>\n",
				this.unitUnderTest.tableToTransferableString(tm));
	}

	/**
	 * @changed OLI 08.12.2016 - Added.
	 */
	@Test
	public void testTransferableStringToTableAddsTheDataOfThePassedStringToTheDataModel() throws Exception {
		TableModel tm = this.dm.getTableByName("Building");
		tm.setName("TEST");
		String s = this.unitUnderTest.tableToTransferableString(tm);
		tm.setName("Building");
		this.unitUnderTest.transferableStringToTable(this.dm, s);
		String s0 = this.unitUnderTest.tableToTransferableString(dm.getTableByName("TEST"));
		assertEquals(s, s0);
	}

}