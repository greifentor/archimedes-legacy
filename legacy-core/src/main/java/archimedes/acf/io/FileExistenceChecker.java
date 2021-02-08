/*
 * FileExistenceChecker.java
 *
 * 18.04.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.io;


import java.io.*;


/**
 * Checks if a file exists.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.04.2013 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public class FileExistenceChecker {

    /**
     * Checks if the file with the passed name exists in the file system.
     *
     * @param fileName The name of the file whose existence is to check.
     *
     * @changed OLI 18.04.2013 - Added.
     */
    public boolean exists(String fileName) {
        return new File(fileName).exists();
    }

}