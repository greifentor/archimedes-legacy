/*
 * SQLScriptEvent.java
 *
 * 22.03.2009
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.script.sql;

import java.util.List;
import java.util.Vector;

import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.model.ColumnModel;
import archimedes.model.TableModel;

/**
 * Diese Klasse definiert ein Ereignis, das die an ein DiagrammModel
 * angebundenen SQLScriptListener &uuml;ber eine &Auml;nderung am Datenschema
 * informiert.
 * 
 * @author ollie
 *         <P>
 * 
 * @changed OLI 22.03.2009 - Hinzugef&uuml;gt
 *          <P>
 *          OLI 25.03.2009 - Erweiterung der Konstruktoraufrufe um das aktuelle
 *          Script (als Vector). In diesem Rahmen ist auch die Methode
 *          <TT>getScriptVectorReference()</TT> hinzugekommen.
 *          <P>
 * 
 */

public class SQLScriptEvent {

	private List<ColumnModel> ltsm = null;
	private SQLScriptEventType type = null;
	private String columnname = null;
	private String dbversion = null;
	private String indexname = null;
	private String stmt = null;
	private String tablename = null;
	private TableModel dtm = null;
	private ColumnModel dcm = null;
	private SQLScript v = null;

	/**
	 * Erzeugt ein SQLScriptEvent anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param type
	 *            Ein SQLScriptEventType, der die genaue Art der Ursache des
	 *            Events spezifiziert.
	 * @param stmt
	 *            Das Statement, das die &Auml;nderung am Datenschema umsetzen
	 *            soll.
	 * @param v
	 *            Das aktuelle Script in Zeilen.
	 */
	public SQLScriptEvent(SQLScriptEventType type, String stmt, SQLScript v) {
		this(type, stmt, v, null, null, null, null, null);
	}

	/**
	 * Erzeugt ein SQLScriptEvent anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param type
	 *            Ein SQLScriptEventType, der die genaue Art der Ursache des
	 *            Events spezifiziert.
	 * @param stmt
	 *            Das Statement, das die &Auml;nderung am Datenschema umsetzen
	 *            soll.
	 * @param v
	 *            Das aktuelle Script in Zeilen.
	 * @param dtm
	 *            Das Tabellenmodell, auf das sich die &Auml;nderung am
	 *            Datenschema bezieht.
	 */
	public SQLScriptEvent(SQLScriptEventType type, String stmt, SQLScript v, TableModel dtm) {
		this(type, stmt, v, dtm, null, null, null, null);
	}

	/**
	 * Erzeugt ein SQLScriptEvent anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param type
	 *            Ein SQLScriptEventType, der die genaue Art der Ursache des
	 *            Events spezifiziert.
	 * @param stmt
	 *            Das Statement, das die &Auml;nderung am Datenschema umsetzen
	 *            soll.
	 * @param v
	 *            Das aktuelle Script in Zeilen.
	 * @param dtm
	 *            Das Tabellenmodell, auf das sich die &Auml;nderung am
	 *            Datenschema bezieht.
	 * @param columnname
	 *            Ein Tabellenspaltenname, auf den sich das Event bezieht.
	 *            Dieser Name stammt aus dem in der Datenbank befindlichen
	 *            Datenschema und kann unter Umst&auml;nden im Datenmodell
	 *            fehlen.
	 */
	public SQLScriptEvent(SQLScriptEventType type, String stmt, SQLScript v, TableModel dtm, String columnname) {
		this(type, stmt, v, dtm, null, null, columnname, null);
	}

	/**
	 * Erzeugt ein SQLScriptEvent anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param type
	 *            Ein SQLScriptEventType, der die genaue Art der Ursache des
	 *            Events spezifiziert.
	 * @param stmt
	 *            Das Statement, das die &Auml;nderung am Datenschema umsetzen
	 *            soll.
	 * @param v
	 *            Das aktuelle Script in Zeilen.
	 * @param dtm
	 *            Das Tabellenmodell, auf das sich die &Auml;nderung am
	 *            Datenschema bezieht.
	 * @param ltsm
	 *            Ein Liste mit Tabellenspaltenmodellen, die durch die
	 *            &Auml;nderung betroffen sind (z. B. beim ein Einrichten eines
	 *            mehrstelligen Prim&auml;rschl&uuml;ssels.
	 */
	public SQLScriptEvent(SQLScriptEventType type, String stmt, SQLScript v, TableModel dtm, List<ColumnModel> ltsm) {
		this(type, stmt, v, dtm, null, null, null, ltsm);
	}

