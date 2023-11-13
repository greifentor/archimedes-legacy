package archimedes.codegenerators;

import java.util.function.Function;

import org.apache.velocity.VelocityContext;

import archimedes.model.DataModel;

/**
 * A class code generator for the session id classes.
 *
 * @author ollie (08.11.2023)
 */
public abstract class AbstractFileGenerator extends AbstractModelCodeGenerator<NameGenerator> {

	private String fileExtension;
	private Function<DataModel, String> packageNameProvider;

	public AbstractFileGenerator(String templateFileName, String fileExtension, AbstractCodeFactory codeFactory,
			String tempFolderPath, Function<DataModel, String> packageNameProvider) {
			super(
					templateFileName,
				tempFolderPath,
				NameGenerator.INSTANCE,
					TypeGenerator.INSTANCE,
					codeFactory);
		this.fileExtension = fileExtension;
		this.packageNameProvider = packageNameProvider;
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel model0) {
		super.extendVelocityContext(context, model, model0);
	}

	@Override
	protected String getClassFileExtension(DataModel dataModel) {
		return fileExtension;
	}

	@Override
	public String getPackageName(DataModel model, DataModel dummy) {
		return packageNameProvider.apply(model);
	}

	@Override
	protected String getBaseFolderName(DataModel dataModel, String subject) {
		return (isModuleModeSet(dataModel) && (getModuleName(dataModel) != null) ? getModuleName(dataModel) + "/" : "")
				+ getPackageNameSuffix();
	}

	protected abstract String getPackageNameSuffix();

}