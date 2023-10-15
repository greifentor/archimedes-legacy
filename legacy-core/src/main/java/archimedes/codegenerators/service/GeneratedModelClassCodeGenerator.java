package archimedes.codegenerators.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Columns.AnnotationData;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.CommonImportAdder;
import archimedes.codegenerators.CompositionListData;
import archimedes.codegenerators.FieldDeclarations;
import archimedes.codegenerators.OptionGetter;
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
		List<ColumnData> columnData = getColumnData(table.getColumns(), table, model, getReferenceMode(model, table));
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

	private List<ColumnData> getColumnData(ColumnModel[] columns, TableModel table, DataModel model,
			ReferenceMode referenceMode) {
		List<ColumnData> l =
				Arrays
						.asList(columns)
						.stream()
						.filter(column -> !isAMember(table) || !isColumnReferencingAParent(column))
						.map(
								column -> new ColumnData()
										.setAnnotations(getAnnotationsForColumn(column))
										.setFieldName(nameGenerator.getAttributeName(column))
										.setFieldType(
												getType(
														column,
														model,
														referenceMode,
														c -> nameGenerator.getModelClassName(c.getReferencedTable()),
														(c, m) -> nameGenerator
																.getModelClassName(c.getDomain(), model)))
										.setInitWith(getInitWithValue(column))
										.setPkMember(column.isPrimaryKey()))
						.collect(Collectors.toList());
		getCompositionLists(table).forEach(cld -> {
			l
					.add(
							new ColumnData()
									.setFieldType("List<" + nameGenerator.getModelClassName(cld.getMemberTable()) + ">")
									.setFieldName(
											nameGenerator.getAttributeName(cld.getMemberTable().getName()) + "s"));
		});
		return l;
	}

	private List<AnnotationData> getAnnotationsForColumn(ColumnModel column) {
		List<AnnotationData> annotations = new ArrayList<>();
		column.ifOptionSetWithValueDo(TO_STRING, "EXCLUDE", om -> {
			importDeclarations.add("lombok", "ToString");
			annotations.add(new AnnotationData().setName("ToString.Exclude"));
		});
		return annotations;
	}

	private List<CompositionListData> getCompositionLists(TableModel table) {
		List<CompositionListData> l = new ArrayList<>();
		OptionGetter
				.getOptionByName(table, MEMBER_LIST)
				.filter(om -> (om.getParameter() != null) && om.getParameter().toUpperCase().equals("PARENT"))
				.ifPresent(om -> {
					getReferencingColumns(table, table.getDataModel())
							.stream()
							.filter(
									cm -> OptionGetter
											.getParameterOfOptionByName(cm.getTable(), MEMBER_LIST)
											.filter(s -> s.toUpperCase().equals("MEMBER"))
											.isPresent())
							.forEach(
									cm -> l
											.add(
													new CompositionListData()
															.setBackReferenceColumn(cm)
															.setMemberTable(cm.getTable())));
				});
		if (l.size() > 0) {
			importDeclarations.add("java.util", "List");
		}
		return l;
//		return Arrays
//				.asList(columns)
//				.stream()
//				.map(
//						column -> new ColumnData()
//								.setFieldName(nameGenerator.getAttributeName(column))
//								.setFieldType(
//										getType(
//												column,
//												model,
//												referenceMode,
//												c -> nameGenerator.getModelClassName(c.getReferencedTable()),
//												(c, m) -> nameGenerator.getModelClassName(c.getDomain(), model)))
//								.setInitWith(getInitWithValue(column))
//								.setPkMember(column.isPrimaryKey()))
//				.collect(Collectors.toList());
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