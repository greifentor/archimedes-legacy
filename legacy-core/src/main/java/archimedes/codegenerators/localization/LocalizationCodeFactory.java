package archimedes.codegenerators.localization;

import java.util.Arrays;
import java.util.List;

import archimedes.codegenerators.AbstractClassCodeFactory;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.CodeGenerator;
import archimedes.legacy.acf.event.CodeFactoryProgressionEventProvider;
import archimedes.legacy.acf.gui.StandardCodeFactoryProgressionFrameUser;
import archimedes.model.OptionType;
import archimedes.model.PredeterminedOptionProvider;

/**
 * A code factory for localization implementations and interfaces.
 *
 * @author ollie (07.01.2022)
 */
public class LocalizationCodeFactory extends AbstractClassCodeFactory implements CodeFactoryProgressionEventProvider,
		PredeterminedOptionProvider, StandardCodeFactoryProgressionFrameUser {

	public static final String TEMPLATE_FOLDER_PATH =
			AbstractCodeFactory.TEMPLATE_PATH + System
					.getProperty(LocalizationCodeFactory.class.getSimpleName() + ".templates.folder", "/localization");

	@Override
	protected List<CodeGenerator<?>> getCodeGenerators() {
		return Arrays
				.asList(
						new LocalizationSOClassCodeGenerator(this),
						new FileBasedResourceManagerConfigurationClassCodeGenerator(this),
						new FileBasedResourceManagerImplClassCodeGenerator(this),
						new ResourceManagerInterfaceCodeGenerator(this));
	}

	@Override
	public String getName() {
		return "Localization Code Factory";
	}

	@Override
	public String[] getResourceBundleNames() {
		return new String[] { "localization-code-factory" };
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public String[] getSelectableOptions(OptionType optionType) {
		switch (optionType) {
		case MODEL:
			return new String[] {
					LocalizationNameGenerator.ALTERNATE_LOCALIZATION_SO_PACKAGE_NAME,
					LocalizationNameGenerator.ALTERNATE_RESOURCE_MANAGER_IMPL_PACKAGE_NAME,
					LocalizationNameGenerator.ALTERNATE_RESOURCE_MANAGER_PACKAGE_NAME };
		default:
			return new String[0];
		}
	}

}