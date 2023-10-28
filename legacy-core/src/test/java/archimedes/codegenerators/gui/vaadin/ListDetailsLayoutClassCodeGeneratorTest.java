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
public class ListDetailsLayoutClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private ListDetailsLayoutClassCodeGenerator unitUnderTest;

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Nested
		class SimpleClass {

			@Test
			void happyRunForASimpleObject() {
				String expected =
						"package de.ollie.bookstore.gui.vaadin.masterdata.layout.list;\n" //
								+ "\n" //
								+ "import java.util.stream.Collectors;\n" //
								+ "\n" //
								+ "import com.vaadin.flow.component.AttachEvent;\n" //
								+ "import com.vaadin.flow.component.button.Button;\n" //
								+ "import com.vaadin.flow.component.grid.Grid;\n" //
								+ "import com.vaadin.flow.component.orderedlayout.HorizontalLayout;\n" //
								+ "import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" //
								+ "\n" //
								+ "import de.ollie.bookstore.gui.vaadin.masterdata.dialog.ChapterDetailsDialog;\n" //
								+ "\n" //
								+ "import de.ollie.bookstore.core.model.Chapter;\n" //
								+ "import de.ollie.bookstore.core.service.ChapterService;\n" //
								+ "import de.ollie.bookstore.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" //
								+ "import de.ollie.bookstore.gui.vaadin.component.ComponentFactory;\n" //
								+ "\n" //
								+ "import lombok.Generated;\n" //
								+ "import lombok.RequiredArgsConstructor;\n" //
								+ "\n" //
								+ "/**\n" //
								+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
								+ " */\n" //
								+ "@Generated\n" //
								+ "@RequiredArgsConstructor\n" //
								+ "public class ChapterListDetailsLayout extends VerticalLayout {\n" //
								+ "\n" //
								+ "	private final ButtonFactory buttonFactory;\n" //
								+ "	private final MasterDataGUIConfiguration guiConfiguration;\n" //
								+ "	private final Chapter model;\n" //
								+ "	private final ResourceManager resourceManager;\n" //
								+ "	private final SessionData session;\n" //
								+ "\n" //
								+ "	private Grid<Chapter> grid;\n" //
								+ "\n" //
								+ "	@Override\n" //
								+ "	public void onAttach(AttachEvent attachEvent) {\n" //
								+ "		Button buttonAdd = buttonFactory.createAddButton(resourceManager, event -> {\n" //
								+ "			new ChapterDetailsDialog(resourceManager, buttonFactory, guiConfiguration, session,\n" //
								+ "					(mmbr, newItem) -> {\n" //
								+ "						model.getChapters().add(mmbr);\n" //
								+ "						grid.setItems(model.getChapters());\n" //
								+ "					}, null, true).open();\n" //
								+ "		}, session);\n" //
								+ "		Button buttonDuplicate = buttonFactory.createDuplicateButton(resourceManager, event -> {\n" //
								+ "			new ChapterDetailsDialog(resourceManager, buttonFactory, guiConfiguration, session,\n" //
								+ "					(mmbr, newItem) -> {\n" //
								+ "						model.getChapters().add(mmbr);\n" //
								+ "						grid.setItems(model.getChapters());\n" //
								+ "					}, grid.getSelectedItems().toArray(new Chapter[0])[0], true).open();\n" //
								+ "		}, session);\n" //
								+ "		Button buttonEdit = buttonFactory.createEditButton(resourceManager, event -> {\n" //
								+ "			new ChapterDetailsDialog(resourceManager, buttonFactory, guiConfiguration, session,\n" //
								+ "					(toEdit, newItem) -> {\n" //
								+ "						Chapter mmbr = grid.getSelectedItems().toArray(new Chapter[0])[0];\n" //
								+ "						mmbr.setSortOrder(toEdit.getSortOrder());\n" //
								+ "						mmbr.setTitle(toEdit.getTitle());\n" //
								+ "						mmbr.setSummary(toEdit.getSummary());\n" //
								+ "						mmbr.setContent(toEdit.getContent());\n" //
								+ "						grid.setItems(model.getChapters());\n" //
								+ "					}, grid.getSelectedItems().toArray(new Chapter[0])[0], false).open();\n" //
								+ "		}, session);\n" //
								+ "		Button buttonRemove = buttonFactory.createRemoveButton(resourceManager, event -> {\n" //
								+ "			Chapter mmbr = grid.getSelectedItems().toArray(new Chapter[0])[0];\n" //
								+ "			model.getChapters().remove(mmbr);\n" //
								+ "			grid.setItems(model.getChapters());\n" //
								+ "		}, session);\n" //
								+ "		HorizontalLayout buttons = new HorizontalLayout(buttonAdd, buttonEdit, buttonDuplicate, buttonRemove);\n" //
								+ "		grid = new Grid<>();\n" //
								+ "		grid.addColumn(Chapter -> Chapter.getSortOrder())\n" //
								+ "				.setHeader(resourceManager.getLocalizedString(\n" //
								+ "						\"BookDetailsLayout.field.gridChapter.columnSortOrder.label\", session.getLocalization()))\n" //
								+ "				.setSortable(true).setWidth(\"10%\");\n" //
								+ "		grid.addColumn(Chapter -> Chapter.getTitle())\n" //
								+ "				.setHeader(resourceManager.getLocalizedString(\n" //
								+ "						\"BookDetailsLayout.field.gridChapter.columnTitle.label\", session.getLocalization()))\n" //
								+ "				.setSortable(true).setWidth(\"90%\");\n" //
								+ "		grid.setItems(model.getChapters().stream().sorted().collect(Collectors.toList()));\n" //
								+ "		grid.setWidthFull();\n" //
								+ "		add(buttons, grid);\n" //
								+ "	}\n" //
								+ "\n" //
								+ "}";
				DataModel dataModel = readDataModel("Example-BookStore.xml", EXAMPLE_XMLS);
				TableModel tableModel = dataModel.getTableByName("CHAPTER");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

}
