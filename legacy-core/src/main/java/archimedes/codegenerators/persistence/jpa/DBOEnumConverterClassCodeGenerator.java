package archimedes.codegenerators.persistence.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractDomainCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;

/**
 * A JPA repository code generator for JPA database objects (DBO's).
 *
 * @author ollie (27.06.2021)
 */
public class DBOEnumConverterClassCodeGenerator extends AbstractDomainCodeGenerator<PersistenceJPANameGenerator> {

	private ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public DBOEnumConverterClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"DBOEnumConverterClass.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
				new PersistenceJPANameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DomainModel domain) {
		context.put("ClassName", getClassName(model, domain));
		context.put("DBOClassName", nameGenerator.getDBOClassName(domain, model));
		context
				.put(
						"DBOClassNameQualified",
						getQualifiedName(
								nameGenerator.getDBOPackageName(model, domain),
								nameGenerator.getDBOClassName(domain, model)));
		context.put("Identifiers", getIdentifier(domain));
		context.put("ModelClassName", serviceNameGenerator.getModelClassName(domain, model));
		context
				.put(
						"ModelClassNameQualified",
						getQualifiedName(
								serviceNameGenerator.getModelPackageName(model, domain),
								serviceNameGenerator.getModelClassName(domain, model)));
		context.put("PackageName", getPackageName(model, domain));
		context.put("ToDBOMethodName", nameGenerator.getToDBOMethodName(model));
		context.put("ToModelMethodName", nameGenerator.getToModelMethodName(model));
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
		return nameGenerator.getDBOConverterClassName(domain.getName(), model);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, DomainModel domain) {
		return nameGenerator.getDBOConverterPackageName(model, null);
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, DomainModel domain) {
		return !domain.isOptionSet(ENUM);
	}

}