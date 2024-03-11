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
public class SelectionDialogClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private SelectionDialogClassCodeGenerator unitUnderTest;

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
							+ "import java.util.stream.Collectors;\n" //
							+ "import java.util.stream.Stream;\n" //
							+ "\n" //
							+ "import com.vaadin.flow.component.combobox.ComboBox;\n" //
							+ "import com.vaadin.flow.component.dialog.Dialog;\n" //
							+ "\n" //
							+ "import base.pack.age.name.core.service.localization.ResourceManager;\n" //
							+ "import base.pack.age.name.gui.SessionData;\n" //
							+ "\n" //
							+ "import lombok.Generated;\n" //
							+ "\n";
			if (!suppressComment) {
				s +=
						"/**\n" //
								+ " * A simple selection dialog.\n" //
								+ " *\n" //
								+ " * " + AbstractCodeGenerator.GENERATED_CODE + "\n" //
								+ " */\n";
			}
			s +=
					"@Generated\n" //
							+ "public class SelectionDialog extends Dialog {\n" //
							+ "\n" //
							+ "	public interface Observer {\n" //
							+ "\n" //
							+ "		void select(Selectable selectable);\n" //
							+ "\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public interface Selectable {\n" //
							+ "\n" //
							+ "		String getLabel();\n" //
							+ "\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private Button buttonCancel;\n" //
							+ "	private Button buttonSelect;\n" //
							+ "	private ComboBox<Selectable> comboBoxSelectable;\n" //
							+ "	private Observer observer;\n" //
							+ "\n" //
							+ "	public SelectionDialog(ButtonFactory buttonFactory, Observer observer, ResourceManager resourceManager,\n" //
							+ "			SessionData session, Selectable... selectables) {\n" //
							+ "		this.observer = observer;\n" //
							+ "		buttonCancel =\n" //
							+ "				buttonFactory\n" //
							+ "						.createButton(\n" //
							+ "								resourceManager\n" //
							+ "										.getLocalizedString(\n" //
							+ "												\"SelectionDialog.button.cancel.label\",\n" //
							+ "												session.getLocalization()));\n" //
							+ "		buttonCancel.addClickListener(event -> cancel());\n" //
							+ "		buttonCancel.setWidthFull();\n" //
							+ "		buttonSelect =\n" //
							+ "				buttonFactory\n" //
							+ "						.createButton(\n" //
							+ "								resourceManager\n" //
							+ "										.getLocalizedString(\n" //
							+ "												\"SelectionDialog.button.select.label\",\n" //
							+ "												session.getLocalization()));\n" //
							+ "		buttonSelect.addClickListener(event -> select(comboBoxSelectable.getValue()));\n" //
							+ "		buttonSelect.setWidthFull();\n" //
							+ "		comboBoxSelectable = new ComboBox<Selectable>();\n" //
							+ "		comboBoxSelectable.setItemLabelGenerator(selectable -> selectable.getLabel());\n" //
							+ "		comboBoxSelectable.setItems(getItemsOrdered(selectables));\n" //
							+ "		comboBoxSelectable.setWidthFull();\n" //
							+ "		add(comboBoxSelectable, buttonCancel, buttonSelect);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private Selectable[] getItemsOrdered(Selectable... selectables) {\n" //
							+ "		return Stream\n" //
							+ "				.of(selectables)\n" //
							+ "				.sorted((s0, s1) -> s0.getLabel().compareTo(s1.getLabel()))\n" //
							+ "				.collect(Collectors.toList())\n" //
							+ "				.toArray(new Selectable[selectables.length]);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public void cancel() {\n" //
							+ "		close();\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public void select(Selectable selected) {\n" //
							+ "		if (observer != null) {\n" //
							+ "			observer.select(selected);\n" //
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
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, null);
			// Check
			assertEquals(expected, returned);
		}

	}

}
