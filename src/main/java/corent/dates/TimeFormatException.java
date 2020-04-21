/*
 * TimeFormatException.java
 *
 * 04.01.2005
 *
 * (c) by O.Lieshoff
 *
 */

package corent.dates;


import java.io.Serializable;


/**
 * Mit Hilfe dieser Klasse kann ein Problem mit dem Uhrzeitformat zur Anzeige gebracht werden.
 * <BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public class TimeFormatException extends RuntimeException implements Serializable {

    public TimeFormatException() {
        super();
    }

    public TimeFormatException(String s) {
        super(s);
    }

}
