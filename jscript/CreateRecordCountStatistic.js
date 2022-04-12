/*
 * Creates a table with a record count statistic over all tables of the data scheme.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 19.02.2021 - Erzeugt.
 */

s = "create table if not exists tmp_counts (table_name varchar(255), record_count int);\n\n";
s = s + "delete from tmp_counts;\n\n";
v = model.getTables();
for (i = 0, leni = v.length; i < leni; i++) {
    s = s + "insert into tmp_counts (table_name, record_count) select '" + v[i].getName() + "', count(*) from " + v[i].getName() + ";\n";
}
s = s + "\nselect * from tmp_counts where record_count < 1 order by table_name;\n";
clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
clipboard.setContents(new java.awt.datatransfer.StringSelection(s), null);
