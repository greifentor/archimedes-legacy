package archimedes.codegenerators.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.CommonImportAdder;
import archimedes.codegenerators.FieldDeclarations;
import archimedes.codegenerators.ReferenceMode;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;

/**
 * A class code generator for model objects.
 *
 * @author ollie (20.07.2021)
 */
public class GeneratedModelClassCodeGenerator extends AbstractClassCodeGenerator<ServiceNameGenerator> {

	public GeneratedModelClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"GeneratedModelClass.vm",
				ServiceCodeFactory.TEMPLATE_FOLDER_PATH,
				ServiceNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		commonImportAdder = new CommonImportAdder();
		fieldDeclarations = new FieldDeclarations();
		List<ColumnData> columnData = getColumnData(table.getColumns(), model, getReferenceMode(model, table));
		commonImportAdder.addCommonImports(context, columnData);
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("EntityName", nameGenerator.getClassName(table));
		context.put("HasEnums", hasEnums(table.getColumns()));
		context.put("HasReferences", hasReferences(table.getColumns()));
		context.put("PackageName", getPackageName(model, table));
		context.put("POJOMode", getPOJOMode(model, table).name());
		context.put("ReferenceMode", getReferenceMode(model, table).name());
		context.put("Subclass", table.isOptionSet(AbstractClassCodeGenerator.SUBCLASS));
		context.put("Superclass", table.isOptionSet(AbstractClassCodeGenerator.SUPERCLASS));
		context.put("SuperclassName", getSuperclassName(table, nameGenerator::getModelClassName));
		context.put("TableName", table.getName());
	}

	private List<ColumnData> getColumnData(ColumnModel[] columns, DataModel model, ReferenceMode referenceMode) {
		return Arrays
				.asList(columns)
				.stream()
				.map(
						column -> new ColumnData()
								.setFieldName(nameGenerator.getAttributeName(column))
								.setFieldType(
										getType(
												column,
												model,
												referenceMode,
												c -> nameGenerator.getModelClassName(c.getReferencedTable()),
												(c, m) -> nameGenerator.getModelClassName(c.getDomain(), model)))
								.setInitWith(getInitWithValue(column))
								.setPkMember(column.isPrimaryKey()))
				.collect(Collectors.toList());
	}

	private String getInitWithValue(ColumnModel column) {
		OptionModel initWith = column.getOptionByName(INIT_WITH);
		if (initWith != null) {
			return initWith.getParameter();
		}
		return null;
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getGeneratedModelClassName(table);
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