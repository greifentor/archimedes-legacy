/*
 * ChangedChecker.java
 *
 * 03.06.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.util.checker;


import archimedes.acf.*;
import archimedes.acf.io.*;
import archimedes.acf.report.*;
import corentx.dates.*;
import corentx.io.*;
import corentx.util.*;
import gengen.*;

import java.io.*;
import java.util.*;


/**
 * A utility class which provides methods to check if a class has been changed by the generation
 * process compared to the stored file.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 03.06.2015 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public class ChangedChecker {

    public static final String UNCHANGED_TEXT = "// UNCHANGED - Remove this tag and place your "
            + "code here.";

    /**
     * Checks if the class stored in the file with the passed name is different to the passed
     * source code.
     *
     * @param fileName The name of the file to compare.
     * @param code The code to compare with the file.
     * @param individualPreferences The individual preferences of the user.
     * @param sourceFileReader A source file reader to access the file system.
     * @return <CODE>true</CODE> if the passed code is equal to the content of the file with the
     *         passed name.
     *
     * @changed OLI 03.06.2015 - Added comment.
     */
    public boolean hasBeenChanged(String fileName, String code, 
            IndividualPreferences individualPreferences, SourceFileReader sourceFileReader) {
        try {
            String fileContent = sourceFileReader.read(fileName);
            fileContent = this.cleanUpLineEnds(fileContent);
            fileContent = this.changeIndividualData(fileContent, individualPreferences);
            if (fileContent.endsWith("\n")) {
                fileContent = fileContent.substring(0, fileContent.length()-1);
            }
            return !code.equals(fileContent);
        } catch (Exception e) {
            return true;
        }
    }

    private String cleanUpLineEnds(String fileContent) {
        fileContent = fileContent.replace("\r\n", "\n");
        return fileContent.replace("\r", "\n");
    }

    private String changeIndividualData(String fileContent,
            IndividualPreferences individualPreferences) {
        List<String> fileContentList = Str.splitToList(fileContent, "\n");
        String date = this.getDateFromSourceFile(fileContentList);
        String userName = this.getUserNameFromSourceFile(fileContentList);
        String userToken = this.getUserTokenFromSourceFile(fileContentList);
        fileContent = fileContent.replace(date, new PDate().toString());
        fileContent = fileContent.replace("@author " + userName, "@author "
                + individualPreferences.getUserName());
        String ipToken = individualPreferences.getUserToken();
        while (ipToken.length() < 3) {
            ipToken += " ";
        }
        fileContent = fileContent.replace("@changed " + userToken, "@changed " + ipToken);
        return fileContent;
    }

    private String getDateFromSourceFile(List<String> fileContent) {
        return fileContent.get(3).substring(2);
    }

    private String getUserNameFromSourceFile(List<String> fileContent) {
        for (String s : fileContent) {
            int i = s.indexOf("@author");
            if (i > -1) {
                return s.substring(i + "@author".length()).trim();
            }
        }
        return "Archimedes Code Factory";
    }

    private String getUserTokenFromSourceFile(List<String> fileContent) {
        for (String s : fileContent) {
            int i = s.indexOf("@changed");
            if (i > -1) {
                s = s.substring(i + "@changed".length()).trim();
                s = s.substring(0, s.indexOf(" ")).trim();
                while (s.length() < 3) {
                    s += " ";
                }
                return s;
            }
        }
        return "ICF";
    }

    /**
     * Checks if the file with the passed name is changed.
     *
     * @param fileName The name to check.
     * @return <CODE>true</CODE> if the file is changed.
     *
     * @changed OLI 29.09.2013 - Added.
     */
    public boolean isCodeChanged(String fileName) {
        try {
            String fileContent = FileUtil.readTextFromFile(fileName);
            return !fileContent.contains(UNCHANGED_TEXT);
        } catch (FileNotFoundException e) {
            return false;
        } catch (Exception e) {
            return true;
        }
    }

}