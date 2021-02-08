/*
 * SourceFileReader.java
 *
 * 28.11.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.io;


import java.io.*;


/**
 * An interface which describes a class which is able to read a source code file from the file
 * system.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 28.11.2013 - Added.
 * @changed OLI 16.09.2015 - Moved from "Archimedes".
 */

public interface SourceFileReader {

    /**
     * Reads the content of the file with the passed name from the file system.
     *
     * @param fileName The name of the file which should be read.
     * @return content The content of the file.
     * @throws IOException In case of an error occurs while reading the file.
     *
     * @changed OLI 28.11.2013 - Added.
     */
    abstract public String read(String fileName) throws IOException;

}