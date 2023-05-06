package archimedes.codegenerators.gui.vaadin.masterdata;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.OptionGetter;
import archimedes.codegenerators.gui.vaadin.AbstractGUIVaadinClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.AbstractVaadinModelCodeGenerator;
import archimedes.codegenerators.gui.vaadin.GUIVaadinNameGenerator;
import archimedes.codegenerators.gui.vaadin.MasterDataData;
import archimedes.codegenerators.localization.LocalizationNameGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A code generator for the master data view.
 *
 * @author ollie (19.12.2022)
 */
public class MasterDataViewClassCodeGenerator extends AbstractVaadinModelCodeGenerator {

	public static final String ALTERNATIVE_MAIN_VIEW = "ALTERNATIVE_MAIN_VIEW";

	public MasterDataViewClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super("masterdata/MasterDataViewClass.vm", codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel sameModel) {
		context.put("BaseURL", getBaseURL(model));
		context.put("ButtonClassName", nameGenerator.getButtonClassName(model));
		context.put("ButtonFactoryClassName", nameGenerator.getButtonFactoryClassName(model));
		context.put("ButtonFactoryPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("ButtonGridClassName", nameGenerator.getButtonGridClassName(model));
		context.put("ButtonGridPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("ButtonPackageName", nameGenerator.getVaadinComponentPackageName(model));
		context.put("ClassName", getClassName(model, model));
		context.put("CommentsOff", isCommentsOff(model));
		context.put("HeaderLayoutClassName", nameGenerator.getHeaderLayoutClassName(model));
		context.put("HeaderLayoutPackageName", nameGenerator.getHeaderLayoutPackageName(model));
		context.put("MainMenuViewClassName", nameGenerator.getMainMenuViewClassName());
		context.put("MainMenuViewPackageName", nameGenerator.getVaadinPackageName(model));
		context.put("MasterDataGUIConfigurationClassName", nameGenerator.getMasterDataGUIConfigurationClassName(model));
		context
				.put(
						"MasterDataGUIConfigurationPackageName",
						nameGenerator.getMasterDataGUIConfigurationPackageName(model));
		context.put("MasterDataInfos", getMasterDataInfos(model));
		context.put("MasterDataViewClassName", nameGenerator.getMasterDataViewClassName(model));
		context.put("MasterDataViewPackageName", nameGenerator.getMasterDataPackageName(model));
		context
				.put(
						"MasterDataViewButtonAdderInterfaceName",
						nameGenerator.getMasterDataViewButtonAdderInterfaceName(model));
		context
				.put(
						"MasterDataViewButtonAdderPackageName",
						nameGenerator.getMasterDataViewButtonAdderPackageName(model));
		context.put("MainMenuViewImport", getMainMenuViewImport(model));
		context.put("MainMenuViewURL", getMainMenuViewURL(model));
		context.put("PackageName", getPackageName(model, model));
		context
				.put(
						"ResourceManagerInterfaceName",
						LocalizationNameGenerator.INSTANCE.getResourceManagerInterfaceName());
		context
				.put(
						"ResourceManagerPackageName",
						LocalizationNameGenerator.INSTANCE.getResourceManagerPackageName(model, null));
		context.put("SessionDataClassName", nameGenerator.getSessionDataClassName(model));
		context.put("SessionDataPackageName", nameGenerator.getSessionDataPackageName(model));
		context.put("UserAuthorizationCheckerClassName", nameGenerator.getUserAuthorizationCheckerClassName(model));
		context.put("UserAuthorizationCheckerPackageName", nameGenerator.getUserAuthorizationCheckerPackageName(model));
	}

	private List<MasterDataData> getMasterDataInfos(DataModel model) {
		return List
				.of(model.getTables())
				.stream()
				.filter(this::isInMasterDataGUI)
				.map(this::createMasterDataDataForTable)
				.sorted((m0, m1) -> m0.getModelClassName().compareTo(m1.getModelClassName()))
				.collect(Collectors.toList());
	}

	private boolean isInMasterDataGUI(TableModel table) {
		return table.isOptionSet(AbstractGUIVaadinClassCodeGenerator.GENERATE_MASTER_DATA_GUI)
				&& !table.isOptionSet(AbstractClassCodeGenerator.SUBCLASS);
	}

	private MasterDataData createMasterDataDataForTable(TableModel table) {
		return new MasterDataData()
				.setModelClassName(ServiceNameGenerator.INSTANCE.getModelClassName(table))
				.setPageViewName(nameGenerator.getPageViewClassName(table))
				.setResourceIdentifier(ServiceNameGenerator.INSTANCE.getModelClassName(table).toLowerCase());
	}

	private String getMainMenuViewImport(DataModel model) {
		return OptionGetter
				.getParameterOfOptionByName(model, ALTERNATIVE_MAIN_VIEW)
				.filter(value -> value.contains("|"))
				.map(value -> value.substring(value.indexOf("|") + 1))
				.orElse(null);
	}

	private String getMainMenuViewURL(DataModel model) {
		return OptionGetter
				.getParameterOfOptionByName(model, ALTERNATIVE_MAIN_VIEW)
				.map(
						value -> value.contains("|") && value.indexOf("|") > 0
								? value.substring(0, value.indexOf("|"))
								: value)
				.orElse("MainMenuView.URL");
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return nameGenerator.getMasterDataViewClassName(model);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, DataModel sameModel) {
		return nameGenerator.getMasterDataPackageName(model);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

}