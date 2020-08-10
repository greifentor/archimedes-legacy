/*
 * ModelCheckerNReferencesAreSetProperly.java
 *
 * 10.07.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.checker;

import static corentx.util.Checks.ensure;

import java.util.LinkedList;
import java.util.List;

import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.NReferenceModel;
import archimedes.legacy.model.TableModel;
import baccara.gui.GUIBundle;

/**
 * Checks if the n-references are set properly.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 10.07.2016 - Added.
 */

public class ModelCheckerNReferencesAreSetProperly implements ModelChecker {

	public static final String RES_MODEL_CHECKER_EDITOR_MEMBER_WITHOUT_DEFAULT_LABEL = "ModelChecker.lost.n.reference.label";

	private GUIBundle guiBundle = null;

	/**
	 * Creates a new model checker with the passed parameters.
	 *
	 * @param guiBundle A GUI bundle e. g. for text resources.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 10.07.2016 - Added.
	 */
	public ModelCheckerNReferencesAreSetProperly(GUIBundle guiBundle) {
		super();
		ensure(guiBundle != null, "GUI bundle cannot be null.");
		this.guiBundle = guiBundle;
	}

	/**
	 * @changed OLI 10.07.2016 - Added.
	 */
	@Override
	public ModelCheckerMessage[] check(DataModel model) {
		List<ModelCheckerMessage> l = new LinkedList<ModelCheckerMessage>();
		for (TableModel t : model.getTables()) {
			for (NReferenceModel nm : t.getNReferences()) {
				if ((nm.getColumn() == null) || this.isColumnRemoved(nm.getColumn())) {
					l.add(new ModelCheckerMessage(ModelCheckerMessage.Level.ERROR,
							this.guiBundle.getResourceText(RES_MODEL_CHECKER_EDITOR_MEMBER_WITHOUT_DEFAULT_LABEL,
									nm.getPanel().getTabTitle(), t.getName()),
							t));
				}
			}
		}
		return l.toArray(new ModelCheckerMessage[0]);
	}

	private boolean isColumnRemoved(ColumnModel c) {
		for (ColumnModel c0 : c.getTable().getColumns()) {
			if (c.getFullName().equals(c0.getFullName())) {
				return false;
			}
		}
		return true;
	}

}