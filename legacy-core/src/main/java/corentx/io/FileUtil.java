/*
 * FileUtil.java
 *
 * 01.11.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.io;


import static corentx.util.Checks.*;

import java.io.*;
import java.util.*;


/**
 * Diese Klasse bietet eine Handvoll Methoden an, die f&uuml;r die Arbeit mit dem Dateisystem
 * gedacht sind.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 23.07.2008 - Erweiterung um die TextFile-Methoden (WriteTextToFile(String,
 *         boolean, String) und ReadTextFromFile(String)) aus der Klasse corent.tools.Tools.
 * @changed OLI 12.02.2009 - Erweiterung um die Methode <TT>CompletePath(String)</TT>.
 * @changed OLI 13.02.2009 - Erweiterung um die Methode <TT>ReadStringList(String)</TT>.
 * @changed OLI 02.04.2009 - Erweiterung um die Methode <TT>ReadTextFromFile(String,
 *         String)</TT>.
 * @changed OLI 03.04.2009 - Debugging an der Methode <TT>ReadTextFromFile(String)</TT>.
 * @changed OLI 09.09.2009 - &Uuml;bernahme aus <TT>corent.util</TT>.
 * @changed OLI 23.02.2010 - Erweiterung um die Methoden <TT>copyFile(String, String)</TT> und
 *         <TT>moveFile(String, String)</TT>.
 */

public class FileUtil {

