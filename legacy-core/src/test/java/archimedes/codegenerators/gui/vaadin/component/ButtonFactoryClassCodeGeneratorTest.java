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
public class ButtonFactoryClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private ButtonFactoryClassCodeGenerator unitUnderTest;

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
					"package " + BASE_PACKAGE_NAME + "." + (prefix != null ? prefix + "." : "") + packageName + ";\n" + //
							"\n" + //
							"import java.util.Optional;\n" + //
							"import java.util.function.Consumer;\n" + //
							"import java.util.function.Supplier;\n" + //
							"\n" + //
							"import javax.inject.Named;\n" + //
							"\n" + //
							"import org.apache.logging.log4j.Logger;\n" + //
							"\n" + //
							"import com.vaadin.flow.component.ClickEvent;\n" + //
							"import com.vaadin.flow.component.UI;\n" + //
							"import com.vaadin.flow.router.QueryParameters;\n" + //
							"\n" + //
							"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
							"import base.pack.age.name.gui.SessionData;\n" + //
							"import base.pack.age.name.gui.vaadin.ApplicationStartView;\n" + //
							"import lombok.Generated;\n" + //
							"import lombok.RequiredArgsConstructor;\n" + //
							"\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * A button factory.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s += "@Generated\n" + //
					"@Named\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class ButtonFactory {\n" + //
					"\n" + //
					"	private final ButtonFactoryConfiguration buttonFactoryConfiguration;\n" + //
					"\n" + //
					"	public Button createButton(String text) {\n" + //
					"		Button button = new Button(text)\n" + //
					"				.setBackgroundColor(\"white\")\n" + //
					"				.setBorder(\"solid 1px\")\n" + //
					"				.setBorderColor(buttonFactoryConfiguration.getButtonEnabledBorderColor())\n" + //
					"				.setColor(\"black\")\n" + //
					"				.setBackgroundImage(buttonFactoryConfiguration.getButtonEnabledBackgroundFileName());\n"
					+ //
					"		return button;\n" + //
					"	}\n" + //
					"\n" + //
					"	public Button createAddButton(ResourceManager resourceManager, Consumer<ClickEvent<?>> action,\n"
					+ //
					"			SessionData sessionData) {\n" + //
					"		return createResourcedButton(resourceManager, \"commons.button.add.text\", action, sessionData);\n"
					+ //
					"	}\n" + //
					"\n" + //
					"	public Button createBackButton(ResourceManager resourceManager, Supplier<Optional<UI>> uiSupplier,\n"
					+ //
					"			String urlBack, SessionData sessionData) {\n" + //
					"		Button buttonBack =\n" + //
					"				createButton(\n" + //
					"						resourceManager.getLocalizedString(\"commons.button.back.text\", sessionData.getLocalization()));\n"
					+ //
					"		buttonBack.addClickListener(event -> uiSupplier.get().ifPresent(ui -> ui.navigate(urlBack)));\n"
					+ //
					"		return buttonBack;\n" + //
					"	}\n" + //
					"\n" + //
					"	public Button createBackButton(ResourceManager resourceManager, Supplier<Optional<UI>> uiSupplier, String urlBack,\n"
					+ //
					"			SessionData sessionData, QueryParameters parameters) {\n" + //
					"		Button buttonBack =\n" + //
					"				createButton(\n" + //
					"						resourceManager.getLocalizedString(\"commons.button.back.text\", sessionData.getLocalization()));\n"
					+ //
					"		buttonBack.addClickListener(event -> uiSupplier.get().ifPresent(ui -> ui.navigate(urlBack, parameters)));\n"
					+ //
					"		return buttonBack;\n" + //
					"	}\n" + //
					"\n" + //
					"	public Button createCancelButton(ResourceManager resourceManager, Consumer<ClickEvent<?>> action,\n"
					+ //
					"			SessionData sessionData) {\n" + //
					"		return createResourcedButton(resourceManager, \"commons.button.cancel.text\", action, sessionData);\n"
					+ //
					"	}\n" + //
					"\n" + //
					"	public Button createDuplicateButton(ResourceManager resourceManager, Consumer<ClickEvent<?>> action,\n"
					+ //
					"			SessionData sessionData) {\n" + //
					"		return createResourcedButton(resourceManager, \"commons.button.duplicate.text\", action, sessionData);\n"
					+ //
					"	}\n" + //
					"\n" + //
					"	public Button createEditButton(ResourceManager resourceManager, Consumer<ClickEvent<?>> action,\n"
					+ //
					"			SessionData sessionData) {\n" + //
					"		return createResourcedButton(resourceManager, \"commons.button.edit.text\", action, sessionData);\n"
					+ //
					"	}\n" + //
					"\n" + //
					"	public Button createLogoutButton(ResourceManager resourceManager, Supplier<Optional<UI>> uiSupplier,\n"
					+ //
					"			SessionData sessionData, Logger logger) {\n" + //
					"		Button buttonLogout =\n" + //
					"				createButton(\n" + //
					"						resourceManager\n" + //
					"								.getLocalizedString(\"commons.button.logout.text\", sessionData.getLocalization()));\n"
					+ //
					"		buttonLogout.addClickListener(event -> uiSupplier.get().ifPresent(ui -> {\n" + //
					"			ui.navigate(ApplicationStartView.URL);\n" + //
					"		}));\n" + //
					"		return buttonLogout;\n" + //
					"	}\n" + //
					"\n" + //
					"	public Button createRemoveButton(ResourceManager resourceManager, Consumer<ClickEvent<?>> action,\n"
					+ //
					"			SessionData sessionData) {\n" + //
					"		return createResourcedButton(resourceManager, \"commons.button.remove.text\", action, sessionData);\n"
					+ //
					"	}\n" + //
					"\n" + //
					"	public Button createResourcedButton(ResourceManager resourceManager, String resourceId,\n"
					+ //
					"			Consumer<ClickEvent<?>> action, SessionData sessionData) {\n" + //
					"		Button button = createButton(resourceManager.getLocalizedString(resourceId, sessionData.getLocalization()));\n"
					+ //
					"		button.addClickListener(action::accept);\n" + //
					"		return button;\n" + //
					"	}\n" + //
					"\n" + //
					"	public Button createSaveButton(ResourceManager resourceManager, Consumer<ClickEvent<?>> action,\n"
					+ //
					"			SessionData sessionData) {\n" + //
					"		return createResourcedButton(resourceManager, \"commons.button.save.text\", action, sessionData);\n"
					+ //
					"	}\n" + //
					"\n" + //
					"}";
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