package archimedes.codegenerators;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import archimedes.acf.checker.ModelChecker;
import archimedes.gui.checker.ModelCheckerMessageListFrameListener;
import archimedes.legacy.acf.event.CodeFactoryProgressionEvent;
import archimedes.legacy.gui.Counter;
import archimedes.model.TableModel;

/**
 * A base class for class code factories.
 *
 * @author ollie (01.04.2021)
 */
public abstract class AbstractClassCodeFactory extends AbstractCodeFactory {

	public static final String GENERATE_ONLY_FOR = "GENERATE_ONLY_FOR";

	private static final Logger LOG = LogManager.getLogger(AbstractClassCodeFactory.class);

	@Override
	public boolean generate(String path) {
		LOG.info("Started code generation for: {}", getClass().getSimpleName());
		new File(path).mkdirs();
		String basePackageName = dataModel.getBasePackageName();
		List<CodeGenerator> generators = getCodeGenerators();
		Counter processCounter = new Counter(1);
		initializeProgress(generators.size());
		for (TableModel tableModel : dataModel.getTables()) {
			incrementProcessProgress(processCounter, "processing table: " + tableModel.getName());
			if (tableModel.isGenerateCode() && isInCodeGeneration(tableModel)) {
				initializeStepProgress(null);
				Counter stepCounter = new Counter(1);
				generators.forEach(codeGenerator -> {
					String fileName = codeGenerator.getSourceFileName(path, dataModel, tableModel);
					incrementStepProgress(stepCounter, "- writing file: " + fileName);
					if (isReadyToOverride(fileName) && (codeGenerator instanceof AbstractClassCodeGenerator<?>)) {
						AbstractClassCodeGenerator<?> generator = ((AbstractClassCodeGenerator<?>) codeGenerator);
						if (!generator.isToIgnoreFor(dataModel, tableModel)) {
							generator.generate(path, basePackageName, dataModel, tableModel);
							LOG.info("- wrote file to: {}", fileName);
						} else {
							LOG.info("- ignored file to: {}", fileName);
						}
					}
				});
			} else {
				LOG
						.warn(
								"- suppressed for table '{}' - isGeneratorCode: {}, isInCodeGeneration: {}",
								tableModel.getName(),
								tableModel.isGenerateCode(),
								isInCodeGeneration(tableModel));
			}
		}
		return true;
	}

	private void initializeProgress(int generatorsCount) {
		fireCodeFactoryProgressEvent(
				new CodeFactoryProgressionEvent(
						getClass().getSimpleName(),
						null,
						null,
						null,
						null,
						dataModel.getTables().length,
						generatorsCount));
	}

	private void incrementProcessProgress(Counter processCounter, String message) {
		fireCodeFactoryProgressEvent(
				new CodeFactoryProgressionEvent(
						getClass().getSimpleName(),
						null,
						message,
						processCounter.inc(),
						null,
						null,
						null));
	}

	private void initializeStepProgress(String message) {
		fireCodeFactoryProgressEvent(
				new CodeFactoryProgressionEvent(getClass().getSimpleName(), null, null, null, null, null, null));
	}

	private void incrementStepProgress(Counter stepCounter, String message) {
		fireCodeFactoryProgressEvent(
				new CodeFactoryProgressionEvent(
						getClass().getSimpleName(),
						null,
						message,
						null,
						stepCounter.inc(),
						null,
						null));
	}

	protected boolean isInCodeGeneration(TableModel tableModel) {
		return !tableModel.isOptionSet(GENERATE_ONLY_FOR);
	}

	private boolean isReadyToOverride(String fileName) {
		if (new File(fileName).exists()) {
			try {
				String fileContent = Files.readString(Path.of(fileName));
				return fileContent.contains(AbstractCodeGenerator.GENERATED_CODE);
			} catch (IOException e) {
				LOG.error("error while reading source file for ready to override check: " + fileName, e);
				return false;
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