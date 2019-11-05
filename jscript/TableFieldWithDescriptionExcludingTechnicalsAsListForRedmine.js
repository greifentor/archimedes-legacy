/*
 * Liest alle nicht technischen Felder der angegebenen Tabelle und schreibt sie mit Beschreibung
 * in einem Format, das f&uuml;r den Import nach Redmine geeignet ist, in das Clipboard.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.08.2011 - Hinzugef&uuml;gt.
 */

s = "";
t = model.getTabelle("MonitorStaffAlertCallStaff");
for (i = 0, leni = t.getTabellenspaltenCount(); i < leni; i++) {
    ts = t.getTabellenspalteAt(i);
    if (!ts.isTechnicalField()) {
        s = s + "* " + ts.getName() + " - " + ts.getBeschreibung().replace("\n", "") + "\n";
    }
}
clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
clipboard.setContents(new java.awt.datatransfer.StringSelection(s), null);
