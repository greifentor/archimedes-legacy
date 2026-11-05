package archimedes.ucg.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import archimedes.ucg.yaml.model.YamlArchimedesSection;
import archimedes.ucg.yaml.model.YamlAttribute;
import archimedes.ucg.yaml.model.YamlClassDefinition;
import archimedes.ucg.yaml.model.YamlEnumDefinition;
import archimedes.ucg.yaml.model.YamlModel;
import archimedes.ucg.yaml.model.YamlProperty;
import archimedes.ucg.yaml.model.YamlType;

public class UcgYamlModelReaderTest {

	static final String UCG_TEST_MODEL_FILE_NAME = "src/test/resources/ucg/test-model.yml";

	private UcgYamlModelReader unitUnderTest = new UcgYamlModelReader();

	static YamlModel createUcgTestModel() {
		YamlType uuidType = new YamlType("UUID", List.of(new YamlProperty("import", "java.util.UUID")));
		YamlType stringType = new YamlType("String", null);
		YamlProperty notNullable = new YamlProperty("nullable", "false");
		YamlClassDefinition author =
				new YamlClassDefinition(
						"Author",
						List.of(
								new YamlAttribute("id", uuidType, null, true, List.of(notNullable), false),
								new YamlAttribute("name", stringType, null, false, null, false)),
						null);
		YamlClassDefinition book =
				new YamlClassDefinition(
						"Book",
						List.of(
								new YamlAttribute("id", uuidType, null, true, List.of(notNullable), false),
								new YamlAttribute(
										"title",
										stringType,
										null,
										false,
										List.of(notNullable, new YamlProperty("editor-position", "1")),
										false),
								new YamlAttribute(
										"author",
										new YamlType("Author", List.of(new YamlProperty("project-type", "true"))),
										null,
										false,
										List.of(notNullable, new YamlProperty("editor-position", "2")),
										true)),
						List.of(new YamlProperty("order", "title DESC"), new YamlProperty("gui", "true")));
		return new YamlModel(
				null,
				List.of(author, book),
				List.of(new YamlEnumDefinition("BookType", List.of("HARDCOVER", "SOFTCOVER"))),
				null);
	}

	@Nested
	class TestsOfMethod_read_String {

		@Test
		void returnsTheModel_passingTheUcgTestModelFile() throws IOException {
			// Prepare
			YamlModel expected = createUcgTestModel();
			// Run
			YamlModel returned = unitUnderTest.read(UCG_TEST_MODEL_FILE_NAME);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class TestsOfMethod_readArchimedesSectionFromString_String {

		@Test
		void returnsAnEmptySection_passingAnEmptyString() {
			// Run
			YamlArchimedesSection returned = unitUnderTest.readArchimedesSectionFromString("");
			// Check
			assertEquals(new YamlArchimedesSection(), returned);
		}

		@Test
		void returnsTheSection_passingTheYamlOfASection() {
			// Prepare
			YamlArchimedesSection expected = UcgYamlModelWriterTest.createArchimedesSection();
			// Run
			YamlArchimedesSection returned =
					unitUnderTest.readArchimedesSectionFromString(UcgYamlModelWriterTest.ARCHIMEDES_SECTION_YAML);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void throwsAnException_passingANonNumericPosition() {
			// Prepare
			String yaml = "views:\n  - name: Main\n    tables:\n      - name: A\n        x: left\n";
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.readArchimedesSectionFromString(yaml));
		}

	}

	@Nested
	class TestsOfMethod_readFromString_String {

		@Test
		void returnsAnEmptyModel_passingAnEmptyString() {
			// Run
			YamlModel returned = unitUnderTest.readFromString("");
			// Check
			assertEquals(new YamlModel(), returned);
		}

		@Test
		void returnsTitleAndModelProperties_passingThem() {
			// Prepare
			YamlModel expected = new YamlModel("My Model", null, null, List.of(new YamlProperty("version", "1.0")));
			String yaml = "title: My Model\nproperties:\n  - name: version\n    value: 1.0\nunknown: ignored\n";
			// Run
			YamlModel returned = unitUnderTest.readFromString(yaml);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void returnsDefaultValueOfAnAttribute_passingIt() {
			// Prepare
			YamlModel expected =
					new YamlModel(
							null,
							List.of(
									new YamlClassDefinition(
											"A",
											List.of(new YamlAttribute("count", new YamlType("int", null), "42", false, null, false)),
											null)),
							null,
							null);
			String yaml =
					"classes:\n" //
							+ "  - name: A\n" //
							+ "    attributes:\n" //
							+ "      - name: count\n" //
							+ "        default-value: 42\n" //
							+ "        type:\n" //
							+ "          name: int\n";
			// Run
			YamlModel returned = unitUnderTest.readFromString(yaml);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void returnsEmptyLists_passingEmptyFlowSequences() {
			// Prepare
			YamlModel expected = new YamlModel(null, List.of(), Arrays.asList((YamlEnumDefinition) null), null);
			// Run
			YamlModel returned = unitUnderTest.readFromString("classes: []\nenums:\n  -\n");
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void throwsAnException_passingANonBooleanEnumFlag() {
			// Prepare
			String yaml = "classes:\n  - name: A\n    attributes:\n      - name: a\n        enum: maybe\n";
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.readFromString(yaml));
		}

		@Test
		void throwsAnException_passingAScalarForClasses() {
			// Prepare
			String yaml = "classes: Author";
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.readFromString(yaml));
		}

		@Test
		void throwsAnException_passingASequenceAsRoot() {
			// Prepare
			String yaml = "- a";
			// Run & Check
			assertThrows(YamlParseException.class, () -> unitUnderTest.readFromString(yaml));
		}

	}

}
