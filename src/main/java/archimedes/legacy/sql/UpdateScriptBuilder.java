/*
 * UpdateScriptBuilder.java
 *
 * 24.10.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import archimedes.legacy.metadata.SequenceMetaData;
import archimedes.legacy.model.ColumnMetaData;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TableMetaData;
import archimedes.legacy.scheme.ComplexIndexDescriptionScriptAppender;
import archimedes.legacy.scheme.ComplexIndexSQLScriptAppender;
import archimedes.legacy.scheme.DefaultComplexIndexUpdateGenerator;
import archimedes.legacy.scheme.Diagramm;
import archimedes.legacy.scheme.SQLScriptHeaderBuilder;
import archimedes.legacy.scheme.SchemaChangeStatementAppender;
import archimedes.legacy.scheme.SetSchemeVersionAppender;
import archimedes.legacy.script.sql.SQLScript;
import archimedes.legacy.script.sql.SQLScriptEvent;
import archimedes.legacy.script.sql.SQLScriptEventType;
import archimedes.legacy.script.sql.SQLScriptListener;
import archimedes.legacy.sql.factories.GenericSQLScriptFactory;
import archimedes.legacy.sql.factories.PostgreSQLScriptFactory;
import archimedes.legacy.util.DescriptionGetter;
import archimedes.legacy.util.WildCardChanger;
import archimedes.model.ColumnModel;
import archimedes.model.DomainModel;
import archimedes.model.SimpleIndexMetaData;
import archimedes.model.StereotypeModel;
import archimedes.model.TableModel;
import archimedes.model.UniqueMetaData;
import archimedes.model.UserInformation;
import corent.base.SortedVector;
import corent.base.StrUtil;
import corent.dates.PDate;
import corent.db.DBExecMode;
import corent.db.DBType;
import corentx.util.Str;

/**
 * This class creates an update script for the changes between an Archimedes
 * data model an the meta data of a data scheme.
 * 
 * @author ollie
 * 
 * @changed OLI 24.10.2013 - Added (by taking the code from the Diagramm class).
 */

public class UpdateScriptBuilder {

	private Diagramm dataModel = null;

	/**
	 * Creates a new update script builder for the passed data model.
	 * 
	 * @param dataModel
	 *            The data model to match the meta data against.
	 * 
	 * @changed OLI 24.10.2013 - Added.
	 */
	public UpdateScriptBuilder(Diagramm dataModel) {
		super();
		this.dataModel = dataModel;
	}

