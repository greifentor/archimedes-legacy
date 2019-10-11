/*
 * UniqueFormulaUtil.java
 *
 * 30.03.2012
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.util;


import static corentx.util.Checks.*;

import java.util.*;


/**
 * This class helps to work with unique formulas.
 *
 * @author ollie
 *
 * @changed OLI 30.03.2012 - Added.
 */

public class UniqueFormulaUtil {

    private String uniqueFormula = null;

    /**
     * Creates a new util instance for the passed unique formula.
     *
     * @param uniqueFormula The unique formula which is to work with.
     * @throws IllegalArgumentException In case of passing a null pointer as unique formula.
     *
     * @changed OLI 30.03.2012 - Added.
     */
    public UniqueFormulaUtil(String uniqueFormula) throws IllegalArgumentException {
        super();
        ensure(uniqueFormula != null, "unique formula cannot be null.");
        this.uniqueFormula = uniqueFormula;
    }

    /**
     * Returns the field names which are contained by the unique formula.
     *
     * @return The field names which are contained by the unique formula.
     *
     * @changed OLI 30.03.2012 - Added.
     */
    public String[] getFieldNames() {
        List<String> fieldNames = new Vector<String>();
        StringTokenizer st = new StringTokenizer(this.uniqueFormula, " &");
        if (st.hasMoreTokens()) {
            while (st.hasMoreTokens()) {
                fieldNames.add(st.nextToken());
            }
        }
        return fieldNames.toArray(new String[0]);
    }

}