package archimedes.codegenerators.gui.vaadin;

import static archimedes.codegenerators.DataModelReader.EXAMPLE_XMLS;
import static archimedes.codegenerators.DataModelReader.readDataModel;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.model.DataModel;
import archimedes.model.TableModel;

@ExtendWith(MockitoExtension.class)
public class DetailsDialogClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private DetailsDialogClassCodeGenerator unitUnderTest;

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Nested
		class SimpleClass {

			@Test
			void happyRunForASimpleObject() {
				String expected =
						"package de.ollie.bookstore.gui.vaadin.masterdata.dialog;\n" //
								+ "\n" //
								+ "import com.vaadin.flow.component.AttachEvent;\n" //
								+ "import com.vaadin.flow.component.dialog.Dialog;\n" //
								+ "import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" //
								+ "import com.vaadin.flow.component.combobox.ComboBox;\n" //
								+ "import com.vaadin.flow.component.textfield.TextField;\n" //
								+ "\n" //
								+ "import de.ollie.bookstore.gui.vaadin.component.Button;\n" //
								+ "import de.ollie.bookstore.core.model.Book;\n" //
								+ "import de.ollie.bookstore.core.model.PublicationType;\n" //
								+ "import de.ollie.bookstore.gui.SessionData;\n" //
								+ "import de.ollie.bookstore.gui.vaadin.component.ComponentFactory;\n" //
								+ "import de.ollie.bookstore.gui.vaadin.masterdata.layout.list.ChapterListDetailsLayout;\n" //
								+ "import de.ollie.bookstore.gui.vaadin.component.ServiceProvider;\n" //
								+ "import de.ollie.bookstore.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" //
								+ "\n" //
								+ "import lombok.Generated;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Generated\n" //
								+ "public class BookDetailsDialog extends Dialog {\n" //
								+ "\n" //
								+ "	public interface Observer {\n" //
								+ "\n" //
								+ "		void changed(Book model, boolean newItem);\n" //
								+ "\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private final ComponentFactory componentFactory;\n" //
								+ "	private final MasterDataGUIConfiguration guiConfiguration;\n" //
								+ "	private final Observer observer;\n" //
								+ "	private final ServiceProvider serviceProvider;\n" //
								+ "	private final SessionData session;\n" //
								+ "\n" //
								+ "	private Button buttonCancel;\n" //
								+ "	private Button buttonSave;\n" //
								+ "	private TextField textFieldTitle;\n" //
								+ "	private TextField textFieldIsbn;\n" //
								+ "	private ComboBox<PublicationType> comboBoxPublicationType;\n" //
								+ "	private VerticalLayout mainLayout;\n" //
								+ "\n" //
								+ "	private Book model = new Book();\n" //
								+ "	private boolean newItem;\n" //
								+ "\n" //
								+ "	public BookDetailsDialog(ComponentFactory componentFactory, MasterDataGUIConfiguration guiConfiguration,\n" //
								+ "			Observer observer, SessionData session, Book model, ServiceProvider serviceProvider, boolean newItem) {\n" //
								+ "		this.componentFactory = componentFactory;\n" //
								+ "		this.guiConfiguration = guiConfiguration;\n" //
								+ "		this.newItem = newItem;\n" //
								+ "		this.observer = observer;\n" //
								+ "		this.serviceProvider = serviceProvider;\n" //
								+ "		this.session = session;\n" //
								+ "		if (model != null) {\n" //
								+ "			this.model.setTitle(model.getTitle());\n" //
								+ "			this.model.setIsbn(model.getIsbn());\n" //
								+ "			this.model.setPublicationType(model.getPublicationType());\n" //
								+ "		}\n" //
								+ "		mainLayout = new VerticalLayout();\n" //
								+ "		addComponents();\n" //
								+ "		buttonCancel = componentFactory.createCancelButton(event -> close(), session);\n" //
								+ "		buttonCancel.setWidthFull();\n" //
								+ "		buttonSave = componentFactory.createSaveButton(event -> save(), session);\n" //
								+ "		buttonSave.setWidthFull();\n" //
								+ "		mainLayout.add(buttonCancel, buttonSave);\n" //
								+ "		setWidth(\"90%\");\n" //
								+ "		add(mainLayout);\n" //
								+ "		updateSaveButton();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private void addComponents() {\n" //
								+ "		textFieldTitle = componentFactory.createTextField(\"BookDetailsLayout.field.title.label\", model.getTitle(), session);\n" //
								+ "		textFieldTitle.addValueChangeListener(event -> {\n" //
								+ "			model.setTitle(event.getValue());\n" //
								+ "			updateSaveButton();\n" //
								+ "		});\n" //
								+ "		textFieldIsbn = componentFactory.createTextField(\"BookDetailsLayout.field.isbn.label\", model.getIsbn(), session);\n" //
								+ "		textFieldIsbn.addValueChangeListener(event -> {\n" //
								+ "			model.setIsbn(event.getValue());\n" //
								+ "			updateSaveButton();\n" //
								+ "		});\n" //
								+ "		comboBoxPublicationType = componentFactory.createComboBox(\"BookDetailsLayout.field.publicationtype.label\", model.getPublicationType(), PublicationType.values(), componentFactory.getPublicationTypeItemLabelGenerator(), session);\n" //
								+ "		comboBoxPublicationType.addValueChangeListener(event -> {\n"
								+ "			model.setPublicationType(event.getValue());\n" //
								+ "			updateSaveButton();\n" //
								+ "		});\n" //
								+ "		mainLayout.add(\n" //
								+ "				textFieldTitle,\n" //
								+ "				textFieldIsbn,\n" //
								+ "				comboBoxPublicationType\n" //
								+ "		);\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private void updateSaveButton() {\n" //
								+ "		setButtonEnabled(buttonSave,\n" //
								+ "				(textFieldTitle.getValue() != null) &&\n" //
								+ "				(textFieldIsbn.getValue() != null) && !textFieldIsbn.isEmpty() &&\n" //
								+ "				(comboBoxPublicationType.getValue() != null)\n" //
								+ "		);\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private void setButtonEnabled(Button button, boolean enabled) {\n" //
								+ "		button.setEnabled(enabled);\n" //
								+ "		if (enabled) {\n" //
								+ "			button.setBackgroundImage(guiConfiguration.getButtonEnabledBackgroundFileName());\n" //
								+ "			button.setBorderColor(guiConfiguration.getButtonEnabledBorderColor());\n" //
								+ "		} else {\n" //
								+ "			button.setBackgroundImage(guiConfiguration.getButtonDisabledBackgroundFileName());\n" //
								+ "			button.setBorderColor(guiConfiguration.getButtonDisabledBorderColor());\n" //
								+ "		}\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private void save() {\n" //
								+ "		if (observer != null) {\n" //
								+ "			observer.changed(model, newItem);\n" //
								+ "		}\n" //
								+ "		close();\n" //
								+ "	}\n" //
								+ "}";
				DataModel dataModel = readDataModel("Example-BookStore.xml", EXAMPLE_XMLS);
				TableModel tableModel = dataModel.getTableByName("BOOK");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class ManyToManyRelation {

			@Test
			void happyRunForASimpleObject() {
				String expected =
						"package de.ollie.fahrtenbuch.gui.vaadin.masterdata.dialog;\n" //
								+ "\n" //
								+ "import com.vaadin.flow.component.AttachEvent;\n" //
								+ "import com.vaadin.flow.component.dialog.Dialog;\n" //
								+ "import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" //
								+ "import com.vaadin.flow.component.combobox.ComboBox;\n" //
								+ "\n" //
								+ "import de.ollie.fahrtenbuch.gui.vaadin.component.Button;\n" //
								+ "import de.ollie.fahrtenbuch.core.model.FahrtrouteStrasse;\n" //
								+ "import de.ollie.fahrtenbuch.core.model.Strasse;\n" //
								+ "import de.ollie.fahrtenbuch.core.service.StrasseService;\n" //
								+ "import de.ollie.fahrtenbuch.gui.SessionData;\n" //
								+ "import de.ollie.fahrtenbuch.gui.vaadin.component.ComponentFactory;\n" //
								+ "import de.ollie.fahrtenbuch.gui.vaadin.component.ServiceProvider;\n" //
								+ "import de.ollie.fahrtenbuch.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" //
								+ "\n" //
								+ "import lombok.Generated;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Generated\n" //
								+ "public class FahrtrouteStrasseDetailsDialog extends Dialog {\n" //
								+ "\n" //
								+ "	public interface Observer {\n" //
								+ "\n" //
								+ "		void changed(FahrtrouteStrasse model, boolean newItem);\n" //
								+ "\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private final ComponentFactory componentFactory;\n" //
								+ "	private final MasterDataGUIConfiguration guiConfiguration;\n" //
								+ "	private final Observer observer;\n" //
								+ "	private final ServiceProvider serviceProvider;\n" //
								+ "	private final SessionData session;\n" //
								+ "\n" //
								+ "	private Button buttonCancel;\n" //
								+ "	private Button buttonSave;\n" //
								+ "	private ComboBox<Strasse> comboBoxStrasse;\n" //
								+ "	private VerticalLayout mainLayout;\n" //
								+ "\n" //
								+ "	private FahrtrouteStrasse model = new FahrtrouteStrasse();\n" //
								+ "	private boolean newItem;\n" //
								+ "\n" //
								+ "	public FahrtrouteStrasseDetailsDialog(ComponentFactory componentFactory, MasterDataGUIConfiguration guiConfiguration,\n" //
								+ "			Observer observer, SessionData session, FahrtrouteStrasse model, ServiceProvider serviceProvider, boolean newItem) {\n" //
								+ "		this.componentFactory = componentFactory;\n" //
								+ "		this.guiConfiguration = guiConfiguration;\n" //
								+ "		this.newItem = newItem;\n" //
								+ "		this.observer = observer;\n" //
								+ "		this.serviceProvider = serviceProvider;\n" //
								+ "		this.session = session;\n" //
								+ "		if (model != null) {\n" //
								+ "			this.model.setStrasse(model.getStrasse());\n" //
								+ "		}\n" //
								+ "		mainLayout = new VerticalLayout();\n" //
								+ "		addComponents();\n" //
								+ "		buttonCancel = componentFactory.createCancelButton(event -> close(), session);\n" //
								+ "		buttonCancel.setWidthFull();\n" //
								+ "		buttonSave = componentFactory.createSaveButton(event -> save(), session);\n" //
								+ "		buttonSave.setWidthFull();\n" //
								+ "		mainLayout.add(buttonCancel, buttonSave);\n" //
								+ "		setWidth(\"90%\");\n" //
								+ "		add(mainLayout);\n" //
								+ "		updateSaveButton();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private void addComponents() {\n" //
								+ "		comboBoxStrasse =\n" //
								+ "				componentFactory\n" //
								+ "						.createComboBox(\n" //
								+ "								\"FahrtrouteStrasseDetailsLayout.field.strasse.label\",\n" //
								+ "								model.getStrasse(),\n" //
								+ "								serviceProvider.getStrasseService().findAll().toArray(new Strasse[0]),\n" //
								+ "								session);\n" //
								+ "		comboBoxStrasse\n" //
								+ "				.setItemLabelGenerator(\n" //
								+ "						value -> componentFactory.getStrasseItemLabelGenerator() != null\n" //
								+ "								? componentFactory.getStrasseItemLabelGenerator().apply(value)\n" //
								+ "								: \"\" + value.getName());\n" //
								+ "		comboBoxStrasse.addValueChangeListener(event -> {\n" //
								+ "			model.setStrasse(event.getValue());\n" //
								+ "			updateSaveButton();\n" //
								+ "		});\n" //
								+ "		mainLayout.add(\n" //
								+ "				comboBoxStrasse\n" //
								+ "		);\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private void updateSaveButton() {\n" //
								+ "		setButtonEnabled(buttonSave,\n" //
								+ "				(" + "comboBoxStrasse.getValue() != null)\n" //
								+ "		);\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private void setButtonEnabled(Button button, boolean enabled) {\n" //
								+ "		button.setEnabled(enabled);\n" //
								+ "		if (enabled) {\n" //
								+ "			button.setBackgroundImage(guiConfiguration.getButtonEnabledBackgroundFileName());\n" //
								+ "			button.setBorderColor(guiConfiguration.getButtonEnabledBorderColor());\n" //
								+ "		} else {\n" //
								+ "			button.setBackgroundImage(guiConfiguration.getButtonDisabledBackgroundFileName());\n" //
								+ "			button.setBorderColor(guiConfiguration.getButtonDisabledBorderColor());\n" //
								+ "		}\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private void save() {\n" //
								+ "		if (observer != null) {\n" //
								+ "			observer.changed(model, newItem);\n" //
								+ "		}\n" //
								+ "		close();\n" //
								+ "	}\n" //
								+ "}";
				DataModel dataModel = readDataModel("Example-Fahrtenbuch.xml", EXAMPLE_XMLS);
				TableModel tableModel = dataModel.getTableByName("FAHRTROUTE_STRASSE");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class MemberList {

			@Test
			void happyRunForMemberReferencesMember() {
				String expected =
						"package de.ollie.task.gui.vaadin.masterdata.dialog;\n" //
								+ "\n" //
								+ "import com.vaadin.flow.component.AttachEvent;\n" //
								+ "import com.vaadin.flow.component.dialog.Dialog;\n" //
								+ "import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" //
								+ "import com.vaadin.flow.component.combobox.ComboBox;\n" //
								+ "import com.vaadin.flow.component.datetimepicker.DateTimePicker;\n" //
								+ "import com.vaadin.flow.component.textfield.TextArea;\n" //
								+ "import com.vaadin.flow.component.textfield.TextField;\n" //
								+ "\n" //
								+ "import de.ollie.task.gui.vaadin.component.Button;\n" //
								+ "import de.ollie.task.core.model.Todo;\n" //
								+ "import de.ollie.task.core.model.Task;\n" //
								+ "import de.ollie.task.core.model.Note;\n" //
								+ "import de.ollie.task.core.model.NoteType;\n" //
								+ "import de.ollie.task.gui.SessionData;\n" //
								+ "import de.ollie.task.gui.vaadin.component.ComponentFactory;\n" //
								+ "import de.ollie.task.gui.vaadin.component.ServiceProvider;\n" //
								+ "import de.ollie.task.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" //
								+ "\n" //
								+ "import lombok.Generated;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Generated\n" //
								+ "public class NoteDetailsDialog extends Dialog {\n" //
								+ "\n" //
								+ "	public interface Observer {\n" //
								+ "\n" //
								+ "		void changed(Note model, boolean newItem);\n" //
								+ "\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private final ComponentFactory componentFactory;\n" //
								+ "	private final MasterDataGUIConfiguration guiConfiguration;\n" //
								+ "	private final Observer observer;\n" //
								+ "	private final ServiceProvider serviceProvider;\n" //
								+ "	private final SessionData session;\n" //
								+ "\n" //
								+ "	private Button buttonCancel;\n" //
								+ "	private Button buttonSave;\n" //
								+ "	private TextField textFieldTitle;\n" //
								+ "	private DateTimePicker dateTimePickerCreationDate;\n" //
								+ "	private TextField textFieldUrl;\n" //
								+ "	private ComboBox<NoteType> comboBoxType;\n" //
								+ "	private ComboBox<Todo> comboBoxRelatedTodo;\n" //
								+ "	private TextArea textAreaDescription;\n" //
								+ "	private VerticalLayout mainLayout;\n" //
								+ "\n" //
								+ "	private Note model = new Note();\n" //
								+ "	private boolean newItem;\n" //
								+ "	private Task parent;" + "\n" //
								+ "\n" //
								+ "	public NoteDetailsDialog(ComponentFactory componentFactory, MasterDataGUIConfiguration guiConfiguration,\n" //
								+ "			Observer observer, SessionData session, Note model, ServiceProvider serviceProvider, boolean newItem, Task parent) {\n" //
								+ "		this.componentFactory = componentFactory;\n" //
								+ "		this.guiConfiguration = guiConfiguration;\n" //
								+ "		this.newItem = newItem;\n" //
								+ "		this.observer = observer;\n" //
								+ "		this.serviceProvider = serviceProvider;\n" //
								+ "		this.session = session;\n" //
								+ "		this.parent = parent;\n" //
								+ "		if (model != null) {\n" //
								+ "			this.model.setTitle(model.getTitle());\n" //
								+ "			this.model.setCreationDate(model.getCreationDate());\n" //
								+ "			this.model.setUrl(model.getUrl());\n" //
								+ "			this.model.setType(model.getType());\n" //
								+ "			this.model.setRelatedTodo(model.getRelatedTodo());\n" //
								+ "			this.model.setDescription(model.getDescription());\n" //
								+ "		}\n" //
								+ "		mainLayout = new VerticalLayout();\n" //
								+ "		addComponents();\n" //
								+ "		buttonCancel = componentFactory.createCancelButton(event -> close(), session);\n" //
								+ "		buttonCancel.setWidthFull();\n" //
								+ "		buttonSave = componentFactory.createSaveButton(event -> save(), session);\n" //
								+ "		buttonSave.setWidthFull();\n" //
								+ "		mainLayout.add(buttonCancel, buttonSave);\n" //
								+ "		setWidth(\"90%\");\n" //
								+ "		add(mainLayout);\n" //
								+ "		updateSaveButton();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private void addComponents() {\n" //
								+ "		textFieldTitle = componentFactory.createTextField(\"NoteDetailsLayout.field.title.label\", model.getTitle(), session);\n" //
								+ "		textFieldTitle.addValueChangeListener(event -> {\n" //
								+ "			model.setTitle(event.getValue());\n" //
								+ "			updateSaveButton();\n" //
								+ "		});\n" //
								+ "		dateTimePickerCreationDate = componentFactory.createDateTimePicker(\"NoteDetailsLayout.field.creationdate.label\", session.getLocalization(), model.getCreationDate(), event -> {\n" //
								+ "			model.setCreationDate(event.getValue());\n" //
								+ "			updateSaveButton();\n" //
								+ "		});\n" //
								+ "		textFieldUrl = componentFactory.createTextField(\"NoteDetailsLayout.field.url.label\", model.getUrl(), session);\n" //
								+ "		textFieldUrl.addValueChangeListener(event -> {\n" //
								+ "			model.setUrl(event.getValue());\n" //
								+ "			updateSaveButton();\n" //
								+ "		});\n" //
								+ "		comboBoxType = componentFactory.createComboBox(\"NoteDetailsLayout.field.type.label\", model.getType(), NoteType.values(), componentFactory.getNoteTypeItemLabelGenerator(), session);\n" //
								+ "		comboBoxType.addValueChangeListener(event -> {\n" //
								+ "			model.setType(event.getValue());\n" //
								+ "			updateSaveButton();\n" //
								+ "		});\n" //
								+ "		comboBoxRelatedTodo =\n" //
								+ "				componentFactory\n" //
								+ "						.createComboBox(\n" //
								+ "								\"NoteDetailsLayout.field.relatedtodo.label\",\n" //
								+ "								model.getRelatedTodo(),\n" //
								+ "								serviceProvider.getTodoService().findAll().toArray(new Todo[0]),\n" //
								+ "								session);\n" //
								+ "		comboBoxRelatedTodo\n" //
								+ "				.setItemLabelGenerator(\n" //
								+ "						value -> componentFactory.getTodoItemLabelGenerator() != null\n" //
								+ "								? componentFactory.getTodoItemLabelGenerator().apply(value)\n" //
								+ "								: \"\" + value.getName());\n" //
								+ "		comboBoxRelatedTodo.addValueChangeListener(event -> {\n" //
								+ "			model.setRelatedTodo(event.getValue());\n" //
								+ "			updateSaveButton();\n" //
								+ "		});\n" //
								+ "		textAreaDescription = componentFactory.createTextArea(\"NoteDetailsLayout.field.description.label\", model.getDescription(), session);\n" //
								+ "		textAreaDescription.addValueChangeListener(event -> {\n" //
								+ "			model.setDescription(event.getValue());\n" //
								+ "			updateSaveButton();\n" //
								+ "		});\n" //
								+ "		mainLayout.add(\n" //
								+ "				textFieldTitle,\n" //
								+ "				dateTimePickerCreationDate,\n" //
								+ "				textFieldUrl,\n" //
								+ "				comboBoxType,\n" //
								+ "				comboBoxRelatedTodo,\n" //
								+ "				textAreaDescription\n" //
								+ "		);\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private void updateSaveButton() {\n" //
								+ "		setButtonEnabled(buttonSave,\n" //
								+ "				(textFieldTitle.getValue() != null) &&\n" //
								+ "				(dateTimePickerCreationDate.getValue() != null) &&\n" //
								+ "				(comboBoxType.getValue() != null)\n" //
								+ "		);\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private void setButtonEnabled(Button button, boolean enabled) {\n" //
								+ "		button.setEnabled(enabled);\n" //
								+ "		if (enabled) {\n" //
								+ "			button.setBackgroundImage(guiConfiguration.getButtonEnabledBackgroundFileName());\n" //
								+ "			button.setBorderColor(guiConfiguration.getButtonEnabledBorderColor());\n" //
								+ "		} else {\n" //
								+ "			button.setBackgroundImage(guiConfiguration.getButtonDisabledBackgroundFileName());\n" //
								+ "			button.setBorderColor(guiConfiguration.getButtonDisabledBorderColor());\n" //
								+ "		}\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private void save() {\n" //
								+ "		if (observer != null) {\n" //
								+ "			observer.changed(model, newItem);\n" //
								+ "		}\n" //
								+ "		close();\n" //
								+ "	}\n" //
								+ "}";
				DataModel dataModel = readDataModel("Example-Tasks.xml", EXAMPLE_XMLS);
				TableModel tableModel = dataModel.getTableByName("NOTE");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

}