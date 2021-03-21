package archimedes.codegenerators;

import java.io.File;
import java.io.FileWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * An abstract code generator for class files.
 *
 * @author ollie (15.03.2021)
 */
public abstract class AbstractClassCodeGenerator<N extends NameGenerator> extends AbstractCodeGenerator<N> {

	public static final String GENERATE_ID_CLASS = "GENERATE_ID_CLASS";

	private static final Logger LOG = LogManager.getLogger(AbstractClassCodeGenerator.class);

	public AbstractClassCodeGenerator(
			String templateFileName,
			String templatePathName,
			N nameGenerator,
			TypeGenerator typeGenerator) {
		super(templateFileName, templatePathName, nameGenerator, typeGenerator);
	}

	public void generate(String path, String basePackageName, DataModel dataModel, TableModel tableModel) {
		String code = generate(basePackageName, dataModel, tableModel);
		String pathName =
				path + SLASH + getBaseCodeFolderName(dataModel) + SLASH + getPackageName(dataModel).replace(".", SLASH);
		File packagePath = new File(pathName);
		if (!packagePath.exists()) {
			packagePath.mkdirs();
		}
		String fileName = pathName + SLASH + getClassName(tableModel) + getClassFileExtension(dataModel);
		try (FileWriter writer = new FileWriter(fileName)) {
			writer.write(code);
			LOG.info("wrote file: {}", fileName);
		} catch (Exception e) {
			LOG.error("error while generating class code for table: " + tableModel.getName(), e);
		}
	}

	protected String getBaseCodeFolderName(DataModel dataModel) {
		return System
				.getProperty(
						PROPERTY_PREFIX + getClass().getSimpleName() + ".base.code.folder.name",
						System.getProperty(PROPERTY_PREFIX + "base.code.folder.name", "src/main/java"));
	}

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

}