package archimedes.codegenerators.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.NullableUtils;
import archimedes.codegenerators.ReferenceMode;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for model objects.
 *
 * @author ollie (20.07.2021)
 */
public class ModelClassCodeGenerator extends AbstractClassCodeGenerator<ServiceNameGenerator> {

	public ModelClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"ModelClass.vm",
				ServiceCodeFactory.TEMPLATE_FOLDER_PATH,
				new ServiceNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		List<ColumnData> columnData = getColumnData(table.getColumns(), getReferenceMode(model, table));
		commonImportAdder.addCommonImports(context, columnData);
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("POJOMode", getPOJOMode(model, table).name());
		context.put("ReferenceMode", getReferenceMode(model, table).name());
		context.put("TableName", table.getName());
	}

	private List<ColumnData> getColumnData(ColumnModel[] columns, ReferenceMode referenceMode) {
		return Arrays
				.asList(columns)
				.stream()
				.map(
						column -> new ColumnData()
								.setFieldName(nameGenerator.getAttributeName(column))
								.setFieldType(getType(column, referenceMode)))
				.collect(Collectors.toList());
	}

	private String getType(ColumnModel column, ReferenceMode referenceMode) {
		if ((column.getReferencedColumn() != null) && (referenceMode == ReferenceMode.OBJECT)) {
			return nameGenerator.getModelClassName(column.getReferencedTable());
		}
		return typeGenerator.getJavaTypeString(column.getDomain(), NullableUtils.isNullable(column));
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getModelClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getModelPackageName(model, table);
	}

}