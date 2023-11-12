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
								+ "import java.util.function.Supplier;\n" //
								+ "import java.util.stream.Collectors;\n" //
								+ "\n" //
								+ "import org.springframework.beans.factory.annotation.Autowired;\n" //
								+ "\n" //
								+ "import com.vaadin.flow.component.AttachEvent;\n" //
								+ "import com.vaadin.flow.component.button.Button;\n" //
								+ "import com.vaadin.flow.component.grid.Grid;\n" //
								+ "import com.vaadin.flow.component.orderedlayout.HorizontalLayout;\n" //
								+ "import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" //
								+ "\n" //
								+ "import de.ollie.bookstore.core.model.Book;\n" //
								+ "import de.ollie.bookstore.core.model.Chapter;\n" //
								+ "import de.ollie.bookstore.gui.vaadin.component.ComponentFactory;\n" //
								+ "import de.ollie.bookstore.gui.vaadin.masterdata.dialog.ChapterDetailsDialog;\n" //
								+ "import de.ollie.bookstore.core.service.localization.ResourceManager;\n" //
								+ "import de.ollie.bookstore.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" //
								+ "import de.ollie.bookstore.gui.vaadin.masterdata.MasterDataGridFieldRenderer;\n" //
								+ "import de.ollie.bookstore.gui.vaadin.component.ServiceProvider;\n" //
								+ "import de.ollie.bookstore.gui.SessionData;\n" //
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
								+ "	private final ComponentFactory componentFactory;\n" //
								+ "	private final MasterDataGUIConfiguration guiConfiguration;\n" //
								+ "	private final Book model;\n" //
								+ "	private final ResourceManager resourceManager;\n" //
								+ "	private final ServiceProvider serviceProvider;\n" //
								+ "	private final SessionData session;\n" //
								+ "\n" //
								+ "	private Grid<Chapter> grid;\n" //
								+ "\n" //
								+ "	@Override\n" //
								+ "	public void onAttach(AttachEvent attachEvent) {\n" //
								+ "		Button buttonAdd = componentFactory.createAddButton(event -> {\n" //
								+ "			new ChapterDetailsDialog(componentFactory, guiConfiguration, (mmbr, newItem) -> {\n" //
								+ "				model.getChapters().add(mmbr);\n" //
								+ "				grid.setItems(model.getChapters());\n" //
								+ "			}, session, new Chapter(), serviceProvider, true).open();\n" //
								+ "		}, session);\n" //
								+ "		Button buttonDuplicate = componentFactory.createDuplicateButton(event -> {\n" //
								+ "			new ChapterDetailsDialog(componentFactory, guiConfiguration, (mmbr, newItem) -> {\n" //
								+ "				model.getChapters().add(mmbr);\n" //
								+ "				grid.setItems(model.getChapters());\n" //
								+ "			}, session, grid.getSelectedItems().toArray(new Chapter[0])[0], serviceProvider, true).open();\n" //
								+ "		}, session);\n" //
								+ "		Button buttonEdit = componentFactory.createEditButton(event -> {\n" //
								+ "			new ChapterDetailsDialog(componentFactory, guiConfiguration, (toEdit, newItem) -> {\n" //
								+ "				Chapter mmbr = grid.getSelectedItems().toArray(new Chapter[0])[0];\n" //
								+ "				mmbr.setTitle(toEdit.getTitle());\n" //
								+ "				mmbr.setSortOrder(toEdit.getSortOrder());\n" //
								+ "				mmbr.setSummary(toEdit.getSummary());\n" //
								+ "				mmbr.setContent(toEdit.getContent());\n" //
								+ "				grid.setItems(model.getChapters());\n" //
								+ "			}, session, grid.getSelectedItems().toArray(new Chapter[0])[0], serviceProvider, false).open();\n" //
								+ "		}, session);\n" //
								+ "		Button buttonRemove = componentFactory.createRemoveButton(event -> {\n" //
								+ "			Chapter mmbr = grid.getSelectedItems().toArray(new Chapter[0])[0];\n" //
								+ "			model.getChapters().remove(mmbr);\n" //
								+ "			grid.setItems(model.getChapters());\n" //
								+ "		}, session);\n" //
								+ "		HorizontalLayout buttons = new HorizontalLayout(buttonAdd, buttonEdit, buttonDuplicate, buttonRemove);\n" //
								+ "		grid = new Grid<>();\n" //
								+ "		grid\n" //
								+ "				.addColumn(model -> getCellString(\"TITLE\", model, () -> model.getTitle()))\n" //
								+ "				.setHeader(\n" //
								+ "						resourceManager\n" //
								+ "								.getLocalizedString(\n" //
								+ "										\"ChapterListDetailsLayout.grid.header.title.label\",\n" //
								+ "										session.getLocalization()))\n" //
								+ "				.setSortable(true)\n" //
								+ "				.setWidth(\"90%\");\n" //
								+ "		grid\n" //
								+ "				.addColumn(model -> getCellString(\"SORTORDER\", model, () -> model.getSortOrder()))\n" //
								+ "				.setHeader(\n" //
								+ "						resourceManager\n" //
								+ "								.getLocalizedString(\n" //
								+ "										\"ChapterListDetailsLayout.grid.header.sortorder.label\",\n" //
								+ "										session.getLocalization()))\n" //
								+ "				.setSortable(true);\n" //
								+ "		if (!model.getChapters().isEmpty() && (model.getChapters().get(0) instanceof Comparable)) {\n" //
								+ "			grid.setItems(model.getChapters().stream().sorted().collect(Collectors.toList()));\n" //
								+ "		} else {\n" //
								+ "			grid.setItems(model.getChapters());\n" //
								+ "		}\n" //
								+ "		grid.setWidthFull();\n" //
								+ "		add(buttons, grid);\n" //
								+ "	}\n" //
								+ "\n" //
								+ "	private Object getCellString(String fieldName, Chapter aTable, Supplier<?> f) {\n" //
								+ "		return componentFactory.getChapterMasterDataGridFieldRenderer() != null\n" //
								+ "				&& componentFactory.getChapterMasterDataGridFieldRenderer().hasRenderingFor(fieldName)\n" //
								+ "						? componentFactory\n" //
								+ "								.getChapterMasterDataGridFieldRenderer()\n" //
								+ "								.getHeaderString(fieldName, aTable)\n" //
								+ "						: f.get();\n" //
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
