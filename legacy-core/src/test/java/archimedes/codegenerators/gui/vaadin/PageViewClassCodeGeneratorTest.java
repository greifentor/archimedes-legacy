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
public class PageViewClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private PageViewClassCodeGenerator unitUnderTest;

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
						"import com.vaadin.flow.component.orderedlayout.Scroller;\n" + //
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
						"import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" + //
						"import lombok.Generated;\n" + //
						"import lombok.RequiredArgsConstructor;\n" + //
						"\n" + //
						"/**\n" + //
						" * A view for paginated atable lists.\n" + //
						" *\n" + //
						" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
						" */\n" + //
						"@Generated\n" + //
						"@Route(ATablePageView.URL)\n" + //
						"@RequiredArgsConstructor\n" + //
						"public class ATablePageView extends Scroller implements BeforeEnterObserver, HasUrlParameter<String> {\n"
						+ //
						"\n" + //
						"	public static final String URL = \"test-ws/masterdata/atabellen\";\n" + //
						"\n" + //
						"	private static final Logger logger = LogManager.getLogger(ATablePageView.class);\n" + //
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
						"	private Button buttonDuplicate;\n" + //
						"	private Button buttonEdit;\n" + //
						"	private Button buttonRemove;\n" + //
						"	private Grid<ATable> grid;\n" + //
						"	private VerticalLayout mainLayout;\n" + //
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
						"		mainLayout = new VerticalLayout();\n" + //
						"		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n"
						+ //
						"		getStyle().set(\"background-size\", \"cover\");\n" + //
						"		getStyle().set(\"background-attachment\", \"fixed\");\n" + //
						"		buttonAdd = buttonFactory.createAddButton(resourceManager, event -> addRecord(), session);\n"
						+ //
						"		buttonDuplicate = buttonFactory.createButton(resourceManager.getLocalizedString(\"commons.button.duplicate.text\", session.getLocalization()));\n"
						+ //
						"		buttonDuplicate.addClickListener(event -> duplicateRecord());\n" + //
						"		buttonEdit = buttonFactory.createEditButton(resourceManager, event -> editRecord(), session);\n"
						+ //
						"		buttonRemove = buttonFactory.createRemoveButton(resourceManager, event -> removeRecord(), session);\n"
						+ //
						"		grid = new Grid<>();\n" + //
						"		grid\n" + //
						"				.addColumn(model -> getHeaderString(\"DESCRIPTION\", model, () -> model.getDescription()))\n"
						+ //
						"				.setHeader(resourceManager.getLocalizedString(\"ATablePageView.grid.header.description.label\", session.getLocalization()))\n"
						+ //
						"				.setSortable(true);\n" + //
						"		grid\n" + //
						"				.addColumn(model -> getHeaderString(\"FLAG\", model, () -> model.isFlag()))\n" + //
						"				.setHeader(resourceManager.getLocalizedString(\"ATablePageView.grid.header.flag.label\", session.getLocalization()))\n"
						+ //
						"				.setSortable(true);\n" + //
						"		grid.setMultiSort(true);\n" + //
						"		grid.setWidthFull();\n" + //
						"		grid.addSelectionListener(this::enabledButtons);\n" + //
						"		grid.getStyle().set(\"-moz-border-radius\", \"4px\");\n" + //
						"		grid.getStyle().set(\"-webkit-border-radius\", \"4px\");\n" + //
						"		grid.getStyle().set(\"border-radius\", \"4px\");\n" + //
						"		grid.getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" + //
						"		MasterDataButtonLayout buttonLayout = new MasterDataButtonLayout(buttonAdd, buttonEdit, buttonDuplicate, buttonRemove);\n"
						+ //
						"		buttonLayout.setMargin(false);\n" + //
						"		buttonLayout.setWidthFull();\n" + //
						"		mainLayout.setMargin(false);\n" + //
						"		mainLayout.setSizeFull();\n" + //
						"		setSizeFull();\n" + //
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
						"		mainLayout.add(\n" + //
						"				new HeaderLayout(\n" + //
						"						buttonFactory.createBackButton(resourceManager, this::getUI, MasterDataView.URL, session),\n"
						+ //
						"						buttonFactory.createLogoutButton(resourceManager, this::getUI, session, logger),\n"
						+ //
						"						resourceManager.getLocalizedString(\"ATablePageView.header.label\", session.getLocalization()),\n"
						+ //
						"						HeaderLayoutMode.PLAIN),\n" + //
						"				dataLayout);\n" + //
						"		updateGrid(0);\n" + //
						"		setButtonEnabled(buttonDuplicate, false);\n" + //
						"		setButtonEnabled(buttonEdit, false);\n" + //
						"		setButtonEnabled(buttonRemove, false);\n" + //
						"		setContent(mainLayout);\n" + //
						"		buttonAdd.focus();\n" + //
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
						"			setButtonEnabled(buttonDuplicate, false);\n" + //
						"			setButtonEnabled(buttonEdit, false);\n" + //
						"			setButtonEnabled(buttonRemove, false);\n" + //
						"		} else {\n" + //
						"			setButtonEnabled(buttonAdd, false);\n" + //
						"			setButtonEnabled(buttonDuplicate, true);\n" + //
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
						"		getUI().ifPresent(ui -> ui.navigate(ATableMaintenanceView.URL));\n" + //
						"	}\n" + //
						"\n" + //
						"	private void duplicateRecord() {\n" + //
						"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
						"			QueryParameters parameters =\n" + //
						"					new QueryParameters(Map.of(\"id\", List.of(\"\" + model.getId()), \"duplicate\", List.of(\"true\")));\n"
						+ //
						"			getUI().ifPresent(ui -> ui.navigate(ATableMaintenanceView.URL, parameters));\n" + //
						"		});\n" + //
						"	}\n" + //
						"\n" + //
						"	private void editRecord() {\n" + //
						"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
						"			QueryParameters parameters = new QueryParameters(Map.of(\"id\", List.of(\"\" + model.getId())));\n"
						+ //
						"			getUI().ifPresent(ui -> ui.navigate(ATableMaintenanceView.URL, parameters));\n" + //
						"		});\n" + //
						"	}\n" + //
						"\n" + //
						"	private void removeRecord() {\n" + //
						"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
						"			new RemoveConfirmDialog(buttonFactory, () -> {\n" + //
						"				service.delete(model);\n" + //
						"				updateGrid(0);\n" + //
						"				buttonAdd.focus();\n" + //
						"			}, resourceManager, session).open();\n" + //
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
					"import com.vaadin.flow.component.orderedlayout.Scroller;\n" + //
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
					"import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"/**\n" + //
					" * A view for paginated atable lists.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Route(ATablePageView.URL)\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class ATablePageView extends Scroller implements BeforeEnterObserver, HasUrlParameter<String> {\n"
					+ //
					"\n" + //
					"	public static final String URL = \"test-project/masterdata/atabellen\";\n" + //
					"\n" + //
					"	private static final Logger logger = LogManager.getLogger(ATablePageView.class);\n" + //
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
					"	private Button buttonDuplicate;\n" + //
					"	private Button buttonEdit;\n" + //
					"	private Button buttonRemove;\n" + //
					"	private Grid<ATable> grid;\n" + //
					"	private VerticalLayout mainLayout;\n" + //
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
					"		mainLayout = new VerticalLayout();\n" + //
					"		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n"
					+ //
					"		getStyle().set(\"background-size\", \"cover\");\n" + //
					"		getStyle().set(\"background-attachment\", \"fixed\");\n" + //
					"		buttonAdd = buttonFactory.createAddButton(resourceManager, event -> addRecord(), session);\n"
					+ //
					"		buttonDuplicate = buttonFactory.createButton(resourceManager.getLocalizedString(\"commons.button.duplicate.text\", session.getLocalization()));\n"
					+ //
					"		buttonDuplicate.addClickListener(event -> duplicateRecord());\n" + //
					"		buttonEdit = buttonFactory.createEditButton(resourceManager, event -> editRecord(), session);\n"
					+ //
					"		buttonRemove = buttonFactory.createRemoveButton(resourceManager, event -> removeRecord(), session);\n"
					+ //
					"		grid = new Grid<>();\n" + //
					"		grid\n" + //
					"				.addColumn(model -> getHeaderString(\"DESCRIPTION\", model, () -> model.getDescription()))\n"
					+ //
					"				.setHeader(resourceManager.getLocalizedString(\"ATablePageView.grid.header.description.label\", session.getLocalization()))\n"
					+ //
					"				.setSortable(true);\n" + //
					"		grid\n" + //
					"				.addColumn(model -> getHeaderString(\"FLAG\", model, () -> model.isFlag()))\n" + //
					"				.setHeader(resourceManager.getLocalizedString(\"ATablePageView.grid.header.flag.label\", session.getLocalization()))\n"
					+ //
					"				.setSortable(true);\n" + //
					"		grid.setMultiSort(true);\n" + //
					"		grid.setWidthFull();\n" + //
					"		grid.addSelectionListener(this::enabledButtons);\n" + //
					"		grid.getStyle().set(\"-moz-border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"-webkit-border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" + //
					"		MasterDataButtonLayout buttonLayout = new MasterDataButtonLayout(buttonAdd, buttonEdit, buttonDuplicate, buttonRemove);\n"
					+ //
					"		buttonLayout.setMargin(false);\n" + //
					"		buttonLayout.setWidthFull();\n" + //
					"		mainLayout.setMargin(false);\n" + //
					"		mainLayout.setSizeFull();\n" + //
					"		setSizeFull();\n" + //
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
					"		mainLayout.add(\n" + //
					"				new HeaderLayout(\n" + //
					"						buttonFactory.createBackButton(resourceManager, this::getUI, MasterDataView.URL, session),\n"
					+ //
					"						buttonFactory.createLogoutButton(resourceManager, this::getUI, session, logger),\n"
					+ //
					"						resourceManager.getLocalizedString(\"ATablePageView.header.label\", session.getLocalization()),\n"
					+ //
					"						HeaderLayoutMode.PLAIN),\n" + //
					"				dataLayout);\n" + //
					"		updateGrid(0);\n" + //
					"		setButtonEnabled(buttonDuplicate, false);\n" + //
					"		setButtonEnabled(buttonEdit, false);\n" + //
					"		setButtonEnabled(buttonRemove, false);\n" + //
					"		setContent(mainLayout);\n" + //
					"		buttonAdd.focus();\n" + //
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
					"			setButtonEnabled(buttonDuplicate, false);\n" + //
					"			setButtonEnabled(buttonEdit, false);\n" + //
					"			setButtonEnabled(buttonRemove, false);\n" + //
					"		} else {\n" + //
					"			setButtonEnabled(buttonAdd, false);\n" + //
					"			setButtonEnabled(buttonDuplicate, true);\n" + //
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
					"												\"ATablePageView.subclass.selection.AnotherHeirTable.label\",\n"
					+ //
					"												session.getLocalization()),\n" + //
					"								\"AnotherHeirTable\"),\n" + //
					"						new SelectableSubclass(\n" + //
					"								resourceManager\n" + //
					"										.getLocalizedString(\n" + //
					"												\"ATablePageView.subclass.selection.AnotherHeirTableWithSameReference.label\",\n"
					+ //
					"												session.getLocalization()),\n" + //
					"								\"AnotherHeirTableWithSameReference\"),\n" + //
					"						new SelectableSubclass(\n" + //
					"								resourceManager\n" + //
					"										.getLocalizedString(\n" + //
					"												\"ATablePageView.subclass.selection.AnotherTable.label\",\n"
					+ //
					"												session.getLocalization()),\n" + //
					"								\"AnotherTable\"),\n" + //
					"						new SelectableSubclass(\n" + //
					"								resourceManager\n" + //
					"										.getLocalizedString(\n" + //
					"												\"ATablePageView.subclass.selection.HeirTableWithReference.label\",\n"
					+ //
					"												session.getLocalization()),\n" + //
					"								\"HeirTableWithReference\"),\n" + //
					"						new SelectableSubclass(\n" + //
					"								resourceManager\n" + //
					"										.getLocalizedString(\n" + //
					"												\"ATablePageView.subclass.selection.ATable.label\",\n"
					+ //
					"												session.getLocalization()),\n" + //
					"								\"ATable\") };\n" + //
					"		new SelectionDialog(\n" + //
					"				buttonFactory,\n" + //
					"				selectable -> switchToMaintenanceViewForANewObject(\n" + //
					"						((SelectableSubclass) selectable).getSubclassName()),\n" + //
					"				resourceManager,\n" + //
					"				session,\n" + //
					"				selectableSubclasses).open();\n" + //
					"	}\n" + //
					"\n" + //
					"	private void switchToMaintenanceViewForANewObject(String selectedSubclassName) {\n" + //
					"		QueryParameters parameters = new QueryParameters(Map.of(\"modelClass\", List.of(selectedSubclassName)));\n"
					+ //
					"		getUI().ifPresent(ui -> ui.navigate(ATableMaintenanceView.URL, parameters));\n" + //
					"	}\n" + //
					"\n" + //
					"	private void duplicateRecord() {\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			QueryParameters parameters =\n" + //
					"					new QueryParameters(Map.of(\"id\", List.of(\"\" + model.getId()), \"duplicate\", List.of(\"true\")));\n"
					+ //
					"			getUI().ifPresent(ui -> ui.navigate(ATableMaintenanceView.URL, parameters));\n" + //
					"		});\n" + //
					"	}\n" + //
					"\n" + //
					"	private void editRecord() {\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			QueryParameters parameters = new QueryParameters(Map.of(\"id\", List.of(\"\" + model.getId())));\n"
					+ //
					"			getUI().ifPresent(ui -> ui.navigate(ATableMaintenanceView.URL, parameters));\n" + //
					"		});\n" + //
					"	}\n" + //
					"\n" + //
					"	private void removeRecord() {\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			new RemoveConfirmDialog(buttonFactory, () -> {\n" + //
					"				service.delete(model);\n" + //
					"				updateGrid(0);\n" + //
					"				buttonAdd.focus();\n" + //
					"			}, resourceManager, session).open();\n" + //
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

		@Test
		void objectWithSubSubClasses() {
			// Prepare
			String expected =
					"package base.pack.age.name.gui.vaadin.masterdata;\n" //
							+ "\n" //
							+ "import java.util.List;\n" //
							+ "import java.util.Map;\n" //
							+ "import java.util.function.Supplier;\n" //
							+ "\n" //
							+ "import org.apache.logging.log4j.LogManager;\n" //
							+ "import org.apache.logging.log4j.Logger;\n" //
							+ "import org.springframework.beans.factory.annotation.Autowired;\n" //
							+ "\n" //
							+ "import com.vaadin.flow.component.AttachEvent;\n" //
							+ "import com.vaadin.flow.component.DetachEvent;\n" //
							+ "import com.vaadin.flow.component.grid.Grid;\n" //
							+ "import com.vaadin.flow.component.orderedlayout.Scroller;\n" //
							+ "import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" //
							+ "import com.vaadin.flow.data.selection.SelectionEvent;\n" //
							+ "import com.vaadin.flow.router.BeforeEnterEvent;\n" //
							+ "import com.vaadin.flow.router.BeforeEnterObserver;\n" //
							+ "import com.vaadin.flow.router.BeforeEvent;\n" //
							+ "import com.vaadin.flow.router.HasUrlParameter;\n" //
							+ "import com.vaadin.flow.router.OptionalParameter;\n" //
							+ "import com.vaadin.flow.router.QueryParameters;\n" //
							+ "import com.vaadin.flow.router.Route;\n" //
							+ "\n" //
							+ "import base.pack.age.name.core.model.BTable;\n" //
							+ "import base.pack.age.name.core.model.PageParameters;\n" //
							+ "import base.pack.age.name.core.service.BTableService;\n" //
							+ "import base.pack.age.name.core.service.localization.ResourceManager;\n" //
							+ "import base.pack.age.name.gui.SessionData;\n" //
							+ "import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.Button;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.MasterDataButtonLayout;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.SelectionDialog;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.SelectionDialog.Selectable;\n" //
							+ "import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" //
							+ "import lombok.Generated;\n" //
							+ "import lombok.RequiredArgsConstructor;\n" //
							+ "\n" //
							+ "/**\n" //
							+ " * A view for paginated btable lists.\n" //
							+ " *\n" //
							+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
							+ " */\n" //
							+ "@Generated\n" //
							+ "@Route(BTablePageView.URL)\n" //
							+ "@RequiredArgsConstructor\n" //
							+ "public class BTablePageView extends Scroller implements BeforeEnterObserver, HasUrlParameter<String> {\n" //
							+ "\n" //
							+ "	public static final String URL = \"test-project/masterdata/atabellen\";\n" //
							+ "\n" //
							+ "	private static final Logger logger = LogManager.getLogger(BTablePageView.class);\n" //
							+ "\n" //
							+ "	@Autowired(required = false)\n" //
							+ "	private MasterDataGridFieldRenderer<BTable> masterDataGridFieldRenderer;\n" //
							+ "\n" //
							+ "	private final ButtonFactory buttonFactory;\n" //
							+ "	private final ResourceManager resourceManager;\n" //
							+ "	private final MasterDataGUIConfiguration guiConfiguration;\n" //
							+ "	private final BTableService service;\n" //
							+ "	private final SessionData session;\n" //
							+ "\n" //
							+ "	private Button buttonAdd;\n" //
							+ "	private Button buttonDuplicate;\n" //
							+ "	private Button buttonEdit;\n" //
							+ "	private Button buttonRemove;\n" //
							+ "	private Grid<BTable> grid;\n" //
							+ "	private VerticalLayout mainLayout;\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {\n" //
							+ "		logger.debug(\"setParameter\");\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {\n" //
							+ "		UserAuthorizationChecker.forwardToLoginOnNoUserSetForSession(session, beforeEnterEvent);\n" //
							+ "		mainLayout = new VerticalLayout();\n" //
							+ "		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n" //
							+ "		getStyle().set(\"background-size\", \"cover\");\n" //
							+ "		getStyle().set(\"background-attachment\", \"fixed\");\n" //
							+ "		buttonAdd = buttonFactory.createAddButton(resourceManager, event -> addRecord(), session);\n" //
							+ "		buttonDuplicate = buttonFactory.createButton(resourceManager.getLocalizedString(\"commons.button.duplicate.text\", session.getLocalization()));\n" //
							+ "		buttonDuplicate.addClickListener(event -> duplicateRecord());\n" //
							+ "		buttonEdit = buttonFactory.createEditButton(resourceManager, event -> editRecord(), session);\n" //
							+ "		buttonRemove = buttonFactory.createRemoveButton(resourceManager, event -> removeRecord(), session);\n" //
							+ "		grid = new Grid<>();\n" //
							+ "		grid\n" //
							+ "				.addColumn(model -> getHeaderString(\"DESCRIPTION\", model, () -> model.getDescription()))\n" //
							+ "				.setHeader(resourceManager.getLocalizedString(\"BTablePageView.grid.header.description.label\", session.getLocalization()))\n" //
							+ "				.setSortable(true);\n" //
							+ "		grid\n" //
							+ "				.addColumn(model -> getHeaderString(\"REFERENCE\", model, () -> model.getReference()))\n" //
							+ "				.setHeader(resourceManager.getLocalizedString(\"BTablePageView.grid.header.reference.label\", session.getLocalization()))\n" //
							+ "				.setSortable(true);\n" //
							+ "		grid.setMultiSort(true);\n" //
							+ "		grid.setWidthFull();\n" //
							+ "		grid.addSelectionListener(this::enabledButtons);\n" //
							+ "		grid.getStyle().set(\"-moz-border-radius\", \"4px\");\n" //
							+ "		grid.getStyle().set(\"-webkit-border-radius\", \"4px\");\n" //
							+ "		grid.getStyle().set(\"border-radius\", \"4px\");\n" //
							+ "		grid.getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" //
							+ "		MasterDataButtonLayout buttonLayout = new MasterDataButtonLayout(buttonAdd, buttonEdit, buttonDuplicate, buttonRemove);\n" //
							+ "		buttonLayout.setMargin(false);\n" //
							+ "		buttonLayout.setWidthFull();\n" //
							+ "		mainLayout.setMargin(false);\n" //
							+ "		mainLayout.setSizeFull();\n" //
							+ "		setSizeFull();\n" //
							+ "		VerticalLayout dataLayout = new VerticalLayout();\n" //
							+ "		dataLayout.getStyle().set(\"-moz-border-radius\", \"4px\");\n" //
							+ "		dataLayout.getStyle().set(\"-webkit-border-radius\", \"4px\");\n" //
							+ "		dataLayout.getStyle().set(\"border-radius\", \"4px\");\n" //
							+ "		dataLayout.getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" //
							+ "		dataLayout\n" //
							+ "				.getStyle()\n" //
							+ "				.set(\n" //
							+ "						\"box-shadow\",\n" //
							+ "						\"10px 10px 20px #e4e4e4, -10px 10px 20px #e4e4e4, -10px -10px 20px #e4e4e4, 10px -10px 20px #e4e4e4\");\n" //
							+ "		dataLayout.setMargin(false);\n" //
							+ "		dataLayout.setWidthFull();\n" //
							+ "		dataLayout.add(grid, buttonLayout);\n" //
							+ "		mainLayout.add(\n" //
							+ "				new HeaderLayout(\n" //
							+ "						buttonFactory.createBackButton(resourceManager, this::getUI, MasterDataView.URL, session),\n" //
							+ "						buttonFactory.createLogoutButton(resourceManager, this::getUI, session, logger),\n" //
							+ "						resourceManager.getLocalizedString(\"BTablePageView.header.label\", session.getLocalization()),\n" //
							+ "						HeaderLayoutMode.PLAIN),\n" //
							+ "				dataLayout);\n" //
							+ "		updateGrid(0);\n" //
							+ "		setButtonEnabled(buttonDuplicate, false);\n" //
							+ "		setButtonEnabled(buttonEdit, false);\n" //
							+ "		setButtonEnabled(buttonRemove, false);\n" //
							+ "		setContent(mainLayout);\n" //
							+ "		buttonAdd.focus();\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private Object getHeaderString(String fieldName, BTable aTable, Supplier<?> f) {\n" //
							+ "		return masterDataGridFieldRenderer != null && masterDataGridFieldRenderer.hasRenderingFor(fieldName)\n" //
							+ "				? masterDataGridFieldRenderer.getHeaderString(fieldName, aTable)\n" //
							+ "				: f.get();\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private void enabledButtons(SelectionEvent<Grid<BTable>, BTable> event) {\n" //
							+ "		if (event.getFirstSelectedItem().isEmpty()) {\n" //
							+ "			setButtonEnabled(buttonAdd, true);\n" //
							+ "			setButtonEnabled(buttonDuplicate, false);\n" //
							+ "			setButtonEnabled(buttonEdit, false);\n" //
							+ "			setButtonEnabled(buttonRemove, false);\n" //
							+ "		} else {\n" //
							+ "			setButtonEnabled(buttonAdd, false);\n" //
							+ "			setButtonEnabled(buttonDuplicate, true);\n" //
							+ "			setButtonEnabled(buttonEdit, true);\n" //
							+ "			setButtonEnabled(buttonRemove, true);\n" //
							+ "		}\n" //
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
							+ "	@Override\n" //
							+ "	protected void onAttach(AttachEvent attachEvent) {\n" //
							+ "		logger.info(\"BTable page layout opened for user '{}'.\", session.getUserName());\n" //
							+ "		super.onAttach(attachEvent);\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	protected void onDetach(DetachEvent detachEvent) {\n" //
							+ "		logger.info(\"onDetach\");\n" //
							+ "		super.onDetach(detachEvent);\n" //
							+ "		getElement().removeFromTree();\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private void updateGrid(int pageNumber) {\n" //
							+ "		grid\n" //
							+ "				.setItems(\n" //
							+ "						service\n" //
							+ "								.findAll(new PageParameters().setEntriesPerPage(Integer.MAX_VALUE).setPageNumber(pageNumber))\n" //
							+ "								.getEntries());\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private void addRecord() {\n" //
							+ "		Selectable[] selectableSubclasses =\n" //
							+ "				new SelectableSubclass[] {\n" //
							+ "						new SelectableSubclass(\n" //
							+ "								resourceManager\n" //
							+ "										.getLocalizedString(\n" //
							+ "												\"BTablePageView.subclass.selection.BHeirTable.label\",\n" //
							+ "												session.getLocalization()),\n" //
							+ "								\"BHeirTable\"),\n" //
							+ "						new SelectableSubclass(\n" //
							+ "								resourceManager\n" //
							+ "										.getLocalizedString(\n" //
							+ "												\"BTablePageView.subclass.selection.BHeirHeirTable.label\",\n" //
							+ "												session.getLocalization()),\n" //
							+ "								\"BHeirHeirTable\"),\n" //
							+ "						new SelectableSubclass(\n" //
							+ "								resourceManager\n" //
							+ "										.getLocalizedString(\n" //
							+ "												\"BTablePageView.subclass.selection.BTable.label\",\n" //
							+ "												session.getLocalization()),\n" //
							+ "								\"BTable\") };\n" //
							+ "		new SelectionDialog(\n" //
							+ "				buttonFactory,\n" //
							+ "				selectable -> switchToMaintenanceViewForANewObject(\n" //
							+ "						((SelectableSubclass) selectable).getSubclassName()),\n" //
							+ "				resourceManager,\n" //
							+ "				session,\n" //
							+ "				selectableSubclasses).open();\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private void switchToMaintenanceViewForANewObject(String selectedSubclassName) {\n" //
							+ "		QueryParameters parameters = new QueryParameters(Map.of(\"modelClass\", List.of(selectedSubclassName)));\n" //
							+ "		getUI().ifPresent(ui -> ui.navigate(BTableMaintenanceView.URL, parameters));\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private void duplicateRecord() {\n" //
							+ "		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" //
							+ "			QueryParameters parameters =\n" //
							+ "					new QueryParameters(Map.of(\"id\", List.of(\"\" + model.getId()), \"duplicate\", List.of(\"true\")));\n" //
							+ "			getUI().ifPresent(ui -> ui.navigate(BTableMaintenanceView.URL, parameters));\n" //
							+ "		});\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private void editRecord() {\n" //
							+ "		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" //
							+ "			QueryParameters parameters = new QueryParameters(Map.of(\"id\", List.of(\"\" + model.getId())));\n" //
							+ "			getUI().ifPresent(ui -> ui.navigate(BTableMaintenanceView.URL, parameters));\n" //
							+ "		});\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private void removeRecord() {\n" //
							+ "		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" //
							+ "			new RemoveConfirmDialog(buttonFactory, () -> {\n" //
							+ "				service.delete(model);\n" //
							+ "				updateGrid(0);\n" //
							+ "				buttonAdd.focus();\n" //
							+ "			}, resourceManager, session).open();\n" //
							+ "		});\n" //
							+ "	}\n" //
							+ "\n" //
							+ "}";
			DataModel dataModel = readDataModel("Model-Inheritance.xml");
			TableModel tableModel = dataModel.getTableByName("B_TABLE");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class GUI {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
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
					"import com.vaadin.flow.component.orderedlayout.Scroller;\n" + //
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
					"import base.pack.age.name.core.model.GuiTable;\n" + //
					"import base.pack.age.name.core.model.PageParameters;\n" + //
					"import base.pack.age.name.core.service.GuiTableService;\n" + //
					"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
					"import base.pack.age.name.gui.SessionData;\n" + //
					"import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" + //
					"import base.pack.age.name.gui.vaadin.component.Button;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" + //
					"import base.pack.age.name.gui.vaadin.component.MasterDataButtonLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" + //
					"import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"/**\n" + //
					" * A view for paginated guitable lists.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Route(GuiTablePageView.URL)\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class GuiTablePageView extends Scroller implements BeforeEnterObserver, HasUrlParameter<String> {\n"
					+ //
					"\n" + //
					"	public static final String URL = \"test-project/masterdata/atabellen\";\n" + //
					"\n" + //
					"	private static final Logger logger = LogManager.getLogger(GuiTablePageView.class);\n" + //
					"\n" + //
					"	@Autowired(required = false)\n" + //
					"	private MasterDataGridFieldRenderer<GuiTable> masterDataGridFieldRenderer;\n" + //
					"\n" + //
					"	private final ButtonFactory buttonFactory;\n" + //
					"	private final ResourceManager resourceManager;\n" + //
					"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
					"	private final GuiTableService service;\n" + //
					"	private final SessionData session;\n" + //
					"\n" + //
					"	private Button buttonAdd;\n" + //
					"	private Button buttonDuplicate;\n" + //
					"	private Button buttonEdit;\n" + //
					"	private Button buttonRemove;\n" + //
					"	private Grid<GuiTable> grid;\n" + //
					"	private VerticalLayout mainLayout;\n" + //
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
					"		mainLayout = new VerticalLayout();\n" + //
					"		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n"
					+ //
					"		getStyle().set(\"background-size\", \"cover\");\n" + //
					"		getStyle().set(\"background-attachment\", \"fixed\");\n" + //
					"		buttonAdd = buttonFactory.createAddButton(resourceManager, event -> addRecord(), session);\n"
					+ //
					"		buttonDuplicate = buttonFactory.createButton(resourceManager.getLocalizedString(\"commons.button.duplicate.text\", session.getLocalization()));\n"
					+ //
					"		buttonDuplicate.addClickListener(event -> duplicateRecord());\n" + //
					"		buttonEdit = buttonFactory.createEditButton(resourceManager, event -> editRecord(), session);\n"
					+ //
					"		buttonRemove = buttonFactory.createRemoveButton(resourceManager, event -> removeRecord(), session);\n"
					+ //
					"		grid = new Grid<>();\n" + //
					"		grid\n" + //
					"				.addColumn(model -> getHeaderString(\"REF\", model, () -> model.getRef()))\n" + //
					"				.setHeader(resourceManager.getLocalizedString(\"GuiTablePageView.grid.header.ref.label\", session.getLocalization()))\n"
					+ //
					"				.setSortable(true);\n" + //
					"		grid\n" + //
					"				.addColumn(model -> getHeaderString(\"NAME\", model, () -> model.getName()))\n" + //
					"				.setHeader(resourceManager.getLocalizedString(\"GuiTablePageView.grid.header.name.label\", session.getLocalization()))\n"
					+ //
					"				.setSortable(true);\n" + //
					"		grid.setMultiSort(true);\n" + //
					"		grid.setWidthFull();\n" + //
					"		grid.addSelectionListener(this::enabledButtons);\n" + //
					"		grid.getStyle().set(\"-moz-border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"-webkit-border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" + //
					"		MasterDataButtonLayout buttonLayout = new MasterDataButtonLayout(buttonAdd, buttonEdit, buttonDuplicate, buttonRemove);\n"
					+ //
					"		buttonLayout.setMargin(false);\n" + //
					"		buttonLayout.setWidthFull();\n" + //
					"		mainLayout.setMargin(false);\n" + //
					"		mainLayout.setSizeFull();\n" + //
					"		setSizeFull();\n" + //
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
					"		mainLayout.add(\n" + //
					"				new HeaderLayout(\n" + //
					"						buttonFactory.createBackButton(resourceManager, this::getUI, MasterDataView.URL, session),\n"
					+ //
					"						buttonFactory.createLogoutButton(resourceManager, this::getUI, session, logger),\n"
					+ //
					"						resourceManager.getLocalizedString(\"GuiTablePageView.header.label\", session.getLocalization()),\n"
					+ //
					"						HeaderLayoutMode.PLAIN),\n" + //
					"				dataLayout);\n" + //
					"		updateGrid(0);\n" + //
					"		setButtonEnabled(buttonDuplicate, false);\n" + //
					"		setButtonEnabled(buttonEdit, false);\n" + //
					"		setButtonEnabled(buttonRemove, false);\n" + //
					"		setContent(mainLayout);\n" + //
					"		buttonAdd.focus();\n" + //
					"	}\n" + //
					"\n" + //
					"	private Object getHeaderString(String fieldName, GuiTable aTable, Supplier<?> f) {\n" + //
					"		return masterDataGridFieldRenderer != null && masterDataGridFieldRenderer.hasRenderingFor(fieldName)\n"
					+ //
					"				? masterDataGridFieldRenderer.getHeaderString(fieldName, aTable)\n" + //
					"				: f.get();\n" + //
					"	}\n" + //
					"\n" + //
					"	private void enabledButtons(SelectionEvent<Grid<GuiTable>, GuiTable> event) {\n" + //
					"		if (event.getFirstSelectedItem().isEmpty()) {\n" + //
					"			setButtonEnabled(buttonAdd, true);\n" + //
					"			setButtonEnabled(buttonDuplicate, false);\n" + //
					"			setButtonEnabled(buttonEdit, false);\n" + //
					"			setButtonEnabled(buttonRemove, false);\n" + //
					"		} else {\n" + //
					"			setButtonEnabled(buttonAdd, false);\n" + //
					"			setButtonEnabled(buttonDuplicate, true);\n" + //
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
					"		logger.info(\"GuiTable page layout opened for user '{}'.\", session.getUserName());\n" + //
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
					"		getUI().ifPresent(ui -> ui.navigate(GuiTableMaintenanceView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"	private void duplicateRecord() {\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			QueryParameters parameters =\n" + //
					"					new QueryParameters(Map.of(\"id\", List.of(\"\" + model.getId()), \"duplicate\", List.of(\"true\")));\n"
					+ //
					"			getUI().ifPresent(ui -> ui.navigate(GuiTableMaintenanceView.URL, parameters));\n" + //
					"		});\n" + //
					"	}\n" + //
					"\n" + //
					"	private void editRecord() {\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			QueryParameters parameters = new QueryParameters(Map.of(\"id\", List.of(\"\" + model.getId())));\n"
					+ //
					"			getUI().ifPresent(ui -> ui.navigate(GuiTableMaintenanceView.URL, parameters));\n" + //
					"		});\n" + //
					"	}\n" + //
					"\n" + //
					"	private void removeRecord() {\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			new RemoveConfirmDialog(buttonFactory, () -> {\n" + //
					"				service.delete(model);\n" + //
					"				updateGrid(0);\n" + //
					"				buttonAdd.focus();\n" + //
					"			}, resourceManager, session).open();\n" + //
					"		});\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			TableModel tableModel = dataModel.getTableByName("GUI_TABLE");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void withFilter() {
			// Prepare
			String expected = "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
					"\n" + //
					"import java.util.ArrayList;\n" + //
					"import java.util.List;\n" + //
					"import java.util.Map;\n" + //
					"import java.util.StringTokenizer;\n" + //
					"import java.util.function.Supplier;\n" + //
					"import java.util.stream.Collectors;\n" + //
					"\n" + //
					"import org.apache.logging.log4j.LogManager;\n" + //
					"import org.apache.logging.log4j.Logger;\n" + //
					"import org.springframework.beans.factory.annotation.Autowired;\n" + //
					"\n" + //
					"import com.vaadin.flow.component.AttachEvent;\n" + //
					"import com.vaadin.flow.component.DetachEvent;\n" + //
					"import com.vaadin.flow.component.Key;\n" + //
					"import com.vaadin.flow.component.grid.Grid;\n" + //
					"import com.vaadin.flow.component.orderedlayout.Scroller;\n" + //
					"import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" + //
					"import com.vaadin.flow.component.textfield.TextField;\n" + //
					"import com.vaadin.flow.data.selection.SelectionEvent;\n" + //
					"import com.vaadin.flow.router.BeforeEnterEvent;\n" + //
					"import com.vaadin.flow.router.BeforeEnterObserver;\n" + //
					"import com.vaadin.flow.router.BeforeEvent;\n" + //
					"import com.vaadin.flow.router.HasUrlParameter;\n" + //
					"import com.vaadin.flow.router.OptionalParameter;\n" + //
					"import com.vaadin.flow.router.QueryParameters;\n" + //
					"import com.vaadin.flow.router.Route;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.GuiTable;\n" + //
					"import base.pack.age.name.core.model.PageParameters;\n" + //
					"import base.pack.age.name.core.service.GuiTableService;\n" + //
					"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
					"import base.pack.age.name.gui.SessionData;\n" + //
					"import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" + //
					"import base.pack.age.name.gui.vaadin.component.Button;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" + //
					"import base.pack.age.name.gui.vaadin.component.MasterDataButtonLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" + //
					"import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"/**\n" + //
					" * A view for paginated guitable lists.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Route(GuiTablePageView.URL)\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class GuiTablePageView extends Scroller implements BeforeEnterObserver, HasUrlParameter<String> {\n"
					+ //
					"\n" + //
					"	public static final String URL = \"test-project/masterdata/atabellen\";\n" + //
					"\n" + //
					"	private static final Logger logger = LogManager.getLogger(GuiTablePageView.class);\n" + //
					"	private static final String PARAMETER_FILTER = \"GuiTablePageView.Filter\";\n" + //
					"\n" + //
					"	@Autowired(required = false)\n" + //
					"	private MasterDataGridFieldRenderer<GuiTable> masterDataGridFieldRenderer;\n" + //
					"\n" + //
					"	private final ButtonFactory buttonFactory;\n" + //
					"	private final ResourceManager resourceManager;\n" + //
					"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
					"	private final GuiTableService service;\n" + //
					"	private final SessionData session;\n" + //
					"\n" + //
					"	private Button buttonAdd;\n" + //
					"	private Button buttonDuplicate;\n" + //
					"	private Button buttonEdit;\n" + //
					"	private Button buttonRemove;\n" + //
					"	private Grid<GuiTable> grid;\n" + //
					"	private TextField textFieldFilter;\n" + //
					"	private VerticalLayout mainLayout;\n" + //
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
					"		mainLayout = new VerticalLayout();\n" + //
					"		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n"
					+ //
					"		getStyle().set(\"background-size\", \"cover\");\n" + //
					"		getStyle().set(\"background-attachment\", \"fixed\");\n" + //
					"		textFieldFilter = new TextField();\n" + //
					"		textFieldFilter.addKeyUpListener(event -> {\n" + //
					"			if (event.getKey() == Key.ENTER) {\n" + //
					"				updateGrid(0);\n" + //
					"			}\n" + //
					"		});\n" + //
					"		textFieldFilter.setWidthFull();\n" + //
					"		session.findParameter(PARAMETER_FILTER).ifPresent(s -> textFieldFilter.setValue(s.toString()));\n"
					+ //
					"		textFieldFilter.addValueChangeListener(event -> updateGrid(0));\n" + //
					"		buttonAdd = buttonFactory.createAddButton(resourceManager, event -> addRecord(), session);\n"
					+ //
					"		buttonDuplicate = buttonFactory.createButton(resourceManager.getLocalizedString(\"commons.button.duplicate.text\", session.getLocalization()));\n"
					+ //
					"		buttonDuplicate.addClickListener(event -> duplicateRecord());\n" + //
					"		buttonEdit = buttonFactory.createEditButton(resourceManager, event -> editRecord(), session);\n"
					+ //
					"		buttonRemove = buttonFactory.createRemoveButton(resourceManager, event -> removeRecord(), session);\n"
					+ //
					"		grid = new Grid<>();\n" + //
					"		grid\n" + //
					"				.addColumn(model -> getHeaderString(\"REF\", model, () -> model.getRef()))\n" + //
					"				.setHeader(resourceManager.getLocalizedString(\"GuiTablePageView.grid.header.ref.label\", session.getLocalization()))\n"
					+ //
					"				.setSortable(true);\n" + //
					"		grid\n" + //
					"				.addColumn(model -> getHeaderString(\"NAME\", model, () -> model.getName()))\n" + //
					"				.setHeader(resourceManager.getLocalizedString(\"GuiTablePageView.grid.header.name.label\", session.getLocalization()))\n"
					+ //
					"				.setSortable(true);\n" + //
					"		grid.setMultiSort(true);\n" + //
					"		grid.setWidthFull();\n" + //
					"		grid.addSelectionListener(this::enabledButtons);\n" + //
					"		grid.getStyle().set(\"-moz-border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"-webkit-border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" + //
					"		MasterDataButtonLayout buttonLayout = new MasterDataButtonLayout(buttonAdd, buttonEdit, buttonDuplicate, buttonRemove);\n"
					+ //
					"		buttonLayout.setMargin(false);\n" + //
					"		buttonLayout.setWidthFull();\n" + //
					"		mainLayout.setMargin(false);\n" + //
					"		mainLayout.setSizeFull();\n" + //
					"		setSizeFull();\n" + //
					"		VerticalLayout filterLayout = new VerticalLayout();\n" + //
					"		filterLayout.getStyle().set(\"-moz-border-radius\", \"4px\");\n" + //
					"		filterLayout.getStyle().set(\"-webkit-border-radius\", \"4px\");\n" + //
					"		filterLayout.getStyle().set(\"border-radius\", \"4px\");\n" + //
					"		filterLayout.getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" + //
					"		filterLayout\n" + //
					"				.getStyle()\n" + //
					"				.set(\n" + //
					"						\"box-shadow\",\n" + //
					"						\"10px 10px 20px #e4e4e4, -10px 10px 20px #e4e4e4, -10px -10px 20px #e4e4e4, 10px -10px 20px #e4e4e4\");\n"
					+ //
					"		filterLayout.setMargin(false);\n" + //
					"		filterLayout.setWidthFull();\n" + //
					"		filterLayout.add(textFieldFilter);\n" + //
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
					"		mainLayout.add(\n" + //
					"				new HeaderLayout(\n" + //
					"						buttonFactory.createBackButton(resourceManager, this::getUI, MasterDataView.URL, session),\n"
					+ //
					"						buttonFactory.createLogoutButton(resourceManager, this::getUI, session, logger),\n"
					+ //
					"						resourceManager.getLocalizedString(\"GuiTablePageView.header.label\", session.getLocalization()),\n"
					+ //
					"						HeaderLayoutMode.PLAIN),\n" + //
					"				filterLayout,\n" + //
					"				dataLayout);\n" + //
					"		updateGrid(0);\n" + //
					"		setButtonEnabled(buttonDuplicate, false);\n" + //
					"		setButtonEnabled(buttonEdit, false);\n" + //
					"		setButtonEnabled(buttonRemove, false);\n" + //
					"		setContent(mainLayout);\n" + //
					"		textFieldFilter.focus();\n" + //
					"	}\n" + //
					"\n" + //
					"	private Object getHeaderString(String fieldName, GuiTable aTable, Supplier<?> f) {\n" + //
					"		return masterDataGridFieldRenderer != null && masterDataGridFieldRenderer.hasRenderingFor(fieldName)\n"
					+ //
					"				? masterDataGridFieldRenderer.getHeaderString(fieldName, aTable)\n" + //
					"				: f.get();\n" + //
					"	}\n" + //
					"\n" + //
					"	private void enabledButtons(SelectionEvent<Grid<GuiTable>, GuiTable> event) {\n" + //
					"		if (event.getFirstSelectedItem().isEmpty()) {\n" + //
					"			setButtonEnabled(buttonAdd, true);\n" + //
					"			setButtonEnabled(buttonDuplicate, false);\n" + //
					"			setButtonEnabled(buttonEdit, false);\n" + //
					"			setButtonEnabled(buttonRemove, false);\n" + //
					"		} else {\n" + //
					"			setButtonEnabled(buttonAdd, false);\n" + //
					"			setButtonEnabled(buttonDuplicate, true);\n" + //
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
					"		logger.info(\"GuiTable page layout opened for user '{}'.\", session.getUserName());\n" + //
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
					"								.getEntries()\n" + //
					"								.stream()\n" + //
					"								.filter(this::isMatching)\n" + //
					"								.collect(Collectors.toList()));\n" + //
					"	}\n" + //
					"\n" + //
					"	private boolean isMatching(GuiTable model) {\n" + //
					"		List<String> patterns =\n" + //
					"				getWords(textFieldFilter.getValue()).stream().map(s -> s.toLowerCase()).collect(Collectors.toList());\n"
					+ //
					"		if (patterns.isEmpty()) {\n" + //
					"			return true;\n" + //
					"		}\n" + //
					"		boolean b = true;\n" + //
					"		for (String pattern : patterns) {\n" + //
					"			b &= isMatchingPattern(pattern.toLowerCase(), model);\n" + //
					"		}\n" + //
					"		return b;\n" + //
					"	}\n" + //
					"\n" + //
					"	private List<String> getWords(String s) {\n" + //
					"		List<String> l = new ArrayList<>();\n" + //
					"		if (s != null) {\n" + //
					"			StringTokenizer st = new StringTokenizer(s, \" \");\n" + //
					"			while (st.hasMoreTokens()) {\n" + //
					"				l.add(st.nextToken());\n" + //
					"			}\n" + //
					"		}\n" + //
					"		return l;\n" + //
					"	}\n" + //
					"\n" + //
					"	private boolean isMatchingPattern(String pattern, GuiTable model) {\n" + //
					"		boolean result = false;\n" + //
					"		result = result || getHeaderString(GuiTable.REF, model, () -> model.getRef()).toString().toLowerCase().contains(pattern);\n"
					+ //
					"		result = result || getHeaderString(GuiTable.NAME, model, () -> model.getName()).toString().toLowerCase().contains(pattern);\n"
					+ //
					"		return result;\n" + //
					"	}\n" + //
					"\n" + //
					"	private void addRecord() {\n" + //
					"		session.setParameter(PARAMETER_FILTER, textFieldFilter.getValue());\n" + //
					"		getUI().ifPresent(ui -> ui.navigate(GuiTableMaintenanceView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"	private void duplicateRecord() {\n" + //
					"		session.setParameter(PARAMETER_FILTER, textFieldFilter.getValue());\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			QueryParameters parameters =\n" + //
					"					new QueryParameters(Map.of(\"id\", List.of(\"\" + model.getId()), \"duplicate\", List.of(\"true\")));\n"
					+ //
					"			getUI().ifPresent(ui -> ui.navigate(GuiTableMaintenanceView.URL, parameters));\n" + //
					"		});\n" + //
					"	}\n" + //
					"\n" + //
					"	private void editRecord() {\n" + //
					"		session.setParameter(PARAMETER_FILTER, textFieldFilter.getValue());\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			QueryParameters parameters = new QueryParameters(Map.of(\"id\", List.of(\"\" + model.getId())));\n"
					+ //
					"			getUI().ifPresent(ui -> ui.navigate(GuiTableMaintenanceView.URL, parameters));\n" + //
					"		});\n" + //
					"	}\n" + //
					"\n" + //
					"	private void removeRecord() {\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			new RemoveConfirmDialog(buttonFactory, () -> {\n" + //
					"				service.delete(model);\n" + //
					"				updateGrid(0);\n" + //
					"				textFieldFilter.focus();\n" + //
					"			}, resourceManager, session).open();\n" + //
					"		});\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			TableModel tableModel = dataModel.getTableByName("GUI_TABLE");
			tableModel.getColumnByName("REF").addOption(new Option("FILTER"));
			tableModel.getColumnByName("NAME").addOption(new Option("FILTER"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void withDifferentSpecialFields() {
			// Prepare
			String expected = "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
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
					"import com.vaadin.flow.component.orderedlayout.Scroller;\n" + //
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
					"import base.pack.age.name.core.model.TableWithSpecials;\n" + //
					"import base.pack.age.name.core.model.PageParameters;\n" + //
					"import base.pack.age.name.core.service.TableWithSpecialsService;\n" + //
					"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
					"import base.pack.age.name.gui.SessionData;\n" + //
					"import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" + //
					"import base.pack.age.name.gui.vaadin.component.Button;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" + //
					"import base.pack.age.name.gui.vaadin.component.MasterDataButtonLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" + //
					"import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"/**\n" + //
					" * A view for paginated tablewithspecials lists.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Route(TableWithSpecialsPageView.URL)\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class TableWithSpecialsPageView extends Scroller implements BeforeEnterObserver, HasUrlParameter<String> {\n"
					+ //
					"\n" + //
					"	public static final String URL = \"test-ws/masterdata/atabellen\";\n" + //
					"\n" + //
					"	private static final Logger logger = LogManager.getLogger(TableWithSpecialsPageView.class);\n" + //
					"\n" + //
					"	@Autowired(required = false)\n" + //
					"	private MasterDataGridFieldRenderer<TableWithSpecials> masterDataGridFieldRenderer;\n" + //
					"\n" + //
					"	private final ButtonFactory buttonFactory;\n" + //
					"	private final ResourceManager resourceManager;\n" + //
					"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
					"	private final TableWithSpecialsService service;\n" + //
					"	private final SessionData session;\n" + //
					"\n" + //
					"	private Button buttonAdd;\n" + //
					"	private Button buttonDuplicate;\n" + //
					"	private Button buttonEdit;\n" + //
					"	private Button buttonRemove;\n" + //
					"	private Grid<TableWithSpecials> grid;\n" + //
					"	private VerticalLayout mainLayout;\n" + //
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
					"		mainLayout = new VerticalLayout();\n" + //
					"		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n"
					+ //
					"		getStyle().set(\"background-size\", \"cover\");\n" + //
					"		getStyle().set(\"background-attachment\", \"fixed\");\n" + //
					"		buttonAdd = buttonFactory.createAddButton(resourceManager, event -> addRecord(), session);\n"
					+ //
					"		buttonDuplicate = buttonFactory.createButton(resourceManager.getLocalizedString(\"commons.button.duplicate.text\", session.getLocalization()));\n"
					+ //
					"		buttonDuplicate.addClickListener(event -> duplicateRecord());\n" + //
					"		buttonEdit = buttonFactory.createEditButton(resourceManager, event -> editRecord(), session);\n"
					+ //
					"		buttonRemove = buttonFactory.createRemoveButton(resourceManager, event -> removeRecord(), session);\n"
					+ //
					"		grid = new Grid<>();\n" + //
					"		grid\n" + //
					"				.addColumn(model -> getHeaderString(\"ENUMFIELD\", model, () -> model.getEnumField()))\n"
					+ //
					"				.setHeader(resourceManager.getLocalizedString(\"TableWithSpecialsPageView.grid.header.enumfield.label\", session.getLocalization()))\n"
					+ //
					"				.setSortable(true);\n" + //
					"		grid\n" + //
					"				.addColumn(model -> getHeaderString(\"FLAG\", model, () -> model.getFlag()))\n" + //
					"				.setHeader(resourceManager.getLocalizedString(\"TableWithSpecialsPageView.grid.header.flag.label\", session.getLocalization()))\n"
					+ //
					"				.setSortable(true);\n" + //
					"		grid\n" + //
					"				.addColumn(model -> getHeaderString(\"LONGTEXT\", model, () -> model.getLongtext()))\n"
					+ //
					"				.setHeader(resourceManager.getLocalizedString(\"TableWithSpecialsPageView.grid.header.longtext.label\", session.getLocalization()))\n"
					+ //
					"				.setSortable(true);\n" + //
					"		grid.setMultiSort(true);\n" + //
					"		grid.setWidthFull();\n" + //
					"		grid.addSelectionListener(this::enabledButtons);\n" + //
					"		grid.getStyle().set(\"-moz-border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"-webkit-border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" + //
					"		MasterDataButtonLayout buttonLayout = new MasterDataButtonLayout(buttonAdd, buttonEdit, buttonDuplicate, buttonRemove);\n"
					+ //
					"		buttonLayout.setMargin(false);\n" + //
					"		buttonLayout.setWidthFull();\n" + //
					"		mainLayout.setMargin(false);\n" + //
					"		mainLayout.setSizeFull();\n" + //
					"		setSizeFull();\n" + //
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
					"		mainLayout.add(\n" + //
					"				new HeaderLayout(\n" + //
					"						buttonFactory.createBackButton(resourceManager, this::getUI, MasterDataView.URL, session),\n"
					+ //
					"						buttonFactory.createLogoutButton(resourceManager, this::getUI, session, logger),\n"
					+ //
					"						resourceManager.getLocalizedString(\"TableWithSpecialsPageView.header.label\", session.getLocalization()),\n"
					+ //
					"						HeaderLayoutMode.PLAIN),\n" + //
					"				dataLayout);\n" + //
					"		updateGrid(0);\n" + //
					"		setButtonEnabled(buttonDuplicate, false);\n" + //
					"		setButtonEnabled(buttonEdit, false);\n" + //
					"		setButtonEnabled(buttonRemove, false);\n" + //
					"		setContent(mainLayout);\n" + //
					"		buttonAdd.focus();\n" + //
					"	}\n" + //
					"\n" + //
					"	private Object getHeaderString(String fieldName, TableWithSpecials aTable, Supplier<?> f) {\n" + //
					"		return masterDataGridFieldRenderer != null && masterDataGridFieldRenderer.hasRenderingFor(fieldName)\n"
					+ //
					"				? masterDataGridFieldRenderer.getHeaderString(fieldName, aTable)\n" + //
					"				: f.get();\n" + //
					"	}\n" + //
					"\n" + //
					"	private void enabledButtons(SelectionEvent<Grid<TableWithSpecials>, TableWithSpecials> event) {\n"
					+ //
					"		if (event.getFirstSelectedItem().isEmpty()) {\n" + //
					"			setButtonEnabled(buttonAdd, true);\n" + //
					"			setButtonEnabled(buttonDuplicate, false);\n" + //
					"			setButtonEnabled(buttonEdit, false);\n" + //
					"			setButtonEnabled(buttonRemove, false);\n" + //
					"		} else {\n" + //
					"			setButtonEnabled(buttonAdd, false);\n" + //
					"			setButtonEnabled(buttonDuplicate, true);\n" + //
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
					"		logger.info(\"TableWithSpecials page layout opened for user '{}'.\", session.getUserName());\n"
					+ //
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
					"		getUI().ifPresent(ui -> ui.navigate(TableWithSpecialsMaintenanceView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"	private void duplicateRecord() {\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			QueryParameters parameters =\n" + //
					"					new QueryParameters(Map.of(\"id\", List.of(\"\" + model.getId()), \"duplicate\", List.of(\"true\")));\n"
					+ //
					"			getUI().ifPresent(ui -> ui.navigate(TableWithSpecialsMaintenanceView.URL, parameters));\n"
					+ //
					"		});\n" + //
					"	}\n" + //
					"\n" + //
					"	private void editRecord() {\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			QueryParameters parameters = new QueryParameters(Map.of(\"id\", List.of(\"\" + model.getId())));\n"
					+ //
					"			getUI().ifPresent(ui -> ui.navigate(TableWithSpecialsMaintenanceView.URL, parameters));\n"
					+ //
					"		});\n" + //
					"	}\n" + //
					"\n" + //
					"	private void removeRecord() {\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			new RemoveConfirmDialog(buttonFactory, () -> {\n" + //
					"				service.delete(model);\n" + //
					"				updateGrid(0);\n" + //
					"				buttonAdd.focus();\n" + //
					"			}, resourceManager, session).open();\n" + //
					"		});\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			TableModel tableModel = dataModel.getTableByName("TABLE_WITH_SPECIALS");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void withGridFields() {
			// Prepare
			String expected = "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
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
					"import com.vaadin.flow.component.orderedlayout.Scroller;\n" + //
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
					"import base.pack.age.name.core.model.TableWithGridFields;\n" + //
					"import base.pack.age.name.core.model.PageParameters;\n" + //
					"import base.pack.age.name.core.service.TableWithGridFieldsService;\n" + //
					"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
					"import base.pack.age.name.gui.SessionData;\n" + //
					"import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" + //
					"import base.pack.age.name.gui.vaadin.component.Button;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" + //
					"import base.pack.age.name.gui.vaadin.component.MasterDataButtonLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" + //
					"import base.pack.age.name.gui.vaadin.component.RemoveConfirmDialog;\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"/**\n" + //
					" * A view for paginated tablewithgridfields lists.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Route(TableWithGridFieldsPageView.URL)\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class TableWithGridFieldsPageView extends Scroller implements BeforeEnterObserver, HasUrlParameter<String> {\n"
					+ //
					"\n" + //
					"	public static final String URL = \"test-ws/masterdata/atabellen\";\n" + //
					"\n" + //
					"	private static final Logger logger = LogManager.getLogger(TableWithGridFieldsPageView.class);\n"
					+ //
					"\n" + //
					"	@Autowired(required = false)\n" + //
					"	private MasterDataGridFieldRenderer<TableWithGridFields> masterDataGridFieldRenderer;\n" + //
					"\n" + //
					"	private final ButtonFactory buttonFactory;\n" + //
					"	private final ResourceManager resourceManager;\n" + //
					"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
					"	private final TableWithGridFieldsService service;\n" + //
					"	private final SessionData session;\n" + //
					"\n" + //
					"	private Button buttonAdd;\n" + //
					"	private Button buttonDuplicate;\n" + //
					"	private Button buttonEdit;\n" + //
					"	private Button buttonRemove;\n" + //
					"	private Grid<TableWithGridFields> grid;\n" + //
					"	private VerticalLayout mainLayout;\n" + //
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
					"		mainLayout = new VerticalLayout();\n" + //
					"		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n"
					+ //
					"		getStyle().set(\"background-size\", \"cover\");\n" + //
					"		getStyle().set(\"background-attachment\", \"fixed\");\n" + //
					"		buttonAdd = buttonFactory.createAddButton(resourceManager, event -> addRecord(), session);\n"
					+ //
					"		buttonDuplicate = buttonFactory.createButton(resourceManager.getLocalizedString(\"commons.button.duplicate.text\", session.getLocalization()));\n"
					+ //
					"		buttonDuplicate.addClickListener(event -> duplicateRecord());\n" + //
					"		buttonEdit = buttonFactory.createEditButton(resourceManager, event -> editRecord(), session);\n"
					+ //
					"		buttonRemove = buttonFactory.createRemoveButton(resourceManager, event -> removeRecord(), session);\n"
					+ //
					"		grid = new Grid<>();\n" + //
					"		grid\n" + //
					"				.addColumn(model -> getHeaderString(\"ENUMFIELD\", model, () -> model.getEnumField()))\n"
					+ //
					"				.setHeader(resourceManager.getLocalizedString(\"TableWithGridFieldsPageView.grid.header.enumfield.label\", session.getLocalization()))\n"
					+ //
					"				.setSortable(true);\n" + //
					"		grid\n" + //
					"				.addColumn(model -> getHeaderString(\"FLAG\", model, () -> model.getFlag()))\n" + //
					"				.setHeader(resourceManager.getLocalizedString(\"TableWithGridFieldsPageView.grid.header.flag.label\", session.getLocalization()))\n"
					+ //
					"				.setSortable(true);\n" + //
					"		grid.setMultiSort(true);\n" + //
					"		grid.setWidthFull();\n" + //
					"		grid.addSelectionListener(this::enabledButtons);\n" + //
					"		grid.getStyle().set(\"-moz-border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"-webkit-border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"border-radius\", \"4px\");\n" + //
					"		grid.getStyle().set(\"border\", \"1px solid #A9A9A9\");\n" + //
					"		MasterDataButtonLayout buttonLayout = new MasterDataButtonLayout(buttonAdd, buttonEdit, buttonDuplicate, buttonRemove);\n"
					+ //
					"		buttonLayout.setMargin(false);\n" + //
					"		buttonLayout.setWidthFull();\n" + //
					"		mainLayout.setMargin(false);\n" + //
					"		mainLayout.setSizeFull();\n" + //
					"		setSizeFull();\n" + //
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
					"		mainLayout.add(\n" + //
					"				new HeaderLayout(\n" + //
					"						buttonFactory.createBackButton(resourceManager, this::getUI, MasterDataView.URL, session),\n"
					+ //
					"						buttonFactory.createLogoutButton(resourceManager, this::getUI, session, logger),\n"
					+ //
					"						resourceManager.getLocalizedString(\"TableWithGridFieldsPageView.header.label\", session.getLocalization()),\n"
					+ //
					"						HeaderLayoutMode.PLAIN),\n" + //
					"				dataLayout);\n" + //
					"		updateGrid(0);\n" + //
					"		setButtonEnabled(buttonDuplicate, false);\n" + //
					"		setButtonEnabled(buttonEdit, false);\n" + //
					"		setButtonEnabled(buttonRemove, false);\n" + //
					"		setContent(mainLayout);\n" + //
					"		buttonAdd.focus();\n" + //
					"	}\n" + //
					"\n" + //
					"	private Object getHeaderString(String fieldName, TableWithGridFields aTable, Supplier<?> f) {\n"
					+ //
					"		return masterDataGridFieldRenderer != null && masterDataGridFieldRenderer.hasRenderingFor(fieldName)\n"
					+ //
					"				? masterDataGridFieldRenderer.getHeaderString(fieldName, aTable)\n" + //
					"				: f.get();\n" + //
					"	}\n" + //
					"\n" + //
					"	private void enabledButtons(SelectionEvent<Grid<TableWithGridFields>, TableWithGridFields> event) {\n"
					+ //
					"		if (event.getFirstSelectedItem().isEmpty()) {\n" + //
					"			setButtonEnabled(buttonAdd, true);\n" + //
					"			setButtonEnabled(buttonDuplicate, false);\n" + //
					"			setButtonEnabled(buttonEdit, false);\n" + //
					"			setButtonEnabled(buttonRemove, false);\n" + //
					"		} else {\n" + //
					"			setButtonEnabled(buttonAdd, false);\n" + //
					"			setButtonEnabled(buttonDuplicate, true);\n" + //
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
					"		logger.info(\"TableWithGridFields page layout opened for user '{}'.\", session.getUserName());\n"
					+ //
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
					"		getUI().ifPresent(ui -> ui.navigate(TableWithGridFieldsMaintenanceView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"	private void duplicateRecord() {\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			QueryParameters parameters =\n" + //
					"					new QueryParameters(Map.of(\"id\", List.of(\"\" + model.getId()), \"duplicate\", List.of(\"true\")));\n"
					+ //
					"			getUI().ifPresent(ui -> ui.navigate(TableWithGridFieldsMaintenanceView.URL, parameters));\n"
					+ //
					"		});\n" + //
					"	}\n" + //
					"\n" + //
					"	private void editRecord() {\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			QueryParameters parameters = new QueryParameters(Map.of(\"id\", List.of(\"\" + model.getId())));\n"
					+ //
					"			getUI().ifPresent(ui -> ui.navigate(TableWithGridFieldsMaintenanceView.URL, parameters));\n"
					+ //
					"		});\n" + //
					"	}\n" + //
					"\n" + //
					"	private void removeRecord() {\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			new RemoveConfirmDialog(buttonFactory, () -> {\n" + //
					"				service.delete(model);\n" + //
					"				updateGrid(0);\n" + //
					"				buttonAdd.focus();\n" + //
					"			}, resourceManager, session).open();\n" + //
					"		});\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			TableModel tableModel = dataModel.getTableByName("TABLE_WITH_GRID_FIELDS");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, tableModel);
			// Check
			assertEquals(expected, returned);
		}

	}

}