	/**
	 * Erzeugt ein SQLScriptEvent anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param type
	 *            Ein SQLScriptEventType, der die genaue Art der Ursache des
	 *            Events spezifiziert.
	 * @param stmt
	 *            Das Statement, das die &Auml;nderung am Datenschema umsetzen
	 *            soll.
	 * @param v
	 *            Das aktuelle Script in Zeilen.
	 * @param dtm
	 *            Das Tabellenmodell, auf das sich die &Auml;nderung am
	 *            Datenschema bezieht.
	 * @param ltsm
	 *            Ein Liste mit Tabellenspaltenmodellen, die durch die
	 *            &Auml;nderung betroffen sind (z. B. beim ein Einrichten eines
	 *            mehrstelligen Prim&auml;rschl&uuml;ssels.
	 */
	public SQLScriptEvent(SQLScriptEventType type, String stmt, SQLScript v, TabellenModel dtm,
			List<TabellenspaltenModel> ltsm) {
		this(type, stmt, v, dtm);
		this.ltsm = new Vector<ColumnModel>();
		for (TabellenspaltenModel tsm : ltsm) {
			this.ltsm.add(tsm);
		}
	}

	/**
	 * Erzeugt ein SQLScriptEvent anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param type
	 *            Ein SQLScriptEventType, der die genaue Art der Ursache des
	 *            Events spezifiziert.
	 * @param stmt
	 *            Das Statement, das die &Auml;nderung am Datenschema umsetzen
	 *            soll.
	 * @param v
	 *            Das aktuelle Script in Zeilen.
	 * @param dtm
	 *            Das Tabellenmodell, auf das sich die &Auml;nderung am
	 *            Datenschema bezieht.
	 * @param dcm
	 *            Das Tabellenspaltenmodell, auf das sich die &Auml;nderung am
	 *            Datenschema bezieht.
	 */
	public SQLScriptEvent(SQLScriptEventType type, String stmt, SQLScript v, TableModel dtm, ColumnModel dcm) {
		this(type, stmt, v, dtm, dcm, null, null, null);
	}

	/**
	 * Erzeugt ein SQLScriptEvent anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param type
	 *            Ein SQLScriptEventType, der die genaue Art der Ursache des
	 *            Events spezifiziert.
	 * @param stmt
	 *            Das Statement, das die &Auml;nderung am Datenschema umsetzen
	 *            soll.
	 * @param v
	 *            Das aktuelle Script in Zeilen.
	 * @param tablename
	 *            Ein Tabellenname, auf den sich das Event bezieht. Dieser Name
	 *            stammt aus dem in der Datenbank befindlichen Datenschema und
	 *            kann unter Umst&auml;nden im Datenmodell fehlen.
	 */
	public SQLScriptEvent(SQLScriptEventType type, String stmt, SQLScript v, String tablename) {
		this(type, stmt, v, null, null, tablename, null, null);
	}

	/**
	 * Erzeugt ein SQLScriptEvent anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param type
	 *            Ein SQLScriptEventType, der die genaue Art der Ursache des
	 *            Events spezifiziert.
	 * @param stmt
	 *            Das Statement, das die &Auml;nderung am Datenschema umsetzen
	 *            soll.
	 * @param v
	 *            Das aktuelle Script in Zeilen.
	 * @param tablename
	 *            Ein Tabellenname, auf den sich das Event bezieht. Dieser Name
	 *            stammt aus dem in der Datenbank befindlichen Datenschema und
	 *            kann unter Umst&auml;nden im Datenmodell fehlen.
	 * @param columnname
	 *            Ein Tabellenspaltenname, auf den sich das Event bezieht.
	 *            Dieser Name stammt aus dem in der Datenbank befindlichen
	 *            Datenschema und kann unter Umst&auml;nden im Datenmodell
	 *            fehlen.
	 */
	public SQLScriptEvent(SQLScriptEventType type, String stmt, SQLScript v, String tablename, String columnname) {
		this(type, stmt, v, null, null, tablename, columnname, null);
	}

