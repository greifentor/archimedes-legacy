/*
 * AlterStatementGenerator.java
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
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.legacy.model.TableModel;
import corent.db.DBExecMode;
import corentx.util.Str;

/**
 * Diese Utilityklasse bietet Methoden zur Erzeugung von Alter-Statements unter
 * Ber&uuml;cksichtigung der Besonderheiten der DBMS an.
 * 
 * @author ollie
 * 
 * @changed OLI 06.10.2009 - Hinzugef&uuml;gt
 * @changed OLI 09.03.2010 - Erweiterung der Datentypen&auml;nderung zum den
 *          HSQL-Modus.
 * @changed OLI 18.06.2013 - Gro&szlig;schreibung bei den Statements
 *          eingef&uuml;hrt.
 */

public class AlterStatementGenerator {

	/**
	 * Liefert ein Alter-Statement zum &Auml;ndern des Datentyps der
	 * Tabellenspalte.
	 * 
	 * @param tsm
	 *            Die Tabellenspalte, zu der das Alter-Statement erzeugt werden
	 *            soll.
	 * @param dbmode
	 *            Der DBMS-Modus, zu dem das Statement erzeugt werden soll.
	 * @param quotes
	 *            Klammerzeichen, mit denen Feld- und Tabellennamen eingklammert
	 *            werden k&ouml;nnen.
	 * @return Das Alter-Statement zum &Auml;ndern des Datentyps der
	 *         Tabellenspalte.
	 * @throws IllegalArgumentException
	 *             Falls die Methode im HSQL- oder MSSQL-Modus aufgerufen wird.
	 * @throws NullPointerException
	 *             Falls entweder die Tabellenspalte oder der DBExec-Modus als
	 *             <TT>null</TT>-Referenz &uuml;bergeben werden.
	 * @precondition tsm != <TT>null</TT>
	 * @precondition dbmode != <TT>null</TT>
	 * @precondition dbmode != <TT>HSQL</TT> &amp;&amp; dbmode != <TT>MSSQL</TT>
	 * 
	 * @changed OLI 09.03.2010 - Erweiterung um die Verarbeitung des HSQL-Modus.
	 */
	public static String getAlterDataType(TabellenspaltenModel tsm, DBExecMode dbmode, String quotes)
			throws NullPointerException {
		TabellenModel tm = null;
		ensure(dbmode != null, new NullPointerException("data base mode can not be null."));
		ensure(dbmode != DBExecMode.MSSQL, "mssql is not supported.");
		tm = tsm.getTabelle();
		if (dbmode == DBExecMode.MYSQL) {
			return "ALTER TABLE ".concat(Str.quote(tm.getName(), quotes)).concat(" MODIFY COLUMN ").concat(
					Str.quote(tsm.getName(), quotes)).concat(" ").concat(getTypeName(tsm, dbmode)).concat(";");
		} else if (dbmode == DBExecMode.HSQL) {
			return "ALTER TABLE ".concat(Str.quote(tm.getName(), quotes)).concat(" ALTER COLUMN ").concat(
					Str.quote(tsm.getName(), quotes)).concat(" SET DATA TYPE ").concat(getTypeName(tsm, dbmode))
					.concat(";");
		} else if (dbmode == DBExecMode.POSTGRESQL) {
			return "ALTER TABLE ".concat(Str.quote(tm.getName(), quotes)).concat(" ALTER COLUMN ").concat(
					Str.quote(tsm.getName(), quotes)).concat(" SET DATA TYPE ").concat(getTypeName(tsm, dbmode))
					.concat(";");
		}
		throw new IllegalArgumentException(dbmode + " is not supported.");
	}

	private static String getTypeName(TabellenspaltenModel tsm, DBExecMode dbmode) {
		return SQLGeneratorUtil.getTypeString(false, tsm.getDomain(), dbmode);
	}

