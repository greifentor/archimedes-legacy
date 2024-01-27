package archimedes.codegenerators;

import static corentx.util.Checks.ensure;

import java.io.FileWriter;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.ListAccess.ListAccessConverterData;
import archimedes.codegenerators.ListAccess.ListAccessData;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;

/**
 * An abstract code generator for class files.
 *
 * @author ollie (15.03.2021)
 */
public abstract class AbstractClassCodeGenerator<N extends NameGenerator> extends AbstractCodeGenerator<N, TableModel> {

	public static final String GENERAL_TEMPLATE_FOLDER_PATH =
			AbstractCodeFactory.TEMPLATE_PATH + System.getProperty("general.templates.folder", "");

	public static final String ALTERNATE_MODULE_PREFIX = "ALTERNATE_MODULE_PREFIX";
	public static final String AUTO_INCREMENT = "AUTO_INCREMENT";
	public static final String CASCADE_DELETE = "CASCADE_DELETE";
	public static final String CODE = "CODE";
	public static final String COMMENTS = "COMMENTS";
	public static final String GENERATE_ID_CLASS = "GENERATE_ID_CLASS";
	public static final String INHERITANCE_MODE_JOINED = "JOINED";
	public static final String INIT_WITH = "INIT_WITH";
	public static final String LIST_ACCESS = "LIST_ACCESS";
	public static final String MAPPERS = "MAPPERS";
	public static final String MAX = "MAX";
	public static final String MEMBER_LIST = "MEMBER_LIST";
	public static final String MIN = "MIN";
	public static final String POJO_MODE = "POJO_MODE";
	public static final String POJO_MODE_BUILDER = "BUILDER";
	public static final String POJO_MODE_CHAIN = "CHAIN";
	public static final String REFERENCE_MODE = "REFERENCE_MODE";
	public static final String REFERENCE_MODE_ID = "ID";
	public static final String REFERENCE_MODE_OBJECT = "OBJECT";
	public static final String STEP = "STEP";
	public static final String SUBCLASS = "SUBCLASS";
	public static final String SUPERCLASS = "SUPERCLASS";
	public static final String TO_STRING = "TO_STRING";

	private static final Logger LOG = LogManager.getLogger(AbstractClassCodeGenerator.class);

	protected CommonImportAdder commonImportAdder;
	protected FieldDeclarations fieldDeclarations;

	public AbstractClassCodeGenerator(String templateFileName, String templatePathName, N nameGenerator,
			TypeGenerator typeGenerator, AbstractCodeFactory codeFactory) {
		super(templateFileName, templatePathName, nameGenerator, typeGenerator, codeFactory);
	}

	public void generate(String path, String basePackageName, DataModel dataModel, TableModel tableModel) {
		commonImportAdder = new CommonImportAdder();
		fieldDeclarations = new FieldDeclarations();
		String code = generate(basePackageName, dataModel, tableModel);
		String fileName = getSourceFileName(path, dataModel, tableModel);
		try (FileWriter writer = new FileWriter(fileName)) {
			writer.write(code);
			LOG.info("wrote file: {}", fileName);
		} catch (Exception e) {
			LOG.error("error while generating class code for table: " + tableModel.getName(), e);
		}
	}

	@Override
	protected void afterExtendVelocityContext(VelocityContext context, DataModel model, TableModel t) {
		context.put("FieldDeclarations", fieldDeclarations);
	}

	protected boolean hasEnums(ColumnModel[] columns) {
		return hasEnums(List.of(columns));
	}

	protected boolean hasEnums(List<ColumnModel> columns) {
		return columns.stream().anyMatch(column -> column.getDomain().isOptionSet(ENUM));
	}

	protected boolean hasMemberLists(TableModel table) {
		return OptionGetter
				.getOptionByName(table, MEMBER_LIST)
				.map(om -> (om.getParameter() != null) && om.getParameter().toUpperCase().equals("PARENT"))
				.orElse(false);

	}

	protected boolean hasReferences(ColumnModel[] columns) {
		return List.of(columns).stream().anyMatch(column -> column.getReferencedTable() != null);
	}

	protected boolean isEnum(ColumnModel column) {
		return column.getDomain().isOptionSet(ENUM);
	}

