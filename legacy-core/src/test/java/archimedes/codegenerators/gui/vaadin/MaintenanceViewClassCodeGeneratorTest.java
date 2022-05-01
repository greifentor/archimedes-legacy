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
					"import base.pack.age.name.core.service.ATableService;\n" + //
					"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
					"import base.pack.age.name.gui.SessionData;\n" + //
					"import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" + //
					"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" + //
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
					"	public static final String URL = \"carp-dnd/masterdata/atabellen/details\";\n" + //
					"\n" + //
					"	private static final Logger logger = LogManager.getLogger(ATableMaintenanceView.class);\n" + //
					"\n" + //
					"	@Autowired(required = false)\n" + //
					"	private MaintenanceViewRenderer<ATable> maintenanceViewRenderer;\n" + //
					"\n" + //
					"	private final ButtonFactory buttonFactory;\n" + //
					"	private final ResourceManager resourceManager;\n" + //
					"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
					"	private final ATableService service;\n" + //
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
					"		model = service.findById(id).orElse(createNewModel());\n" + //
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
					"		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n"
					+ //
					"		getStyle().set(\"background-size\", \"cover\");\n" + //
					"		setMargin(false);\n" + //
					"		setWidthFull();\n" + //
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
					"		return new ATableDetailsLayout(buttonFactory, model, service, resourceManager, session, this);\n"
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
					"	public void save() {\n" + //
					"		getUI().ifPresent(ui -> ui.navigate(ATablePageView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void remove() {\n" + //
					"		service.delete(model);\n" + //
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
					"import base.pack.age.name.core.service.ATableService;\n" + //
					"import base.pack.age.name.core.service.AnotherTableService;\n" + //
					"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
					"import base.pack.age.name.gui.SessionData;\n" + //
					"import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" + //
					"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" + //
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
					"\n" + //
					"	private final ButtonFactory buttonFactory;\n" + //
					"	private final ResourceManager resourceManager;\n" + //
					"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
					"	private final ATableService service;\n" + //
					"	private final AnotherTableService anotherTableService;\n" + //
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
					"		model = service.findById(id).orElse(createNewModel());\n" + //
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
					"		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n"
					+ //
					"		getStyle().set(\"background-size\", \"cover\");\n" + //
					"		setMargin(false);\n" + //
					"		setWidthFull();\n" + //
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
					"		return new ATableDetailsLayout(buttonFactory, model, service, anotherTableService, resourceManager, session, this);\n"
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
					"	public void save() {\n" + //
					"		getUI().ifPresent(ui -> ui.navigate(ATablePageView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void remove() {\n" + //
					"		service.delete(model);\n" + //
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
					"import base.pack.age.name.core.model.ATable;\n" + //
					"import base.pack.age.name.core.model.AnotherHeirTable;\n" + //
					"import base.pack.age.name.core.model.AnotherHeirTableWithSameReference;\n" + //
					"import base.pack.age.name.core.model.AnotherTable;\n" + //
					"import base.pack.age.name.core.model.HeirTableWithReference;\n" + //
					"import base.pack.age.name.core.service.ATableService;\n" + //
					"import base.pack.age.name.core.service.ReferencedTableService;\n" + //
					"import base.pack.age.name.core.service.localization.ResourceManager;\n" + //
					"import base.pack.age.name.gui.SessionData;\n" + //
					"import base.pack.age.name.gui.vaadin.UserAuthorizationChecker;\n" + //
					"import base.pack.age.name.gui.vaadin.component.AbstractMasterDataBaseLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.ButtonFactory;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout;\n" + //
					"import base.pack.age.name.gui.vaadin.component.HeaderLayout.HeaderLayoutMode;\n" + //
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
					"\n" + //
					"	private final ButtonFactory buttonFactory;\n" + //
					"	private final ResourceManager resourceManager;\n" + //
					"	private final MasterDataGUIConfiguration guiConfiguration;\n" + //
					"	private final ATableService service;\n" + //
					"	private final ReferencedTableService referencedTableService;\n" + //
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
					"		model = service.findById(id).orElse(createNewModel());\n" + //
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
					"		getStyle().set(\"background-image\", \"url('\" + guiConfiguration.getBackgroundFileName() + \"')\");\n"
					+ //
					"		getStyle().set(\"background-size\", \"cover\");\n" + //
					"		setMargin(false);\n" + //
					"		setWidthFull();\n" + //
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
					"		if (model instanceof AnotherHeirTable) {\n" + //
					"			return new AnotherHeirTableDetailsLayout(\n" + //
					"					buttonFactory,\n" + //
					"					(AnotherHeirTable) model,\n" + //
					"					service,\n" + //
					"					resourceManager,\n" + //
					"					session,\n" + //
					"					this);\n" + //
					"		}\n" + //
					"		if (model instanceof AnotherHeirTableWithSameReference) {\n" + //
					"			return new AnotherHeirTableWithSameReferenceDetailsLayout(\n" + //
					"					buttonFactory,\n" + //
					"					(AnotherHeirTableWithSameReference) model,\n" + //
					"					service,\n" + //
					"					referencedTableService,\n" + //
					"					resourceManager,\n" + //
					"					session,\n" + //
					"					this);\n" + //
					"		}\n" + //
					"		if (model instanceof AnotherTable) {\n" + //
					"			return new AnotherTableDetailsLayout(\n" + //
					"					buttonFactory,\n" + //
					"					(AnotherTable) model,\n" + //
					"					service,\n" + //
					"					resourceManager,\n" + //
					"					session,\n" + //
					"					this);\n" + //
					"		}\n" + //
					"		if (model instanceof HeirTableWithReference) {\n" + //
					"			return new HeirTableWithReferenceDetailsLayout(\n" + //
					"					buttonFactory,\n" + //
					"					(HeirTableWithReference) model,\n" + //
					"					service,\n" + //
					"					referencedTableService,\n" + //
					"					resourceManager,\n" + //
					"					session,\n" + //
					"					this);\n" + //
					"		}\n" + //
					"		return new ATableDetailsLayout(buttonFactory, model, service, resourceManager, session, this);\n"
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
					"	public void save() {\n" + //
					"		getUI().ifPresent(ui -> ui.navigate(ATablePageView.URL));\n" + //
					"	}\n" + //
					"\n" + //
					"	@Override\n" + //
					"	public void remove() {\n" + //
					"		service.delete(model);\n" + //
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

	}

}