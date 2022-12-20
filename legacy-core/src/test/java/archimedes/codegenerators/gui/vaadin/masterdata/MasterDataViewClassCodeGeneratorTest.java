package archimedes.codegenerators.gui.vaadin.masterdata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class MasterDataViewClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private MasterDataViewClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected(null, "gui.vaadin.masterdata", false, "null");
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, null);
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(String prefix, String packageName, boolean suppressComment, String noKeyValue) {
			String s =
					"package " + BASE_PACKAGE_NAME + "." + (prefix != null ? prefix + "." : "") + packageName + ";\n" //
							+ "\n" //
							+ "import org.apache.logging.log4j.LogManager;\n" //
							+ "import org.apache.logging.log4j.Logger;\n" //
							+ "\n" //
							+ "import com.vaadin.flow.component.orderedlayout.VerticalLayout;\n" //
							+ "import com.vaadin.flow.router.BeforeEnterEvent;\n" //
							+ "import com.vaadin.flow.router.BeforeEnterObserver;\n" //
							+ "import com.vaadin.flow.router.BeforeEvent;\n" //
							+ "import com.vaadin.flow.router.HasUrlParameter;\n" //
							+ "import com.vaadin.flow.router.OptionalParameter;\n" //
							+ "import com.vaadin.flow.router.Route;\n" //
							+ "\n" //
							+ "import base.pack.age.name.core.service.localization.ResourceManager;\n" //
							+ "import base.pack.age.name.gui.SessionData;\n" //
							+ "import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" //
							+ "import base.pack.age.name.gui.vaadin.MainMenuView;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.Button;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.ButtonGrid;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" //
							+ "import base.pack.age.name.gui.vaadin.masterdata.MasterDataGUIConfiguration;\n" //
							+ "\n" //
							+ "import lombok.Generated;\n" //
							+ "import lombok.RequiredArgsConstructor;\n" //
							+ "\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * A layout with buttons to select a master data page.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s +=
					"@Generated\n" //
							+ "@Route(MasterDataView.URL)\n" //
							+ "@RequiredArgsConstructor\n" //
							+ "public class MasterDataView extends VerticalLayout implements BeforeEnterObserver, HasUrlParameter<String> {\n" //
							+ "\n" //
							+ "	public static final String URL = \"test-ws/masterdata/menu\";\n" //
							+ "\n" //
							+ "	private static final Logger LOG = LogManager.getLogger(MasterDataView.class);\n" //
							+ "\n" //
							+ "	private final ButtonFactory buttonFactory;\n" //
							+ "	private final MasterDataGUIConfiguration guiConfiguration;\n" //
							+ "	private final ResourceManager resourceManager;\n" //
							+ "	private final SessionData session;\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {\n" //
							+ "		LOG.debug(\"setParameter\");\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	@Override\n" //
							+ "	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {\n" //
							+ "		UserAuthorizationChecker.forwardToLoginOnNoUserSetForSession(session, beforeEnterEvent);\n" //
							+ "		LOG.info(\"created\");\n" //
							+ "		setMargin(false);\n" //
							+ "		setWidthFull();\n" //
							+ "		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n" //
							+ "		getStyle().set(\"background-size\", \"cover\");\n" //
							+ "		Button buttonMasterDataTableWithEnumType =\n" //
							+ "				buttonFactory\n" //
							+ "						.createButton(\n" //
							+ "								resourceManager\n" //
							+ "										.getLocalizedString(\n" //
							+ "												\"master-data.button.tablewithenumtype.text\",\n" //
							+ "												session.getLocalization()));\n" //
							+ "		buttonMasterDataTableWithEnumType.addClickListener(event -> switchToSourceTableWithEnumType());\n" //
							+ "		buttonMasterDataTableWithEnumType.setWidthFull();\n" //
							+ "		Button buttonMasterDataTableWithGridFields =\n" //
							+ "				buttonFactory\n" //
							+ "						.createButton(\n" //
							+ "								resourceManager\n" //
							+ "										.getLocalizedString(\n" //
							+ "												\"master-data.button.tablewithgridfields.text\",\n" //
							+ "												session.getLocalization()));\n" //
							+ "		buttonMasterDataTableWithGridFields.addClickListener(event -> switchToSourceTableWithGridFields());\n" //
							+ "		buttonMasterDataTableWithGridFields.setWidthFull();\n" //
							+ "		Button buttonMasterDataTableWithNumberField =\n" //
							+ "				buttonFactory\n" //
							+ "						.createButton(\n" //
							+ "								resourceManager\n" //
							+ "										.getLocalizedString(\n" //
							+ "												\"master-data.button.tablewithnumberfield.text\",\n" //
							+ "												session.getLocalization()));\n" //
							+ "		buttonMasterDataTableWithNumberField.addClickListener(event -> switchToSourceTableWithNumberField());\n" //
							+ "		buttonMasterDataTableWithNumberField.setWidthFull();\n" //
							+ "		Button buttonMasterDataTableWithSpecials =\n" //
							+ "				buttonFactory\n" //
							+ "						.createButton(\n" //
							+ "								resourceManager\n" //
							+ "										.getLocalizedString(\n" //
							+ "												\"master-data.button.tablewithspecials.text\",\n" //
							+ "												session.getLocalization()));\n" //
							+ "		buttonMasterDataTableWithSpecials.addClickListener(event -> switchToSourceTableWithSpecials());\n" //
							+ "		buttonMasterDataTableWithSpecials.setWidthFull();\n" //
							+ "		ButtonGrid buttonGrid =\n" //
							+ "				new ButtonGrid(\n" //
							+ "						4,\n" //
							+ "						buttonMasterDataTableWithEnumType,\n" //
							+ "						buttonMasterDataTableWithGridFields,\n" //
							+ "						buttonMasterDataTableWithNumberField,\n" //
							+ "						buttonMasterDataTableWithSpecials\n" //
							+ "				);\n" //
							+ "		buttonGrid.setMargin(false);\n" //
							+ "		buttonGrid.setWidthFull();\n" //
							+ "		add(\n" //
							+ "				new HeaderLayout(\n" //
							+ "						buttonFactory.createBackButton(resourceManager, this::getUI, MainMenuView.URL, session),\n" //
							+ "						buttonFactory.createLogoutButton(resourceManager, this::getUI, session, LOG),\n" //
							+ "						resourceManager.getLocalizedString(\"master-data.header.menu.label\", session.getLocalization()),\n" //
							+ "						HeaderLayoutMode.PLAIN),\n" //
							+ "				buttonGrid);\n" //
							+ "		LOG.info(\"main menu view opened for user '{}'.\", session.getUserName());\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private void switchToSourceTableWithEnumType() {\n" //
							+ "		getUI().ifPresent(ui -> ui.navigate(TableWithEnumTypePageView.URL));\n" //
							+ "	}\n" //
							+ "	private void switchToSourceTableWithGridFields() {\n" //
							+ "		getUI().ifPresent(ui -> ui.navigate(TableWithGridFieldsPageView.URL));\n" //
							+ "	}\n" //
							+ "	private void switchToSourceTableWithNumberField() {\n" //
							+ "		getUI().ifPresent(ui -> ui.navigate(TableWithNumberFieldPageView.URL));\n" //
							+ "	}\n" //
							+ "	private void switchToSourceTableWithSpecials() {\n" //
							+ "		getUI().ifPresent(ui -> ui.navigate(TableWithSpecialsPageView.URL));\n" //
							+ "	}\n" //
							+ "\n" //
							+ "}";
			return s;
		}

		@Test
		void happyRunForASimpleObjectWithSuppressedComments() {
			// Prepare
			String expected = getExpected(null, "gui.vaadin.masterdata", true, "null");
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "off"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, null);
			// Check
			assertEquals(expected, returned);
		}

	}

}
