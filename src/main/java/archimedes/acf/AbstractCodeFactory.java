/*
 * AbstractCodeFactory.java
 *
 * 31.03.2016
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.acf;

import static corentx.util.Checks.ensure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import archimedes.acf.checker.ModelChecker;
import archimedes.acf.checker.ModelCheckerMessage;
import archimedes.acf.event.CodeFactoryEvent;
import archimedes.acf.event.CodeFactoryEventType;
import archimedes.acf.event.CodeFactoryListener;
import archimedes.acf.gui.CodeGeneratorListConfigurationDialog;
import archimedes.acf.gui.CodeGeneratorReportHTMLViewer;
import archimedes.acf.io.DefaultSourceFileReader;
import archimedes.acf.io.DefaultSourceFileWriter;
import archimedes.acf.io.FileBaseCodePathReader;
import archimedes.acf.io.FileExistenceChecker;
import archimedes.acf.io.SourceFileReader;
import archimedes.acf.io.SourceFileWriter;
import archimedes.acf.param.TableParamIds;
import archimedes.acf.report.GenerationProcessReport;
import archimedes.gui.checker.ModelCheckerMessageListFrameListener;
import archimedes.gui.checker.ModelCheckerMessageListFrameListener.Event;
import archimedes.gui.checker.ModelCheckerMessageListFrameModal;
import archimedes.legacy.Version;
import archimedes.model.CodeFactory;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import baccara.gui.GUIBundle;
import baccara.gui.progress.ProgressMonitorDialogRunner;
import corentx.io.FileUtil;
import corentx.util.SortedVector;
import gengen.IndividualPreferences;
import gengen.IndividualPreferencesReader;

/**
 * An implementation of the <CODE>CodeFactory</CODE> interface as a base for code factories.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 31.03.2016 - Added.
 */

abstract public class AbstractCodeFactory implements CodeFactory, Runnable {

	protected IndividualPreferencesReader individualPreferencesReader = null;
	protected String basePath = null;
	protected CodeGeneratorListFactory codeGeneratorListFactory = null;
	protected FileExistenceChecker fileExistenceChecker = null;
	protected GUIBundle guiBundle = null;
	protected List<CodeFactoryListener> listeners = new Vector<CodeFactoryListener>();
	protected DataModel model = null;
	protected ModelCheckerMessageListFrameListener[] modelCheckerMessagelisteners = new ModelCheckerMessageListFrameListener[0];
	protected GenerationProcessReport report = null;
	protected Boolean result = null;
	protected SourceFileReader sourceFileReader = null;
	protected SourceFileWriter sourceFileWriter = null;

	private boolean testMode = false;

	/**
	 * Creates a new abstract code factory.
	 *
	 * @changed OLI 31.03.2016 - Added.
	 */
	public AbstractCodeFactory() {
		super();
		this.codeGeneratorListFactory = this.createCodeGeneratorListFactory();
		this.fileExistenceChecker = this.getFileExistenceChecker();
		this.sourceFileReader = this.createSourceFileReader();
		this.sourceFileWriter = this.createSourceFileWriter();
		this.testMode = this.isTestMode();
	}

	/**
	 * @changed OLI 18.04.2013 - Added.
	 */
	@Override
	public void addCodeFactoryListener(CodeFactoryListener l) {
		if (!this.listeners.contains(l)) {
			this.listeners.add(l);
		}
	}

	/**
	 * Creates a code generator list factory which delivers the code generators which are to execute by the factory.
	 *
	 * @return A code generator list factory which delivers the code generators which are to execute by the factory.
	 *
	 * @changed OLI 31.03.2016 - Added.
	 */
	abstract public CodeGeneratorListFactory createCodeGeneratorListFactory();

	/**
	 * Creates a new generation process report which is used for the code generation.
	 *
	 * @return A new generation process report which is used for the code generation.
	 *
	 * @changed OLI 31.03.2016 - Added.
	 */
	public GenerationProcessReport createGenerationProcessReport() {
		return new GenerationProcessReport(this.guiBundle, this.getVersion());
	}

