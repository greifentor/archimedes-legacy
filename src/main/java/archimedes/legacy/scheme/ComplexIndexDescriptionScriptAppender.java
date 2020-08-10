/*
 * ComplexIndexDescriptionScriptAppender.java
 *
 * 20.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static corentx.util.Checks.ensure;
import gengen.metadata.AttributeMetaData;

import java.util.List;

import archimedes.legacy.model.ComplexIndexScriptAppender;
import archimedes.legacy.model.IndexMetaData;
import archimedes.legacy.model.SimpleIndexMetaData;

/**
 * Diese Implementierung des <CODE>ComplexIndexScriptAppender</CODE>-Interfaces
 * erweitert eine angegebene Beschreibung &uuml;ber die Unterschiede der Indices
 * zwischen Datenschema und -modell.
 * 
 * @author ollie
 * 
 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
 */

public class ComplexIndexDescriptionScriptAppender implements ComplexIndexScriptAppender {

	private List<String> html = null;

	enum Action {
		ADDED, REMOVED
	};

	/**
	 * Erzeugt einen neuen Appender anhand der angegebenen Parameter.
	 * 
	 * @param html
	 *            Die zu erweiternde Beschreibung (HTML im Stringlistenformat).
	 * @throws IllegalArgumentException
	 *             Falls eine der Vorbedingungen verletzt wird.
	 * @precondition html != <CODE>null</CODE>
	 * 
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	public ComplexIndexDescriptionScriptAppender(List<String> html) throws IllegalArgumentException {
		super();
		ensure(html != null, "html description cannot be null.");
		this.html = html;
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void indexAdded(IndexMetaData index) {
		AttributeMetaData[] attributes = index.getColumns();
		String[] columnNames = new String[index.getColumns().length];
		for (int i = 0; i < attributes.length; i++) {
			columnNames[i] = attributes[i].getName();
		}
		this.appendDescriptionHeader(index.getName(), Action.ADDED);
		this.appendDescriptionBody(index.getName(), Action.ADDED, index.getTable().getName(), columnNames);
	}

	private void appendDescriptionHeader(String indexName, Action action) {
		StringBuffer sb = new StringBuffer();
		sb.append("<H2>Index ").append(indexName).append(" ");
		if (action == Action.ADDED) {
			sb.append("angelegt");
		} else if (action == Action.REMOVED) {
			sb.append("gel&ouml;scht");
		}
		sb.append("</H2>");
		this.html.add(sb.toString());
	}

	private void appendDescriptionBody(String indexName, Action action, String tableName, String... columnNames) {
		StringBuffer sb = new StringBuffer();
		sb.append("<P>Komplexer Index ").append(indexName).append(" f&uuml;r Tabelle ").append(tableName).append(" (");
		for (int i = 0; i < columnNames.length; i++) {
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(columnNames[i]);
		}
		sb.append(") ");
		if (action == Action.ADDED) {
			sb.append("angelegt");
		} else if (action == Action.REMOVED) {
			sb.append("aus dem Modell entfernt");
		}
		sb.append(".");
		this.html.add(sb.toString());
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void indexRemoved(SimpleIndexMetaData index) {
		this.appendDescriptionHeader(index.getName(), Action.REMOVED);
		this.appendDescriptionBody(index.getName(), Action.REMOVED, index.getTable(), index.getColumns());
	}

}