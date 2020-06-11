/*
 * ModelCheckerTableNameDoesNotContainSpecialCharacters.java
 *
 * 09.12.2016
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
 * A model checker which checks table names for special characters.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 09.12.2016 - Added.
 */

public class ModelCheckerTableNameDoesNotContainSpecialCharacters implements ModelChecker {

    public static final String RES_MODEL_CHECKER_COLUMN_WITHOUT_A_DOMAIN_LABEL
            = "ModelChecker.TableNameContainsASpecialCharacter.label";

    private GUIBundle guiBundle = null;

    /**
     * Creates a new model checker with the passed parameters.
     *
     * @param guiBundle A GUI bundle e. g. for text resources.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 09.12.2016 - Added.
     */
    public ModelCheckerTableNameDoesNotContainSpecialCharacters(GUIBundle guiBundle) {
        super();
        ensure(guiBundle != null, "GUI bundle cannot be null.");
        this.guiBundle = guiBundle;
    }

    /**
     * @changed OLI 09.12.2016 - Added.
     */
    @Override public ModelCheckerMessage[] check(DataModel model) {
        List<ModelCheckerMessage> l = new LinkedList<ModelCheckerMessage>();
        for (TableModel t : model.getTables()) {
            for (int i = 0, leni = t.getName().length(); i < leni; i++) {
                char c = t.getName().charAt(i);
                if ((c <= '/') || (":;<=>?@[\\]^`{|}~".indexOf(c) > -1)) {
                    l.add(new ModelCheckerMessage(ModelCheckerMessage.Level.ERROR,
                            this.guiBundle.getResourceText(
                            RES_MODEL_CHECKER_COLUMN_WITHOUT_A_DOMAIN_LABEL, t.getName(),
                            (int) c, i), t));
                }
            }
        }
        return l.toArray(new ModelCheckerMessage[0]);
    }

}