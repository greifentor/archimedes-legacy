package archimedes.codegenerators;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import archimedes.acf.checker.ModelChecker;
import archimedes.gui.checker.ModelCheckerMessageListFrameListener;
import archimedes.model.TableModel;

/**
 * A base class for class code factories.
 *
 * @author ollie (01.04.2021)
 */
public abstract class AbstractClassCodeFactory extends AbstractCodeFactory {

	private static final Logger LOG = LogManager.getLogger(AbstractClassCodeFactory.class);

	@Override
	public boolean generate(String path) {
		LOG.info("Started code generation for: {}", getClass().getSimpleName());
		new File(path).mkdirs();
		String basePackageName = dataModel.getBasePackageName();
		for (TableModel tableModel : dataModel.getTables()) {
			if (tableModel.isGenerateCode()) {
				getCodeGenerators().forEach(codeGenerator -> {
					if (codeGenerator instanceof AbstractClassCodeGenerator<?>) {
						((AbstractClassCodeGenerator<?>) codeGenerator)
								.generate(path, basePackageName, dataModel, tableModel);
					}
				});
			}
		}
		return true;
	}

	protected abstract List<CodeGenerator> getCodeGenerators();

	@Override
	public ModelChecker[] getModelCheckers() {
		return new ModelChecker[] {};
	}

	@Override
	public void setModelCheckerMessageListFrameListeners(ModelCheckerMessageListFrameListener... listeners) {
		// NOP
	}

}