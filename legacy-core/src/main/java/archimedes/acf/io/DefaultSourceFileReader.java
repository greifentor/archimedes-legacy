/*
 * DefaultSourceFileReader.java
 *
 * 28.11.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.io;


import corentx.io.*;

import java.io.*;


/**
 * A default implementation of the <CODE>SourceFileReader</CODE> interface which accesses the
 * real file system.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 28.11.2013 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public class DefaultSourceFileReader implements SourceFileReader {

    /**
     * @changed OLI 28.11.2013 - Added.
     */
    @Override public String read(String fileName) throws IOException {
        return FileUtil.readTextFromFile(fileName);
    }

}