package archimedes.codegenerators.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class ServiceImplClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private ServiceNameGenerator nameGenerator = new ServiceNameGenerator();

	@InjectMocks
	private ServiceImplClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Test
	void happyRunForASimpleObject() {
		// Prepare
		String expected = createExpected(false);
		DataModel dataModel = readDataModel("Model.xml");
		// Run
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
		// Check
		assertEquals(expected, returned);
	}

	private String createExpected(boolean suppressComment) {
		String expected = "package " + BASE_PACKAGE_NAME + ".core.service.impl;\n" + //
				"\n" + //
				"import javax.inject.Named;\n" + //
				"\n" + //
				"import lombok.Generated;\n" + //
				"\n";
		if (!suppressComment) {
			expected += "/**\n" +
					" * A service interface implementation for ATable management.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n";
		}
		expected += "@Generated\n" + //
				"@Named\n" + //
				"public class ATableServiceImpl extends ATableGeneratedServiceImpl {\n" + //
				"}";
		return expected;
	}

	@Test
	void happyRunForASimpleObject_NoComment() {
		// Prepare
		String expected = createExpected(true);
		DataModel dataModel = readDataModel("Model.xml");
		dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "Off"));
		// Run
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
		// Check
		assertEquals(expected, returned);
	}

	@Nested
	class TestsOfMethod_isToIgnoreFor_DataModel_TableModel {

		@Nested
		class Subclasses {

			@Test
			void passASubclassModel_returnsTrue() {
				// Prepare
				DataModel dataModel = readDataModel("Model.xml");
				TableModel table = dataModel.getTableByName("A_TABLE");
				table.addOption(new Option(AbstractClassCodeGenerator.SUBCLASS));
				// Run & Check
				assertTrue(unitUnderTest.isToIgnoreFor(dataModel, table));
			}

			@Test
			void passANoSubnlassModel_returnsFalse() {
				// Prepare
				DataModel dataModel = readDataModel("Model.xml");
				TableModel table = dataModel.getTableByName("A_TABLE");
				// Run & Check
				assertFalse(unitUnderTest.isToIgnoreFor(dataModel, table));
			}

			@Test
			void passAMemberModel_returnsTrue() {
				// Prepare
				ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
				DataModel dataModel = reader.read("src/test/resources/examples/dm/Example-BookStore.xml");
				TableModel table = dataModel.getTableByName("CHAPTER");
				// Run & Check
				assertTrue(unitUnderTest.isToIgnoreFor(dataModel, table));
			}

		}

	}

}