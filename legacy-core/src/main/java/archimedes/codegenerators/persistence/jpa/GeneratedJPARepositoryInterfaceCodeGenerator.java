package archimedes.codegenerators.persistence.jpa;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.FindByUtils;
import archimedes.codegenerators.GlobalIdOptionChecker;
import archimedes.codegenerators.GlobalIdType;
import archimedes.codegenerators.Layer;
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
				PersistenceJPANameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
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
										typeGenerator,
										this::getType,
										d -> nameGenerator.getDBOPackageName(model, d),
										Layer.PERSISTENCE));
		context.put("HasUniques", FindByUtils.hasUniques(table.getColumns()));
		context.put("HasNoUniques", FindByUtils.hasNoUniques(table.getColumns()));
		context
				.put(
						"HasObjectReferences",
						FindByUtils.hasObjectReferences(table.getColumns()) && (referenceMode == ReferenceMode.OBJECT));
		context.put("IdClassName", getIdClassName(table));
		context
				.put(
						"ListAccess",
						getListAccesses(
								model,
								table,
								c -> nameGenerator.getDBOClassName(c.getReferencedTable()),
								this::getType,
								c -> nameGenerator.getDBOPackageName(model, table) + "."
										+ nameGenerator.getDBOClassName(c.getReferencedTable()),
								(c, m) -> nameGenerator.getDBOPackageName(model, table) + "."
										+ nameGenerator.getDBOClassName(c.getDomain(), model),
								null));
		context.put("PackageName", getPackageName(model, table));
		context.put("Subclass", isSubclass(table));
	}

	private String getType(ColumnModel column, DataModel model) {
		if (GlobalIdOptionChecker.INSTANCE.getGlobalIdType(column) == GlobalIdType.UUID) {
			return "String";
		}
		return nameGenerator.getDBOClassName(column.getDomain(), model);
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

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel t) {
		return super.isToIgnoreFor(model, t) || isAMember(t);
	}

}