/*
 * CreateStatementGenerator.java
 *
 * 06.10.2009
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql;

import static corentx.util.Checks.ensure;

import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.util.UniqueFormulaUtil;
import corent.db.DBExecMode;
import corentx.util.Str;

/**
 * Diese Utility-Klasse bietet Methoden, um aus Tabellen-Modellen
 * Create-Statements unter Ber&uuml;cksichtigung der Spezifika des DBMS zu
 * generieren.
 * 
 * @author ollie
 * 
 * @changed OLI 06.10.2009 - Hinzugef&uuml;gt
 * @changed OLI 10.06.2010 - Erweiterung der <TT>getType</TT>-Aufrufe den
 *          DBMS-Modus.
 * @changed OLI 30.03.2012 - Erweiterung um die Ber&uuml;cksichtigung der
 *          Unifikatsformel zur Codegenerierung.
 */

public class CreateStatementGenerator {

	/**
	 * Liefert die Anzahl der Tabellenspalten, die im Prim&auml;rschl&uuml;ssel
	 * enthalten sind, zum angegebenen Tabellemodell.
	 * 
	 * @param tm
	 *            Das Tabellenmodell, zu dem die Anzahl der
	 *            Prim&auml;rschl&uuml;sselmitglieder ermittelt werden soll.
	 * @return Die Anzahl der Tabellenspalten, die Mitglieder des
	 *         Prim&auml;rschl&uuml;ssels sind.
	 * @throws NullPointerException
	 *             Falls das Tabellenmodell als <TT>null</TT>-Referenz
	 *             &uuml;bergeben wird.
	 * @precondition tm != <TT>null</TT>.
	 */
	public static int getPKMembersCount(TableModel tm) throws NullPointerException {
		ensure(tm != null, "null table models don't have any key at normal case.");
		int pkcount = 0;
		for (ColumnModel c : tm.getColumns()) {
			pkcount = pkcount + (c.isPrimaryKey() ? 1 : 0);
		}
		return pkcount;
	}

	/**
	 * Liefert ein Create-Statement zum angegebenen Tabellenmodell unter
	 * Ber&uuml;cksichtigung der Spezifika des DBMS.
	 * 
	 * @param tm
	 *            Das Tabellenmodell, zu dem das Create-Statement unter
	 *            Ber&uuml;cksichtigung der Besonderheiten des DBMS erzeugt
	 *            werden soll.
	 * @param dbmode
	 *            Der DBMS-Modus, f&uuml;r den das Create-Statement erzeugt
	 *            werden soll.
	 * @param hasDomains
	 *            Diese Flagge ist zu setzen, wenn das Create-Statement mit
	 *            Domains erzeugt werden soll.
	 * @param nameQuotes
	 *            Eine Zeichenfolge, die vor und nach einem Tabellen- oder
	 *            Attributsnamen in das Create-Statement eingef&uuml;gt werden
	 *            soll. Wird der Parameter mit dem Wert <TT>null</TT>
	 *            &uuml;bergeben, so wird keine Zeichenfolge angegeben.
	 * @param ignoreNotNull
	 *            Wenn diese Flagge gesetzt wird, werden Not-Null-Constraints
	 *            ignoriert.
	 * @throws NullPointerException
	 *             Falls Tabellenmodell oder DBMS-Modus als <TT>null</TT>
	 *             -Referenzen &uuml;bergeben werden.
	 * @precondition dbmode != <TT>null</TT>.
	 * @precondition tm != <TT>null</TT>.
	 */
	public static String getCreateStatement(TableModel tm, DBExecMode dbmode, boolean hasDomains, String nameQuotes,
			boolean ignoreNotNull) throws NullPointerException {
		ensure(dbmode != null, "create statement can not be generated for null dbms.");
		ensure(tm != null, "null tables makes it heavy to generate create statements.");
		int i = 0;
		int leni = 0;
		int pkcount = CreateStatementGenerator.getPKMembersCount(tm);
		ColumnModel cm = null;
		StringBuffer sb = new StringBuffer("CREATE TABLE ").append(Str.quote(tm.getName(), nameQuotes)).append(" (\n");
		ColumnModel[] columns = tm.getColumns();
		for (i = 0, leni = columns.length; i < leni; i++) {
			cm = columns[i];
			sb.append("    ").append(Str.quote(cm.getName(), nameQuotes)).append(" ").append(
					SQLGeneratorUtil.getTypeString(hasDomains, cm.getDomain(), dbmode));
			if (((dbmode == DBExecMode.HSQL) || (dbmode == DBExecMode.MSSQL) || (dbmode == DBExecMode.POSTGRESQL))
					&& (cm.getDefaultValue() != null)) {
				sb.append(" DEFAULT ").append(SQLGeneratorUtil.getDefaultValue(cm, dbmode));
			}
			if (!ignoreNotNull && (cm.isNotNull() || cm.isPrimaryKey())) {
				sb.append(" NOT NULL");
			}
			if ((dbmode == DBExecMode.MYSQL) && (cm.getDefaultValue() != null)) {
				sb.append(" DEFAULT ").append(cm.getDefaultValue());
			}
			if (cm.isPrimaryKey() && (pkcount == 1)) {
				sb.append(" PRIMARY KEY");
			}
			if (cm.isUnique() && !cm.isPrimaryKey() && !(dbmode == DBExecMode.HSQL)) {
				sb.append(" UNIQUE");
			}
			if (i < leni - 1) {
				sb.append(",\n");
			}
		}
		if (dbmode == DBExecMode.HSQL) {
			for (ColumnModel c : columns) {
				if (c.isUnique()) {
					sb.append(",\n    UNIQUE(").append(Str.quote(c.getName(), nameQuotes)).append(")");
				}
			}
		}
		sb.append(createPrimaryKeyConstraint(tm, pkcount, nameQuotes));
		sb.append(createUniqueConstraint(((TabellenModel) tm).getComplexUniqueSpecification()));
		sb.append("\n);");
		return sb.toString();
	}

	private static String createPrimaryKeyConstraint(TableModel tm, int pkCount, String nameQuotes) {
		StringBuffer cnstrnt = new StringBuffer();
		if (pkCount > 1) {
			cnstrnt.append(",\n    PRIMARY KEY(");
			for (ColumnModel c : tm.getColumns()) {
				if (c.isPrimaryKey()) {
					cnstrnt.append(Str.quote(c.getName(), nameQuotes)).append((--pkCount > 0 ? ", " : ""));
				}
			}
			cnstrnt.append(")");
		}
		return cnstrnt.toString();
	}

	private static String createUniqueConstraint(String uniqueFormula) {
		StringBuffer cnstrnt = new StringBuffer();
		if ((uniqueFormula != null) && !uniqueFormula.isEmpty()) {
			String[] fieldNames = new UniqueFormulaUtil(uniqueFormula).getFieldNames();
			if (fieldNames.length > 0) {
				cnstrnt.append(",\n    UNIQUE(");
				for (int i = 0; i < fieldNames.length; i++) {
					if (i > 0) {
						cnstrnt.append(", ");
					}
					cnstrnt.append(fieldNames[i]);
				}
				cnstrnt.append(")");
			}
		}
		return cnstrnt.toString();
	}

}