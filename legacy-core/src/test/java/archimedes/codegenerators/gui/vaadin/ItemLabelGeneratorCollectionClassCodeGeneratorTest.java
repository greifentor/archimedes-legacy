package archimedes.codegenerators.gui.vaadin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
class ItemLabelGeneratorCollectionClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private ItemLabelGeneratorCollectionClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObjectWithNoGenerators() {
			// Prepare
			String expected =
					"package base.pack.age.name.gui.vaadin.masterdata.renderer;\n" //
							+ "\n" //
							+ "import javax.inject.Named;\n" //
							+ "\n" //
							+ "import org.springframework.beans.factory.annotation.Autowired;\n" //
							+ "\n" //
							+ "import com.vaadin.flow.component.ItemLabelGenerator;\n" //
							+ "\n" //
							+ "import lombok.Generated;\n" //
							+ "import lombok.Getter;\n" //
							+ "\n" //
							+ "/**\n" //
							+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
							+ " */\n" //
							+ "@Named\n" //
							+ "@Getter\n" //
							+ "@Generated\n" //
							+ "public class ATableItemLabelGeneratorCollection {\n" //
							+ "\n" //
							+ "}";
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void withAnEnum() {
			// Prepare
			String expected =
					"package base.pack.age.name.gui.vaadin.masterdata.renderer;\n" //
							+ "\n" //
							+ "import javax.inject.Named;\n" //
							+ "\n" //
							+ "import org.springframework.beans.factory.annotation.Autowired;\n" //
							+ "\n" //
							+ "import com.vaadin.flow.component.ItemLabelGenerator;\n" //
							+ "\n" //
							+ "import base.pack.age.name.core.model.EnumType;\n" //
							+ "import lombok.Generated;\n" //
							+ "import lombok.Getter;\n" //
							+ "\n" //
							+ "/**\n" //
							+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
							+ " */\n" //
							+ "@Named\n" //
							+ "@Getter\n" //
							+ "@Generated\n" //
							+ "public class TableWithEnumTypeItemLabelGeneratorCollection {\n" //
							+ "\n" //
							+ "	@Autowired(required = false)\n" //
							+ "	private ItemLabelGenerator<EnumType> enumTypeItemLabelGenerator;\n" //
							+ "\n" //
							+ "}";
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned =
					unitUnderTest
							.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("TABLE_WITH_ENUM_TYPE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}