	/**
	 * Erzeugt ein SQLScriptEvent anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param type
	 *            Ein SQLScriptEventType, der die genaue Art der Ursache des
	 *            Events spezifiziert.
	 * @param stmt
	 *            Das Statement, das die &Auml;nderung am Datenschema umsetzen
	 *            soll.
	 * @param v
	 *            Das aktuelle Script in Zeilen.
	 * @param dtm
	 *            Das Tabellenmodell, auf das sich die &Auml;nderung am
	 *            Datenschema bezieht.
	 * @param dcm
	 *            Das Tabellenspaltenmodell, auf das sich die &Auml;nderung am
	 *            Datenschema bezieht.
	 * @param tablename
	 *            Ein Tabellenname, auf den sich das Event bezieht. Dieser Name
	 *            stammt aus dem in der Datenbank befindlichen Datenschema und
	 *            kann unter Umst&auml;nden im Datenmodell fehlen.
	 * @param columnname
	 *            Ein Tabellenspaltenname, auf den sich das Event bezieht.
	 *            Dieser Name stammt aus dem in der Datenbank befindlichen
	 *            Datenschema und kann unter Umst&auml;nden im Datenmodell
	 *            fehlen.
	 * @param ltsm
	 *            Ein Liste mit Tabellenspaltenmodellen, die durch die
	 *            &Auml;nderung betroffen sind (z. B. beim ein Einrichten eines
	 *            mehrstelligen Prim&auml;rschl&uuml;ssels.
	 */
	public SQLScriptEvent(SQLScriptEventType type, String stmt, SQLScript v, TableModel dtm, ColumnModel dcm,
			String tablename, String columnname, List<ColumnModel> ltsm) {
		this(type, stmt, v, dtm, dcm, tablename, columnname, ltsm, null, null);
	}

	/**
	 * Erzeugt ein SQLScriptEvent anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param type
	 *            Ein SQLScriptEventType, der die genaue Art der Ursache des
	 *            Events spezifiziert.
	 * @param stmt
	 *            Das Statement, das die &Auml;nderung am Datenschema umsetzen
	 *            soll.
	 * @param v
	 *            Das aktuelle Script in Zeilen.
	 * @param dtm
	 *            Das Tabellenmodell, auf das sich die &Auml;nderung am
	 *            Datenschema bezieht.
	 * @param dcm
	 *            Das Tabellenspaltenmodell, auf das sich die &Auml;nderung am
	 *            Datenschema bezieht.
	 * @param tablename
	 *            Ein Tabellenname, auf den sich das Event bezieht. Dieser Name
	 *            stammt aus dem in der Datenbank befindlichen Datenschema und
	 *            kann unter Umst&auml;nden im Datenmodell fehlen.
	 * @param columnname
	 *            Ein Tabellenspaltenname, auf den sich das Event bezieht.
	 *            Dieser Name stammt aus dem in der Datenbank befindlichen
	 *            Datenschema und kann unter Umst&auml;nden im Datenmodell
	 *            fehlen.
	 * @param ltsm
	 *            Ein Liste mit Tabellenspaltenmodellen, die durch die
	 *            &Auml;nderung betroffen sind (z. B. beim ein Einrichten eines
	 *            mehrstelligen Prim&auml;rschl&uuml;ssels.
	 * @param indexname
	 *            Der Name des Index der durch das Event manipuliert wird.
	 * @param dbversion
	 *            Die Version des Datenmodells.
	 */
	public SQLScriptEvent(SQLScriptEventType type, String stmt, SQLScript v, TableModel dtm, ColumnModel dcm,
			String tablename, String columnname, List<ColumnModel> ltsm, String indexname, String dbversion) {
		super();
		this.columnname = columnname;
		this.dcm = dcm;
		this.dtm = dtm;
		this.dbversion = dbversion;
		this.indexname = indexname;
		this.ltsm = ltsm;
		this.stmt = stmt;
		this.tablename = tablename;
		this.type = type;
		this.v = v;
	}

	/**
	 * Liefert den Namen einer Tabellenspalte aus dem Schema dar Datenbank.
	 * Diese Tabellenspalte existiert unter Umst&auml;nden nicht mehr im
	 * Datenmodell.
	 * 
	 * @return Der Name einer Tabellenspalte aus dem Schema dar Datenbank. Diese
	 *         Tabellenspalte existiert unter Umst&auml;nden nicht mehr im
	 *         Datenmodell. Handelt es sich bei der Tabellenspalte, die an dem
	 *         Event beteiligt ist, um eine aus dem Datenmodell, so liefert die
	 *         Methode den Wert <TT>null</TT>.
	 */
	public String getColumnName() {
		return this.columnname;
	}

