package archimedes.codegenerators.gui.vaadin.cube;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.gui.vaadin.AbstractGUIVaadinClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.GUIVaadinCodeFactory;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.DataModel;

/**
 * A class code generator for the JWT service interface.
 *
 * @author ollie (06.01.2023)
 */
public class JWTServiceImplClassCodeGenerator extends AbstractModelCodeGenerator<CubeNameGenerator> {

	public JWTServiceImplClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"JWTServiceImplClass.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH + "/cube",
				CubeNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel sameModel) {
		context.put("AuthorizationDataClassName", nameGenerator.getAuthorizationDataClassName(model));
		context.put("AuthorizationUserInterfaceName", nameGenerator.getAuthorizationUserInterfaceName(model));
		context.put("AuthorizationUserPackageName", ServiceNameGenerator.INSTANCE.getModelPackageName(model, null));
		context
				.put(
						"AuthorizationUserServiceInterfaceName",
						nameGenerator.getAuthorizationUserServiceInterfaceName(model));
		context.put("ClassName", getClassName(model, null));
		context.put("CommentsOff", isCommentsOff(model));
		context.put("JWTServiceConfigurationClassName", nameGenerator.getJWTServiceConfigurationClassName(model));
		context.put("ExceptionPackageName", ServiceNameGenerator.INSTANCE.getExceptionsPackageName(model));
		context.put("JWTNotValidExceptionClassName", nameGenerator.getJWTNotValidExceptionClassName(model));
		context.put("JWTServiceInterfaceName", nameGenerator.getJWTServiceInterfaceName(model));
		context.put("PackageName", getPackageName(model, null));
		context
				.put(
						"ServiceInterfacePackageName",
						ServiceNameGenerator.INSTANCE.getServiceInterfacePackageName(model, null));
		context.put("UtilPackageName", ServiceNameGenerator.INSTANCE.getUtilPackageName(model));
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return nameGenerator.getJWTServiceImplClassName(model);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, DataModel sameModel) {
		return ServiceNameGenerator.INSTANCE.getServiceImplPackageName(model, null);
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, DataModel sameModel) {
		return !model.isOptionSet(AbstractGUIVaadinClassCodeGenerator.CUBE_APPLICATION);
	}

}