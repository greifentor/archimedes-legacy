/*
 * BaccaraModelCheckerInheritedTablesExists.java
 *
 * 24.05.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.acf.checkers;

import static corentx.util.Checks.*;

import baccara.acf.*;
import baccara.gui.*;

import java.util.*;

import archimedes.acf.checker.*;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.OptionModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.*;


/**
 * Checks if tables which are referenced by table parameters are valid.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 24.05.2016 - Added.
 */

public class BaccaraModelCheckerParameterTableReferences implements ModelChecker {

    private static final String RES_MODEL_CHECKER_PARAMETER_TABLE_REFERENCE_INVALID =
            "ModelChecker.parameter.table.reference.invalid";

    private GUIBundle guiBundle = null;

    /**
     * Creates a new model checker with the passed parameters.
     *
     * @param guiBundle A GUI bundle e. g. for text resources.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 24.05.2016 - Added.
     */
    public BaccaraModelCheckerParameterTableReferences(GUIBundle guiBundle) {
        super();
        ensure(guiBundle != null, "GUI bundle cannot be null.");
        this.guiBundle = guiBundle;
    }

    /**
     * @changed OLI 24.05.2016 - Added.
     */
    @Override public ModelCheckerMessage[] check(DataModel model) {
        List<ModelCheckerMessage> l = new LinkedList<ModelCheckerMessage>();
        for (TableModel t : model.getTables()) {
            this.checkAndRaiseError(t, TableParamIds.INCLUDE_LIST, model, l);
            this.checkAndRaiseError(t, TableParamIds.INCLUDE_SET, model, l);
            this.checkAndRaiseError(t, TableParamIds.INHERITS, model, l);
        }
        return l.toArray(new ModelCheckerMessage[0]);
    }

    private void checkAndRaiseError(TableModel tm, String key, DataModel dm,
            List<ModelCheckerMessage> messages) {
        for (OptionModel o : tm.getOptions()) {
            if (o.getName().equals(key)) {
                if (dm.getTableByName(o.getParameter()) == null) { 
                    messages.add(new ModelCheckerMessage(ModelCheckerMessage.Level.ERROR,
                            this.guiBundle.getResourceText(
                            RES_MODEL_CHECKER_PARAMETER_TABLE_REFERENCE_INVALID, tm.getName(),
                            o.getParameter(), key), tm));
                }
            }
        }
    }

}