	/**
	 * Creates an update script between the passed data model and the passed
	 * meta data.
	 * 
	 * @param md
	 *            The meta data of the data scheme which the data model is to
	 *            match against.
	 */
	public Vector buildUpdateScript(java.util.List md, boolean hasDomains, boolean fkNotNullBeachten,
			boolean referenzenSetzen, DBExecMode dbmode, Vector html, java.util.List<SimpleIndexMetaData> indices,
			String quoteCharacter, SequenceMetaData[] sequences, List<UniqueMetaData> uniques,
			UserInformation userInfos, boolean suppressAdditionalSQL) {
		DescriptionGetter descriptionGetter = new DescriptionGetter(this.dataModel.getDefaultComments());
		WildCardChanger wildCardChanged = new WildCardChanger();
		boolean altered = false;
		boolean debug = Boolean.getBoolean("archimedes.scheme.Diagramm.debug")
				|| Boolean.getBoolean("archimedes.Archimedes.debug");
		boolean deleted = false;
		boolean htmlprinted = false;
		boolean notnull = false;
		boolean pkchangedetected = false;
		ColumnMetaData cmd = null;
		DomainModel dm = null;
		Hashtable cols = null;
		int pkcount = 0;
		java.util.List<String> lln = StrUtil.SplitToList(this.dataModel.getAdditionalSQLScriptListener(), ",");
		SortedVector<TableModel> tables = new SortedVector<TableModel>();
		SortedVector<ColumnModel> svtsm = null;
		SQLScriptListener sqlsl = null;
		String colname = null;
		String indexname = null;
		String n = null;
		String s = "";
		String stmt = null;
		StringBuffer sb = null;
		TableMetaData tmd = null;
		List<TableModel> t = Arrays.asList(this.dataModel.getTables());
		SQLScript v = new SQLScript();
		Vector vd = null;
		Vector<String> vdn = null;
		Vector<SQLScriptListener> vsqlsl = new Vector();
		SQLScriptFactory sqlScriptFactory = null;
		if (dbmode == DBExecMode.POSTGRESQL) {
			sqlScriptFactory = new PostgreSQLScriptFactory(quoteCharacter, this.dataModel);
		} else {
			sqlScriptFactory = new GenericSQLScriptFactory(quoteCharacter, this.dataModel, dbmode);
		}
		if (!fkNotNullBeachten) {
			notnull = false;
			for (TableModel table : tables) {
				for (ColumnModel column : table.getColumns()) {
					if (column.isNotNull()) {
						v.addExtendingStatement("/* WARNING: " + column.getFullName() + " has "
								+ "a set not null flag but not null option for script " + "generation is off. */");
						notnull = true;
					}
				}
			}
			if (notnull) {
				v.addExtendingStatement("");
				v.addExtendingStatement("");
			}
		}
		new SchemaChangeStatementAppender(this.dataModel.getSchemaName(), sqlScriptFactory).append(v);
		new SQLScriptHeaderBuilder().append(this.dataModel, v, userInfos, dbmode);
		new SetSchemeVersionAppender(this.dataModel.getVersion(), this.dataModel.getSchemaName(), sqlScriptFactory)
				.append(v);
		for (int i = 0, leni = lln.size(); i < leni; i++) {
			n = lln.get(i).trim();
			try {
				sqlsl = (SQLScriptListener) Class.forName(n).newInstance();
				this.dataModel.addSQLScriptListener(sqlsl);
				vsqlsl.addElement(sqlsl);
			} catch (Exception e) {
				System.out.println("SQLScriptListener " + n + " could not be added.");
			}
		}
		this.dataModel.fireDataSchemeChanged(new SQLScriptEvent(SQLScriptEventType.STARTBUILDING, stmt, v, null, null,
				"", "", null, null, this.dataModel.getVersion()));
		html.addElement("<html>");
		html.addElement("    <head>");
		html.addElement("        <title>&Auml;nderungsreport f&uuml;r " + StrUtil.ToHTML(this.dataModel.getName())
				+ " (Version " + StrUtil.ToHTML(this.dataModel.getVersion()) + ")</title>");
		html.addElement("    </head>");
		html.addElement("    <body>");
		html.addElement("        <hr SIZE=3 WIDTH=\"100%\">");
		html.addElement("        <h1>" + StrUtil.ToHTML(this.dataModel.getName()) + " (Version "
				+ StrUtil.ToHTML(this.dataModel.getVersion()) + ")</h1>");
		html.addElement("        <hr SIZE=1 WIDTH=\"100%\">");
		html
				.addElement("        <p align=justify>"
						+ (this.dataModel.getComment().length() > 0 ? StrUtil.ToHTML(this.dataModel.getComment()) : "")
						+ "<p>");
		html.addElement("        <hr SIZE=1 WIDTH=\"100%\">");
		if (hasDomains) {
			vdn = new Vector<String>();
			for (int i = 0, len = md.size(); i < len; i++) {
				tmd = (TableMetaData) md.get(i);
				cols = tmd.getColumns();
				for (Iterator it = cols.keySet().iterator(); it.hasNext();) {
					n = (String) it.next();
					cmd = tmd.getColumn(n);
					if (!vdn.contains(cmd.typename)) {
						vdn.addElement(cmd.typename);
					}
				}
			}
			vd = this.dataModel.getDomains();
			for (int i = 0, len = vd.size(); i < len; i++) {
				dm = (DomainModel) vd.elementAt(i);
				if (!vdn.contains(dm.getName())) {
					String addDomainStmt = sqlScriptFactory.createDomainStatement(dm);
					if (addDomainStmt != null) {
						v.addExtendingStatement(addDomainStmt);
						if (dbmode == DBExecMode.MSSQL) {
							v.addExtendingStatement("go");
						}
						v.addExtendingStatement("");
					}
				}
			}
		}
		// Indexbereinigung (MYSQL, POSTGRESQL, HSQL).
		for (int i = indices.size() - 1; i >= 0; i--) {
			indexname = indices.get(i).getName();
			if (sqlScriptFactory.isPrimaryKeyIndex(indexname)) {
				indices.remove(i);
			}
		}
		if (indices.size() > 0) {
			System.out.println("database has indices: " + indices);
		}
		for (int i = 0, len = t.size(); i < len; i++) {
			tables.addElement((TabellenModel) t.get(i));
		}
		if (!fkNotNullBeachten) {
			notnull = false;
			for (TableModel table : tables) {
				for (ColumnModel column : table.getColumns()) {
					if (column.isNotNull()) {
						v.addExtendingStatement("/* WARNING: " + column.getFullName() + " has "
								+ "a set not null flag but not null option for script " + "generation is off. */");
						notnull = true;
					}
				}
			}
			if (notnull) {
				v.addExtendingStatement("");
				v.addExtendingStatement("");
			}
		}
		for (int j = 0, lenj = md.size(); j < lenj; j++) {
			tmd = (TableMetaData) md.get(j);
			;
			TableModel table = this.findTable(tables, tmd.name);
			if (table == null) {
				if (!htmlprinted) {
					htmlprinted = true;
					html.addElement("        <h2>L&ouml;schen</h2>");
					html.addElement("        <hr SIZE=1 WIDTH=\"100%\">");
				}
				v.addExtendingStatement("");
				v.addExtendingStatement("");
				v.addExtendingStatement("/******************** ATTENTION! ********************/");
				v.addExtendingStatement("/* TABLE " + tmd.name + " WILL BE REMOVED! */");
				v.addExtendingStatement(sqlScriptFactory.dropTableStatement(tmd));
				html.addElement("    <h3>" + StrUtil.ToHTML(tmd.name) + "</h3><p>");
				this.dataModel
						.fireDataSchemeChanged(new SQLScriptEvent(SQLScriptEventType.DROPTABLE, stmt, v, tmd.name));
			}
		}
		if (htmlprinted) {
			html.addElement("        <hr SIZE=2 WIDTH=\"100%\">");
		}
		html.addElement("        <h2>&Auml;ndern &amp; Hinzuf&uuml;gen</h2>");
		html.addElement("        <hr SIZE=1 WIDTH=\"100%\">");
		htmlprinted = new SequenceScriptBuilder(this.dataModel, sqlScriptFactory, sequences).build(v, html);
		if (referenzenSetzen) {
			new ForeignKeyRemoveConstraintBuilder(this.dataModel, sqlScriptFactory, md).build(v);
		}
		for (TableModel table : tables) {
			pkchangedetected = false;
			if (!table.isDeprecated() && !table.isDraft()) {
				tmd = this.findTableMetaData(md, table);
				if (tmd == null) {
					pkcount = 0;
					for (ColumnModel column : table.getColumns()) {
						if (column.isPrimaryKey()) {
							pkcount++;
						}
					}
					if (!htmlprinted) {
						htmlprinted = true;
					} else {
						html.addElement("        <p>");
						html.addElement("        <hr SIZE=1 WIDTH=\"100%\">");
					}
					html.addElement("        <h3>Tabelle: " + StrUtil.ToHTML(table.getName())
							+ " (hinzuf&uuml;gen)</h3>");
					s = "";
					for (StereotypeModel stm : table.getStereotypes()) {
						if (s.length() != 0) {
							s += ", ";
						} else {
							s = "        <p><i>Stereotypen: ";
						}
						s += StrUtil.ToHTML(stm.getName());
					}
					if (s.length() > 0) {
						html.addElement(s + "</i><br>");
					}
					html.addElement("        <hr SIZE=1 WIDTH=\"100%\">");
					html.addElement("        <p align=justify>"
							+ (table.getComment().length() > 0 ? StrUtil.ToHTML(table.getComment()) : "-/-") + "<p>");
					html.addElement("        <hr SIZE=1 WIDTH=\"100%\">");
					v.addExtendingStatement("");
					v.addExtendingStatement("");
					stmt = sqlScriptFactory.createTableStatement(table, hasDomains, !fkNotNullBeachten);
					for (ColumnModel column : table.getColumns()) {
						if (column.isUnique()) {
							String genName = column.getTable().getName() + "_" + column.getName() + "_key";
							uniques.add(new UniqueMetaData(genName, table.getName()));
						}
					}
					v.addExtendingStatement(stmt);
					this.dataModel.fireDataSchemeChanged(new SQLScriptEvent(SQLScriptEventType.CREATETABLE, stmt, v,
							table));
					for (ColumnModel column : table.getColumns()) {
						notnull = (column.isNotNull() && (column.isPrimaryKey() || ((column.getRelation() != null) && fkNotNullBeachten)));
						html.addElement("    <p><b>"
								+ StrUtil.ToHTML(column.getName())
								+ "</b> :"
								+ (hasDomains ? column.getDomain().getName() : column.getDomain().getType(dbmode))
								+ (column.isPrimaryKey() ? " <b>(PK)</b>" : "")
								+ (column.getRelation() != null ? " <i>" + "(FK -&gt; " + column.getReferencedColumn()
										+ ")</i>" : "") + (notnull ? " <b>NOT NULL</b>" : ""));
						html.addElement("    <p align=justify>" + descriptionGetter.getDescription(column)
								+ "<br>&nbsp;");
					}
					for (ColumnModel column : table.getColumns()) {
						notnull = (column.isNotNull() && (column.isPrimaryKey() || ((column.getRelation() != null) && fkNotNullBeachten)));
						html.addElement("    <p><b>"
								+ StrUtil.ToHTML(column.getName())
								+ "</b>"
								+ " :"
								+ (hasDomains ? column.getDomain().getName() : column.getDomain().getType(dbmode))
								+ (column.isPrimaryKey() ? " <b>(PK)</b>" : "")
								+ (column.getRelation() != null ? " <i>" + "(FK -&gt; " + column.getReferencedColumn()
										+ ")</i>" : "") + (notnull ? " <b>NOT NULL</b>" : ""));
						html.addElement("    <p align=justify>" + descriptionGetter.getDescription(column)
								+ "<br>&nbsp;");
					}
				} else {
					altered = false;
					for (ColumnModel column : table.getColumns()) {
						notnull = ((column.isNotNull() && fkNotNullBeachten) || (column.isPrimaryKey() || ((column
								.getRelation() != null)
								&& column.isNotNull() && fkNotNullBeachten)));
						cmd = (ColumnMetaData) tmd.getColumn(column.getName());
						if (cmd == null) {
							if (!altered) {
								v.addExtendingStatement("");
								v.addExtendingStatement("");
								if (!htmlprinted) {
									htmlprinted = true;
								} else {
									html.addElement("        <p>");
									html.addElement("        <hr SIZE=1 WIDTH=\"100%\">");
								}
								html.addElement("        <h3>Tabelle: " + StrUtil.ToHTML(column.getName()) + "</h3>");
								s = "";
								for (StereotypeModel stm : table.getStereotypes()) {
									if (s.length() != 0) {
										s += ", ";
									} else {
										s = "        <p><i>Stereotypen: ";
									}
									s += StrUtil.ToHTML(stm.getName());
								}
								if (s.length() > 0) {
									html.addElement(s + "</i><br>");
								}
								html.addElement("        <hr SIZE=1 WIDTH=\"100%\">");
								html
										.addElement("        <p align=justify>"
												+ (table.getComment().length() > 0 ? StrUtil.ToHTML(table.getComment())
														: "-/-") + "<p>");
								html.addElement("        <hr SIZE=1 WIDTH=\"100%\">");
								altered = true;
							}
							stmt = sqlScriptFactory
									.alterTableAddColumnStatement(column, hasDomains, false /* fkNotNullBeachten */);
							v.addExtendingStatement(stmt);
							if (column.isNotNull()) {
								v.addReducingStatement(sqlScriptFactory.alterTableModifyConstraintNotNull(column));
							}
							this.dataModel.fireDataSchemeChanged(new SQLScriptEvent(SQLScriptEventType.ADDCOLUMN, stmt,
									v, table, column));
							html.addElement("    <p><b>"
									+ StrUtil.ToHTML(column.getName())
									+ "</b> :"
									+ (hasDomains ? column.getDomain().getName() : column.getDomain().getType(dbmode))
									+ (column.isPrimaryKey() ? " <b>(PK)</b>" : "")
									+ (column.getRelation() != null ? " <i>(FK -&gt; " + column.getReferencedColumn()
											+ ")</i>" : "") + (notnull ? " <b>NOT NULL</b>" : "")
									+ (column.isDeprecated() ? " <b>AUFGEHOBEN!</b>" : ""));
							html.addElement("    <p align=justify>" + descriptionGetter.getDescription(column)
									+ "<br>&nbsp;");
						} else {
							dm = column.getDomain();
							if (dbmode == DBExecMode.POSTGRESQL) {
								if (dm.getDataType() == -1) {
									cmd.colsize = 0;
									cmd.datatype = DBType.LONGVARCHAR;
								} else if (dm.getDataType() == -5) {
									cmd.colsize = 0;
									// cmd.primaryKey = tsm.isPrimaryKey();
									// cmd.setNotNull(notnull);
								} else if (dm.getDataType() == -2) {
									cmd.colsize = 0;
									cmd.datatype = DBType.BLOB;
								}
							} else if ((dbmode == DBExecMode.HSQL) && (dm.getDataType() == 3)) {
								cmd.datatype = DBType.LONGVARCHAR; // 3
							}
							if (column.isPrimaryKey() != cmd.primaryKey) {
								System.out.println("\nPrimaerschluesselaenderung fuer " + "Tabellenspalte "
										+ column.getFullName() + " (" + dm.getName() + ")");
								System.out.println("PK:       " + column.isPrimaryKey() + " - " + cmd.primaryKey);
								// TODO Gegen Factory-Methodenaufruf
								// austauschen.
								if (Boolean.getBoolean("archimedes.template." + dbmode.toString().toLowerCase()
										+ ".statemant.pk.drop.eachcolumn")) {
									stmt = System.getProperty("archimedes.template." + dbmode.toString().toLowerCase()
											+ ".statement.pk.drop", "");
									stmt = wildCardChanged.change(stmt, table.getName(), column.getName(), null);
									v.addExtendingStatement(stmt);
									this.dataModel.fireDataSchemeChanged(new SQLScriptEvent(
											SQLScriptEventType.DROPPRIMARYKEY, stmt, v, table, column));
									pkchangedetected = false;
								} else {
									pkchangedetected = true;
								}
							}
							if (/*
								 * (dm.getDatatype() !=
								 * DBType.Convert(cmd.datatype))
								 */
							(!DBType.GetSQLType(DBType.Convert(dm.getDataType()), dbmode).equals(
									DBType.GetSQLType(cmd.datatype, dbmode)))
									|| ((dm.getLength() != cmd.colsize) && dm.getLength() > 0)
									|| (dm.getDecimalPlace() != cmd.nks)
									|| ((notnull != cmd.isNotNull() && fkNotNullBeachten))) {
								if (debug) {
									// TODO
									System.out.println("\nUnterschiede Tabellenspalte " + column.getFullName() + " ("
											+ dm.getName() + ")");
									System.out.println("cmd:      " + cmd.toString());
									System.out.println("Datatype: " + ((short) dm.getDataType()) + " - "
											+ DBType.Convert(cmd.datatype) + " - " + cmd.datatype);
									System.out.println("SQL-Type: "
											+ DBType.GetSQLType(DBType.Convert(dm.getDataType()), dbmode) + " - "
											+ DBType.GetSQLType(cmd.datatype, dbmode));
									System.out.println("Length:   " + dm.getLength() + " - " + cmd.colsize);
									/*
									 * OLI 30.12.2008
									 * System.out.println("PK:       " +
									 * tsm.isPrimaryKey() + " - " +
									 * cmd.primaryKey);
									 */
									System.out.println("Not null: " + notnull + " - " + cmd.isNotNull());
								}
								if (!altered) {
									v.addChangingStatement("");
									v.addChangingStatement("");
									if (!htmlprinted) {
										htmlprinted = true;
									} else {
										html.addElement("        <p>");
										html.addElement("        <hr SIZE=1 WIDTH=\"100%\">");
									}
									html.addElement("        <h3>Tabelle:" + StrUtil.ToHTML(table.getName()) + "</h3>");
									s = "";
									for (StereotypeModel stm : table.getStereotypes()) {
										if (s.length() != 0) {
											s += ", ";
										} else {
											s = "        <p><i>Stereotypen: ";
										}
										s += StrUtil.ToHTML(stm.getName());
									}
									if (s.length() > 0) {
										html.addElement(s + "</i><br>");
									}
									html.addElement("        <hr SIZE=1 WIDTH=\"100%\">");
									html.addElement("        <p align=justify>"
											+ (table.getComment().length() > 0 ? StrUtil.ToHTML(table.getComment())
													: "-/-") + "<p>");
									html.addElement("        <hr SIZE=1 WIDTH=\"100%\">");
									altered = true;
								}
								// stmt = "alter table " + tm.getName() + " " +
								// (dbmode
								// == DBExecMode.MYSQL ? "modify" : "alter") +
								// " column "
								// + tsm.getName() + (dbmode ==
								// DBExecMode.POSTGRESQL ?
								// " type" : "") + " " + (hasDomains ?
								// tsm.getDomain(
								// ).getName() : tsm.getDomain().getType()) + /*
								// (
								// tsm.isPrimaryKey() ? " primary key" : */
								// (notnull
								// ? " not null" : " null")/*)*/ + " default "
								// +
								// (tsm.getDomain().getInitialwert().equals("NULL")
								// ? "null" :
								// (tsm.getDomain().getType().toLowerCase(
								// ).contains("char") ||
								// tsm.getDomain().getType(
								// ).toLowerCase().contains("text") ? "'" +
								// tsm.getDomain(
								// ).getInitialwert() + "'" : tsm.getDomain(
								// ).getInitialwert())) + ";";
								if ((!DBType.GetSQLType(DBType.Convert(dm.getDataType()), dbmode).equals(
										DBType.GetSQLType(cmd.datatype, dbmode)))
										|| ((dm.getLength() != cmd.colsize) && dm.getLength() > 0)
										|| (dm.getDecimalPlace() != cmd.nks)) {
									v.addChangingStatement(sqlScriptFactory.alterTableAlterColumnSetDataTypeStatement(
											column, hasDomains));
									v.addChangingStatement(sqlScriptFactory
											.alterTableAlterColumnSetDefaultStatement(column));
								}
								if ((notnull != cmd.isNotNull()) && fkNotNullBeachten) {
									String cnn = sqlScriptFactory.alterTableModifyConstraintNotNull(column);
									if (notnull) {
										v.addReducingStatement(cnn);
									} else {
										v.addExtendingStatement(cnn);
									}
								}
								this.dataModel.fireDataSchemeChanged(new SQLScriptEvent(SQLScriptEventType.ALTERCOLUMN,
										stmt, v, table, column));
								html.addElement("    <p><b>"
										+ StrUtil.ToHTML(column.getName())
										+ "</b> :"
										+ (hasDomains ? column.getDomain().getName() : column.getDomain().getType())
										+ (column.isPrimaryKey() ? " <b>(PK)</b>" : "")
										+ (column.getRelation() != null ? " <i>(FK -&gt; "
												+ column.getReferencedColumn() + ")</i>" : "")
										+ (notnull ? " <b>NOT NULL</b>" : "")
										+ (column.isDeprecated() ? " <b>AUFGEHOBEN!</b>" : ""));
								html.addElement("    <p align=justify>" + descriptionGetter.getDescription(column)
										+ "&nbsp;&nbsp;&nbsp;&nbsp;<u>ge&auml;ndert</u><br>" + "&nbsp;");
							}
						}
					}
					deleted = false;
					for (Iterator it = tmd.getColumns().keySet().iterator(); it.hasNext();) {
						colname = (String) it.next();
						cmd = (ColumnMetaData) tmd.getColumn(colname);
						if (cmd != null) {
							ColumnModel column = this.findColumn(table.getColumns(), cmd.name);
							if (column == null) {
								if (!altered) {
									v.addReducingStatement("");
									if (!htmlprinted) {
										htmlprinted = true;
									} else {
										html.addElement("        <p>");
										html.addElement("        <hr SIZE=1 WIDTH=\"100%\">");
									}
									html.addElement("        <h3>" + StrUtil.ToHTML(table.getName()) + "</h3>");
									html.addElement("        <hr SIZE=1 WIDTH=\"100%\">");
									html.addElement("        <p align=justify>"
											+ (table.getComment().length() > 0 ? StrUtil.ToHTML(table.getComment())
													: "-/-") + "<p>");
									html.addElement("        <hr SIZE=1 WIDTH=\"100%\">");
									altered = true;
								}
								if (!deleted) {
									v.addReducingStatement("");
									deleted = true;
								}
								// TODO Gegen Factory-Methodenaufruf
								// austauschen.
								v.addReducingStatement("ALTER TABLE " + Str.quote(table.getName(), quoteCharacter)
										+ " DROP COLUMN " + Str.quote(cmd.name, quoteCharacter) + ";");
								this.dataModel.fireDataSchemeChanged(new SQLScriptEvent(SQLScriptEventType.DROPCOLUMN,
										stmt, v, table, cmd.name));
								html.addElement("    <p><strike><b>" + StrUtil.ToHTML(cmd.name) + "</b>: "
										+ StrUtil.ToHTML(cmd.getSQLType()) + "</strike>");
							}
						}
					}
				}
				// Handling der Primaerschluesselaenderungen.
				if (pkchangedetected) {
					v.addExtendingStatement("");
					v.addExtendingStatement("/* Primaerschluesselaenderungen. */");
					// TODO OLI 11.03.2011 - Hier ist zu pruefen, ob das
					// Loeschen von
					// Primaerschluesseln nicht fuer die gaengigen DBMS
					// vorkonfiguriert
					// werden kann.
					// TODO Gegen Factory-Methodenaufruf austauschen.
					stmt = System.getProperty("archimedes.template." + dbmode.toToken().toLowerCase()
							+ ".statement.pk.drop", System.getProperty("archimedes.template.statement.pk.drop",
							"alter table " + Str.quote("$TABLENAME$", quoteCharacter) + " drop primary key;"));
					stmt = wildCardChanged.change(stmt, table.getName(), null, null);
					v.addExtendingStatement(stmt);
					this.dataModel.fireDataSchemeChanged(new SQLScriptEvent(SQLScriptEventType.DROPPRIMARYKEY, stmt, v,
							table));
					if (dbmode == DBExecMode.MSSQL) {
						v.addExtendingStatement("go");
					}
					sb = new StringBuffer();
					svtsm = new SortedVector<ColumnModel>();
					for (ColumnModel column : table.getColumns()) {
						if (column.isPrimaryKey()) {
							if (sb.length() > 0) {
								sb.append(", ");
							}
							sb.append(Str.quote(column.getName(), quoteCharacter));
							svtsm.addElement(column);
						}
					}
					if (sb.length() > 0) {
						// TODO Gegen Factory-Methodenaufruf austauschen.
						v.addReducingStatement("ALTER TABLE " + Str.quote(table.getName(), quoteCharacter)
								+ " ADD PRIMARY KEY(" + sb.toString() + ");");
						this.dataModel.fireDataSchemeChanged(new SQLScriptEvent(SQLScriptEventType.ADDPRIMARYKEY, stmt,
								v, table, svtsm));
						if (dbmode == DBExecMode.MSSQL) {
							v.addReducingStatement("go");
						}
					}
				}
				// Handling der Indices.
				for (ColumnModel column : table.getColumns()) {
					if (column.hasIndex()) {
						indexname = this.getIndexName(table, column);
						if (!indexExists(indexname, indices)) {
							if (dbmode == DBExecMode.MSSQL) {
								v.addReducingStatement("go");
							}
							v.addReducingStatement("");
							// TODO Gegen Factory-Methodenaufruf austauschen.
							v.addReducingStatement("CREATE INDEX " + indexname + " ON "
									+ Str.quote(table.getName(), quoteCharacter) + " ("
									+ Str.quote(column.getName(), quoteCharacter) + ");");
							this.dataModel.fireDataSchemeChanged(new SQLScriptEvent(SQLScriptEventType.ADDINDEX, stmt,
									v, table, column, "", "", null, indexname, null));
							if (dbmode == DBExecMode.MSSQL) {
								v.addReducingStatement("go");
							}
						} else {
							this.removeIndex(indexname.toLowerCase(), indices);
						}
					}
				}
				for (ColumnModel column : table.getColumns()) {
					indexname = this.getIndexName(table, column);
					if (indexExists(indexname, indices)) {
						// TODO Gegen Factory-Methodenaufruf austauschen.
						if (dbmode == DBExecMode.MSSQL) {
							v.addExtendingStatement("go");
						}
						v.addExtendingStatement("");
						v.addExtendingStatement("DROP INDEX " + (dbmode == DBExecMode.POSTGRESQL ? "IF EXISTS " : "")
								+ indexname + (dbmode != DBExecMode.POSTGRESQL ? " on " + table.getName() : "") + ";");
						this.dataModel.fireDataSchemeChanged(new SQLScriptEvent(SQLScriptEventType.DROPINDEX, stmt, v,
								table, column, "", "", null, indexname, null));
						this.removeIndex(indexname.toLowerCase(), indices);
					}
				}
				// TODO Gegen Factory-Methodenaufruf austauschen.
				new UniqueConstraintBuilder(dbmode, this.dataModel, quoteCharacter).addUniqueChanges(table, v, uniques);
			}
			for (String ats : sqlScriptFactory.alterTableAddConstraintForComplexUniques(table, uniques)) {
				v.addReducingStatement(ats);
			}
		}
		for (String ats : sqlScriptFactory.alterTableDropConstraintForComplexUniques(this.dataModel, uniques)) {
			v.addExtendingStatement(ats);
		}
		// TODO Gegen Factory-Methodenaufruf austauschen.
		new DefaultComplexIndexUpdateGenerator().generate(indices, Arrays.asList(this.dataModel.getComplexIndices()),
				new ComplexIndexSQLScriptAppender(v), new ComplexIndexDescriptionScriptAppender(html));
		if (referenzenSetzen) {
			// TODO Gegen Factory-Methodenaufruf austauschen.
			new ForeignKeyAddConstraintBuilder(dbmode, this.dataModel, md, quoteCharacter).build(v);
		}
		if (!suppressAdditionalSQL && (this.dataModel.getAdditionalSQLCodePostReducingCode().length() > 0)) {
			v.addReducingStatement("");
			v.addReducingStatement("");
			v.addReducingStatement("/* ADDITIONAL SQL CODE. */");
			v.addReducingStatement(this.dataModel.getAdditionalSQLCodePostReducingCode());
		}
		html.addElement("        <hr SIZE=3 WIDTH=\"100%\">");
		html.addElement("        gedruckt:" + new PDate());
		html.addElement("        <br>Author: " + StrUtil.ToHTML(this.dataModel.getAuthor()));
		html.addElement("        <br>");
		html.addElement("        <hr SIZE=3 WIDTH=\"100%\">");
		html.addElement("        </font>");
		html.addElement("    </body>");
		html.addElement("</html>");
		if ((this.dataModel.getDBVersionTablename() != null) && (this.dataModel.getDBVersionTablename().length() > 0)) {
			this.dataModel.fireDataSchemeChanged(new SQLScriptEvent(SQLScriptEventType.BEFOREDBVERSION, stmt, v, null,
					null, "", "", null, null, this.dataModel.getVersion()));
			v.addReducingStatement("");
			v.addReducingStatement("");
			v.addReducingStatement("/* UPDATE OF THE VERSION TABLE. */");
			v.addReducingStatement("INSERT INTO " + Str.quote(this.dataModel.getDBVersionTablename(), quoteCharacter)
					+ " (" + Str.quote(this.dataModel.getDBVersionDBVersionColumn(), quoteCharacter) + ", "
					+ Str.quote(this.dataModel.getDBVersionDescriptionColumn(), quoteCharacter) + ") VALUES ("
					+ this.dataModel.getVersion() + ", '" + this.dataModel.getVersionComment() + "');");
		}
		this.dataModel.fireDataSchemeChanged(new SQLScriptEvent(SQLScriptEventType.COMPLETEBUILDING, stmt, v, null,
				null, "", "", null, null, this.dataModel.getVersion()));
		for (int i = 0, leni = vsqlsl.size(); i < leni; i++) {
			sqlsl = vsqlsl.get(i);
			this.dataModel.removeSQLScriptListener(sqlsl);
		}
		Vector vs = new Vector();
		vs.add(v.createScript("", "", "", "", ""));
		return vs;
	}

