package archimedes.codegenerators.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
class PageParametersToPageableConverterClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private PageParametersToPageableConverterClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected(null, "persistence.converter", false, "null");
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel);
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(String prefix, String packageName, boolean suppressComment, String noKeyValue) {
			String s =
					"package " + BASE_PACKAGE_NAME + "." + (prefix != null ? prefix + "." : "") + packageName + ";\n" + //
							"\n" + //
							"import javax.inject.Named;\n" + //
							"\n" + //
							"import org.springframework.data.domain.PageRequest;\n" + //
							"import org.springframework.data.domain.Pageable;\n" + //
							"import org.springframework.data.domain.Sort.Direction;\n" + //
							"\n" + //
							"import base.pack.age.name.core.model.PageParameters;\n" + //
							"\n" + //
							"import lombok.Generated;\n" + //
							"\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * A converter to create a Pageable from a PageParameters object.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s +=
					"@Generated\n" //
							+ "@Named\n" //
							+ "public class PageParametersToPageableConverter {\n" //
							+ "\n" //
							+ "	public Pageable convert(PageParameters pageParameters) {\n" //
							+ "		if (pageParameters == null) {\n" //
							+ "			return null;\n" //
							+ "		}\n" //
							+ "		if (pageParameters.getSort() != null) {\n" //
							+ "			return PageRequest\n" //
							+ "					.of(\n" //
							+ "							pageParameters.getPageNumber(),\n" //
							+ "							pageParameters.getEntriesPerPage(),\n" //
							+ "							getDirection(pageParameters.getSort().getDirection()),\n" //
							+ "							pageParameters.getSort().getFieldNames());\n" //
							+ "		}\n" //
							+ "		return PageRequest.of(pageParameters.getPageNumber(), pageParameters.getEntriesPerPage());\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private Direction getDirection(PageParameters.Direction direction) {\n" //
							+ "		return direction == PageParameters.Direction.ASC ? Direction.ASC : Direction.DESC;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "}";
			return s;
		}

		@Test
		void happyRunForASimpleObjectWithSuppressedComments() {
			// Prepare
			String expected = getExpected(null, "persistence.converter", true, "null");
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "off"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel);
			// Check
			assertEquals(expected, returned);
		}

	}

}