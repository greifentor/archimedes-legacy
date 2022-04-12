/*
 * Creates a class for PlantUML
 *
 * @author O.Lieshoff
 *
 * @changed OLI (03.11.2021)
 */

s = "";
searchForTable = "TABLE_NAME";

table = model.getTabelle(searchForTable);

s = s + "class " + table.getName() + "{\n";

columns = table.getAlleTabellenspalten();
for (j = 0, lenj = columns.size(); j < lenj; j++) {
    sp = columns.get(j);
    s = s + "\t" + sp.getName() + " :" + sp.getDomain().getType() + "\n";
}

s = s + "}\n";

java.lang.System.out.println(s);

clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
clipboard.setContents(new java.awt.datatransfer.StringSelection(s), null);
