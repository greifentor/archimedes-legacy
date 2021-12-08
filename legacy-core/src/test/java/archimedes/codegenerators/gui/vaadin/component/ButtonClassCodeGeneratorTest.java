package archimedes.codegenerators.gui.vaadin.component;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.codegenerators.gui.vaadin.component.ButtonClassCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class ButtonClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private ButtonClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected(null, "gui.vaadin.component", false, "null");
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(String prefix, String packageName, boolean suppressComment, String noKeyValue) {
			String s =
					"package " + BASE_PACKAGE_NAME + "." + (prefix != null ? prefix + "." : "") + packageName + ";\n" //
							+ "\n" //
							+ "import lombok.Generated;\n" //
							+ "\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * An extended button for easy configuration.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s +=
					"@Generated\n" //
							+ "public class Button extends com.vaadin.flow.component.button.Button {\n" //
							+ "\n" //
							+ "	public Button(String text) {\n" //
							+ "		super(text);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private Button setStyle(String name, String value) {\n" //
							+ "		getStyle().set(name, value);\n" //
							+ "		return this;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public Button setBackgroundColor(String backgroundColor) {\n" //
							+ "		return setStyle(\"background-color\", backgroundColor);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public Button setBackgroundImage(String imageFileName) {\n" //
							+ "		return setStyle(\"background-image\", \"url('\" + imageFileName + \"')\");\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public Button setBorder(String border) {\n" //
							+ "		return setStyle(\"border\", border);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public Button setBorderColor(String borderColor) {\n" //
							+ "		return setStyle(\"border-color\", borderColor);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public Button setColor(String color) {\n" //
							+ "		return setStyle(\"color\", color);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "}";
			return s;
		}

		@Test
		void happyRunForASimpleObjectWithSuppressedComments() {
			// Prepare
			String expected = getExpected(null, "gui.vaadin.component", true, "null");
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