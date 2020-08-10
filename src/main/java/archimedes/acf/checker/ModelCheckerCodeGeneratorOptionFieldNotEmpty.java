/*
 * ModelCheckerCodeGeneratorOptionFieldNotEmpty.java
 *
 * 27.05.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.checker;

import static corentx.util.Checks.ensure;

import java.util.LinkedList;
import java.util.List;

import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.TableModel;
import baccara.gui.GUIBundle;

/**
 * A model checker which checks if a code generator field in a table model.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 27.05.2016 - Added.
 */

public class ModelCheckerCodeGeneratorOptionFieldNotEmpty implements ModelChecker {

	public static final String RES_MODEL_CHECKER_GENERATE_CODE_OPTIONS_FIELD_IS_SET = "ModelChecker.generate.code.options.field.is.set";

	private GUIBundle guiBundle = null;

	/**
	 * Creates a new model checker with the passed parameters.
	 *
	 * @param guiBundle A GUI bundle e. g. for text resources.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 24.05.2016 - Added.
	 */
	public ModelCheckerCodeGeneratorOptionFieldNotEmpty(GUIBundle guiBundle) {
		super();
		ensure(guiBundle != null, "GUI bundle cannot be null.");
		this.guiBundle = guiBundle;
	}

	/**
	 * @changed OLI 24.05.2016 - Added.
	 */
	@Override
	public ModelCheckerMessage[] check(DataModel model) {
		List<ModelCheckerMessage> l = new LinkedList<ModelCheckerMessage>();
		for (TableModel t : model.getTables()) {
			if ((t.getGenerateCodeOptions() != null) && !t.getGenerateCodeOptions().isEmpty()) {
				l.add(new ModelCheckerMessage(ModelCheckerMessage.Level.WARNING, this.guiBundle
						.getResourceText(RES_MODEL_CHECKER_GENERATE_CODE_OPTIONS_FIELD_IS_SET, t.getName()), t));
			}
		}
		return l.toArray(new ModelCheckerMessage[0]);
	}

}