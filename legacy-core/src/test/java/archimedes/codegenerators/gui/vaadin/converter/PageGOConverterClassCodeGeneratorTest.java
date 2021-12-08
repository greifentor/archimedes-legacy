package archimedes.codegenerators.gui.vaadin.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.codegenerators.gui.vaadin.converter.PageGOConverterClassCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
class PageGOConverterClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private PageGOConverterClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected(null, "gui.vaadin.converter", false, "null");
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(String prefix, String packageName, boolean suppressComment, String noKeyValue) {
			String s =
					"package " + BASE_PACKAGE_NAME + "." + (prefix != null ? prefix + "." : "") + packageName + ";\n" + //
							"\n" + //
							"import java.util.stream.Collectors;\n" + //
							"\n" + //
							"import base.pack.age.name.core.model.Page;\n" + //
							"import base.pack.age.name.gui.vaadin.go.converter.PageGO;\n" + //
							"\n" + //
							"import lombok.AllArgsConstructor;\n" + //
							"import lombok.Generated;\n" + //
							"\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * A class to convert a service page to a GUI web layer page object.\n" + //
						" *\n" + //
						" * @param <GO>    The type of the GO's which are representing model objects in the " + //
						"GUI web layer.\n" + //
						" * @param <MODEL> The type of the service layer model class.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s += "@AllArgsConstructor\n" + //
					"@Generated\n" + //
					"public class PageGOConverter<GO, MODEL> {\n" + //
					"\n" + //
					"	private final ToGOConverter<GO, MODEL> toGOConverter;\n" + //
					"\n" + //
					"	public PageGO<GO> toGO(Page<MODEL> page) {\n" + //
					"		if (page == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new PageGO<GO>()\n" + //
					"				.setEntries(page.getEntries().stream().map(toGOConverter::toGO).collect(Collectors"
					+ //
					".toList()))\n" + //
					"				.setEntriesPerPage(page.getEntriesPerPage())\n" + //
					"				.setEntriesTotal(page.getEntriesTotal());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			return s;
		}

		@Test
		void happyRunForASimpleObjectWithSuppressedComments() {
			// Prepare
			String expected = getExpected(null, "gui.vaadin.converter", true, "null");
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "off"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}