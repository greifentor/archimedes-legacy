package archimedes.codegenerators.persistence.jpa;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Columns;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.Converters.ConverterData;
import archimedes.codegenerators.OptionGetter;
import archimedes.codegenerators.ReferenceMode;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A code generator for DBO converters.
 *
 * @author ollie (28.06.2021)
 */
public class DBOConverterClassCodeGenerator extends AbstractClassCodeGenerator<PersistenceJPANameGenerator> {

	private ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public DBOConverterClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"DBOConverterClass.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
				new PersistenceJPANameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		ReferenceMode referenceMode = getReferenceMode(model, table);
		List<ColumnData> columnData = getColumnData(table.getColumns(), model, referenceMode);
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		context.put("ConverterData", getConverterData(table.getColumns(), referenceMode));
		context.put("DBOClassName", nameGenerator.getDBOClassName(table));
		context
				.put(
						"DBOClassNameQualified",
						getQualifiedName(
								nameGenerator.getDBOPackageName(model, table),
								nameGenerator.getDBOClassName(table)));
		context.put("GenerateIdClass", isGenerateIdClass(model, table));
		if (Columns.containsFieldWithType(columnData, "LocalDate")) {
			context.put("ImportLocalDate", "java.time.LocalDate");
		}
		context.put("HasReferences", hasReferences(table.getColumns()));
		context.put("ModelClassName", serviceNameGenerator.getModelClassName(table));
		context
				.put(
						"ModelClassNameQualified",
						getQualifiedName(
								serviceNameGenerator.getModelPackageName(model, table),
								serviceNameGenerator.getModelClassName(table)));
		context.put("PackageName", getPackageName(model, table));
		context.put("ReferenceMode", referenceMode);
		context.put("ToDBOMethodName", nameGenerator.getToDBOMethodName(table));
		context.put("ToModelMethodName", nameGenerator.getToModelMethodName(table));
	}

	private List<ColumnData> getColumnData(ColumnModel[] columns, DataModel model, ReferenceMode referenceMode) {
		return List
				.of(columns)
				.stream()
				.map(
						column -> new ColumnData()
								.setConverterAttributeName(getDboConverterAttributeName(column))
								.setFieldName(nameGenerator.getAttributeName(column))
								.setFieldType(typeGenerator.getJavaTypeString(column.getDomain(), false))
								.setGetterCall(getGetterCall(column, model, referenceMode))
								.setPkMember(column.isPrimaryKey())
								.setReference(column.getReferencedColumn() != null)
								.setSetterName(getSetterName(column)))
				.collect(Collectors.toList());
	}

	private String getDboConverterAttributeName(ColumnModel column) {
		return (column.getReferencedColumn() == null)
				? "UNKNOWN"
				: nameGenerator.getAttributeName(getClassName(column.getReferencedTable()));
	}

	private String getGetterCall(ColumnModel column, DataModel model, ReferenceMode referenceMode) {
		String getterName = super.getGetterName(column);
		VelocityContext context = new VelocityContext();
		context.put("GetterName", getterName);
		context.put("ReferenceMode", referenceMode.name());
		if (isGenerateIdClass(model, column.getTable())) {
			context.put("KeyFromIdClass", column.isPrimaryKey());
		} else {
			context.put("KeyFromIdClass", "false");
		}
		return processTemplate(context, "DBOKeyGetter.vm");
	}

	private List<ConverterData> getConverterData(ColumnModel[] columns, ReferenceMode referenceMode) {
		if (referenceMode != ReferenceMode.OBJECT) {
			return List.of();
		}
		return List
				.of(columns)
				.stream()
				.filter(column -> column.getReferencedColumn() != null)
				.map(this::toConverterData)
				.sorted((cd0, cd1) -> cd0.getClassName().compareTo(cd1.getClassName()))
				.collect(Collectors.toList());
	}

	private ConverterData toConverterData(ColumnModel column) {
		String dboConverterClassName = getClassName(column.getReferencedTable());
		return new ConverterData()
				.setAttributeName(nameGenerator.getAttributeName(dboConverterClassName))
				.setClassName(dboConverterClassName);
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getDBOConverterClassName(table);
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
		return OptionGetter.getOptionByName(model, AbstractClassCodeGenerator.MAPPERS).isPresent();
	}

}