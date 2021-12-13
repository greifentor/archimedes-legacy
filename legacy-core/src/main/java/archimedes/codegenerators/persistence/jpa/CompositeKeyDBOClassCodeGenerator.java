package archimedes.codegenerators.persistence.jpa;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.TableUtil;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for JPA database composite key (DBO's).
 *
 * @author ollie (13.12.2021)
 */
public class CompositeKeyDBOClassCodeGenerator extends AbstractClassCodeGenerator<PersistenceJPANameGenerator> {

	public CompositeKeyDBOClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"CompositeKeyDBOClass.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
				new PersistenceJPANameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		List<ColumnData> columnData = getColumnData(table.getColumns());
		commonImportAdder.addCommonImports(context, columnData);
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("POJOMode", getPOJOMode(model, table).name());
	}

	private List<ColumnData> getColumnData(ColumnModel[] columns) {
		return Arrays
				.asList(columns)
				.stream()
				.filter(column -> column.isPrimaryKey())
				.map(
						column -> new ColumnData()
								.setFieldName(nameGenerator.getAttributeName(column))
								.setFieldType(typeGenerator.getJavaTypeString(column.getDomain(), false)))
				.collect(Collectors.toList());
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getCompositeKeyDBOClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getDBOPackageName(model, table);
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel table) {
		return !TableUtil.hasCompositeKey(table);
	}

}