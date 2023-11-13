package archimedes.codegenerators.gui.vaadin;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractDomainCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.localization.LocalizationNameGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;

/**
 * A class code generator for the item label generator collection.
 *
 * @author ollie (08.10.2023)
 */
public class EnumItemLabelGeneratorClassCodeGenerator extends AbstractDomainCodeGenerator<GUIVaadinNameGenerator> {

	public static final String PREFERENCE = "PREFERENCE";

	public EnumItemLabelGeneratorClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"EnumItemLabelGeneratorClass.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				GUIVaadinNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DomainModel domain) {
		String enumClassName = nameGenerator.getClassName(domain.getName());
		context.put("ClassName", getClassName(model, domain));
		context.put("CommentsOff", isCommentsOff(model, domain));
		context.put("EnumPackageName", ServiceNameGenerator.INSTANCE.getModelPackageName(model, domain));
		context.put("EnumClassName", enumClassName);
		context
				.put(
						"ItemLabelGeneratorClassName",
						nameGenerator.getItemLabelGeneratorClassName(model, domain));
		context
				.put(
						"ItemLabelGeneratorCollectionPackageName",
						nameGenerator.getItemLabelGeneratorPackageName(model));
		context.put("PackageName", getPackageName(model, domain));
		context
				.put(
						"ResourceManagerInterfaceName",
						LocalizationNameGenerator.INSTANCE.getResourceManagerInterfaceName());
		context
				.put(
						"ResourceManagerPackageName",
						LocalizationNameGenerator.INSTANCE.getResourceManagerPackageName(model, null));
		getIdentifier(domain).forEach(l -> LabelPropertiesGenerator.addLabel(enumClassName + "." + l + ".label", l));
		LabelPropertiesGenerator.getLabels().forEach(System.out::println);
	}

	private List<String> getIdentifier(DomainModel domain) {
		StringTokenizer st = new StringTokenizer(domain.getOptionByName(ENUM).getParameter(), ",");
		List<String> l = new ArrayList<>();
		while (st.hasMoreTokens()) {
			l.add(st.nextToken());
		}
		return l;
	}

	@Override
	public String getClassName(DataModel model, DomainModel domain) {
		return nameGenerator.getItemLabelGeneratorClassName(model, domain);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, DomainModel domain) {
		return nameGenerator.getItemLabelGeneratorPackageName(model);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, DomainModel domain) {
		return !domain.isOptionSet(ENUM);
	}

}