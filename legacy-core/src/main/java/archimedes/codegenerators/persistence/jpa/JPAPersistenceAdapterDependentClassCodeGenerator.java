package archimedes.codegenerators.persistence.jpa;

import java.util.Arrays;
import java.util.Optional;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.OptionGetter;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A code generator for JPA persistence adapters for dependent objects.
 *
 * @author ollie (02.07.2021)
 */
public class JPAPersistenceAdapterDependentClassCodeGenerator
		extends AbstractClassCodeGenerator<PersistenceJPANameGenerator> {

	public static final String DEPENDENT_ATTRIBUTE = "DEPENDENT_ATTRIBUTE";

	private ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public JPAPersistenceAdapterDependentClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"JPAPersistenceAdapterDependentClass.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
		        PersistenceJPANameGenerator.INSTANCE,
		        TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("ClassName", getClassName(table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("DependentAttributeName", getDependentAttributeName(table));
		context.put("DependentAttributeNameCamilCase", nameGenerator.getClassName(getDependentAttributeName(table)));
		context.put("DependentClassName", getDependentClassName(table));
		context.put("DBOConverterClassName", nameGenerator.getDBOConverterClassName(table.getName(), model));
		context.put("DBOConverterPackageName", nameGenerator.getDBOConverterPackageName(model, table));
		context.put("IdClassName", getIdClassName(table));
		context.put("IdFieldNameCamelCase", getIdFieldNameCamelCase(table));
		context.put("JPARepositoryClassName", nameGenerator.getJPARepositoryInterfaceName(table));
		context.put("JPARepositoryPackageName", nameGenerator.getJPARepositoryPackageName(model, table));
		context.put("ModelClassName", serviceNameGenerator.getModelClassName(table));
		context.put("ModelPackageName", serviceNameGenerator.getModelPackageName(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("PersistencePortInterfaceName", serviceNameGenerator.getPersistencePortInterfaceName(table));
		context
				.put(
						"PersistencePortInterfacePackageName",
						serviceNameGenerator.getPersistencePortPackageName(model, table));
		context.put("ToDBOMethodName", nameGenerator.getToDBOMethodName(model));
		context.put("ToModelMethodName", nameGenerator.getToModelMethodName(model));
	}

	private String getDependentAttributeName(TableModel table) {
		return getDependentColumn(table).map(nameGenerator::getAttributeName).orElse("UNKNOWN!!!");
	}

	private Optional<ColumnModel> getDependentColumn(TableModel table) {
		return Arrays
				.asList(table.getColumns())
				.stream()
				.filter(column -> OptionGetter.getOptionByName(column, DEPENDENT_ATTRIBUTE).isPresent())
				.findFirst();
	}

	private String getDependentClassName(TableModel table) {
		return getDependentColumn(table)
				.map(column -> typeGenerator.getJavaTypeString(column.getDomain(), !column.isNotNull()))
				.orElse("UNKNOWN!!!");
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
		return hasNoDependentAttribute(model, table) || isSubclass(table);
	}

	private boolean hasNoDependentAttribute(DataModel model, TableModel table) {
		return !Arrays
				.asList(table.getColumns())
				.stream()
				.anyMatch(column -> OptionGetter.getOptionByName(column, DEPENDENT_ATTRIBUTE).isPresent());
	}

}