/*
 * STFAdditionalSQLCodeReader.java
 *
 * 26.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.reader;


import archimedes.legacy.model.*;
import archimedes.legacy.scheme.stf.handler.*;

import corent.files.*;


/**
 * A reader for the additional SQL code from a STF.
 *
 * @author ollie
 *
 * @changed OLI 26.04.2013 - Added.
 */

public class STFAdditionalSQLCodeReader extends STFAdditionalSQLCodeHandler {

    /**
     * Updates the additional SQL code in the passed data model by the information stored in the
     * STF.
     *
     * @param stf The STF whose additional SQL code should be read to the diagram.
     * @param model The diagram model which is to fill with the additional SQL code.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    public void read(StructuredTextFile stf, DiagrammModel model) {
        model.setAdditionalSQLCodePostChangingCode(fromHTML(stf.readStr(this.createPath(
                SQL_CODE, ADDITIONAL_SQL_CODE_POST_CHANGING), "")));
        model.setAdditionalSQLCodePostReducingCode(fromHTML(stf.readStr(this.createPath(
                SQL_CODE, ADDITIONAL_SQL_CODE_POST_REDUCING), "")));
        if (model.getAdditionalSQLCodePostReducingCode().isEmpty()) {
            model.setAdditionalSQLCodePostReducingCode(fromHTML(stf.readStr(this.createPath(
                    SQL_CODE), "")));
        }
        model.setAdditionalSQLCodePreChangingCode(fromHTML(stf.readStr(this.createPath(
                SQL_CODE, ADDITIONAL_SQL_CODE_PRE_CHANGING), "")));
        model.setAdditionalSQLCodePreExtendingCode(fromHTML(stf.readStr(this.createPath(
                SQL_CODE, ADDITIONAL_SQL_CODE_PRE_EXTENDING), "")));
    }

}