package archimedes.codegenerators;

import java.io.StringWriter;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;

import archimedes.model.TableModel;

/**
 * A base class for code generators.
 *
 * @author ollie (03.03.2021)
 */
public abstract class AbstractCodeGenerator<N extends NameGenerator> implements CodeGenerator {

	public static final String GENERATED_CODE = "GENERATED CODE !!! DO NOT CHANGE !!!";

	private static final Logger LOG = LogManager.getLogger(AbstractCodeGenerator.class);

	protected N nameGenerator;
	protected TypeGenerator typeGenerator;

	private String templateFileName;
	private String templatePathName;

	public AbstractCodeGenerator(
			String templateFileName,
			String templatePathName,
			N nameGenerator,
			TypeGenerator typeGenerator) {
		super();
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
	public String generate(String basePackageName, TableModel table) {
		Velocity.init();
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("resource.loaders", "file");
		velocityEngine.setProperty("resource.loader.file.class", FileResourceLoader.class.getName());
		velocityEngine
				.setProperty("resource.loader.file.path", Paths.get(templatePathName).toAbsolutePath().toString());
		velocityEngine.init();
		LOG
				.info(
						"loading template: " + Paths.get(templatePathName).toAbsolutePath().toString() + " -> "
								+ templateFileName);
		Template t = velocityEngine.getTemplate(templateFileName);
		VelocityContext context = new VelocityContext();
		context.put("BasePackageName", basePackageName);
		context.put("Generated", GENERATED_CODE);
		context.put("PluralName", table.getName().toLowerCase() + "s");
		extendVelocityContext(context, table);
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}

	protected void extendVelocityContext(VelocityContext context, TableModel table) {
	}

}