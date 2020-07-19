/*
 * DefaultSourceFileWriter.java
 *
 * 07.08.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.io;


import corentx.io.*;

import java.io.*;


/**
 * A default implementation of the <CODE>SourceFileWriter</CODE> interface which writes the
 * files to the file system. The target directories have to be created.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 07.08.2013 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public class DefaultSourceFileWriter implements SourceFileWriter {

    /**
     * @changed OLI 07.08.2013 - Added.
     */
    @Override public void write(String fileName, String content, boolean append)
            throws IOException {
        System.out.println("writing file: " + fileName);
        File dir = new File(FileUtil.cutLastPathDir(fileName));
        if (!dir.exists()) {
            System.out.println("creating dir: " + dir.getAbsolutePath());
            dir.mkdirs();
        }
        FileUtil.writeTextToFile(fileName, append, content);
    }

}