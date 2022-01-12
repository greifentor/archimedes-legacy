package archimedes.codegenerators.gui.vaadin.go;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.codegenerators.NameGenerator;
import archimedes.codegenerators.gui.vaadin.GUIVaadinNameGenerator;
import archimedes.codegenerators.gui.vaadin.go.GOClassCodeGenerator;
import archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator;
import archimedes.codegenerators.service.ModelClassCodeGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GOClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private GOClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected("gui.vaadin.go");
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(String packageName) {
			return getExpected(null, packageName, false);
		}

		private String getExpected(String prefix, String packageName, boolean suppressComment) {
			return getExpected(prefix, packageName, suppressComment, false);
		}

		private String getExpected(String prefix, String packageName, boolean suppressComment,
				boolean descriptionNotNull) {
			String s =
					"package " + BASE_PACKAGE_NAME + "." + (prefix != null ? prefix + "." : "") + packageName + ";\n" + //
							"\n" + //
							"import java.time.LocalDate;\n" + //
							"\n" + //
							"import lombok.Data;\n" + //
							"import lombok.Generated;\n" + //
							"import lombok.experimental.Accessors;\n" + //
							"\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * A container for a_tables data in the GUI layer.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s += "@Accessors(chain = true)\n" + //
					"@Data\n" + //
					"@Generated\n" + //
					"public class ATableGO {\n" + //
					"\n" + //
					"	private long id;\n" + //
					"	private LocalDate aDate;\n" + //
					"	private String description;\n" + //
					"\n" + //
					"}";
			return s;
		}

		@Test
		void happyRunForASimpleObjectAndAlternatePackageName() {
			// Prepare
			String alternatePackageName = "alternate.name";
			String expected = getExpected(alternatePackageName);
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.addOption(new Option(GUIVaadinNameGenerator.ALTERNATE_GO_PACKAGE_NAME, alternatePackageName));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectAndTechnicalPackageName() {
			// Prepare
			String technicalContextName = "technical";
			String expected = getExpected(technicalContextName + ".gui.vaadin.go");
			DataModel dataModel = readDataModel("Model.xml");
			dataModel
					.getTableByName("A_TABLE")
					.addOption(new Option(NameGenerator.TECHNICAL_CONTEXT, technicalContextName));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithModuleForTable() {
			// Prepare
			String prefix = "prefix";
			String expected = getExpected(prefix, "gui.vaadin.go", false);
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.addOption(new Option(PersistenceJPANameGenerator.MODULE, prefix));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithSuppressedComment() {
			// Prepare
			String expected = getExpected(null, "gui.vaadin.go", true);
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "off"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithNotNullField() {
			// Prepare
			String expected = getExpected(null, "gui.vaadin.go", false, true);
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.getColumnByName("Description").setNotNull(true);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		private String getExpectedPOJOModeBuilder(String packageName, String generatedValue) {
			return "package " + BASE_PACKAGE_NAME + "." + packageName + ";\n" + //
					"\n" + //
					"import java.time.LocalDate;\n" + //
					"\n" + //
					"import lombok.AllArgsConstructor;\n" + //
					"import lombok.Builder;\n" + //
					"import lombok.NoArgsConstructor;\n" + //
					"import lombok.Data;\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"/**\n" + //
					" * A container for a_tables data in the GUI layer.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Builder\n" + //
					"@AllArgsConstructor\n" + //
					"@NoArgsConstructor\n" + //
					"@Data\n" + //
					"@Generated\n" + //
					"public class ATableGO {\n" + //
					"\n" + //
					"	private long id;\n" + //
					"	private LocalDate aDate;\n" + //
					"	private String description;\n" + //
					"\n" + //
					"}";
		}

		@Test
		void happyRunForASimpleObjectPOJOModeBUILDWithIDENTITY() {
			// Prepare
			String expected = getExpectedPOJOModeBuilder("gui.vaadin.go", "IDENTITY");
			DataModel dataModel = readDataModel("Model.xml");
			dataModel
					.addOption(
							new Option(
									AbstractClassCodeGenerator.POJO_MODE,
									AbstractClassCodeGenerator.POJO_MODE_BUILDER));
			// Run
			dataModel
					.getTableByName("A_TABLE")
					.getColumnByName("ID")
					.addOption(new Option(AbstractClassCodeGenerator.AUTO_INCREMENT, "IDENTITY"));
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectPOJOModeBUILDWithSEQUENCE() {
			// Prepare
			String expected = getExpectedPOJOModeBuilder("gui.vaadin.go", "SEQUENCE");
			DataModel dataModel = readDataModel("Model.xml");
			dataModel
					.addOption(
							new Option(
									AbstractClassCodeGenerator.POJO_MODE,
									AbstractClassCodeGenerator.POJO_MODE_BUILDER));
			// Run
			dataModel
					.getTableByName("A_TABLE")
					.getColumnByName("ID")
					.addOption(new Option(AbstractClassCodeGenerator.AUTO_INCREMENT, "SEQUENCE"));
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}