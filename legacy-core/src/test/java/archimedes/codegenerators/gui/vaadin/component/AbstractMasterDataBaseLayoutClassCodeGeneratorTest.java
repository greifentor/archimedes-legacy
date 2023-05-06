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
public class AbstractMasterDataBaseLayoutClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private AbstractMasterDataBaseLayoutClassCodeGenerator unitUnderTest;

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
							+ "import java.util.List;\n" //
							+ "import java.util.Map;\n" //
							+ "\n" //
							+ "import org.apache.logging.log4j.LogManager;\n" //
							+ "import org.apache.logging.log4j.Logger;\n" //
							+ "\n" //
							+ "import com.vaadin.flow.component.AttachEvent;\n" //
							+ "import com.vaadin.flow.component.Component;\n" //
							+ "import com.vaadin.flow.component.checkbox.Checkbox;\n" //
							+ "import com.vaadin.flow.component.combobox.ComboBox;\n" //
							+ "import com.vaadin.flow.component.orderedlayout.Scroller;\n" //
							+ "import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" //
							+ "import com.vaadin.flow.component.textfield.IntegerField;\n" //
							+ "import com.vaadin.flow.component.textfield.NumberField;\n" //
							+ "import com.vaadin.flow.component.textfield.TextArea;\n" //
							+ "import com.vaadin.flow.router.BeforeEnterEvent;\n" //
							+ "import com.vaadin.flow.router.BeforeEnterObserver;\n" //
							+ "import com.vaadin.flow.router.BeforeEvent;\n" //
							+ "import com.vaadin.flow.router.HasUrlParameter;\n" //
							+ "import com.vaadin.flow.router.Location;\n" //
							+ "import com.vaadin.flow.router.OptionalParameter;\n" //
							+ "import com.vaadin.flow.router.QueryParameters;\n" //
							+ "\n" //
							+ "import base.pack.age.name.core.service.localization.ResourceManager;\n" //
							+ "import base.pack.age.name.gui.SessionData;\n" //
							+ "import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" //
							+ "\n" //
							+ "import lombok.Generated;\n" //
							+ "\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * A base class for master data base layouts.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s +=
					"@Generated\n" //
							+ "public abstract class AbstractMasterDataBaseLayout extends Scroller implements BeforeEnterObserver, HasUrlParameter<String> {\n" //
							+ "\n" //
							+ "	public interface Observer {\n" //
							+ "		void save(Object model);\n" //
							+ "\n" //
							+ "		void remove();\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private static final Logger LOG = LogManager.getLogger(AbstractMasterDataBaseLayout.class);\n" //
							+ "\n" //
							+ "	protected Button buttonRemove;\n" //
							+ "	protected Button buttonSave;\n" //
							+ "	protected VerticalLayout mainLayout;\n"
							+ "\n" //
							+ "	protected Map<String, List<String>> parametersMap;\n" //
							+ "\n" //
							+ "	protected abstract ButtonFactory getButtonFactory();\n" //
							+ "\n" //
							+ "	protected abstract ResourceManager getResourceManager();\n" //
							+ "\n" //
							+ "	protected abstract SessionData getSessionData();\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	protected void onAttach(AttachEvent attachEvent) {\n" //
							+ "		super.onAttach(attachEvent);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {\n" //
							+ "		LOG.info(\"setParameter\");\n" //
							+ "		Location location = event.getLocation();\n" //
							+ "		QueryParameters queryParameters = location.getQueryParameters();\n" //
							+ "		parametersMap = queryParameters.getParameters();\n" //
							+ "		doSetParameter(event);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	protected void doSetParameter(BeforeEvent event) {\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {\n" //
							+ "		LOG.info(\"check for authorization\");\n" //
							+ "		UserAuthorizationChecker.forwardToLoginOnNoUserSetForSession(getSessionData(), beforeEnterEvent);\n" //
							+ "		createButtons();\n" //
							+ "		doBeforeEnter(beforeEnterEvent);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	protected void createButtons() {\n" //
							+ "		buttonRemove = getButtonFactory().createRemoveButton(getResourceManager(), event -> remove(), getSessionData());\n" //
							+ "		buttonSave = getButtonFactory().createSaveButton(getResourceManager(), event -> save(), getSessionData());\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	protected abstract void remove();\n" //
							+ "\n" //
							+ "	protected abstract void save();\n" //
							+ "\n" //
							+ "	protected void doBeforeEnter(BeforeEnterEvent beforeEnterEvent) {\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	protected Checkbox createCheckbox(String resourceId, Boolean fieldContent) {\n" //
							+ "		Checkbox checkBox =\n" //
							+ "				new Checkbox(getResourceManager().getLocalizedString(resourceId, getSessionData().getLocalization()));\n" //
							+ "		checkBox.setValue(fieldContent);\n" //
							+ "		checkBox.setWidthFull();\n" //
							+ "		return checkBox;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	protected <T> ComboBox<T> createComboBox(String resourceId, T fieldContent, T[] valuesToSelect) {\n" //
							+ "		ComboBox<T> comboBox =\n" //
							+ "				new ComboBox<>(getResourceManager().getLocalizedString(resourceId, getSessionData().getLocalization()));\n" //
							+ "		comboBox.setItems(valuesToSelect);\n" //
							+ "		comboBox.setValue(fieldContent);\n" //
							+ "		comboBox.setWidthFull();\n" //
							+ "		return comboBox;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	protected IntegerField createIntegerField(String resourceId, Integer fieldContent, Integer min, Integer max) {\n" //
							+ "		IntegerField integerField =\n" //
							+ "				new IntegerField(\n" //
							+ "						getResourceManager().getLocalizedString(resourceId, getSessionData().getLocalization()));\n" //
							+ "		if (max != null) {\n" //
							+ "			integerField.setMax(max);\n" //
							+ "		}\n" //
							+ "		if (min != null) {\n" //
							+ "			integerField.setMin(min);\n" //
							+ "		}\n" //
							+ "		integerField.setHasControls(true);\n" //
							+ "		integerField.setValue(fieldContent);\n" //
							+ "		integerField.setWidthFull();\n" //
							+ "		return integerField;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	protected IntegerField createIntegerField(String resourceId, Integer fieldContent, Integer min, Integer max,\n" //
							+ "			Integer step) {\n" //
							+ "		IntegerField integerField =\n" //
							+ "				new IntegerField(\n" //
							+ "						getResourceManager().getLocalizedString(resourceId, getSessionData().getLocalization()));\n" //
							+ "		if (max != null) {\n" //
							+ "			integerField.setMax(max);\n" //
							+ "		}\n" //
							+ "		if (min != null) {\n" //
							+ "			integerField.setMin(min);\n" //
							+ "		}\n" //
							+ "		if (step != null) {\n" //
							+ "			integerField.setStep(step);\n" //
							+ "		}\n" //
							+ "		integerField.setHasControls(true);\n" //
							+ "		integerField.setValue(fieldContent);\n" //
							+ "		integerField.setWidthFull();\n" //
							+ "		return integerField;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	protected NumberField createNumberField(String resourceId, Double fieldContent, Double min, Double max,\n" //
							+ "			Double step) {\n" //
							+ "		NumberField integerField =\n" //
							+ "				new NumberField(\n" //
							+ "						getResourceManager().getLocalizedString(resourceId, getSessionData().getLocalization()));\n" //
							+ "		if (max != null) {\n" //
							+ "			integerField.setMax(max);\n" //
							+ "		}\n" //
							+ "		if (min != null) {\n" //
							+ "			integerField.setMin(min);\n" //
							+ "		}\n" //
							+ "		integerField.setHasControls(true);\n" //
							+ "		integerField.setValue(fieldContent);\n" //
							+ "		integerField.setWidthFull();\n" //
							+ "		if (step != null) {\n" //
							+ "			integerField.setStep(step);\n" //
							+ "		}\n" //
							+ "		return integerField;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	protected TextField createTextField(String resourceId, String fieldContent) {\n" //
							+ "		TextField textField =\n" //
							+ "				TextFieldFactory\n" //
							+ "						.createTextField(\n" //
							+ "								getResourceManager()\n" //
							+ "										.getLocalizedString(resourceId, getSessionData().getLocalization()));\n" //
							+ "		textField.setValue(fieldContent != null ? fieldContent : \"\");\n" //
							+ "		textField.setWidthFull();\n" //
							+ "		return textField;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	protected TextArea createTextArea(String resourceId, String fieldContent) {\n" //
							+ "		TextArea textArea =\n" //
							+ "				new TextArea(getResourceManager().getLocalizedString(resourceId, getSessionData().getLocalization()));\n" //
							+ "		textArea.setValue(fieldContent != null ? fieldContent : \"\");\n" //
							+ "		textArea.setWidthFull();\n" //
							+ "		return textArea;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	protected Button[] getButtons(boolean couldBeRemoved) {\n" //
							+ "		Button[] buttons = new Button[1 + (couldBeRemoved ? 1 : 0)];\n" //
							+ "		buttons[0] = buttonSave;\n" //
							+ "		if (couldBeRemoved) {\n" //
							+ "			buttons[1] = buttonRemove;\n" //
							+ "		}\n" //
							+ "		return buttons;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	protected MasterDataButtonLayout getMasterDataButtonLayout(boolean couldBeRemoved) {\n" //
							+ "		return new MasterDataButtonLayout(getButtons(couldBeRemoved));\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	protected void setMargin(boolean b) {\n" //
							+ "		if (mainLayout != null) {\n" //
							+ "			mainLayout.setMargin(b);\n" //
							+ "		}\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	protected void add(Component... components) {\n" //
							+ "		if (mainLayout == null) {\n" //
							+ "			mainLayout = new VerticalLayout();\n" //
							+ "			setContent(mainLayout);\n" //
							+ "		}\n" //
							+ "		mainLayout.add(components);\n" //
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
