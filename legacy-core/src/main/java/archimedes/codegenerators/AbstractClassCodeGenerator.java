package archimedes.codegenerators;

import static corentx.util.Checks.ensure;

import java.io.File;
import java.io.FileWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;

/**
 * An abstract code generator for class files.
 *
 * @author ollie (15.03.2021)
 */
public abstract class AbstractClassCodeGenerator<N extends NameGenerator> extends AbstractCodeGenerator<N> {

	public static final String ALTERNATE_MODULE_PREFIX = "ALTERNATE_MODULE_PREFIX";
	public static final String POJO_MODE = "POJO_MODE";
	public static final String POJO_MODE_BUILDER = "BUILDER";
	public static final String POJO_MODE_CHAIN = "CHAIN";
	public static final String GENERATE_ID_CLASS = "GENERATE_ID_CLASS";
	public static final String MODULE_MODE = "MODULE_MODE";
	public static final String MODULE = "MODULE";

	private static final Logger LOG = LogManager.getLogger(AbstractClassCodeGenerator.class);

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

	protected String getBaseCodeFolderName(DataModel dataModel) {
		return (isModuleModeSet(dataModel) && (getModuleName(dataModel) != null) ? getModuleName(dataModel) + "/" : "")
				+ System
						.getProperty(
								PROPERTY_PREFIX + getClass().getSimpleName() + ".base.code.folder.name",
								System.getProperty(PROPERTY_PREFIX + "base.code.folder.name", "src/main/java"));
	}

	private boolean isModuleModeSet(DataModel dataModel) {
		return dataModel.getOptionByName(MODULE_MODE) != null;
	}

	protected String getModuleName(DataModel dataModel) {
		String modulePrefix = dataModel.getApplicationName().toLowerCase();
		if (getAlternateModuleName(dataModel) != null) {
			modulePrefix = getAlternateModuleName(dataModel);
		}
		return modulePrefix + (modulePrefix.isEmpty() ? "" : "-") + getDefaultModuleName(dataModel);
	}

	private String getAlternateModuleName(DataModel dataModel) {
		OptionModel option = dataModel.getOptionByName(AbstractClassCodeGenerator.ALTERNATE_MODULE_PREFIX);
		return option != null ? option.getParameter() : null;
	}

	@Override
	public String getSourceFileName(String path, DataModel dataModel, TableModel tableModel) {
		String pathName = path + SLASH
				+ getBaseCodeFolderName(dataModel)
				+ SLASH
				+ getPackageName(dataModel, tableModel).replace(".", SLASH);
		File packagePath = new File(pathName);
		if (!packagePath.exists()) {
			packagePath.mkdirs();
		}
		return pathName + SLASH + getClassName(tableModel) + getClassFileExtension(dataModel);
	}

	protected abstract String getDefaultModuleName(DataModel dataModel);

	protected String getClassFileExtension(DataModel dataModel) {
		return System
				.getProperty(
						PROPERTY_PREFIX + getClass().getSimpleName() + "class.file.extension",
						System.getProperty(PROPERTY_PREFIX + "class.file.extension", ".java"));
	}

	protected String getGetterName(ColumnModel column) {
		return "get" + getAttributeNameFirstLetterUpperCase(column);
	}

	private String getAttributeNameFirstLetterUpperCase(ColumnModel column) {
		String attrName = nameGenerator.getAttributeName(column);
		return attrName.substring(0, 1).toUpperCase()
				+ (attrName.length() == 1 ? "" : attrName.substring(1, attrName.length()));
	}

	protected String getSetterName(ColumnModel column) {
		return "set" + getAttributeNameFirstLetterUpperCase(column);
	}

	protected String getQualifiedName(String packageName, String className) {
		return ((packageName != null) && !packageName.isEmpty() ? packageName + "." : "") + className;
	}

	protected boolean isGenerateIdClass(DataModel model, TableModel table) {
		return (model.getOptionByName(AbstractClassCodeGenerator.GENERATE_ID_CLASS) != null)
				|| (table.getOptionByName(AbstractClassCodeGenerator.GENERATE_ID_CLASS) != null);
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

	public String getPackageName(DataModel model, TableModel table) {
		return OptionGetter.getOptionByName(table, MODULE).map(option -> option.getParameter() + ".").orElse("")
				+ getPackageNameLastPart(model, table);
	}

	public String getPackageNameLastPart(DataModel model, TableModel table) {
		return "";
	}

}