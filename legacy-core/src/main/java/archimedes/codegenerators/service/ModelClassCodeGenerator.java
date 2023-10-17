package archimedes.codegenerators.service;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.CommonImportAdder;
import archimedes.codegenerators.FieldDeclarations;
import archimedes.codegenerators.ImportDeclarations;
import archimedes.codegenerators.NullableUtils;
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
public class ModelClassCodeGenerator extends AbstractClassCodeGenerator<ServiceNameGenerator> {

	public static final String IMPLEMENTS = "IMPLEMENTS";

	public ModelClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"ModelClass.vm",
				ServiceCodeFactory.TEMPLATE_FOLDER_PATH,
				ServiceNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		commonImportAdder = new CommonImportAdder();
		fieldDeclarations = new FieldDeclarations();
		List<ColumnData> columnData = getColumnData(table.getColumns(), table, model, getReferenceMode(model, table));
		commonImportAdder.addCommonImports(context, columnData);
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("GeneratedModelClassName", nameGenerator.getGeneratedModelClassName(table));
		context.put("Implements", getImplements(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("POJOMode", getPOJOMode(model, table).name());
		context.put("Subclass", true);
		context.put("TableName", table.getName());
	}

	private List<ColumnData> getColumnData(ColumnModel[] columns, TableModel table, DataModel model,
			ReferenceMode referenceMode) {
		List<ColumnData> l =
				Arrays
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
								.setPkMember(column.isPrimaryKey())
								.setSetterName(nameGenerator.getCamelCase(nameGenerator.getAttributeName(column))))
				.collect(Collectors.toList());
		getCompositionLists(table).forEach(cld -> {
			l
					.add(
							new ColumnData()
									.setFieldType("List<" + nameGenerator.getModelClassName(cld.getMemberTable()) + ">")
									.setFieldName(
											nameGenerator.getAttributeName(cld.getMemberTable().getName()) + "s")
									.setSetterName(
											nameGenerator
													.getCamelCase(
															nameGenerator
																	.getAttributeName(cld.getMemberTable().getName())
																	+ "s")));
			importDeclarations.add("java.util", "List");
		});
		return l;
	}

	private ImportDeclarations getImplements(DataModel model, TableModel table) {
		ImportDeclarations i = new ImportDeclarations();
		for (OptionModel option : table.getOptionsByName(IMPLEMENTS)) {
			String packageName = "";
			String typeName = option.getParameter();
			if (option.getParameter().contains(".")) {
				packageName = option.getParameter().substring(0, option.getParameter().lastIndexOf("."));
				typeName = option.getParameter().substring(option.getParameter().lastIndexOf(".") + 1);
			}
			i.add(packageName, typeName);
		}
		return i;
	}

	@Override
	protected String getType(ColumnModel column, DataModel model, ReferenceMode referenceMode,
			Function<ColumnModel, String> referencedClassNameProvider,
			BiFunction<ColumnModel, DataModel, String> enumClassNameProvider) {
		return column.isPrimaryKey()
				? typeGenerator.getJavaTypeString(column.getDomain(), NullableUtils.isNullable(column))
				: super.getType(column, model, referenceMode, referencedClassNameProvider, enumClassNameProvider);
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
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