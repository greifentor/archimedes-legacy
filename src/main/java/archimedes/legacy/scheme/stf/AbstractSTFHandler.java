/*
 * AbstractSTFListReader.java
 *
 * 26.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf;


import archimedes.legacy.scheme.stf.reader.*;

import corent.base.*;


/**
 * This class prepares accesses to lists in the STF.
 *
 * @author ollie
 *
 * @changed OLI 26.04.2013 - Added.
 */

abstract public class AbstractSTFHandler {

    /**
     * Returns the identifier of the tag which contains a single list element.
     *
     * @return The identifier of the tag which contains a single list element.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    abstract public String getSingleListElementTagId(); 

    /**
     * Returns the identifier of the tag which contains the whole blocks informations.
     *
     * @return The identifier of the tag which contains the whole blocks informations.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    abstract public String getWholeBlockTagId(); 

    /**
     * Creates an array with the strings to identify a general sequence field.
     *
     * @param arr The id's which are following the id header for sequences.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    public static final String[] createRootPath(String... arr) {
        return concat(new String[] {STFDiagrammReader.DIAGRAMM}, arr);
    }

    /**
     * Creates an array with the strings to identify a general sequence field.
     *
     * @param additionalIds The additional id's which are following the id header for sequences.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    public static final String[] createPath(String[] arr0, String... arr1) {
        return concat(arr0, arr1);
    }

    /**
     * Concats the to passed arrays.
     *
     * @param arr0 The first array to concat.
     * @param arr1 The second array to concat.
     * @returns An arrays with arr0 + arr1.
     *
     * @changed OLI 29.04.2013 - Added.
     */
    public static final String[] concat(String[] arr0, String[] arr1) {
        String[] s = new String[arr0.length+arr1.length];
        for (int i = 0; i < arr0.length; i++) {
            s[i] = arr0[i];
        }
        for (int i = arr0.length; i < arr1.length+arr0.length; i++) {
            s[i] = arr1[i-arr0.length];
        }
        return s;
    }

    /**
     * Creates an array with the strings to identify a general fields.
     *
     * @param additionalIds The additional id's which are following the id header for fields.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    public static String[] createPath(AbstractSTFHandler reader, String... additionalIds) {
        return createPath(new String[] {STFDiagrammReader.DIAGRAMM, reader.getWholeBlockTagId()
                }, additionalIds);
    }

    /**
     * Creates an array with the strings to identify a field of a list element.
     *
     * @param seqId The id of the selected list element.
     * @param additionalIds The additional id's which are following the id header for list
     *         elements.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    public static String[] createPath(AbstractSTFHandler reader, int seqId,
            String... additionalIds) {
        return createPath(createPath(reader, new String[] {reader.getSingleListElementTagId()
                + seqId}), additionalIds);
    }

    /**
     * Creates an array with the strings to identify a general fields.
     *
     * @param additionalIds The additional id's which are following the id header for fields.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    public String[] createPath(String... additionalIds) {
        return createPath(this, additionalIds);
    }

    /**
     * Creates an array with the strings to identify a field of a list element.
     *
     * @param seqId The id of the selected list element.
     * @param additionalIds The additional id's which are following the id header for list
     *         elements.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    public String[] createPath(int seqId, String... additionalIds) {
        return createPath(this, seqId, additionalIds);
    }

    /**
     * Returns a string with the changed special representation for saving in a STF (HTML plus
     * line feed markers).
     *
     * @param s The string which is to change back to the general string representation.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    public static String fromHTML(String s) {
        if (s != null) {
            s = StrUtil.FromHTML(s);
            return s.replace("$BR$", "\n");
        }
        return s;
    }

    /**
     * Returns a string with a special representation for saving in a STF (HTML plus line feed
     * markers).
     *
     * @param s The string which is to change to the special representation.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    public static String toHTML(String s) {
        s = StrUtil.ToHTML(s);
        return s.replace("\n", "$BR$");
    }

}