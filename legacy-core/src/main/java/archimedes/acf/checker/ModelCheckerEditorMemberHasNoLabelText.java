/*
 * ModelCheckerEditorMemberHasNoLabelText.java
 *
 * 08.07.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.checker;

import static corentx.util.Checks.ensure;

import java.util.LinkedList;
import java.util.List;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import baccara.gui.GUIBundle;

/**
 * A checker for editor members without default label.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 08.07.2016 - Added.
 */

public class ModelCheckerEditorMemberHasNoLabelText implements ModelChecker {

    public static final String RES_MODEL_CHECKER_EDITOR_MEMBER_WITHOUT_DEFAULT_LABEL
            = "ModelChecker.Messages.EditorMemberWithoutDefault.label";

    private GUIBundle guiBundle = null;

    /**
     * Creates a new model checker with the passed parameters.
     *
     * @param guiBundle A GUI bundle e. g. for text resources.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 08.07.2016 - Added.
     */
    public ModelCheckerEditorMemberHasNoLabelText(GUIBundle guiBundle) {
        super();
        ensure(guiBundle != null, "GUI bundle cannot be null.");
        this.guiBundle = guiBundle;
    }

    /**
     * @changed OLI 08.07.2016 - Added.
     */
    @Override public ModelCheckerMessage[] check(DataModel model) {
        List<ModelCheckerMessage> l = new LinkedList<ModelCheckerMessage>();
        for (TableModel t : model.getTables()) {
			if (t.isOptionSet(IGNORE_CHECKER_OPTION)) {
				continue;
			}
            for (ColumnModel c : t.getColumns()) {
                if (c.isEditorMember() && ((c.getEditorLabelText() == null)
                        || c.getEditorLabelText().isEmpty())) {
                    l.add(new ModelCheckerMessage(ModelCheckerMessage.Level.ERROR,
                            this.guiBundle.getResourceText(
                            RES_MODEL_CHECKER_EDITOR_MEMBER_WITHOUT_DEFAULT_LABEL, c.getName(),
                            t.getName()), t));
                }
            }
        }
        return l.toArray(new ModelCheckerMessage[0]);
    }

}