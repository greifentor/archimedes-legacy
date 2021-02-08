/*
 * JScriptRunner.java
 *
 * 17.08.2011
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.script;


import static corentx.util.Checks.*;

import java.util.*;

import javax.script.*;


/**
 * Mit Hilfe dieser Klasse kann ein Java-Script ausgef&uuml;hrt werden. Das auszuf&uuml;hrende
 * Script ist als String an den Konstruktor der Klasse nebst eventuellen Parametern und einer
 * Liste mit erwarteten Ergebnisvariablen zu &uuml;bergeben.
 *
 * F&uuml;r die Ausf&uuml;hrung des folgenden Scripts:
 * <PRE>
 * var result = pts.toString();
 * result = result + " :o)";
 * </PRE>
 * kann folgender Aufruf implementiert werden:
 * 
 * <PRE>
 * Map&lt;String, Object&gt; params = new Hashtable&lt;String, Object&gt;();
 * params.put("pts", new PTimestamp(20000407231500L));
 * List&lt;String&gt; resultVars = new Vector&lt;String&gt;();
 * resultVars.add("result");
 * JScriptRunner runner = new JScriptRunner(script, params, resultVars);
 * Map&lt;String, Object&gt; result = runner.exec();
 * </PRE>
 * Der Aufruf &uuml;bergibt den Zeitstempel als Variable "pts" an das Script und erwartet als
 * Ergebnis die Variable "result" aus dem Script.
 * <BR>Nach dem Aufruf findet sich in der durch die Methode <TT>exec()</TT>
 * zur&uuml;ckgelieferten Map "result" unter dem Schl&uuml;ssel "result" der Eintrag "07.04.2000
 * 23:15:00 :o)".
 * <BR>Optional k&ouml;nnen die Parametermap oder die Liste mit den Namen der Ergebnisfelder
 * leer oder als <TT>null</TT>-Pointer &uuml;bergeben werden. In diesem Fall werden keine
 * Parameter an das Script &uuml;bergeben bzw. keine Ergebnisse zur&uuml;ck erwartet.
 *
 * <P>Das angegebene Script und der Aufruf kommen in &auml;hnlicher Form auch in den Tests zur
 * Klasse zum Einsatz.
 *
 * <P>&nbsp;
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.08.2011 - Hinzugef&uuml;gt.
 */

public class JScriptRunner {

    private Map<String, Object> params = null;
    private List<String> results = null;
    private String script = null;

    /**
     * Erzeugt eine neue Instanz der Klasse anhand der &uuml;bergebenen Parameter.
     *
     * @param script Das Java-Script, das durch den Runner ausgef&uuml;hrt werden soll.
     * @param params Eine Sammlung von Objekten, die unter dem angegebenen Namen innerhalb des
     *         Scripts verf&uuml;gbar sein sollen. Wenn keine Parameter an das Script
     *         &uuml;bergeben werden sollen, kann hier eine leere Map oder eine
     *         <TT>null</TT>-Referenz &uumL;bergeben werden.
     * @param results Eine Liste mit den Namen der Variablen, die durch das Script belegt werden
     *         sollen. Wenn vom Script keine Ergebnisse zur&uuml;ck erwartet werden, kann hier
     *         eine leere Map oder eine <TT>null</TT>-Referenz &uumL;bergeben werden. 
     * @throws IllegalArgumentException Falls eine der Vorbedingungen verletzt wird.
     * @precondition script != <TT>null</TT>
     *
     * @changed OLI 17.08.2011 - Hinzugef&uuml;gt.
     */
    public JScriptRunner(String script, Map<String, Object> params, List<String> results)
            throws IllegalArgumentException {
        super();
        ensure(script != null, "script cannot be null.");
        this.params = params;
        this.results = results;
        this.script = script;
    }

    /**
     * F&uuml;hrt das angegebene Script aus und liefert gegebenenfalls die erwarteten Parameter
     * zur&uuml;ck.
     *
     * @return Eine Map&lt;String,Object&gt; mit den Inhalten der angeforderten
     *         Variableninhalten oder eine leere Map, falls keine Ergebniswerte angefordert
     *         worden sind.
     * @throws Exception Falls bei der Ausf&uuml;hrung des Scripts ein Fehler auftritt.
     *
     * @changed OLI 17.08.2011 - Hinzugef&uuml;gt.
     */
    public Map<String, Object> exec() throws Exception {
        Map<String, Object> resultContents = new Hashtable<String, Object>();
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        if (this.params != null) {
            for (String key : this.params.keySet()) {
                engine.put(key, this.params.get(key));
            }
        }
        engine.eval(this.script);
        if (this.results != null) {
            for (String key : this.results) {
                resultContents.put(key, engine.get(key));
            }
        }
        return resultContents;
    }

}