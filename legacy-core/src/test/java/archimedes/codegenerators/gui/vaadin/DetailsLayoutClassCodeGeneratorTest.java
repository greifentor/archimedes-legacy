package archimedes.codegenerators.gui.vaadin;

import static archimedes.codegenerators.gui.vaadin.AbstractGUIVaadinClassCodeGenerator.GUI_EDITOR_POS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Types;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.legacy.scheme.Domain;
import archimedes.legacy.scheme.Tabellenspalte;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class DetailsLayoutClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private DetailsLayoutClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Nested
		class SimpleClass {

			private String getExpected() {
				return "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
						"\n" + //
						"import com.vaadin.flow.component.AttachEvent;\n" + //
						"import com.vaadin.flow.component.textfield.IntegerField;\n" + //
						"import com.vaadin.flow.component.textfield.TextField;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.service.ATableService;\n" + //
						"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
						"import base.pack.age.name.gui.SessionData;\n" + //
						"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
						"import lombok.Generated;\n" + //
						"import lombok.RequiredArgsConstructor;\n" + //
						"\n" + //
						"/**\n" + //
						" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
						" */\n" + //
						"@Generated\n" + //
						"@RequiredArgsConstructor\n" + //
						"public class ATableDetailsLayout extends AbstractMasterDataBaseLayout {\n" + //
						"\n" + //
						"	private final ButtonFactory buttonFactory;\n" + //
						"	private final ATable model;\n" + //
						"	private final ATableService service;\n" + //
						"	private final ResourceManager resourceManager;\n" + //
						"	private final SessionData session;\n" + //
						"	private final Observer observer;\n" + //
						"\n" + //
						"	private IntegerField integerFieldCount;\n" + //
						"	private TextField textFieldDescription;\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void onAttach(AttachEvent attachEvent) {\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"		createButtons();\n" + //
						"		integerFieldCount = createIntegerField(\"count\", model.getCount(), 1, 10);\n" + //
						"		textFieldDescription = createTextField(\"description\", model.getDescription());\n" + //
						"		getStyle().set(\"-moz-border-radius\", \"4px\");\n" + //
						"		getStyle().set(\"-webkit-border-radius\", \"4px\");\n" + //
						"		getStyle().set(\"border-radius\", \"4px\");\n" + //
						"		getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" + //
						"		getStyle()\n" + //
						"				.set(\n" + //
						"						\"box-shadow\",\n" + //
						"						\"10px 10px 20px #e4e4e4, -10px 10px 20px #e4e4e4, -10px -10px 20px #e4e4e4, 10px -10px 20px #e4e4e4\");\n"
						+ //
						"		setMargin(false);\n" + //
						"		setWidthFull();\n" + //
						"		add(\n" + //
						"				integerFieldCount,\n" + //
						"				textFieldDescription,\n" + //
						"				getMasterDataButtonLayout(model.getId() > 0));\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected ButtonFactory getButtonFactory() {\n" + //
						"		return buttonFactory;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected ResourceManager getResourceManager() {\n" + //
						"		return resourceManager;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected SessionData getSessionData() {\n" + //
						"		return session;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected String getTextFieldResourceId() {\n" + //
						"		return \"ATableDetailsLayout.details.field.{}.label\";\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void remove() {\n" + //
						"		service.delete(model);\n" + //
						"		observer.remove();\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void save() {\n" + //
						"		model.setCount(integerFieldCount.getValue());\n" + //
						"		model.setDescription(textFieldDescription.getValue());\n" + //
						"		service.update(model);\n" + //
						"		observer.save();\n" + //
						"	}\n" + //
						"\n" + //
						"}";
			}

			@Test
			void happyRunForASimpleObject() {
				// Prepare
				String expected = getExpected();
				DataModel dataModel = readDataModel("Model.xml");
				TableModel tableModel = dataModel.getTableByName("A_TABLE");
				tableModel.getColumnByName("Description").addOption(new Option(GUI_EDITOR_POS, "1"));
				tableModel.addColumn(new Tabellenspalte("COUNT", new Domain("Counts", Types.INTEGER, -1, -1)));
				tableModel.getColumnByName("COUNT").addOption(new Option(GUI_EDITOR_POS, "0"));
				tableModel.getColumnByName("COUNT").addOption(new Option(AbstractClassCodeGenerator.MAX, "10"));
				tableModel.getColumnByName("COUNT").addOption(new Option(AbstractClassCodeGenerator.MIN, "1"));
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class WithReference {

			private String getExpected() {
				return "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
						"\n" + //
						"import com.vaadin.flow.component.AttachEvent;\n" + //
						"import com.vaadin.flow.component.combobox.ComboBox;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.model.AnotherTable;\n" + //
						"import base.pack.age.name.core.service.ATableService;\n" + //
						"import base.pack.age.name.core.service.AnotherTableService;\n" + //
						"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
						"import base.pack.age.name.gui.SessionData;\n" + //
						"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
						"import lombok.Generated;\n" + //
						"import lombok.RequiredArgsConstructor;\n" + //
						"\n" + //
						"/**\n" + //
						" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
						" */\n" + //
						"@Generated\n" + //
						"@RequiredArgsConstructor\n" + //
						"public class ATableDetailsLayout extends AbstractMasterDataBaseLayout {\n" + //
						"\n" + //
						"	private final ButtonFactory buttonFactory;\n" + //
						"	private final ATable model;\n" + //
						"	private final ATableService service;\n" + //
						"	private final AnotherTableService anotherTableService;\n" + //
						"	private final ResourceManager resourceManager;\n" + //
						"	private final SessionData session;\n" + //
						"	private final Observer observer;\n" + //
						"\n" + //
						"	private ComboBox<AnotherTable> comboBoxRef;\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void onAttach(AttachEvent attachEvent) {\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"		createButtons();\n" + //
						"		comboBoxRef = new ComboBox<>(\"AnotherTable\", anotherTableService.findAll());\n" + //
						"		comboBoxRef.setValue(model.getAnotherTable());\n" + //
						"		comboBoxRef.setItemLabelGenerator(AnotherTable::getName);\n" + //
						"		comboBoxRef.setWidthFull();\n" + //
						"		getStyle().set(\"-moz-border-radius\", \"4px\");\n" + //
						"		getStyle().set(\"-webkit-border-radius\", \"4px\");\n" + //
						"		getStyle().set(\"border-radius\", \"4px\");\n" + //
						"		getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" + //
						"		getStyle()\n" + //
						"				.set(\n" + //
						"						\"box-shadow\",\n" + //
						"						\"10px 10px 20px #e4e4e4, -10px 10px 20px #e4e4e4, -10px -10px 20px #e4e4e4, 10px -10px 20px #e4e4e4\");\n"
						+ //
						"		setMargin(false);\n" + //
						"		setWidthFull();\n" + //
						"		add(\n" + //
						"				comboBoxRef,\n" + //
						"				getMasterDataButtonLayout(model.getId() > 0));\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected ButtonFactory getButtonFactory() {\n" + //
						"		return buttonFactory;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected ResourceManager getResourceManager() {\n" + //
						"		return resourceManager;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected SessionData getSessionData() {\n" + //
						"		return session;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected String getTextFieldResourceId() {\n" + //
						"		return \"ATableDetailsLayout.details.field.{}.label\";\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void remove() {\n" + //
						"		service.delete(model);\n" + //
						"		observer.remove();\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void save() {\n" + //
						"		model.setRef(comboBoxRef.getValue());\n" + //
						"		service.update(model);\n" + //
						"		observer.save();\n" + //
						"	}\n" + //
						"\n" + //
						"}";
			}

			@Test
			void happyRunForASimpleObject() {
				// Prepare
				String expected = getExpected();
				DataModel dataModel = readDataModel("Model-ForeignKey.xml");
				TableModel tableModel = dataModel.getTableByName("A_TABLE");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class Specials {

			private String getExpected(boolean flagIsNullable) {
				return "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
						"\n" + //
						"import com.vaadin.flow.component.AttachEvent;\n" + //
						"import com.vaadin.flow.component.checkbox.Checkbox;\n" + //
						"import com.vaadin.flow.component.combobox.ComboBox;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.TableWithSpecials;\n" + //
						"import base.pack.age.name.core.service.TableWithSpecialsService;\n" + //
						"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
						"import base.pack.age.name.gui.SessionData;\n" + //
						"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
						"import base.pack.age.name.core.model.EnumType;\n" + //
						"import lombok.Generated;\n" + //
						"import lombok.RequiredArgsConstructor;\n" + //
						"\n" + //
						"/**\n" + //
						" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
						" */\n" + //
						"@Generated\n" + //
						"@RequiredArgsConstructor\n" + //
						"public class TableWithSpecialsDetailsLayout extends AbstractMasterDataBaseLayout {\n" + //
						"\n" + //
						"	private final ButtonFactory buttonFactory;\n" + //
						"	private final TableWithSpecials model;\n" + //
						"	private final TableWithSpecialsService service;\n" + //
						"	private final ResourceManager resourceManager;\n" + //
						"	private final SessionData session;\n" + //
						"	private final Observer observer;\n" + //
						"\n" + //
						"	private ComboBox<EnumType> comboBoxEnumField;\n" + //
						"	private Checkbox checkboxFlag;\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void onAttach(AttachEvent attachEvent) {\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"		createButtons();\n" + //
						"		comboBoxEnumField = createComboBox(\"enumfield\", model.getEnumField(), EnumType.values());\n"
						+ //
						"		checkboxFlag = createCheckbox(\"flag\", model." + (flagIsNullable ? "get" : "is")
						+ "Flag());\n" + //
						"		getStyle().set(\"-moz-border-radius\", \"4px\");\n" + //
						"		getStyle().set(\"-webkit-border-radius\", \"4px\");\n" + //
						"		getStyle().set(\"border-radius\", \"4px\");\n" + //
						"		getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" + //
						"		getStyle()\n" + //
						"				.set(\n" + //
						"						\"box-shadow\",\n" + //
						"						\"10px 10px 20px #e4e4e4, -10px 10px 20px #e4e4e4, -10px -10px 20px #e4e4e4, 10px -10px 20px #e4e4e4\");\n"
						+ //
						"		setMargin(false);\n" + //
						"		setWidthFull();\n" + //
						"		add(\n" + //
						"				comboBoxEnumField,\n" + //
						"				checkboxFlag,\n" + //
						"				getMasterDataButtonLayout(model.getId() > 0));\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected ButtonFactory getButtonFactory() {\n" + //
						"		return buttonFactory;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected ResourceManager getResourceManager() {\n" + //
						"		return resourceManager;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected SessionData getSessionData() {\n" + //
						"		return session;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected String getTextFieldResourceId() {\n" + //
						"		return \"TableWithSpecialsDetailsLayout.details.field.{}.label\";\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void remove() {\n" + //
						"		service.delete(model);\n" + //
						"		observer.remove();\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void save() {\n" + //
						"		model.setEnumField(comboBoxEnumField.getValue());\n" + //
						"		model.setFlag(checkboxFlag.getValue());\n" + //
						"		service.update(model);\n" + //
						"		observer.save();\n" + //
						"	}\n" + //
						"\n" + //
						"}";
			}

			@Test
			void happyRunForASimpleObject() {
				// Prepare
				String expected = getExpected(true);
				DataModel dataModel = readDataModel("Model.xml");
				TableModel tableModel = dataModel.getTableByName("TABLE_WITH_SPECIALS");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
				// Check
				assertEquals(expected, returned);
			}

			@Test
			void happyRunForASimpleObject_withNotNullBoolean() {
				// Prepare
				String expected = getExpected(false);
				DataModel dataModel = readDataModel("Model.xml");
				TableModel tableModel = dataModel.getTableByName("TABLE_WITH_SPECIALS");
				tableModel.getColumnByName("Flag").setNotNull(true);
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

	@Nested
	class Inheritance {

		@Nested
		class Superclass {

			private String getExpected() {
				return "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
						"\n" + //
						"import com.vaadin.flow.component.AttachEvent;\n" + //
						"import com.vaadin.flow.component.checkbox.Checkbox;\n" + //
						"import com.vaadin.flow.component.textfield.TextField;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.service.ATableService;\n" + //
						"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
						"import base.pack.age.name.gui.SessionData;\n" + //
						"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
						"import lombok.Generated;\n" + //
						"import lombok.RequiredArgsConstructor;\n" + //
						"\n" + //
						"/**\n" + //
						" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
						" */\n" + //
						"@Generated\n" + //
						"@RequiredArgsConstructor\n" + //
						"public class ATableDetailsLayout extends AbstractMasterDataBaseLayout {\n" + //
						"\n" + //
						"	private final ButtonFactory buttonFactory;\n" + //
						"	private final ATable model;\n" + //
						"	private final ATableService service;\n" + //
						"	private final ResourceManager resourceManager;\n" + //
						"	private final SessionData session;\n" + //
						"	private final Observer observer;\n" + //
						"\n" + //
						"	private TextField textFieldDescription;\n" + //
						"	private Checkbox checkboxFlag;\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void onAttach(AttachEvent attachEvent) {\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"		createButtons();\n" + //
						"		textFieldDescription = createTextField(\"description\", model.getDescription());\n" + //
						"		checkboxFlag = createCheckbox(\"flag\", model.isFlag());\n" + //
						"		getStyle().set(\"-moz-border-radius\", \"4px\");\n" + //
						"		getStyle().set(\"-webkit-border-radius\", \"4px\");\n" + //
						"		getStyle().set(\"border-radius\", \"4px\");\n" + //
						"		getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" + //
						"		getStyle()\n" + //
						"				.set(\n" + //
						"						\"box-shadow\",\n" + //
						"						\"10px 10px 20px #e4e4e4, -10px 10px 20px #e4e4e4, -10px -10px 20px #e4e4e4, 10px -10px 20px #e4e4e4\");\n"
						+ //
						"		setMargin(false);\n" + //
						"		setWidthFull();\n" + //
						"		add(\n" + //
						"				textFieldDescription,\n" + //
						"				checkboxFlag,\n" + //
						"				getMasterDataButtonLayout(model.getId() > 0));\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected ButtonFactory getButtonFactory() {\n" + //
						"		return buttonFactory;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected ResourceManager getResourceManager() {\n" + //
						"		return resourceManager;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected SessionData getSessionData() {\n" + //
						"		return session;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected String getTextFieldResourceId() {\n" + //
						"		return \"ATableDetailsLayout.details.field.{}.label\";\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void remove() {\n" + //
						"		service.delete(model);\n" + //
						"		observer.remove();\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void save() {\n" + //
						"		model.setDescription(textFieldDescription.getValue());\n" + //
						"		model.setFlag(checkboxFlag.getValue());\n" + //
						"		service.update(model);\n" + //
						"		observer.save();\n" + //
						"	}\n" + //
						"\n" + //
						"}";
			}

			@Test
			void happyRun() {
				// Prepare
				String expected = getExpected();
				DataModel dataModel = readDataModel("Model-Inheritance.xml");
				TableModel tableModel = dataModel.getTableByName("A_TABLE");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class Subclass {

			private String getExpected() {
				return "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
						"\n" + //
						"import com.vaadin.flow.component.AttachEvent;\n" + //
						"import com.vaadin.flow.component.checkbox.Checkbox;\n" + //
						"import com.vaadin.flow.component.textfield.TextField;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.AnotherTable;\n" + //
						"import base.pack.age.name.core.service.ATableService;\n" + //
						"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
						"import base.pack.age.name.gui.SessionData;\n" + //
						"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
						"import lombok.Generated;\n" + //
						"import lombok.RequiredArgsConstructor;\n" + //
						"\n" + //
						"/**\n" + //
						" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
						" */\n" + //
						"@Generated\n" + //
						"@RequiredArgsConstructor\n" + //
						"public class AnotherTableDetailsLayout extends AbstractMasterDataBaseLayout {\n" + //
						"\n" + //
						"	private final ButtonFactory buttonFactory;\n" + //
						"	private final AnotherTable model;\n" + //
						"	private final ATableService service;\n" + //
						"	private final ResourceManager resourceManager;\n" + //
						"	private final SessionData session;\n" + //
						"	private final Observer observer;\n" + //
						"\n" + //
						"	private TextField textFieldDescription;\n" + //
						"	private TextField textFieldName;\n" + //
						"	private Checkbox checkboxFlag;\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void onAttach(AttachEvent attachEvent) {\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"		createButtons();\n" + //
						"		textFieldDescription = createTextField(\"description\", model.getDescription());\n" + //
						"		textFieldName = createTextField(\"name\", model.getName());\n" + //
						"		checkboxFlag = createCheckbox(\"flag\", model.isFlag());\n" + //
						"		getStyle().set(\"-moz-border-radius\", \"4px\");\n" + //
						"		getStyle().set(\"-webkit-border-radius\", \"4px\");\n" + //
						"		getStyle().set(\"border-radius\", \"4px\");\n" + //
						"		getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" + //
						"		getStyle()\n" + //
						"				.set(\n" + //
						"						\"box-shadow\",\n" + //
						"						\"10px 10px 20px #e4e4e4, -10px 10px 20px #e4e4e4, -10px -10px 20px #e4e4e4, 10px -10px 20px #e4e4e4\");\n"
						+ //
						"		setMargin(false);\n" + //
						"		setWidthFull();\n" + //
						"		add(\n" + //
						"				textFieldDescription,\n" + //
						"				textFieldName,\n" + //
						"				checkboxFlag,\n" + //
						"				getMasterDataButtonLayout(model.getId() > 0));\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected ButtonFactory getButtonFactory() {\n" + //
						"		return buttonFactory;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected ResourceManager getResourceManager() {\n" + //
						"		return resourceManager;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected SessionData getSessionData() {\n" + //
						"		return session;\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected String getTextFieldResourceId() {\n" + //
						"		return \"AnotherTableDetailsLayout.details.field.{}.label\";\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void remove() {\n" + //
						"		service.delete(model);\n" + //
						"		observer.remove();\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void save() {\n" + //
						"		model.setDescription(textFieldDescription.getValue());\n" + //
						"		model.setName(textFieldName.getValue());\n" + //
						"		model.setFlag(checkboxFlag.getValue());\n" + //
						"		service.update(model);\n" + //
						"		observer.save();\n" + //
						"	}\n" + //
						"\n" + //
						"}";
			}

			@Test
			void happyRun() {
				// Prepare
				String expected = getExpected();
				DataModel dataModel = readDataModel("Model-Inheritance.xml");
				TableModel tableModel = dataModel.getTableByName("ANOTHER_TABLE");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

}