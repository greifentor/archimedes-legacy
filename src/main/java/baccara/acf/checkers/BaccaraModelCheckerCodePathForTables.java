/*
 * BaccaraModelCheckerCodePathForTables.java
 *
 * 18.05.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.acf.checkers;


import static corentx.util.Checks.*;

import archimedes.acf.checker.*;
import archimedes.model.*;

import baccara.gui.*;

import java.util.*;


/**
 * Checks the code path for all tables.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 18.05.2016 - Added.
 */

public class BaccaraModelCheckerCodePathForTables implements ModelChecker {

    private static final String RES_MODEL_CHECKER_CODE_FOLDER_IS_A_DOT
            = "ModelChecker.code.folder.is.a.dot.text";

    private GUIBundle guiBundle = null;

    /**
     * Creates a new model checker with the passed parameters.
     *
     * @param guiBundle A GUI bundle e. g. for text resources.
     *
     * @changed OLI 18.05.2016 - Added.
     */
    public BaccaraModelCheckerCodePathForTables(GUIBundle guiBundle) {
        super();
        ensure(guiBundle != null, "GUI bundle cannot be null.");
        this.guiBundle = guiBundle;
    }

    /**
     * @changed OLI 18.05.2016 - Added.
     */
    @Override public ModelCheckerMessage[] check(DataModel model) {
        List<ModelCheckerMessage> l = new LinkedList<ModelCheckerMessage>();
        for (TableModel t : model.getTables()) {
            if (t.getCodeFolder().equals(".")) {
                l.add(new ModelCheckerMessage(ModelCheckerMessage.Level.ERROR,
                        this.guiBundle.getResourceText(RES_MODEL_CHECKER_CODE_FOLDER_IS_A_DOT,
                        t.getName()), t));
            }
        }
        return l.toArray(new ModelCheckerMessage[0]);
    }

}