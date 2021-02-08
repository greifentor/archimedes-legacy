/*
 * PTime.java
 *
 * 04.01.2005
 *
 * (c) by O.Lieshoff
 *
 */

package corent.dates;


import corent.base.*;

import java.io.*;
import java.util.*;


/**
 * &Auml;hnlich der PTime-Klasse bildet diese Klasse eine Uhrzeit als Ganzzahl ab und bietet 
 * entsprechende Methoden zur Manipulation an.
 * <P>Die Property <TT>corent.dates.PTime.undefined.string</TT> erm&ouml;glicht die Angabe eines
 * alternativen String bei der Ausgabe undefinierter PTime-Angaben. Der Defaultwert ist 
 * "XX:XX:XX".
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.03.2010 - Korrektur: Bei &Uuml;bergabe des Wertes <TT>0</TT> wird nun keine
 *         Exception mehr von den Methoden <TT>naechsteMinute(int)</TT>,
 *         <TT>naechsteSekunde(int)</TT>, <TT>vorherigeMinute(int)</TT> und
 *         <TT>vorherigeSekunde(int)</TT> geworfen.
 */

public class PTime implements Serializable {
    
    /** Das undefinierte Datum als Bezeichner. */
    public static final PTime UNDEFINIERT = new PTime(false);
    
    /* Hier wird der Wert der PTime gespeichert. */
    private int t = -1;
    
    private PTime(boolean b) {
        super();
    }

    /** Erzeugt eine PTime mit der aktuellen Uhrzeit der ausf&uuml;hrenden Maschine. */
    public PTime() {
        super();
        Calendar dt = Calendar.getInstance();
        t = (dt.get(Calendar.HOUR_OF_DAY) * 10000) + (dt.get(Calendar.MINUTE) * 100) 
                + dt.get(Calendar.SECOND);
    }

    /**
     * Erzeugt eine PTime aus dem &uuml;bergebenen Integer-Wert, falls dieser eine g&uuml;ltige 
     * PTime enth&auml;t.
     *
     * @param pt Ein int-Wert im PTime-Format.
     * @throws TimeFormatException wenn der int-Wert keine g&uuml;ltige Uhrzeit enth&auml;lt.
     */
    public PTime(int pt) throws TimeFormatException {
        this(pt / 10000, (pt / 100) % 100, pt % 100);
    }

    /**
     * Erzeugt eine PTime als Kopie der &uuml;bergebenen PTime. 
     *
     * @param pt Die zu kopierende PTime.
     * @throws TimeFormatException, wenn der Inhalt der &uuml;bergebenen PTime fehlerhaft ist.
     */
    public PTime(PTime pt) {
        this(pt.getStunde(), pt.getMinute(), pt.getSekunde());
    }

    /**
     * Erzeugt eine PTime aus den &uuml;bergebenen Parametern.
     *
     * @param stunde Die Stunde der &uuml;bergebenen Uhrzeit. 
     * @param minute Die Minute der &uuml;bergebenen Uhrzeit. 
     * @param sekunde Die Sekunde der &uuml;bergebenen Uhrzeit. 
     */
    public PTime(int stunde, int minute, int sekunde) {
        super();
        if (CheckTime(stunde, minute, sekunde)) {
            this.t = stunde * 10000 + minute * 100 + sekunde;
        } else {
            throw new TimeFormatException("time not valid: " + stunde + ":" + minute + ":" 
                    + sekunde);
        }
    }

