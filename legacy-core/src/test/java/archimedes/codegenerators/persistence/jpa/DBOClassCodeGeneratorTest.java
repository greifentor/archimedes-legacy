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
public class DBOClassCodeGeneratorTest {

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
			String expected = getExpected("persistence.entities");
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(String packageName) {
			return getExpected(null, packageName);
		}

		private String getExpected(String prefix, String packageName) {
			return "package " + BASE_PACKAGE_NAME + "." + (prefix != null ? prefix + "." : "") + packageName + ";\n" + //
					"\n" + //
					"import java.time.LocalDate;\n" + //
					"\n" + //
					"import javax.persistence.Column;\n" + //
					"import javax.persistence.Entity;\n" + //
					"import javax.persistence.Id;\n" + //
					"import javax.persistence.Table;\n" + //
					"\n" + //
					"import lombok.Data;\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.experimental.Accessors;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Accessors(chain = true)\n" + //
					"@Data\n" + //
					"@Generated\n" + //
					"@Entity(name = \"ATable\")\n" + //
					"@Table(name = \"A_TABLE\")\n" + //
					"public class ATableDBO {\n" + //
					"\n" + //
					"	@Id\n" + //
					"	@Column(name = \"ID\")\n" + //
					"	private long id;\n" + //
					"	@Column(name = \"ADate\")\n" + //
					"	private LocalDate aDate;\n" + //
					"	@Column(name = \"Description\")\n" + //
					"	private String description;\n" + //
					"\n" + //
					"}";
		}

		@Test
		void happyRunForASimpleObjectAndAlternatePackageName() {
			// Prepare
			String alternatePackageName = "alternate.name";
			String expected = getExpected(alternatePackageName);
			DataModel dataModel = readDataModel("Model.xml");
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
			String expected = getExpected(technicalContextName + ".persistence.entities");
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
			String expected = getExpected(prefix, "persistence.entities");
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.addOption(new Option(PersistenceJPANameGenerator.MODULE, prefix));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		private String getExpectedPOJOModeBuilder(String packageName, boolean autoincrement) {
			String s = "package " + BASE_PACKAGE_NAME + "." + packageName + ";\n" + //
					"\n" + //
					"import java.time.LocalDate;\n" + //
					"\n" + //
					"import javax.persistence.Column;\n" + //
					"import javax.persistence.Entity;\n";
			if (autoincrement) {
				s += "import javax.persistence.GeneratedValue;\n" + //
						"import javax.persistence.GenerationType;\n";
			}
			s += "import javax.persistence.Id;\n" + //
					"import javax.persistence.Table;\n" + //
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
			if (autoincrement) {
				s += "	@GeneratedValue(strategy = GenerationType.IDENTITY)\n";
			}
			return s + "	@Column(name = \"ID\")\n" + //
					"	private long id;\n" + //
					"	@Column(name = \"ADate\")\n" + //
					"	private LocalDate aDate;\n" + //
					"	@Column(name = \"Description\")\n" + //
					"	private String description;\n" + //
					"\n" + //
					"}";
		}

		@Test
		void happyRunForASimpleObjectPOJOModeBUILD() {
			// Prepare
			String expected = getExpectedPOJOModeBuilder("persistence.entities", true);
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
					.addOption(new Option(AbstractClassCodeGenerator.AUTOINCREMENT));
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}