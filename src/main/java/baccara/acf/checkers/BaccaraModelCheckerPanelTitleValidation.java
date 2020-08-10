/*
 * BaccaraModelCheckerPanelTitleValidation.java
 *
 * 22.07.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.acf.checkers;

import static corentx.util.Checks.*;

import java.util.*;

import archimedes.acf.checker.*;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.PanelModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.*;
import baccara.acf.*;
import baccara.gui.*;

/**
 * A Baccara model checker which checks the validity of panel titles.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 22.07.2016 - Added.
 */

public class BaccaraModelCheckerPanelTitleValidation implements ModelChecker {

    private static final String RES_MODEL_CHECKER_PANEL_TITLE_CONTAINS_INVALID_CHARACTER =
            "ModelChecker.panels.panelTitleContainsInvalidCharacter";

    private GUIBundle guiBundle = null;

    /**
     * Creates a new model checker with the passed parameters.
     *
     * @param guiBundle A GUI bundle e. g. for text resources.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 22.07.2016 - Added.
     */
    public BaccaraModelCheckerPanelTitleValidation(GUIBundle guiBundle) {
        super();
        ensure(guiBundle != null, "GUI bundle cannot be null.");
        this.guiBundle = guiBundle;
    }

    /**
     * @changed OLI 22.07.2016 - Added.
     */
    @Override public ModelCheckerMessage[] check(DataModel model) {
        List<ModelCheckerMessage> messages = new LinkedList<ModelCheckerMessage>();
        for (TableModel tm : model.getTables()) {
            for (PanelModel pm : tm.getPanels()) {
                String t = pm.getTabTitle();
                for (String ic : new String[] {".", "-"}) {
                    if (t.contains(ic)) {
                        messages.add(new ModelCheckerMessage(ModelCheckerMessage.Level.ERROR,
                                this.guiBundle.getResourceText(
                                RES_MODEL_CHECKER_PANEL_TITLE_CONTAINS_INVALID_CHARACTER,
                                tm.getName(), pm.getTabTitle(), ic), tm));
                    }
                }
            }
        }
        return messages.toArray(new ModelCheckerMessage[0]);
    }

}
