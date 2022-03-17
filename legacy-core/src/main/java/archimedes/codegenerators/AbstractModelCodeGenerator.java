package archimedes.codegenerators;

import java.io.FileWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import archimedes.model.DataModel;

public abstract class AbstractModelCodeGenerator<N extends NameGenerator> extends AbstractCodeGenerator<N, DataModel> {

	public static final String COMMENTS = "COMMENTS";

	private static final Logger LOG = LogManager.getLogger(AbstractModelCodeGenerator.class);

	public AbstractModelCodeGenerator(String templateFileName, String templatePathName, N nameGenerator,
	        TypeGenerator typeGenerator, AbstractCodeFactory codeFactory) {
		super(templateFileName, templatePathName, nameGenerator, typeGenerator, codeFactory);
	}

	public void generate(String path, String basePackageName, DataModel dataModel) {
		String code = generate(basePackageName, dataModel, dataModel);
		String fileName = getSourceFileName(path, dataModel, dataModel);
		try (FileWriter writer = new FileWriter(fileName)) {
			writer.write(code);
			LOG.info("wrote file: {}", fileName);
		} catch (Exception e) {
			LOG.error("error while generating class code for data model.", e);
		}
	}

	protected boolean isCommentsOff(DataModel model) {
		if (model == null) {
			return false;
		}
		return OptionGetter
		        .getOptionByName(model, COMMENTS)
		        .map(option -> "off".equalsIgnoreCase(option.getParameter()))
		        .orElse(false);
	}

	@Override
	public Type getType() {
		return Type.MODEL;
	}

}