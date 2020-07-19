/*
 * DomainUtil.java
 *
 * 25.06.2015
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.acf.util;

import archimedes.acf.*;
import archimedes.acf.param.*;
import archimedes.model.*;


/**
 * A class with utility methods for domains.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 25.06.2015 - Added.
 */

public class DomainUtil extends ParameterUtilOnwer {

    /**
     * Creates a new domain util with the passed parameters.
     *
     * @param parameterUtil A utility class for working with parameters.
     *
     * @changed OLI 25.06.2015 - Added.
     */
    public DomainUtil(ParameterUtil parameterUtil) {
        super(parameterUtil);
    }

    /**
     * Checks if a domain is an enum domain.
     *
     * @param domain The domain to check.
     * @return <CODE>true</CODE> if the domain is an enum domain, <CODE>false</CODE> otherwise.
     *
     * @changed OLI 25.06.2015 - Added.
     */
    public boolean isEnumDomain(DomainModel domain) {
        return this.getEnumTypeName(domain) != null;
    }

    /**
     * Returns the enum type name for the domain model.
     *
     * @param domain The domain to get the enum type name.
     * @return The enum type name for the domain or <CODE>null</CODE> if the domain is no enum.
     *
     * @changed OLI 25.06.2015 - Added.
     */
    public String getEnumTypeName(DomainModel domain) {
        String p = this.parameterUtil.getParameterStartsWith(DomainParamIds.ENUM, domain);
        if (p != null) {
            return p.substring(p.indexOf(":")+1).trim();
        }
        return null;
    }

}