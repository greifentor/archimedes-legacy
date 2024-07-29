package archimedes.codegenerators.service;

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
class PageParametersClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private ServiceNameGenerator nameGenerator = new ServiceNameGenerator();

	@InjectMocks
	private PageParametersClassCodeGenerator unitUnderTest;

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
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel);
		// Check
		assertEquals(expected, returned);
	}

	private String createExpected(boolean suppressComment) {
		String expected =
				"package base.pack.age.name.core.model;\n" //
						+ "\n" //
						+ "import lombok.Data;\n" //
						+ "import lombok.Generated;\n" //
						+ "import lombok.experimental.Accessors;\n" //
						+ "\n";
		if (!suppressComment) {
			expected +=
					"/**\n" //
							+ " * A page parameters to limit page access.\n" //
							+ " *\n" //
							+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
							+ " */\n";
		}
		expected +=
				"@Accessors(chain = true)\n" //
						+ "@Data\n" //
						+ "@Generated\n" //
						+ "public class PageParameters {\n" //
						+ "\n" //
						+ "	public enum Direction {\n" //
						+ "		ASC,\n" //
						+ "		DESC;\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	@Accessors(chain = true)\n" //
						+ "	@Data\n" //
						+ "	public static class Sort {\n" //
						+ "		private Direction direction;\n" //
						+ "		private String[] fieldNames;\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	private int pageNumber;\n" //
						+ "	private int entriesPerPage;\n" //
						+ "	private Sort sort;\n" //
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
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel);
		// Check
		assertEquals(expected, returned);
	}
}