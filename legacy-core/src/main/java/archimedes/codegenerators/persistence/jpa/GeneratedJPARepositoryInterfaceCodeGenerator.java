package archimedes.codegenerators.persistence.jpa;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.FindByUtils;
import archimedes.codegenerators.ListAccess.ListAccessData;
import archimedes.codegenerators.NullableUtils;
import archimedes.codegenerators.ReferenceMode;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A JPA repository code generator for JPA database objects (DBO's).
 *
 * @author ollie (27.06.2021)
 */
public class GeneratedJPARepositoryInterfaceCodeGenerator
		extends AbstractClassCodeGenerator<PersistenceJPANameGenerator> {

	public GeneratedJPARepositoryInterfaceCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"GeneratedJPARepositoryInterface.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
				new PersistenceJPANameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		ReferenceMode referenceMode = getReferenceMode(model, table);
		context.put("ClassName", getClassName(table));
		context.put("DBOClassName", nameGenerator.getDBOClassName(table));
		context.put("DBOPackageName", nameGenerator.getDBOPackageName(model, table));
		context
				.put(
						"FindBys",
						FindByUtils
								.getFindBys(
										table.getColumns(),
										referenceMode,
										nameGenerator,
										nameGenerator::getDBOClassName,
										t -> nameGenerator.getDBOPackageName(model, t),
										typeGenerator));
		context.put("HasUniques", FindByUtils.hasUniques(table.getColumns()));
		context.put("HasNoUniques", FindByUtils.hasNoUniques(table.getColumns()));
		context
				.put(
						"HasObjectReferences",
						FindByUtils.hasObjectReferences(table.getColumns()) && (referenceMode == ReferenceMode.OBJECT));
		context.put("IdClassName", getIdClassName(table));
		context.put("ListAccess", getListAccesses(model, table));
		context.put("PackageName", getPackageName(model, table));
	}

	private List<ListAccessData> getListAccesses(DataModel model, TableModel table) {
		return List
				.of(table.getColumns())
				.stream()
				.filter(column -> column.isOptionSet(LIST_ACCESS))
				.map(
						column -> new ListAccessData()
								.setFieldName(nameGenerator.getAttributeName(column.getName()))
								.setFieldNameCamelCase(nameGenerator.getClassName(column.getName()))
								.setTypeName(getType(column, model, getReferenceMode(model, table))))
				.collect(Collectors.toList());
	}

	private String getType(ColumnModel column, DataModel model, ReferenceMode referenceMode) {
		if ((column.getReferencedColumn() != null) && (referenceMode == ReferenceMode.OBJECT)) {
			return nameGenerator.getDBOClassName(column.getReferencedTable());
		} else if (isEnum(column)) {
			return nameGenerator.getDBOClassName(column.getDomain(), model);
		}
		return typeGenerator.getJavaTypeString(column.getDomain(), NullableUtils.isNullable(column));
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getGeneratedJPARepositoryInterfaceName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getGeneratedJPARepositoryPackageName(model, table);
	}

}