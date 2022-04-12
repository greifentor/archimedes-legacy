/*
 * Lists all qualified field names which containing "searchForParticle" on console and copies it to the clipboard.
 *
 * @author O.Lieshoff
 *
 * @changed OLI (05.10.2021)
 */

v = model.getAlleFelder();
s = "";
searchForParticle = "PARTICLE";

for (j = 0, lenj = v.size(); j < lenj; j++) {
    sp = v.get(j);
    if (sp.getName().contains(searchForParticle)) {
        if (!s.isEmpty()) {
            s = s + ", ";
        }
        s = s + sp.getFullName();
    }
}

java.lang.System.out.println(s);

clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
clipboard.setContents(new java.awt.datatransfer.StringSelection(s), null);