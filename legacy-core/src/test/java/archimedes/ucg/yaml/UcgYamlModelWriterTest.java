package archimedes.ucg.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import archimedes.ucg.yaml.model.YamlArchimedesColor;
import archimedes.ucg.yaml.model.YamlArchimedesPoint;
import archimedes.ucg.yaml.model.YamlArchimedesRelation;
import archimedes.ucg.yaml.model.YamlArchimedesSection;
import archimedes.ucg.yaml.model.YamlArchimedesTable;
import archimedes.ucg.yaml.model.YamlArchimedesTablePosition;
import archimedes.ucg.yaml.model.YamlArchimedesView;
import archimedes.ucg.yaml.model.YamlAttribute;
import archimedes.ucg.yaml.model.YamlClassDefinition;
import archimedes.ucg.yaml.model.YamlEnumDefinition;
import archimedes.ucg.yaml.model.YamlModel;
import archimedes.ucg.yaml.model.YamlProperty;
import archimedes.ucg.yaml.model.YamlType;

public class UcgYamlModelWriterTest {

	static final String ARCHIMEDES_SECTION_YAML =
			"title: Library\n" //
					+ "views:\n" //
					+ "  - name: Main\n" //
					+ "    main-view: true\n" //
					+ "    show-referenced-columns: true\n" //
					+ "    tables:\n" //
					+ "      - name: Author\n" //
					+ "        x: 100\n" //
					+ "        y: 200\n" //
					+ "    relations:\n" //
					+ "      - table: Book\n" //
					+ "        column: author\n" //
					+ "        direction-0: LEFT\n" //
					+ "        offset-0: 5\n" //
					+ "        direction-1: RIGHT\n" //
					+ "        offset-1: 7\n" //
					+ "        points:\n" //
					+ "          - x: 300\n" //
					+ "            y: 150\n" //
					+ "  - name: Details\n" //
					+ "    description: Only books\n" //
					+ "    hide-technical-fields: true\n" //
					+ "    hide-transient-fields: true\n" //
					+ "tables:\n" //
					+ "  - name: Author\n" //
					+ "    background-color: hellblau\n" //
					+ "    font-color: '#000000'\n" //
					+ "colors:\n" //
					+ "  - name: hellblau\n" //
					+ "    red: 200\n" //
					+ "    green: 220\n" //
					+ "    blue: 255\n";

	private UcgYamlModelWriter unitUnderTest = new UcgYamlModelWriter();

	static YamlArchimedesSection createArchimedesSection() {
		return new YamlArchimedesSection(
				"Library",
				List
						.of(
								new YamlArchimedesView(
										"Main",
										null,
										true,
										true,
										false,
										false,
										List.of(new YamlArchimedesTablePosition("Author", 100, 200)),
										List
												.of(
														new YamlArchimedesRelation(
																"Book",
																"author",
																"LEFT",
																5,
																"RIGHT",
																7,
																List.of(new YamlArchimedesPoint(300, 150))))),
								new YamlArchimedesView("Details", "Only books", false, false, true, true, null, null)),
				List.of(new YamlArchimedesTable("Author", "hellblau", "#000000")),
				List.of(new YamlArchimedesColor("hellblau", 200, 220, 255)));
	}

	@Nested
	class TestsOfMethod_toYamlString_YamlArchimedesSection {

		@Test
		void returnsAnEmptyString_passingAnEmptySection() {
			// Run
			String returned = unitUnderTest.toYamlString(new YamlArchimedesSection());
			// Check
			assertEquals("", returned);
		}

		@Test
		void returnsTheYaml_passingASectionWithViewsTablesAndColors() {
			// Run
			String returned = unitUnderTest.toYamlString(createArchimedesSection());
			// Check
			assertEquals(ARCHIMEDES_SECTION_YAML, returned);
		}

	}

	@Nested
	class TestsOfMethod_write_YamlArchimedesSection_String {

		@Test
		void writesTheYamlToTheFile_passingASectionAndAFileName(@TempDir Path tempDir) throws IOException {
			// Prepare
			Path file = tempDir.resolve("model.archimedes.yml");
			// Run
			unitUnderTest.write(createArchimedesSection(), file.toString());
			// Check
			assertEquals(ARCHIMEDES_SECTION_YAML, Files.readString(file, StandardCharsets.UTF_8));
		}

	}

	@Nested
	class TestsOfMethod_toYamlString_YamlModel {

		@Test
		void returnsAnEmptyString_passingAnEmptyModel() {
			// Run
			String returned = unitUnderTest.toYamlString(new YamlModel());
			// Check
			assertEquals("", returned);
		}

