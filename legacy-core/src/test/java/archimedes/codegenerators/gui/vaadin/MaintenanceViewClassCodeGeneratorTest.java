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
public class MaintenanceViewClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private MaintenanceViewClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class SimpleTable {

		private String getExpected() {
			return "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
					"\n" + //
					"import org.apache.logging.log4j.LogManager;\n" + //
					"import org.apache.logging.log4j.Logger;\n" + //
					"import org.springframework.beans.factory.annotation.Autowired;\n" + //
					"\n" + //
					"import com.vaadin.flow.component.AttachEvent;\n" + //
					"import com.vaadin.flow.component.DetachEvent;\n" + //
					"import com.vaadin.flow.component.dependency.CssImport;\n" + //
					"import com.vaadin.flow.router.BeforeEnterEvent;\n" + //
					"import com.vaadin.flow.router.BeforeEvent;\n" + //
					"import com.vaadin.flow.router.Route;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.ATable;\n" + //
					"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
					"import base.pack.age.name.gui.SessionData;\n" + //
					"import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" + //
					"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"/**\n" + //
					" * A dialog to edit ATable details.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Route(ATableMaintenanceView.URL)\n" + //
					"@CssImport(\"./styles/shared-styles.css\")\n" + //
					"@CssImport(value = \"./styles/vaadin-text-field-styles.css\", themeFor = \"vaadin-text-field\")\n"
					+ //
					"@CssImport(value = \"./styles/vaadin-text-area-styles.css\", themeFor = \"vaadin-text-area\")\n" + //
					"@CssImport(value = \"./styles/vaadin-combo-box-styles.css\", themeFor = \"vaadin-combo-box\")\n" + //
					"@CssImport(value = \"./styles/vaadin-checkbox-styles.css\", themeFor = \"vaadin-checkbox\")\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class ATableMaintenanceView extends AbstractMasterDataBaseLayout implements ATableDetailsLayout.Observer {\n"
					+ //
					"\n" + //
					"	public static final String URL = \"test-ws/masterdata/atabellen/details\";\n" + //
					"\n" + //
					"	private static final Logger logger = LogManager.getLogger(ATableMaintenanceView.class);\n" + //
					"\n" + //
					"	@Autowired(required = false)\n" + //
					"	private MaintenanceViewRenderer<ATable> maintenanceViewRenderer;\n" + //
					"	@Autowired(required = false)\n" + //
					"	private DetailsLayoutComboBoxItemLabelGenerator<ATable> comboBoxItemLabelGenerator;\n" + //
					"\n" + //
					"	private final ButtonFactory buttonFactory;\n" + //
					"	private final ComponentFactory componentFactory;\n" + //
					"	private final ResourceManager resourceManager;\n" + //
					"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
					"	private final ServiceProvider serviceProvider;\n" + //
					"	private final SessionData session;\n" + //
					"\n" + //
					"	private ATable model;\n" + //
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
					"	public void doSetParameter(BeforeEvent event) {\n" + //
					"		long id = parametersMap.containsKey(\"id\") && (parametersMap.get(\"id\").size() > 0)\n" + //
					"				? Long.parseLong(parametersMap.get(\"id\").get(0))\n" + //
					"				: -1;\n" + //
					"		model = serviceProvider.getATableService().findById(id).orElse(createNewModel());\n" + //
					"		if (parametersMap.containsKey(\"duplicate\") && \"true\".equals(parametersMap.get(\"duplicate\").get(0))) {\n"
					+ //
					"			model.setId(-1);\n" + //
					"		}\n" + //
					"	}\n" + //
					"\n" + //
					"	private ATable createNewModel() {\n" + //
					"		return new ATable();\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void doBeforeEnter(BeforeEnterEvent beforeEnterEvent) {\n" + //
					"		UserAuthorizationChecker.forwardToLoginOnNoUserSetForSession(getSessionData(), beforeEnterEvent);\n"
					+ //
					"		setMargin(false);\n" + //
					"		setSizeFull();\n" + //
					"		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n"
					+ //
					"		getStyle().set(\"background-size\", \"cover\");\n" + //
					"		getStyle().set(\"background-attachment\", \"fixed\");\n" + //
					"		add(\n" + //
					"				new HeaderLayout(\n" + //
					"						buttonFactory\n" + //
					"										.createBackButton(\n" + //
					"												resourceManager,\n" + //
					"												this::getUI,\n" + //
					"												ATablePageView.URL,\n" + //
					"												session),\n" + //
					"						buttonFactory.createLogoutButton(resourceManager, this::getUI, session, logger),\n"
					+ //
					"								resourceManager.getLocalizedString(\"ATableMaintenanceView.header.prefix.label\", session.getLocalization()) + getHeaderSuffix(model),\n"
					+ //
					"								HeaderLayoutMode.PLAIN),\n" + //
					"				getDetailsLayout(model));\n" + //
					"	}\n" + //
					"\n" + //
					"	private String getHeaderSuffix(ATable model) {\n" + //
					"		return maintenanceViewRenderer != null\n" + //
					"				? maintenanceViewRenderer.getHeaderSuffix(model)\n" + //
					"				: \"\" + model.getDescription();\n" + //
					"	}\n" + //
					"\n" + //
					"	private AbstractMasterDataBaseLayout getDetailsLayout(ATable model) {\n" + //
					"		return new ATableDetailsLayout(buttonFactory, componentFactory, model, serviceProvider, guiConfiguration, resourceManager, session, this, comboBoxItemLabelGenerator);\n"
					+ //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	protected void onAttach(AttachEvent attachEvent) {\n" + //
					"		logger.info(\"onAttach\");\n" + //
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
					"	@Override\n" + //
					"	public void save(Object model) {\n" + //
					"		getUI().ifPresent(ui -> ui.navigate(ATablePageView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void save() {\n" + //
					"		save(model);\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void remove() {\n" + //
					"		serviceProvider.getATableService().delete(model);\n" + //
					"		getUI().ifPresent(ui -> ui.navigate(ATablePageView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"}";
		}

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected();
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class ForeignKey {

		private String getExpected() {
			return "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
					"\n" + //
					"import org.apache.logging.log4j.LogManager;\n" + //
					"import org.apache.logging.log4j.Logger;\n" + //
					"import org.springframework.beans.factory.annotation.Autowired;\n" + //
					"\n" + //
					"import com.vaadin.flow.component.AttachEvent;\n" + //
					"import com.vaadin.flow.component.DetachEvent;\n" + //
					"import com.vaadin.flow.component.dependency.CssImport;\n" + //
					"import com.vaadin.flow.router.BeforeEnterEvent;\n" + //
					"import com.vaadin.flow.router.BeforeEvent;\n" + //
					"import com.vaadin.flow.router.Route;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.ATable;\n" + //
					"import base.pack.age.name.core.service.AnotherTableService;\n" + //
					"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
					"import base.pack.age.name.gui.SessionData;\n" + //
					"import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" + //
					"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"/**\n" + //
					" * A dialog to edit ATable details.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Route(ATableMaintenanceView.URL)\n" + //
					"@CssImport(\"./styles/shared-styles.css\")\n" + //
					"@CssImport(value = \"./styles/vaadin-text-field-styles.css\", themeFor = \"vaadin-text-field\")\n"
					+ //
					"@CssImport(value = \"./styles/vaadin-text-area-styles.css\", themeFor = \"vaadin-text-area\")\n" + //
					"@CssImport(value = \"./styles/vaadin-combo-box-styles.css\", themeFor = \"vaadin-combo-box\")\n" + //
					"@CssImport(value = \"./styles/vaadin-checkbox-styles.css\", themeFor = \"vaadin-checkbox\")\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class ATableMaintenanceView extends AbstractMasterDataBaseLayout implements ATableDetailsLayout.Observer {\n"
					+ //
					"\n" + //
					"	public static final String URL = \"test-project/masterdata/atabellen/details\";\n" + //
					"\n" + //
					"	private static final Logger logger = LogManager.getLogger(ATableMaintenanceView.class);\n" + //
					"\n" + //
					"	@Autowired(required = false)\n" + //
					"	private MaintenanceViewRenderer<ATable> maintenanceViewRenderer;\n" + //
					"	@Autowired(required = false)\n" + //
					"	private DetailsLayoutComboBoxItemLabelGenerator<ATable> comboBoxItemLabelGenerator;\n" + //
					"\n" + //
					"	private final ButtonFactory buttonFactory;\n" + //
					"	private final ComponentFactory componentFactory;\n" + //
					"	private final ResourceManager resourceManager;\n" + //
					"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
					"	private final ServiceProvider serviceProvider;\n" + //
					"	private final SessionData session;\n" + //
					"\n" + //
					"	private ATable model;\n" + //
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
					"	public void doSetParameter(BeforeEvent event) {\n" + //
					"		long id = parametersMap.containsKey(\"id\") && (parametersMap.get(\"id\").size() > 0)\n" + //
					"				? Long.parseLong(parametersMap.get(\"id\").get(0))\n" + //
					"				: -1;\n" + //
					"		model = serviceProvider.getATableService().findById(id).orElse(createNewModel());\n" + //
					"		if (parametersMap.containsKey(\"duplicate\") && \"true\".equals(parametersMap.get(\"duplicate\").get(0))) {\n"
					+ //
					"			model.setId(-1);\n" + //
					"		}\n" + //
					"	}\n" + //
					"\n" + //
					"	private ATable createNewModel() {\n" + //
					"		return new ATable();\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void doBeforeEnter(BeforeEnterEvent beforeEnterEvent) {\n" + //
					"		UserAuthorizationChecker.forwardToLoginOnNoUserSetForSession(getSessionData(), beforeEnterEvent);\n"
					+ //
					"		setMargin(false);\n" + //
					"		setSizeFull();\n" + //
					"		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n"
					+ //
					"		getStyle().set(\"background-size\", \"cover\");\n" + //
					"		getStyle().set(\"background-attachment\", \"fixed\");\n" + //
					"		add(\n" + //
					"				new HeaderLayout(\n" + //
					"						buttonFactory\n" + //
					"										.createBackButton(\n" + //
					"												resourceManager,\n" + //
					"												this::getUI,\n" + //
					"												ATablePageView.URL,\n" + //
					"												session),\n" + //
					"						buttonFactory.createLogoutButton(resourceManager, this::getUI, session, logger),\n"
					+ //
					"								resourceManager.getLocalizedString(\"ATableMaintenanceView.header.prefix.label\", session.getLocalization()) + getHeaderSuffix(model),\n"
					+ //
					"								HeaderLayoutMode.PLAIN),\n" + //
					"				getDetailsLayout(model));\n" + //
					"	}\n" + //
					"\n" + //
					"	private String getHeaderSuffix(ATable model) {\n" + //
					"		return maintenanceViewRenderer != null\n" + //
					"				? maintenanceViewRenderer.getHeaderSuffix(model)\n" + //
					"				: \"\" + model.getRef();\n" + //
					"	}\n" + //
					"\n" + //
					"	private AbstractMasterDataBaseLayout getDetailsLayout(ATable model) {\n" + //
					"		return new ATableDetailsLayout(buttonFactory, componentFactory, model, serviceProvider, guiConfiguration, resourceManager, session, this, comboBoxItemLabelGenerator);\n"
					+ //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	protected void onAttach(AttachEvent attachEvent) {\n" + //
					"		logger.info(\"onAttach\");\n" + //
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
					"	@Override\n" + //
					"	public void save(Object model) {\n" + //
					"		getUI().ifPresent(ui -> ui.navigate(ATablePageView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void save() {\n" + //
					"		save(model);\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void remove() {\n" + //
					"		serviceProvider.getATableService().delete(model);\n" + //
					"		getUI().ifPresent(ui -> ui.navigate(ATablePageView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"}";
		}

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected();
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class Inheritance {

		private String getExpected() {
			return "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
					"\n" + //
					"import org.apache.logging.log4j.LogManager;\n" + //
					"import org.apache.logging.log4j.Logger;\n" + //
					"import org.springframework.beans.factory.annotation.Autowired;\n" + //
					"\n" + //
					"import com.vaadin.flow.component.AttachEvent;\n" + //
					"import com.vaadin.flow.component.DetachEvent;\n" + //
					"import com.vaadin.flow.component.dependency.CssImport;\n" + //
					"import com.vaadin.flow.router.BeforeEnterEvent;\n" + //
					"import com.vaadin.flow.router.BeforeEvent;\n" + //
					"import com.vaadin.flow.router.Route;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.AnotherHeirTable;\n" + //
					"import base.pack.age.name.core.model.AnotherHeirTableWithSameReference;\n" + //
					"import base.pack.age.name.core.model.AnotherTable;\n" + //
					"import base.pack.age.name.core.model.ATable;\n" + //
					"import base.pack.age.name.core.model.HeirTableWithReference;\n" + //
					"import base.pack.age.name.core.service.ReferencedTableService;\n" + //
					"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
					"import base.pack.age.name.gui.SessionData;\n" + //
					"import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" + //
					"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n"
					+ //
					"import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"/**\n" + //
					" * A dialog to edit ATable details.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Route(ATableMaintenanceView.URL)\n" + //
					"@CssImport(\"./styles/shared-styles.css\")\n" + //
					"@CssImport(value = \"./styles/vaadin-text-field-styles.css\", themeFor = \"vaadin-text-field\")\n"
					+ //
					"@CssImport(value = \"./styles/vaadin-text-area-styles.css\", themeFor = \"vaadin-text-area\")\n" + //
					"@CssImport(value = \"./styles/vaadin-combo-box-styles.css\", themeFor = \"vaadin-combo-box\")\n" + //
					"@CssImport(value = \"./styles/vaadin-checkbox-styles.css\", themeFor = \"vaadin-checkbox\")\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class ATableMaintenanceView extends AbstractMasterDataBaseLayout implements ATableDetailsLayout.Observer {\n"
					+ //
					"\n" + //
					"	public static final String URL = \"test-project/masterdata/atabellen/details\";\n" + //
					"\n" + //
					"	private static final Logger logger = LogManager.getLogger(ATableMaintenanceView.class);\n" + //
					"\n" + //
					"	@Autowired(required = false)\n" + //
					"	private MaintenanceViewRenderer<ATable> maintenanceViewRenderer;\n" + //
					"	@Autowired(required = false)\n" + //
					"	private DetailsLayoutComboBoxItemLabelGenerator<ATable> comboBoxItemLabelGenerator;\n" + //
					"\n" + //
					"	private final ButtonFactory buttonFactory;\n" + //
					"	private final ComponentFactory componentFactory;\n" + //
					"	private final ResourceManager resourceManager;\n" + //
					"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
					"	private final ServiceProvider serviceProvider;\n" + //
					"	private final SessionData session;\n" + //
					"\n" + //
					"	private ATable model;\n" + //
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
					"	public void doSetParameter(BeforeEvent event) {\n" + //
					"		long id = parametersMap.containsKey(\"id\") && (parametersMap.get(\"id\").size() > 0)\n" + //
					"				? Long.parseLong(parametersMap.get(\"id\").get(0))\n" + //
					"				: -1;\n" + //
					"		model = serviceProvider.getATableService().findById(id).orElse(createNewModel());\n" + //
					"		if (parametersMap.containsKey(\"duplicate\") && \"true\".equals(parametersMap.get(\"duplicate\").get(0))) {\n"
					+ //
					"			model.setId(-1);\n" + //
					"		}\n" + //
					"	}\n" + //
					"\n" + //
					"	private ATable createNewModel() {\n" + //
					"		String modelClassName = parametersMap.containsKey(\"modelClass\") && (parametersMap.get(\"modelClass\").size() > 0)\n"
					+ //
					"				? parametersMap.get(\"modelClass\").get(0)\n" + //
					"				: \"ATable\";\n" + //
					"		if (modelClassName.equals(\"AnotherHeirTable\")) {\n" + //
					"			return new AnotherHeirTable();\n" + //
					"		}\n" + //
					"		if (modelClassName.equals(\"AnotherHeirTableWithSameReference\")) {\n" + //
					"			return new AnotherHeirTableWithSameReference();\n" + //
					"		}\n" + //
					"		if (modelClassName.equals(\"AnotherTable\")) {\n" + //
					"			return new AnotherTable();\n" + //
					"		}\n" + //
					"		if (modelClassName.equals(\"HeirTableWithReference\")) {\n" + //
					"			return new HeirTableWithReference();\n" + //
					"		}\n" + //
					"		return new ATable();\n" + //
					"	}\n" + //
					"\n" + // + //
					"	@Override\n" + //
					"	public void doBeforeEnter(BeforeEnterEvent beforeEnterEvent) {\n" + //
					"		UserAuthorizationChecker.forwardToLoginOnNoUserSetForSession(getSessionData(), beforeEnterEvent);\n"
					+ //
					"		setMargin(false);\n" + //
					"		setSizeFull();\n" + //
					"		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n"
					+ //
					"		getStyle().set(\"background-size\", \"cover\");\n" + //
					"		getStyle().set(\"background-attachment\", \"fixed\");\n" + //
					"		add(\n" + //
					"				new HeaderLayout(\n" + //
					"						buttonFactory\n" + //
					"										.createBackButton(\n" + //
					"												resourceManager,\n" + //
					"												this::getUI,\n" + //
					"												ATablePageView.URL,\n" + //
					"												session),\n" + //
					"						buttonFactory.createLogoutButton(resourceManager, this::getUI, session, logger),\n"
					+ //
					"								resourceManager.getLocalizedString(\"ATableMaintenanceView.header.prefix.label\", session.getLocalization()) + getHeaderSuffix(model),\n"
					+ //
					"								HeaderLayoutMode.PLAIN),\n" + //
					"				getDetailsLayout(model));\n" + //
					"	}\n" + //
					"\n" + //
					"	private String getHeaderSuffix(ATable model) {\n" + //
					"		return maintenanceViewRenderer != null\n" + //
					"				? maintenanceViewRenderer.getHeaderSuffix(model)\n" + //
					"				: \"\" + model.getDescription();\n" + //
					"	}\n" + //
					"\n" + //
					"	private AbstractMasterDataBaseLayout getDetailsLayout(ATable model) {\n" + //
					"		if (model.getClass() == AnotherHeirTable.class) {\n" + //
					"			return new AnotherHeirTableDetailsLayout(\n" + //
					"					buttonFactory,\n" + //
					"					componentFactory,\n" + //
					"					(AnotherHeirTable) model,\n" + //
					"					serviceProvider,\n" + //
					"					guiConfiguration,\n" + //
					"					resourceManager,\n" + //
					"					session,\n" + //
					"					this,\n" + //
					"					comboBoxItemLabelGenerator);\n" + //
					"		}\n" + //
					"		if (model.getClass() == AnotherHeirTableWithSameReference.class) {\n" + //
					"			return new AnotherHeirTableWithSameReferenceDetailsLayout(\n" + //
					"					buttonFactory,\n" + //
					"					componentFactory,\n" + //
					"					(AnotherHeirTableWithSameReference) model,\n" + //
					"					serviceProvider,\n" + //
					"					guiConfiguration,\n" + //
					"					resourceManager,\n" + //
					"					session,\n" + //
					"					this,\n" + //
					"					comboBoxItemLabelGenerator);\n" + //
					"		}\n" + //
					"		if (model.getClass() == AnotherTable.class) {\n" + //
					"			return new AnotherTableDetailsLayout(\n" + //
					"					buttonFactory,\n" + //
					"					componentFactory,\n" + //
					"					(AnotherTable) model,\n" + //
					"					serviceProvider,\n" + //
					"					guiConfiguration,\n" + //
					"					resourceManager,\n" + //
					"					session,\n" + //
					"					this,\n" + //
					"					comboBoxItemLabelGenerator);\n" + //
					"		}\n" + //
					"		if (model.getClass() == HeirTableWithReference.class) {\n" + //
					"			return new HeirTableWithReferenceDetailsLayout(\n" + //
					"					buttonFactory,\n" + //
					"					componentFactory,\n" + //
					"					(HeirTableWithReference) model,\n" + //
					"					serviceProvider,\n" + //
					"					guiConfiguration,\n" + //
					"					resourceManager,\n" + //
					"					session,\n" + //
					"					this,\n" + //
					"					comboBoxItemLabelGenerator);\n" + //
					"		}\n" + //
					"		return new ATableDetailsLayout(buttonFactory, componentFactory, model, serviceProvider, guiConfiguration, resourceManager, session, this, comboBoxItemLabelGenerator);\n"
					+ //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	protected void onAttach(AttachEvent attachEvent) {\n" + //
					"		logger.info(\"onAttach\");\n" + //
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
					"	@Override\n" + //
					"	public void save(Object model) {\n" + //
					"		getUI().ifPresent(ui -> ui.navigate(ATablePageView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void save() {\n" + //
					"		save(model);\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void remove() {\n" + //
					"		serviceProvider.getATableService().delete(model);\n" + //
					"		getUI().ifPresent(ui -> ui.navigate(ATablePageView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"}";
		}

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected();
			DataModel dataModel = readDataModel("Model-Inheritance.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void differentSubClassReferences() {
			// Prepare
			String expected = "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
					"\n" + //
					"import org.apache.logging.log4j.LogManager;\n" + //
					"import org.apache.logging.log4j.Logger;\n" + //
					"import org.springframework.beans.factory.annotation.Autowired;\n" + //
					"\n" + //
					"import com.vaadin.flow.component.AttachEvent;\n" + //
					"import com.vaadin.flow.component.DetachEvent;\n" + //
					"import com.vaadin.flow.component.dependency.CssImport;\n" + //
					"import com.vaadin.flow.router.BeforeEnterEvent;\n" + //
					"import com.vaadin.flow.router.BeforeEvent;\n" + //
					"import com.vaadin.flow.router.Route;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.DifferentSubclassReferences;\n" + //
					"import base.pack.age.name.core.service.ATableService;\n" + //
					"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
					"import base.pack.age.name.gui.SessionData;\n" + //
					"import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" + //
					"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"/**\n" + //
					" * A dialog to edit DifferentSubclassReferences details.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Route(DifferentSubclassReferencesMaintenanceView.URL)\n" + //
					"@CssImport(\"./styles/shared-styles.css\")\n" + //
					"@CssImport(value = \"./styles/vaadin-text-field-styles.css\", themeFor = \"vaadin-text-field\")\n"
					+ //
					"@CssImport(value = \"./styles/vaadin-text-area-styles.css\", themeFor = \"vaadin-text-area\")\n" + //
					"@CssImport(value = \"./styles/vaadin-combo-box-styles.css\", themeFor = \"vaadin-combo-box\")\n" + //
					"@CssImport(value = \"./styles/vaadin-checkbox-styles.css\", themeFor = \"vaadin-checkbox\")\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class DifferentSubclassReferencesMaintenanceView extends AbstractMasterDataBaseLayout implements DifferentSubclassReferencesDetailsLayout.Observer {\n"
					+ //
					"\n" + //
					"	public static final String URL = \"test-project/masterdata/different_subclass_referencess/details\";\n"
					+ //
					"\n" + //
					"	private static final Logger logger = LogManager.getLogger(DifferentSubclassReferencesMaintenanceView.class);\n"
					+ //
					"\n" + //
					"	@Autowired(required = false)\n" + //
					"	private MaintenanceViewRenderer<DifferentSubclassReferences> maintenanceViewRenderer;\n" + //
					"	@Autowired(required = false)\n" + //
					"	private DetailsLayoutComboBoxItemLabelGenerator<DifferentSubclassReferences> comboBoxItemLabelGenerator;\n"
					+ //
					"\n" + //
					"	private final ButtonFactory buttonFactory;\n" + //
					"	private final ComponentFactory componentFactory;\n" + //
					"	private final ResourceManager resourceManager;\n" + //
					"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
					"	private final ServiceProvider serviceProvider;\n" + //
					"	private final SessionData session;\n" + //
					"\n" + //
					"	private DifferentSubclassReferences model;\n" + //
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
					"	public void doSetParameter(BeforeEvent event) {\n" + //
					"		long id = parametersMap.containsKey(\"id\") && (parametersMap.get(\"id\").size() > 0)\n" + //
					"				? Long.parseLong(parametersMap.get(\"id\").get(0))\n" + //
					"				: -1;\n" + //
					"		model = serviceProvider.getDifferentSubclassReferencesService().findById(id).orElse(createNewModel());\n"
					+ //
					"		if (parametersMap.containsKey(\"duplicate\") && \"true\".equals(parametersMap.get(\"duplicate\").get(0))) {\n"
					+ //
					"			model.setId(-1);\n" + //
					"		}\n" + //
					"	}\n" + //
					"\n" + //
					"	private DifferentSubclassReferences createNewModel() {\n" + //
					"		return new DifferentSubclassReferences();\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void doBeforeEnter(BeforeEnterEvent beforeEnterEvent) {\n" + //
					"		UserAuthorizationChecker.forwardToLoginOnNoUserSetForSession(getSessionData(), beforeEnterEvent);\n"
					+ //
					"		setMargin(false);\n" + //
					"		setSizeFull();\n" + //
					"		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n"
					+ //
					"		getStyle().set(\"background-size\", \"cover\");\n" + //
					"		getStyle().set(\"background-attachment\", \"fixed\");\n" + //
					"		add(\n" + //
					"				new HeaderLayout(\n" + //
					"						buttonFactory\n" + //
					"										.createBackButton(\n" + //
					"												resourceManager,\n" + //
					"												this::getUI,\n" + //
					"												DifferentSubclassReferencesPageView.URL,\n" + //
					"												session),\n" + //
					"						buttonFactory.createLogoutButton(resourceManager, this::getUI, session, logger),\n"
					+ //
					"								resourceManager.getLocalizedString(\"DifferentSubclassReferencesMaintenanceView.header.prefix.label\", session.getLocalization()) + getHeaderSuffix(model),\n"
					+ //
					"								HeaderLayoutMode.PLAIN),\n" + //
					"				getDetailsLayout(model));\n" + //
					"	}\n" + //
					"\n" + //
					"	private String getHeaderSuffix(DifferentSubclassReferences model) {\n" + //
					"		return maintenanceViewRenderer != null\n" + //
					"				? maintenanceViewRenderer.getHeaderSuffix(model)\n" + //
					"				: \"\" + model.getId();\n" + //
					"	}\n" + //
					"\n" + //
					"	private AbstractMasterDataBaseLayout getDetailsLayout(DifferentSubclassReferences model) {\n" + //
					"		return new DifferentSubclassReferencesDetailsLayout(buttonFactory, componentFactory, model, serviceProvider, guiConfiguration, resourceManager, session, this, comboBoxItemLabelGenerator);\n"
					+ //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	protected void onAttach(AttachEvent attachEvent) {\n" + //
					"		logger.info(\"onAttach\");\n" + //
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
					"	@Override\n" + //
					"	public void save(Object model) {\n" + //
					"		getUI().ifPresent(ui -> ui.navigate(DifferentSubclassReferencesPageView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void save() {\n" + //
					"		save(model);\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void remove() {\n" + //
					"		serviceProvider.getDifferentSubclassReferencesService().delete(model);\n" + //
					"		getUI().ifPresent(ui -> ui.navigate(DifferentSubclassReferencesPageView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model-Inheritance.xml");
			// Run
			String returned =
					unitUnderTest
							.generate(
									BASE_PACKAGE_NAME,
									dataModel,
									dataModel.getTableByName("DIFFERENT_SUBCLASS_REFERENCES"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void superClassReferences() {
			// Prepare
			String expected = "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
					"\n" + //
					"import org.apache.logging.log4j.LogManager;\n" + //
					"import org.apache.logging.log4j.Logger;\n" + //
					"import org.springframework.beans.factory.annotation.Autowired;\n" + //
					"\n" + //
					"import com.vaadin.flow.component.AttachEvent;\n" + //
					"import com.vaadin.flow.component.DetachEvent;\n" + //
					"import com.vaadin.flow.component.dependency.CssImport;\n" + //
					"import com.vaadin.flow.router.BeforeEnterEvent;\n" + //
					"import com.vaadin.flow.router.BeforeEvent;\n" + //
					"import com.vaadin.flow.router.Route;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.BHeirHeirTable;\n" + //
					"import base.pack.age.name.core.model.BHeirTable;\n" + //
					"import base.pack.age.name.core.model.BTable;\n" + //
					"import base.pack.age.name.core.service.BReferencedTableService;\n" + //
					"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
					"import base.pack.age.name.gui.SessionData;\n" + //
					"import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" + //
					"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"/**\n" + //
					" * A dialog to edit BTable details.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Route(BTableMaintenanceView.URL)\n" + //
					"@CssImport(\"./styles/shared-styles.css\")\n" + //
					"@CssImport(value = \"./styles/vaadin-text-field-styles.css\", themeFor = \"vaadin-text-field\")\n"
					+ //
					"@CssImport(value = \"./styles/vaadin-text-area-styles.css\", themeFor = \"vaadin-text-area\")\n" + //
					"@CssImport(value = \"./styles/vaadin-combo-box-styles.css\", themeFor = \"vaadin-combo-box\")\n" + //
					"@CssImport(value = \"./styles/vaadin-checkbox-styles.css\", themeFor = \"vaadin-checkbox\")\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class BTableMaintenanceView extends AbstractMasterDataBaseLayout implements BTableDetailsLayout.Observer {\n"
					+ //
					"\n" + //
					"	public static final String URL = \"test-project/masterdata/atabellen/details\";\n" + //
					"\n" + //
					"	private static final Logger logger = LogManager.getLogger(BTableMaintenanceView.class);\n" + //
					"\n" + //
					"	@Autowired(required = false)\n" + //
					"	private MaintenanceViewRenderer<BTable> maintenanceViewRenderer;\n" + //
					"	@Autowired(required = false)\n" + //
					"	private DetailsLayoutComboBoxItemLabelGenerator<BTable> comboBoxItemLabelGenerator;\n" + //
					"\n" + //
					"	private final ButtonFactory buttonFactory;\n" + //
					"	private final ComponentFactory componentFactory;\n" + //
					"	private final ResourceManager resourceManager;\n" + //
					"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
					"	private final ServiceProvider serviceProvider;\n" + //
					"	private final SessionData session;\n" + //
					"\n" + //
					"	private BTable model;\n" + //
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
					"	public void doSetParameter(BeforeEvent event) {\n" + //
					"		long id = parametersMap.containsKey(\"id\") && (parametersMap.get(\"id\").size() > 0)\n" + //
					"				? Long.parseLong(parametersMap.get(\"id\").get(0))\n" + //
					"				: -1;\n" + //
					"		model = serviceProvider.getBTableService().findById(id).orElse(createNewModel());\n" + //
					"		if (parametersMap.containsKey(\"duplicate\") && \"true\".equals(parametersMap.get(\"duplicate\").get(0))) {\n"
					+ //
					"			model.setId(-1);\n" + //
					"		}\n" + //
					"	}\n" + //
					"\n" + //
					"	private BTable createNewModel() {\n" + //
					"		String modelClassName = parametersMap.containsKey(\"modelClass\") && (parametersMap.get(\"modelClass\").size() > 0)\n"
					+ //
					"				? parametersMap.get(\"modelClass\").get(0)\n" + //
					"				: \"BTable\";\n" + //
					"		if (modelClassName.equals(\"BHeirTable\")) {\n" + //
					"			return new BHeirTable();\n" + //
					"		}\n" + //
					"		if (modelClassName.equals(\"BHeirHeirTable\")) {\n" + //
					"			return new BHeirHeirTable();\n" + //
					"		}\n" + //
					"		return new BTable();\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void doBeforeEnter(BeforeEnterEvent beforeEnterEvent) {\n" + //
					"		UserAuthorizationChecker.forwardToLoginOnNoUserSetForSession(getSessionData(), beforeEnterEvent);\n"
					+ //
					"		setMargin(false);\n" + //
					"		setSizeFull();\n" + //
					"		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n"
					+ //
					"		getStyle().set(\"background-size\", \"cover\");\n" + //
					"		getStyle().set(\"background-attachment\", \"fixed\");\n" + //
					"		add(\n" + //
					"				new HeaderLayout(\n" + //
					"						buttonFactory\n" + //
					"										.createBackButton(\n" + //
					"												resourceManager,\n" + //
					"												this::getUI,\n" + //
					"												BTablePageView.URL,\n" + //
					"												session),\n" + //
					"						buttonFactory.createLogoutButton(resourceManager, this::getUI, session, logger),\n"
					+ //
					"								resourceManager.getLocalizedString(\"BTableMaintenanceView.header.prefix.label\", session.getLocalization()) + getHeaderSuffix(model),\n"
					+ //
					"								HeaderLayoutMode.PLAIN),\n" + //
					"				getDetailsLayout(model));\n" + //
					"	}\n" + //
					"\n" + //
					"	private String getHeaderSuffix(BTable model) {\n" + //
					"		return maintenanceViewRenderer != null\n" + //
					"				? maintenanceViewRenderer.getHeaderSuffix(model)\n" + //
					"				: \"\" + model.getDescription();\n" + //
					"	}\n" + //
					"\n" + //
					"	private AbstractMasterDataBaseLayout getDetailsLayout(BTable model) {\n" + //
					"		if (model.getClass() == BHeirTable.class) {\n" + //
					"			return new BHeirTableDetailsLayout(\n" + //
					"					buttonFactory,\n" + //
					"					componentFactory,\n" + //
					"					(BHeirTable) model,\n" + //
					"					serviceProvider,\n" + //
					"					guiConfiguration,\n" + //
					"					resourceManager,\n" + //
					"					session,\n" + //
					"					this,\n" + //
					"					comboBoxItemLabelGenerator);\n" + //
					"		}\n" + //
					"		if (model.getClass() == BHeirHeirTable.class) {\n" + //
					"			return new BHeirHeirTableDetailsLayout(\n" + //
					"					buttonFactory,\n" + //
					"					componentFactory,\n" + //
					"					(BHeirHeirTable) model,\n" + //
					"					serviceProvider,\n" + //
					"					guiConfiguration,\n" + //
					"					resourceManager,\n" + //
					"					session,\n" + //
					"					this,\n" + //
					"					comboBoxItemLabelGenerator);\n" + //
					"		}\n" + //
					"		return new BTableDetailsLayout(buttonFactory, componentFactory, model, serviceProvider, guiConfiguration, resourceManager, session, this, comboBoxItemLabelGenerator);\n"
					+ //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	protected void onAttach(AttachEvent attachEvent) {\n" + //
					"		logger.info(\"onAttach\");\n" + //
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
					"	@Override\n" + //
					"	public void save(Object model) {\n" + //
					"		getUI().ifPresent(ui -> ui.navigate(BTablePageView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void save() {\n" + //
					"		save(model);\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void remove() {\n" + //
					"		serviceProvider.getBTableService().delete(model);\n" + //
					"		getUI().ifPresent(ui -> ui.navigate(BTablePageView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model-Inheritance.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("B_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void subClassReferenced() {
			// Prepare
			String expected = "package base.pack.age.name.gui.vaadin.masterdata;\n" + //
					"\n" + //
					"import org.apache.logging.log4j.LogManager;\n" + //
					"import org.apache.logging.log4j.Logger;\n" + //
					"import org.springframework.beans.factory.annotation.Autowired;\n" + //
					"\n" + //
					"import com.vaadin.flow.component.AttachEvent;\n" + //
					"import com.vaadin.flow.component.DetachEvent;\n" + //
					"import com.vaadin.flow.component.dependency.CssImport;\n" + //
					"import com.vaadin.flow.router.BeforeEnterEvent;\n" + //
					"import com.vaadin.flow.router.BeforeEvent;\n" + //
					"import com.vaadin.flow.router.Route;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.CTable;\n" + //
					"import base.pack.age.name.core.model.CTableSub0;\n" + //
					"import base.pack.age.name.core.model.CTableSub1;\n" + //
					"import base.pack.age.name.core.service.CAnotherTableService;\n" + //
					"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
					"import base.pack.age.name.gui.SessionData;\n" + //
					"import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" + //
					"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ComponentFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ServiceProvider;\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.RequiredArgsConstructor;\n" + //
					"\n" + //
					"/**\n" + //
					" * A dialog to edit CTable details.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Route(CTableMaintenanceView.URL)\n" + //
					"@CssImport(\"./styles/shared-styles.css\")\n" + //
					"@CssImport(value = \"./styles/vaadin-text-field-styles.css\", themeFor = \"vaadin-text-field\")\n" + //
					"@CssImport(value = \"./styles/vaadin-text-area-styles.css\", themeFor = \"vaadin-text-area\")\n" + //
					"@CssImport(value = \"./styles/vaadin-combo-box-styles.css\", themeFor = \"vaadin-combo-box\")\n" + //
					"@CssImport(value = \"./styles/vaadin-checkbox-styles.css\", themeFor = \"vaadin-checkbox\")\n" + //
					"@RequiredArgsConstructor\n" + //
					"public class CTableMaintenanceView extends AbstractMasterDataBaseLayout implements CTableDetailsLayout.Observer {\n" + //
					"\n" + //
					"	public static final String URL = \"test-project/masterdata/atabellen/details\";\n" + //
					"\n" + //
					"	private static final Logger logger = LogManager.getLogger(CTableMaintenanceView.class);\n" + //
					"\n" + //
					"	@Autowired(required = false)\n" + //
					"	private MaintenanceViewRenderer<CTable> maintenanceViewRenderer;\n" + //
					"	@Autowired(required = false)\n" + //
					"	private DetailsLayoutComboBoxItemLabelGenerator<CTable> comboBoxItemLabelGenerator;\n" + //
					"\n" + //
					"	private final ButtonFactory buttonFactory;\n" + //
					"	private final ComponentFactory componentFactory;\n" + //
					"	private final ResourceManager resourceManager;\n" + //
					"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
					"	private final ServiceProvider serviceProvider;\n" + //
					"	private final SessionData session;\n" + //
					"\n" + //
					"	private CTable model;\n" + //
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
					"	public void doSetParameter(BeforeEvent event) {\n" + //
					"		long id = parametersMap.containsKey(\"id\") && (parametersMap.get(\"id\").size() > 0)\n" + //
					"				? Long.parseLong(parametersMap.get(\"id\").get(0))\n" + //
					"				: -1;\n" + //
					"		model = serviceProvider.getCTableService().findById(id).orElse(createNewModel());\n" + //
					"		if (parametersMap.containsKey(\"duplicate\") && \"true\".equals(parametersMap.get(\"duplicate\").get(0))) {\n" + //
					"			model.setId(-1);\n" + //
					"		}\n" + //
					"	}\n" + //
					"\n" + //
					"	private CTable createNewModel() {\n" + //
					"		String modelClassName = parametersMap.containsKey(\"modelClass\") && (parametersMap.get(\"modelClass\").size() > 0)\n" + //
					"				? parametersMap.get(\"modelClass\").get(0)\n" + //
					"				: \"CTable\";\n" + //
					"		if (modelClassName.equals(\"CTableSub0\")) {\n" + //
					"			return new CTableSub0();\n" + //
					"		}\n" + //
					"		if (modelClassName.equals(\"CTableSub1\")) {\n" + //
					"			return new CTableSub1();\n" + //
					"		}\n" + //
					"		return new CTable();\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void doBeforeEnter(BeforeEnterEvent beforeEnterEvent) {\n" + //
					"		UserAuthorizationChecker.forwardToLoginOnNoUserSetForSession(getSessionData(), beforeEnterEvent);\n" + //
					"		setMargin(false);\n" + //
					"		setSizeFull();\n" + //
					"		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n"
					+ //
					"		getStyle().set(\"background-size\", \"cover\");\n" + //
					"		getStyle().set(\"background-attachment\", \"fixed\");\n" + //
					"		add(\n" + //
					"				new HeaderLayout(\n" + //
					"						buttonFactory\n" + //
					"										.createBackButton(\n" + //
					"												resourceManager,\n" + //
					"												this::getUI,\n" + //
					"												CTablePageView.URL,\n" + //
					"												session),\n" + //
					"						buttonFactory.createLogoutButton(resourceManager, this::getUI, session, logger),\n" + //
					"								resourceManager.getLocalizedString(\"CTableMaintenanceView.header.prefix.label\", session.getLocalization()) + getHeaderSuffix(model),\n" + //
					"								HeaderLayoutMode.PLAIN),\n" + //
					"				getDetailsLayout(model));\n" + //
					"	}\n" + //
					"\n" + //
					"	private String getHeaderSuffix(CTable model) {\n" + //
					"		return maintenanceViewRenderer != null\n" + //
					"				? maintenanceViewRenderer.getHeaderSuffix(model)\n" + //
					"				: \"\" + model.getRef();\n" + //
					"	}\n" + //
					"\n" + //
					"	private AbstractMasterDataBaseLayout getDetailsLayout(CTable model) {\n" + //
					"		if (model.getClass() == CTableSub0.class) {\n" + //
					"			return new CTableSub0DetailsLayout(\n" + //
					"					buttonFactory,\n" + //
					"					componentFactory,\n" + //
					"					(CTableSub0) model,\n" + //
					"					serviceProvider,\n" + //
					"					guiConfiguration,\n" + //
					"					resourceManager,\n" + //
					"					session,\n" + //
					"					this,\n" + //
					"					comboBoxItemLabelGenerator);\n" + //
					"		}\n" + //
					"		if (model.getClass() == CTableSub1.class) {\n" + //
					"			return new CTableSub1DetailsLayout(\n" + //
					"					buttonFactory,\n" + //
					"					componentFactory,\n" + //
					"					(CTableSub1) model,\n" + //
					"					serviceProvider,\n" + //
					"					guiConfiguration,\n" + //
					"					resourceManager,\n" + //
					"					session,\n" + //
					"					this,\n" + //
					"					comboBoxItemLabelGenerator);\n" + //
					"		}\n" + //
					"		return new CTableDetailsLayout(buttonFactory, componentFactory, model, serviceProvider, guiConfiguration, resourceManager, session, this, comboBoxItemLabelGenerator);\n"
					+ //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	protected void onAttach(AttachEvent attachEvent) {\n" + //
					"		logger.info(\"onAttach\");\n" + //
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
					"	@Override\n" + //
					"	public void save(Object model) {\n" + //
					"		getUI().ifPresent(ui -> ui.navigate(CTablePageView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void save() {\n" + //
					"		save(model);\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void remove() {\n" + //
					"		serviceProvider.getCTableService().delete(model);\n" + //
					"		getUI().ifPresent(ui -> ui.navigate(CTablePageView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model-ForeignKey.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("C_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}