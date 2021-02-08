/*
 * Listet die Namen aller  Tabellen des Datenmodells auf und &uuml;bertr&auml;gt diese Liste auf das Clipboard.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 06.11.2019 - Erzeugt.
 */

s = "";
v = model.getTables();
for (i = 0, leni = v.length; i < leni; i++) {
    s = s + i + "> " + v[i].getName().concat("\n");
}
clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
clipboard.setContents(new java.awt.datatransfer.StringSelection(s), null);
