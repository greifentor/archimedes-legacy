package archimedes.legacy.exporter;

import java.util.List;

import de.ollie.dbcomp.comparator.model.ChangeActionCRO;

/**
 * A class which is able to create an SQL script to update a database based on a diagram.
 *
 * @author ollie (19.06.2021)
 */
public class SQLScriptCreator extends ScriptCreator {

	@Override
	protected List<String> createScript(List<ChangeActionCRO> changeActions, String connectionDataOptions) {
		return null;
	}

	@Override
	protected String getViewerTitel() {
		return "SQL Update Script";
	}

}