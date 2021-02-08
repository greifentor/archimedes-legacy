/*
 * DefaultUnchangedByTagChecker.java
 *
 * 20.09.2017
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf;

import java.io.*;

import archimedes.acf.util.checker.*;
import corentx.io.*;

/**
 * A default implementation for an unchanged by tag checker.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 20.09.2017 - Added.
 */

public class DefaultUnchangedByTagChecker implements UnchangedByTagChecker {

    /**
     * @changed OLI 20.09.2017 - Added.
     */
    @Override public boolean isFileUnchanged(String absoluteFileName) {
        File f = new File(absoluteFileName);
        if (f.exists()) {
            return this.checkFile(f);
        }
        return true;
    }

    private boolean checkFile(File f) {
        try {
            String c = FileUtil.readTextFromFile(f.getAbsolutePath());
            if (c.contains(ChangedChecker.UNCHANGED_TEXT)) {
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}