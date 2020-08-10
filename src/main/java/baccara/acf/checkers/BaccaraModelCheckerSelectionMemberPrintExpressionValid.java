/*
 * IsisModelCheckerSelectionMemberPrintExpressionValid.java
 *
 * 02.08.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.acf.checkers;

import java.util.*;

import archimedes.acf.checker.*;
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.SelectionMemberModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.*;
import baccara.acf.util.*;
import baccara.gui.*;
import corentx.util.*;


/**
 * Checks if the print expressions of the selection members are valid.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 02.08.2016 - Added.
 */

public class BaccaraModelCheckerSelectionMemberPrintExpressionValid implements ModelChecker {

    protected static final String RES_ISIS_MODEL_CHECKER_PRINT_EXPRESSION_INVALID =
            "ModelChecker.errors.SelectionMembers.PrintExpression.Invalid.message";
    protected static final String
            RES_ISIS_MODEL_CHECKER_PRINT_EXPRESSION_NON_REFERENCE_HAS_EXPRESSION =
            "ModelChecker.errors.SelectionMembers.PrintExpression.NonReferenceFieldHasPrintExpression.message";
    protected static final String
            RES_ISIS_MODEL_CHECKER_PRINT_EXPRESSION_POINTS_TO_EXTERNAL_TABLE =
            "ModelChecker.errors.SelectionMembers.PrintExpression.PointsToExternalTable";

    private GUIBundle guiBundle = null;
    private SelectionMemberFieldGetter selectionMemberFieldGetter =
            new SelectionMemberFieldGetter();

    /**
     * Creates a new model checker with the passed parameters.
     *
     * @param guiBundle A GUI bundle e. g. for text resources.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 02.08.2016 - Added.
     */
    public BaccaraModelCheckerSelectionMemberPrintExpressionValid(GUIBundle guiBundle) {
        super();
        this.guiBundle = guiBundle;
    }

    /**
     * @changed OLI 02.08.2016 - Added..
     */
    @Override public ModelCheckerMessage[] check(DataModel dm) {
        List<ModelCheckerMessage> messages = new LinkedList<ModelCheckerMessage>();
        for (TableModel tm : dm.getTables()) {
            for (SelectionMemberModel smm : tm.getSelectableColumns()) {
                if ((smm.getColumn().getReferencedColumn() == null)
                        && (smm.getPrintExpression() != null)
                        && !smm.getPrintExpression().isEmpty()) {
                    messages.add(new ModelCheckerMessage(ModelCheckerMessage.Level.ERROR,
                            this.guiBundle.getResourceText(
                            RES_ISIS_MODEL_CHECKER_PRINT_EXPRESSION_NON_REFERENCE_HAS_EXPRESSION,
                            smm.getColumn().getName(), tm.getName(), smm.getPrintExpression()),
                            tm));
                }
                if (this.getExternalColumn(smm) != null) {
                    messages.add(new ModelCheckerMessage(ModelCheckerMessage.Level.ERROR,
                            this.guiBundle.getResourceText(
                            RES_ISIS_MODEL_CHECKER_PRINT_EXPRESSION_POINTS_TO_EXTERNAL_TABLE,
                            tm.getName(), smm.getColumn(), this.getExternalColumn(smm).getTable(
                            ), smm.getPrintExpression()), tm));
                }
                if ((this.selectionMemberFieldGetter.getColumn(smm) == null
                        && (smm.getPrintExpression() != null)
                        && !smm.getPrintExpression().isEmpty())) {
                    messages.add(new ModelCheckerMessage(ModelCheckerMessage.Level.ERROR,
                            this.guiBundle.getResourceText(
                            RES_ISIS_MODEL_CHECKER_PRINT_EXPRESSION_INVALID, tm.getName(),
                            smm.getColumn(), smm.getAttribute(), smm.getPrintExpression()), tm)
                            );
                }
            }
        }
        return messages.toArray(new ModelCheckerMessage[0]);
    }

    public ColumnModel getExternalColumn(SelectionMemberModel smm) {
        if ((smm.getPrintExpression() != null) && !smm.getPrintExpression().isEmpty()) {
            List<String> p = Str.splitToList(smm.getPrintExpression(), ".");
            return this.checkPath(smm.getColumn(), p);
        }
        return null;
    }

    private ColumnModel checkPath(ColumnModel c, List<String> p) {
        if (c == null) {
            return null;
        }
        if (c.getTable().isExternalTable()) {
            return c;
        }
        if (p.size() == 0) {
            return null;
        }
        if (c.getReferencedColumn() != null) {
            return this.checkPath(c.getReferencedColumn(), p);
        } else {
            String cn = p.get(0);
            p.remove(0);
            return this.checkPath(c.getTable().getColumnByName(cn), p);
        }
    }

}