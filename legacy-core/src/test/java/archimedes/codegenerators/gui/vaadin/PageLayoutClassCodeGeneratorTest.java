package archimedes.codegenerators.gui.vaadin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
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

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.Map;\n" + //
					"\n" + //
					"import org.apache.logging.log4j.LogManager;\n" + //
					"import org.apache.logging.log4j.Logger;\n" + //
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
			        "import base.pack.age.name.gui.vaadin.SessionData;\n" + //
			        "import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" + //
			        "import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" + //
					"import ${UserAuthorizationCheckerPackageName}.${UserAuthorizationCheckerClassName};\n" + //
					"import base.pack.age.name.gui.vaadin.component.Button;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
			        "import base.pack.age.name.gui.vaadin.component.MasterDataButtonLayout;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"/**\n" + //
					" * A view for paginated atable lists.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Route(ATablePageLayout.URL)\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class ATablePageLayout extends VerticalLayout implements BeforeEnterObserver, HasUrlParameter<String> {\n"
					+ //
					"\n" + //
					"	public static final String URL = \"${BaseURL}/masterdata/atables\";\n" + //
					"\n" + //
					"	private static final Logger logger = LogManager.getLogger(ATablePageLayout.class);\n" + //
					"\n" + //
					"	private final ResourceManager resourceManager;\n" + //
					"	private final ATableService service;\n" + //
			        "	private final SessionData session;\n"
			        + //
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
					"		${UserAuthorizationCheckerClassName}.forwardToLoginOnNoUserSetForSession(session, beforeEnterEvent);\n"
					+ //
					"		getStyle().set(\"background-image\", \"url('${BackgroundImageName}')\");\n" + //
					"		buttonAdd = ButtonFactory.createAddButton(resourceManager, event -> addRecord(), session);\n"
					+ //
					"		buttonEdit = ButtonFactory.createEditButton(resourceManager, event -> editRecord(), session);\n"
					+ //
					"		buttonRemove = ButtonFactory.createRemoveButton(resourceManager, event -> removeRecord(), session);\n"
					+ //
					"		grid = new Grid<>();\n" + //
					"		grid.setWidthFull();\n" + //
					"		grid.addSelectionListener(this::enabledButtons);\n" + //
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
					"		dataLayout.getStyle().set(\"border\", \"1px solid gray\");\n" + //
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
			        "				new HeaderLayout(\n"
			        + //
					"						ButtonFactory.createBackButton(resourceManager, this::getUI, ${MasterDataLayoutClassName}.URL, session),\n"
					+ //
					"						ButtonFactory.createLogoutButton(resourceManager, this::getUI, session, logger),\n"
					+ //
					"						\"atables\",\n" + //
					"						HeaderLayoutMode.PLAIN),\n" + //
					"				dataLayout);\n" + //
					"		updateGrid(0);\n" + //
					"		setButtonEnabled(buttonEdit, false);\n" + //
					"		setButtonEnabled(buttonRemove, false);\n" + //
					"	}\n" + //
					"\n" + //
					"	private void enabledButtons(SelectionEvent<Grid<ATable>, ATable> event) {\n"
					+ //
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
					"			button.setBackgroundImage(\"${ButtonBackGround}\");\n" + //
					"			button.setBorderColor(\"${ButtonBorderColor}\");\n" + //
					"		} else {\n" + //
					"			button.setBackgroundImage(\"${ButtonBackGroundDisabled}\");\n" + //
					"			button.setBorderColor(\"${ButtonBorderColorDisabled}\");\n" + //
					"		}\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	protected void onAttach(AttachEvent attachEvent) {\n" + //
					"		logger.info(\"ATable page layout opened for user '{}'.\", session.getUserName());\n"
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
					"								.findAll(new PageParameters().setEntriesPerPage(10).setPageNumber(pageNumber))\n"
					+ //
					"								.getEntries());\n" + //
					"	}\n" + //
					"\n" + //
					"	private void addRecord() {\n" + //
					"		getUI().ifPresent(ui -> ui.navigate(${MaintenanceLayoutClassName}.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"	private void editRecord() {\n" + //
					"		grid.getSelectedItems().stream().findFirst().ifPresent(model -> {\n" + //
					"			QueryParameters parameters = new QueryParameters(Map.of(\"id\", List.of(\"\" + model.getId())));\n"
					+ //
					"			getUI().ifPresent(ui -> ui.navigate(${MaintenanceLayoutClassName}.URL, parameters));\n"
					+ //
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
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}