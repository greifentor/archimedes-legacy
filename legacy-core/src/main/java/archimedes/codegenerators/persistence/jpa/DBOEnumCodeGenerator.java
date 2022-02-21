package archimedes.codegenerators.persistence.jpa;

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
public class DBOEnumCodeGenerator extends AbstractDomainCodeGenerator<PersistenceJPANameGenerator> {

	public DBOEnumCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"DBOEnum.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
				new PersistenceJPANameGenerator(),
				new TypeGenerator(),
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
		return nameGenerator.getDBOClassName(domain, model);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, DomainModel domain) {
		return nameGenerator.getDBOPackageName(model, null);
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, DomainModel domain) {
		return !domain.isOptionSet(ENUM);
	}

}