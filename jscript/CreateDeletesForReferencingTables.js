tableNameToDelete = "TABLE_NAME";
rts = getReferencingTables(model.getTableByName(tableNameToDelete), new java.util.ArrayList());
s = "";
java.lang.System.out.println("\n\nRESULT:\n");
for (i = 0, leni = rts.size(); i < leni; i++) {
	s = s + "DELETE FROM " + rts.get(i).getName().toUpperCase() + ";\n";
}
s = s + "DELETE FROM " + tableNameToDelete.toUpperCase() + ";\n";

java.lang.System.out.println(s);

clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
clipboard.setContents(new java.awt.datatransfer.StringSelection(s), null);


function getReferencingTables(table, l) {
	var allFields = model.getAlleFelder();
	for (var j = 0, lenj = allFields.size(); j < lenj; j++) {
		var field = allFields.get(j);
		if ((table != null) && (field.getReferencedTable() == table)) {
			if (!l.contains(field.getTable())) {
				l.add(field.getTable());
				getReferencingTables(field.getTable(), l);
			}
		}
	}
	return l;
}
