package archimedes.codegenerators.gui.vaadin.component;

import static archimedes.codegenerators.DataModelReader.EXAMPLE_XMLS;
import static archimedes.codegenerators.DataModelReader.readDataModel;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.gui.vaadin.AbstractGUIVaadinClassCodeGenerator;
import archimedes.model.DataModel;
import archimedes.scheme.Option;

@ExtendWith(MockitoExtension.class)
public class ComponentFactoryClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private ComponentFactoryClassCodeGenerator unitUnderTest;

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRun() {
			// Prepare
			String expected = getExpected(23);
			DataModel dataModel = readDataModel("Example-BookStore.xml", EXAMPLE_XMLS);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel);
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(int vaadinVersion) {
			String s =
					"package de.ollie.bookstore.gui.vaadin.component;\n" //
							+ "\n" //
							+ "import java.time.LocalDateTime;\n" //
							+ "import java.util.Optional;\n" //
							+ "import java.util.function.Consumer;\n" //
							+ "import java.util.function.Supplier;\n" //
							+ "\n" //
							+ "import javax.inject.Named;\n" //
							+ "\n" //
							+ "import org.apache.logging.log4j.Logger;\n" //
							+ "import org.springframework.beans.factory.annotation.Autowired;\n" //
							+ "\n" //
							+ "import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;\n" //
							+ "import com.vaadin.flow.component.ClickEvent;\n" //
							+ "import com.vaadin.flow.component.HasValue.ValueChangeListener;\n" //
							+ "import com.vaadin.flow.component.ItemLabelGenerator;\n" //
							+ "import com.vaadin.flow.component.UI;\n" //
							+ "import com.vaadin.flow.component.checkbox.Checkbox;\n" //
							+ "import com.vaadin.flow.component.combobox.ComboBox;\n" //
							+ "import com.vaadin.flow.component.datetimepicker.DateTimePicker;\n" //
							+ "import com.vaadin.flow.component.textfield.IntegerField;\n" //
							+ "import com.vaadin.flow.component.textfield.NumberField;\n" //
							+ "import com.vaadin.flow.component.textfield.TextArea;\n" //
							+ "import com.vaadin.flow.router.QueryParameters;\n" //
							+ "\n" //
							+ "import de.ollie.bookstore.core.model.PublicationType;\n" //
							+ "import de.ollie.bookstore.core.model.Book;\n" //
							+ "import de.ollie.bookstore.core.model.Chapter;\n" //
							+ "import de.ollie.bookstore.core.model.localization.LocalizationSO;\n" //
							+ "import de.ollie.bookstore.core.service.localization.ResourceManager;\n" //
							+ "import de.ollie.bookstore.gui.SessionData;\n" //
							+ "import de.ollie.bookstore.gui.vaadin.ApplicationStartView;\n" //
							+ "import de.ollie.bookstore.gui.vaadin.masterdata.MasterDataGridFieldRenderer;\n" //
							+ "\n" //
							+ "import lombok.Generated;\n" //
							+ "import lombok.Getter;\n" //
							+ "import lombok.RequiredArgsConstructor;\n" //
							+ "\n" //
							+ "/**\n" //
							+ " * A component factory.\n" //
							+ " *\n" //
							+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
							+ " */\n" //
							+ "@Generated\n" //
							+ "@Getter\n" //
							+ "@Named\n" //
							+ "@RequiredArgsConstructor\n" //
							+ "public class ComponentFactory {\n" //
							+ "\n" //
							+ "	private final ButtonFactoryConfiguration buttonFactoryConfiguration;\n" //
							+ "	private final ResourceManager resourceManager;\n" //
							+ "\n" //
							+ "	@Autowired(required = false)\n" //
							+ "	private ItemLabelGenerator<PublicationType> publicationTypeItemLabelGenerator;\n" //
							+ "\n" //
							+ "	@Autowired(required = false)\n" //
							+ "	private ItemLabelGenerator<Book> bookItemLabelGenerator;\n" //
							+ "\n" //
							+ "	@Autowired(required = false)\n" //
							+ "	private MasterDataGridFieldRenderer<Book> bookMasterDataGridFieldRenderer;\n" //
							+ "	@Autowired(required = false)\n" //
							+ "	private MasterDataGridFieldRenderer<Chapter> chapterMasterDataGridFieldRenderer;\n" //
							+ "\n" //
							+ "	public Button createButton(String text) {\n" //
							+ "		Button button =\n" //
							+ "				new Button(text)\n" //
							+ "						.setBackgroundColor(\"white\")\n" //
							+ "						.setBorder(\"solid 1px\")\n" //
							+ "						.setBorderColor(buttonFactoryConfiguration.getButtonEnabledBorderColor())\n" //
							+ "						.setColor(\"black\")\n" //
							+ "						.setBackgroundImage(buttonFactoryConfiguration.getButtonEnabledBackgroundFileName());\n" //
							+ "		return button;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public Button createAddButton(Consumer<ClickEvent<?>> action, SessionData sessionData) {\n" //
							+ "		return createResourcedButton(\"commons.button.add.text\", action, sessionData);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public Button createBackButton(Supplier<Optional<UI>> uiSupplier, String urlBack, SessionData sessionData) {\n" //
							+ "		Button buttonBack =\n" //
							+ "				createButton(\n" //
							+ "						resourceManager.getLocalizedString(\"commons.button.back.text\", sessionData.getLocalization()));\n" //
							+ "		buttonBack.addClickListener(event -> uiSupplier.get().ifPresent(ui -> ui.navigate(urlBack)));\n" //
							+ "		return buttonBack;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public Button createBackButton(Supplier<Optional<UI>> uiSupplier, String urlBack, QueryParameters parameters,\n" //
							+ "			SessionData sessionData) {\n" //
							+ "		Button buttonBack =\n" //
							+ "				createButton(\n" //
							+ "						resourceManager.getLocalizedString(\"commons.button.back.text\", sessionData.getLocalization()));\n" //
							+ "		buttonBack.addClickListener(event -> uiSupplier.get().ifPresent(ui -> ui.navigate(urlBack, parameters)));\n" //
							+ "		return buttonBack;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public Button createCancelButton(Consumer<ClickEvent<?>> action, SessionData sessionData) {\n" //
							+ "		return createResourcedButton(\"commons.button.cancel.text\", action, sessionData);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public Button createDuplicateButton(Consumer<ClickEvent<?>> action, SessionData sessionData) {\n" //
							+ "		return createResourcedButton(\"commons.button.duplicate.text\", action, sessionData);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public Button createEditButton(Consumer<ClickEvent<?>> action, SessionData sessionData) {\n" //
							+ "		return createResourcedButton(\"commons.button.edit.text\", action, sessionData);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public Button createLogoutButton(Supplier<Optional<UI>> uiSupplier, Logger logger, SessionData sessionData) {\n" //
							+ "		Button buttonLogout =\n" //
							+ "				createButton(\n" //
							+ "						resourceManager\n" //
							+ "								.getLocalizedString(\"commons.button.logout.text\", sessionData.getLocalization()));\n" //
							+ "		buttonLogout.addClickListener(event -> uiSupplier.get().ifPresent(ui -> {\n" //
							+ "			ui.navigate(ApplicationStartView.URL);\n" //
							+ "		}));\n" //
							+ "		return buttonLogout;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public Button createRemoveButton(Consumer<ClickEvent<?>> action, SessionData sessionData) {\n" //
							+ "		return createResourcedButton(\"commons.button.remove.text\", action, sessionData);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public Button createResourcedButton(String resourceId, Consumer<ClickEvent<?>> action, SessionData sessionData) {\n" //
							+ "		Button button = createButton(resourceManager.getLocalizedString(resourceId, sessionData.getLocalization()));\n" //
							+ "		button.addClickListener(action::accept);\n" //
							+ "		return button;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public Button createSaveButton(Consumer<ClickEvent<?>> action, SessionData sessionData) {\n" //
							+ "		return createResourcedButton(\"commons.button.save.text\", action, sessionData);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public Checkbox createCheckbox(String resourceId, Boolean fieldContent, SessionData sessionData) {\n" //
							+ "		Checkbox checkBox = new Checkbox(resourceManager.getLocalizedString(resourceId, sessionData.getLocalization()));\n" //
							+ "		checkBox.setValue(fieldContent);\n" //
							+ "		checkBox.setWidthFull();\n" //
							+ "		return checkBox;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public <T> ComboBox<T> createComboBox(String resourceId, T fieldContent, T[] valuesToSelect,\n" //
							+ "			SessionData sessionData) {\n" //
							+ "		return createComboBox(resourceId, fieldContent, valuesToSelect, null, sessionData);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public <T> ComboBox<T> createComboBox(String resourceId, T fieldContent, T[] valuesToSelect,\n" //
							+ "			ItemLabelGenerator<T> itemLabelGenerator, SessionData sessionData) {\n" //
							+ "		ComboBox<T> comboBox =\n" //
							+ "				new ComboBox<>(resourceManager.getLocalizedString(resourceId, sessionData.getLocalization()));\n" //
							+ "		comboBox.setItems(valuesToSelect);\n" //
							+ "		comboBox.setValue(fieldContent);\n" //
							+ "		comboBox.setWidthFull();\n" //
							+ "		if (itemLabelGenerator != null) {\n" //
							+ "			comboBox.setItemLabelGenerator(itemLabelGenerator);\n" //
							+ "		}\n" //
							+ "		return comboBox;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public IntegerField createIntegerField(String resourceId, Integer fieldContent, Integer min, Integer max,\n" //
							+ "			SessionData sessionData) {\n" //
							+ "		IntegerField integerField =\n" //
							+ "				new IntegerField(resourceManager.getLocalizedString(resourceId, sessionData.getLocalization()));\n" //
							+ "		if (max != null) {\n" //
							+ "			integerField.setMax(max);\n" //
							+ "		}\n" //
							+ "		if (min != null) {\n" //
							+ "			integerField.setMin(min);\n" //
							+ "		}\n"; //
			if (vaadinVersion == 23) {
				s += "		integerField.setHasControls(true);\n";
			} else {
				s += "		integerField.setStepButtonsVisible(true);\n";
			}
			s +=
					"		integerField.setValue(fieldContent);\n" //
							+ "		integerField.setWidthFull();\n" //
							+ "		return integerField;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public IntegerField createIntegerField(String resourceId, Integer fieldContent, Integer min, Integer max,\n" //
							+ "			Integer step, SessionData sessionData) {\n" //
							+ "		IntegerField integerField =\n" //
							+ "				new IntegerField(resourceManager.getLocalizedString(resourceId, sessionData.getLocalization()));\n" //
							+ "		if (max != null) {\n" //
							+ "			integerField.setMax(max);\n" //
							+ "		}\n" //
							+ "		if (min != null) {\n" //
							+ "			integerField.setMin(min);\n" //
							+ "		}\n" //
							+ "		if (step != null) {\n" //
							+ "			integerField.setStep(step);\n" //
							+ "		}\n"; //
			if (vaadinVersion == 23) {
				s += "		integerField.setHasControls(true);\n";
			} else {
				s += "		integerField.setStepButtonsVisible(true);\n";
			}
			s +=
					"		integerField.setValue(fieldContent);\n" //
							+ "		integerField.setWidthFull();\n" //
							+ "		return integerField;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public NumberField createNumberField(String resourceId, Double fieldContent, Double min, Double max, Double step,\n" //
							+ "			SessionData sessionData) {\n" //
							+ "		NumberField integerField =\n" //
							+ "				new NumberField(resourceManager.getLocalizedString(resourceId, sessionData.getLocalization()));\n" //
							+ "		if (max != null) {\n" //
							+ "			integerField.setMax(max);\n" //
							+ "		}\n" //
							+ "		if (min != null) {\n" //
							+ "			integerField.setMin(min);\n" //
							+ "		}\n"; //
			if (vaadinVersion == 23) {
				s += "		integerField.setHasControls(true);\n";
			} else {
				s += "		integerField.setStepButtonsVisible(true);\n";
			}
			s +=
					"		integerField.setValue(fieldContent);\n" //
							+ "		integerField.setWidthFull();\n" //
							+ "		if (step != null) {\n" //
							+ "			integerField.setStep(step);\n" //
							+ "		}\n" //
							+ "		return integerField;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public TextField createTextField(String resourceId, String fieldContent, SessionData sessionData) {\n" //
							+ "		TextField textField =\n" //
							+ "				TextFieldFactory\n" //
							+ "						.createTextField(resourceManager.getLocalizedString(resourceId, sessionData.getLocalization()));\n" //
							+ "		textField.setValue(fieldContent != null ? fieldContent : \"\");\n" //
							+ "		textField.setWidthFull();\n" //
							+ "		return textField;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public TextArea createTextArea(String resourceId, String fieldContent, SessionData sessionData) {\n" //
							+ "		TextArea textArea = new TextArea(resourceManager.getLocalizedString(resourceId, sessionData.getLocalization()));\n" //
							+ "		textArea.setValue(fieldContent != null ? fieldContent : \"\");\n" //
							+ "		textArea.setWidthFull();\n" //
							+ "		return textArea;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	public DateTimePicker createDateTimePicker(String resourceId, LocalizationSO localization, LocalDateTime timestamp,\n" //
							+ "			ValueChangeListener<ComponentValueChangeEvent<DateTimePicker, LocalDateTime>> listener) {\n" //
							+ "		DateTimePicker dtp = new DateTimePicker(\n" //
							+ "				resourceManager.getLocalizedString(resourceId, localization), timestamp, listener);\n" //
							+ "		dtp.setWidthFull();\n" //
							+ "		return dtp;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "}";
			return s;
		}

		@Test
		void happyRun_ForVaadin24() {
			// Prepare
			String expected = getExpected(24);
			DataModel dataModel = readDataModel("Example-BookStore.xml", EXAMPLE_XMLS);
			dataModel.addOption(new Option(AbstractGUIVaadinClassCodeGenerator.VAADIN_VERSION, "24"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel);
			// Check
			assertEquals(expected, returned);
		}

	}

}
