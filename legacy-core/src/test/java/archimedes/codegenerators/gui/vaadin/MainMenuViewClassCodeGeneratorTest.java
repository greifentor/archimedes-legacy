package archimedes.codegenerators.gui.vaadin;

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
public class MainMenuViewClassCodeGeneratorTest {
	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private MainMenuViewClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected(null, "gui.vaadin", false, "null");
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
							+ "import com.vaadin.flow.component.orderedlayout.Scroller;\n" //
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
							+ "import base.pack.age.name.gui.vaadin.component.Button;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.ButtonGrid;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" //
							+ "import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" //
							+ "import base.pack.age.name.gui.vaadin.GUIConfiguration;\n" //
							+ "import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" //
							+ "import base.pack.age.name.gui.vaadin.masterdata.MasterDataView;\n" //
							+ "\n" //
							+ "import lombok.Generated;\n" //
							+ "import lombok.RequiredArgsConstructor;\n" //
							+ "\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * A main menu view for the application.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s +=
					"@Generated\n" //
							+ "@Route(MainMenuView.URL)\n" //
							+ "@RequiredArgsConstructor\n" //
							+ "public class MainMenuView extends Scroller implements BeforeEnterObserver, HasUrlParameter<String> {\n" //
							+ "\n" //
							+ "	public static final String URL = \"test-ws/menu\";\n" //
							+ "\n" //
							+ "	private static final Logger LOG = LogManager.getLogger(MainMenuView.class);\n" //
							+ "\n" //
							+ "	private final ButtonFactory buttonFactory;\n" //
							+ "	private final GUIConfiguration guiConfiguration;\n" //
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
							+ "		setWidthFull();\n" //
							+ "		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getMainMenuBackgroundFileName() + \"')\");\n" //
							+ "		getStyle().set(\"background-size\", \"cover\");\n" //
							+ "		getStyle().set(\"background-attachment\", \"fixed\");\n" //
							+ "		Button buttonMasterData =\n" //
							+ "				buttonFactory\n" //
							+ "						.createButton(\n" //
							+ "								resourceManager\n" //
							+ "										.getLocalizedString(\n" //
							+ "												\"main-menu.button.master-data.text\",\n" //
							+ "												session.getLocalization()));\n" //
							+ "		buttonMasterData.addClickListener(event -> switchToMasterData());\n" //
							+ "		buttonMasterData.setWidthFull();\n" //
							+ "		ButtonGrid buttonGridMasterData = new ButtonGrid(4, buttonMasterData);\n" //
							+ "		buttonGridMasterData.setMargin(false);\n" //
							+ "		buttonGridMasterData.setWidthFull();\n" //
							+ "		mainLayout.add(\n" //
							+ "				new HeaderLayout(\n" //
							+ "						buttonFactory.createLogoutButton(resourceManager, this::getUI, session, LOG),\n" //
							+ "						resourceManager.getLocalizedString(\"commons.header.main-menu.label\", session.getLocalization()),\n" //
							+ "						HeaderLayoutMode.PLAIN),\n" //
							+ "				buttonGridMasterData);\n" //
							+ "		setContent(mainLayout);\n" //
							+ "		LOG.info(\"main menu view opened for user '{}'.\", session.getUserName());\n" //
							+ "	}\n" //
							+ "\n" //
							+ "	private void switchToMasterData() {\n" //
							+ "		getUI().ifPresent(ui -> ui.navigate(MasterDataView.URL));\n" //
							+ "	}\n" //
							+ "\n" //
							+ "}";
			return s;
		}

		@Test
		void happyRunForASimpleObjectWithSuppressedComments() {
			// Prepare
			String expected = getExpected(null, "gui.vaadin", true, "null");
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "off"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, null);
			// Check
			assertEquals(expected, returned);
		}

	}

}
