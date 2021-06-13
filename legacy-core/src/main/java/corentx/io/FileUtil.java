/*
 * FileUtil.java
 *
 * 01.11.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Diese Klasse bietet eine Handvoll Methoden an, die f&uuml;r die Arbeit mit dem Dateisystem gedacht sind.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 23.07.2008 - Erweiterung um die TextFile-Methoden (WriteTextToFile(String, boolean, String) und
 *          ReadTextFromFile(String)) aus der Klasse corent.tools.Tools.
 * @changed OLI 12.02.2009 - Erweiterung um die Methode <TT>CompletePath(String)</TT>.
 * @changed OLI 13.02.2009 - Erweiterung um die Methode <TT>ReadStringList(String)</TT>.
 * @changed OLI 02.04.2009 - Erweiterung um die Methode <TT>ReadTextFromFile(String,
 *         String)</TT>.
 * @changed OLI 03.04.2009 - Debugging an der Methode <TT>ReadTextFromFile(String)</TT>.
 * @changed OLI 09.09.2009 - &Uuml;bernahme aus <TT>corent.util</TT>.
 * @changed OLI 23.02.2010 - Erweiterung um die Methoden <TT>copyFile(String, String)</TT> und
 *          <TT>moveFile(String, String)</TT>.
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
	 * Vervollst&auml;ndigt gegebenenfalls ein Pfadangabe mit einem abschliessenden Slash. Bei Dieser Gelegenheit werden
	 * alle Backslashes in dem String in Slashes umgesetzt.
	 *
	 * @param path Ein Pfad zur Vervollst&auml;ndigung.
	 * @return Ein mit einem Slash abschliessender Pfad, dessen Backslashes gegen Slashes ersetzt worden sind.
	 * @throws NullPointerException Falls der Pfad als <TT>null</TT>-Referenz &uuml;bergeben wird.
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
	 * Diese Methode entfernt das letzte Element aus der Pfadangabe (bis zum letzten Slash von hinten).
	 *
	 * @param p Der Pfad, dessen letztes Element entfernt werden soll.
	 * @throws IllegalArgumentException Falls der Pfad als <TT>null</TT>-Referenz &uuml;bergeben wird.
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
		for (i = p.length() - 1; i >= 0; i--) {
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
	 * <P>
	 * <I>Beispiel: <BR>
	 * GetExtension(new File("beschreibung.txt.pdf") =&gt; "pdf". <BR>
	 * GetExtension(new File("beschreibung.txt") =&gt; "txt". <BR>
	 * GetExtension(new File("beschreibung") =&gt; "". </I>
	 *
	 * @param f Das File dessen Extension ermittelt werden soll.
	 * @return Die Erweiterung im Dateiname oder "", wenn es keine gibt.
	 */
	public static String getExtension(File f) {
		String ext = "";
		String fn = f.getName();
		while (fn.indexOf(".") > -1) {
			if (fn.length() > fn.indexOf(".") + 1) {
				fn = fn.substring(fn.indexOf(".") + 1, fn.length());
			} else {
				fn = "";
			}
			ext = fn;
		}
		return ext;
	}

	/**
	 * Liefert eine Liste aller Dateinamen der Dateien, die in dem angegebenen Pfad (und seinen Untervezeichnissen)
	 * abgelegt.
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
	 * Liest die angegebene Property-Datei in die angegebenen Properties ein. Es wird entsprechender Output auf der
	 * Konsole erzeugt.
	 *
	 * @since 1.9.1
	 *
	 * @param p   Die Properties, in die die Propertiesdatei eingelesen werden soll.
	 * @param pfn Der Name der Datei, aus der die Properties gelesen werden sollen.
	 * @throws FileNotFoundException Falls es keine Datei unter dem angegebenen Namen gibt.
	 * @throws IOException           Falls es beim Einlesen der Datei zu einem Fehler kommt.
	 * @throws NullPointerException  Falls der Name der Property-Datei oder die Properties als <TT>null</TT>-Referenz
	 *                               &uuml;bergeben werden.
	 *
	 * @changed OLI 14.10.2009 - &Uuml;bernahme aus <TT>archimedes.app.ApplicationUtil</TT>
	 */
	public static void readProperties(Properties p, String pfn) throws FileNotFoundException, IOException {
		File f = new File(pfn);
		FileInputStream fis = new FileInputStream(f);
		p.load(fis);
		fis.close();
	}

	/**
	 * Liest einen Text (mit Zeilenumbr&uuml;chen) aus der angegebenen Datei.
	 *
	 * @param filename Der Name der Datei, aus der der Text gelesen werden soll.
	 * @return Der Text, der aus der Datei gelesen worden ist.
	 * @throws FileNotFoundException Weiterreichen der durch den Lesezugriff entstehenden Exceptions.
	 * @throws IOException           Weiterreichen der durch den Lesezugriff entstehenden Exceptions.
	 *
	 * @changed OLI 23.07.2008 - Aus corent.tools.Tools hergezogen.
	 * @changed OLI 03.04.2009 - Debugging.
	 */
	public static String readTextFromFile(String filename) throws FileNotFoundException, IOException {
		String ls = System.getProperty("line.separator");
		String s = readTextFromFile(filename, null);
		return s.concat(((s.length() > 0) && !s.endsWith(ls) ? ls : ""));
	}

	/**
	 * Liest einen Text (mit Zeilenumbr&uuml;chen) aus der angegebenen Datei. Es kann eine Kommentarsequenz vereinbart
	 * werden, mit deren Hilfe Zeilen aus dem Lesevorgang ausgeklammert werden k&ouml;nnen. Die Kommentarsequenz
	 * mu&szlig; am Anfang der Zeile stehen.
	 *
	 * @param filename        Der Name der Datei, aus der der Text gelesen werden soll.
	 * @param commentSequence Eine Sequenz, die den Anfang von zu ignorierenden Kommentarzeilen kennzeichnet. Wird hier
	 *                        der Wert <TT>null</TT> &uuml;bergeben, so wird keine Zeile auskommentiert.
	 * @return Der Text, der aus der Datei gelesen worden ist.
	 * @throws FileNotFoundException Weiterreichen der durch den Lesezugriff entstehenden Exceptions.
	 * @throws IOException           Weiterreichen der durch den Lesezugriff entstehenden Exceptions.
	 *
	 * @changed OLI 02.04.2009 - Hinzugef&uuml;gt.
	 */
	private static String readTextFromFile(String filename, String commentSequence)
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
	 * Schreibt die angegebenen Properties in eine Datei mit dem angegebenen Namen.
	 *
	 * @since 1.22.2
	 *
	 * @param p       Die Properties, die in die Propertiesdatei geschrieben werden sollen.
	 * @param pfn     Der Name der Datei, in die die Properties geschrieben werden sollen.
	 * @param comment Ein Kommentar zur Propertiesdatei.
	 * @throws IOException          Falls es beim Schreiben der Datei zu einem Fehler kommt.
	 * @throws NullPointerException Falls der Name der Property-Datei oder die Properties als <TT>null</TT>-Referenz
	 *                              &uuml;bergeben werden.
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
	 * @param append   Die Append-Flagge zum &Ouml;ffnen der Datei.
	 * @param text     Der Text, der in die angegebene Datei geschrieben werden soll.
	 * @throws IOException Weiterreichen der durch den Schreibzugriff entstehenden Exceptions.
	 *
	 * @changed OLI 23.07.2008 - Aus corent.tools.Tools hergezogen.
	 */
	public static void writeTextToFile(String filename, boolean append, String text) throws IOException {
		FileWriter fw = new FileWriter(filename, append);
		BufferedWriter writer = new BufferedWriter(fw);
		writer.write(text);
		writer.flush();
		writer.close();
		fw.close();
	}

}
