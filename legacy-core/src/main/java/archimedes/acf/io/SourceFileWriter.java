/*
 * SourceFileWriter.java
 *
 * 18.04.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.io;


import java.io.*;


/**
 * An interface which describes a class which is able to write a source code file to the file
 * system.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.04.2013 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public interface SourceFileWriter {

    /**
     * Writes the passed file content by the passed name to the file system.
     *
     * @param fileName The name of the file which should be written.
     * @param content The content of the file.
     * @param append Set this flag if the writer have not to override the file but to append the
     *         content.
     * @throws IOException In case of an error occurs while writing the file.
     *
     * @changed OLI 18.04.2013 - Added.
     */
    abstract public void write(String fileName, String content, boolean append)
            throws IOException;

}