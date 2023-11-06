package archimedes.codegenerators.gui.vaadin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.FileManipulator;
import archimedes.codegenerators.NameGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;

public class LabelPropertiesGenerator extends AbstractModelCodeGenerator<NameGenerator> implements FileManipulator {

	public static final String ALTERNATIVE_LABEL_MODULE_NAME = "ALTERNATIVE_LABEL_MODULE_NAME";

	private static final Logger LOG = LogManager.getLogger(LabelPropertiesGenerator.class);

	private static Map<String, String> labels = new HashMap<>();

	public static void addLabel(String id, String content) {
		labels.put(id, content);
	}

	public static List<String> getLabels() {
		return labels
				.entrySet()
				.stream()
				.map(e -> e.getKey() + " - " + e.getValue())
				.sorted()
				.collect(Collectors.toList());
	}

	public LabelPropertiesGenerator(AbstractCodeFactory codeFactory) {
		super("", "", NameGenerator.INSTANCE, TypeGenerator.INSTANCE, codeFactory);
		addLabel("commons.button.add.text", "Anfügen");
		addLabel("commons.button.back.text", "Zurück");
		addLabel("commons.button.duplicate.text", "Duplizieren");
		addLabel("commons.button.edit.text", "Bearbeiten");
		addLabel("commons.button.logout.text", "Logout");
		addLabel("commons.button.remove.text", "Löschen");
		addLabel("commons.button.save.text", "Speichern");
	}

	@Override
	public String getSourceFileName(String path, DataModel dataModel, DataModel t) {
		return getLabelFilePathName(path, dataModel, t) + SLASH + getSourceFileName(dataModel);
	}

	public String getLabelFilePathName(String path, DataModel dataModel, DataModel t) {
		return path + SLASH + getBaseResourceFolderName(dataModel) + SLASH + "localization";
	}

	private String getSourceFileName(DataModel model) {
		return getAppResourcePrefix(model) + "_resources_de.properties";
	}

	private String getAppResourcePrefix(DataModel model) {
		return model.getApplicationName().toLowerCase().replace(" ", "-");
	}

	@Override
	public String getClassName(DataModel model, DataModel t) {
		return "";
	}

	@Override
	public String getPackageName(DataModel model, DataModel t) {
		return "";
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return !dataModel.isOptionSet(ALTERNATIVE_LABEL_MODULE_NAME)
				? "gui-web"
				: dataModel.getOptionByName(ALTERNATIVE_LABEL_MODULE_NAME).getParameter();
	}

	@Override
	public String generate(String path, DataModel model) {
		// LabelPropertiesGenerator.getLabels().forEach(System.out::println);
		return toString(updateLabelFileContent(getLabelFileContent(path, model)));
	}

	private Properties getLabelFileContent(String pathName, DataModel dataModel) {
		File path = new File(getLabelFilePathName(pathName, dataModel, dataModel));
		if (!path.exists()) {
			path.mkdirs();
		}
		String fileName = getSourceFileName(pathName, dataModel, dataModel);
		File file = new File(fileName);
		Properties labelFileContent = new Properties();
		if (file.exists()) {
			try {
				labelFileContent.load(new FileReader(fileName));
			} catch (IOException ioe) {
				LOG
						.error(
								"error while reading label file content - starting with empty file.",
								dataModel.getName(),
								"LabelPropertiesGenerator",
								fileName);
			}
		}
		return labelFileContent;
	}

	private Properties updateLabelFileContent(Properties labelFileContent) {
		for (String key : labels.keySet()) {
			String label = labels.get(key);
			if (!labelFileContent.containsKey(key)) {
				labelFileContent.setProperty(key, label);
			}
		}
		return labelFileContent;
	}

	private String toString(Properties labelFileContent) {
		List<String> labels = new ArrayList<>();
		for (Object key : labelFileContent.keySet()) {
			labels.add(key + "=" + replaceSpecialCharacters(labelFileContent.getOrDefault(key, "TODO")));
		}
		return labels.stream().sorted((s0, s1) -> s0.compareTo(s1)).reduce((s0, s1) -> s0 + "\n" + s1).orElse("");
	}

	private String replaceSpecialCharacters(Object o) {
		if (o == null) {
			return null;
		}
		String s = o.toString();
		s = s.replace("Ä", "\\u00c4");
		s = s.replace("Ö", "\\u00d6");
		s = s.replace("Ü", "\\u00dc");
		s = s.replace("ß", "\\u00df");
		s = s.replace("ä", "\\u00e4");
		s = s.replace("ö", "\\u00f6");
		s = s.replace("ü", "\\u00fc");
		return s;
	}

	@Override
	protected boolean isOverrideAlways() {
		return true;
	}

}