	protected String getGetterName(ColumnModel column) {
		VelocityContext context = new VelocityContext();
		context.put("FieldName", getAttributeNameFirstLetterUpperCase(column));
		context.put("NotNull", column.isNotNull());
		context.put("BooleanType", "boolean".equalsIgnoreCase(column.getDomain().getName()));
		return processTemplate(context, "JavaGetterName.vm", GENERAL_TEMPLATE_FOLDER_PATH).trim();
	}

	protected String getGetterName(String columnName) {
		VelocityContext context = new VelocityContext();
		context.put("FieldName", getAttributeNameFirstLetterUpperCase(columnName));
		return processTemplate(context, "JavaGetterName.vm", AbstractCodeFactory.TEMPLATE_PATH).trim();
	}

	private String getAttributeNameFirstLetterUpperCase(ColumnModel column) {
		String attrName = nameGenerator.getAttributeName(column);
		return attrName.substring(0, 1).toUpperCase()
				+ (attrName.length() == 1 ? "" : attrName.substring(1, attrName.length()));
	}

	private String getAttributeNameFirstLetterUpperCase(String columnName) {
		String attrName = nameGenerator.getAttributeName(columnName);
		return attrName.substring(0, 1).toUpperCase()
				+ (attrName.length() == 1 ? "" : attrName.substring(1, attrName.length()));
	}

	protected String getSetterName(ColumnModel column) {
		VelocityContext context = new VelocityContext();
		context.put("FieldName", getAttributeNameFirstLetterUpperCase(column));
		context.put("NotNull", column.isNotNull());
		context.put("BooleanType", "boolean".equalsIgnoreCase(column.getDomain().getName()));
		return processTemplate(context, "JavaSetterName.vm", GENERAL_TEMPLATE_FOLDER_PATH).trim();
	}

	protected String getSetterName(String columnName) {
		VelocityContext context = new VelocityContext();
		context.put("FieldName", getAttributeNameFirstLetterUpperCase(columnName));
		context.put("NotNull", "true");
		context.put("BooleanType", "false");
		return processTemplate(context, "JavaSetterName.vm", AbstractCodeFactory.TEMPLATE_PATH).trim();
	}

	protected boolean isGenerateIdClass(DataModel model, TableModel table) {
		return (model.getOptionByName(AbstractClassCodeGenerator.GENERATE_ID_CLASS) != null)
				|| (table.getOptionByName(AbstractClassCodeGenerator.GENERATE_ID_CLASS) != null);
	}

	@Deprecated
	protected boolean isNullable(ColumnModel column) {
		return NullableUtils.isNullable(column);
	}

	protected POJOMode getPOJOMode(DataModel model, TableModel table) {
		ensure(model != null, "data model cannot be null.");
		ensure(table != null, "table model cannot be null.");
		return OptionGetter
				.getOptionByName(table, POJO_MODE)
				.map(option -> POJOMode.valueOf(option.getParameter()))
				.orElse(
						OptionGetter
								.getOptionByName(model, POJO_MODE)
								.map(option -> POJOMode.valueOf(option.getParameter()))
								.orElse(POJOMode.CHAIN));
	}

	public String getClassName(TableModel table) {
		return getClassName(table.getDataModel(), table);
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return "UNKNOWN";
	}

	protected boolean isCommentsOff(DataModel model, TableModel table) {
		if ((model == null) || (table == null)) {
			return false;
		}
		return OptionGetter
				.getOptionByName(model, COMMENTS)
				.map(option -> "off".equalsIgnoreCase(option.getParameter()))
				.orElse(false);
	}

	protected String getIdFieldNameCamelCase(TableModel table) {
		return Arrays
				.asList(table.getPrimaryKeyColumns())
				.stream()
				.findFirst()
				.map(column -> nameGenerator.getClassName(column.getName()))
				.orElse("UNKNOWN");
	}

	protected boolean getIdFieldIsEnum(TableModel table) {
		return Arrays
				.asList(table.getPrimaryKeyColumns())
				.stream()
				.findFirst()
				.map(column -> isEnum(column))
				.orElse(false);
	}

