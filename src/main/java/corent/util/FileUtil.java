/*
 * FileUtil.java
 *
 * 01.11.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corent.util;


import java.io.*;
import java.util.*;


/**
 * Diese Klasse bietet eine Handvoll Methoden an, die f&uuml;r die Arbeit mit dem Dateisystem
 * gedacht sind.
 *
 * @author 
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 23.07.2008 - Erweiterung um die TextFile-Methoden (WriteTextToFile(String, boolean, 
 *             String) und ReadTextFromFile(String)) aus der Klasse corent.tools.Tools.
 *     <P>OLI 12.02.2009 - Erweiterung um die Methode <TT>CompletePath(String)</TT>.
 *     <P>OLI 13.02.2009 - Erweiterung um die Methode <TT>ReadStringList(String)</TT>.
 *     <P>OLI 02.04.2009 - Erweiterung um die Methode <TT>ReadTextFromFile(String, String)</TT>.
 *     <P>OLI 03.04.2009 - Debugging an der Methode <TT>ReadTextFromFile(String)</TT>.
 *     <P>
 *
 */

public class FileUtil {

    private static void AddFiles(Vector<String> v, File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    AddFiles(v, files[i]);
                }
            }
        } else {
            v.addElement(f.getAbsolutePath());
        }
    }

    /**
     * Vervollst&auml;ndigt gegebenenfalls ein Pfadangabe mit einem abschliessenden Slash. Bei
     * Dieser Gelegenheit werden alle Backslashes in dem String in Slashes umgesetzt.
     *
     * @param path Ein Pfad zur Vervollst&auml;ndigung.
     * @return Ein mit einem Slash abschliessender Pfad, dessen Backslashes gegen Slashes
     *         ersetzt worden sind.
     *
     * @changed
     *     OLI 12.02.2009 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public static String CompletePath(String path) {
        path = path.replace("\\", "/");
        if (!path.endsWith("/")) {
            path = path.concat("/");
        }
        return path;
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
    public static String GetExtension(File f) {
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
    public static Vector<String> GetFilenames(String pfad) {
        File f = new File(pfad);
        Vector<String> files = new Vector<String>();
        AddFiles(files, f);
        return files;
    }

    /**
     * Ermittelt den Pfad zum angegebenen Dateinamen. Es wird immer der erste Treffer 
     * zur&uuml;ckgegeben.
     *
     * @param f Das Fectory, in dem die Suche starten soll.
     * @param fn Der Dateiname, nach dem gesucht werden soll.
     * @param echo Wird diese Flagge gesetzt, wird der Name der aktuell gepr&uuml;ften Datei auf
     *     der Konsole ausgegeben.
     * @return Der vollst&auml;ndige Pfad der Datei, sofern eine des angegebenen Namens gefunden
     *     wird. Sonst <TT>null</TT>.
     *
     * @changed
     *     OLI 23.07.2008 - Hinzugef&uuml;gt.
     *
     */
    public static String FindPath(File f, String fn, boolean echo) {
        return FindPath(f, fn, echo, false, null);
    }

    /**
     * Ermittelt den Pfad zum angegebenen Dateinamen. Es wird immer der erste Treffer 
     * zur&uuml;ckgegeben.
     *
     * @param f Das Fectory, in dem die Suche starten soll.
     * @param fn Der Dateiname, nach dem gesucht werden soll.
     * @param echo Wird diese Flagge gesetzt, wird der Name der aktuell gepr&uuml;ften Datei auf
     *     der Konsole ausgegeben.
     * @param gcs Wird diese Flagge gestetzt, so wird nach dem Durcharbeiten eines jeden 
     *     Unterverzeichnisses eine Garbage-Collection durchgef&uuml;hrt. Wird zudem die 
     *     echo-Flagge gesetzt, so wird hier auch der freie Speicher in bytes ausgegeben.
     * @param pfl Referenz auf einen PathFinderListener, der &uuml;ber das gerade untersuchte 
     *     File informiert wird (z. B. zur Realisation einer Anzeigefunktion).
     * @return Der vollst&auml;ndige Pfad der Datei, sofern eine des angegebenen Namens gefunden
     *     wird. Sonst <TT>null</TT>.
     *
     * @changed
     *     OLI 23.07.2008 - Hinzugef&uuml;gt und Inhalt aus FindPath(File, String, boolean) 
     *             &uuml;bernommen.
     *     <P>OLI 24.07.2008 - Ersatz durch PathFinder-Aufruf.
     *
     */
    public static String FindPath(File f, String fn, boolean echo, boolean gcs, 
            corent.files.PathFinderListener pfl) {
        corent.files.PathFinder pf = new corent.files.PathFinder();
        return pf.find(f, fn, echo, gcs, pfl);
    }

    /**
     * Die Implementierung der Main-Methode dient dem Text einiger Methoden der Klasse.
     *
     * @param args Die Kommandozeilenparameter des Aufrufs.
     *
     * @changed
     *     OLI 23.07.2008 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public static void main(String[] args) {
        File[] roots = File.listRoots();
        // System.out.println(FindPath(new File("/home/ollie"), "caesar.gif", true));
        for (int i = 0; i < roots.length; i++) {
            System.out.println(FindPath(roots[i], args[0], true));
        }
        System.exit(0);
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
     *     Exceptions. 
     * @throws IOException Weiterreichen der durch den Lesezugriff entstehenden Exceptions.
     *
     * @changed
     *     OLI 13.02.2009 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public static List<String> ReadStringList(String filename, boolean trim) 
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
     * Liest einen Text (mit Zeilenumbr&uuml;chen) aus der angegebenen Datei.
     *
     * @param filename Der Name der Datei, aus der der Text gelesen werden soll.
     * @return Der Text, der aus der Datei gelesen worden ist.
     * @throws FileNotFoundException Weiterreichen der durch den Lesezugriff entstehenden 
     *     Exceptions. 
     * @throws IOException Weiterreichen der durch den Lesezugriff entstehenden Exceptions.
     *
     * @changed
     *     OLI 23.07.2008 - Aus corent.tools.Tools hergezogen.
     *     <P>OLI 03.04.2009 - Debugging: Der 
     *
     */
    public static String ReadTextFromFile(String filename) throws FileNotFoundException, 
            IOException {
        String ls = System.getProperty("line.separator");
        String s = ReadTextFromFile(filename, null);
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
     * @changed
     *     OLI 02.04.2009 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public static String ReadTextFromFile(String filename, String commentSequence) 
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
     * @changed
     *     OLI 03.04.2009 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public static List<String> Tail(String fn, int lines) throws FileNotFoundException, 
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
     * Schreibt den &uuml;bergebenen Text als Textdatei mit dem angegebenen filename.
     *
     * @param filename Der Name der Datei, in die der angegebene Text geschrieben werden soll.
     * @param append Die Append-Flagge zum &Ouml;ffnen der Datei.
     * @param text Der Text, der in die angegebene Datei geschrieben werden soll.
     * @throws IOException Weiterreichen der durch den Schreibzugriff entstehenden Exceptions.
     *
     * @changed
     *     OLI 23.07.2008 - Aus corent.tools.Tools hergezogen.
     *     <P>
     *
     */
    public static void WriteTextToFile(String filename, boolean append, String text) 
            throws IOException {
        FileWriter fw = new FileWriter(filename, append);
        BufferedWriter writer = new BufferedWriter(fw);
        writer.write(text);
        writer.flush();
        writer.close();
        fw.close();
    }

}
