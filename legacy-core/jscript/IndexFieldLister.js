/*
 * Lists all field whose index flag is set and copies this list to the clipboard.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.04.2013 - Added.
 */

s = "";
vt = model.getTabellen();
for (j = 0, lenj = vt.size(); j < lenj; j++)  {
    t = vt.get(j);
    for (i = 0, leni = t.getTabellenspaltenCount(); i < leni; i++) {
        ts = t.getTabellenspalteAt(i);
        if (ts.isIndexed()) {
            s = s + "* " + ts.getFullName() + "\n";
        }
    }
}
clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
clipboard.setContents(new java.awt.datatransfer.StringSelection(s), null);
