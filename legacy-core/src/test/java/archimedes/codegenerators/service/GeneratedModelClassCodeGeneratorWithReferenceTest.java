package archimedes.codegenerators.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.codegenerators.NameGenerator;
import archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
class GeneratedModelClassCodeGeneratorWithReferenceTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private GeneratedModelClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected("core.model");
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(String packageName) {
			return getExpected(null, packageName, false);
		}

		private String getExpected(String prefix, String packageName, boolean suppressComment) {
			return getExpected(prefix, packageName, suppressComment, false, false);
		}

		private String getExpected(String prefix, String packageName, boolean suppressComment, boolean refNotNull,
				boolean refMode) {
			String s =
					"package " + BASE_PACKAGE_NAME + "." + (prefix != null ? prefix + "." : "") + packageName + ";\n" + //
							"\n" + //
							"import lombok.Data;\n" + //
							"import lombok.Generated;\n" + //
							"import lombok.experimental.Accessors;\n" + //
							"\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * A model for a_tables.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s += "@Accessors(chain = true)\n" + //
					"@Data\n" + //
					"@Generated\n" + //
					"public abstract class GeneratedATable<T extends ATable> {\n" + //
					"\n" + //
					"	public static final String ID = \"ID\";\n" + //
					"	public static final String REF = \"REF\";\n" + //
					"\n" + //
					"	private Long id;\n";
			String refType = refMode ? "AnotherTable" : (refNotNull ? "long" : "Long");
			s += "	private " + refType + " ref;\n";
			s += "\n" + //
					"	protected abstract T self();\n" + //
					"\n" + //
					"	public T setId(Long id) {\n" + //
					"		this.id = id;\n" + //
					"		return self();\n" + //
					"	}\n" + //
					"\n" + //
					"	public T setRef(" + refType + " ref) {\n" + //
					"		this.ref = ref;\n" + //
					"		return self();\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			return s;
		}

		@Test
		void happyRunForASimpleObjectAndAlternatePackageName() {
			// Prepare
			String alternatePackageName = "alternate.name";
			String expected = getExpected(alternatePackageName);
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			dataModel.addOption(new Option(ServiceNameGenerator.ALTERNATE_MODEL_PACKAGE_NAME, alternatePackageName));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectAndTechnicalPackageName() {
			// Prepare
			String technicalContextName = "technical";
			String expected = getExpected(technicalContextName + ".core.model");
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
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
			String expected = getExpected(prefix, "core.model", false);
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
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
			String expected = getExpected(null, "core.model", true);
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
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
			String expected = getExpected(null, "core.model", false, true, false);
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.getColumnByName("REF").setNotNull(true);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithAReferenceField() {
			// Prepare
			String expected = getExpected(null, "core.model", false, true, true);
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			dataModel
					.addOption(
							new Option(
									AbstractClassCodeGenerator.REFERENCE_MODE,
									AbstractClassCodeGenerator.REFERENCE_MODE_OBJECT));
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.getColumnByName("REF").setNotNull(true);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		private String getExpectedPOJOModeBuilder(String packageName, String generatedValue) {
			return "package " + BASE_PACKAGE_NAME + "." + packageName + ";\n" + //
					"\n" + //
					"import lombok.AllArgsConstructor;\n" + //
					"import lombok.Builder;\n" + //
					"import lombok.NoArgsConstructor;\n" + //
					"import lombok.Data;\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"/**\n" + //
					" * A model for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Builder\n" + //
					"@AllArgsConstructor\n" + //
					"@NoArgsConstructor\n" + //
					"@Data\n" + //
					"@Generated\n" + //
					"public abstract class GeneratedATable<T extends ATable> {\n" + //
					"\n" + //
					"	public static final String ID = \"ID\";\n" + //
					"	public static final String REF = \"REF\";\n" + //
					"\n" + //
					"	private Long id;\n" + //
					"	private Long ref;\n" + //
					"\n" + //
					"	protected abstract T self();\n" + //
					"\n" + //
					"	public T setId(Long id) {\n" + //
					"		this.id = id;\n" + //
					"		return self();\n" + //
					"	}\n" + //
					"\n" + //
					"	public T setRef(Long ref) {\n" + //
					"		this.ref = ref;\n" + //
					"		return self();\n" + //
					"	}\n" + //
					"\n" + //
					"}";
		}

		@Test
		void happyRunForASimpleObjectPOJOModeBUILDWithIDENTITY() {
			// Prepare
			String expected = getExpectedPOJOModeBuilder("core.model", "IDENTITY");
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
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
			String expected = getExpectedPOJOModeBuilder("core.model", "SEQUENCE");
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
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