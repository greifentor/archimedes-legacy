/*
 * DBExecEvent.java
 *
 * 27.04.2011
 *
 * (c) by O.Lieshoff
 *
 */

package corent.db;


/**
 * Ein Container f&uuml;r die Daten zu einem Ereignis in der DBExec-Klasse.
 * 
 * @author O.Lieshoff
 *
 * @changed OLI 27.04.2011 - Hinzugef&uuml;gt.
 */

public class DBExecEvent {

    private boolean isStatic = false;
    private DBExecEventType type = null;
    private long millis = -1;
    private long timestamp = -1;
    private String statement = null;

    /**
     * Erzeugt eine neue Instanz der Klasse anhand der angegebenen Parameter.
     *
     * @param type Der Type zum Ereignis.
     * @param timestamp Der in eine Ganzzahl codierter Zeitstempel, an dem das Ereignis
     *         aufgetreten ist.
     * @param millis Die Anzahl der Millisekunden, die die Ausf&uuml;hrung der zum Ereignis
     *         passenden Aktion gedauert hat.
     * @param statement Das SQL-Statement, das mit dem Ereignis in Zusammenhang steht, wenn es
     *         eines gibt, sonst <TT>null</TT>.
     * @param isStatic Diese Flagge muss gesetzt werden, wenn das Ereignis von der statischen
     *         Instanz von DBExec ausgel&ouml;st worden ist.
     * @throws IllegalArgumentException Falls eine der Vorbedingungen verletzt wird.
     * @precondition millis &gt;= <TT>0</TT>
     * @precondition type != <TT>null</TT>
     *
     * @changed OLI 27.04.2011 - Hinzugef&uuml;gt.
     */
    public DBExecEvent(DBExecEventType type, long timestamp, long millis, String statement,
            boolean isStatic) throws IllegalArgumentException {
        super();
        if (millis < 0) {
            throw new IllegalArgumentException("milli second amount cannot be lesser than zero."
                    );
        }
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null.");
        }
        this.isStatic = isStatic;
        this.millis = millis;
        this.statement = statement;
        this.timestamp = timestamp;
        this.type = type;
    }

    /**
     * Liefert die Anzahl der Millisekunden, die die zum Ereignis f&uuml;hrende Aktion gedauert
     * hat.
     *
     * @return Die Anzahl der Millisekunden, die die zum Ereignis f&uuml;hrende Aktion gedauert
     *         hat.
     *
     * @changed OLI 27.04.2011 - Hinzugef&uuml;gt.
     */
    public long getMillis() {
        return this.millis;
    }

    /**
     * Liefert das Statement, das an dem Ereignis beteiligt war, falls es ein solches gibt,
     * sonst <TT>null</TT>.
     *
     * @return Das Statement, das an dem Ereignis beteiligt war, falls es ein solches gibt,
     *         sonst <TT>null</TT>.
     *
     * @changed OLI 27.04.2011 - Hinzugef&uuml;gt.
     */
    public String getStatement() {
        return this.statement;
    }

    /**
     * Liefert den in einer Ganzzahl verpackten Zeitstempel, an dem das Ereignis eingetreten
     * ist.
     *
     * @return Der in einer Ganzzahl verpackten Zeitstempel, an dem das Ereignis eingetreten
     *         ist.
     *
     * @changed OLI 27.04.2011 - Hinzugef&uuml;gt.
     */
    public long getTimestamp() {
        return this.timestamp;
    }

    /**
     * Liefert den Typ zum Ereignis.
     *
     * @return Der Typ zum Ereignis
     *
     * @changed OLI 27.04.2011 - Hinzugef&uuml;gt.
     */
    public DBExecEventType getType() {
        return this.type;
    }

    /**
     * Pr&uuml;ft, ob das Ereignis von der statischen <TT>DBExec</TT>-Instanz ausgel&ouml;st
     * worden ist. 
     *
     * @return <TT>true</TT>, falls das Ereignis von der statischen <TT>DBExec</TT>-Instanz
     *         ausgel&ouml;st worden ist, sonst <TT>false</TT>.
     *
     * @changed OLI 27.04.2011 - Hinzugef&uuml;gt.
     */
    public boolean isStatic() {
        return this.isStatic;
    }

}