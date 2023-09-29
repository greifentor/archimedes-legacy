package archimedes.codegenerators.persistence.jpa;

import java.util.Arrays;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.OptionGetter;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A code generator for JPA persistence adapters.
 *
 * @author ollie (28.06.2021)
 */
public class JPAPersistenceAdapterClassCodeGenerator extends AbstractClassCodeGenerator<PersistenceJPANameGenerator> {

	public JPAPersistenceAdapterClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"JPAPersistenceAdapterClass.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
		        PersistenceJPANameGenerator.INSTANCE,
		        TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("ClassName", getClassName(table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("PackageName", getPackageName(model, table));
		context
				.put(
						"GeneratedJPAPersistenceAdapterClassName",
						nameGenerator.getGeneratedJPAPersistenceAdapterClassName(table));
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getJPAPersistenceAdapterClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getJPAPersistenceAdapterPackageName(model, table);
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel table) {
		return hasDependentAttribute(model, table) || isSubclass(table) || isAMember(table);
	}

	private boolean hasDependentAttribute(DataModel model, TableModel table) {
		return Arrays
				.asList(table.getColumns())
				.stream()
				.anyMatch(
						column -> OptionGetter
								.getOptionByName(
										column,
										JPAPersistenceAdapterDependentClassCodeGenerator.DEPENDENT_ATTRIBUTE)
								.isPresent());
	}

}