/*
 * BaccaraModelCheckerParameterColumnReferences.java
 *
 * 24.05.2016
 *
 * (c) by HealthCarion
 *
 */

package baccara.acf.checkers;

import static corentx.util.Checks.*;

import baccara.gui.*;

import java.util.*;

import archimedes.acf.checker.*;
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.OptionModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.*;


/**
 * A checker which shows invalid table referenced by columns. 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 24.05.2016 - Added.
 */

public class BaccaraModelCheckerParameterColumnReferences implements ModelChecker {

    private static final String RES_MODEL_CHECKER_PARAMETER_COLUMN_REFERENCE_INVALID
            = "ModelChecker.parameter.column.reference.invalid";

    private GUIBundle guiBundle = null;
    private String[] optionKeys = null;

    /**
     * Creates a new model checker with the passed parameters.
     *
     * @param guiBundle A GUI bundle e. g. for text resources.
     * @param optionKeys The keys of the options which should be checked. 
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 24.05.2016 - Added.
     */
    public BaccaraModelCheckerParameterColumnReferences(GUIBundle guiBundle, 
            String... optionKeys) {
        super();
        ensure(guiBundle != null, "GUI bundle cannot be null.");
        ensure(optionKeys != null, "option keys to check cannot be null.");
        this.guiBundle = guiBundle;
        this.optionKeys = optionKeys;
    }

    /**
     * @changed OLI 24.05.2016 - Added.
     */
    @Override public ModelCheckerMessage[] check(DataModel model) {
        List<ModelCheckerMessage> l = new LinkedList<ModelCheckerMessage>();
        for (TableModel t : model.getTables()) {
            for (String key : this.optionKeys) {
                this.checkAndRaiseError(t, key, model, l);
            }
        }
        return l.toArray(new ModelCheckerMessage[0]);
    }

    private void checkAndRaiseError(TableModel tm, String key, DataModel dm,
            List<ModelCheckerMessage> messages) {
        for (ColumnModel c : tm.getColumns()) {
            for (OptionModel o : c.getOptions()) {
                if (o.getName().equals(key)) {
                    if (dm.getTableByName(o.getParameter()) == null) { 
                        messages.add(new ModelCheckerMessage(ModelCheckerMessage.Level.ERROR,
                                this.guiBundle.getResourceText(
                                RES_MODEL_CHECKER_PARAMETER_COLUMN_REFERENCE_INVALID,
                                c.getName(), c.getTable().getName(), o.getParameter(), key), tm)
                                );
                    }
                }
            }
        }
    }

}