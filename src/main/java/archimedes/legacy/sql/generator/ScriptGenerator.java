/*
 * ScriptGenerator.java
 *
 * 18.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.sql.generator;

import java.sql.SQLException;

import archimedes.legacy.connections.DatabaseConnection;
import archimedes.legacy.connections.DatabaseConnectionWildcardReplacer;
import archimedes.legacy.gui.DatabaseConnectionRecord;
import archimedes.legacy.meta.MetaDataModel;
import archimedes.legacy.meta.MetaDataModelComparator;
import archimedes.legacy.meta.MetaDataReader;
import archimedes.legacy.meta.chops.AbstractChangeOperation;
import archimedes.legacy.meta.chops.ScriptSectionType;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.UserInformation;
import archimedes.legacy.util.NameGenerator;
import corent.db.DBExecMode;
import corent.db.JDBCDataSourceRecord;
import corentx.dates.PTimestamp;

/**
 * A class which is able to generate a script for a data model and a JDBC connection. This script updates the database
 * linked by the database connection to the scheme of the data model.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.12.2015 - Added.
 */

public class ScriptGenerator {

	/**
	 * Creates a SQL script in string format which would change the database to the scheme of the data model.
	 *
	 * @param diagram         The diagram which is to match against the passed database connection.
	 * @param dcr             The data for the database connection whose database is to update.
	 * @param userInformation Some information about the user.
	 * @return A SQL script in string format which would change the database to the scheme of the data model or
	 *         <CODE>null</CODE> if no script could be created.
	 * @throws SQLException
	 *
	 * @changed OLI 18.12.2015 - Added.
	 */
	public String generate(DataModel diagram, DatabaseConnectionRecord dcr, UserInformation userInformation)
			throws SQLException {
		DatabaseConnection dc = dcr.getDatabaseConnection();
		if (dc != null) {
			dc = new DatabaseConnectionWildcardReplacer(dc).replace();
			JDBCDataSourceRecord dsr = new JDBCDataSourceRecord(dc.getDriver(), dc.getUrl(), dc.getUserName(),
					dcr.getPassword());
			MetaDataModel mdm = new MetaDataReader().read(diagram, dc.getDBMode());
			MetaDataModel mdb = new MetaDataReader().read(dsr, diagram.getSchemaName(), dc.getDBMode());
			MetaDataModelComparator mdmc = new MetaDataModelComparator(new NameGenerator(dc.getDBMode()))
					.addScript(diagram.getAdditionalSQLCodePreExtendingCode(),
							ScriptSectionType.SCRIPT_BEFORE_ANY_CHANGES)
					.addScript(diagram.getAdditionalSQLCodePreChangingCode(), ScriptSectionType.SCRIPT_BEFORE_UPDATES)
					.addScript(diagram.getAdditionalSQLCodePostChangingCode(), ScriptSectionType.SCRIPT_AFTER_UPDATES)
					.addScript(diagram.getAdditionalSQLCodePostReducingCode(),
							ScriptSectionType.SCRIPT_AFTER_ALLCHANGES);
			mdb.setTemporaryPrefix(mdm.getTemporaryPrefix());
			AbstractChangeOperation[] chops = mdmc.compare(mdm, mdb);
			SQLScriptGeneratorFactory sgf = new SQLScriptGeneratorFactory(dc.getQuote(), dc.isSetDomains(),
					dc.isSetReferences(), dc.isSetNotNull(), new NameGenerator(dc.getDBMode()),
					this.getScriptHeaderBuilder(dc.getDBMode(), mdm.getSchemeName(), mdm.getSchemeName(),
							new PTimestamp(), diagram.getVersion(), userInformation));
			SQLScriptGenerator sg = sgf.getSQLScriptGenerator(dc.getDBMode());
			if (sg != null) {
				return sg.generate(chops);
			}
		}
		return null;
	}

	ScriptHeaderBuilder getScriptHeaderBuilder(DBExecMode dbMode, String dbName, String schemeName,
			PTimestamp timestamp, String dbVersion, UserInformation userInformation) {
		return new ScriptHeaderBuilder(dbMode, dbName, schemeName, timestamp, dbVersion, userInformation);
	}

}