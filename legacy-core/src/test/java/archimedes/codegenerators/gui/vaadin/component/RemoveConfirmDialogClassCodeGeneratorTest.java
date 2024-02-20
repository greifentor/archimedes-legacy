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
public class RemoveConfirmDialogClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private RemoveConfirmDialogClassCodeGenerator unitUnderTest;

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
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel);
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(String prefix, String packageName, boolean suppressComment, String noKeyValue) {
			String s =
					"package base.pack.age.name.gui.vaadin.component;\n" //
							+ "\n" //
							+ "import com.vaadin.flow.component.dialog.Dialog;\n" //
							+ "import com.vaadin.flow.component.html.Label;\n" //
							+ "import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;\n" //
							+ "import com.vaadin.flow.component.orderedlayout.HorizontalLayout;\n" //
							+ "\n" //
							+ "import base.pack.age.name.core.service.localization.ResourceManager;\n" //
							+ "import base.pack.age.name.gui.SessionData;\n" //
							+ "\n" //
							+ "";
			if (!suppressComment) {
				s +=
						"/**\n" //
								+ " * A dialog to confirm data record remove actions.\n" //
								+ " *\n" //
								+ " * " + AbstractCodeGenerator.GENERATED_CODE + "\n" //
								+ " */\n";
			}
			s +=
					"@Generated\n" //
							+ "public class RemoveConfirmDialog extends Dialog {\n" //
							+ "\n" //
							+ "	public interface Observer {\n" //
							+ "\n" //
							+ "		void confirmed();\n" //
							+ "\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private Button buttonCancel;\n" //
							+ "	private Button buttonConfirm;\n" //
							+ "	private Observer observer;\n" //
							+ "\n" //
							+ "	public RemoveConfirmDialog(ButtonFactory buttonFactory, Observer observer, ResourceManager resourceManager,\n" //
							+ "			SessionData session) {\n" //
							+ "		this.observer = observer;\n" //
							+ "		buttonCancel =\n" //
							+ "				buttonFactory\n" //
							+ "						.createButton(\n" //
							+ "								resourceManager\n" //
							+ "										.getLocalizedString(\n" //
							+ "												\"RemoveConfirmDialog.button.cancel.label\",\n" //
							+ "												session.getLocalization()));\n" //
							+ "		buttonCancel.addClickListener(event -> cancel());\n" //
							+ "		buttonCancel.setWidth(\"40%\");\n" //
							+ "		buttonConfirm =\n" //
							+ "				buttonFactory\n" //
							+ "						.createButton(\n" //
							+ "								resourceManager\n" //
							+ "										.getLocalizedString(\n" //
							+ "												\"RemoveConfirmDialog.button.confirm.label\",\n" //
							+ "												session.getLocalization()));\n" //
							+ "		buttonConfirm.addClickListener(event -> confirmed());\n" //
							+ "		buttonConfirm.setWidth(\"20%\");\n" //
							+ "		Label label = new Label(\"\");\n" //
							+ "		label.setWidth(\"40%\");\n" //
							+ "		HorizontalLayout buttonLayout = new HorizontalLayout();\n" //
							+ "		buttonLayout.add(label, buttonConfirm, buttonCancel);\n" //
							+ "		buttonLayout.setWidthFull();\n" //
							+ "		add(\n" //
							+ "				new Label(\n" //
							+ "						resourceManager\n" //
							+ "								.getLocalizedString(\"RemoveConfirmDialog.message.label\", session.getLocalization())),\n" //
							+ "				new Label(\"\"),\n" //
							+ "				buttonLayout);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public void cancel() {\n" //
							+ "		close();\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public void confirmed() {\n" //
							+ "		if (observer != null) {\n" //
							+ "			observer.confirmed();\n" //
							+ "		}\n" //
							+ "		close();\n" //
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
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "off"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel);
			// Check
			assertEquals(expected, returned);
		}

	}

}
