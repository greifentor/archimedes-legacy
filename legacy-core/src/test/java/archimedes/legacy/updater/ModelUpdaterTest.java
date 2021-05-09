package archimedes.legacy.updater;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.legacy.Archimedes;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.legacy.updater.UpdateReportAction.Status;
import archimedes.legacy.updater.UpdateReportAction.Type;
import archimedes.model.DataModel;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class ModelUpdaterTest {

	@BeforeAll
	static void setUpAll() {
		System.setProperty("corentx.util.Str.suppress.html.note", "true");
		System.setProperty("corent.base.StrUtil.suppress.html.note", "true");
	}

	@Test
	void passedSameModelTwice_ReturnsAnEmptyUpdateReport() {
		// Prepare
		UpdateReport expected = new UpdateReport();
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel = reader.read("src/test/resources/dm/ModelUpdater/BaseModel.xml");
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel, dataModel, Archimedes.Factory);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void passedEqualModels_ReturnsAnEmptyUpdateReport() {
		// Prepare
		UpdateReport expected = new UpdateReport();
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel0 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel.xml");
		DataModel dataModel1 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel.xml");
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1, Archimedes.Factory);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal.
	}

	@Test
	void passedModelWithOneColumnMoreAsSource_ReturnsAnUpdateReportWithMessageToAdd() {
		// Prepare
		UpdateReport expected = new UpdateReport()
				.addUpdateReportAction(
						new UpdateReportAction()
								.setMessage(
										"AddColumnChangeActionCRO(tableName=Account, schemaName=, columnName=Note, sqlType=LONGVARCHAR)")
								.setStatus(Status.DONE)
								.setType(Type.ADD_COLUMN)
								.setValues("Account", "Note", "LONGVARCHAR"));
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel0 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel-Account-Note-Dropped.xml");
		DataModel dataModel1 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel.xml");
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1, Archimedes.Factory);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertEquals(0, dataModel0.getTableByName("Account").getColumnByName("Note").getPanel().getPanelNumber());
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal (means changes done).
	}

	@Test
	void passedModelWithOneColumnLessAsSource_ReturnsAnUpdateReportWithMessageToDrop() {
		// Prepare
		UpdateReport expected = new UpdateReport()
				.addUpdateReportAction(
						new UpdateReportAction()
								.setMessage(
										"DropColumnChangeActionCRO(tableName=Account, schemaName=, columnName=Note)")
								.setStatus(Status.DONE)
								.setType(Type.DROP_COLUMN)
								.setValues("Account", "Note"));
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel0 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel.xml");
		DataModel dataModel1 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel-Account-Note-Dropped.xml");
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1, Archimedes.Factory);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal (means changes done).
	}

	@Test
	void passedModelWithOneTableLessAsSource_ReturnsAnUpdateReportWithMessageToDrop() {
		// Prepare
		UpdateReport expected = new UpdateReport()
				.addUpdateReportAction(
						new UpdateReportAction()
								.setMessage("DropTableChangeActionCRO(tableName=Account, schemaName=)")
								.setStatus(Status.DONE)
								.setType(Type.DROP_TABLE)
								.setValues("Account"));
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel0 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel.xml");
		DataModel dataModel1 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel-Account-Dropped.xml");
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1, Archimedes.Factory);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal (means changes done).
	}

	@Test
	void passedModelWithAColumnWithDifferentNullable_ReturnsAnUpdateReportWithMessageToModify() {
		// Prepare
		UpdateReport expected = new UpdateReport()
				.addUpdateReportAction(
						new UpdateReportAction()
								.setMessage(
										"ModifyNullableCRO(tableName=Account, schemaName=, columnName=AccountNumber, newNullable=true)")
								.setStatus(Status.DONE)
								.setType(Type.MODIFY_COLUMN_CONSTRAINT_NOT_NULL)
								.setValues("Account", "AccountNumber", "false"));
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel0 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel.xml");
		DataModel dataModel1 =
				reader.read("src/test/resources/dm/ModelUpdater/BaseModel-Account-AccountNumber-Nullable.xml");
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1, Archimedes.Factory);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal (means changes done).
	}

	@Test
	void passedModelWithAColumnWithDifferentType_ReturnsAnUpdateReportWithMessageToModify() {
		// Prepare
		UpdateReport expected = new UpdateReport()
				.addUpdateReportAction(
						new UpdateReportAction()
								.setMessage(
										"ModifyDataTypeCRO(tableName=Account, schemaName=, columnName=AccountNumber, newDataType=VARCHAR(42))")
								.setStatus(Status.DONE)
								.setType(Type.MODIFY_COLUMN_DATATYPE)
								.setValues("Account", "AccountNumber", "VARCHAR(42)"));
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel0 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel.xml");
		DataModel dataModel1 =
				reader.read("src/test/resources/dm/ModelUpdater/BaseModel-Account-AccountNumber-Varchar42.xml");
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1, Archimedes.Factory);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertEquals(
				"Varchar42",
				dataModel0.getTableByName("Account").getColumnByName("AccountNumber").getDomain().getName());
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal (means changes done).
	}

	@Test
	void passedModelWithAForeignKeyMore_ReturnsAnUpdateReportWithMessageToDrop() {
		// Prepare
		UpdateReport expected = new UpdateReport()
				.addUpdateReportAction(
						new UpdateReportAction()
								.setMessage(
										"Account.Owner -> Customer.Id - DropForeignKeyCRO(tableName=Account, schemaName=, constraintName=FK_TO_Customer_Id, members=[ForeignKeyMemberCRO(baseColumnName=Owner, baseTableName=Account, referencedColumnName=Id, referencedTableName=Customer)])")
								.setStatus(Status.DONE)
								.setType(Type.DROP_FOREIGN_KEY)
								.setValues(
										"Account",
										"[ForeignKeyMemberCRO(baseColumnName=Owner, baseTableName=Account, referencedColumnName=Id, referencedTableName=Customer)]"));
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel0 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel.xml");
		DataModel dataModel1 =
				reader.read("src/test/resources/dm/ModelUpdater/BaseModel-Account-Customer-FK-Dropped.xml");
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1, Archimedes.Factory);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertNull(dataModel0.getTableByName("Account").getColumnByName("Owner").getReferencedColumn());
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal (means changes done).
	}

	@Test
	void passedModelWithAForeignKeyLess_ReturnsAnUpdateReportWithMessageToAdd() {
		// Prepare
		UpdateReport expected = new UpdateReport()
				.addUpdateReportAction(
						new UpdateReportAction()
								.setMessage(
										"Account.Owner -> Customer.Id - AddForeignKeyCRO(tableName=Account, schemaName=, members=[ForeignKeyMemberCRO(baseColumnName=Owner, baseTableName=Account, referencedColumnName=Id, referencedTableName=Customer)])")
								.setStatus(Status.DONE)
								.setType(Type.ADD_FOREIGN_KEY)
								.setValues(
										"Account",
										"[ForeignKeyMemberCRO(baseColumnName=Owner, baseTableName=Account, referencedColumnName=Id, referencedTableName=Customer)]"));
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel0 =
				reader.read("src/test/resources/dm/ModelUpdater/BaseModel-Account-Customer-FK-Dropped.xml");
		DataModel dataModel1 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel.xml");
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1, Archimedes.Factory);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertNotNull(dataModel0.getTableByName("Account").getColumnByName("Owner").getReferencedColumn());
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal (means changes done).
	}

	@Test
	void passedModelWithTwoForeignKeysLess_ReturnsAnUpdateReportWithMessagesToAdd() {
		// Prepare
		UpdateReport expected = new UpdateReport()
				.addUpdateReportAction(
						new UpdateReportAction()
								.setMessage(
										"Account.Bank -> Bank.Id - AddForeignKeyCRO(tableName=Account, schemaName=, members=[ForeignKeyMemberCRO(baseColumnName=Bank, baseTableName=Account, referencedColumnName=Id, referencedTableName=Bank)])")
								.setStatus(Status.DONE)
								.setType(Type.ADD_FOREIGN_KEY)
								.setValues(
										"Account",
										"[ForeignKeyMemberCRO(baseColumnName=Bank, baseTableName=Account, referencedColumnName=Id, referencedTableName=Bank)]"))
				.addUpdateReportAction(
						new UpdateReportAction()
								.setMessage(
										"Account.Owner -> Customer.Id - AddForeignKeyCRO(tableName=Account, schemaName=, members=[ForeignKeyMemberCRO(baseColumnName=Owner, baseTableName=Account, referencedColumnName=Id, referencedTableName=Customer)])")
								.setStatus(Status.DONE)
								.setType(Type.ADD_FOREIGN_KEY)
								.setValues(
										"Account",
										"[ForeignKeyMemberCRO(baseColumnName=Owner, baseTableName=Account, referencedColumnName=Id, referencedTableName=Customer)]"));
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel0 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel-Two-FKs-Dropped.xml");
		DataModel dataModel1 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel-Two-FKs.xml");
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1, Archimedes.Factory);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertNotNull(dataModel0.getTableByName("Account").getColumnByName("Owner").getReferencedColumn());
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal (means changes done).
	}

	@Test
	void passedModelWithOneTableLessMoreThanSource_ReturnsAnUpdateReportWithMessageToCreate() {
		// Prepare
		UpdateReport expected = new UpdateReport()
				.addUpdateReportAction(
						new UpdateReportAction()
								.setMessage(
										"CreateTableChangeActionCRO(columns=[ColumnDataCRO(name=Id, sqlType=BIGINT, nullable=false), ColumnDataCRO(name=Name, sqlType=VARCHAR(255), nullable=false)], tableName=Bank, schemaName=, primaryKeyMemberNames=[Id])")
								.setStatus(Status.DONE)
								.setType(Type.CREATE_TABLE)
								.setValues("Bank"));
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel0 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel.xml");
		DataModel dataModel1 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel-Bank-Added.xml");
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1, Archimedes.Factory);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertNotNull(dataModel0.getTableByName("Bank"));
		assertNotNull(dataModel0.getTableByName("Bank").getColumnByName("Name"));
		assertNotNull(dataModel0.getTableByName("Bank").getColumnByName("Name").getPanel());
		assertFalse(dataModel0.getTableByName("Bank").isDraft());
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal (means changes done).
	}

	@Test
	void passedModelWithOnePrimaryKeyMoreThanSource_ReturnsAnUpdateReportWithMessageToDrop() {
		// Prepare
		UpdateReport expected = new UpdateReport()
				.addUpdateReportAction(
						new UpdateReportAction()
								.setMessage("AddPrimaryKeyCRO(tableName=Account, schemaName=, pkMemberNames=[Id])")
								.setStatus(Status.DONE)
								.setType(Type.ADD_PRIMARY_KEY)
								.setValues("Account", "[Id]"));
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel1 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel.xml");
		DataModel dataModel0 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel-Account-PK-Dropped.xml");
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1, Archimedes.Factory);
		// Run
		assertFalse(dataModel0.getTableByName("Account").getColumnByName("Id").isPrimaryKey());
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertNotNull(dataModel0.getTableByName("Account"));
		assertNotNull(dataModel0.getTableByName("Account").getColumnByName("Id"));
		assertTrue(dataModel0.getTableByName("Account").getColumnByName("Id").isPrimaryKey());
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal (means changes done).
	}

	@Test
	void passedModelWithOneIndexMoreThanSource_ReturnsAnUpdateReportWithMessageToAdd() {
		// Prepare
		UpdateReport expected = new UpdateReport()
				.addUpdateReportAction(
						new UpdateReportAction()
								.setMessage(
										"AddIndexCRO(tableName=Customer, schemaName=, indexName=ix_Customer_Name, indexMemberNames=[Name])")
								.setStatus(Status.DONE)
								.setType(Type.ADD_INDEX)
								.setValues("ix_Customer_Name", "Customer", "[Name]"));
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel0 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel.xml");
		DataModel dataModel1 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel-Index-Added.xml");
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1, Archimedes.Factory);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertNotNull(dataModel0.getTableByName("Customer"));
		assertNotNull(dataModel0.getTableByName("Customer").getColumnByName("Name"));
		assertTrue(dataModel0.getTableByName("Customer").getColumnByName("Name").hasIndex());
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal (means changes done).
	}

	@Test
	void passedModelWithOneIndexLessThanSource_ReturnsAnUpdateReportWithMessageToDrop() {
		// Prepare
		UpdateReport expected = new UpdateReport()
				.addUpdateReportAction(
						new UpdateReportAction()
								.setMessage(
										"DropIndexCRO(tableName=Customer, schemaName=, indexName=ix_Customer_Name, indexMemberNames=[Name])")
								.setStatus(Status.DONE)
								.setType(Type.DROP_INDEX)
								.setValues("ix_Customer_Name", "Customer", "[Name]"));
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel0 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel.xml");
		DataModel dataModel1 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel-Index-Added.xml");
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel1, dataModel0, Archimedes.Factory);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertNotNull(dataModel0.getTableByName("Customer"));
		assertNotNull(dataModel0.getTableByName("Customer").getColumnByName("Name"));
		assertFalse(dataModel0.getTableByName("Customer").getColumnByName("Name").hasIndex());
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal (means changes done).
	}

}