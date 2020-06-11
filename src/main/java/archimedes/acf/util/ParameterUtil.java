/*
 * ParameterUtil.java
 *
 * 29.05.2015
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.acf.util;

import archimedes.model.*;

import java.util.*;


/**
 * Utility methods for working with model object parameters (like table or column parameters).
 *
 * @author O.Lieshoff
 *
 * @changed OLI 29.05.2015 - Added.
 */

public class ParameterUtil {

    /**
     * Returns a list with the parameters from the passed column. 
     *
     * @param c The column whose parameters are to return.
     * @return A list with the parameters from the passed column.
     *
     * @changed OLI 10.10.2013 - Added.
     *
     * @deprecated OLI 25.05.2016 - Use "ColumnModel.getOptions()" instead of.
     */
    @Deprecated public List<String> getParameters(ColumnModel c) {
        return this.getParameters(c.getParameters());
    }

    /**
     * Returns a list with the parameters from the passed model. 
     *
     * @param m The model whose parameters are to return.
     * @return A list with the parameters from the passed model.
     *
     * @changed OLI 02.07.2014 - Added.
     */
    public List<String> getParameters(DataModel m) {
        String p = "";
        for (OptionModel o : m.getOptions()) {
            if (p.length() > 0) {
                p += " | ";
            }
            p += o.getName();
            if ((o.getParameter() != null) && !o.getParameter().isEmpty()) {
                p += ": " + o.getParameter();
            }
        }
        return this.getParameters(p);
    }

    /**
     * Returns a list with the parameters from the passed domain. 
     *
     * @param d The domain whose parameters are to return.
     * @return A list with the parameters from the passed domain.
     *
     * @changed OLI 10.10.2013 - Added.
     */
    public List<String> getParameters(DomainModel d) {
        return this.getParameters(d.getParameters());
    }

    /**
     * Returns a list with the parameters from the passed table. 
     *
     * @param t The table whose parameters are to return.
     * @return A list with the parameters from the passed table.
     *
     * @changed OLI 10.10.2013 - Added.
     *
     * @deprecated OLI 25.05.2016 - Use "TableModel.getOptions()" instead of.
     */
    @Deprecated public List<String> getParameters(TableModel t) {
        String p = t.getGenerateCodeOptions();
        for (OptionModel o : t.getOptions()) {
            if (p.length() > 0) {
                p += " | ";
            }
            p += o.getName();
            if ((o.getParameter() != null) && !o.getParameter().isEmpty()) {
                p += ": " + o.getParameter();
            }
        }
        return this.getParameters(p);
    }

    /**
     * Returns a list with the parameters from the passed panel. 
     *
     * @param p The panel whose parameters are to return.
     * @return A list with the parameters from the passed panel.
     *
     * @changed OLI 18.11.2014 - Added.
     */
    public List<String> getParameters(PanelModel p) {
        return this.getParameters(p.getPanelClass());
    }

    /**
     * Returns a list with the parameters from the passed string. 
     *
     * @param s The string whose parameters are to return.
     * @return A list with the parameters from the passed string.
     *
     * @changed OLI 10.10.2013 - Added.
     */
    public List<String> getParameters(String s) {
        List<String> p = new LinkedList<String>();
        StringTokenizer st = new StringTokenizer(s, "|");
        while (st.hasMoreTokens()) {
            p.add(st.nextToken().trim());
        }
        return p;
    }

    /**
     * Returns the first parameter from the column with the passed prefix.
     *
     * @param prefix The prefix which the parameter should have.
     * @param c The column whose parameters are to check.
     * @return The parameter with the passed prefix.
     *
     * @changed OLI 10.10.2013 - Added.
     *
     * @deprecated OLI 25.05.2016 - Use "ColumnModel.getOption(String)" instead of.
     */
    @Deprecated public String getParameterStartsWith(String prefix, ColumnModel c) {
        for (String p : this.getParameters(c)) {
            if (p.startsWith(prefix)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Returns the first parameter from the domain with the passed prefix.
     *
     * @param prefix The prefix which the parameter should have.
     * @param d The domain whose parameters are to check.
     * @return The parameter with the passed prefix.
     *
     * @changed OLI 10.10.2013 - Added.
     */
    public String getParameterStartsWith(String prefix, DomainModel d) {
        for (String p : this.getParameters(d.getParameters())) {
            if (p.startsWith(prefix)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Returns the first parameter from the table with the passed prefix.
     *
     * @param prefix The prefix which the parameter should have.
     * @param tm The table whose parameters are to check.
     * @return The parameter with the passed prefix.
     *
     * @changed OLI 10.10.2013 - Added.
     *
     * @deprecated OLI 25.05.2016 - Use "TableModel.getOption(String)" instead of.
     */
    @Deprecated public String getParameterStartsWith(String prefix, TableModel t) {
        for (String p : this.getParameters(t)) {
            if (p.startsWith(prefix)) {
                return p;
            }
        }
        return null;
    }

    /**
     * @deprecated OLI 25.05.2016 - Use "ColumnModel.getOptionByName(String)" instead of.
     */
    @Deprecated public String getValueParameterStartsWith(String prefix, ColumnModel c) {
        return this.getParameterValue(this.getParameterStartsWith(prefix, c));
    }

    /**
     * @deprecated OLI 25.05.2016 - Use "TableModel.getOptionByName(String)" instead of.
     */
    @Deprecated public String getValueParameterStartsWith(String prefix, TableModel tm) {
        return this.getParameterValue(this.getParameterStartsWith(prefix, tm));
    }

    public String getParameterValue(String p) {
        if (p != null) {
            return p.substring(p.indexOf(":")+1, p.length()).trim();
        }
        return p;
    }

}