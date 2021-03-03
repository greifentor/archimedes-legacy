package archimedes.codegenerators;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import archimedes.model.TableModel;

/**
 * A base class for code generators.
 *
 * @author ollie (03.03.2021)
 */
public abstract class AbstractCodeGenerator {

	public static final String GENERATED_CODE = "GENERATED CODE !!! DO NOT CHANGE !!!";

	private static final String TEMPLATE_PATH = "src/main/resources/templates/persistence-jpa";

	private String templateFileName;

	public AbstractCodeGenerator(String templateFileName) {
		super();
		this.templateFileName = templateFileName;
	}

	public String generate(String basePackageName, TableModel table) {
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.init();
		Template t = velocityEngine.getTemplate(TEMPLATE_PATH + "/" + templateFileName);
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