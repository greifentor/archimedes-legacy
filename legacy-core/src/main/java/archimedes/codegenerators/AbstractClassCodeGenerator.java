package archimedes.codegenerators;

import static corentx.util.Checks.ensure;

import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.VelocityContext;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * An abstract code generator for class files.
 *
 * @author ollie (15.03.2021)
 */
public abstract class AbstractClassCodeGenerator<N extends NameGenerator> extends AbstractCodeGenerator<N, TableModel> {

	public static final String ALTERNATE_MODULE_PREFIX = "ALTERNATE_MODULE_PREFIX";
	public static final String AUTO_INCREMENT = "AUTO_INCREMENT";
	public static final String COMMENTS = "COMMENTS";
	public static final String GENERATE_ID_CLASS = "GENERATE_ID_CLASS";
	public static final String LIST_ACCESS = "LIST_ACCESS";
	public static final String MAPPERS = "MAPPERS";
	public static final String POJO_MODE = "POJO_MODE";
	public static final String POJO_MODE_BUILDER = "BUILDER";
	public static final String POJO_MODE_CHAIN = "CHAIN";
	public static final String REFERENCE_MODE = "REFERENCE_MODE";
	public static final String REFERENCE_MODE_ID = "ID";
	public static final String REFERENCE_MODE_OBJECT = "OBJECT";

	private static final Logger LOG = LogManager.getLogger(AbstractClassCodeGenerator.class);

	protected CommonImportAdder commonImportAdder = new CommonImportAdder();

	public AbstractClassCodeGenerator(
			String templateFileName,
			String templatePathName,
			N nameGenerator,
			TypeGenerator typeGenerator,
			AbstractCodeFactory codeFactory) {
		super(templateFileName, templatePathName, nameGenerator, typeGenerator, codeFactory);
	}

	public void generate(String path, String basePackageName, DataModel dataModel, TableModel tableModel) {
		String code = generate(basePackageName, dataModel, tableModel);
		String fileName = getSourceFileName(path, dataModel, tableModel);
		try (FileWriter writer = new FileWriter(fileName)) {
			writer.write(code);
			LOG.info("wrote file: {}", fileName);
		} catch (Exception e) {
			LOG.error("error while generating class code for table: " + tableModel.getName(), e);
		}
	}

	protected boolean hasEnums(ColumnModel[] columns) {
		return List.of(columns).stream().anyMatch(column -> column.getDomain().isOptionSet(ENUM));
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
		return processTemplate(context, "JavaGetterName.vm", AbstractCodeFactory.TEMPLATE_PATH).trim();
	}

	private String getAttributeNameFirstLetterUpperCase(ColumnModel column) {
		String attrName = nameGenerator.getAttributeName(column);
		return attrName.substring(0, 1).toUpperCase()
				+ (attrName.length() == 1 ? "" : attrName.substring(1, attrName.length()));
	}

	protected String getSetterName(ColumnModel column) {
		VelocityContext context = new VelocityContext();
		context.put("FieldName", getAttributeNameFirstLetterUpperCase(column));
		context.put("NotNull", column.isNotNull());
		context.put("BooleanType", "boolean".equalsIgnoreCase(column.getDomain().getName()));
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

}