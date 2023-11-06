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
import archimedes.model.DomainModel;
import archimedes.model.MessageCollector.Message;
import archimedes.model.MessageCollector.Priority;
import archimedes.model.TableModel;

/**
 * A base class for class code factories.
 *
 * @author ollie (01.04.2021)
 */
public abstract class AbstractClassCodeFactory extends AbstractCodeFactory {

	public static final String NO_GENERATION = "NO_GENERATION";
	public static final String GENERATE_ONLY_FOR = "GENERATE_ONLY_FOR";

	private static final Logger LOG = LogManager.getLogger(AbstractClassCodeFactory.class);

	@Override
	public boolean generate(String path) {
		LOG.info("Started code generation for: {}", getClass().getSimpleName());
		new File(path).mkdirs();
		String basePackageName = dataModel.getBasePackageName();
		List<CodeGenerator<?>> generators = getCodeGenerators();
		Counter processCounter = new Counter(1);
		initializeProgress(generators.size());
		for (DomainModel domainModel : dataModel.getAllDomains()) {
			try {
				generators
						.stream()
						.filter(codeGenerator -> codeGenerator instanceof AbstractDomainCodeGenerator<?>)
						.map(codeGenerator -> (AbstractDomainCodeGenerator<?>) codeGenerator)
						.forEach(codeGenerator -> {
							String fileName = codeGenerator.getSourceFileName(path, dataModel, domainModel);
							String generatorName = codeGenerator.getName();
							if (isReadyToOverride(fileName)) {
								if (!codeGenerator.isToIgnoreFor(dataModel, domainModel)) {
									// incrementStepProgress(stepCounter, "- writing file: " + fileName);
									LOG.info("CALLED generate for: " + generatorName);
									codeGenerator.generate(path, basePackageName, dataModel, domainModel);
									LOG.info("- wrote file to: {}", fileName);
									LOG.info("FINISHED generation for: " + generatorName);
								} else {
									// incrementStepProgress(stepCounter, "- ignored by generator: " + generatorName);
									messageCollector
											.add(
													new Message(
															generatorName,
															Priority.WARN,
															"Domain ignored: " + domainModel.getName()));
									LOG
											.info(
													"- ignored domain '{}' by generator: {}",
													domainModel.getName(),
													generatorName);
								}
								System.gc();
							} else {
								// incrementStepProgress(
								// stepCounter,
								// "- ignored by not ready to override for generator: " + generatorName);
								messageCollector
										.add(
												new Message(
														generatorName,
														Priority.WARN,
														"Domain not ready to override: " + domainModel.getName()));
								LOG
										.info(
												"- ignored domain '{}' by not ready to override: {}",
												domainModel.getName(),
												generatorName);
							}
						});
			} catch (RuntimeException e) {
				LOG.error("error while creating code for domain: " + domainModel.getName(), e);
				throw e;
			}
		}
		for (TableModel tableModel : dataModel.getTables()) {
			try {
				incrementProcessProgress(processCounter, "processing table: " + tableModel.getName());
				if (tableModel.isGenerateCode() && isInCodeGeneration(tableModel)) {
					initializeStepProgress(null);
					Counter stepCounter = new Counter(1);
					generators
							.stream()
							.filter(codeGenerator -> codeGenerator instanceof AbstractClassCodeGenerator<?>)
							.map(codeGenerator -> (AbstractClassCodeGenerator<?>) codeGenerator)
							.forEach(codeGenerator -> {
								String fileName = codeGenerator.getSourceFileName(path, dataModel, tableModel);
								String generatorName = codeGenerator.getName();
								if (isReadyToOverride(fileName)) {
									if (!codeGenerator.isToIgnoreFor(dataModel, tableModel)) {
										incrementStepProgress(stepCounter, null);
										LOG.info("CALLED generate for: " + generatorName);
										try {
										codeGenerator.generate(path, basePackageName, dataModel, tableModel);
										LOG.info("FINISHED generation for: " + generatorName);
										LOG.info("- wrote file to: {}", fileName);
										} catch (Exception e) {
											LOG
													.error(
															"Error while generating with: " + generatorName
																	+ ", table: " + tableModel.getName());
										}
									} else {
										incrementStepProgress(stepCounter, null);
										messageCollector
												.add(
														new Message(
																generatorName,
																Priority.WARN,
																"Table ignored: " + tableModel.getName()));
										LOG
												.info(
														"- ignored table '{}' by generator: {}",
														tableModel.getName(),
														generatorName);
									}
									System.gc();
								} else {
									incrementStepProgress(
											stepCounter,
											"- ignored by not ready to override for generator: " + generatorName);
									messageCollector
											.add(
													new Message(
															generatorName,
															Priority.WARN,
															"Table not ready to override: " + tableModel.getName()));
									LOG
											.info(
													"- ignored table '{}' by not ready to override: {}",
													tableModel.getName(),
													generatorName);
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
			} catch (RuntimeException e) {
				LOG.error("error while creating code for table: " + tableModel.getName(), e);
				throw e;
			}
		}
		generators
				.stream()
				.filter(codeGenerator -> codeGenerator instanceof AbstractModelCodeGenerator<?>)
				.map(codeGenerator -> (AbstractModelCodeGenerator<?>) codeGenerator)
				.forEach(codeGenerator -> {
					String fileName = codeGenerator.getSourceFileName(path, dataModel, dataModel);
					String generatorName = codeGenerator.getName();
					if (codeGenerator.isOverrideAlways() || isReadyToOverride(fileName)) {
						if (!codeGenerator.isToIgnoreFor(dataModel, dataModel)) {
							// incrementStepProgress(stepCounter, "- writing file: " + fileName);
							LOG.info("CALLED generate for: " + generatorName);
							codeGenerator.generate(path, basePackageName, dataModel);
							LOG.info("FINISHED generation for: " + generatorName);
							LOG.info("- wrote file to: {}", fileName);
						} else {
							// incrementStepProgress(stepCounter, "- ignored by generator: " + generatorName);
							LOG.info("- ignored model '{}' by generator: {}", dataModel.getName(), generatorName);
						}
						System.gc();
					} else {
						// incrementStepProgress(
						// stepCounter,
						// "- ignored by not ready to override for generator: " + generatorName);
						LOG
								.info(
										"- ignored model '{}' by not ready to override: {} - {}",
										dataModel.getName(),
										generatorName,
										fileName);
					}
				});
		System.gc();
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
		return !tableModel.isOptionSet(GENERATE_ONLY_FOR) && !tableModel.isOptionSet(NO_GENERATION);
	}

	protected boolean isReadyToOverride(String fileName) {
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

	protected abstract List<CodeGenerator<?>> getCodeGenerators();

	@Override
	public ModelChecker[] getModelCheckers() {
		return new ModelChecker[] {};
	}

	@Override
	public void setModelCheckerMessageListFrameListeners(ModelCheckerMessageListFrameListener... listeners) {
		// NOP
	}

}