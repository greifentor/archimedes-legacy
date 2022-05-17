/*
 * Creates Sequence updates for PostgreSQL.
 *
 * @author O.Lieshoff
 */

pattern = "select setval('seq_{TN}', (select max(id)+1 from {TN}));";
s= "";
v = model.getTables();
for (i = 0, leni = v.length; i < leni; i++) {
    if ((v[i].getPrimaryKeyColumns().length == 0) || v[i].getName().endsWith("_hist")) {
        continue;
    }
    s = pattern.replace("{TN}", v[i].getName()).replace("{TN}", v[i].getName()) + "\n";
}
clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
clipboard.setContents(new java.awt.datatransfer.StringSelection(s), null);
