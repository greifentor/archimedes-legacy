/*
 * IndividualPreferences.java
 *
 * 18.04.2013
 *
 * (c) by O.Lieshoff
 *
 */

package gengen;


import static corentx.util.Checks.ensure;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import corentx.util.SortedVector;


/**
 * A container for the individual preferences for the code factory.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.04.2013 - Added.
 */

public class IndividualPreferences {

    private Map<String, String> alternateBaseCodePathes = new Hashtable<String, String>();
    private String baseCodePath = null;
    private String companyName = null;
    private String userName = null;
    private String userToken = null;

    /**
     * Creates new individual preferences with the passed parameters.
     *
     * @param baseCodePath The base code path for the individual preferences.
     * @param companyName The name of the company for which the code is to create.
     * @param userName The name of the user which started the generation process.
     * @param userToken The token of the user which started the generation process.
     *
     * @changed OLI 18.04.2013 - Added.
     */
    public IndividualPreferences(String baseCodePath, String companyName, String userName,
            String userToken) {
        super();
        ensure(baseCodePath != null, "base code path cannot be null.");
        ensure(!baseCodePath.isEmpty(), "base code path cannot be empty.");
        ensure(companyName != null, "company name cannot be null.");
        ensure(!companyName.isEmpty(), "company name cannot be empty.");
        ensure(userName != null, "user name cannot be null.");
        ensure(!userName.isEmpty(), "user name cannot be empty.");
        ensure(userToken != null, "user token cannot be null.");
        ensure(!userToken.isEmpty(), "user token cannot be empty.");
        this.baseCodePath = baseCodePath;
        this.companyName = companyName;
        this.userName = userName;
        this.userToken = userToken;
    }

    /**
     * Adds the passed alternate base code path for the project with passed token.
     *
     * @param projectToken The token of the project which the alternate base code path is to
     *         add for.
     * @param alternateBaseCodePath The alternate base code path for the passed project.
     *
     * @changed OLI 10.07.2014 - Added.
     */
    public void addAlternateBaseCodePath(String projectToken, String alternateBaseCodePath) {
        this.alternateBaseCodePathes.put(projectToken.toUpperCase(), alternateBaseCodePath);
    }

    /**
     * Removes all alternate pathes.
     *
     * @changed OLI 10.07.2014 - Added.
     */
    public void clearAlternateBaseCodePathes() {
        this.alternateBaseCodePathes.clear();
    }

    /**
     * @changed OLI 18.04.2013 - Added.
     */
    @Override public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, false);
    }

    /**
     * @changed OLI 18.04.2013 - Added.
     */
    @Override public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    /**
     * Returns a list with the tokens which the preferences have an alternate path configured.
     *
     * @return A list with the tokens which the preferences have an alternate path configured.
     *
     * @changed OLI 10.07.2014 - Added.
     */
    public String[] getBaseCodePathAlternateTokens() {
        List<String> l = new SortedVector<String>();
        for (String token : this.alternateBaseCodePathes.keySet().toArray(new String[0])) {
            l.add(token);
        }
        return l.toArray(new String[0]);
    }

    /**
     * Returns the base code path of the individual preferences.
     *
     * @return The base code path of the individual preferences.
     *
     * @changed OLI 18.04.2013 - Added.
     */
    public String getBaseCodePath() {
        return this.baseCodePath;
    }

    /**
     * Returns the alternate base code path of the individual preferences for the passed
     * project. 
     *
     * @param projectToken The token of the project which the alternate base code path is to
     *         return for. 
     * @return An alternate base code path of the individual preferences for the passed project
     *         or the general base code path.
     *
     * @changed OLI 10.07.2014 - Added.
     */
    public String getBaseCodePath(String projectToken) {
        String p = this.alternateBaseCodePathes.get(projectToken.toUpperCase());
        if (p == null) {
            p = this.baseCodePath;
        }
        return p;
    }

    /**
     * Returns the all base code path of the individual preferences (separated by ";" and
     * alternate pathes with prefix {token}+"=").
     *
     * @return The all base code path of the individual preferences (separated by ";" and
     *         alternate pathes with prefix {token}+"=").
     *
     * @changed OLI 18.04.2013 - Added.
     */
    public String getBaseCodePathes() {
        String s = this.baseCodePath;
        for (String token : this.alternateBaseCodePathes.keySet().toArray(new String[0])) {
            s += ";" + token + "=" + this.alternateBaseCodePathes.get(token);
        }
        return s;
    }

    /**
     * Returns the name of the company for which the code is to create.
     *
     * @return The name of the company for which the code is to create.
     *
     * @changed OLI 18.04.2013 - Added.
     */
    public String getCompanyName() {
        return this.companyName;
    }

    /**
     * Returns the name of the user which started the generation process.
     *
     * @return The name of the user which started the generation process.
     *
     * @changed OLI 18.04.2013 - Added.
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Returns the token of the user which started the generation process.
     *
     * @return The token of the user which started the generation process.
     *
     * @changed OLI 18.04.2013 - Added.
     */
    public String getUserToken() {
        return this.userToken;
    }

    /**
     * Sets the passed string as new base code path.
     *
     * @param baseCodePath The new base code path.
     *
     * @changed OLI 10.07.2014 - Added.
     */
    public void setBaseCodePath(String baseCodePath) {
        this.baseCodePath = baseCodePath;
    }

    /**
     * Sets the passed string as new company name.
     *
     * @param companyName The new company name.
     *
     * @changed OLI 10.07.2014 - Added.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Sets the passed string as new user name.
     *
     * @param userName The new user name.
     *
     * @changed OLI 10.07.2014 - Added.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Sets the passed string as new user token.
     *
     * @param userToken The new user token.
     *
     * @changed OLI 10.07.2014 - Added.
     */
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    /**
     * @changed OLI 18.04.2013 - Added.
     */
    @Override public String toString() {
        return "BaseCodePath=" + this.baseCodePath + ",CompanyName=" + this.companyName
                + ",UserName=" + this.userName + ",UserToken=" + this.userToken;
    }

}