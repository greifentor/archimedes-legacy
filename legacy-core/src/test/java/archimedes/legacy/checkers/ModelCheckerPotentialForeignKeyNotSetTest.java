package archimedes.legacy.checkers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.acf.checker.ModelCheckerMessage;
import archimedes.acf.checker.ModelCheckerMessage.Level;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;
import baccara.gui.GUIBundle;

@ExtendWith(MockitoExtension.class)
public class ModelCheckerPotentialForeignKeyNotSetTest {

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/" + fileName);
	}

	@Mock
	private GUIBundle guiBundle;

	@InjectMocks
	private ModelCheckerPotentialForeignKeyNotSet unitUnderTest;

	@Test
	void happyRun_FindThePotentialForeignKey() {
		// Prepare
		DataModel dataModel = readDataModel("ModelCheckers-PotentialForeignKeyNotSet.xml");
		String message1 = "message1";
		String message2 = "message2";
		String message3 = "message3";
		doReturn(message1)
				.when(guiBundle)
				.getResourceText(
						ModelCheckerPotentialForeignKeyNotSet.RES_MODEL_CHECKER_POTENTIAL_FOREIGN_KEY,
						"Referencer.ATableId",
						"ATable");
		doReturn(message2)
				.when(guiBundle)
				.getResourceText(
						ModelCheckerPotentialForeignKeyNotSet.RES_MODEL_CHECKER_POTENTIAL_FOREIGN_KEY,
						"Referencer.AnotherTable_Id",
						"AnotherTable");
		doReturn(message3)
				.when(guiBundle)
				.getResourceText(
						ModelCheckerPotentialForeignKeyNotSet.RES_MODEL_CHECKER_POTENTIAL_FOREIGN_KEY,
						"Referencer.Id_ATable",
						"ATable");
		// Run
		ModelCheckerMessage[] returned = unitUnderTest.check(dataModel);
		// Check
		assertEquals(3, returned.length);
		assertMessageContained(
				new ModelCheckerMessage(Level.WARNING, message1, dataModel.getTableByName("Referencer")),
				returned);
		assertMessageContained(
				new ModelCheckerMessage(Level.WARNING, message2, dataModel.getTableByName("Referencer")),
				returned);
		assertMessageContained(
				new ModelCheckerMessage(Level.WARNING, message3, dataModel.getTableByName("Referencer")),
				returned);
	}

	public static void assertMessageContained(ModelCheckerMessage message, ModelCheckerMessage[] messages) {
		for (ModelCheckerMessage m : messages) {
			if ((m.getLevel() == message.getLevel())
					&& (m.getMessage().equals(message.getMessage()) && (m.getObject() == message.getObject()))) {
				return;
			}
		}
		fail("message not contained in array: " + message);
	}

	@Test
	void happyRun_SuppressedWarnings() {
		// Prepare
		DataModel dataModel = readDataModel("ModelCheckers-PotentialForeignKeyNotSet.xml");
		String message = "message";
		doReturn(message)
				.when(guiBundle)
				.getResourceText(
						ModelCheckerPotentialForeignKeyNotSet.RES_MODEL_CHECKER_POTENTIAL_FOREIGN_KEY,
						"Referencer.ATableId",
						"ATable");
		dataModel
				.getTableByName("Referencer")
				.getColumnByName("AnotherTable_Id")
				.addOption(new Option(ModelCheckerPotentialForeignKeyNotSet.SUPPRESS_POTENTIAL_FK_WARNING));
		dataModel
				.getTableByName("Referencer")
				.getColumnByName("Id_ATable")
				.addOption(new Option(ModelCheckerPotentialForeignKeyNotSet.SUPPRESS_POTENTIAL_FK_WARNING));
		// Run
		ModelCheckerMessage[] returned = unitUnderTest.check(dataModel);
		// Check
		assertEquals(1, returned.length);
		assertMessageContained(
				new ModelCheckerMessage(Level.WARNING, message, dataModel.getTableByName("Referencer")),
				returned);
	}

	@Test
	void happyRun_FindThePotentialForeignKey_ModeStrictInDataModel() {
		// Prepare
		DataModel dataModel = readDataModel("ModelCheckers-PotentialForeignKeyNotSet.xml");
		String message1 = "message1";
		String message2 = "message2";
		String message3 = "message3";
		String message4 = "message4";
		doReturn(message1)
		        .when(guiBundle)
		        .getResourceText(
		                ModelCheckerPotentialForeignKeyNotSet.RES_MODEL_CHECKER_POTENTIAL_FOREIGN_KEY,
		                "Referencer.ATableId",
		                "ATable");
		doReturn(message2)
		        .when(guiBundle)
		        .getResourceText(
		                ModelCheckerPotentialForeignKeyNotSet.RES_MODEL_CHECKER_POTENTIAL_FOREIGN_KEY,
		                "Referencer.AnotherTable_Id",
		                "AnotherTable");
		doReturn(message3)
		        .when(guiBundle)
		        .getResourceText(
		                ModelCheckerPotentialForeignKeyNotSet.RES_MODEL_CHECKER_POTENTIAL_FOREIGN_KEY,
		                "Referencer.Id_ATable",
		                "ATable");
		doReturn(message4)
		        .when(guiBundle)
		        .getResourceText(
		                ModelCheckerPotentialForeignKeyNotSet.RES_MODEL_CHECKER_POTENTIAL_FOREIGN_KEY,
		                "Referencer.Id_BTable",
		                "BTable");
		dataModel
		        .addOption(
		                new Option(
		                        ModelCheckerPotentialForeignKeyNotSet.POTENTIAL_FK_WARNING_MODE,
		                        ModelCheckerPotentialForeignKeyNotSet.POTENTIAL_FK_WARNING_MODE_STRICT));
		// Run
		ModelCheckerMessage[] returned = unitUnderTest.check(dataModel);
		// Check
		assertEquals(4, returned.length);
		assertMessageContained(
		        new ModelCheckerMessage(Level.WARNING, message1, dataModel.getTableByName("Referencer")),
		        returned);
		assertMessageContained(
		        new ModelCheckerMessage(Level.WARNING, message2, dataModel.getTableByName("Referencer")),
		        returned);
		assertMessageContained(
		        new ModelCheckerMessage(Level.WARNING, message3, dataModel.getTableByName("Referencer")),
		        returned);
		assertMessageContained(
		        new ModelCheckerMessage(Level.WARNING, message4, dataModel.getTableByName("Referencer")),
		        returned);
	}

	@Test
	void happyRun_FindThePotentialForeignKey_ModeStrictInTableModel() {
		// Prepare
		DataModel dataModel = readDataModel("ModelCheckers-PotentialForeignKeyNotSet.xml");
		String message1 = "message1";
		String message2 = "message2";
		String message3 = "message3";
		String message4 = "message4";
		doReturn(message1)
		        .when(guiBundle)
		        .getResourceText(
		                ModelCheckerPotentialForeignKeyNotSet.RES_MODEL_CHECKER_POTENTIAL_FOREIGN_KEY,
		                "Referencer.ATableId",
		                "ATable");
		doReturn(message2)
		        .when(guiBundle)
		        .getResourceText(
		                ModelCheckerPotentialForeignKeyNotSet.RES_MODEL_CHECKER_POTENTIAL_FOREIGN_KEY,
		                "Referencer.AnotherTable_Id",
		                "AnotherTable");
		doReturn(message3)
		        .when(guiBundle)
		        .getResourceText(
		                ModelCheckerPotentialForeignKeyNotSet.RES_MODEL_CHECKER_POTENTIAL_FOREIGN_KEY,
		                "Referencer.Id_ATable",
		                "ATable");
		doReturn(message4)
		        .when(guiBundle)
		        .getResourceText(
		                ModelCheckerPotentialForeignKeyNotSet.RES_MODEL_CHECKER_POTENTIAL_FOREIGN_KEY,
		                "Referencer.Id_BTable",
		                "BTable");
		dataModel
		        .getTableByName("Referencer")
		        .addOption(
		                new Option(
		                        ModelCheckerPotentialForeignKeyNotSet.POTENTIAL_FK_WARNING_MODE,
		                        ModelCheckerPotentialForeignKeyNotSet.POTENTIAL_FK_WARNING_MODE_STRICT));
		// Run
		ModelCheckerMessage[] returned = unitUnderTest.check(dataModel);
		// Check
		assertEquals(4, returned.length);
		assertMessageContained(
		        new ModelCheckerMessage(Level.WARNING, message1, dataModel.getTableByName("Referencer")),
		        returned);
		assertMessageContained(
		        new ModelCheckerMessage(Level.WARNING, message2, dataModel.getTableByName("Referencer")),
		        returned);
		assertMessageContained(
		        new ModelCheckerMessage(Level.WARNING, message3, dataModel.getTableByName("Referencer")),
		        returned);
		assertMessageContained(
		        new ModelCheckerMessage(Level.WARNING, message4, dataModel.getTableByName("Referencer")),
		        returned);
	}

	@Test
	void happyRun_FindThePotentialForeignKey_ModeStrictInDataModelButOverridenWithWeakInTable() {
		// Prepare
		DataModel dataModel = readDataModel("ModelCheckers-PotentialForeignKeyNotSet.xml");
		String message1 = "message1";
		String message2 = "message2";
		String message3 = "message3";
		doReturn(message1)
		        .when(guiBundle)
		        .getResourceText(
		                ModelCheckerPotentialForeignKeyNotSet.RES_MODEL_CHECKER_POTENTIAL_FOREIGN_KEY,
		                "Referencer.ATableId",
		                "ATable");
		doReturn(message2)
		        .when(guiBundle)
		        .getResourceText(
		                ModelCheckerPotentialForeignKeyNotSet.RES_MODEL_CHECKER_POTENTIAL_FOREIGN_KEY,
		                "Referencer.AnotherTable_Id",
		                "AnotherTable");
		doReturn(message3)
		        .when(guiBundle)
		        .getResourceText(
		                ModelCheckerPotentialForeignKeyNotSet.RES_MODEL_CHECKER_POTENTIAL_FOREIGN_KEY,
		                "Referencer.Id_ATable",
		                "ATable");
		dataModel
		        .addOption(
		                new Option(
		                        ModelCheckerPotentialForeignKeyNotSet.POTENTIAL_FK_WARNING_MODE,
		                        ModelCheckerPotentialForeignKeyNotSet.POTENTIAL_FK_WARNING_MODE_STRICT));
		dataModel
		        .getTableByName("Referencer")
		        .addOption(
		                new Option(
		                        ModelCheckerPotentialForeignKeyNotSet.POTENTIAL_FK_WARNING_MODE,
		                        ModelCheckerPotentialForeignKeyNotSet.POTENTIAL_FK_WARNING_MODE_WEAK));
		// Run
		ModelCheckerMessage[] returned = unitUnderTest.check(dataModel);
		assertEquals(3, returned.length);
		assertMessageContained(
		        new ModelCheckerMessage(Level.WARNING, message1, dataModel.getTableByName("Referencer")),
		        returned);
		assertMessageContained(
		        new ModelCheckerMessage(Level.WARNING, message2, dataModel.getTableByName("Referencer")),
		        returned);
		assertMessageContained(
		        new ModelCheckerMessage(Level.WARNING, message3, dataModel.getTableByName("Referencer")),
		        returned);
	}

}