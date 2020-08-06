/*
 * SelectionMemberFieldGetter.java
 *
 * 03.08.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.acf.util;

import java.util.*;

import archimedes.model.*;
import corentx.util.*;

/**
 * Gets the column to print related to the references column and the print expression if there
 * is one defined.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 03.08.2016 - Added.
 */

public class SelectionMemberFieldGetter {

    /**
     * Returns the references column related to the column of the selection member and the
     * print expression if there is one defined.
     *
     * @param selectionMember The selection member whose referenced column is to return.
     * @return The references column related to the column of the selection member and the
     *         print expression if there is one defined.
     *
     * @changed OLI 03.08.2016 - Added.
     */
    public ColumnModel getColumn(SelectionMemberModel smm) {
        if (smm != null) {
            if ((smm.getPrintExpression() != null) && !smm.getPrintExpression().isEmpty()) {
                List<String> p = Str.splitToList(smm.getPrintExpression(), ".");
                List<ColumnModel> l = new LinkedList<ColumnModel>();
                ColumnModel c = this.checkPath(smm.getColumn(), l, p);
                if (c == null) {
                    return null;
                }
                for (int i = l.size()-2; i >= 0; i--) {
                    if (l.get(i).getReferencedColumn() == null) {
                        l.remove(i);
                    }
                }
                c = this.createParent(l);
                return c;
            }
        }
        return null;
    }

    private ColumnModel checkPath(ColumnModel c, List<ColumnModel> l, List<String> p) {
        if (c == null) {
            return null;
        }
        if (p.size() == 0) {
            l.add(c);
            return c;
        }
        if (c.getReferencedColumn() != null) {
            l.add(c);
            return this.checkPath(c.getReferencedColumn(), l, p);
        } else {
            String cn = p.get(0);
            p.remove(0);
            l.add(c);
            return this.checkPath(c.getTable().getColumnByName(cn), l, p);
        }
    }

    private ColumnModel createParent(List<ColumnModel> l) {
        if (l.size() > 1) {
            ColumnModel c = l.get(l.size()-1);
            l.remove(l.size()-1);
            return new CompoundColumn(this.createParent(l), c);
        }
        return l.get(0);
    }

}