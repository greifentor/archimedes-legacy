package archimedes.codegenerators.service;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A code generator for service interface (port).
 *
 * @author ollie (05.07.2021)
 */
public class ServiceInterfaceCodeGenerator extends AbstractClassCodeGenerator<ServiceNameGenerator> {

	public ServiceInterfaceCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"ServiceInterface.vm",
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
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getServiceInterfaceName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getServiceInterfacePackageName(model, table);
	}

}
