package archimedes.codegenerators.gui.vaadin.component;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeFactory;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractDomainCodeGenerator;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.EnumData;
import archimedes.codegenerators.MasterDataGridFieldRendererData;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.gui.vaadin.AbstractGUIVaadinClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.GUIVaadinCodeFactory;
import archimedes.codegenerators.gui.vaadin.GUIVaadinNameGenerator;
import archimedes.codegenerators.localization.LocalizationNameGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;

/**
 * A code generator for button classes.
 *
 * @author ollie (07.12.2021)
 */
public class ComponentFactoryClassCodeGenerator extends AbstractModelCodeGenerator<GUIVaadinNameGenerator> {

	private final static ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public ComponentFactoryClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"component/ComponentFactoryClass.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				GUIVaadinNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel sameModel) {
		OptionModel versionOption = model.getOptionByName(AbstractGUIVaadinClassCodeGenerator.VAADIN_VERSION);
		String version = versionOption == null ? "23" : versionOption.getParameter();
		context.put("ApplicationStartViewClassName", nameGenerator.getApplicationStartViewClassName());
		context.put("ApplicationStartViewPackageName", nameGenerator.getApplicationStartViewPackageName(model));
		context.put("ButtonClassName", nameGenerator.getButtonClassName(model));
		context.put("ButtonFactoryConfigurationClassName", nameGenerator.getButtonFactoryConfigurationClassName(model));
		context.put("EnumDataCollection", getEnumDataColection(model));
		context.put("ClassName", getClassName(model, model));
		context.put("CommentsOff", isCommentsOff(model));
		context.put("LocalizationSOClassName", LocalizationNameGenerator.INSTANCE.getLocalizationSOClassName());
		context
				.put(
						"LocalizationSOPackageName",
						LocalizationNameGenerator.INSTANCE.getLocalizationSOPackageName(model));
		context.put("PackageName", getPackageName(model, model));
		context.put("MasterDataGridFieldRendererPackageName", nameGenerator.getMasterDataPackageName(model));
		context
				.put(
						"MasterDataGridFieldRendererInterfaceName",
						nameGenerator.getMasterDataGridFieldRendererInterfaceName(model));
		context.put("MasterDataGridFieldRendererCollection", getMasterDataGridFieldRendererData(model));
		context.put("ResourceManagerInterfaceName", serviceNameGenerator.getResourceManagerInterfaceName());
		context
				.put(
						"ResourceManagerInterfacePackageName",
						serviceNameGenerator.getResourceManagerInterfacePackageName(model));
		context.put("SessionDataClassName", nameGenerator.getSessionDataClassName(model));
		context.put("SessionDataPackageName", nameGenerator.getSessionDataPackageName(model));
		context.put("Services", getServiceData(model, true));
		context.put("VaadinVersion", version);
	}

	private List<EnumData> getEnumDataColection(DataModel model) {
		return List
				.of(model.getAllDomains())
				.stream()
				.filter(domain -> domain.isOptionSet(AbstractDomainCodeGenerator.ENUM))
				.sorted((d0, d1) -> d0.getName().compareTo(d1.getName()))
				.map(
						domain -> new EnumData()
								.setEnumAttributeName(nameGenerator.getAttributeName(domain.getName()))
								.setEnumClassName(nameGenerator.getClassName(domain.getName()))
								.setEnumPackageName(serviceNameGenerator.getModelPackageName(model, domain)))
				.collect(Collectors.toList());
	}

	private List<MasterDataGridFieldRendererData> getMasterDataGridFieldRendererData(DataModel model) {
		return List
				.of(model.getTables())
				.stream()
				.filter(table -> !table.isOptionSet(AbstractClassCodeFactory.NO_GENERATION))
				.map(
						table -> new MasterDataGridFieldRendererData()
								.setAttributeName(nameGenerator.getAttributeName(table.getName()))
								.setModelClassName(serviceNameGenerator.getModelClassName(table))
								.setModelPackageName(serviceNameGenerator.getModelPackageName(model, model)))
				.collect(Collectors.toList());
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return nameGenerator.getComponentFactoryClassName(model);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, DataModel sameModel) {
		return nameGenerator.getVaadinComponentPackageName(model);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

}