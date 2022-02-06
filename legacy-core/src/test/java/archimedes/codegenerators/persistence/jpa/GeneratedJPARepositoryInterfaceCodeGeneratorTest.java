package archimedes.codegenerators.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class GeneratedJPARepositoryInterfaceCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private GeneratedJPARepositoryInterfaceCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected("persistence.repository");
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(String packageName) {
			return getExpected(null, packageName, null);
		}

		private String getExpected(String prefix, String packageName, String findByLine) {
			String s =
					"package " + BASE_PACKAGE_NAME + "." + (prefix != null ? prefix + "." : "") + packageName + ";\n" + //
							"\n" + //
							"import org.springframework.data.jpa.repository.JpaRepository;\n" + //
							"import org.springframework.stereotype.Repository;\n" + //
							"\n" + //
							"import " + BASE_PACKAGE_NAME + ".persistence.entity.ATableDBO;\n" + //
							"import lombok.Generated;\n";
			if (findByLine != null) {
				if (findByLine.startsWith("List")) {
					s += "import java.util.List;\n";
				} else if (findByLine.startsWith("Optional")) {
					s += "import java.util.Optional;\n";
				}
			}
			s +=
							"\n" + //
							"/**\n" + //
							" * A generated JPA repository for a_tables.\n" + //
							" *\n" + //
							" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
							" */\n" + //
							"@Generated\n" + //
							"@Repository\n" + //
							"public interface ATableGeneratedDBORepository extends JpaRepository<ATableDBO, Long> {\n";
			if (findByLine != null) {
				s += "\n" + //
						"	" + findByLine + ";\n" + //
						"\n";
			}
			s += "}";
			return s;
		}

		@Test
		void happyRunForASimpleObjectWithFindBy_SimpleType() {
			// Prepare
			String expected =
					getExpected(
							null,
							"persistence.repository",
							"List<ATableDBO> findAllByDescription(String description)");
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.getColumnByName("Description").addOption(new Option("FIND_BY"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithFindBy_SimpleType_Unique() {
			// Prepare
			String expected =
					getExpected(
							null,
							"persistence.repository",
							"Optional<ATableDBO> findByDescription(String description)");
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.getColumnByName("Description").addOption(new Option("FIND_BY"));
			table.getColumnByName("Description").setUnique(true);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithFindBy_ReferenceType() {
			// Prepare
			String expected = "package base.pack.age.name.persistence.repository;\n" + //
					"\n" + //
					"import org.springframework.data.jpa.repository.JpaRepository;\n" + //
					"import org.springframework.stereotype.Repository;\n" + //
					"\n" + //
					"import base.pack.age.name.persistence.entity.ATableDBO;\n" + //
					"import lombok.Generated;\n" + //
					"import java.util.Optional;\n" + //
					"\n" + //
					"import base.pack.age.name.persistence.entity.AnotherTableDBO;\n" + //
					"\n" + //
					"/**\n" + //
					" * A generated JPA repository for a_tables.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Repository\n" + //
					"public interface ATableGeneratedDBORepository extends JpaRepository<ATableDBO, Long> {\n" + //
					"\n" + //
					"	Optional<ATableDBO> findByRef(AnotherTableDBO ref);\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			dataModel
					.addOption(
							new Option(
									AbstractClassCodeGenerator.REFERENCE_MODE,
									AbstractClassCodeGenerator.REFERENCE_MODE_OBJECT));
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.getColumnByName("REF").addOption(new Option("FIND_BY"));
			table.getColumnByName("REF").setUnique(true);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

	}

}