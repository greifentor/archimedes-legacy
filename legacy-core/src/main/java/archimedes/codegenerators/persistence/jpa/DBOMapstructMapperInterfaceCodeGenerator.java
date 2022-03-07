package archimedes.codegenerators.persistence.jpa;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.OptionGetter;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A code generator for DTO converters.
 *
 * @author ollie (29.06.2021)
 */
public class DBOMapstructMapperInterfaceCodeGenerator extends AbstractClassCodeGenerator<PersistenceJPANameGenerator> {

	private ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public DBOMapstructMapperInterfaceCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"DBOMapstructMapperInterface.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
				new PersistenceJPANameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("ConverterExtension", isConverterExtensionSet(model, table));
		context.put("ClassName", getClassName(table));
		context.put("DBOClassName", nameGenerator.getDBOClassName(table));
		context.put("DBOPackageName", nameGenerator.getDBOPackageName(model, table));
		context.put("ModelClassName", serviceNameGenerator.getModelClassName(table));
		context.put("ModelPackageName", serviceNameGenerator.getModelPackageName(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("ToDBOMethodName", nameGenerator.getToDBOMethodName(table));
		context.put("ToModelMethodName", nameGenerator.getToModelMethodName(table));
	}

	private boolean isConverterExtensionSet(DataModel model, TableModel table) {
		String mapper = getMappersParameter(model, table);
		return (mapper != null) && mapper.toLowerCase().endsWith(":converter");
	}

	private String getMappersParameter(DataModel model, TableModel table) {
		return OptionGetter.getParameterOfOptionByName(model, AbstractClassCodeGenerator.MAPPERS).orElse(null);
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getDBOConverterClassName(table.getName(), model);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getDBOConverterPackageName(model, table);
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel table) {
		String mapper = getMappersParameter(model, table);
		return (mapper == null) || !mapper.toLowerCase().startsWith("mapstruct") || isSubclass(table);
	}

}