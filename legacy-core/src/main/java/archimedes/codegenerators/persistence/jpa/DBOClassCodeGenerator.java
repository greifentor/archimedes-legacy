package archimedes.codegenerators.persistence.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.codegenerators.Columns.AnnotationData;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.Columns.ParameterData;
import archimedes.codegenerators.CommonImportAdder;
import archimedes.codegenerators.FieldDeclarations;
import archimedes.codegenerators.ReferenceMode;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;

/**
 * A class code generator for JPA database objects (DBO's).
 *
 * @author ollie (03.03.2021)
 */
public class DBOClassCodeGenerator extends AbstractClassCodeGenerator<PersistenceJPANameGenerator> {

	public DBOClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"DBOClass.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
				PersistenceJPANameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		commonImportAdder = new CommonImportAdder();
		fieldDeclarations = new FieldDeclarations();
		List<ColumnData> columnData = getColumnData(table, model, getReferenceMode(model, table));
		commonImportAdder.addCommonImports(context, columnData);
		context.put("Autoincrement", getAutoincrementMode(columnData));
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("EntityName", nameGenerator.getClassName(table));
		context.put("HasEnums", hasEnums(table.getColumns()));
		context.put("HasReferences", hasReferences(table.getColumns()));
		context.put("HasCompositionLists", !getCompositionLists(table).isEmpty());
		context.put("IdColumnName", table.getPrimaryKeyColumns()[0].getName());
		context.put("PackageName", getPackageName(model, table));
		context.put("POJOMode", getPOJOMode(model, table).name());
		context.put("ReferenceMode", getReferenceMode(model, table).name());
		context.put("Subclass", table.isOptionSet(AbstractClassCodeGenerator.SUBCLASS));
		context.put("Superclass", table.isOptionSet(AbstractClassCodeGenerator.SUPERCLASS));
		context.put("SuperclassName", getDirectSuperclassName(table, nameGenerator::getDBOClassName));
		context.put("TableName", table.getName());
	}

	private String getAutoincrementMode(List<ColumnData> columnData) {
		return columnData
				.stream()
				.filter(
						column -> column
								.getAnnotations()
								.stream()
								.anyMatch(annotation -> annotation.getName().equals("GeneratedValue")))
				.map(column -> getGeneratedValueType(column.getAnnotations()))
				.reduce((s0, s1) -> s0 + "," + s1)
				.orElse(null);
	}

	private String getGeneratedValueType(List<AnnotationData> annotations) {
		return annotations
				.stream()
				.filter(annotation -> annotation.getName().equals("GeneratedValue"))
				.map(annotation -> annotation.getParameters().get(0).toJavaCode().replace("GenerationType.", ""))
				.findFirst()
				.orElse(null);
	}

	private List<ColumnData> getColumnData(TableModel table, DataModel model, ReferenceMode referenceMode) {
		List<ColumnData> l = Arrays
				.asList(table.getColumns())
				.stream()
				.filter(column -> !isAMember(table) || !isColumnReferencingAParent(column))
				.map(
						column -> new ColumnData()
								.setAnnotations(getAnnotations(column, referenceMode))
								.setFieldName(nameGenerator.getAttributeName(column))
								.setFieldType(
										getType(
												column,
												model,
												referenceMode,
												c -> nameGenerator.getDBOClassName(c.getReferencedTable()),
												(c, m) -> nameGenerator.getDBOClassName(c.getDomain(), model)))
								.setPkMember(column.isPrimaryKey()))
				.collect(Collectors.toList());
		getCompositionLists(table).forEach(cld -> {
			importDeclarations.add("java.util", "List");
			l
					.add(
							new ColumnData()
									.setAnnotations(
											Arrays
													.asList(
															new AnnotationData()
																	.setName("OneToMany")
																	.addParameter(
																			new ParameterData()
																					.setName("cascade")
																					.setValue("CascadeType.ALL"))
																	.addParameter(
																			new ParameterData()
																					.setName("fetch")
																					.setValue("FetchType.EAGER"))
																	.addParameter(
																			new ParameterData()
																					.setName("orphanRemoval")
																					.setValue("true")),
															new AnnotationData()
																	.setName("Fetch")
																	.addParameter(
																			new ParameterData()
																					.setName("value")
																					.setValue("FetchMode.SUBSELECT")),
															new AnnotationData()
																	.setName("JoinColumn")
																	.addParameter(
																			new ParameterData()
																					.setName("name")
																					.setValue(
																							"\"" + cld
																									.getBackReferenceColumn()
																									.getName()
																									+ "\""))))
									.setFieldType("List<" + nameGenerator.getDBOClassName(cld.getMemberTable()) + ">")
									.setFieldName(
											nameGenerator.getAttributeName(cld.getMemberTable().getName()) + "s"));
		});
		return l;
	}

	private List<AnnotationData> getAnnotations(ColumnModel column, ReferenceMode referenceMode) {
		List<AnnotationData> annotations = new ArrayList<>();
		if (column.isPrimaryKey()) {
			annotations.add(new AnnotationData().setName("Id"));
		}
		OptionModel autoincrement = column.getOptionByName(AbstractClassCodeGenerator.AUTO_INCREMENT);
		if (autoincrement != null) {
			if ("IDENTITY".equals(autoincrement.getParameter())) {
				annotations
						.add(
								new AnnotationData()
										.setName("GeneratedValue")
										.setParameters(
												Arrays
														.asList(
																new ParameterData()
																		.setName("strategy")
																		.setValue("GenerationType.IDENTITY"))));
			} else if ("SEQUENCE".equals(autoincrement.getParameter())) {
				String className = nameGenerator.getClassName(column.getTable());
				String sequenceGeneratorName = className + "Sequence";
				String sequenceName =
						column.getTable().getName().toLowerCase() + "_" + column.getName().toLowerCase() + "_seq";
				annotations
						.add(
								new AnnotationData()
										.setName("SequenceGenerator")
										.setParameters(
												Arrays
														.asList(
																new ParameterData()
																		.setName("allocationSize")
																		.setValue("1"),
																new ParameterData()
																		.setName("name")
																		.setValue("\"" + sequenceGeneratorName + "\""),
																new ParameterData()
																		.setName("sequenceName")
																		.setValue("\"" + sequenceName + "\""))));
				annotations
						.add(
								new AnnotationData()
										.setName("GeneratedValue")
										.setParameters(
												Arrays
														.asList(
																new ParameterData()
																		.setName("strategy")
																		.setValue("GenerationType.SEQUENCE"),
																new ParameterData()
																		.setName("generator")
																		.setValue(
																				"\"" + sequenceGeneratorName + "\""))));
			} else {
				System.out
						.println(
								"\n\nAUTOINCREMENT: invalid parameter value: " + autoincrement.getParameter() + "\n\n");
			}
		}
		if (column.getDomain().isOptionSet(AbstractCodeGenerator.ENUM)) {
			annotations
					.add(
							new AnnotationData()
									.setName("Enumerated")
									.addParameter(new ParameterData().setValue("EnumType.STRING")));
		}
		if ((column.getReferencedColumn() != null) && (referenceMode == ReferenceMode.OBJECT)) {
			AnnotationData annotationData =
					new AnnotationData().setName("JoinColumn").setParameters(getColumnAnnotationParameters(column));
			annotationData
					.addParameter(
							new ParameterData()
									.setName("referencedColumnName")
									.setValue("\"" + column.getReferencedColumn().getName() + "\""));
			annotations.add(annotationData);
			annotationData = new AnnotationData()
					.setName("ManyToOne")
					.addParameter(new ParameterData().setName("fetch").setValue("FetchType.EAGER"));
			annotations.add(annotationData);
		} else {
			annotations
					.add(new AnnotationData().setName("Column").setParameters(getColumnAnnotationParameters(column)));
		}
		column.ifOptionSetWithValueDo("TO_STRING", "EXCLUDE", om -> {
			importDeclarations.add("lombok", "ToString");
			annotations.add(new AnnotationData().setName("ToString.Exclude"));
		});
		return annotations;
	}

	private List<ParameterData> getColumnAnnotationParameters(ColumnModel column) {
		List<ParameterData> l = new ArrayList<>();
		l.add(new ParameterData().setName("name").setValue("\"" + column.getName() + "\""));
		if (column.isNotNull()) {
			l.add(new ParameterData().setName("nullable").setValue("false"));
		}
		return l;
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getDBOClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getDBOPackageName(model, table);
	}

}