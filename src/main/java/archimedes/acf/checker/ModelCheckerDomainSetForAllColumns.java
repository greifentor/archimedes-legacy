/*
 * ModelCheckerDomainSetForAllColumns.java
 *
 * 08.12.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.checker;

import static corentx.util.Checks.*;

import java.util.*;

import archimedes.model.*;
import baccara.gui.*;

/**
 * A model checker implementation for set domains in columns.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.12.2016 - Added.
 */

public class ModelCheckerDomainSetForAllColumns implements ModelChecker {

    public static final String RES_MODEL_CHECKER_COLUMN_WITHOUT_A_DOMAIN_LABEL
            = "ModelChecker.ColumnWithoutADomain.label";

    private GUIBundle guiBundle = null;

    /**
     * Creates a new model checker with the passed parameters.
     *
     * @param guiBundle A GUI bundle e. g. for text resources.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 08.12.2016 - Added.
     */
    public ModelCheckerDomainSetForAllColumns(GUIBundle guiBundle) {
        super();
        ensure(guiBundle != null, "GUI bundle cannot be null.");
        this.guiBundle = guiBundle;
    }

    /**
     * @changed OLI 08.12.2016 - Added.
     */
    @Override public ModelCheckerMessage[] check(DataModel model) {
        List<ModelCheckerMessage> l = new LinkedList<ModelCheckerMessage>();
        for (TableModel t : model.getTables()) {
            for (ColumnModel c : t.getColumns()) {
                if (c.getDomain() == null) {
                    l.add(new ModelCheckerMessage(ModelCheckerMessage.Level.ERROR,
                            this.guiBundle.getResourceText(
                            RES_MODEL_CHECKER_COLUMN_WITHOUT_A_DOMAIN_LABEL, c.getName(),
                            t.getName()), t));
                }
            }
        }
        return l.toArray(new ModelCheckerMessage[0]);
    }

}