	protected boolean hasSimpleTypeId(List<ColumnData> columns) {
		return columns
				.stream()
				.filter(column -> column.isPkMember())
				.anyMatch(column -> typeGenerator.isSimpleType(column.getFieldType()));
	}

	protected String getIdClassName(TableModel table) {
		return Arrays
				.asList(table.getPrimaryKeyColumns())
				.stream()
				.findFirst()
				.map(column -> typeGenerator.getJavaTypeString(column.getDomain(), true))
				.orElse("UNKNOWN");
	}

	protected ReferenceMode getReferenceMode(DataModel model, TableModel table) {
		ensure(model != null, "data model cannot be null.");
		ensure(table != null, "table model cannot be null.");
		return OptionGetter
				.getOptionByName(table, REFERENCE_MODE)
				.map(option -> ReferenceMode.valueOf(option.getParameter()))
				.orElse(
						OptionGetter
								.getOptionByName(model, REFERENCE_MODE)
								.map(option -> ReferenceMode.valueOf(option.getParameter()))
								.orElse(ReferenceMode.ID));
	}

	protected List<ColumnModel> getReferencingColumns(TableModel table, DataModel dataModel) {
		List<ColumnModel> columns = new ArrayList<>();
		for (ColumnModel column : dataModel.getAllColumns()) {
			if (column.getReferencedTable() == table) {
				columns.add(column);
			}
		}
		return columns;
	}

	protected List<ColumnModel> getAllColumns(List<ColumnModel> columns, TableModel table) {
		columns.addAll(List.of(table.getColumns()));
		TableModel superClassTable = getSuperclassTable(table);
		if (superClassTable != null) {
			columns.addAll(getAllColumns(columns, superClassTable));
		}
		return columns;
	}

	protected List<ListAccessData> getListAccesses(DataModel model, TableModel table,
			Function<ColumnModel, String> referencedClassNameProvider,
			BiFunction<ColumnModel, DataModel, String> enumClassNameProvider,
			Function<ColumnModel, String> referencedClassQualifiedNameProvider,
			BiFunction<ColumnModel, DataModel, String> enumClassQualifiedNameProvider,
			Function<ColumnModel, ListAccessConverterData> listAccessConverterDataProvider) {
		return List
				.of(table.getColumns())
				.stream()
				.filter(column -> column.isOptionSet(LIST_ACCESS))
				.map(
						column -> new ListAccessData()
								.setConverterData(
										listAccessConverterDataProvider != null
												? listAccessConverterDataProvider.apply(column)
												: null)
								.setFieldName(nameGenerator.getAttributeName(column.getName()))
								.setFieldNameCamelCase(nameGenerator.getClassName(column.getName()))
								.setTypeQualifiedName(
										getTypeQualifiedName(
												column,
												model,
												getReferenceMode(model, table),
												referencedClassQualifiedNameProvider,
												enumClassQualifiedNameProvider))
								.setTypeName(
										getType(
												column,
												model,
												getReferenceMode(model, table),
												referencedClassNameProvider,
												enumClassNameProvider)))
				.collect(Collectors.toList());
	}

	protected List<TableModel> getSubclassTables(TableModel tableToCheckFor) {
		List<TableModel> l =
				forAllTables(tableToCheckFor.getDataModel())
						.filter(tableToCheck -> isTableSubclassTableOf(tableToCheckFor, tableToCheck))
						.collect(Collectors.toList());
		List.of(l.toArray(new TableModel[l.size()])).forEach(t -> l.addAll(getSubclassTables(t)));
		return l;
	}

	private Stream<TableModel> forAllTables(DataModel model) {
		return List.of(model.getTables()).stream();
	}

	private boolean isTableSubclassTableOf(TableModel tableToCheckFor, TableModel tableToCheck) {
		return isSubclass(tableToCheck) && tableToCheckFor.isOptionSet(SUPERCLASS)
				? forAllPrimaryKeys(tableToCheck).anyMatch(column -> column.getReferencedTable() == tableToCheckFor)
				: false;
	}

	private Stream<ColumnModel> forAllPrimaryKeys(TableModel table) {
		return List.of(table.getPrimaryKeyColumns()).stream();
	}