	/**
	 * Liefert eine Referenz auf das Tabellenspaltenmodell, auf das sich die
	 * &Auml;nderung bezieht.
	 * 
	 * @return Eine Referenz auf das Tabellenspaltenmodell, auf das sich die
	 *         &Auml;nderung bezieht, bzw. <TT>null</TT>, wenn die &Auml;nderung
	 *         am Datenmodell nicht direkt mit einer Tabellenspalte in
	 *         Verbindung steht.
	 */
	public ColumnModel getDataColumnModel() {
		return this.dcm;
	}

	/**
	 * Liefert eine Referenz auf das Tabellenmodell, auf das sich die
	 * &Auml;nderung bezieht.
	 * 
	 * @return Eine Referenz auf das Tabellenmodell, auf das sich die
	 *         &Auml;nderung bezieht, bzw. <TT>null</TT>, wenn die &Auml;nderung
	 *         am Datenmodell nicht direkt mit einer Tabelle in Verbindung
	 *         steht.
	 */
	public TableModel getDataTableModel() {
		return this.dtm;
	}

	/**
	 * Liefert bei bestimmten Aktionen die Version des Datenmodells.
	 * 
	 * @return Bei bestimmten Aktionen die Version des Datenmodells, sonst
	 *         <TT>null</TT>.
	 */
	public String getDBVersion() {
		return this.dbversion;
	}

	/**
	 * Liefert das Typ, der die Ursache des Events spezifiziert.
	 * 
	 * @return Der Typ, der die Ursache des Events spezifiziert.
	 */
	public SQLScriptEventType getEventType() {
		return this.type;
	}

	/**
	 * Liefert den Namen eines Index, falls ein solcher an dem Event beteiligt
	 * ist.
	 * 
	 * @return Der Name eines Index, falls ein solcher an dem Event beteiligt
	 *         ist, sonst <TT>null</TT>.
	 */
	public String getIndexName() {
		return this.indexname;
	}

	/**
	 * Liefert eine Liste von Tabellenspaltenmodellen, falls eine solche an dem
	 * Event beteiligt ist.
	 * 
	 * @return Eine Liste von Tabellenspaltenmodellen, falls eine solche an dem
	 *         Event beteiligt ist, sonst <TT>null</TT>.
	 */
	public List<ColumnModel> getListOfDataColumnModel() {
		return this.ltsm;
	}

	/**
	 * Liefert eine Referenz auf den aktuellen Zustand des Gesamtscripts.
	 * <P>
	 * <I><B>Achtung!</B> &Auml;nderungen am Script schlagen sich auf den
	 * gesamten Proze&szlig; der Update-Script-Erstellung durch.</I>
	 * 
	 * @return Eine Referenz auf den aktuellen Zustand des Gesamtscripts.
	 */
	public SQLScript getScriptVectorReference() {
		return this.v;
	}

	/**
	 * Liefert das SQL-Statement, das mit der &Auml;nderung am Datenschema
	 * einhergeht.
	 * 
	 * @return Das SQL-Statement, das die &Auml;nderung am Datenschema
	 *         durchf&uuml;hren soll.
	 */
	public String getSQLStatement() {
		return this.stmt;
	}

	/**
	 * Liefert den Namen einer Tabelle aus dem Schema dar Datenbank. Diese
	 * Tabelle existiert unter Umst&auml;nden nicht mehr im Datenmodell.
	 * 
	 * @return Der Name einer Tabelle aus dem Schema dar Datenbank. Diese
	 *         Tabelle existiert unter Umst&auml;nden nicht mehr im Datenmodell.
	 *         Handelt es sich bei der Tabelle, die an dem Event beteiligt ist,
	 *         um eine aus dem Datenmodell, so liefert die Methode den Wert
	 *         <TT>null</TT>.
	 */
	public String getTableName() {
		return this.tablename;
	}

	@Override
	public String toString() {
		return this.getEventType().toString().concat("-").concat(
				(this.getSQLStatement() != null ? this.getSQLStatement() : "<null>"));
	}

}