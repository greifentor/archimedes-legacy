package archimedes.legacy.exporter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import de.ollie.dbcomp.comparator.model.ChangeActionCRO;
import de.ollie.dbcomp.liquibase.writer.ChangeActionToDatabaseChangeLogConverter;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.serializer.core.xml.XMLChangeLogSerializer;

/**
 * A class which is able to create a liquibase script to update a database based on a diagram.
 *
 * @author ollie (24.02.2021)
 */
public class LiquibaseScriptCreator extends ScriptCreator {

	@Override
	protected List<String> createScript(List<ChangeActionCRO> changeActions) {
		DatabaseChangeLog databaseChangeLog = new ChangeActionToDatabaseChangeLogConverter().convert(changeActions);
		return Arrays.asList(exportDatabaseChangeLog(databaseChangeLog).toString());
	}

	@Override
	protected String getViewerTitel() {
		return "Liquibase Update Script";
	}

	private OutputStream exportDatabaseChangeLog(DatabaseChangeLog databaseChangeLog) {
		ByteArrayOutputStream outputStream = null;
		try {
			outputStream = new ByteArrayOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		XMLChangeLogSerializer changeLogSerializer = new XMLChangeLogSerializer();
		try {
			changeLogSerializer.write(databaseChangeLog.getChangeSets(), outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream;
	}

}