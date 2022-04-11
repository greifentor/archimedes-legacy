/*
 * Creates code snipet with a set of Java constants for each field of the table
 * with the assigned name and copies this to the clipboard.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 24.11.2021 - Added.
 */
 
tableName = "TABLE_NAME";

bigintCounter = 0;
dayCounter = 1;
doubleCounter = 11.11;
intCounter = 0;

s = "";

t = model.getTableByName(tableName);
if (t == null) {
	throw new java.util.NoSuchElementException("table not found with name: " + tableName);
}
for (i = 0, leni = t.getTabellenspaltenCount(); i < leni; i++) {
	ts = t.getTabellenspalteAt(i);
	s = s + "\tprivate static final " + getType(ts) + " " + ts.getName().toUpperCase() + " = " + getDefaultValue(ts) + ";\n";
}

clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
clipboard.setContents(new java.awt.datatransfer.StringSelection(s), null);


function getType(ts) {
	type = ts.getDomain().getType().toLowerCase();
	if (type.equals("bigint")) {
		return "Long";
	} else if (type.equals("boolean")) {
		return "Boolean";
	} else if (type.equals("date")) {
		return "LocalDate";
	} else if (type.equals("double") || type.startsWith("numeric")) {
		return "Double";
	} else if (type.equals("int")) {
		return "Integer";
	} else if (type.equals("timestamp")) {
		return "LocalDateTime";
	}
	return "String";
}

function getDefaultValue(ts) {
	type = getType(ts);
	if (type.equals("Boolean")) {
		return "true";
	} else if (type.equals("Double")) {
		s = "" + doubleCounter + "D";
		doubleCounter = doubleCounter * 2;
		return s;
	} else if (type.equals("LocalDate")) {
		return "LocalDate.now().plusDays(" + dayCounter++ + ")";
	} else if (type.equals("LocalDateTime")) {
		return "LocalDateTime.now().plusDays(" + dayCounter++ + ")";
	} else if (type.equals("Long")) {
		return "" + ++bigintCounter + "L";
	} else if (type.equals("Integer")) {
		return "" + ++intCounter;
	}
	content = ts.getName().toLowerCase();
	while (content.contains("_")) {
		content = content.replace("_", " ");
	}
	return "\"" + content + "\"";
}