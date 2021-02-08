/*
 * Checks.java
 *
 * 03.09.2010
 *
 * (c) O.Lieshoff
 *
 */

package corentx.util;


/**
 * Diese Klasse bietet Methoden zum Check bestimmter Sachverhalte w&auml;hrend der Laufzeit. So
 * kann z. B. durch Aufruf der ensure-Methode auf das Konstrukt einer if-Anweisung mit Werfen
 * einer IllegalArgumentException verzichtet werden.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 03.09.2010 - Hinzugef&uuml;gt.
 * @changed OLI 25.10.2010 - Erweiterung um die Methode <TT>ensure(boolean, Exception</TT>.
 * @changed OLI 16.11.2011 - Erweiterung um ein paar <TT>ensure</TT>-Methoden.
 */

public class Checks {
    
    public Checks() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("there is no sense to instaniate this class.");
    }

    /**
     * Wirft eine IllegalArgumentException mit dem angegebenen Text, falls die Bedingung als
     * <TT>false</TT> &uuml;bergeben wird.
     *
     * @param condition Die Bedingung, die gepr&uuml;ft werden soll.
     * @param message Der Text, der an die zu werfende Exception &uuml;bergeben werden soll.
     * @throws IllegalArgumentException Falls der Parameter <TT>condition</TT> mit dem Wert
     *         <TT>false</TT> &uuml;bergeben wird.
     *
     * @changed OLI 03.09.2010 - Hinzugef&uuml;gt.
     */
    public static void ensure(boolean condition, String message)
            throws IllegalArgumentException {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Wirft die &uuml;bergebene Exception, falls die Bedingung als <TT>false</TT>
     * &uuml;bergeben wird.
     *
     * @param condition Die Bedingung, die gepr&uuml;ft werden soll.
     * @param exception Die zu werfende Exception, falls die Bedingung nicht erf&uuml;llt wird.
     * @throws Exception Falls der Parameter <TT>condition</TT> mit dem Wert <TT>false</TT>
     *         &uuml;bergeben wird.
     * @throws NullPointerException Falls die Exception als <TT>null</TT>Referenz &uuml;bergeben
     *         wird.
     *
     * @changed OLI 25.10.2010 - Hinzugef&uuml;gt.
     * @changed OLI 19.01.2012 - Umbau, sodass die Methode auch das Werfen anderer Exceptions
     *         zul&auml;sst (nach Idee von VM).
     */
    public static <T extends Exception> void ensure(boolean condition, T exception) throws T {
        if (exception == null) {
            throw new NullPointerException("exception cannot be null.");
        }
        if (!condition) {
            throw exception;
        }
    }

    /**
     * Wirft die angegebene RuntimeException, falls der &uuml;bergebene String leer ist.
     *
     * @param string Der String, der &uuml;berpr&uuml;ft werden soll.
     * @param message Der Text, der an die zu werfende Exception &uuml;bergeben werden soll.
     * @throws IllegalArgumentException Falls eine der Vorbedingungen verletzt wird.
     * @precondition string != <TT>null</TT>
     * @precondition string.length() &gt; <TT>0</TT>
     * 
     * @changed OLI 16.11.2011 - Hinzugef&uuml;gt.
     */
    public static void ensureNotEmpty(String string, String message) {
        ensureNotNull(string, message);
        ensure(string.length() > 0, message);
    }

    /**
     * Wirft eine <TT>IllegalArgumentException</TT>, falls der &uuml;bergebene String leer ist.
     *
     * @param string Der String, der &uuml;berpr&uuml;ft werden soll.
     * @param exception Die zu werfende Exception, falls die Bedingung nicht erf&uuml;llt wird.
     * @throws Exception Falls die angegebene Bedingung nicht erf&uuml;llt ist.
     * @throws NullPointerException Falls die Exception als <TT>null</TT>Referenz &uuml;bergeben
     *         wird.
     * @precondition string != <TT>null</TT>
     * @precondition string.length() &gt; <TT>0</TT>
     * 
     * @changed OLI 16.11.2011 - Hinzugef&uuml;gt.
     */
    public static <T extends Exception> void ensureNotEmpty(String string, T exception)
            throws T {
        ensureNotNull(string, exception);
        ensure(string.length() > 0, exception);
    }

    /**
     * Pr&uuml;ft, ob das Object ungleich <TT>null</TT> ist und wirft gegebenenfalls eine
     * <TT>IllegalArgumentException</TT>.
     * 
     * @param object Das zu &uuml;berpr&uuml;fende Objekt.
     * @param message Der Text, der an die zu werfende Exception &uuml;bergeben werden soll.
     * @throws IllegalArgumentException Falls das der Parameter <TT>object</TT> als
     *         <TT>null</TT>-Referenz &uuml;bergeben wird.
     * 
     * @changed OLI 16.11.2011 - Hinzugef&uuml;gt.
     */
    public static void ensureNotNull(Object object, String message) {
        ensure(object != null, message);
    }

    /**
     * Pr&uuml;ft, ob das Object ungleich <TT>null</TT> ist und wirft gegebenenfalls eine
     * <TT>IllegalArgumentException</TT>.
     * 
     * @param object Das zu &uuml;berpr&uuml;fende Objekt.
     * @param exception Die zu werfende Exception, falls die Bedingung nicht erf&uuml;llt wird.
     * @throws Exception Falls eine der Vorbedingungen verletzt wird.
     * @throws NullPointerException Falls die Exception als <TT>null</TT>Referenz &uuml;bergeben
     *         wird.
     * @precondition object != <TT>null</TT>
     * 
     * @changed OLI 16.11.2011 - Hinzugef&uuml;gt.
     */
    public static <T extends Exception> void ensureNotNull(Object object, T exception)
            throws T {
        ensure(object != null, exception);
    }

}