package archimedes.codegenerators;

import java.io.File;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;

import archimedes.model.DataModel;
import archimedes.model.NamedObject;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;

/**
 * A base class for code generators.
 *
 * @author ollie (03.03.2021)
 */
public abstract class AbstractCodeGenerator<N extends NameGenerator, T extends NamedObject>
		implements CodeGenerator<T> {

	public static final String GENERATED_CODE = "GENERATED CODE !!! DO NOT CHANGE !!!";

	public static final String ENUM = "ENUM";
	public static final String CONTEXT_NAME = "CONTEXT_NAME";
	public static final String MODULE_MODE = "MODULE_MODE";
	public static final String JAVAX_PACKAGE_NAME = "JAVAX_PACKAGE_NAME";
	public static final String SPRING_BOOT_VERSION = "SPRING_BOOT_VERSION";
	public static final String TEXT = "TEXT";

	protected static final String PROPERTY_PREFIX = "archimdes.code.generators.";
	protected static final String SLASH = "/";

	private static final Logger LOG = LogManager.getLogger(AbstractCodeGenerator.class);

	protected AbstractCodeFactory codeFactory;
	protected ImportDeclarations importDeclarations;
	protected N nameGenerator;
	protected TypeGenerator typeGenerator;

	private String templateFileName;
	private String templatePathName;
	private Map<String, Template> templateCache = new HashMap<>();

	public AbstractCodeGenerator(String templateFileName, String templatePathName, N nameGenerator,
			TypeGenerator typeGenerator, AbstractCodeFactory codeFactory) {
		super();
		this.codeFactory = codeFactory;
		this.nameGenerator = nameGenerator;
		this.templateFileName = changeSeparators(templateFileName);
		this.templatePathName = changeSeparators(templatePathName);
//		this.templatePathName = this.templatePathName + (!this.templatePathName.endsWith("/") ? "/" : "");
		this.typeGenerator = typeGenerator;
	}

	private String changeSeparators(String s) {
		return s.replace("\\", "/");
	}

	@Override
	public String generate(String basePackageName, DataModel model, T t) {
		importDeclarations = new ImportDeclarations();
		VelocityContext context = new VelocityContext();
		String javaxPackageName =
				model.getOptionByName(JAVAX_PACKAGE_NAME) == null
						? "javax"
						: model.getOptionByName(JAVAX_PACKAGE_NAME).getParameter();
		String springBootVersion = model.getOptionByName(SPRING_BOOT_VERSION) == null
				? "2"
				: model.getOptionByName(SPRING_BOOT_VERSION).getParameter();
		context.put("BasePackageName", basePackageName);
		context.put("Dollar", "$");
		context.put("Generated", GENERATED_CODE);
		context.put("PluralName", t != null ? t.getName().toLowerCase() + "s" : "");
		extendVelocityContext(context, model, t);
		afterExtendVelocityContext(context, model, t);
		context.put("ImportDeclarations", importDeclarations);
		context.put("JavaxPackageName", javaxPackageName);
		context.put("SpringBootVersion", springBootVersion);
		return processTemplate(context, templateFileName);
	}

	protected void afterExtendVelocityContext(VelocityContext context, DataModel model, T t) {
	}

	protected String getQualifiedName(String packageName, String className) {
		return ((packageName != null) && !packageName.isEmpty() ? packageName + "." : "") + className;
	}

	protected String processTemplate(VelocityContext context, String templateFileName) {
		return processTemplate(context, templateFileName, templatePathName);
	}

	protected String processTemplate(VelocityContext context, String templateFileName, String templatePathName) {
		Velocity.init();
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("resource.loaders", "file");
		velocityEngine.setProperty("resource.loader.file.class", FileResourceLoader.class.getName());
		velocityEngine
				.setProperty("resource.loader.file.path", Paths.get(templatePathName).toAbsolutePath().toString());
		velocityEngine.init();
		Template t = null;
		if (templateCache.containsKey(templateFileName)) {
			LOG
					.info(
							"loading template from cache: {} -> {}",
							Paths.get(templatePathName).toAbsolutePath().toString(),
							templateFileName);
			t = templateCache.get(templateFileName);
		} else {
			LOG
					.info(
							"loading template: {} -> {}",
							Paths.get(templatePathName).toAbsolutePath().toString(),
							templateFileName);
			t = velocityEngine.getTemplate(templateFileName);
			templateCache.put(templateFileName, t);
		}
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}

	protected void extendVelocityContext(VelocityContext context, DataModel model, T t) {
	}

	protected abstract String getDefaultModuleName(DataModel dataModel);

	protected boolean isToIgnoreFor(DataModel model, T t) {
		return false;
	}

	@Override
	public String getSourceFileName(String path, DataModel dataModel, T t) {
		String pathName = path + SLASH + getBaseCodeFolderName(dataModel) + SLASH
				+ getPackageName(dataModel, t).replace(".", SLASH);
		File packagePath = new File(pathName);
		if (!packagePath.exists()) {
			packagePath.mkdirs();
		}
		return pathName + SLASH + getClassName(dataModel, t) + getClassFileExtension(dataModel);
	}

	protected String getBaseCodeFolderName(DataModel dataModel) {
		return getBaseFolderName(dataModel, "java");
	}

	protected String getBaseResourceFolderName(DataModel dataModel) {
		return getBaseFolderName(dataModel, "resources");
	}

	protected String getBaseFolderName(DataModel dataModel, String subject) {
		return (isModuleModeSet(dataModel) && (getModuleName(dataModel) != null) ? getModuleName(dataModel) + "/" : "")
				+ System
						.getProperty(
								PROPERTY_PREFIX + getClass().getSimpleName() + ".base.code.folder.name",
								System
										.getProperty(
												PROPERTY_PREFIX + "base." + subject + ".folder.name",
												"src/main/" + subject));
	}

	protected String getClassFileExtension(DataModel dataModel) {
		return System
				.getProperty(
						PROPERTY_PREFIX + getClass().getSimpleName() + "class.file.extension",
						System.getProperty(PROPERTY_PREFIX + "class.file.extension", ".java"));
	}

	protected boolean isModuleModeSet(DataModel dataModel) {
		return dataModel.getOptionByName(MODULE_MODE) != null;
	}

	protected String getModuleName(DataModel dataModel) {
		String modulePrefix = dataModel.getApplicationName().toLowerCase();
		if (getAlternateModuleNamePrefix(dataModel) != null) {
			modulePrefix = getAlternateModuleNamePrefix(dataModel);
		}
		return modulePrefix + (modulePrefix.isEmpty() ? "" : "-") + buildModuleName(dataModel);
	}

	private String buildModuleName(DataModel dataModel) {
		OptionModel option = dataModel.getOptionByName(getAlternateModule());
		return option != null ? option.getParameter() : getDefaultModuleName(dataModel);
	}

	private String getAlternateModuleNamePrefix(DataModel dataModel) {
		OptionModel option = dataModel.getOptionByName(AbstractClassCodeGenerator.ALTERNATE_MODULE_PREFIX);
		return option != null ? option.getParameter() : null;
	}

	protected String getAlternateModule() {
		return "-";
	}

	protected String getContextName(TableModel table) {
		return table == null
				? null
				: OptionGetter
						.getOptionByName(table, CONTEXT_NAME)
						.map(OptionModel::getParameter)
						.orElse(nameGenerator.getClassName(table));
	}

}