    private static void addFiles(List<String> l, File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    addFiles(l, files[i]);
                }
            }
        } else {
            l.add(f.getAbsolutePath());
        }
    }

    /**
     * Vervollst&auml;ndigt gegebenenfalls ein Pfadangabe mit einem abschliessenden Slash. Bei
     * Dieser Gelegenheit werden alle Backslashes in dem String in Slashes umgesetzt.
     *
     * @param path Ein Pfad zur Vervollst&auml;ndigung.
     * @return Ein mit einem Slash abschliessender Pfad, dessen Backslashes gegen Slashes
     *         ersetzt worden sind.
     * @throws NullPointerException Falls der Pfad als <TT>null</TT>-Referenz &uuml;bergeben
     *         wird.
     * @precondition path != <TT>null</TT>
     *
     * @changed OLI 12.02.2009 - Hinzugef&uuml;gt.
     */
    public static String completePath(String path) {
        path = path.replace("\\", "/");
        if (!path.endsWith("/")) {
            path = path.concat("/");
        }
        return path;
    }

    /**
     * Kopiert die angegebene Datei an den vorgegebenen Zielort.
     *
     * @since 1.20.1
     *
     * @param fnsrc Der Name der Datei, die kopiert werden soll.
     * @param fntrgt Der Name der Datei oder des Verzeichnisses, das als Zielort f&uuml;r das
     *         Kopieren herhalten soll.
     * @throws FileNotFoundException Falls die Quelldatei oder das Zielverzeichnis nicht
     *         existiert.
     * @throws IllegalArgumentException Falls einer beiden Parameter als <TT>null</TT>-Referenz
     *         &uuml;bergeben wird.
     * @throws IOException Falls beim Kopieren der Datei ein Fehler auftritt.
     * @precondition fnsrc != <TT>null</TT>
     * @precondition fntrgt != <TT>null</TT>
     * @precondition new File(fnsrc).exists()
     * @precondition target directory exists
     *
     * @changed OLI 23.02.2010 - Hinzugef&uuml;gt.
     */
    public static void copyFile(String fnsrc, String fntrgt) throws FileNotFoundException,
            IllegalArgumentException, IOException {
        copyFile(fnsrc, fntrgt, 100000, null);
    }

    /**
     * Kopiert die angegebene Datei an den vorgegebenen Zielort.
     *
     * @since 1.20.1
     *
     * @param fnsrc Der Name der Datei, die kopiert werden soll.
     * @param fntrgt Der Name der Datei oder des Verzeichnisses, das als Zielort f&uuml;r das
     *         Kopieren herhalten soll.
     * @throws FileNotFoundException Falls die Quelldatei oder das Zielverzeichnis nicht
     *         existiert.
     * @throws IllegalArgumentException Falls einer beiden Parameter als <TT>null</TT>-Referenz
     *         &uuml;bergeben wird.
     * @throws IOException Falls beim Kopieren der Datei ein Fehler auftritt.
     * @precondition fnsrc != <TT>null</TT>
     * @precondition fntrgt != <TT>null</TT>
     * @precondition new File(fnsrc).exists()
     * @precondition target directory exists
     *
     * @changed OLI 06.06.2015 - Extended by variable buffer size and progress listener.
     */
    public static void copyFile(String fnsrc, String fntrgt, int bufferSize,
            CopyFileProgressListener progressListener) throws FileNotFoundException,
            IllegalArgumentException, IOException {
        byte[] fileContent = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        int cnt = 0;
        int offset = 0;
        if (fnsrc == null) {
            throw new IllegalArgumentException("source file to copy can not be null.");
        }
        if (fntrgt == null) {
            throw new IllegalArgumentException("target file or dir to copy can not be null.");
        }
        if (new File(fntrgt).isDirectory()) {
            fntrgt = FileUtil.completePath(fntrgt).concat(new File(fnsrc).getName());
        }
        fileContent = new byte[bufferSize];
        if (progressListener != null) {
            progressListener.fileCopyStarted(new File(fnsrc).length(), fnsrc, fntrgt);
        }
        fis = new FileInputStream(fnsrc);
        fos = new FileOutputStream(fntrgt);
        long sum = 0;
        cnt = fis.read(fileContent, offset, bufferSize);
        sum += cnt;
        fos.write(fileContent, offset, cnt);
        if (progressListener != null) {
            progressListener.fileCopyProgressed(sum);
        }
        while (cnt != -1) {
            cnt = fis.read(fileContent, offset, bufferSize);
            sum += cnt;
            if (progressListener != null) {
                progressListener.fileCopyProgressed(sum);
            }
            if (cnt >= 0) {
                fos.write(fileContent, offset, cnt);
            }
        }
        fis.close();
        fos.close();
        if (progressListener != null) {
            progressListener.fileCopyFinished(sum);
        }
    }

    /**
     * Diese Methode entfernt das letzte Element aus der Pfadangabe (bis zum letzten Slash von
     * hinten).
     *
     * @param p Der Pfad, dessen letztes Element entfernt werden soll.
     * @throws IllegalArgumentException Falls der Pfad als <TT>null</TT>-Referenz &uuml;bergeben
     *         wird.
     * @precondition p != <TT>null</TT>.
     *
     * @changed OLI 19.01.2010 - Hinzugef&uuml;gt.
     */
    public static String cutLastPathDir(String p) throws IllegalArgumentException {
        int i = 0;
        if (p == null) {
            throw new IllegalArgumentException("path can not be null.");
        }
        p = p.replace("\\", "/");
        for (i = p.length()-1; i >= 0; i--) {
            if (p.charAt(i) == '/') {
                break;
            }
        }
        if (i >= 0) {
            p = p.substring(0, i);
        }
        return p;
    }

    /**
     * Diese Methode liefert die letzte Extension des Namens der angegebenen Datei.
     *
     * <P><I>Beispiel:
     * <BR>GetExtension(new File("beschreibung.txt.pdf") =&gt; "pdf".
     * <BR>GetExtension(new File("beschreibung.txt") =&gt; "txt".
     * <BR>GetExtension(new File("beschreibung") =&gt; "".
     * </I>
     *
     * @param f Das File dessen Extension ermittelt werden soll.
     * @return Die Erweiterung im Dateiname oder "", wenn es keine gibt.
     */
    public static String getExtension(File f) {
        String ext = "";
        String fn = f.getName();
        while (fn.indexOf(".") > -1) {
            if (fn.length() > fn.indexOf(".")+1) {
                fn = fn.substring(fn.indexOf(".")+1, fn.length());
            } else {
                fn = "";
            }
            ext = fn;
        }
        return ext;
    }

    /**
     * Liefert eine Liste aller Dateinamen der Dateien, die in dem angegebenen Pfad (und seinen
     * Untervezeichnissen) abgelegt.
     *
     * @param pfad Der Pfad, der als Basis f&uuml;r die Dateinamensuche dienen soll.
     * @return Ein Vector&lt;String&gt; mit den Dateinamen der gefundenen Dateien.
     */
    public static List<String> getFilenames(String pfad) {
        File f = new File(pfad);
        List<String> files = new LinkedList<String>();
        addFiles(files, f);
        return files;
    }

    /**
     * Verschiebt die angegebene Datei an den vorgegebenen Zielort.
     *
     * @since 1.20.1
     *
     * @param fnsrc Der Name der Datei, die verschoben werden soll.
     * @param fntrgt Der Name der Datei oder des Verzeichnisses, das als Zielort f&uuml;r das
     *         Verschieben herhalten soll.
     * @throws FileNotFoundException Falls die Quelldatei nicht existiert.
     * @throws IllegalArgumentException Falls einer beiden Parameter als <TT>null</TT>-Referenz
     *         &uuml;bergeben wird.
     * @throws IOException Falls beim Kopieren der Datei ein Fehler auftritt.
     * @precondition fnsrc != <TT>null</TT>
     * @precondition fntrgt != <TT>null</TT>
     * @precondition new File(fnsrc).exists()
     * @precondition target directory exists
     *
     * @changed OLI 23.02.2010 - Hinzugef&uuml;gt.
     */
    public static void moveFile(String fnsrc, String fntrgt) throws FileNotFoundException,
            IllegalArgumentException, IOException {
        File f = null;
        try {
            FileUtil.copyFile(fnsrc, fntrgt);
        } catch (FileNotFoundException fnfe) {
            throw fnfe;
        } catch (IOException ioe) {
            String msg = "error while move file " + fnsrc + " to " + fntrgt + ".";
            try {
                f = new File(fntrgt);
                if (f.exists()) {
                    f.delete();
                }
            } catch (Exception e) {
                msg = msg.concat(" can not remove copied file.");
            }
            throw new IOException(msg);
        }
        new File(fnsrc).delete();
    }

    /**
     * &Uuml;berschreibt die angegebene Datei in ihrer G&auml;nze mit dem char 0xFF.
     *
     * @since 1.23.1
     *
     * @param fileName Der Name der Datei, die &uuml;berschrieben werden soll.
     * @throws IllegalArgumentException Falls der Dateiname als <TT>null</TT>-Referenz
     *         &uuml;bergeben wird.
     * @throws IOException Falls beim Zugriff auf die Datei ein Fehler auftritt.
     * @precondition fileName != <TT>null</TT>
     *
     * @changed OLI 08.02.2011 - Hinzugef&uuml;gt.
     */
    public static void overrideWithAsterix(String fileName) throws IOException {
        ensure(fileName != null, "file name cannot be null.");
        File fileToOverride = new File(fileName);
        long fileSize = fileToOverride.length();
        FileOutputStream fos = new FileOutputStream(fileToOverride);
        for (long i = 0; i < fileSize; i++) {
            fos.write('*');
        }
        fos.flush();
        fos.close();
    }

    /**
     * Liest die angegebene Property-Datei in die Systemproperties ein.
     *
     * @since 1.9.1
     *
     * @param pfn Der Name der Datei, aus der die Properties gelesen werden sollen.
     * @throws FileNotFoundException Falls es keine Datei unter dem angegebenen Namen gibt.
     * @throws IOException Falls es beim Einlesen der Datei zu einem Fehler kommt.
     * @throws NullPointerException Falls der Name der Property-Datei als <TT>null</TT>-Referenz
     *         &uuml;bergeben wird.
     *
     * @changed OLI 14.10.2009 - &Uuml;bernahme aus <TT>archimedes.app.ApplicationUtil</TT>
     */
    @Deprecated
    public static void readProperties(String pfn) throws FileNotFoundException, IOException {
        FileUtil.readProperties(System.getProperties(), pfn);
    }

    /**
     * Liest die angegebene Property-Datei in die angegebenen Properties ein. Es wird
     * entsprechender Output auf der Konsole erzeugt.
     *
     * @since 1.9.1
     *
     * @param p Die Properties, in die die Propertiesdatei eingelesen werden soll.
     * @param pfn Der Name der Datei, aus der die Properties gelesen werden sollen.
     * @throws FileNotFoundException Falls es keine Datei unter dem angegebenen Namen gibt.
     * @throws IOException Falls es beim Einlesen der Datei zu einem Fehler kommt.
     * @throws NullPointerException Falls der Name der Property-Datei oder die Properties als
     *         <TT>null</TT>-Referenz &uuml;bergeben werden.
     *
     * @changed OLI 14.10.2009 - &Uuml;bernahme aus <TT>archimedes.app.ApplicationUtil</TT>
     */
    public static void readProperties(Properties p, String pfn) throws FileNotFoundException,
            IOException {
        File f = new File(pfn);
        FileInputStream fis = new FileInputStream(f);
        p.load(fis);
        fis.close();
    }

    /**
     * Liest eine Liste von Strings aus der angegebenen Datei. Jede Zeile ergibt ein
     * Listenelement. Zeilen, die mit der Raute (#) beginnen, werden als Kommentar gewertet
     * und ignoriert. Leere Zeilen werden ebenfalls ignoriert.
     *
     * @param filename Der Name der Datei, aus der der Text gelesen werden soll.
     * @param trim Wenn diese Flagge gesetzt wird, wird die eingelesene Zeile vor allen anderen
     *         Aktionen getrimmt (d. h. f&uuml;hrende und abschliessende Leerzeichen werden
     *         entfernt.
     * @return Eine Liste mit den aus der Datei gelesenen Strings.
     * @throws FileNotFoundException Weiterreichen der durch den Lesezugriff entstehenden
     *         Exceptions.
     * @throws IOException Weiterreichen der durch den Lesezugriff entstehenden Exceptions.
     *
     * @changed OLI 13.02.2009 - Hinzugef&uuml;gt.
     */
    public static List<String> readStringList(String filename, boolean trim)
            throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(filename);
        BufferedReader reader = new BufferedReader(fr);
        List<String> list = new Vector<String>();
        String line = "";
        line = reader.readLine();
        while (line != null) {
            if (trim) {
                line = line.trim();
            }
            if (!line.startsWith("#") && (line.length() > 0)) {
                list.add(line);
            }
            line = reader.readLine();
        }
        reader.close();
        fr.close();
        return list;
    }

    /**
     * Liest eine Liste von Strings aus der angegebenen Datei. Jede Zeile ergibt ein
     * Listenelement. Zeilen, die mit der Raute (#) beginnen, werden als Kommentar gewertet
     * und ignoriert. Leere Zeilen werden ebenfalls ignoriert.
     *
     * @param filename Der Name der Datei, aus der der Text gelesen werden soll.
     * @param trim Wenn diese Flagge gesetzt wird, wird die eingelesene Zeile vor allen anderen
     *         Aktionen getrimmt (d. h. f&uuml;hrende und abschliessende Leerzeichen werden
     *         entfernt.
     * @param encoding Eine Angabe zum Encoding der Datei.
     * @return Eine Liste mit den aus der Datei gelesenen Strings.
     * @throws FileNotFoundException Weiterreichen der durch den Lesezugriff entstehenden
     *         Exceptions.
     * @throws IOException Weiterreichen der durch den Lesezugriff entstehenden Exceptions.
     *
     * @changed OLI 13.02.2009 - Hinzugef&uuml;gt.
     */
    public static List<String> readStringList(String filename, boolean trim, String encoding)
            throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(filename);
        InputStreamReader isr = new InputStreamReader(fis, encoding);
        BufferedReader reader = new BufferedReader(isr);
        List<String> list = new Vector<String>();
        String line = "";
        line = reader.readLine();
        while (line != null) {
            if (trim) {
                line = line.trim();
            }
            if (!line.startsWith("#") && (line.length() > 0)) {
                list.add(line);
            }
            line = reader.readLine();
        }
        reader.close();
        isr.close();
        fis.close();
        return list;
    }

    /**
     * Liest einen Text (mit Zeilenumbr&uuml;chen) aus der angegebenen Datei.
     *
     * @param filename Der Name der Datei, aus der der Text gelesen werden soll.
     * @return Der Text, der aus der Datei gelesen worden ist.
     * @throws FileNotFoundException Weiterreichen der durch den Lesezugriff entstehenden
     *         Exceptions.
     * @throws IOException Weiterreichen der durch den Lesezugriff entstehenden Exceptions.
     *
     * @changed OLI 23.07.2008 - Aus corent.tools.Tools hergezogen.
     * @changed OLI 03.04.2009 - Debugging.
     */
    public static String readTextFromFile(String filename) throws FileNotFoundException,
            IOException {
        String ls = System.getProperty("line.separator");
        String s = readTextFromFile(filename, null);
        return s.concat(((s.length() > 0) && !s.endsWith(ls) ? ls : ""));
    }

    /**
     * Liest einen Text (mit Zeilenumbr&uuml;chen) aus der angegebenen Datei. Es kann eine
     * Kommentarsequenz vereinbart werden, mit deren Hilfe Zeilen aus dem Lesevorgang
     * ausgeklammert werden k&ouml;nnen. Die Kommentarsequenz mu&szlig; am Anfang der Zeile
     * stehen.
     *
     * @param filename Der Name der Datei, aus der der Text gelesen werden soll.
     * @param commentSequence Eine Sequenz, die den Anfang von zu ignorierenden Kommentarzeilen
     *         kennzeichnet. Wird hier der Wert <TT>null</TT> &uuml;bergeben, so wird keine
     *         Zeile auskommentiert.
     * @return Der Text, der aus der Datei gelesen worden ist.
     * @throws FileNotFoundException Weiterreichen der durch den Lesezugriff entstehenden
     *         Exceptions. 
     * @throws IOException Weiterreichen der durch den Lesezugriff entstehenden Exceptions.
     *
     * @changed OLI 02.04.2009 - Hinzugef&uuml;gt.
     */
    public static String readTextFromFile(String filename, String commentSequence)
            throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(filename);
        BufferedReader reader = new BufferedReader(fr);
        String ls = System.getProperty("line.separator");
        String line = "";
        StringBuffer text = new StringBuffer();
        line = reader.readLine();
        while (line != null) {
            if ((commentSequence == null) || (!line.startsWith(commentSequence))) {
                if (text.length() > 0) {
                    text.append(ls);
                }
                text.append(line);
            }
            line = reader.readLine();
        }
        reader.close();
        fr.close();
        return text.toString();
    }

    /**
     * Liefert die letzten n (=lines) Zeilen der durch den Namen angegebenen Textdatei.
     *
     * @param fn Der Name der Datei, deren n letzte Zeilen gelesen werden sollen.
     * @param lines Die Anzahl der Zeilen des Dateiendes, die gelesen werden sollen.
     * @return Eine Liste (Strings) mit den letzten n (=lines) Zeilen der Datei.
     * @throws FileNotFoundException Falls die angegebene Datei nicht existiert.
     * @throws IOException Weiterreichen der durch den Schreibzugriff entstehenden Exceptions.
     *
     * @changed OLI 03.04.2009 - Hinzugef&uuml;gt.
     */
    public static List<String> tail(String fn, int lines) throws FileNotFoundException, 
            IOException {
        FileReader fr = new FileReader(new File(fn));
        BufferedReader br = new BufferedReader(fr);
        String liner = br.readLine();
        String line = null;
        Vector<String> tail = new Vector<String>();
        while (liner != null) {
            line = liner;
            tail.addElement(line);
            if (tail.size() > lines) {
                tail.removeElementAt(0);
            }
            liner = br.readLine();
        }
        br.close();
        fr.close();
        return tail;
    }

    /**
     * Schreibt die System-Properties in eine Datei mit dem angegebenen Namen.
     *
     * @since 1.22.2
     *
     * @param pfn Der Name der Datei, in die die Properties geschrieben werden sollen.
     * @param comment Ein Kommentar zur Propertiesdatei.
     * @throws IOException Falls es beim Schreiben der Datei zu einem Fehler kommt.
     * @throws NullPointerException Falls der Name der Property-Datei als <TT>null</TT>-Referenz
     *         &uuml;bergeben wird.
     * @precondition pfn != <TT>null</TT>
     *
     * @changed OLI 29.09.2010 - Hinzugef&uuml;gt.
     */
    public static void writeProperties(String pfn, String comment) throws FileNotFoundException,
            IOException {
        FileUtil.writeProperties(System.getProperties(), pfn, comment);
    }

    /**
     * Schreibt die angegebenen Properties in eine Datei mit dem angegebenen Namen.
     *
     * @since 1.22.2
     *
     * @param p Die Properties, die in die Propertiesdatei geschrieben werden sollen.
     * @param pfn Der Name der Datei, in die die Properties geschrieben werden sollen.
     * @param comment Ein Kommentar zur Propertiesdatei.
     * @throws IOException Falls es beim Schreiben der Datei zu einem Fehler kommt.
     * @throws NullPointerException Falls der Name der Property-Datei oder die Properties als
     *         <TT>null</TT>-Referenz &uuml;bergeben werden.
     * @precondition p != <TT>null</TT>
     * @precondition pfn != <TT>null</TT>
     *
     * @changed OLI 29.09.2010 - Hinzugef&uuml;gt.
     */
    public static void writeProperties(Properties p, String pfn, String comment)
            throws FileNotFoundException, IOException {
        File f = new File(pfn);
        FileOutputStream fos = new FileOutputStream(f);
        p.store(fos, comment);
        fos.close();
    }

    /**
     * Schreibt den &uuml;bergebenen Text als Textdatei mit dem angegebenen filename.
     *
     * @param filename Der Name der Datei, in die der angegebene Text geschrieben werden soll.
     * @param append Die Append-Flagge zum &Ouml;ffnen der Datei.
     * @param text Der Text, der in die angegebene Datei geschrieben werden soll.
     * @throws IOException Weiterreichen der durch den Schreibzugriff entstehenden Exceptions.
     *
     * @changed OLI 23.07.2008 - Aus corent.tools.Tools hergezogen.
     */
    public static void writeTextToFile(String filename, boolean append, String text)
            throws IOException {
        FileWriter fw = new FileWriter(filename, append);
        BufferedWriter writer = new BufferedWriter(fw);
        writer.write(text);
        writer.flush();
        writer.close();
        fw.close();
    }

}
