/*
 * Liest alle Felder des Datenmodells mit Tabelle, Name, Typ und gegebenenfalls referenzierter Spalte und
 * &uuml;bertr&auml;gt diese Liste auf das Clipboard.
 *
 * @author O.Lieshoff
 *
 * @changed OLI (18.05.2020)
 */

s = "";

v = model.getAlleFelder();

tables = model.getTabellen();

for (j = 0, lenj = tables.size(); j < lenj; j++) {
    t = tables.get(j);
    columns = t.getTabellenspalten();
    for (i = 0, leni = columns.size(); i < leni; i++) {
        ts = columns.get(i);
        s = s + ts.getTable().getName() + "|" + ts.getName() + "|" + ts.getDomain().getType() + "|" + getRefString(ts) + "\n";
    }
}

clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
clipboard.setContents(new java.awt.datatransfer.StringSelection(s), null);


function getRefString(ts) {
    if ((ts.getRelation() != null) && (ts.getRelation().getReferenced() != ts)) {
        return "referenziert " + ts.getRelation().getReferenced();
    }
    return "";
}