package archimedes.codegenerators.gui.vaadin;

import static archimedes.codegenerators.DataModelReader.EXAMPLE_XMLS;
import static archimedes.codegenerators.DataModelReader.readDataModel;
import static archimedes.codegenerators.gui.vaadin.AbstractGUIVaadinClassCodeGenerator.GUI_EDITOR_POS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Types;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.legacy.scheme.Domain;
import archimedes.legacy.scheme.Tabellenspalte;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;

@ExtendWith(MockitoExtension.class)
public class DetailsLayoutClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private DetailsLayoutClassCodeGenerator unitUnderTest;

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Nested
		class SimpleClass {

			private String getExpected() {
				return "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
						"\n" + //
						"import com.vaadin.flow.component.AttachEvent;\n" + //
						"import com.vaadin.flow.component.dialog.Dialog;\n" + //
						"import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" + //
						"import com.vaadin.flow.component.textfield.IntegerField;\n" + //
						"import com.vaadin.flow.component.textfield.TextField;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.service.ATableService;\n" + //
						"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
						"import base.pack.age.name.gui.SessionData;\n" + //
						"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" + //
						"import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" + //
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
						"	private final ComponentFactory componentFactory;\n" + //
						"	private final ATable model;\n" + //
						"	private final ServiceProvider serviceProvider;\n" + //
						"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
						"	private final ResourceManager resourceManager;\n" + //
						"	private final SessionData session;\n" + //
						"	private final Observer observer;\n" + //
						"	private final DetailsLayoutComboBoxItemLabelGenerator<ATable> comboBoxItemLabelGenerator;\n"
						+ //
						"\n" + //
						"	private IntegerField integerFieldCount;\n" + //
						"	private TextField textFieldDescription;\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void onAttach(AttachEvent attachEvent) {\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"		createButtons();\n" + //
						"		integerFieldCount = createIntegerField(\"ATableDetailsLayout.field.count.label\", model.getCount(), 1, 10, null);\n"
						+ //
						"		textFieldDescription = createTextField(\"ATableDetailsLayout.field.description.label\", model.getDescription());\n"
						+ //
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
						"		integerFieldCount.focus();\n" + //
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
						"	protected void remove() {\n" + //
						"		new RemoveConfirmDialog(buttonFactory, () -> {\n" + //
						"			serviceProvider.getATableService().delete(model);\n" + //
						"			observer.remove();\n" + //
						"		}, resourceManager, session).open();\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void save() {\n" + //
						"		model.setCount(integerFieldCount.getValue());\n" + //
						"		model.setDescription(textFieldDescription.getValue());\n" + //
						"		observer.save(serviceProvider.getATableService().update(model));\n" + //
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
		class DateAndTimeFields {

			private String getExpected() {
				return "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
						"\n" + //
						"import com.vaadin.flow.component.AttachEvent;\n" + //
						"import com.vaadin.flow.component.dialog.Dialog;\n" + //
						"import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" + //
						"import com.vaadin.flow.component.datetimepicker.DateTimePicker;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.service.ATableService;\n" + //
						"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
						"import base.pack.age.name.gui.SessionData;\n" + //
						"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" + //
						"import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" + //
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
						"	private final ComponentFactory componentFactory;\n" + //
						"	private final ATable model;\n" + //
						"	private final ServiceProvider serviceProvider;\n" + //
						"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
						"	private final ResourceManager resourceManager;\n" + //
						"	private final SessionData session;\n" + //
						"	private final Observer observer;\n" + //
						"	private final DetailsLayoutComboBoxItemLabelGenerator<ATable> comboBoxItemLabelGenerator;\n"
						+ //
						"\n" + //
						"	private DateTimePicker dateTimePickerAdate;\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void onAttach(AttachEvent attachEvent) {\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"		createButtons();\n" + //
						"		dateTimePickerAdate = new DateTimePicker(resourceManager.getLocalizedString(\"ATableDetailsLayout.field.adate.label\", session.getLocalization()), model.getAdate(), event -> {});\n"
						+ //
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
						"				dateTimePickerAdate,\n" + //
						"				getMasterDataButtonLayout(model.getId() > 0));\n" + //
						"		dateTimePickerAdate.focus();\n" + //
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
						"	protected void remove() {\n" + //
						"		new RemoveConfirmDialog(buttonFactory, () -> {\n" + //
						"			serviceProvider.getATableService().delete(model);\n" + //
						"			observer.remove();\n" + //
						"		}, resourceManager, session).open();\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void save() {\n" + //
						"		model.setAdate(dateTimePickerAdate.getValue());\n" + //
						"		observer.save(serviceProvider.getATableService().update(model));\n" + //
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
				tableModel.getColumnByName("ADate").setDomain(dataModel.getDomainByName("Timestamp"));
				tableModel.getColumnByName("ADate").addOption(new Option(GUI_EDITOR_POS, "1"));
				tableModel.removeColumn(tableModel.getColumnByName("Description"));
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
						"import com.vaadin.flow.component.dialog.Dialog;\n" + //
						"import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" + //
						"import com.vaadin.flow.component.combobox.ComboBox;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.AnotherTable;\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.service.AnotherTableService;\n" + //
						"import base.pack.age.name.core.service.ATableService;\n" + //
						"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
						"import base.pack.age.name.gui.SessionData;\n" + //
						"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" + //
						"import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" + //
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
						"	private final ComponentFactory componentFactory;\n" + //
						"	private final ATable model;\n" + //
						"	private final ServiceProvider serviceProvider;\n" + //
						"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
						"	private final ResourceManager resourceManager;\n" + //
						"	private final SessionData session;\n" + //
						"	private final Observer observer;\n" + //
						"	private final DetailsLayoutComboBoxItemLabelGenerator<ATable> comboBoxItemLabelGenerator;\n"

						+ //
						"\n" + //
						"	private ComboBox<AnotherTable> comboBoxRef;\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void onAttach(AttachEvent attachEvent) {\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"		createButtons();\n" + //
						"		comboBoxRef = createComboBox(\"ATableDetailsLayout.field.ref.label\", model.getRef(), serviceProvider.getAnotherTableService()"
						+ ".findAll().toArray(new AnotherTable[0]));\n" + //
						"		comboBoxRef\n" + //
						"				.setItemLabelGenerator(\n" + //
						"						anotherTable  -> comboBoxItemLabelGenerator != null\n" + //
						"								? comboBoxItemLabelGenerator.getLabel(ATable.REF, anotherTable)\n"
						+ //
						"								: \"\" + anotherTable.getName());\n" + //
						"		comboBoxRef.setClearButtonVisible(true);\n" + //
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
						"		comboBoxRef.focus();\n" + //
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
						"	protected void remove() {\n" + //
						"		new RemoveConfirmDialog(buttonFactory, () -> {\n" + //
						"			serviceProvider.getATableService().delete(model);\n" + //
						"			observer.remove();\n" + //
						"		}, resourceManager, session).open();\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void save() {\n" + //
						"		model.setRef(comboBoxRef.getValue());\n" + //
						"		observer.save(serviceProvider.getATableService().update(model));\n" + //
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
						"import com.vaadin.flow.component.dialog.Dialog;\n" + //
						"import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" + //
						"import com.vaadin.flow.component.checkbox.Checkbox;\n" + //
						"import com.vaadin.flow.component.combobox.ComboBox;\n" + //
						"import com.vaadin.flow.component.textfield.TextArea;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.EnumType;\n" + //
						"import base.pack.age.name.core.model.TableWithSpecials;\n" + //
						"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
						"import base.pack.age.name.core.service.TableWithSpecialsService;\n" + //
						"import base.pack.age.name.gui.SessionData;\n" + //
						"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" + //
						"import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" + //
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
						"	private final ComponentFactory componentFactory;\n" + //
						"	private final TableWithSpecials model;\n" + //
						"	private final ServiceProvider serviceProvider;\n" + //
						"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
						"	private final ResourceManager resourceManager;\n" + //
						"	private final SessionData session;\n" + //
						"	private final Observer observer;\n" + //
						"	private final DetailsLayoutComboBoxItemLabelGenerator<TableWithSpecials> comboBoxItemLabelGenerator;\n"
						+ //
						"\n" + //
						"	private ComboBox<EnumType> comboBoxEnumField;\n" + //
						"	private Checkbox checkboxFlag;\n" + //
						"	private TextArea textAreaLongtext;\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void onAttach(AttachEvent attachEvent) {\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"		createButtons();\n" + //
						"		comboBoxEnumField = createComboBox(\"TableWithSpecialsDetailsLayout.field.enumfield.label\", model.getEnumField(), EnumType.values(), componentFactory.getEnumTypeItemLabelGenerator());\n"
						+ //
						"		comboBoxEnumField.setClearButtonVisible(true);\n" + //
						"		checkboxFlag = createCheckbox(\"TableWithSpecialsDetailsLayout.field.flag.label\", model."
						+ (flagIsNullable ? "get" : "is") + "Flag());\n" + //
						"		textAreaLongtext = createTextArea(\"TableWithSpecialsDetailsLayout.field.longtext.label\", model.getLongtext());\n"
						+ //
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
						"				textAreaLongtext,\n" + //
						"				getMasterDataButtonLayout(model.getId() > 0));\n" + //
						"		comboBoxEnumField.focus();\n" + //
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
						"	protected void remove() {\n" + //
						"		new RemoveConfirmDialog(buttonFactory, () -> {\n" + //
						"			serviceProvider.getTableWithSpecialsService().delete(model);\n" + //
						"			observer.remove();\n" + //
						"		}, resourceManager, session).open();\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void save() {\n" + //
						"		model.setEnumField(comboBoxEnumField.getValue());\n" + //
						"		model.setFlag(checkboxFlag.getValue());\n" + //
						"		model.setLongtext(textAreaLongtext.getValue());\n" + //
						"		observer.save(serviceProvider.getTableWithSpecialsService().update(model));\n" + //
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

		@Nested
		class Numbers {

			@Test
			void happyRunForASimpleObject_withNotNullBoolean() {
				// Prepare
				String expected = "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
						"\n" + //
						"import com.vaadin.flow.component.AttachEvent;\n" + //
						"import com.vaadin.flow.component.dialog.Dialog;\n" + //
						"import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" + //
						"import com.vaadin.flow.component.textfield.IntegerField;\n" + //
						"import com.vaadin.flow.component.textfield.NumberField;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.TableWithNumberField;\n" + //
						"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
						"import base.pack.age.name.core.service.TableWithNumberFieldService;\n" + //
						"import base.pack.age.name.gui.SessionData;\n" + //
						"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" + //
						"import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" + //
						"import lombok.Generated;\n" + //
						"import lombok.RequiredArgsConstructor;\n" + //
						"\n" + //
						"/**\n" + //
						" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
						" */\n" + //
						"@Generated\n" + //
						"@RequiredArgsConstructor\n" + //
						"public class TableWithNumberFieldDetailsLayout extends AbstractMasterDataBaseLayout {\n" + //
						"\n" + //
						"	private final ButtonFactory buttonFactory;\n" + //
						"	private final ComponentFactory componentFactory;\n" + //
						"	private final TableWithNumberField model;\n" + //
						"	private final ServiceProvider serviceProvider;\n" + //
						"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
						"	private final ResourceManager resourceManager;\n" + //
						"	private final SessionData session;\n" + //
						"	private final Observer observer;\n" + //
						"	private final DetailsLayoutComboBoxItemLabelGenerator<TableWithNumberField> comboBoxItemLabelGenerator;\n"
						+ //
						"\n" + //
						"	private IntegerField integerFieldCounter;\n" + //
						"	private NumberField numberFieldAmount;\n" + //
						"	private NumberField numberFieldAmountWithLimits;\n" + //
						"	private IntegerField integerFieldCounterWithLimits;\n" + //
						"	private IntegerField integerFieldWithSpecialDomain;\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void onAttach(AttachEvent attachEvent) {\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"		createButtons();\n" + //
						"		integerFieldCounter = createIntegerField(\"TableWithNumberFieldDetailsLayout.field.counter.label\", model.getCounter(), null, null, null);\n"
						+ //
						"		numberFieldAmount = createNumberField(\"TableWithNumberFieldDetailsLayout.field.amount.label\", model.getAmount(), null, null, null);\n"
						+ //
						"		numberFieldAmountWithLimits = createNumberField(\"TableWithNumberFieldDetailsLayout.field.amountwithlimits.label\", model.getAmountWithLimits(), -999, 999.9, 1.5);\n"
						+ //
						"		integerFieldCounterWithLimits = createIntegerField(\"TableWithNumberFieldDetailsLayout.field.counterwithlimits.label\", model.getCounterWithLimits(), 0, 99, 3);\n"
						+ //
						"		integerFieldWithSpecialDomain = createIntegerField(\"TableWithNumberFieldDetailsLayout.field.withspecialdomain.label\", model.getWithSpecialDomain(), -20, 10, 2);\n"
						+ //
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
						"				integerFieldCounter,\n" + //
						"				numberFieldAmount,\n" + //
						"				numberFieldAmountWithLimits,\n" + //
						"				integerFieldCounterWithLimits,\n" + //
						"				integerFieldWithSpecialDomain,\n" + //
						"				getMasterDataButtonLayout(model.getId() > 0));\n" + //
						"		integerFieldCounter.focus();\n" + //
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
						"	protected void remove() {\n" + //
						"		new RemoveConfirmDialog(buttonFactory, () -> {\n" + //
						"			serviceProvider.getTableWithNumberFieldService().delete(model);\n" + //
						"			observer.remove();\n" + //
						"		}, resourceManager, session).open();\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void save() {\n" + //
						"		model.setCounter(integerFieldCounter.getValue());\n" + //
						"		model.setAmount(numberFieldAmount.getValue());\n" + //
						"		model.setAmountWithLimits(numberFieldAmountWithLimits.getValue());\n" + //
						"		model.setCounterWithLimits(integerFieldCounterWithLimits.getValue());\n" + //
						"		model.setWithSpecialDomain(integerFieldWithSpecialDomain.getValue());\n" + //
						"		observer.save(serviceProvider.getTableWithNumberFieldService().update(model));\n" + //
						"	}\n" + //
						"\n" + //
						"}";
				DataModel dataModel = readDataModel("Model.xml");
				TableModel tableModel = dataModel.getTableByName("TABLE_WITH_NUMBER_FIELD");
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
						"import com.vaadin.flow.component.dialog.Dialog;\n" + //
						"import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" + //
						"import com.vaadin.flow.component.checkbox.Checkbox;\n" + //
						"import com.vaadin.flow.component.textfield.TextField;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.service.ATableService;\n" + //
						"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
						"import base.pack.age.name.gui.SessionData;\n" + //
						"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" + //
						"import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" + //
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
						"	private final ComponentFactory componentFactory;\n" + //
						"	private final ATable model;\n" + //
						"	private final ServiceProvider serviceProvider;\n" + //
						"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
						"	private final ResourceManager resourceManager;\n" + //
						"	private final SessionData session;\n" + //
						"	private final Observer observer;\n" + //
						"	private final DetailsLayoutComboBoxItemLabelGenerator<ATable> comboBoxItemLabelGenerator;\n"
						+ //
						"\n" + //
						"	private TextField textFieldDescription;\n" + //
						"	private Checkbox checkboxFlag;\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void onAttach(AttachEvent attachEvent) {\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"		createButtons();\n" + //
						"		textFieldDescription = createTextField(\"ATableDetailsLayout.field.description.label\", model.getDescription());\n"
						+ //
						"		checkboxFlag = createCheckbox(\"ATableDetailsLayout.field.flag.label\", model.isFlag());\n"
						+ //
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
						"		textFieldDescription.focus();\n" + //
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
						"	protected void remove() {\n" + //
						"		new RemoveConfirmDialog(buttonFactory, () -> {\n" + //
						"			serviceProvider.getATableService().delete(model);\n" + //
						"			observer.remove();\n" + //
						"		}, resourceManager, session).open();\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void save() {\n" + //
						"		model.setDescription(textFieldDescription.getValue());\n" + //
						"		model.setFlag(checkboxFlag.getValue());\n" + //
						"		observer.save(serviceProvider.getATableService().update(model));\n" + //
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
						"import com.vaadin.flow.component.dialog.Dialog;\n" + //
						"import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" + //
						"import com.vaadin.flow.component.checkbox.Checkbox;\n" + //
						"import com.vaadin.flow.component.textfield.TextField;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.AnotherTable;\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.service.ATableService;\n" + //
						"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
						"import base.pack.age.name.gui.SessionData;\n" + //
						"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" + //
						"import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" + //
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
						"	private final ComponentFactory componentFactory;\n" + //
						"	private final AnotherTable model;\n" + //
						"	private final ServiceProvider serviceProvider;\n" + //
						"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
						"	private final ResourceManager resourceManager;\n" + //
						"	private final SessionData session;\n" + //
						"	private final Observer observer;\n" + //
						"	private final DetailsLayoutComboBoxItemLabelGenerator<ATable> comboBoxItemLabelGenerator;\n"
						+ //
						"\n" + //
						"	private TextField textFieldDescription;\n" + //
						"	private TextField textFieldName;\n" + //
						"	private Checkbox checkboxFlag;\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void onAttach(AttachEvent attachEvent) {\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"		createButtons();\n" + //
						"		textFieldDescription = createTextField(\"ATableDetailsLayout.field.description.label\", model.getDescription());\n"
						+ //
						"		textFieldName = createTextField(\"AnotherTableDetailsLayout.field.name.label\", model.getName());\n"
						+ //
						"		checkboxFlag = createCheckbox(\"ATableDetailsLayout.field.flag.label\", model.isFlag());\n"
						+ //
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
						"		textFieldDescription.focus();\n" + //
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
						"	protected void remove() {\n" + //
						"		new RemoveConfirmDialog(buttonFactory, () -> {\n" + //
						"			serviceProvider.getATableService().delete(model);\n" + //
						"			observer.remove();\n" + //
						"		}, resourceManager, session).open();\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void save() {\n" + //
						"		model.setDescription(textFieldDescription.getValue());\n" + //
						"		model.setName(textFieldName.getValue());\n" + //
						"		model.setFlag(checkboxFlag.getValue());\n" + //
						"		observer.save(serviceProvider.getATableService().update(model));\n" + //
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

			@Test
			void differentSubClassReferences() {
				// Prepare
				String expected = "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
						"\n" + //
						"import com.vaadin.flow.component.AttachEvent;\n" + //
						"import com.vaadin.flow.component.dialog.Dialog;\n" + //
						"import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" + //
						"import com.vaadin.flow.component.combobox.ComboBox;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.AnotherTable;\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.model.DifferentSubclassReferences;\n" + //
						"import base.pack.age.name.core.service.ATableService;\n" + //
						"import base.pack.age.name.core.service.DifferentSubclassReferencesService;\n" + //
						"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
						"import base.pack.age.name.gui.SessionData;\n" + //
						"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" + //
						"import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" + //
						"import lombok.Generated;\n" + //
						"import lombok.RequiredArgsConstructor;\n" + //
						"\n" + //
						"/**\n" + //
						" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
						" */\n" + //
						"@Generated\n" + //
						"@RequiredArgsConstructor\n" + //
						"public class DifferentSubclassReferencesDetailsLayout extends AbstractMasterDataBaseLayout {\n"
						+ //
						"\n" + //
						"	private final ButtonFactory buttonFactory;\n" + //
						"	private final ComponentFactory componentFactory;\n" + //
						"	private final DifferentSubclassReferences model;\n" + //
						"	private final ServiceProvider serviceProvider;\n" + //
						"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
						"	private final ResourceManager resourceManager;\n" + //
						"	private final SessionData session;\n" + //
						"	private final Observer observer;\n" + //
						"	private final DetailsLayoutComboBoxItemLabelGenerator<DifferentSubclassReferences> comboBoxItemLabelGenerator;\n"
						+ //
						"\n" + //
						"	private ComboBox<AnotherTable> comboBoxAnotherTable;\n" + //
						"	private ComboBox<ATable> comboBoxATable;\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void onAttach(AttachEvent attachEvent) {\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"		createButtons();\n" + //
						"		comboBoxAnotherTable = createComboBox(\"DifferentSubclassReferencesDetailsLayout.field.anothertable.label\", model.getAnotherTable(), serviceProvider.getATableService().findAllAnotherTable().toArray(new AnotherTable[0]));\n"
						+ //
						"		comboBoxAnotherTable\n" + //
						"				.setItemLabelGenerator(\n" + //
						"						anotherTable  -> comboBoxItemLabelGenerator != null\n" + //
						"								? comboBoxItemLabelGenerator.getLabel(DifferentSubclassReferences.ANOTHERTABLE, anotherTable)\n"
						+ //
						"								: \"\" + anotherTable.getDescription());\n" + //
						"		comboBoxATable = createComboBox(\"DifferentSubclassReferencesDetailsLayout.field.atable.label\", model.getATable(), serviceProvider.getATableService().findAll().toArray(new ATable[0]));\n"
						+ //
						"		comboBoxATable\n" + //
						"				.setItemLabelGenerator(\n" + //
						"						aTable  -> comboBoxItemLabelGenerator != null\n" + //
						"								? comboBoxItemLabelGenerator.getLabel(DifferentSubclassReferences.ATABLE, aTable)\n"
						+ //
						"								: \"\" + aTable.getDescription());\n" + //
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
						"				comboBoxAnotherTable,\n" + //
						"				comboBoxATable,\n" + //
						"				getMasterDataButtonLayout(model.getId() > 0));\n" + //
						"		comboBoxAnotherTable.focus();\n" + //
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
						"	protected void remove() {\n" + //
						"		new RemoveConfirmDialog(buttonFactory, () -> {\n" + //
						"			serviceProvider.getDifferentSubclassReferencesService().delete(model);\n" + //
						"			observer.remove();\n" + //
						"		}, resourceManager, session).open();\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void save() {\n" + //
						"		model.setAnotherTable(comboBoxAnotherTable.getValue());\n" + //
						"		model.setATable(comboBoxATable.getValue());\n" + //
						"		observer.save(serviceProvider.getDifferentSubclassReferencesService().update(model));\n"
						+ //
						"	}\n" + //
						"\n" + //
						"}";
				DataModel dataModel = readDataModel("Model-Inheritance.xml");
				TableModel tableModel = dataModel.getTableByName("DIFFERENT_SUBCLASS_REFERENCES");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
				// Check
				assertEquals(expected, returned);
			}

			@Test
			void subClassWithSubClass() {
				// Prepare
				String expected =
						"package base.pack.age.name.gui.vaadin.masterdata;\n" //
								+ "\n" //
								+ "import com.vaadin.flow.component.AttachEvent;\n" //
								+ "import com.vaadin.flow.component.dialog.Dialog;\n" //
								+ "import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" //
								+ "import com.vaadin.flow.component.combobox.ComboBox;\n" //
								+ "import com.vaadin.flow.component.textfield.TextField;\n" //
								+ "\n" //
								+ "import base.pack.age.name.core.model.BHeirHeirTable;\n" //
								+ "import base.pack.age.name.core.model.BReferencedTable;\n" //
								+ "import base.pack.age.name.core.model.BTable;\n" //
								+ "import base.pack.age.name.core.service.BReferencedTableService;\n" //
								+ "import base.pack.age.name.core.service.BTableService;\n" //
								+ "import base.pack.age.name.core.service.localization.ResourceManager;\n" //
								+ "import base.pack.age.name.gui.SessionData;\n" //
								+ "import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" //
								+ "import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" //
								+ "import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" //
								+ "import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" //
								+ "import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" //
								+ "import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" //
								+ "import lombok.Generated;\n" //
								+ "import lombok.RequiredArgsConstructor;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Generated\n" //
								+ "@RequiredArgsConstructor\n" //
								+ "public class BHeirHeirTableDetailsLayout extends AbstractMasterDataBaseLayout {\n" //
								+ "\n" //
								+ "	private final ButtonFactory buttonFactory;\n" //
								+ "	private final ComponentFactory componentFactory;\n" //
								+ "	private final BHeirHeirTable model;\n" //
								+ "	private final ServiceProvider serviceProvider;\n" //
								+ "	private final MasterDataGUIConfiguration guiConfiguration;\n" //
								+ "	private final ResourceManager resourceManager;\n" //
								+ "	private final SessionData session;\n" //
								+ "	private final Observer observer;\n" //
								+ "	private final DetailsLayoutComboBoxItemLabelGenerator<BTable> comboBoxItemLabelGenerator;\n" //
								+ "\n" //
								+ "	private ComboBox<BReferencedTable> comboBoxReference;\n" //
								+ "	private TextField textFieldDescription;\n" //
								+ "	private TextField textFieldName;\n" //
								+ "	private TextField textFieldAdditionalDescription;\n" //
								+ "\n" //
								+ "	@Override\n" //
								+ "	public void onAttach(AttachEvent attachEvent) {\n" //
								+ "		super.onAttach(attachEvent);\n" //
								+ "		createButtons();\n" //
								+ "		comboBoxReference = createComboBox(\"BTableDetailsLayout.field.reference.label\", model.getReference(), serviceProvider.getBReferencedTableService().findAll().toArray(new BReferencedTable[0]));\n" //
								+ "		comboBoxReference\n" //
								+ "				.setItemLabelGenerator(\n" //
								+ "						bReferencedTable  -> comboBoxItemLabelGenerator != null\n" //
								+ "								? comboBoxItemLabelGenerator.getLabel(BHeirHeirTable.REFERENCE, bReferencedTable)\n" //
								+ "								: \"\" + bReferencedTable.getDescription());\n" //
								+ "		textFieldDescription = createTextField(\"BTableDetailsLayout.field.description.label\", model.getDescription());\n" //
								+ "		textFieldName = createTextField(\"BHeirTableDetailsLayout.field.name.label\", model.getName());\n" //
								+ "		textFieldAdditionalDescription = createTextField(\"BHeirHeirTableDetailsLayout.field.additionaldescription.label\", model.getAdditionalDescription());\n" //
								+ "		getStyle().set(\"-moz-border-radius\", \"4px\");\n" //
								+ "		getStyle().set(\"-webkit-border-radius\", \"4px\");\n" //
								+ "		getStyle().set(\"border-radius\", \"4px\");\n" //
								+ "		getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" //
								+ "		getStyle()\n" //
								+ "				.set(\n" //
								+ "						\"box-shadow\",\n" //
								+ "						\"10px 10px 20px #e4e4e4, -10px 10px 20px #e4e4e4, -10px -10px 20px #e4e4e4, 10px -10px 20px #e4e4e4\");\n" //
								+ "		setMargin(false);\n" //
								+ "		setWidthFull();\n" //
								+ "		add(\n" //
								+ "				textFieldDescription,\n" //
								+ "				textFieldName,\n" //
								+ "				textFieldAdditionalDescription,\n" //
								+ "				comboBoxReference,\n" //
								+ "				getMasterDataButtonLayout(model.getId() > 0));\n" //
								+ "		textFieldDescription.focus();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	@Override\n" //
								+ "	protected ButtonFactory getButtonFactory() {\n" //
								+ "		return buttonFactory;\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	@Override\n" //
								+ "	protected ResourceManager getResourceManager() {\n" //
								+ "		return resourceManager;\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	@Override\n" //
								+ "	protected SessionData getSessionData() {\n" //
								+ "		return session;\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	@Override\n" //
								+ "	protected void remove() {\n" //
								+ "		new RemoveConfirmDialog(buttonFactory, () -> {\n" //
								+ "			serviceProvider.getBTableService().delete(model);\n" //
								+ "			observer.remove();\n" //
								+ "		}, resourceManager, session).open();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	@Override\n" //
								+ "	protected void save() {\n" //
								+ "		model.setDescription(textFieldDescription.getValue());\n" //
								+ "		model.setName(textFieldName.getValue());\n" //
								+ "		model.setAdditionalDescription(textFieldAdditionalDescription.getValue());\n" //
								+ "		model.setReference(comboBoxReference.getValue());\n" //
								+ "		observer.save(serviceProvider.getBTableService().update(model));\n" //
								+ "	}\n" //
								+ "\n" //
								+ "}";
				DataModel dataModel = readDataModel("Model-Inheritance.xml");
				TableModel tableModel = dataModel.getTableByName("B_HEIR_HEIR_TABLE");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class WithReferences {

			private String getExpected() {
				return "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
						"\n" + //
						"import com.vaadin.flow.component.AttachEvent;\n" + //
						"import com.vaadin.flow.component.dialog.Dialog;\n" + //
						"import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" + //
						"import com.vaadin.flow.component.combobox.ComboBox;\n" + //
						"import com.vaadin.flow.component.textfield.TextField;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.BHeirTable;\n" + //
						"import base.pack.age.name.core.model.BReferencedTable;\n" + //
						"import base.pack.age.name.core.model.BTable;\n" + //
						"import base.pack.age.name.core.service.BReferencedTableService;\n" + //
						"import base.pack.age.name.core.service.BTableService;\n" + //
						"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
						"import base.pack.age.name.gui.SessionData;\n" + //
						"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" + //
						"import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" + //
						"import lombok.Generated;\n" + //
						"import lombok.RequiredArgsConstructor;\n" + //
						"\n" + //
						"/**\n" + //
						" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
						" */\n" + //
						"@Generated\n" + //
						"@RequiredArgsConstructor\n" + //
						"public class BHeirTableDetailsLayout extends AbstractMasterDataBaseLayout {\n" + //
						"\n" + //
						"	private final ButtonFactory buttonFactory;\n" + //
						"	private final ComponentFactory componentFactory;\n" + //
						"	private final BHeirTable model;\n" + //
						"	private final ServiceProvider serviceProvider;\n" + //
						"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
						"	private final ResourceManager resourceManager;\n" + //
						"	private final SessionData session;\n" + //
						"	private final Observer observer;\n" + //
						"	private final DetailsLayoutComboBoxItemLabelGenerator<BTable> comboBoxItemLabelGenerator;\n"
						+ //
						"\n" + //
						"	private ComboBox<BReferencedTable> comboBoxReference;\n" + //
						"	private TextField textFieldDescription;\n" + //
						"	private TextField textFieldName;\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void onAttach(AttachEvent attachEvent) {\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"		createButtons();\n" + //
						"		comboBoxReference = createComboBox(\"BTableDetailsLayout.field.reference.label\", model.getReference(), serviceProvider.getBReferencedTableService().findAll().toArray(new BReferencedTable[0]));\n"
						+ //
						"		comboBoxReference\n" + //
						"				.setItemLabelGenerator(\n" + //
						"						bReferencedTable  -> comboBoxItemLabelGenerator != null\n" + //
						"								? comboBoxItemLabelGenerator.getLabel(BHeirTable.REFERENCE, bReferencedTable)\n"
						+ //
						"								: \"\" + bReferencedTable.getDescription());\n" + //
						"		textFieldDescription = createTextField(\"BTableDetailsLayout.field.description.label\", model.getDescription());\n"
						+ //
						"		textFieldName = createTextField(\"BHeirTableDetailsLayout.field.name.label\", model.getName());\n"
						+ //
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
						"				comboBoxReference,\n" + //
						"				getMasterDataButtonLayout(model.getId() > 0));\n" + //
						"		textFieldDescription.focus();\n" + //
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
						"	protected void remove() {\n" + //
						"		new RemoveConfirmDialog(buttonFactory, () -> {\n" + //
						"			serviceProvider.getBTableService().delete(model);\n" + //
						"			observer.remove();\n" + //
						"		}, resourceManager, session).open();\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void save() {\n" + //
						"		model.setDescription(textFieldDescription.getValue());\n" + //
						"		model.setName(textFieldName.getValue());\n" + //
						"		model.setReference(comboBoxReference.getValue());\n" + //
						"		observer.save(serviceProvider.getBTableService().update(model));\n" + //
						"	}\n" + //
						"\n" + //
						"}";
			}

			@Test
			void happyRun() {
				// Prepare
				String expected = getExpected();
				DataModel dataModel = readDataModel("Model-Inheritance.xml");
				TableModel tableModel = dataModel.getTableByName("B_HEIR_TABLE");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class FieldPreferences {

			@Test
			void firstFieldIsMarked_comboBox() {
				// Prepare
				String expected = "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
						"\n" + //
						"import com.vaadin.flow.component.AttachEvent;\n" + //
						"import com.vaadin.flow.component.dialog.Dialog;\n" + //
						"import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" + //
						"import com.vaadin.flow.component.checkbox.Checkbox;\n" + //
						"import com.vaadin.flow.component.combobox.ComboBox;\n" + //
						"import com.vaadin.flow.component.textfield.TextField;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.AnotherTable;\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.service.AnotherTableService;\n" + //
						"import base.pack.age.name.core.service.ATableService;\n" + //
						"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
						"import base.pack.age.name.gui.SessionData;\n" + //
						"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" + //
						"import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" + //
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
						"	private static final String COMBO_BOX_ANOTHER_TABLE_PREFERENCE_ID = \"ATableDetailsLayout.fieldAnotherTable.preference\";\n"
						+ //
						"\n" + //
						"	private final ButtonFactory buttonFactory;\n" + //
						"	private final ComponentFactory componentFactory;\n" + //
						"	private final ATable model;\n" + //
						"	private final ServiceProvider serviceProvider;\n" + //
						"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
						"	private final ResourceManager resourceManager;\n" + //
						"	private final SessionData session;\n" + //
						"	private final Observer observer;\n" + //
						"	private final DetailsLayoutComboBoxItemLabelGenerator<ATable> comboBoxItemLabelGenerator;\n"
						+ //
						"\n" + //
						"	private ComboBox<AnotherTable> comboBoxAnotherTable;\n" + //
						"	private TextField textFieldDescription;\n" + //
						"	private Checkbox checkboxFlag;\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void onAttach(AttachEvent attachEvent) {\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"		createButtons();\n" + //
						"		comboBoxAnotherTable = createComboBox(\"ATableDetailsLayout.field.anothertable.label\", model.getAnotherTable(), serviceProvider.getAnotherTableService().findAll().toArray(new AnotherTable[0]));\n"
						+ //
						"		comboBoxAnotherTable\n" + //
						"				.setItemLabelGenerator(\n" + //
						"						anotherTable  -> comboBoxItemLabelGenerator != null\n" + //
						"								? comboBoxItemLabelGenerator.getLabel(ATable.ANOTHERTABLE, anotherTable)\n"
						+ //
						"								: \"\" + anotherTable.getName());\n" + //
						"		comboBoxAnotherTable.setClearButtonVisible(true);\n" + //
						"		textFieldDescription = createTextField(\"ATableDetailsLayout.field.description.label\", model.getDescription());\n"
						+ //
						"		checkboxFlag = createCheckbox(\"ATableDetailsLayout.field.flag.label\", model.isFlag());\n"
						+ //
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
						"				comboBoxAnotherTable,\n" + //
						"				textFieldDescription,\n" + //
						"				checkboxFlag,\n" + //
						"				getMasterDataButtonLayout(model.getId() > 0));\n" + //
						"		if (model.getId() < 1) {\n" + //
						"			session\n" + //
						"					.findParameter(COMBO_BOX_ANOTHER_TABLE_PREFERENCE_ID)\n" + //
						"					.ifPresentOrElse(anotherTable -> {\n" + //
						"							comboBoxAnotherTable.setValue((AnotherTable) anotherTable);\n" + //
						"							textFieldDescription.focus();\n" + //
						"					}, () -> comboBoxAnotherTable.focus());\n" + //
						"		} else {\n" + //
						"			comboBoxAnotherTable.focus();\n" + //
						"		}\n" + //
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
						"	protected void remove() {\n" + //
						"		new RemoveConfirmDialog(buttonFactory, () -> {\n" + //
						"			serviceProvider.getATableService().delete(model);\n" + //
						"			observer.remove();\n" + //
						"		}, resourceManager, session).open();\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void save() {\n" + //
						"		session.setParameter(COMBO_BOX_ANOTHER_TABLE_PREFERENCE_ID, comboBoxAnotherTable.getValue());\n"
						+ //
						"		model.setAnotherTable(comboBoxAnotherTable.getValue());\n" + //
						"		model.setDescription(textFieldDescription.getValue());\n" + //
						"		model.setFlag(checkboxFlag.getValue());\n" + //
						"		observer.save(serviceProvider.getATableService().update(model));\n" + //
						"	}\n" + //
						"\n" + //
						"}";
				DataModel dataModel = readDataModel("Model-Preferences.xml");
				TableModel tableModel = dataModel.getTableByName("A_TABLE");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
				// Check
				assertEquals(expected, returned);
			}

			@Test
			void fieldToFocusIsAnInteger() {
				// Prepare
				String expected = "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
						"\n" + //
						"import com.vaadin.flow.component.AttachEvent;\n" + //
						"import com.vaadin.flow.component.dialog.Dialog;\n" + //
						"import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" + //
						"import com.vaadin.flow.component.textfield.IntegerField;\n" + //
						"import com.vaadin.flow.component.textfield.TextField;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.BTable;\n" + //
						"import base.pack.age.name.core.service.BTableService;\n" + //
						"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
						"import base.pack.age.name.gui.SessionData;\n" + //
						"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" + //
						"import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" + //
						"import lombok.Generated;\n" + //
						"import lombok.RequiredArgsConstructor;\n" + //
						"\n" + //
						"/**\n" + //
						" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
						" */\n" + //
						"@Generated\n" + //
						"@RequiredArgsConstructor\n" + //
						"public class BTableDetailsLayout extends AbstractMasterDataBaseLayout {\n" + //
						"\n" + //
						"	private static final String TEXT_FIELD_DESCRIPTION_PREFERENCE_ID = \"BTableDetailsLayout.fieldDescription.preference\";\n"
						+ //
						"\n" + //
						"	private final ButtonFactory buttonFactory;\n" + //
						"	private final ComponentFactory componentFactory;\n" + //
						"	private final BTable model;\n" + //
						"	private final ServiceProvider serviceProvider;\n" + //
						"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
						"	private final ResourceManager resourceManager;\n" + //
						"	private final SessionData session;\n" + //
						"	private final Observer observer;\n" + //
						"	private final DetailsLayoutComboBoxItemLabelGenerator<BTable> comboBoxItemLabelGenerator;\n"
						+ //
						"\n" + //
						"	private TextField textFieldDescription;\n" + //
						"	private IntegerField integerFieldCounter;\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void onAttach(AttachEvent attachEvent) {\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"		createButtons();\n" + //
						"		textFieldDescription = createTextField(\"BTableDetailsLayout.field.description.label\", model.getDescription());\n"
						+ //

						"		integerFieldCounter = createIntegerField(\"BTableDetailsLayout.field.counter.label\", model.getCounter(), null, null, null);\n"
						+ //
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
						"				integerFieldCounter,\n" + //
						"				getMasterDataButtonLayout(model.getId() > 0));\n" + //
						"		if (model.getId() < 1) {\n" + //
						"			session\n" + //
						"					.findParameter(TEXT_FIELD_DESCRIPTION_PREFERENCE_ID)\n" + //
						"					.ifPresentOrElse(description -> {\n" + //
						"							textFieldDescription.setValue((String) description);\n" + //
						"							integerFieldCounter.focus();\n" + //
						"					}, () -> textFieldDescription.focus());\n" + //
						"		} else {\n" + //
						"			textFieldDescription.focus();\n" + //
						"		}\n" + //
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
						"	protected void remove() {\n" + //
						"		new RemoveConfirmDialog(buttonFactory, () -> {\n" + //
						"			serviceProvider.getBTableService().delete(model);\n" + //
						"			observer.remove();\n" + //
						"		}, resourceManager, session).open();\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void save() {\n" + //
						"		session.setParameter(TEXT_FIELD_DESCRIPTION_PREFERENCE_ID, textFieldDescription.getValue());\n"
						+ //
						"		model.setDescription(textFieldDescription.getValue());\n" + //
						"		model.setCounter(integerFieldCounter.getValue());\n" + //
						"		observer.save(serviceProvider.getBTableService().update(model));\n" + //
						"	}\n" + //
						"\n" + //
						"}";
				DataModel dataModel = readDataModel("Model-Preferences.xml");
				TableModel tableModel = dataModel.getTableByName("B_TABLE");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

	@Nested
	class Blobs {

		@Test
		void createsAnUploadComponent() {
			// Prepare
			String expected =
					"package base.pack.age.name.gui.vaadin.masterdata;\n" //
							+ "\n" //
							+ "import java.io.IOException;\n" //
							+ "import java.io.InputStream;\n" //
							+ "\n" //
							+ "import com.vaadin.flow.component.AttachEvent;\n" //
							+ "import com.vaadin.flow.component.dialog.Dialog;\n" //
							+ "import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" //
							+ "import com.vaadin.flow.component.upload.Upload;\n"
							+ "import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;\n" //
							+ "\n" //
							+ "import base.pack.age.name.core.model.BlobTable;\n" //
							+ "import base.pack.age.name.core.service.BlobTableService;\n" //
							+ "import base.pack.age.name.core.service.localization.ResourceManager;\n" //
							+ "import base.pack.age.name.gui.SessionData;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" //
							+ "import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" //
							+ "import lombok.Generated;\n" //
							+ "import lombok.RequiredArgsConstructor;\n" //
							+ "\n" //
							+ "/**\n" //
							+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
							+ " */\n" //
							+ "@Generated\n" //
							+ "@RequiredArgsConstructor\n" //
							+ "public class BlobTableDetailsLayout extends AbstractMasterDataBaseLayout {\n" //
							+ "\n" //
							+ "	private final ButtonFactory buttonFactory;\n" //
							+ "	private final ComponentFactory componentFactory;\n" //
							+ "	private final BlobTable model;\n" //
							+ "	private final ServiceProvider serviceProvider;\n" //
							+ "	private final MasterDataGUIConfiguration guiConfiguration;\n" //
							+ "	private final ResourceManager resourceManager;\n" //
							+ "	private final SessionData session;\n" //
							+ "	private final Observer observer;\n" //
							+ "	private final DetailsLayoutComboBoxItemLabelGenerator<BlobTable> comboBoxItemLabelGenerator;\n" //
							+ "\n" //
							+ "	private Upload uploadBlob;\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public void onAttach(AttachEvent attachEvent) {\n" //
							+ "		super.onAttach(attachEvent);\n" //
							+ "		createButtons();\n" //
							+ "		MultiFileMemoryBuffer bufferBlob = new MultiFileMemoryBuffer();\n" //
							+ "		uploadBlob = new Upload(bufferBlob);\n" //
							+ "		uploadBlob.setMaxFiles(1);\n" //
							+ "		uploadBlob.setMaxFileSize(Integer.MAX_VALUE);\n" //
							+ "		uploadBlob.addSucceededListener(event -> {\n" //
							+ "			System.out.println(\"upload (Blob) started.\");\n" //
							+ "			String fileName = event.getFileName();\n" //
							+ "			InputStream inputStream = bufferBlob.getInputStream(fileName);\n" //
							+ "			System.out.println(\"upload complete!\");\n" //
							+ "			try {\n" //
							+ "				model.setBlob(inputStream.readAllBytes());\n" //
							+ "			} catch (IOException ioe) {\n" //
							+ "				System.out.println(\"while uploading file: \" + fileName);\n" //
							+ "				ioe.printStackTrace();\n" //
							+ "			} finally {\n" //
							+ "				try {\n" //
							+ "					inputStream.close();\n" //
							+ "				} catch (Exception e) {\n" //
							+ "					e.printStackTrace();\n" //
							+ "				}\n" //
							+ "			}\n" //
							+ "		});\n" //
							+ "		uploadBlob.setWidthFull();\n" //
							+ "		getStyle().set(\"-moz-border-radius\", \"4px\");\n" //
							+ "		getStyle().set(\"-webkit-border-radius\", \"4px\");\n" //
							+ "		getStyle().set(\"border-radius\", \"4px\");\n" //
							+ "		getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" //
							+ "		getStyle()\n" //
							+ "				.set(\n" //
							+ "						\"box-shadow\",\n" //
							+ "						\"10px 10px 20px #e4e4e4, -10px 10px 20px #e4e4e4, -10px -10px 20px #e4e4e4, 10px -10px 20px #e4e4e4\");\n" //
							+ "		setMargin(false);\n" //
							+ "		setWidthFull();\n" //
							+ "		add(\n" //
							+ "				uploadBlob,\n" //
							+ "				getMasterDataButtonLayout(model.getId() > 0));\n" //
							+ "		textFieldBlob.focus();\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	protected ButtonFactory getButtonFactory() {\n" //
							+ "		return buttonFactory;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	protected ResourceManager getResourceManager() {\n" //
							+ "		return resourceManager;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	protected SessionData getSessionData() {\n" //
							+ "		return session;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	protected void remove() {\n" //
							+ "		new RemoveConfirmDialog(buttonFactory, () -> {\n" //
							+ "			serviceProvider.getBlobTableService().delete(model);\n" //
							+ "			observer.remove();\n" //
							+ "		}, resourceManager, session).open();\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	protected void save() {\n" //
							+ "		observer.save(serviceProvider.getBlobTableService().update(model));\n" //
							+ "	}\n" //
							+ "\n" //
							+ "}";
			DataModel dataModel = readDataModel("Model.xml");
			TableModel tableModel = dataModel.getTableByName("BLOB_TABLE");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class SimpleClass {

		@Test
		void happyRunForAnObjectWithAMemberList() {
			// Prepare
			String expected =
					"package de.ollie.bookstore.gui.vaadin.masterdata;\n" //
							+ "\n" //
							+ "import com.vaadin.flow.component.AttachEvent;\n" //
							+ "import com.vaadin.flow.component.dialog.Dialog;\n" //
							+ "import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" //
							+ "import com.vaadin.flow.component.combobox.ComboBox;\n" //
							+ "import com.vaadin.flow.component.textfield.TextField;\n" //
							+ "\n" //
							+ "import de.ollie.bookstore.core.model.Book;\n" //
							+ "import de.ollie.bookstore.core.model.PublicationType;\n" //
							+ "import de.ollie.bookstore.core.service.BookService;\n" //
							+ "import de.ollie.bookstore.core.service.localization.ResourceManager;\n" //
							+ "import de.ollie.bookstore.gui.SessionData;\n" //
							+ "import de.ollie.bookstore.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" //
							+ "import de.ollie.bookstore.gui.vaadin.component.ButtonFactory;\n" //
							+ "import de.ollie.bookstore.gui.vaadin.component.ComponentFactory;\n" //
							+ "import de.ollie.bookstore.gui.vaadin.masterdata.layout.list.ChapterListDetailsLayout;\n" //
							+ "import de.ollie.bookstore.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" //
							+ "import de.ollie.bookstore.gui.vaadin.component.RemoveConfirmDialog;\n" //
							+ "import de.ollie.bookstore.gui.vaadin.component.ServiceProvider;\n" //
							+ "import lombok.Generated;\n" //
							+ "import lombok.RequiredArgsConstructor;\n" //
							+ "\n" //
							+ "/**\n" //
							+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
							+ " */\n" //
							+ "@Generated\n" //
							+ "@RequiredArgsConstructor\n" //
							+ "public class BookDetailsLayout extends AbstractMasterDataBaseLayout {\n" //
							+ "\n" //
							+ "	private final ButtonFactory buttonFactory;\n" //
							+ "	private final ComponentFactory componentFactory;\n" //
							+ "	private final Book model;\n" //
							+ "	private final ServiceProvider serviceProvider;\n" //
							+ "	private final MasterDataGUIConfiguration guiConfiguration;\n" //
							+ "	private final ResourceManager resourceManager;\n" //
							+ "	private final SessionData session;\n" //
							+ "	private final Observer observer;\n" //
							+ "	private final DetailsLayoutComboBoxItemLabelGenerator<Book> comboBoxItemLabelGenerator;\n" //
							+ "\n" //
							+ "	private TextField textFieldTitle;\n" //
							+ "	private TextField textFieldIsbn;\n" //
							+ "	private ComboBox<PublicationType> comboBoxPublicationType;\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public void onAttach(AttachEvent attachEvent) {\n" //
							+ "		super.onAttach(attachEvent);\n" //
							+ "		createButtons();\n" //
							+ "		textFieldTitle = createTextField(\"BookDetailsLayout.field.title.label\", model.getTitle());\n" //
							+ "		textFieldIsbn = createTextField(\"BookDetailsLayout.field.isbn.label\", model.getIsbn());\n" //
							+ "		comboBoxPublicationType = createComboBox(\"BookDetailsLayout.field.publicationtype.label\", model.getPublicationType(), PublicationType.values(), componentFactory.getPublicationTypeItemLabelGenerator());\n" //
							+ "		getStyle().set(\"-moz-border-radius\", \"4px\");\n" //
							+ "		getStyle().set(\"-webkit-border-radius\", \"4px\");\n" //
							+ "		getStyle().set(\"border-radius\", \"4px\");\n" //
							+ "		getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" //
							+ "		getStyle()\n" //
							+ "				.set(\n" //
							+ "						\"box-shadow\",\n" //
							+ "						\"10px 10px 20px #e4e4e4, -10px 10px 20px #e4e4e4, -10px -10px 20px #e4e4e4, 10px -10px 20px #e4e4e4\");\n" //
							+ "		setMargin(false);\n" //
							+ "		setWidthFull();\n" //
							+ "		add(\n" //
							+ "				textFieldTitle,\n" //
							+ "				textFieldIsbn,\n" //
							+ "				comboBoxPublicationType,\n" //
							+ "				new ChapterListDetailsLayout(componentFactory, guiConfiguration, model, resourceManager, serviceProvider, session),\n" //
							+ "				getMasterDataButtonLayout(model.getId() > 0));\n" //
							+ "		textFieldTitle.focus();\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	protected ButtonFactory getButtonFactory() {\n" //
							+ "		return buttonFactory;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	protected ResourceManager getResourceManager() {\n" //
							+ "		return resourceManager;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	protected SessionData getSessionData() {\n" //
							+ "		return session;\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	protected void remove() {\n" //
							+ "		new RemoveConfirmDialog(buttonFactory, () -> {\n" //
							+ "			serviceProvider.getBookService().delete(model);\n" //
							+ "			observer.remove();\n" //
							+ "		}, resourceManager, session).open();\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	protected void save() {\n" //
							+ "		model.setTitle(textFieldTitle.getValue());\n" //
							+ "		model.setIsbn(textFieldIsbn.getValue());\n" //
							+ "		model.setPublicationType(comboBoxPublicationType.getValue());\n" //
							+ "		observer.save(serviceProvider.getBookService().update(model));\n" //
							+ "	}\n" //
							+ "\n" //
							+ "}";
			DataModel dataModel = readDataModel("Example-BookStore.xml", EXAMPLE_XMLS);
			TableModel tableModel = dataModel.getTableByName("BOOK");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
			// Check
			assertEquals(expected, returned);
		}

	}

}