package archimedes.codegenerators.localization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class ResourceManagerInterfaceCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private LocalizationNameGenerator nameGenerator = new LocalizationNameGenerator();

	@InjectMocks
	private ResourceManagerInterfaceCodeGenerator unitUnderTest;

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
		String expected = "package " + BASE_PACKAGE_NAME + ".core.service.localization;\n" + //
				"\n" + //
				"import " + BASE_PACKAGE_NAME + ".core.model.localization.LocalizationSO;\n" + //
				"\n";
		if (!suppressComment) {
			expected += "/**\n" + //
					" * An interface for resource managers.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n";
		}
		expected +=
				"public interface ResourceManager {\n" //
						+ "\n" //
						+ "	/**\n" //
						+ "	 * Returns a localized string for the passed resource id string (standard localization).\n" //
						+ "	 * \n" //
						+ "	 * @param resourceId The resource id string of the requested resource.\n" //
						+ "	 * @return The localized string of the requested resource or the resource id if no localized string is found for the\n" //
						+ "	 *         resource id.\n" //
						+ "	 */\n" //
						+ "	String getLocalizedString(String resourceId);\n" //
						+ "\n" //
						+ "	/**\n" //
						+ "	 * Returns a localized string for the passed resource id string.\n" //
						+ "	 * \n" //
						+ "	 * @param resourceId   The resource id string of the requested resource.\n" //
						+ "	 * @param localization The localization which the string is to return for.\n" //
						+ "	 * @return The localized string of the requested resource or the resource id if no localized string is found for the\n" //
						+ "	 *         resource id.\n" //
						+ "	 */\n" //
						+ "	String getLocalizedString(String resourceId, LocalizationSO localization);\n" //
						+ "\n" //
						+ "}";
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

}