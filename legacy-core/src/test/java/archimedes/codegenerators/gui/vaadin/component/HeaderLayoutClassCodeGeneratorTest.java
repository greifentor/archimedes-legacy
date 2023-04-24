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
public class HeaderLayoutClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private HeaderLayoutClassCodeGenerator unitUnderTest;

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
							+ "import com.vaadin.flow.component.html.Label;\n" //
							+ "import com.vaadin.flow.component.orderedlayout.HorizontalLayout;\n" //
							+ "\n" //
							+ "import lombok.Generated;\n" //
							+ "\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * A layout for the page headers.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s +=
					"@Generated\n" //
							+ "public class HeaderLayout extends HorizontalLayout {\n" //
							+ "\n" //
							+ "	public enum HeaderLayoutMode {\n" //
							+ "		PLAIN,\n" //
							+ "		WRAPPED;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public HeaderLayout(Button buttonLogout, String labelText, HeaderLayoutMode mode) {\n" //
							+ "		this(null, buttonLogout, labelText, mode);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public HeaderLayout(Button buttonBack, Button buttonLogout, String labelText, HeaderLayoutMode mode) {\n" //
							+ "		if (mode == HeaderLayoutMode.WRAPPED) {\n" //
							+ "			HorizontalLayout wrapper = new HorizontalLayout();\n" //
							+ "			prepareLayout(wrapper);\n" //
							+ "			wrapper.add(getInnerLayout(buttonBack, buttonLogout, labelText));\n" //
							+ "			setWidthFull();\n" //
							+ "			setPadding(true);\n" //
							+ "			add(wrapper);\n" //
							+ "		} else {\n" //
							+ "			prepareLayout(this);\n" //
							+ "			add(getInnerLayout(buttonBack, buttonLogout, labelText));\n" //
							+ "		}\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private void prepareLayout(HorizontalLayout layout) {\n" //
							+ "		layout.setWidthFull();\n" //
							+ "		layout.setPadding(true);\n" //
							+ "		layout.getStyle().set(\"-moz-border-radius\", \"4px\");\n" //
							+ "		layout.getStyle().set(\"-webkit-border-radius\", \"4px\");\n" //
							+ "		layout.getStyle().set(\"border-radius\", \"4px\");\n" //
							+ "		layout.getStyle().set(\"border\", \"1px solid gray\");\n" //
							+ "		layout\n" //
							+ "				.getStyle()\n" //
							+ "				.set(\n" //
							+ "						\"box-shadow\",\n" //
							+ "						\"10px 10px 20px #e4e4e4, -10px 10px 20px #e4e4e4, -10px -10px 20px #e4e4e4, 10px -10px 20px #e4e4e4\");\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private HorizontalLayout getInnerLayout(Button buttonBack, Button buttonLogout, String labelText) {\n" //
							+ "		HorizontalLayout headerInner = new HorizontalLayout();\n" //
							+ "		headerInner.setWidthFull();\n" //
							+ "		headerInner.setMargin(false);\n" //
							+ "		Label label = new Label(labelText);\n" //
							+ "		label.setWidthFull();\n" //
							+ "		label.getStyle().set(\"display\", \"flex\");\n" //
							+ "		label.getStyle().set(\"align-items\", \"center\");\n" //
							+ "		label.getStyle().set(\"font-weight\", \"bold\");\n" //
							+ "		buttonLogout.setWidth(\"15%\");\n" //
							+ "		if (buttonBack == null) {\n" //
							+ "			headerInner.add(label, buttonLogout);\n" //
							+ "		} else {\n" //
							+ "			buttonBack.setWidth(\"15%\");\n" //
							+ "			headerInner.add(label, buttonLogout, buttonBack);\n" //
							+ "		}\n" //
							+ "		return headerInner;\n" //
							+ "	}\n" //
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
