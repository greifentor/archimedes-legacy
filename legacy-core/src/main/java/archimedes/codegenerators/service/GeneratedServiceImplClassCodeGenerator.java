package archimedes.codegenerators.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.FindByUtils;
import archimedes.codegenerators.ReferenceMode;
import archimedes.codegenerators.Subclasses.SubclassData;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.ColumnModel;
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
				ServiceNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		ReferenceMode referenceMode = getReferenceMode(model, table);
		context.put("CascadeDeleteCode", getCascadeDeleteCodeData(model, table));
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
										typeGenerator,
										(c, m) -> nameGenerator.getModelClassName(c.getDomain(), m),
										d -> nameGenerator.getModelPackageName(model, d)));
		context.put("HasUniques", FindByUtils.hasUniques(table.getColumns()));
		context.put("HasNoUniques", FindByUtils.hasNoUniques(table.getColumns()));
		context
				.put(
						"HasObjectReferences",
						FindByUtils.hasObjectReferences(table.getColumns()) && (referenceMode == ReferenceMode.OBJECT));
		context.put("IdClassName", getIdClassName(table));
		context.put("IdFieldName", nameGenerator.getAttributeName(getIdFieldNameCamelCase(table)));
		context
				.put(
						"ListAccess",
						getListAccesses(
								model,
								table,
								c -> nameGenerator.getModelClassName(c.getReferencedTable()),
								(c, m) -> nameGenerator.getModelClassName(c.getDomain(), model),
								c -> nameGenerator.getModelPackageName(model, table) + "."
										+ nameGenerator.getModelClassName(c.getReferencedTable()),
								(c, m) -> nameGenerator.getModelPackageName(model, table) + "."
										+ nameGenerator.getModelClassName(c.getDomain(), model),
								null));
		context.put("ModelClassName", nameGenerator.getModelClassName(table));
		context.put("ModelPackageName", nameGenerator.getModelPackageName(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("PageClassName", nameGenerator.getPageClassName());
		context.put("PagePackageName", nameGenerator.getPagePackageName(model));
		context.put("PageParametersClassName", nameGenerator.getPageParametersClassName());
		context.put("PersistencePortInterfaceName", nameGenerator.getPersistencePortInterfaceName(table));
		context.put("PersistencePortPackageName", nameGenerator.getPersistencePortPackageName(model, table));
		context.put("ServiceInterfaceName", nameGenerator.getServiceInterfaceName(table));
		context.put("ServiceInterfacePackageName", nameGenerator.getServiceInterfacePackageName(model, table));
		context.put("Subclasses", getSubclassData(model, table));
	}

	private List<SubclassData> getSubclassData(DataModel model, TableModel table) {
		return getSubclassTables(table)
				.stream()
				.map(
						subclassTable -> new SubclassData()
								.setModelClassName(nameGenerator.getModelClassName(subclassTable))
								.setModelClassNameQualified(
										nameGenerator.getModelPackageName(model, subclassTable) + "."
												+ nameGenerator.getModelClassName(subclassTable)))
				.collect(Collectors.toList());
	}

	private List<CascadeDeleteCodeData> getCascadeDeleteCodeData(DataModel model, TableModel table) {
		return getReferencingColumns(table, model)
				.stream()
				.filter(this::isCascadeDeleteCodeSet)
				.map(column -> createCascadeDeleteCodeData(column, model))
				.collect(Collectors.toList());
	}

	private boolean isCascadeDeleteCodeSet(ColumnModel column) {
		return (column.getOptionByName(AbstractClassCodeGenerator.CASCADE_DELETE) != null)
				&& (column.getOptionByName(AbstractClassCodeGenerator.CASCADE_DELETE).getParameter() != null)
				&& (column
						.getOptionByName(AbstractClassCodeGenerator.CASCADE_DELETE)
						.getParameter()
						.contains(AbstractClassCodeGenerator.CODE));
	}

	private CascadeDeleteCodeData createCascadeDeleteCodeData(ColumnModel column, DataModel model) {
		return new CascadeDeleteCodeData()
				.setFindMethodName("findAllBy" + nameGenerator.getCamelCase(column.getName()))
				.setPersistencePortAttributeName(
						nameGenerator
								.getAttributeName(nameGenerator.getPersistencePortInterfaceName(column.getTable())))
				.setPersistencePortInterfaceName(nameGenerator.getPersistencePortInterfaceName(column.getTable()))
				.setPersistencePortPackageName(nameGenerator.getPersistencePortPackageName(model, column.getTable()));
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

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel table) {
		return super.isToIgnoreFor(model, table) || isSubclass(table) || isAMember(table);
	}

}