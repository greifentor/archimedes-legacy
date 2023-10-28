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
								+ "import com.vaadin.flow.component.textfield.TextField;\n" //
								+ "\n" //
								+ "import de.ollie.bookstore.gui.vaadin.component.Button;\n" //
								+ "import de.ollie.bookstore.core.model.Book;\n" //
								+ "import de.ollie.bookstore.core.service.BookService;\n" //
								+ "import de.ollie.bookstore.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" //
								+ "import de.ollie.bookstore.gui.vaadin.component.ComponentFactory;\n" //
								+ "import de.ollie.bookstore.gui.vaadin.masterdata.layout.list.ChapterListDetailsLayout;\n" //
								+ "import de.ollie.bookstore.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" //
								+ "\n" //
								+ "import lombok.Generated;\n" //
								+ "import lombok.RequiredArgsConstructor;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Generated\n" //
								+ "@RequiredArgsConstructor\n" //
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
								+ "\n" //
								+ "	private Button buttonCancel;\n" //
								+ "	private Button buttonSave;\n" //
								+ "	private TextField textFieldTitle;\n" //
								+ "	private TextField textFieldIsbn;\n" //
								+ "	private VerticalLayout mainLayout;\n" //
								+ "\n" //
								+ "	private Book model = new Book();\n" //
								+ "	private boolean newItem;\n" //
								+ "\n" //
								+ "	public BookDetailsDialog(ComponentFactory componentFactory, MasterDataGUIConfiguration guiConfiguration,\n" //
								+ "			Observer observer, Book model, boolean newItem) {\n" //
								+ "		this.componentFactory = componentFactory;\n" //
								+ "		this.guiConfiguration = guiConfiguration;\n" //
								+ "		this.newItem = newItem;\n" //
								+ "		this.observer = observer;\n" //
								+ "		if (model != null) {\n" //
								+ "			this.model.setTitle(model.getTitle())\n" //
								+ "			this.model.setIsbn(model.getIsbn())\n" //
								+ "		}\n" //
								+ "		mainLayout = new VerticalLayout();\n" //
								+ "		addComponents();\n" //
								+ "		buttonCancel = componentFactory.createCancelButton(event -> close());\n" //
								+ "		buttonCancel.setWidthFull();\n" //
								+ "		buttonSave = componentFactory.createSaveButton(event -> save());\n" //
								+ "		buttonSave.setWidthFull();\n" //
								+ "		mainLayout.add(buttonCancel, buttonSave);\n" //
								+ "		setWidth(\"90%\");\n" //
								+ "		add(mainLayout);\n" //
								+ "		updateSaveButton();\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private void addComponents() {\n" //
								+ "		textFieldTitle = componentFactory.createTextField(\"BookDetailsLayout.field.title.label\", model.getTitle());\n" //
								+ "		textFieldIsbn = componentFactory.createTextField(\"BookDetailsLayout.field.isbn.label\", model.getIsbn());\n" //
								+ "		mainLayout.add(\n" //
								+ "				textFieldTitle,\n" //
								+ "				textFieldIsbn\n" //
								+ "		);\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private void updateSaveButton() {\n" //
								+ "		setButtonEnabled(buttonSave,\n" //
								+ "				(textFieldTitle.getValue() != null)\n" //
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

	}

}