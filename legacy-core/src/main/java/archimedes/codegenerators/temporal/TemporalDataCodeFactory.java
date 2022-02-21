package archimedes.codegenerators.temporal;

import java.util.Arrays;
import java.util.List;

import archimedes.codegenerators.AbstractClassCodeFactory;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.CodeGenerator;
import archimedes.legacy.acf.event.CodeFactoryProgressionEventProvider;
import archimedes.legacy.acf.gui.StandardCodeFactoryProgressionFrameUser;
import archimedes.model.OptionType;
import archimedes.model.PredeterminedOptionProvider;
import archimedes.model.TableModel;

/**
 * A code factory for temporal data management based on attributes.
 *
 * @author ollie (22.04.2021)
 */
public class TemporalDataCodeFactory extends AbstractClassCodeFactory implements CodeFactoryProgressionEventProvider,
		PredeterminedOptionProvider, StandardCodeFactoryProgressionFrameUser {

	public static final String TEMPORAL = "TEMPORAL";

	public static final String TEMPLATE_FOLDER_PATH = AbstractCodeFactory.TEMPLATE_PATH
			+ System.getProperty(TemporalDataCodeFactory.class.getSimpleName() + ".templates.folder", "/temporal");

	@Override
	protected List<CodeGenerator<?>> getCodeGenerators() {
		return Arrays
				.asList(
						new PersistencePortGeneratedInterfaceCodeGenerator(this),
						new PersistencePortAdapterGeneratedClassCodeGenerator(this),
						new ServiceImplGeneratedClassCodeGenerator(this),
						new ServiceGeneratedInterfaceCodeGenerator(this));
	}

	@Override
	protected boolean isInCodeGeneration(TableModel tableModel) {
		return tableModel.isOptionSet(AbstractClassCodeFactory.GENERATE_ONLY_FOR) && tableModel
				.getOptionByName(AbstractClassCodeFactory.GENERATE_ONLY_FOR)
				.getParameter()
				.contains(TEMPORAL);
	}

	@Override
	public String getName() {
		return "Temporal Data Code Factory";
	}

	@Override
	public String[] getResourceBundleNames() {
		return new String[] { "archimedes", "temporal-data-code-factory" };
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public String[] getSelectableOptions(OptionType optionType) {
		switch (optionType) {
		case TABLE:
            return new String[] {
                    AbstractClassCodeFactory.NO_GENERATION,
                    AbstractClassCodeFactory.GENERATE_ONLY_FOR,
                    TEMPORAL };
		default:
			return new String[0];
		}
	}

}