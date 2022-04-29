package archimedes.codegenerators.gui.vaadin;

import static archimedes.codegenerators.gui.vaadin.AbstractGUIVaadinClassCodeGenerator.GUI_EDITOR_POS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Types;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.legacy.scheme.Domain;
import archimedes.legacy.scheme.Tabellenspalte;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class PageLayoutClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private PageLayoutClassCodeGenerator unitUnderTest;

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
						"import java.util.List;\n" + //
						"import java.util.Map;\n" + //
						"import java.util.function.Supplier;\n" + //
						"\n" + //
						"import org.apache.logging.log4j.LogManager;\n" + //
						"import org.apache.logging.log4j.Logger;\n" + //
						"import org.springframework.beans.factory.annotation.Autowired;\n" + //
						"\n" + //
						"import com.vaadin.flow.component.AttachEvent;\n" + //
						"import com.vaadin.flow.component.DetachEvent;\n" + //
						"import com.vaadin.flow.component.grid.Grid;\n" + //
						"import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" + //
						"import com.vaadin.flow.data.selection.SelectionEvent;\n" + //
						"import com.vaadin.flow.router.BeforeEnterEvent;\n" + //
						"import com.vaadin.flow.router.BeforeEnterObserver;\n" + //
						"import com.vaadin.flow.router.BeforeEvent;\n" + //
						"import com.vaadin.flow.router.HasUrlParameter;\n" + //
						"import com.vaadin.flow.router.OptionalParameter;\n" + //
						"import com.vaadin.flow.router.QueryParameters;\n" + //
						"import com.vaadin.flow.router.Route;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.model.PageParameters;\n" + //
						"import base.pack.age.name.core.service.ATableService;\n" + //
						"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
						"import base.pack.age.name.gui.SessionData;\n" + //
						"import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" + //
						"import base.pack.age.name.gui.vaadin.component.Button;\n" + //
						"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
						"import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" + //
						"import base.pack.age.name.gui.vaadin.component.MasterDataButtonLayout;\n" + //
						"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" + //
						"import lombok.Generated;\n" + //
						"import lombok.RequiredArgsConstructor;\n" + //
						"\n" + //
						"/**\n" + //
						" * A view for paginated atable lists.\n" + //
						" *\n" + //
						" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
						" */\n" + //
						"@Generated\n" + //
						"@Route(ATablePageLayout.URL)\n" + //
						"@RequiredArgsConstructor\n" + //
						"public class ATablePageLayout extends VerticalLayout implements BeforeEnterObserver, HasUrlParameter<String> {\n"
						+ //
						"\n" + //
						"	public static final String URL = \"carp-dnd/masterdata/atabellen\";\n" + //
						"\n" + //
						"	private static final Logger logger = LogManager.getLogger(ATablePageLayout.class);\n" + //
						"\n" + //
						"	@Autowired(required = false)\n" + //
						"	private MasterDataGridFieldRenderer<ATable> masterDataGridFieldRenderer;\n" + //
						"\n" + //
						"	private final ButtonFactory buttonFactory;\n" + //
						"	private final ResourceManager resourceManager;\n" + //
						"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
						"	private final ATableService service;\n" + //
						"	private final SessionData session;\n" + //
						"\n" + //
						"	private Button buttonAdd;\n" + //
						"	private Button buttonEdit;\n" + //
						"	private Button buttonRemove;\n" + //
						"	private Grid<ATable> grid;\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {\n" + //
						"		logger.debug(\"setParameter\");\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {\n" + //
						"		UserAuthorizationChecker.forwardToLoginOnNoUserSetForSession(session, beforeEnterEvent);\n"
						+ //
						"		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n"
						+ //
						"		getStyle().set(\"background-size\", \"cover\");\n" + //
						"		buttonAdd = buttonFactory.createAddButton(resourceManager, event -> addRecord(), session);\n"
						+ //
						"		buttonEdit = buttonFactory.createEditButton(resourceManager, event -> editRecord(), session);\n"
						+ //
						"		buttonRemove = buttonFactory.createRemoveButton(resourceManager, event -> removeRecord(), session);\n"
						+ //
						"		grid = new Grid<>();\n" + //
						"		grid\n" + //
						"				.addColumn(model -> getHeaderString(\"DESCRIPTION\", model, () -> model.getDescription()))\n"
						+ //
						"				.setHeader(resourceManager.getLocalizedString(\"ATablePageLayout.grid.header.description.label\", session.getLocalization()))\n"
						+ //
						"				.setSortable(true);\n" + //
						"		grid\n" + //
						"				.addColumn(model -> getHeaderString(\"FLAG\", model, () -> model.isFlag()))\n" + //
						"				.setHeader(resourceManager.getLocalizedString(\"ATablePageLayout.grid.header.flag.label\", session.getLocalization()))\n"
						+ //
						"				.setSortable(true);\n" + //
						"		grid.setMultiSort(true);\n" + //
						"		grid.setWidthFull();\n" + //
						"		grid.addSelectionListener(this::enabledButtons);\n" + //
						"		grid.getStyle().set(\"-moz-border-radius\", \"4px\");\n" + //
						"		grid.getStyle().set(\"-webkit-border-radius\", \"4px\");\n" + //
						"		grid.getStyle().set(\"border-radius\", \"4px\");\n" + //
						"		grid.getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" + //
						"		MasterDataButtonLayout buttonLayout = new MasterDataButtonLayout(buttonAdd, buttonEdit, buttonRemove);\n"
						+ //
						"		buttonLayout.setMargin(false);\n" + //
						"		buttonLayout.setWidthFull();\n" + //
						"		setMargin(false);\n" + //
						"		setWidthFull();\n" + //
						"		VerticalLayout dataLayout = new VerticalLayout();\n" + //
						"		dataLayout.getStyle().set(\"-moz-border-radius\", \"4px\");\n" + //
						"		dataLayout.getStyle().set(\"-webkit-border-radius\", \"4px\");\n" + //
						"		dataLayout.getStyle().set(\"border-radius\", \"4px\");\n" + //
						"		dataLayout.getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" + //
						"		dataLayout\n" + //
						"				.getStyle()\n" + //
						"				.set(\n" + //
						"						\"box-shadow\",\n" + //
						"						\"10px 10px 20px #e4e4e4, -10px 10px 20px #e4e4e4, -10px -10px 20px #e4e4e4, 10px -10px 20px #e4e4e4\");\n"
						+ //
						"		dataLayout.setMargin(false);\n" + //
						"		dataLayout.setWidthFull();\n" + //
						"		dataLayout.add(grid, buttonLayout);\n" + //
						"		add(\n" + //
						"				new HeaderLayout(\n" + //
						"						buttonFactory.createBackButton(resourceManager, this::getUI, MasterDataLayout.URL, session),\n"
						+ //
						"						buttonFactory.createLogoutButton(resourceManager, this::getUI, session, logger),\n"
						+ //
						"						resourceManager.getLocalizedString(\"ATablePageLayout.header.label\", session.getLocalization()),\n"
						+ //
						"						HeaderLayoutMode.PLAIN),\n" + //
						"				dataLayout);\n" + //
						"		updateGrid(0);\n" + //
						"		setButtonEnabled(buttonEdit, false);\n" + //
						"		setButtonEnabled(buttonRemove, false);\n" + //
						"	}\n" + //
						"\n" + //
						"	private Object getHeaderString(String fieldName, ATable aTable, Supplier<?> f) {\n" + //
						"		return masterDataGridFieldRenderer != null && masterDataGridFieldRenderer.hasRenderingFor(fieldName)\n"
						+ //
						"				? masterDataGridFieldRenderer.getHeaderString(fieldName, aTable)\n" + //
						"				: f.get();\n" + //
						"	}\n" + //
						"\n" + //
						"	private void enabledButtons(SelectionEvent<Grid<ATable>, ATable> event) {\n" + //
						"		if (event.getFirstSelectedItem().isEmpty()) {\n" + //
						"			setButtonEnabled(buttonAdd, true);\n" + //
						"			setButtonEnabled(buttonEdit, false);\n" + //
						"			setButtonEnabled(buttonRemove, false);\n" + //
						"		} else {\n" + //
						"			setButtonEnabled(buttonAdd, false);\n" + //
						"			setButtonEnabled(buttonEdit, true);\n" + //
						"			setButtonEnabled(buttonRemove, true);\n" + //
						"		}\n" + //
						"	}\n" + //
						"\n" + //
						"	private void setButtonEnabled(Button button, boolean enabled) {\n" + //
						"		button.setEnabled(enabled);\n" + //
						"		if (enabled) {\n" + //
						"			button.setBackgroundImage(guiConfiguration.getButtonEnabledBackgroundFileName());\n"
						+ //
						"			button.setBorderColor(guiConfiguration.getButtonEnabledBorderColor());\n" + //
						"		} else {\n" + //
						"			button.setBackgroundImage(guiConfiguration.getButtonDisabledBackgroundFileName());\n"
						+ //
						"			button.setBorderColor(guiConfiguration.getButtonDisabledBorderColor());\n" + //
						"		}\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void onAttach(AttachEvent attachEvent) {\n" + //
						"		logger.info(\"ATable page layout opened for user '{}'.\", session.getUserName());\n" + //
						"		super.onAttach(attachEvent);\n" + //
						"	}\n" + //
						"\n" + //
						"	@Override\n" + //
						"	protected void onDetach(DetachEvent detachEvent) {\n" + //
						"		logger.info(\"onDetach\");\n" + //
						"		super.onDetach(detachEvent);\n" + //
						"		getElement().removeFromTree();\n" + //
						"	}\n" + //
						"\n" + //
						"	private void updateGrid(int pageNumber) {\n" + //
						"		grid\n" + //
						"				.setItems(\n" + //
						"						service\n" + //
						"								.findAll(new PageParameters().setEntriesPerPage(Integer.MAX_VALUE).setPageNumber(pageNumber))\n"
						+ //
						"								.getEntries());\n" + //
						"	}\n" + //
						"\n" + //
						"	private void addRecord() {\n" + //
						"		getUI().ifPresent(ui -> ui.navigate(ATableMaintenanceLayout.URL));\n" + //
						"	}\n" + //
						"\n" + //
						"	private void editRecord() {\n" + //
						"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
						"			QueryParameters parameters = new QueryParameters(Map.of(\"id\", List.of(\"\" + model.getId())));\n"
						+ //
						"			getUI().ifPresent(ui -> ui.navigate(ATableMaintenanceLayout.URL, parameters));\n" + //
						"		});\n" + //
						"	}\n" + //
						"\n" + //
						"	private void removeRecord() {\n" + //
						"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
						"			service.delete(model);\n" + //
						"			updateGrid(0);\n" + //
						"		});\n" + //
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
				ColumnModel columnToAdd = new Tabellenspalte("Flag", new Domain("Boolean", Types.BOOLEAN, -1, -1));
				columnToAdd.setNotNull(true);
				columnToAdd.addOption(new Option(GUI_EDITOR_POS, "99"));
				tableModel.addColumn(columnToAdd);
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

	@Nested
	class Inheritance {

		private String getExpected() {
			return "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.Map;\n" + //
					"import java.util.function.Supplier;\n" + //
					"\n" + //
					"import org.apache.logging.log4j.LogManager;\n" + //
					"import org.apache.logging.log4j.Logger;\n" + //
					"import org.springframework.beans.factory.annotation.Autowired;\n" + //
					"\n" + //
					"import com.vaadin.flow.component.AttachEvent;\n" + //
					"import com.vaadin.flow.component.DetachEvent;\n" + //
					"import com.vaadin.flow.component.grid.Grid;\n" + //
					"import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" + //
					"import com.vaadin.flow.data.selection.SelectionEvent;\n" + //
					"import com.vaadin.flow.router.BeforeEnterEvent;\n" + //
					"import com.vaadin.flow.router.BeforeEnterObserver;\n" + //
					"import com.vaadin.flow.router.BeforeEvent;\n" + //
					"import com.vaadin.flow.router.HasUrlParameter;\n" + //
					"import com.vaadin.flow.router.OptionalParameter;\n" + //
					"import com.vaadin.flow.router.QueryParameters;\n" + //
					"import com.vaadin.flow.router.Route;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.ATable;\n" + //
					"import base.pack.age.name.core.model.PageParameters;\n" + //
					"import base.pack.age.name.core.service.ATableService;\n" + //
					"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
					"import base.pack.age.name.gui.SessionData;\n" + //
					"import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" + //
					"import base.pack.age.name.gui.vaadin.component.Button;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" + //
					"import base.pack.age.name.gui.vaadin.component.MasterDataButtonLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.SelectionDialog;\n" + //
					"import base.pack.age.name.gui.vaadin.component.SelectionDialog.Selectable;\n" + //
					"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"/**\n" + //
					" * A view for paginated atable lists.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Route(ATablePageLayout.URL)\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class ATablePageLayout extends VerticalLayout implements BeforeEnterObserver, HasUrlParameter<String> {\n"
					+ //
					"\n" + //
					"	public static final String URL = \"test-project/masterdata/atabellen\";\n" + //
					"\n" + //
					"	private static final Logger logger = LogManager.getLogger(ATablePageLayout.class);\n" + //
					"\n" + //
					"	@Autowired(required = false)\n" + //
					"	private MasterDataGridFieldRenderer<ATable> masterDataGridFieldRenderer;\n" + //
					"\n" + //
					"	private final ButtonFactory buttonFactory;\n" + //
					"	private final ResourceManager resourceManager;\n" + //
					"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
					"	private final ATableService service;\n" + //
					"	private final SessionData session;\n" + //
					"\n" + //
					"	private Button buttonAdd;\n" + //
					"	private Button buttonEdit;\n" + //
					"	private Button buttonRemove;\n" + //
					"	private Grid<ATable> grid;\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {\n" + //
					"		logger.debug(\"setParameter\");\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {\n" + //
					"		UserAuthorizationChecker.forwardToLoginOnNoUserSetForSession(session, beforeEnterEvent);\n"
					+ //
					"		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n"
					+ //
					"		getStyle().set(\"background-size\", \"cover\");\n" + //
					"		buttonAdd = buttonFactory.createAddButton(resourceManager, event -> addRecord(), session);\n"
					+ //
					"		buttonEdit = buttonFactory.createEditButton(resourceManager, event -> editRecord(), session);\n"
					+ //
					"		buttonRemove = buttonFactory.createRemoveButton(resourceManager, event -> removeRecord(), session);\n"
					+ //
					"		grid = new Grid<>();\n" + //
					"		grid\n" + //
					"				.addColumn(model -> getHeaderString(\"DESCRIPTION\", model, () -> model.getDescription()))\n"
					+ //
					"				.setHeader(resourceManager.getLocalizedString(\"ATablePageLayout.grid.header.description.label\", session.getLocalization()))\n"
					+ //
					"				.setSortable(true);\n" + //
					"		grid\n" + //
					"				.addColumn(model -> getHeaderString(\"FLAG\", model, () -> model.isFlag()))\n" + //
					"				.setHeader(resourceManager.getLocalizedString(\"ATablePageLayout.grid.header.flag.label\", session.getLocalization()))\n"
					+ //
					"				.setSortable(true);\n" + //
					"		grid.setMultiSort(true);\n" + //
					"		grid.setWidthFull();\n" + //
					"		grid.addSelectionListener(this::enabledButtons);\n" + //
					"		grid.getStyle().set(\"-moz-border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"-webkit-border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" + //
					"		MasterDataButtonLayout buttonLayout = new MasterDataButtonLayout(buttonAdd, buttonEdit, buttonRemove);\n"
					+ //
					"		buttonLayout.setMargin(false);\n" + //
					"		buttonLayout.setWidthFull();\n" + //
					"		setMargin(false);\n" + //
					"		setWidthFull();\n" + //
					"		VerticalLayout dataLayout = new VerticalLayout();\n" + //
					"		dataLayout.getStyle().set(\"-moz-border-radius\", \"4px\");\n" + //
					"		dataLayout.getStyle().set(\"-webkit-border-radius\", \"4px\");\n" + //
					"		dataLayout.getStyle().set(\"border-radius\", \"4px\");\n" + //
					"		dataLayout.getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" + //
					"		dataLayout\n" + //
					"				.getStyle()\n" + //
					"				.set(\n" + //
					"						\"box-shadow\",\n" + //
					"						\"10px 10px 20px #e4e4e4, -10px 10px 20px #e4e4e4, -10px -10px 20px #e4e4e4, 10px -10px 20px #e4e4e4\");\n"
					+ //
					"		dataLayout.setMargin(false);\n" + //
					"		dataLayout.setWidthFull();\n" + //
					"		dataLayout.add(grid, buttonLayout);\n" + //
					"		add(\n" + //
					"				new HeaderLayout(\n" + //
					"						buttonFactory.createBackButton(resourceManager, this::getUI, MasterDataLayout.URL, session),\n"
					+ //
					"						buttonFactory.createLogoutButton(resourceManager, this::getUI, session, logger),\n"
					+ //
					"						resourceManager.getLocalizedString(\"ATablePageLayout.header.label\", session.getLocalization()),\n"
					+ //
					"						HeaderLayoutMode.PLAIN),\n" + //
					"				dataLayout);\n" + //
					"		updateGrid(0);\n" + //
					"		setButtonEnabled(buttonEdit, false);\n" + //
					"		setButtonEnabled(buttonRemove, false);\n" + //
					"	}\n" + //
					"\n" + //
					"	private Object getHeaderString(String fieldName, ATable aTable, Supplier<?> f) {\n" + //
					"		return masterDataGridFieldRenderer != null && masterDataGridFieldRenderer.hasRenderingFor(fieldName)\n"
					+ //
					"				? masterDataGridFieldRenderer.getHeaderString(fieldName, aTable)\n" + //
					"				: f.get();\n" + //
					"	}\n" + //
					"\n" + //
					"	private void enabledButtons(SelectionEvent<Grid<ATable>, ATable> event) {\n" + //
					"		if (event.getFirstSelectedItem().isEmpty()) {\n" + //
					"			setButtonEnabled(buttonAdd, true);\n" + //
					"			setButtonEnabled(buttonEdit, false);\n" + //
					"			setButtonEnabled(buttonRemove, false);\n" + //
					"		} else {\n" + //
					"			setButtonEnabled(buttonAdd, false);\n" + //
					"			setButtonEnabled(buttonEdit, true);\n" + //
					"			setButtonEnabled(buttonRemove, true);\n" + //
					"		}\n" + //
					"	}\n" + //
					"\n" + //
					"	private void setButtonEnabled(Button button, boolean enabled) {\n" + //
					"		button.setEnabled(enabled);\n" + //
					"		if (enabled) {\n" + //
					"			button.setBackgroundImage(guiConfiguration.getButtonEnabledBackgroundFileName());\n" + //
					"			button.setBorderColor(guiConfiguration.getButtonEnabledBorderColor());\n" + //
					"		} else {\n" + //
					"			button.setBackgroundImage(guiConfiguration.getButtonDisabledBackgroundFileName());\n" + //
					"			button.setBorderColor(guiConfiguration.getButtonDisabledBorderColor());\n" + //
					"		}\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	protected void onAttach(AttachEvent attachEvent) {\n" + //
					"		logger.info(\"ATable page layout opened for user '{}'.\", session.getUserName());\n" + //
					"		super.onAttach(attachEvent);\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	protected void onDetach(DetachEvent detachEvent) {\n" + //
					"		logger.info(\"onDetach\");\n" + //
					"		super.onDetach(detachEvent);\n" + //
					"		getElement().removeFromTree();\n" + //
					"	}\n" + //
					"\n" + //
					"	private void updateGrid(int pageNumber) {\n" + //
					"		grid\n" + //
					"				.setItems(\n" + //
					"						service\n" + //
					"								.findAll(new PageParameters().setEntriesPerPage(Integer.MAX_VALUE).setPageNumber(pageNumber))\n"
					+ //
					"								.getEntries());\n" + //
					"	}\n" + //
					"\n" + //
					"	private void addRecord() {\n" + //
					"		Selectable[] selectableSubclasses =\n" + //
					"				new SelectableSubclass[] {\n" + //
					"						new SelectableSubclass(\n" + //
					"								resourceManager\n" + //
					"										.getLocalizedString(\n" + //
					"												\"ATablePageLayout.subclass.selection.AnotherHeirTable.label\",\n"
					+ //
					"												session.getLocalization()),\n" + //
					"								\"AnotherHeirTable\"),\n" + //
					"						new SelectableSubclass(\n" + //
					"								resourceManager\n" + //
					"										.getLocalizedString(\n" + //
					"												\"ATablePageLayout.subclass.selection.AnotherHeirTableWithSameReference.label\",\n"
					+ //
					"												session.getLocalization()),\n" + //
					"								\"AnotherHeirTableWithSameReference\"),\n" + //
					"						new SelectableSubclass(\n" + //
					"								resourceManager\n" + //
					"										.getLocalizedString(\n" + //
					"												\"ATablePageLayout.subclass.selection.AnotherTable.label\",\n"
					+ //
					"												session.getLocalization()),\n" + //
					"								\"AnotherTable\"),\n" + //
					"						new SelectableSubclass(\n" + //
					"								resourceManager\n" + //
					"										.getLocalizedString(\n" + //
					"												\"ATablePageLayout.subclass.selection.HeirTableWithReference.label\",\n"
					+ //
					"												session.getLocalization()),\n" + //
					"								\"HeirTableWithReference\"),\n" + //
					"						new SelectableSubclass(\n" + //
					"								resourceManager\n" + //
					"										.getLocalizedString(\n" + //
					"												\"ATablePageLayout.subclass.selection.ATable.label\",\n"
					+ //
					"												session.getLocalization()),\n" + //
					"								\"ATable\") };\n" + //
					"		new SelectionDialog(\n" + //
					"				buttonFactory,\n" + //
					"				selectable -> switchToMaintenanceLayoutForANewObject(\n" + //
					"						((SelectableSubclass) selectable).getSubclassName()),\n" + //
					"				resourceManager,\n" + //
					"				session,\n" + //
					"				selectableSubclasses).open();\n" + //
					"	}\n" + //
					"\n" + //
					"	private void switchToMaintenanceLayoutForANewObject(String selectedSubclassName) {\n" + //
					"		QueryParameters parameters = new QueryParameters(Map.of(\"modelClass\", List.of(selectedSubclassName)));\n"
					+ //
					"		getUI().ifPresent(ui -> ui.navigate(ATableMaintenanceLayout.URL, parameters));\n" + //
					"	}\n" + //
					"\n" + //
					"	private void editRecord() {\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			QueryParameters parameters = new QueryParameters(Map.of(\"id\", List.of(\"\" + model.getId())));\n"
					+ //
					"			getUI().ifPresent(ui -> ui.navigate(ATableMaintenanceLayout.URL, parameters));\n" + //
					"		});\n" + //
					"	}\n" + //
					"\n" + //
					"	private void removeRecord() {\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			service.delete(model);\n" + //
					"			updateGrid(0);\n" + //
					"		});\n" + //
					"	}\n" + //
					"\n" + //
					"}";
		}

		@Test
		void happyRunForASimpleObject() {
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

}