package archimedes.codegenerators.service;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A code generator for persistence port interfaces.
 *
 * @author ollie (04.07.2021)
 */
public class GeneratedPersistencePortInterfaceCodeGenerator extends AbstractClassCodeGenerator<ServiceNameGenerator> {

	public GeneratedPersistencePortInterfaceCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"GeneratedPersistencePortInterface.vm",
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
		context.put("IdFieldNameCamelCase", getIdFieldNameCamelCase(table));
		context.put("ModelClassName", nameGenerator.getModelClassName(table));
		context.put("ModelPackageName", nameGenerator.getModelPackageName(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("PageClassName", nameGenerator.getPageClassName());
		context.put("PagePackageName", nameGenerator.getPagePackageName(model, table));
		context.put("PageParametersClassName", nameGenerator.getPageParametersClassName());
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getGeneratedPersistencePortInterfaceName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getPersistencePortPackageName(model, table);
	}

}