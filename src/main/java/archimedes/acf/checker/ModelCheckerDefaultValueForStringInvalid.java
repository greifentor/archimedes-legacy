/*
 * ModelCheckerDefaultValueForStringInvalid.java
 *
 * 03.02.2017
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.checker;

import static corentx.util.Checks.ensure;

import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.DomainModel;
import archimedes.legacy.model.TableModel;
import baccara.gui.GUIBundle;

/**
 * A checker for the validity of default values for string fields.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 03.02.2017 - Added.
 */

public class ModelCheckerDefaultValueForStringInvalid implements ModelChecker {

	public static final String RES_MODEL_CHECKER_INVALID_STRING_DEFAULT_LABEL = "ModelChecker.Messages.InvalidStringDefault.label";

	private GUIBundle guiBundle = null;

	/**
	 * Creates a new model checker with the passed parameters.
	 *
	 * @param guiBundle A GUI bundle e. g. for text resources.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 03.02.2017 - Added.
	 */
	public ModelCheckerDefaultValueForStringInvalid(GUIBundle guiBundle) {
		super();
		ensure(guiBundle != null, "GUI bundle cannot be null.");
		this.guiBundle = guiBundle;
	}

	/**
	 * @changed OLI 03.02.2017 - Added.
	 */
	@Override
	public ModelCheckerMessage[] check(DataModel model) {
		List<ModelCheckerMessage> l = new LinkedList<ModelCheckerMessage>();
		for (TableModel t : model.getTables()) {
			for (ColumnModel c : t.getColumns()) {
				if (this.isStringType(c.getDomain())) {
					String dv = c.getDefaultValue();
					if (dv != null) {
						if (!dv.startsWith("'") || !dv.endsWith("'")) {
							l.add(new ModelCheckerMessage(ModelCheckerMessage.Level.ERROR,
									this.guiBundle.getResourceText(RES_MODEL_CHECKER_INVALID_STRING_DEFAULT_LABEL,
											c.getName(), t.getName(), dv),
									t));
						}
					}
				}
			}
		}
		return l.toArray(new ModelCheckerMessage[0]);
	}

	boolean isStringType(DomainModel d) {
		int t = d.getDataType();
		if ((t == Types.CHAR) || (t == Types.LONGNVARCHAR) || (t == Types.LONGVARCHAR) || (t == Types.NCHAR)
				|| (t == Types.NVARCHAR) || (t == Types.VARCHAR)) {
			return true;
		}
		return false;
	}

}