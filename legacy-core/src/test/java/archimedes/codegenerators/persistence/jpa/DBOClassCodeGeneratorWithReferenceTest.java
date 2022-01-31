package archimedes.codegenerators.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.codegenerators.NameGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class DBOClassCodeGeneratorWithReferenceTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private DBOClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected("persistence.entity");
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
							"import javax.persistence.Column;\n" + //
							"import javax.persistence.Entity;\n" + //
							"import javax.persistence.Id;\n" + //
							"import javax.persistence.Table;\n" + //
							"\n" + //
							"import lombok.Data;\n" + //
							"import lombok.Generated;\n" + //
							"import lombok.experimental.Accessors;\n" + //
							"\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * A DBO for a_tables.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s += "@Accessors(chain = true)\n" + //
					"@Data\n" + //
					"@Generated\n" + //
					"@Entity(name = \"ATable\")\n" + //
					"@Table(name = \"A_TABLE\")\n" + //
					"public class ATableDBO {\n" + //
					"\n" + //
					"	@Id\n" + //
					"	@Column(name = \"ID\")\n" + //
					"	private Long id;\n";
			if (!refMode) {
				s += "	@Column(name = \"REF\"" + (refNotNull ? ", nullable = false" : "") + ")\n" + //
						"	private " + (refNotNull ? "long" : "Long") + " ref;\n";
			} else {
				s += "	@JoinColumn(name = \"REF\"" + //
						(refNotNull ? ", nullable = false" : "") + ", referencedColumnName = \"ID\")\n" + //
						"	@OneToOne(fetch = FetchType.EAGER, optional = false)\n" + //
						"	private AnotherTableDBO ref;\n";
			}
			s += "\n" + //
					"}";
			return s;
		}

		@Test
		void happyRunForASimpleObjectAndAlternatePackageName() {
			// Prepare
			String alternatePackageName = "alternate.name";
			String expected = getExpected(alternatePackageName);
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			dataModel
					.addOption(
							new Option(
									PersistenceJPANameGenerator.ALTERNATE_ENTITY_PACKAGE_NAME,
									alternatePackageName));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectAndTechnicalPackageName() {
			// Prepare
			String technicalContextName = "technical";
			String expected = getExpected(technicalContextName + ".persistence.entity");
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
			String expected = getExpected(prefix, "persistence.entity", false);
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
			String expected = getExpected(null, "persistence.entity", true);
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
			String expected = getExpected(null, "persistence.entity", false, true, false);
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
			String expected = getExpected(null, "persistence.entity", false, true, true);
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
			String s = "package " + BASE_PACKAGE_NAME + "." + packageName + ";\n" + //
					"\n" + //
					"import javax.persistence.Column;\n" + //
					"import javax.persistence.Entity;\n";
			if (!generatedValue.equals("")) {
				s += "import javax.persistence.GeneratedValue;\n" + //
						"import javax.persistence.GenerationType;\n";
			}
			s += "import javax.persistence.Id;\n";
			if (generatedValue.contains("SEQUENCE")) {
				s += "import javax.persistence.SequenceGenerator;\n";
			}
			s += "import javax.persistence.Table;\n" + //
					"\n" + //
					"import lombok.AllArgsConstructor;\n" + //
					"import lombok.Builder;\n" + //
					"import lombok.NoArgsConstructor;\n" + //
					"import lombok.Data;\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Builder\n" + //
					"@AllArgsConstructor\n" + //
					"@NoArgsConstructor\n" + //
					"@Data\n" + //
					"@Generated\n" + //
					"@Entity(name = \"ATable\")\n" + //
					"@Table(name = \"A_TABLE\")\n" + //
					"public class ATableDBO {\n" + //
					"\n" + //
					"	@Id\n";
			if (generatedValue.equals("IDENTITY")) {
				s += "	@GeneratedValue(strategy = GenerationType.IDENTITY)\n";
			} else if (generatedValue.equals("SEQUENCE")) {
				s +=
						"	@SequenceGenerator(allocationSize = 1, name = \"ATableSequence\", sequenceName = \"a_table_id_seq\")\n"
								+ //
								"	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = \"ATableSequence\")\n";
			}
			return s + "	@Column(name = \"ID\")\n" + //
					"	private Long id;\n" + //
					"	@Column(name = \"REF\")\n" + //
					"	private Long ref;\n" + //
					"\n" + //
					"}";
		}

		@Test
		void happyRunForASimpleObjectPOJOModeBUILDWithIDENTITY() {
			// Prepare
			String expected = getExpectedPOJOModeBuilder("persistence.entity", "IDENTITY");
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
			String expected = getExpectedPOJOModeBuilder("persistence.entity", "SEQUENCE");
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