		@Test
		void returnsTheUcgYaml_passingAModel() {
			// Prepare
			String expected =
					"title: Library\n" //
							+ "properties:\n" //
							+ "  - name: version\n" //
							+ "    value: 1.0\n" //
							+ "classes:\n" //
							+ "  - name: Book\n" //
							+ "    properties:\n" //
							+ "      - name: order\n" //
							+ "        value: title DESC\n" //
							+ "    attributes:\n" //
							+ "      - name: id\n" //
							+ "        enum: true\n" //
							+ "        properties:\n" //
							+ "          - name: nullable\n" //
							+ "            value: false\n" //
							+ "        type:\n" //
							+ "          name: UUID\n" //
							+ "          properties:\n" //
							+ "            - name: import\n" //
							+ "              value: java.util.UUID\n" //
							+ "      - name: author\n" //
							+ "        default-value: unknown\n" //
							+ "        reference: true\n" //
							+ "        type:\n" //
							+ "          name: Author\n" //
							+ "enums:\n" //
							+ "  - name: BookType\n" //
							+ "    identifiers:\n" //
							+ "      - HARDCOVER\n" //
							+ "      - SOFTCOVER\n";
			YamlModel model =
					new YamlModel(
							"Library",
							List.of(
									new YamlClassDefinition(
											"Book",
											List.of(
													new YamlAttribute(
															"id",
															new YamlType("UUID", List.of(new YamlProperty("import", "java.util.UUID"))),
															null,
															true,
															List.of(new YamlProperty("nullable", "false")),
															false),
													new YamlAttribute("author", new YamlType("Author", null), "unknown", false, null, true)),
											List.of(new YamlProperty("order", "title DESC")))),
							List.of(new YamlEnumDefinition("BookType", List.of("HARDCOVER", "SOFTCOVER"))),
							List.of(new YamlProperty("version", "1.0")));
			// Run
			String returned = unitUnderTest.toYamlString(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void returnsQuotedValues_passingValuesWithSpecialCharacters() {
			// Prepare
			String expected =
					"properties:\n" //
							+ "  - name: ''\n" //
							+ "    value: 'a: b'\n" //
							+ "  - name: '- x'\n" //
							+ "    value: 'it''s # here'\n" //
							+ "  - name: 'null'\n" //
							+ "    value: \"line1\\nline2 \\\"q\\\"\"\n" //
							+ "  - name: ' padded '\n" //
							+ "    value: '~'\n";
			YamlModel model =
					new YamlModel(
							null,
							null,
							null,
							List.of(
									new YamlProperty("", "a: b"),
									new YamlProperty("- x", "it's # here"),
									new YamlProperty("null", "line1\nline2 \"q\""),
									new YamlProperty(" padded ", "~")));
			// Run
			String returned = unitUnderTest.toYamlString(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void returnsEmptyFlowSequencesAndEmptyItems_passingEmptyListsAndNullItems() {
			// Prepare
			String expected = "classes: []\nenums:\n  -\n";
			YamlModel model = new YamlModel(null, List.of(), Arrays.asList((YamlEnumDefinition) null), null);
			// Run
			String returned = unitUnderTest.toYamlString(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void returnsAYamlWhichIsReadToAnEqualModel_passingTheUcgTestModel() {
			// Prepare
			YamlModel model = UcgYamlModelReaderTest.createUcgTestModel();
			// Run
			String returned = unitUnderTest.toYamlString(model);
			// Check
			assertEquals(model, new UcgYamlModelReader().readFromString(returned));
		}

		@Test
		void returnsAYamlWhichIsReadToAnEqualModel_passingValuesWithSpecialCharacters() {
			// Prepare
			YamlModel model =
					new YamlModel(
							"#title: x",
							null,
							List.of(new YamlEnumDefinition("E", List.of("'quoted'", "[bracket]", "tab\there"))),
							List.of(new YamlProperty("key:", "\"double\" \\ back"), new YamlProperty("ctrl", "\u0001")));
			// Run
			String returned = unitUnderTest.toYamlString(model);
			// Check
			assertEquals(model, new UcgYamlModelReader().readFromString(returned));
		}

	}

	@Nested
	class TestsOfMethod_write_YamlModel_String {

		@Test
		void writesTheYamlToTheFile_passingAModelAndAFileName(@TempDir Path tempDir) throws IOException {
			// Prepare
			YamlModel model = UcgYamlModelReaderTest.createUcgTestModel();
			Path file = tempDir.resolve("model.yml");
			// Run
			unitUnderTest.write(model, file.toString());
			// Check
			assertEquals(unitUnderTest.toYamlString(model), Files.readString(file, StandardCharsets.UTF_8));
		}

	}

}
