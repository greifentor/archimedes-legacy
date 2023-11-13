package archimedes.codegenerators;

import java.io.FileWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import archimedes.model.DataModel;
import archimedes.model.DomainModel;

public abstract class AbstractDomainCodeGenerator<N extends NameGenerator>
		extends AbstractCodeGenerator<N, DomainModel> {

	public static final String COMMENTS = "COMMENTS";

	private static final Logger LOG = LogManager.getLogger(AbstractClassCodeGenerator.class);

	public AbstractDomainCodeGenerator(String templateFileName, String templatePathName, N nameGenerator,
			TypeGenerator typeGenerator, AbstractCodeFactory codeFactory) {
		super(templateFileName, templatePathName, nameGenerator, typeGenerator, codeFactory);
	}

	public void generate(String path, String basePackageName, DataModel dataModel, DomainModel domainModel) {
		String code = generate(basePackageName, dataModel, domainModel);
		String fileName = getSourceFileName(path, dataModel, domainModel);
		try (FileWriter writer = new FileWriter(fileName)) {
			writer.write(code);
			LOG.info("wrote file: {}", fileName);
		} catch (Exception e) {
			LOG.error("error while generating class code for domain: " + domainModel.getName(), e);
		}
	}

	protected boolean isCommentsOff(DataModel model, DomainModel domain) {
		if ((model == null) || (domain == null)) {
			return false;
		}
		return OptionGetter
				.getOptionByName(model, COMMENTS)
				.map(option -> "off".equalsIgnoreCase(option.getParameter()))
				.orElse(false);
	}

	@Override
	public Type getType() {
		return Type.DOMAIN;
	}

}