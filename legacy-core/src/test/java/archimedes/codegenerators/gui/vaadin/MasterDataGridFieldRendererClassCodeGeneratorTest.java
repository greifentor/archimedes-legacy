package archimedes.codegenerators.gui.vaadin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class MasterDataGridFieldRendererClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private MasterDataGridFieldRendererClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class SimpleTable {

		@Test
		void happyRunForASimpleObject() {
			DataModel dataModel = readDataModel("Model.xml");
			assertFalse(unitUnderTest.isToIgnoreFor(dataModel, dataModel.getTableByName("A_TABLE")));
		}

	}

	@Nested
	class TableWithObjectReferences {

		private String getExpected() {
			return "";
		}

		@Test
		void happyRun() {
			// Prepare
			String expected = getExpected();
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			// Run
			String returned =
					unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("GUI_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}
