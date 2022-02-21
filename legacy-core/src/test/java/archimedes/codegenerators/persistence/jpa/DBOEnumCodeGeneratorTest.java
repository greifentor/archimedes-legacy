package archimedes.codegenerators.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class DBOEnumCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private DBOEnumCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_DomainModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected();
			DataModel dataModel = readDataModel("Model.xml");
			dataModel
					.getDomainByName("Description")
					.addOption(new Option(AbstractClassCodeGenerator.ENUM, "ONE,TWO,THREE"));
			// Run
			String returned =
					unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getDomainByName("Description"));
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected() {
			String s = "package base.pack.age.name.persistence.entity;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO enum for descriptions.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"public enum DescriptionDBO {\n" + //
					"\n" + //
					"	ONE,\n" + //
					"	TWO,\n" + //
					"	THREE;\n" + //
					"\n" + //
					"}";
			return s;
		}

	}

	@Nested
	class TestsOfMethod_isToIgnoreFor_DomainModel {

		@Test
		void passADomainModelWithNoEnum_returnsTrue() {
			// Prepare
			DataModel dataModel = readDataModel("Model.xml");
			// Run & Check
			assertTrue(unitUnderTest.isToIgnoreFor(dataModel, dataModel.getDomainByName("Description")));
		}

		@Test
		void passADomainModelWithNoEnum_returnsFalse() {
			// Prepare
			DataModel dataModel = readDataModel("Model.xml");
			dataModel
					.getDomainByName("Description")
					.addOption(new Option(AbstractClassCodeGenerator.ENUM, "ONE,TWO,THREE"));
			// Run & Check
			assertFalse(unitUnderTest.isToIgnoreFor(dataModel, dataModel.getDomainByName("Description")));
		}

	}

}