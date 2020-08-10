/*
 * ComplexIndexSQLScriptAppender.java
 *
 * 20.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static corentx.util.Checks.ensure;
import gengen.metadata.AttributeMetaData;
import archimedes.legacy.model.ComplexIndexScriptAppender;
import archimedes.legacy.model.IndexMetaData;
import archimedes.legacy.model.SimpleIndexMetaData;
import archimedes.legacy.script.sql.SQLScript;

/**
 * Diese Implementierung des <CODE>ComplexIndexScriptAppender</CODE>-Interfaces
 * erweitert ein angegebenes SQL-Script um entsprechende Anweisungen, um die
 * Unterschiede zwischen Datenschema und -modell auszugleichen.
 * 
 * @author ollie
 * 
 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
 */

public class ComplexIndexSQLScriptAppender implements ComplexIndexScriptAppender {

	private SQLScript script = null;

	/**
	 * Erzeugt einen neuen Appender anhand der angegebenen Parameter.
	 * 
	 * @param script
	 *            Das zu erweiternde SQL-Script (im Stringlistenformat).
	 * @throws IllegalArgumentException
	 *             Falls eine der Vorbedingungen verletzt wird.
	 * @precondition script != <CODE>null</CODE>
	 * 
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	public ComplexIndexSQLScriptAppender(SQLScript script) throws IllegalArgumentException {
		super();
		ensure(script != null, "script cannot be null.");
		this.script = script;
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void indexAdded(IndexMetaData index) {
		AttributeMetaData[] attributes = index.getColumns();
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE INDEX ").append(index.getName());
		sb.append(" ON ").append(index.getTable().getName()).append(" (");
		for (int i = 0; i < attributes.length; i++) {
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(attributes[i].getName());
		}
		sb.append(");");
		this.script.addReducingStatement(sb.toString());
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void indexRemoved(SimpleIndexMetaData index) {
		this.script.addExtendingStatement("DROP INDEX " + index.getName() + ";");
	}

}