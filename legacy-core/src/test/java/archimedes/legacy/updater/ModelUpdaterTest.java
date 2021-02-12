package archimedes.legacy.updater;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.legacy.updater.UpdateReportAction.Status;
import archimedes.legacy.updater.UpdateReportAction.Type;
import archimedes.model.DataModel;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class ModelUpdaterTest {

	@Test
	void passedSameModelTwice_ReturnsAnEmptyUpdateReport() {
		// Prepare
		UpdateReport expected = new UpdateReport();
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel = reader.read("src/test/resources/dm/ModelUpdater/BaseModel.xml");
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel, dataModel);
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
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal.
	}

	@Test
	void passedModelWithOneColumnMoreAsSource_ReturnsAnUpdateReportWithMessageToAdd() {
		// Prepare
		UpdateReport expected =
				new UpdateReport()
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
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal (means changes done).
	}

	@Test
	void passedModelWithOneColumnLessAsSource_ReturnsAnUpdateReportWithMessageToDrop() {
		// Prepare
		UpdateReport expected =
				new UpdateReport()
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
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal (means changes done).
	}

	@Test
	void passedModelWithOneTableLessAsSource_ReturnsAnUpdateReportWithMessageToDrop() {
		// Prepare
		UpdateReport expected =
				new UpdateReport()
						.addUpdateReportAction(
								new UpdateReportAction()
										.setMessage("DropTableChangeActionCRO(tableName=Account, schemaName=)")
										.setStatus(Status.DONE)
										.setType(Type.DROP_TABLE)
										.setValues("Account"));
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel0 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel.xml");
		DataModel dataModel1 = reader.read("src/test/resources/dm/ModelUpdater/BaseModel-Account-Dropped.xml");
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal (means changes done).
	}

	@Test
	void passedModelWithAColumnWithDifferentNullable_ReturnsAnUpdateReportWithMessageToModify() {
		// Prepare
		UpdateReport expected =
				new UpdateReport()
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
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal (means changes done).
	}

	@Test
	void passedModelWithAColumnWithDifferentType_ReturnsAnUpdateReportWithMessageToModify() {
		// Prepare
		UpdateReport expected =
				new UpdateReport()
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
		ModelUpdater unitUnderTest = new ModelUpdater(dataModel0, dataModel1);
		// Run
		UpdateReport returned = unitUnderTest.update();
		// Check
		assertEquals(expected, returned);
		assertEquals(new UpdateReport(), unitUnderTest.update()); // Models are equal (means changes done).
	}

}