package archimedes.codegenerators.gui.vaadin.component;

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
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class ButtonGridClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private ButtonGridClassCodeGenerator unitUnderTest;

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
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, null);
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(String prefix, String packageName, boolean suppressComment, String noKeyValue) {
			String s =
					"package " + BASE_PACKAGE_NAME + "." + (prefix != null ? prefix + "." : "") + packageName + ";\n" //
							+ "\n" //
							+ "import java.util.ArrayList;\n" //
							+ "import java.util.Arrays;\n" //
							+ "import java.util.List;\n" //
							+ "\n" //
							+ "import com.vaadin.flow.component.html.Label;\n" //
							+ "import com.vaadin.flow.component.orderedlayout.HorizontalLayout;\n" //
							+ "import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" //
							+ "\n" //
							+ "import lombok.Generated;\n" //
							+ "\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * A component for a grid of buttons.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s +=
					"@Generated\n" //
							+ "public class ButtonGrid extends VerticalLayout {\n" //
							+ "\n" //
							+ "	public ButtonGrid(int buttonsPerRow, Button... buttons) {\n" //
							+ "		this(buttonsPerRow, new ArrayList<>(Arrays.asList(buttons)));\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public ButtonGrid(int buttonsPerRow, List<Button> buttonList) {\n" //
							+ "		getStyle().set(\"-moz-border-radius\", \"4px\");\n" //
							+ "		getStyle().set(\"-webkit-border-radius\", \"4px\");\n" //
							+ "		getStyle().set(\"border-radius\", \"4px\");\n" //
							+ "		getStyle().set(\"border\", \"1px solid gray\");\n" //
							+ "		getStyle()\n" //
							+ "				.set(\n" //
							+ "						\"box-shadow\",\n" //
							+ "						\"10px 10px 20px #e4e4e4, -10px 10px 20px #e4e4e4, -10px -10px 20px #e4e4e4, 10px -10px 20px #e4e4e4\");\n" //
							+ "		while (!buttonList.isEmpty()) {\n" //
							+ "			add(createRow(buttonsPerRow, buttonList));\n" //
							+ "		}\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private HorizontalLayout createRow(int buttonsPerRow, List<Button> buttonList) {\n" //
							+ "		HorizontalLayout row = new HorizontalLayout();\n" //
							+ "		row.setMargin(false);\n" //
							+ "		row.setWidthFull();\n" //
							+ "		while (buttonsPerRow > 0 && !buttonList.isEmpty()) {\n" //
							+ "			Button button = buttonList.get(0);\n" //
							+ "			if (button == null) {\n" //
							+ "				Label label = new Label(\"\");\n" //
							+ "				label.setWidthFull();\n" //
							+ "				row.add(label);\n" //
							+ "			} else {\n" //
							+ "				row.add(button);\n" //
							+ "			}\n" //
							+ "			buttonList.remove(button);\n" //
							+ "			buttonsPerRow--;\n" //
							+ "		}\n" //
							+ "		while (buttonsPerRow > 0) {\n" //
							+ "			Label label = new Label(\"\");\n" //
							+ "			label.setWidthFull();\n" //
							+ "			row.add(label);\n" //
							+ "			buttonsPerRow--;\n" //
							+ "		}\n" //
							+ "		return row;\n" //
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
			dataModel.getTableByName("A_TABLE");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "off"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, null);
			// Check
			assertEquals(expected, returned);
		}

	}

}
