package archimedes.codegenerators;

import java.io.FileWriter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import archimedes.codegenerators.gui.vaadin.ServiceData;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;

public abstract class AbstractModelCodeGenerator<N extends NameGenerator> extends AbstractCodeGenerator<N, DataModel> {

	public static final String COMMENTS = "COMMENTS";
	public static final String GLOBAL_ID = "GLOBAL_ID";

	protected final static ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	private static final Logger LOG = LogManager.getLogger(AbstractModelCodeGenerator.class);

	public AbstractModelCodeGenerator(String templateFileName, String templatePathName, N nameGenerator,
			TypeGenerator typeGenerator, AbstractCodeFactory codeFactory) {
		super(templateFileName, templatePathName, nameGenerator, typeGenerator, codeFactory);
	}

	public void generate(String path, String basePackageName, DataModel dataModel) {
		String code = null;
		if (this instanceof FileManipulator) {
			code = ((FileManipulator) this).generate(path, dataModel);
		} else {
			code = generate(basePackageName, dataModel, dataModel);
		}
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

	public List<ServiceData> getServiceData(DataModel model) {
		return List
				.of(model.getTables())
				.stream()
				.filter(table -> !isSubclass(table) && !isAMember(table) && !isNoGeneration(table))
				.map(
						table -> new ServiceData()
								.setServiceAttributeName(
										nameGenerator
												.getAttributeName(serviceNameGenerator.getServiceInterfaceName(table)))
								.setServiceInterfaceName(serviceNameGenerator.getServiceInterfaceName(table))
								.setServiceModelAttributeName(
										nameGenerator.getAttributeName(serviceNameGenerator.getModelClassName(table)))
								.setServiceModelClassName(serviceNameGenerator.getModelClassName(table))
								.setServiceModelPackageName(serviceNameGenerator.getModelPackageName(model, table))
								.setServicePackageName(
										serviceNameGenerator.getServiceInterfacePackageName(model, table)))
				.collect(Collectors.toList());
	}

	protected boolean isSubclass(TableModel table) {
		return table != null ? table.isOptionSet(AbstractClassCodeGenerator.SUBCLASS) : false;
	}

	protected boolean isNoGeneration(TableModel table) {
		return table != null ? table.isOptionSet(AbstractClassCodeFactory.NO_GENERATION) : false;
	}

	protected boolean isAMember(TableModel table) {
		return OptionGetter
				.getOptionByName(table, AbstractClassCodeGenerator.MEMBER_LIST)
				.map(om -> isParameterEquals(om, "MEMBER"))
				.orElse(false);
	}

	protected boolean isParameterEquals(OptionModel om, String value) {
		return (om.getParameter() != null) && om.getParameter().toUpperCase().equals(value);
	}

	@Override
	public Type getType() {
		return Type.MODEL;
	}

	protected boolean isOverrideAlways() {
		return false;
	}

}