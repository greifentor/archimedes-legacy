package archimedes.legacy.checkers;

import archimedes.acf.checker.ModelChecker;
import archimedes.acf.checker.ModelCheckerMessage;
import archimedes.legacy.scripting.DialogScriptExecuter;
import archimedes.model.DataModel;
import baccara.gui.GUIBundle;
import corentx.script.JScriptRunner;
import logging.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelCheckerByScript implements ModelChecker {

	private static final Logger LOG = Logger.getLogger(DialogScriptExecuter.class);

	private GUIBundle guiBundle;

	public ModelCheckerByScript(GUIBundle guiBundle) {
		super();
		this.guiBundle = guiBundle;
	}

	@Override
	public ModelCheckerMessage[] check(DataModel model) {
		List<ModelCheckerMessage> messages = new ArrayList<>();
		if ((model.getModelCheckerScript() != null) && !model.getModelCheckerScript().isEmpty()) {
			try {
				Map<String, Object> params = new HashMap<>();
				params.put("guiBundle", guiBundle);
				params.put("messages", messages);
				params.put("model", model);
				params.put("tables", Arrays.asList(model.getTables()));
				new JScriptRunner(model.getModelCheckerScript(), params, null).exec();
			} catch (Exception e) {
				LOG.error("while executing model checker script: " + e.getMessage());
			}
		}
		return messages.toArray(new ModelCheckerMessage[messages.size()]);
	}

}
