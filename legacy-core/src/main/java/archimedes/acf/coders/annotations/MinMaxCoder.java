/*
 * MinMaxCoder.java
 *
 * 13.09.2017
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.coders.annotations;

import static corentx.util.Checks.*;

import archimedes.acf.param.*;
import archimedes.model.*;

/**
 * A coder for minimum and maximum value annotations.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 13.09.2017 - Added.
 */

public class MinMaxCoder {

    /**
     * Returns the code for the minimum and maximum value annotations.
     *
     * @param c The column model whose minimum and maximum value annotations are to create.
     * @param 
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 13.09.2017 - Added.
     */
    public String getCode(ColumnModel c, boolean externalRef) {
        ensure(c != null, "column to code for cannot be null.");
        String s = "";
        if (externalRef && (c.isNotNull() || c.isPrimaryKey())) {
            s += "@Min(value=1, message=\"{reference.can.not.be.unset}\") "; 
        } else {
            if (c.isOptionSet(ColParamIds.MIN_VALUE)) {
                String v = c.getOptionByName(ColParamIds.MIN_VALUE).getParameter();
    //            boolean noMessage = v.contains(",NO_MESSAGE");
                v = v.replace(",NO_MESSAGE", "");
                s += "@Min(value=" + v + ") ";
    //            s += "@Min(value=" + v + (noMessage ? "" : ", message=\"{"
    //                    + this.getAttributeName(c.getTable()) + "."
    //                    + this.getAttributeName(c) + ".is.to.less}\"") + ") ";
            }
            if (c.isOptionSet(ColParamIds.MAX_VALUE)) {
                String v = c.getOptionByName(ColParamIds.MAX_VALUE).getParameter();
    //            boolean noMessage = v.contains(",NO_MESSAGE");
                v = v.replace(",NO_MESSAGE", "");
                s += "@Max(value=" + v + ") ";
    //            s += "@Max(value=" + v + (noMessage ? "" : ", message=\"{"
    //                    + this.getAttributeName(c.getTable()) + "."
    //                    + this.getAttributeName(c) + ".is.to.great}\"") + ") ";
            }
        }
        return s;
    }

}