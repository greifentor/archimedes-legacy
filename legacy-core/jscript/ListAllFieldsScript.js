/*
 * Liest alle Felder des Datenmodells mit qualifiziertem Namen auf und &uuml;bertr&auml;gt diese
 * Liste auf das Clipboard.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.08.2011 - Hinzugef&uuml;gt.
 */

s = "";
v = model.getAlleFelder();
for (i = 0, leni = v.size(); i < leni; i++) {
    s = s + i + "> " + v.get(i).getFullName().concat("\n");
}
clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
clipboard.setContents(new java.awt.datatransfer.StringSelection(s), null);