    /**
     * Erzeugt aus dem angegeben String eine PTime.
     *
     * @param s Der String, aus dem die PTime generiert werden soll.
     * @throws TimeFormatException wenn der Inhalt des &uuml;bergebenen String fehlerhaft ist.
     */
    public static PTime valueOf(String s) throws TimeFormatException {
        char c;
        int a = 0;
        int stunde = 0;
        int minute = 0;
        int p = 0;
        int sekunde = 0;
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            if ((c >= '0') && (c <= '9')) {
                a = a * 10;
                a += (c - '0');
            } else if (c == ':') {
                p++;
                switch (p) {
                    case 1:
                        stunde = a;
                        break;
                    case 2:
                        minute = a;
                        break;
                    case 3:
                        sekunde = a;
                        break;
                    default:
                        throw new TimeFormatException("to many arguments");
                }
                a = 0;
            } else {
                throw new TimeFormatException("invalid digit: " + c);
            }
        }
        sekunde = a;
        if (!CheckTime(stunde, minute, sekunde)) {
            throw new TimeFormatException("time not valid: " + stunde + ":" + minute + ":" 
                    + sekunde);
        }
        return new PTime(stunde, minute, sekunde);
    }
    
    /**
     * Checkt die &uuml;bergebenen Parameter auf Darstellung einer g&uuml;tigen Uhrzeit. 
     *
     * @param stunde Die Stunde der zu pr&uuml;fenden Uhrzeit. 
     * @param minute Die Minute der zu pr&uuml;fenden Uhrzeit. 
     * @param sekunde Die Sekunde der zu pr&uuml;fenden Uhrzeit. 
     * @return <TT>true</TT>, wenn die &uuml;bergebenen Parameter eine g&uuml;ltige Uhrzeit 
     *     ergeben,<BR><TT>false</TT> sonst.
     */
    public static boolean CheckTime(int stunde, int minute, int sekunde) {
        if ((sekunde < 0) ||(sekunde > 59)) {
            return false;
        }
        if ((minute < 0) ||(minute > 59)) {
            return false;
        }
        if ((stunde < 0) || (stunde > 23)) {
            return false;
        }
        return true;
    }

    /** 
     * Wandelt eine PTime in einen int-Wert um.
     *
     * @return Der int-Wert (HHMMSS) zur PTime.
     */
    public int toInt() {
        return this.t;
    }

    /** @return Die Sekunde der Uhrzeit. */
    public int getSekunde() {
        return this.t % 100;
    }

    /** @return Die Minute der Uhrzeit. */     
    public int getMinute() {
        return (this.t % 10000) / 100;
    }

    /** @return Die Stunde der Uhrzeit. */
    public int getStunde() {
        return this.t / 10000;
    }

    /** @return Liefert eine neue Uhrzeit mit der angegebenen Sekunde. */ 
    public PTime setSekunde(int sekunde) {
        return new PTime(this.getStunde(), this.getMinute(), sekunde);
    }

    /** @return Liefert eine neue Uhrzeit mit der angegebenen Minute. */ 
    public PTime setMinute(int minute) {
        return new PTime(this.getStunde(), minute, this.getSekunde());
    }

    /** @return Liefert eine neue Uhrzeit mit der angegebenen Stunde. */ 
    public PTime setStunde(int stunde) {
        return new PTime(stunde, this.getMinute(), this.getSekunde());
    }

    public boolean equals(Object o) {
        if (!(o instanceof PTime)) {
            return false;
        }
        PTime p = (PTime) o;
        return (this.toInt() == p.toInt());
    }

    public int hashCode() {
        return this.toInt();
    }

    public String toString() {
        String s = "";
        if (this.toInt() == -1) {
            s = s + Utl.GetProperty("corent.dates.PTime.undefined.string", "XX:XX:XX");
        } else {
            String h = "";
            h = "" + this.getStunde();
            if (h.length() < 2) {
                s = "0";
            }
            s = s + h + ":";
            h = "" + this.getMinute();
            if (h.length() < 2) {
                s = s + "0";
            }
            s = s + h + ":";
            h = "" + this.getSekunde();
            while (h.length() < 2) {
                h = "0" + h;
            }
            s = s + h;
        }
        return s;
    }

    /** @return Die vorliegende Uhrzeit plus eine Sekunde. */
    public PTime naechsteSekunde() {
        int minute = this.getMinute(); 
        int sekunde = this.getSekunde()+1; 
        int stunde = this.getStunde();
        if (sekunde > 59) {
            sekunde = 0;
            minute++;
        }
        if (minute > 59) {
            minute = 0;
            stunde++;
        }
        if (stunde > 23) {
            stunde = 0;
        }
        return new PTime(stunde, minute, sekunde);
    }

    /**
     * Ermittelt die Uhrzeit, die n Sekunden nach der vorliegenden liegt.
     *
     * @param n Anzahl der Sekunden.
     * @return Uhrzeit, die n Sekunden nach der vorliegenden liegt.
     * @throws IllegalArgumentException wenn n kleiner als 0 ist.
     * @precondition n &gt;= <TT>0</TT>
     *
     * @changed OLI 18.03.2010 - Debugging: Die Exception wird jetzt nicht mehr geworfen, wenn
     *         n == 0 ist.
     */
    public PTime naechsteSekunde(int n) throws IllegalArgumentException {
        if (n < 0) {
            throw new IllegalArgumentException("n muss groesser oder gleich 0 sein!");
        }
        PTime next = new PTime(this);
        for (int i = 0; i < n; i++) {
            next = next.naechsteSekunde();
        }
        return next;
    }

    /** @return Das Datum, das eine Sekunde vor dem vorliegenden liegt. */
    public PTime vorherigeSekunde() {
        int minute = this.getMinute(); 
        int sekunde = this.getSekunde()-1; 
        int stunde = this.getStunde();
        if (sekunde < 0) {
            sekunde = 59;
            minute--;
        }
        if (minute < 0) {
            minute = 59;
            stunde--;
        }
        if (stunde < 0) {
            stunde = 23;
        }
        return new PTime(stunde, minute, sekunde);
    }

    /**
     * Ermittelt die Uhrzeit, die n Sekunden vor der vorliegenden liegt.
     *
     * @param n Anzahl der Sekunden.
     * @return Uhrzeit, die n Sekunden vor der vorliegenden liegt.
     * @throws IllegalArgumentException wenn n kleiner als 0 ist.
     * @precondition n &gt;= <TT>0</TT>
     *
     * @changed OLI 18.03.2010 - Debugging: Die Exception wird jetzt nicht mehr geworfen, wenn
     *         n == 0 ist.
     */
    public PTime vorherigeSekunde(int n) throws IllegalArgumentException {
        if (n < 0) {
            throw new IllegalArgumentException("n muss groesser oder gleich 0 sein!");
        }
        PTime previous = new PTime(this);
        for (int i = 0; i < n; i++) {
            previous = previous.vorherigeSekunde();
        }
        return previous;
    }

    /** @return Die vorliegende Uhrzeit plus eine Minute. */
    public PTime naechsteMinute() {
        int minute = this.getMinute()+1; 
        int sekunde = this.getSekunde(); 
        int stunde = this.getStunde();
        if (minute > 59) {
            minute = 0;
            stunde++;
        }
        if (stunde > 23) {
            stunde = 0;
        }
        return new PTime(stunde, minute, sekunde);
    }

    /**
     * Ermittelt die Uhrzeit, die n Minuten nach der vorliegenden liegt.
     *
     * @param n Anzahl der Minuten.
     * @return Uhrzeit, die n Minuten nach der vorliegenden liegt.
     * @throws IllegalArgumentException wenn n kleiner als 0 ist.
     * @precondition n &gt;= <TT>0</TT>
     *
     * @changed OLI 18.03.2010 - Debugging: Die Exception wird jetzt nicht mehr geworfen, wenn
     *         n == 0 ist.
     */
    public PTime naechsteMinute(int n) throws IllegalArgumentException {
        if (n < 0) {
            throw new IllegalArgumentException("n muss groesser oder gleich 0 sein!");
        }
        PTime next = new PTime(this);
        for (int i = 0; i < n; i++) {
            next = next.naechsteMinute();
        }
        return next;
    }

    /** @return Das Datum, das eine Minute vor dem vorliegenden liegt. */
    public PTime vorherigeMinute() {
        int minute = this.getMinute()-1; 
        int sekunde = this.getSekunde(); 
        int stunde = this.getStunde();
        if (minute < 0) {
            minute = 59;
            stunde--;
        }
        if (stunde < 0) {
            stunde = 23;
        }
        return new PTime(stunde, minute, sekunde);
    }

    /**
     * Ermittelt die Uhrzeit, die n Minuten vor der vorliegenden liegt.
     *
     * @param n Anzahl der Minuten.
     * @return Uhrzeit, die n Minuten vor der vorliegenden liegt.
     * @throws IllegalArgumentException wenn n kleiner als 0 ist.
     * @precondition n &gt;= <TT>0</TT>
     *
     * @changed OLI 18.03.2010 - Debugging: Die Exception wird jetzt nicht mehr geworfen, wenn
     *         n == 0 ist.
     */
    public PTime vorherigeMinute(int n) throws IllegalArgumentException {
        if (n < 0) {
            throw new IllegalArgumentException("n muss groesser oder gleich 0 sein!");
        }
        PTime previous = new PTime(this);
        for (int i = 0; i < n; i++) {
            previous = previous.vorherigeMinute();
        }
        return previous;
    }

    /**
     * Ermittelt, ob sich die vorliegende Uhrzeit zwischen den beiden angegebenen Uhrzeiten 
     * befindet. Liegt Die Uhrzeit t0 hinter der Uhrzeit t1, so werden die Uhrzeiten vertauscht.
     *
     * @param t0 Beginn des Zeitraums.
     * @param t1 Ende des Zeitraumes.
     * @return <TT>true</TT>, wenn die vorliegende Uhrzeit in dem angegebenen Zeitraum liegt.
     */
    public boolean isImZeitraum(PTime t0, PTime t1) {
        if (t0.toInt() > t1.toInt()) {
            PTime th = new PTime(t0);
            t0 = new PTime(t1);
            t1 = new PTime(th);
        }
        if ((this.toInt() >= t0.toInt()) && (this.toInt() <= t1.toInt())) {
            return true;
        }
        return false;
    }

}
