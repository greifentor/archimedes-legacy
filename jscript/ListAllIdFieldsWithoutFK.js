/*
 * Lists all qualified field names which are potential references without FK.
 *
 * @author O.Lieshoff
 *
 * @changed OLI (08.08.2022)
 */

v = model.getAlleFelder();
s = "";

for (j = 0, lenj = v.size(); j < lenj; j++) {
    sp = v.get(j);
    if ((sp.getName().toLowerCase().endsWith("_id") || sp.getName().toLowerCase().startsWith("id_")) 
    		&& (sp.getReferencedTable() == null)) {
        if (!s.isEmpty()) {
            s = s + "\n";
        }
        s = s + sp.getFullName();
    }
}

java.lang.System.out.println(s);

clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
clipboard.setContents(new java.awt.datatransfer.StringSelection(s), null);