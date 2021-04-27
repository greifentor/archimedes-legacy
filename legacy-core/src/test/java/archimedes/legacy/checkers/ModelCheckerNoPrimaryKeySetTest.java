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
public class ModelCheckerNoPrimaryKeySetTest {

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/" + fileName);
	}

	@Mock
	private GUIBundle guiBundle;

	@InjectMocks
	private ModelCheckerNoPrimaryKeySet unitUnderTest;

	@Test
	void happyRun_FindThePotentialForeignKey() {
		// Prepare
		DataModel dataModel = readDataModel("ModelCheckers-NoPrimaryKeySet.xml");
		String message = "message";
		doReturn(message)
				.when(guiBundle)
				.getResourceText(ModelCheckerNoPrimaryKeySet.RES_MODEL_CHECKER_NO_PRIMARY_KEY_SET, "ATable");
		// Run
		ModelCheckerMessage[] returned = unitUnderTest.check(dataModel);
		// Check
		assertEquals(1, returned.length);
		assertMessageContained(
				new ModelCheckerMessage(Level.WARNING, message, dataModel.getTableByName("ATable")),
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
		DataModel dataModel = readDataModel("ModelCheckers-NoPrimaryKeySet.xml");
		dataModel
				.getTableByName("ATable")
				.addOption(new Option(ModelCheckerNoPrimaryKeySet.SUPPRESS_NO_PK_WARNING));
		// Run
		ModelCheckerMessage[] returned = unitUnderTest.check(dataModel);
		// Check
		assertEquals(0, returned.length);
	}

}