	protected String getTypeQualifiedName(ColumnModel column, DataModel model, ReferenceMode referenceMode,
			Function<ColumnModel, String> referencedClassQualifiedNameProvider,
			BiFunction<ColumnModel, DataModel, String> enumClassQualifiedNameProvider) {
		if ((column.getReferencedColumn() != null) && (referenceMode == ReferenceMode.OBJECT)) {
			return referencedClassQualifiedNameProvider.apply(column);
		} else if (isEnum(column)) {
			return enumClassQualifiedNameProvider.apply(column, model);
		}
		return null;
	}

	protected String getType(ColumnModel column, DataModel model, ReferenceMode referenceMode,
			Function<ColumnModel, String> referencedClassNameProvider,
			BiFunction<ColumnModel, DataModel, String> enumClassNameProvider) {
		if ((column.getReferencedColumn() != null) && (referenceMode == ReferenceMode.OBJECT)) {
			return referencedClassNameProvider.apply(column);
		} else if (isEnum(column)) {
			return enumClassNameProvider.apply(column, model);
		}
		return typeGenerator.getJavaTypeString(column.getDomain(), NullableUtils.isNullable(column));
	}

	protected boolean isSimpleBoolean(ColumnModel column) {
		return (column.getDomain().getDataType() == Types.BOOLEAN) && column.isNotNull();
	}

	protected boolean isSubclass(TableModel table) {
		return table != null ? table.isOptionSet(AbstractClassCodeGenerator.SUBCLASS) : false;
	}

	protected boolean isSubclass(TableModel superTable, TableModel subTable) {
		return (subTable != null) && (superTable != null)
				? subTable.isOptionSet(AbstractClassCodeGenerator.SUBCLASS)
						&& isPrimaryKeyReferencingTo(superTable, subTable)
				: false;
	}

	protected boolean isPrimaryKeyReferencingTo(TableModel superTable, TableModel subTable) {
		return (subTable != null) && (superTable != null) && (subTable.getPrimaryKeyColumns().length > 0)
				? subTable.getPrimaryKeyColumns()[0].getReferencedTable() == superTable
				: false;
	}

	protected String getSuperclassName(TableModel table, Function<TableModel, String> classNameProvider) {
		TableModel referencedTable = getSuperclassTable(table);
		if (referencedTable != null) {
			return classNameProvider.apply(referencedTable);
		}
		return null;
	}

	protected String getDirectSuperclassName(TableModel table, Function<TableModel, String> classNameProvider) {
		TableModel referencedTable = getDirectSuperclassTable(table);
		if (referencedTable != null) {
			return classNameProvider.apply(referencedTable);
		}
		return null;
	}

	protected TableModel getSuperclassTable(TableModel table) {
		if (table.isOptionSet(AbstractClassCodeGenerator.SUBCLASS)) {
			TableModel superTable = table.getPrimaryKeyColumns()[0].getReferencedTable();
			return superTable.isOptionSet(AbstractClassCodeGenerator.SUBCLASS)
					? getSuperclassTable(superTable)
					: superTable;
		}
		return null;
	}

	protected TableModel getDirectSuperclassTable(TableModel table) {
		if (table.isOptionSet(AbstractClassCodeGenerator.SUBCLASS)) {
			return table.getPrimaryKeyColumns()[0].getReferencedTable();
		}
		return null;
	}

	@Override
	public Type getType() {
		return Type.TABLE;
	}

	protected boolean isAMember(TableModel table) {
		return OptionGetter
				.getOptionByName(table, MEMBER_LIST)
				.map(om -> isParameterEquals(om, "MEMBER"))
				.orElse(false);
	}

	protected boolean isParameterEquals(OptionModel om, String value) {
		return (om.getParameter() != null) && om.getParameter().toUpperCase().equals(value);
	}

	protected boolean isColumnReferencingAParent(ColumnModel column) {
		return (column.getReferencedTable() != null) && isAParent(column.getReferencedTable());
	}

	protected boolean isAParent(TableModel table) {
		return table != null
				? OptionGetter
						.getOptionByName(table, MEMBER_LIST)
						.map(om -> isParameterEquals(om, "PARENT"))
						.orElse(false)
				: false;
	}

	protected List<CompositionListData> getCompositionLists(TableModel table) {
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
		return l;
	}

}