	private TableModel findTable(List<TableModel> tables, String name) {
		for (TableModel table : tables) {
			if (table.getName().equalsIgnoreCase(name)) {
				return table;
			}
		}
		return null;
	}

	private TableMetaData findTableMetaData(List metaData, TableModel table) {
		for (int j = 0, lenj = metaData.size(); j < lenj; j++) {
			TableMetaData tmd = (TableMetaData) metaData.get(j);
			if (table.getName().equalsIgnoreCase(tmd.name)) {
				return tmd;
			}
		}
		return null;
	}

	private ColumnModel findColumn(ColumnModel[] columns, String name) {
		for (ColumnModel column : columns) {
			if (column.getName().equalsIgnoreCase(name)) {
				return column;
			}
		}
		return null;
	}

	private boolean indexExists(String indexName, List<SimpleIndexMetaData> indices) {
		for (SimpleIndexMetaData index : indices) {
			if (index.getName().toLowerCase().equals(indexName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private void removeIndex(String indexName, List<SimpleIndexMetaData> indices) {
		for (SimpleIndexMetaData index : indices) {
			if (index.getName().toLowerCase().equals(indexName.toLowerCase())) {
				indices.remove(index);
				return;
			}
		}
	}

	private String getIndexName(TableModel table, ColumnModel column) {
		return "I" + table.getName() + column.getName();
	}

}