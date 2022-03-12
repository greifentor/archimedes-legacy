package archimedes.legacy.checkers;

import static corentx.util.Checks.ensure;

import java.util.Arrays;
import java.util.stream.Collectors;

import archimedes.acf.checker.ModelChecker;
import archimedes.acf.checker.ModelCheckerMessage;
import archimedes.acf.checker.ModelCheckerMessage.Level;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import baccara.gui.GUIBundle;

/**
 * A model checker for tables without any primary key constraints.
 *
 * @author ollie (27.04.2021)
 */
public class ModelCheckerNoPrimaryKeySet implements ModelChecker {

	public static final String SUPPRESS_NO_PK_WARNING = "SUPPRESS_NO_PK_WARNING";

	public static final String RES_MODEL_CHECKER_NO_PRIMARY_KEY_SET = "ModelChecker.NoPrimaryKeySet.label";

	private GUIBundle guiBundle = null;

	public ModelCheckerNoPrimaryKeySet(GUIBundle guiBundle) {
		super();
		ensure(guiBundle != null, "GUI bundle cannot be null.");
		this.guiBundle = guiBundle;
	}

	@Override
	public ModelCheckerMessage[] check(DataModel model) {
		ensure(model != null, "data model cannot be null.");
		return Arrays
				.asList(model.getTables())
				.stream()
				.filter(table -> !table.isOptionSet(IGNORE_CHECKER_OPTION))
				.filter(
						table -> (table.getPrimaryKeyColumns().length == 0)
								&& !table.isOptionSet(SUPPRESS_NO_PK_WARNING))
				.map(table -> getMessage(table))
				.collect(Collectors.toList())
				.toArray(new ModelCheckerMessage[0]);
	}

	private ModelCheckerMessage getMessage(TableModel table) {
		return new ModelCheckerMessage(
				Level.WARNING,
				guiBundle.getResourceText(RES_MODEL_CHECKER_NO_PRIMARY_KEY_SET, table.getName()),
				table);
	}

}