	/**
	 * Liefert ein Alter-Statement zum &Auml;ndern, Anlegen oder L&ouml;schen
	 * eines Defaultwertes f&uuml;r die angegebene Tabellenspalte.
	 * 
	 * @param tsm
	 *            Die Tabellenspalte, zu der das Alter-Statement erzeugt werden
	 *            soll.
	 * @param dbmode
	 *            Der DBMS-Modus, zu dem das Statement erzeugt werden soll.
	 * @param quotes
	 *            Klammerzeichen, mit denen Feld- und Tabellennamen eingklammert
	 *            werden k&ouml;nnen.
	 * @return Das Alter-Statement zum &Auml;ndern, Anlegen oder L&ouml;schen
	 *         eines Defaultwertes zur angebenen Tabellenspalte.
	 * @throws NullPointerException
	 *             Falls entweder die Tabellenspalte oder der DBExec-Modus als
	 *             <TT>null</TT>-Referenz &uuml;bergeben werden.
	 * @precondition tsm != <TT>null</TT>.
	 * @precondition dbmode != <TT>null</TT>.
	 */
	public static String getAlterDefault(TabellenspaltenModel tsm, DBExecMode dbmode, String quotes)
			throws NullPointerException {
		assert tsm != null : "there is no way to generate an alter statement for a null " + "column.";
		assert dbmode != null : "dbms named null is not known.";
		StringBuffer sb = new StringBuffer("ALTER TABLE ").append(Str.quote(tsm.getTabelle().getName(), quotes))
				.append(" ALTER COLUMN ").append(Str.quote(tsm.getName(), quotes));
		if (tsm.getDomain().getInitialValue().equals("NULL")) {
			if (dbmode == DBExecMode.HSQL) {
				sb.append(" SET DEFAULT NULL");
			} else {
				sb.append(" DROP DEFAULT");
			}
		} else {
			sb.append(" SET DEFAULT ").append(tsm.getDomain().getInitialValue());
		}
		sb.append(";");
		return sb.toString();
	}

	/**
	 * Liefert ein Alter-Statement zum Setzen bzw. L&ouml;schen einer
	 * <TT>not null</TT> Klausel f&uuml;r die angegebene Tabellenspalte.
	 * 
	 * @param tsm
	 *            Die Tabellenspalte, zu der das Alter-Statement erzeugt werden
	 *            soll.
	 * @param dbmode
	 *            Der DBMS-Modus, zu dem das Statement erzeugt werden soll.
	 * @param hasDomains
	 *            Diese Flagge ist zu setzen, wenn Domainnamen im
	 *            Alter-Statement genutzt werden sollen.
	 * @param quotes
	 *            Klammerzeichen, mit denen Feld- und Tabellennamen eingklammert
	 *            werden k&ouml;nnen.
	 * @return Das Alter-Statement zum Setzen bzw. L&ouml;schen des
	 *         <TT>not null</TT>-Constraints zur angebenen Tabellenspalte.
	 * @throws NullPointerException
	 *             Falls entweder die Tabellenspalte oder der DBExec-Modus als
	 *             <TT>null</TT>-Referenz &uuml;bergeben werden.
	 * @precondition tsm != <TT>null</TT>.
	 * @precondition dbmode != <TT>null</TT>.
	 */
	public static String getAlterNull(TabellenspaltenModel tsm, DBExecMode dbmode, boolean hasDomains, String quotes)
			throws NullPointerException {
		assert tsm != null : "there is no way to generate an alter statement for a null " + "column.";
		assert dbmode != null : "dbms named null is not known.";
		StringBuffer sb = new StringBuffer("ALTER TABLE ").append(Str.quote(tsm.getTabelle().getName(), quotes));
		if (dbmode == DBExecMode.MYSQL) {
			sb.append(" MODIFY ");
		} else {
			sb.append(" ALTER COLUMN ");
		}
		sb.append(Str.quote(tsm.getName(), quotes));
		if (dbmode == DBExecMode.MYSQL) {
			sb.append(" ").append((hasDomains ? tsm.getDomain().getName() : getTypeName(tsm, dbmode)));
		} else if ((dbmode == DBExecMode.POSTGRESQL) && !tsm.isNotNull()) {
			sb.append(" DROP");
		} else if (dbmode != DBExecMode.MSSQL) {
			sb.append(" SET");
		}
		if (tsm.isNotNull() || (dbmode == DBExecMode.POSTGRESQL)) {
			sb.append(" NOT");
		}
		sb.append(" NULL;");
		return sb.toString();
	}

	/**
	 * Creates aa alter statement for the passed parameters.
	 * 
	 * @changed OLI 26.07.2013 - Added.
	 */
	public static String getAlterStatement(ColumnModel column, DBExecMode dbmode, boolean hasDomains, boolean notnull,
			String quoteCharacter) {
		TableModel tm = column.getTable();
		String s = "ALTER TABLE " + Str.quote(tm.getName(), quoteCharacter);
		s += " ADD " + (dbmode != DBExecMode.MSSQL ? "COLUMN " : "") + Str.quote(column.getName(), quoteCharacter)
				+ " ";
		s += SQLGeneratorUtil.getTypeString(hasDomains, column.getDomain(), dbmode);
		s += (notnull && column.isNotNull() ? " NOT NULL" : "");
		if (column.getDefaultValue() != null) {
			s += " DEFAULT ";
			boolean isString = column.getDomain().getType(dbmode).toLowerCase().contains("char")
					|| column.getDomain().getType(dbmode).toLowerCase().contains("text");
			s += (isString ? "'" : "") + SQLGeneratorUtil.getDefaultValue(column, dbmode) + (isString ? "'" : "");
		}
		s += ";";
		return s;
	}

}