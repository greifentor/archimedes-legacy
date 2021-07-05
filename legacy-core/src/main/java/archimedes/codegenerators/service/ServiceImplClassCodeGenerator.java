package archimedes.codegenerators.service;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import org.apache.velocity.VelocityContext;

/**
 * A code generator for service impl classes (port implementations).
 *
 * @author ollie (05.07.2021)
 */
public class ServiceImplClassCodeGenerator extends AbstractClassCodeGenerator<ServiceNameGenerator> {

	public ServiceImplClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"ServiceImplClass.vm",
				ServiceCodeFactory.TEMPLATE_FOLDER_PATH,
				new ServiceNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("ClassName", getClassName(table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("ContextName", getContextName(table));
		context.put("IdClassName", getIdClassName(table));
		context.put("IdFieldName", nameGenerator.getAttributeName(getIdFieldNameCamelCase(table)));
		context.put("ModelClassName", nameGenerator.getModelClassName(table));
		context.put("ModelPackageName", nameGenerator.getModelPackageName(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("PersistencePortInterfaceName", nameGenerator.getPersistencePortInterfaceName(table));
		context.put("PersistencePortPackageName", nameGenerator.getPersistencePortPackageName(model, table));
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getServiceImplClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getServiceImplPackageName(model, table);
	}

}
