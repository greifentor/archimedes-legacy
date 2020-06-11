/*
 * Semaphore.java
 *
 * 04.07.2007
 *
 * (c) by O.Lieshoff
 *
 */

package corent.base;


/**
 * Die vorliegende Semaphoren-Klasse ist abgekupfert aus den Vorlesungsunterlagen der
 * NSP-Vorlesung des WS 99/00. Die Ehre der Autorenschaft geb&uuml;hrt Prof.Dr.Heinz Schweppe 
 * (FUB).
 *
 * <P>Die vorliegende Semaphoren-Klasse richtet sich nur eingeschr&auml;nkt nach der 
 * eigentlichen Spezifikation des Semaphors. Der Unterschied liegt darin, da&szlig;, im 
 * Gegensatz zur Definition, keine Aussage &uuml;ber die Reihenfolge gemacht werden kann, in der
 * die passierwilligen Prozesse aus dem Wartezustand geholt werden. Laut urspr&uuml;nglicher
 * Definition sollten sie in der Reihenfolge ihres Eintreffen passieren d&uuml;rfen. DAS IST
 * HIER NICHT DER FALL. <P>
 *
 * @author O.Lieshoff.
 *
 */

public class Semaphore {

    /**
     * In dieser Variablen wird die Anzahl der Prozesse gehalten, die noch passieren
     * k&ouml;nnen.
     */
    private int count = 1;

    /**
     * Der parameterlose Konstruktor initialisiert den Semaphor mit dem Wert 1. Es kann also
     * immer nur ein einzelner Prozesse passieren.
     */
    public Semaphore() {
        super();
        count = 1;
    }

    /**
     * Dieser Konstruktor fordert einen Initialwert. Der &uuml;bergebene Wert definiert die
     * Maximalanzahl der Prozesse, die sich im kritischen Abschnitt aufhalten d&uuml;rfen.
     *
     * @param i Description of the Parameter
     */
    public Semaphore(int i) {
        super();
        count = i;
    }

    /**
     * Diese Methode mu&szlig; an den Beginn des zu sch&uuml;tzenden Abschnitts gesetzt werden.
     * P steht f&uuml; <I>passeren</I> , V f&uuml; <I>vrijgeven</I> . Wie man sich leicht
     * vorstellen kann, waren die Erfinder dieser Methodik muttersprachliche Niederl&auml;nder.
     * <P>Bei jedem Aufruf von P() wird &uuml;berpr&uuml;ft, ob der Proze&szlig; noch passieren
     * darf. Gegebenenfalls wird er in Wartezustand versetzt.
     *
     * @throws InterruptedException
     */
    public synchronized void P() throws InterruptedException {
        while (count <= 0) {
            wait();
        }
        count--;
    }

    /**
     * Mit dieser Operation wird der kritische Abschnitt wieder freigegeben. Befinden sich
     * Prozesse im Wartezustand, so wird ihnen dies mitgeteilt. Der erste Proze&szlig;, der
     * reagiert, darf eintreten. Die Reihenfolge ist also willk&uuml;rlich (entgegen der
     * eigentlichen Definition.
     */
    public synchronized void V() {
        count++;
        notify();
    }

}
