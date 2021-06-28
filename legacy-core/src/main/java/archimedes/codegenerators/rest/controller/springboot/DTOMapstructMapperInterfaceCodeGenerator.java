package archimedes.codegenerators.rest.controller.springboot;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.OptionGetter;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A code generator for DTO converters.
 *
 * @author ollie (15.03.2021)
 */
public class DTOMapstructMapperInterfaceCodeGenerator extends AbstractClassCodeGenerator<RESTControllerNameGenerator> {

	private ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public DTOMapstructMapperInterfaceCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"DTOMapstructMapperInterface.vm",
				RESTControllerSpringBootCodeFactory.TEMPLATE_FOLDER_PATH,
				new RESTControllerNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("ConverterExtension", isConverterExtensionSet(model, table));
		context.put("ClassName", getClassName(table));
		context.put("DTOClassName", nameGenerator.getDTOClassName(table));
		context.put("DTOPackageName", nameGenerator.getDTOPackageName(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("SOClassName", serviceNameGenerator.getSOClassName(table));
		context.put("SOPackageName", serviceNameGenerator.getSOPackageName(model, table));
	}

	private boolean isConverterExtensionSet(DataModel model, TableModel table) {
		String mapper = getMappersParameter(model, table);
		return (mapper != null) && mapper.toLowerCase().endsWith(":converter");
	}

	private String getMappersParameter(DataModel model, TableModel table) {
		return OptionGetter.getParameterOfOptionByName(model, AbstractClassCodeGenerator.MAPPERS).orElse(null);
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getDTOConverterClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getDTOConverterPackageName(model, table);
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel table) {
		String mapper = getMappersParameter(model, table);
		return (mapper == null) || !mapper.toLowerCase().startsWith("mapstruct");
	}

}