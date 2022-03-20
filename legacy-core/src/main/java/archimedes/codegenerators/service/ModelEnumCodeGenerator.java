package archimedes.codegenerators.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractDomainCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;

/**
 * A JPA repository code generator for JPA database objects (DBO's).
 *
 * @author ollie (27.06.2021)
 */
public class ModelEnumCodeGenerator extends AbstractDomainCodeGenerator<ServiceNameGenerator> {

	public ModelEnumCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"ModelEnum.vm",
				ServiceCodeFactory.TEMPLATE_FOLDER_PATH,
				ServiceNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DomainModel domain) {
		context.put("ClassName", getClassName(model, domain));
		context.put("Identifiers", getIdentifier(domain));
		context.put("PackageName", getPackageName(model, domain));
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
		return nameGenerator.getModelClassName(domain, model);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, DomainModel domain) {
		return nameGenerator.getModelPackageName(model, null);
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, DomainModel domain) {
		return !domain.isOptionSet(ENUM);
	}

}