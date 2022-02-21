package archimedes.codegenerators.service;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.FindByUtils;
import archimedes.codegenerators.ReferenceMode;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A code generator for service impl classes (port implementations).
 *
 * @author ollie (05.07.2021)
 */
public class GeneratedServiceImplClassCodeGenerator extends AbstractClassCodeGenerator<ServiceNameGenerator> {

	public GeneratedServiceImplClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"GeneratedServiceImplClass.vm",
				ServiceCodeFactory.TEMPLATE_FOLDER_PATH,
				new ServiceNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		ReferenceMode referenceMode = getReferenceMode(model, table);
		context.put("ClassName", getClassName(table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("ContextName", getContextName(table));
		context
				.put(
						"FindBys",
						FindByUtils
								.getFindBys(
										table.getColumns(),
										referenceMode,
										nameGenerator,
										nameGenerator::getModelClassName,
										t -> nameGenerator.getModelPackageName(model, t),
										typeGenerator));
		context.put("HasUniques", FindByUtils.hasUniques(table.getColumns()));
		context.put("HasNoUniques", FindByUtils.hasNoUniques(table.getColumns()));
		context
				.put(
						"HasObjectReferences",
						FindByUtils.hasObjectReferences(table.getColumns()) && (referenceMode == ReferenceMode.OBJECT));
		context.put("IdClassName", getIdClassName(table));
		context.put("IdFieldName", nameGenerator.getAttributeName(getIdFieldNameCamelCase(table)));
		context.put("ModelClassName", nameGenerator.getModelClassName(table));
		context.put("ModelPackageName", nameGenerator.getModelPackageName(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("PageClassName", nameGenerator.getPageClassName());
		context.put("PagePackageName", nameGenerator.getPagePackageName(model, table));
		context.put("PageParametersClassName", nameGenerator.getPageParametersClassName());
		context.put("PersistencePortInterfaceName", nameGenerator.getPersistencePortInterfaceName(table));
		context.put("PersistencePortPackageName", nameGenerator.getPersistencePortPackageName(model, table));
		context.put("ServiceInterfaceName", nameGenerator.getServiceInterfaceName(table));
		context.put("ServiceInterfacePackageName", nameGenerator.getServiceInterfacePackageName(model, table));
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getGeneratedServiceImplClassName(table);
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
