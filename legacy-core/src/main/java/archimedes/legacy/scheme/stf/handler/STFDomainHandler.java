/*
 * STFDomainHandler.java
 *
 * 26.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.handler;


import archimedes.legacy.scheme.stf.*;


/**
 * A class with the basic data for STF access of domains.
 *
 * @author ollie
 *
 * @changed OLI 26.04.2013 - Added.
 */

public class STFDomainHandler extends AbstractSTFHandler {

    public static final String COMMENT = "Kommentar";
    public static final String COUNT = "Anzahl";
    public static final String DATA_TYPE = "Datatype";
    public static final String DOMAIN = "Domain";
    public static final String DOMAINS = "Domains";
    public static final String INITIAL_VALUE = "Initialwert";
    public static final String HISTORY = "History";
    public static final String LENGTH = "Length";
    public static final String NAME = "Name";
    public static final String NKS = "NKS";
    public static final String PARAMETERS = "Parameters";

    /**
     * @changed OLI 26.04.2013 - Added.
     */
    @Override public String getSingleListElementTagId() {
        return DOMAIN;
    }

    /**
     * @changed OLI 26.04.2013 - Added.
     */
    @Override public String getWholeBlockTagId() {
        return DOMAINS;
    }

}