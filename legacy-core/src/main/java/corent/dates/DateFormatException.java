/*
 * DateFormatException.java
 *
 * 23.12.1999
 *
 * (c) by O.Lieshoff
 *
 */

package corent.dates;


import java.io.Serializable;


/**
 * Mit Hilfe dieser Klasse kann ein Problem mit dem Datumsformat zur Anzeige gebracht werden.
 * <BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public class DateFormatException extends RuntimeException implements Serializable {

    public DateFormatException() {
        super();
    }

    public DateFormatException(String s) {
        super(s);
    }

}
