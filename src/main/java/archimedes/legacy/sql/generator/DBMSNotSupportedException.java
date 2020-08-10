/*
 * DBMSNotSupportedException.java
 *
 * 14.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.sql.generator;



/**
 * This exception is thrown if the DBMS is not supported by a SQL script generator.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 14.12.2015 - Added.
 */

public class DBMSNotSupportedException extends IllegalArgumentException {

    /**
     * Creates a new DBMS not supported exception.
     *
     * @param message A message for the exception.
     *
     * @changed OLI 14.12.2015 - Added.
     */
    public DBMSNotSupportedException(String message) {
        super(message);
    }

}