	/**
	 * Creates a source file reader for the code factory.
	 *
	 * @return A source file reader for the code factory.
	 *
	 * @changed OLI 31.03.2016 - Added.
	 */
	public SourceFileReader createSourceFileReader() {
		return new DefaultSourceFileReader();
	}

	/**
	 * Creates a source file writer for the code factory.
	 *
	 * @return A source file writer for the code factory.
	 *
	 * @changed OLI 31.03.2016 - Added.
	 */
	public SourceFileWriter createSourceFileWriter() {
		return new DefaultSourceFileWriter();
	}

	protected void fireCodeFactoryEvent(CodeFactoryEvent event) {
		for (CodeFactoryListener l : this.listeners) {
			try {
				l.eventFired(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void fireExceptionDetected(Throwable e) {
		for (CodeFactoryListener l : this.listeners) {
			try {
				l.exceptionDetected(this, e);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * @changed OLI 18.04.2013 - Added.
	 */
	@Override
	public boolean generate(String basePath) {
		this.report = this.createGenerationProcessReport();
		this.basePath = basePath;
		Thread t = new Thread(this);
		t.start();
		return true;
	}

	/**
	 * Returns the name of the stereotype which have to set to keep a table out of the generation process.
	 *
	 * @return The name of the stereotype which have to set to keep a table out of the generation process.
	 * 
	 * @changed OLI 31.03.2016 - Added.
	 */
	abstract public String getExclusionStereoTypeName();

	/**
	 * Returns a file existence checker for the code factory.
	 *
	 * @return A file existence checker for the code factory.
	 *
	 * @changed OLI 31.03.2016 - Added.
	 */
	public FileExistenceChecker getFileExistenceChecker() {
		return new FileExistenceChecker();
	}

	/**
	 * @changed OLI 08.12.2016 - Added.
	 */
	@Override
	public GUIBundle getGUIBundle() {
		return this.guiBundle;
	}

	/**
	 * Returns the result value.
	 *
	 * @return The result value;
	 *
	 * @changed OLI 26.09.2013 - Added.
	 */
	public Boolean getResult() {
		return this.result;
	}

	/**
	 * Checks if the data model is ready to create code for.
	 *
	 * @return <CODE>true</CODE> if no problems are detected and the code generation process could be started. Otherwise
	 *         it shows a message and returns <CODE>false</CODE>.
	 *
	 * @changed OLI 12.01.2015 - Added.
	 */
	public boolean isCodeFactoryIsReadyToGenerate() {
		List<ModelCheckerMessage> messages = new LinkedList<ModelCheckerMessage>();
		for (ModelChecker mc : this.getModelCheckers()) {
			ModelCheckerMessage[] mcms = mc.check(this.model);
			if (mcms.length > 0) {
				for (ModelCheckerMessage mcm : mcms) {
					messages.add(mcm);
				}
			}
		}
		if (messages.size() > 0) {
			ModelCheckerMessageListFrameModal d = new ModelCheckerMessageListFrameModal(this.guiBundle,
					messages.toArray(new ModelCheckerMessage[0]), true, this.modelCheckerMessagelisteners);
			return d.getReturnType() == Event.Type.GENERATE;
		}
		return true;
	}

	/**
	 * Checks if the code factory is running in the test mode (for test purposes overwrite this method and return
	 * <CODE>true</CODE>.
	 *
	 * @return <CODE>true</CODE> if running in test mode.
	 *
	 * @changed OLI 31.03.2016 - Added.
	 */
	public boolean isTestMode() {
		return false;
	}

	/*
	 * This method will be called in test mode only and allows to prepare the code generators. It can be overridden if
	 * changes in the code generators are required.
	 *
	 * @param codeGenerators The list of code generators which will be called be the factory.
	 *
	 * @changed OLI 27.11.2013 - Added.
	 */
	protected void prepareForTest(List<CodeGenerator> codeGenerators) {
	}

	/**
	 * @changed OLI 18.04.2013 - Added.
	 */
	@Override
	public void removeCodeFactoryListener(CodeFactoryListener l) {
		if (this.listeners.contains(l)) {
			this.listeners.remove(l);
		}
	}

	/**
	 * @changed OLI 25.09.2013 - Added.
	 */
	@Override
	public void run() {
		List<CodeGenerator> codeGenerators = this.codeGeneratorListFactory.getCodeGenerators();
		String configFilePath = FileUtil.completePath(System.getProperty("user.home")).concat(".isisacf.properties");
		IndividualPreferences ip = this.checkForIndividualPreferences(this.basePath, configFilePath);
		if (!this.isCodeFactoryIsReadyToGenerate()) {
			return;
		}
		if (!testMode) {
			CodeGeneratorListConfigurationDialog d = new CodeGeneratorListConfigurationDialog(this.guiBundle,
					codeGenerators.toArray(new CodeGenerator[0]), ip, this.getTablesToGenerate(), configFilePath,
					this.getVersion());
			if (d.getClosingState() != CodeGeneratorListConfigurationDialog.ClosingState.GENERATE) {
				return;
			}
		} else {
			this.prepareForTest(codeGenerators);
		}
		double progressStep = this.computeSingleProgressStep(codeGenerators.size(), this.model);
		List<PostGeneratingCommand> postGeneratingCommands = new Vector<PostGeneratingCommand>();
		this.fireCodeFactoryEvent(
				new CodeFactoryEvent(CodeFactoryEventType.PREPARATION_COMPLETE, ip.getBaseCodePathes(), this));
		boolean result = true;
		ProgressMonitorDialogRunner pmdr = this.createProgressMonitorDialogRunner();
		List<String> generatedFileNames = new LinkedList<String>();
		UnchangedByTagChecker unchangedByTagChecker = this.createUnchangedByTagChecker(ip, pmdr, this.model.getName(),
				generatedFileNames);
		this.report.setGeneratedFileNames(generatedFileNames);
		double p = 0.0D;
		for (CodeGenerator cg : codeGenerators) {
			pmdr.update(this.guiBundle.getResourceText("generation.process.generating.by.label",
					cg.getClass().getSimpleName()));
			p = this.incProgressMonitor(pmdr, p, progressStep);
			if (cg.isTemporarilySuspended()) {
				continue;
			}
			if (cg.getType() == CodeGeneratorType.MODEL) {
				cg.setUnchangedByTagFileInfo(unchangedByTagChecker);
				try {
					result = cg.generate(this.model, ip, this.sourceFileWriter, this.sourceFileReader, this.report,
							postGeneratingCommands) && result;
				} catch (Exception e) {
					this.fireExceptionDetected(e);
					return;
				}
			}
		}
		for (CodeGenerator cg : codeGenerators) {
			pmdr.update(this.guiBundle.getResourceText("generation.process.generating.by.label",
					cg.getClass().getSimpleName()));
			p = this.incProgressMonitor(pmdr, p, progressStep * this.model.getTables().length);
			if (cg.isTemporarilySuspended()) {
				continue;
			}
			if (cg.getType() == CodeGeneratorType.DOMAIN) {
				cg.setUnchangedByTagFileInfo(unchangedByTagChecker);
				try {
					result = cg.generate(this.model, ip, this.sourceFileWriter, this.sourceFileReader, this.report,
							postGeneratingCommands) && result;
				} catch (Exception e) {
					this.fireExceptionDetected(e);
					return;
				}
			}
		}
		for (CodeGenerator cg : codeGenerators) {
			pmdr.update(this.guiBundle.getResourceText("generation.process.generating.by.label",
					cg.getClass().getSimpleName()));
			p = this.incProgressMonitor(pmdr, p, progressStep * this.model.getTables().length);
			if (cg.isTemporarilySuspended()) {
				continue;
			}
			if (cg.getType() == CodeGeneratorType.PANEL) {
				cg.setUnchangedByTagFileInfo(unchangedByTagChecker);
				try {
					result = cg.generate(this.model, ip, this.sourceFileWriter, this.sourceFileReader, this.report,
							postGeneratingCommands) && result;
				} catch (Exception e) {
					this.fireExceptionDetected(e);
					return;
				}
			}
		}
		for (CodeGenerator cg : codeGenerators) {
			pmdr.update(this.guiBundle.getResourceText("generation.process.generating.by.label",
					cg.getClass().getSimpleName()));
			p = this.incProgressMonitor(pmdr, p, progressStep * this.model.getTables().length);
			if (cg.isTemporarilySuspended()) {
				continue;
			}
			if (cg.getType() == CodeGeneratorType.TABLE) {
				cg.setUnchangedByTagFileInfo(unchangedByTagChecker);
				try {
					result = cg.generate(this.model, ip, this.sourceFileWriter, this.sourceFileReader, this.report,
							postGeneratingCommands) && result;
				} catch (Exception e) {
					this.fireExceptionDetected(e);
					return;
				}
			}
		}
		pmdr.update(90);
		pmdr.update(this.guiBundle.getResourceText("generation.process.command.processing.label"));
		System.out.println("found post generation commands: " + postGeneratingCommands.size());
		if (result && (postGeneratingCommands.size() > 0)) {
			int cnt = postGeneratingCommands.size();
			while (postGeneratingCommands.size() > 0) {
				result = postGeneratingCommands.get(0).process(this.model, ip, this.sourceFileWriter,
						this.sourceFileReader, this.report, postGeneratingCommands);
				p = this.incProgressMonitor(pmdr, p, (progressStep * 10.0) / cnt);
				pmdr.update(this.guiBundle.getResourceText("generation.process.command.processed.label",
						postGeneratingCommands.get(0)));
				System.out.println("post generating command processed: " + postGeneratingCommands.get(0));
				postGeneratingCommands.remove(0);
			}
		}
		pmdr.update(95);
		pmdr.update(this.guiBundle.getResourceText("generation.process.updating.resource.label"));
		if (this.codeGeneratorListFactory.getResourceUpdater() != null) {
			this.codeGeneratorListFactory.getResourceUpdater().updateResources(this.report.getResourceIds(), ip,
					this.model);
		}
		p = this.incProgressMonitor(pmdr, p, (progressStep * 10.0));
		pmdr.update(this.guiBundle.getResourceText("generation.process.refinishing.label"));
		if (result) {
			int cnt = this.getTablesToGenerate().length;
			for (TableModel tm : this.getTablesToGenerate()) {
				p = this.incProgressMonitor(pmdr, p, (progressStep * 10.0) / cnt);
				pmdr.update(this.guiBundle.getResourceText("generation.process.finished.label", tm.getName()));
				tm.setFirstGenerationDone(true);
			}
		}
		pmdr.update(this.guiBundle.getResourceText("generation.process.double.file.check.label"));
		if (result) {
			DoubleClassesChecker c = new DoubleClassesChecker();
			String path = FileUtil.completePath(ip.getBaseCodePath()) + this.model.getBasicCodePath();
			System.out.println("for path: " + path);
			List<String> fileNames = FileUtil.getFilenames(path);
			int cnt = fileNames.size();
			for (String fileName : fileNames) {
				String fnlc = fileName.toLowerCase().replace("\\", "/");
				if (!fnlc.endsWith("java") || fnlc.endsWith("package-info.java") || fnlc.contains("/test/")) {
					continue;
				}
				p = this.incProgressMonitor(pmdr, p, (progressStep * 10.0) / cnt);
				c.addPath(fileName);
			}
			this.report.setDoubleClassesChecker(c);
		}
		pmdr.update(100);
		pmdr.close();
		if (!testMode) {
			this.report.setSuccess(result);
			new CodeGeneratorReportHTMLViewer(guiBundle, this.report, Version.INSTANCE.getVersion());
		}
		this.result = result;
		this.fireCodeFactoryEvent(
				new CodeFactoryEvent(CodeFactoryEventType.GENERATION_FINISHED, ip.getBaseCodePathes(), this));
	}

	private IndividualPreferences checkForIndividualPreferences(String basePath, String configFilePath) {
		IndividualPreferences ip = new IndividualPreferences(basePath, "COMPANYNAME", "USER NAME", "USR");
		if (this.fileExistenceChecker.exists(configFilePath)) {
			try {
				IndividualPreferencesReader baseCodePathReader = new FileBaseCodePathReader(configFilePath);
				if (this.individualPreferencesReader != null) {
					baseCodePathReader = this.individualPreferencesReader;
				}
				ip = baseCodePathReader.read(basePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ip;
	}

	private double computeSingleProgressStep(int generatorCount, DataModel dm) {
		int sum = generatorCount * 3 * dm.getTables().length; // PANEL & TABLE & DOMAINS
		sum += generatorCount; // MODEL
		sum += 10; // Post generatings.
		sum += 10; // Resource update.
		sum += 10; // Set first generation done.
		sum += 10; // Double class check.
		return 100.0D / (double) sum;
	}

	private double incProgressMonitor(ProgressMonitorDialogRunner pmdr, double p, double progressStep) {
		p += progressStep;
		pmdr.update((int) (p + .5));
		return p;
	}

	private UnchangedByTagChecker createUnchangedByTagChecker(IndividualPreferences ip,
			ProgressMonitorDialogRunner pmdr, String projectName, List<String> generatedFileNames) {
		String path = FileUtil.completePath(ip.getBaseCodePath(projectName)) + this.model.getBasicCodePath();
		pmdr.update(this.guiBundle.getResourceText("generator.preparation.loading.file.info"));
		System.out.println("reading unchanged files from: " + path);
		List<String> fns = FileUtil.getFilenames(path);
		double p = 5.0 / (fns.size() * 2);
		int i = 0;
		for (String fn : fns) {
			File f = new File(fn);
			pmdr.update(this.guiBundle.getResourceText("generator.preparation.checking.file", fn));
			if (f.exists() && fn.toLowerCase().endsWith(".java")) {
				try {
					String c = FileUtil.readTextFromFile(fn);
					if ((c.contains("GENERATED AUTOMATICALLY !!!") || c.contains(" - Generated."))
							&& !c.contains("MANUALLY_MAINTAINED")) {
						fn = fn.replace("\\", "/");
						if (!generatedFileNames.contains(fn)) {
							generatedFileNames.add(fn);
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			pmdr.update((int) (((double) i++) * p + .5));
		}
		return new DefaultUnchangedByTagChecker();
	}

	private TableModel[] getTablesToGenerate() {
		List<TableModel> tms = new SortedVector<TableModel>();
		for (TableModel tm : this.model.getTables()) {
			if (!tm.isExternalTable() && !tm.isOptionSet(TableParamIds.NO_CODE_GENERATION)
					&& !tm.isStereotype(this.getExclusionStereoTypeName())) {
				tms.add(tm);
			}
		}
		return tms.toArray(new TableModel[0]);
	}

	private ProgressMonitorDialogRunner createProgressMonitorDialogRunner() {
		ProgressMonitorDialogRunner pmdr = new ProgressMonitorDialogRunner(null,
				this.guiBundle.getResourceText("generation.progress.label"), "-");
		while (!pmdr.isReady()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}
		return pmdr;
	}

	/**
	 * Sets the passed data model for the code factory.
	 *
	 * @param dataModel The data model to set for the code factory.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 24.05.2016 - Added.
	 */
	@Override
	public void setDataModel(DataModel dataModel) {
		ensure(dataModel != null, "data model cannot be null.");
		this.model = dataModel;
	}

	/**
	 * @changed OLI 31.03.2016 - Added.
	 */
	@Override
	public void setGUIBundle(GUIBundle guiBundle) {
		this.guiBundle = guiBundle;
	}

	/**
	 * Sets an new individual preference reader (usually used in tests only.
	 *
	 * @param individualPreferencesReader The new individual preference reader for the code factory.
	 * 
	 * @changed OLI 31.03.2016 - Added.
	 */
	public void setIndividualPreferencesReader(IndividualPreferencesReader individualPreferencesReader) {
		this.individualPreferencesReader = individualPreferencesReader;
	}

	/**
	 * @changed OLI 15.06.2016 - Added.
	 */
	@Override
	public void setModelCheckerMessageListFrameListeners(ModelCheckerMessageListFrameListener... listeners) {
		this.modelCheckerMessagelisteners = listeners;
	}

}