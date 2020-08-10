package archimedes.legacy.checkers;

import static corentx.util.Checks.ensure;

import java.util.Arrays;
import java.util.stream.Collectors;

import archimedes.acf.checker.ModelChecker;
import archimedes.acf.checker.ModelCheckerMessage;
import archimedes.acf.checker.ModelCheckerMessage.Level;
import archimedes.legacy.model.DataModel;
import baccara.gui.GUIBundle;

/**
 * A ModelChecker implementation for no complex primary key check.
 *
 * @author ollie (31.10.2019)
 */
public class ModelCheckerNoComplexPrimaryKey implements ModelChecker {

	public static final String RES_MODEL_CHECKER_NO_COMPLEX_PRIMARY_KEY = "ModelChecker.NoComplexPrimaryKey.label";

	private GUIBundle guiBundle = null;

	/**
	 * Creates a new model checker with the passed parameters.
	 *
	 * @param guiBundle A GUI bundle e. g. for text resources.
	 * @throws IllegalArgumentException Passing a null pointer.
	 */
	public ModelCheckerNoComplexPrimaryKey(GUIBundle guiBundle) {
		super();
		ensure(guiBundle != null, "GUI bundle cannot be null.");
		this.guiBundle = guiBundle;
	}

	/**
	 * @changed OLI 08.12.2016 - Added.
	 */
	@Override
	public ModelCheckerMessage[] check(DataModel model) {
		ensure(model != null, "data model cannot be null.");
		return Arrays.asList(model.getTables()).stream() //
				.filter(t -> (t.getPrimaryKeyColumns().length > 1 && !t.isManyToManyRelation())) //
				.map(t -> new ModelCheckerMessage( //
						Level.ERROR, //
						guiBundle.getResourceText(RES_MODEL_CHECKER_NO_COMPLEX_PRIMARY_KEY, t.getName()), //
						t)) //
				.collect(Collectors.toList()) //
				.toArray(new ModelCheckerMessage[0]);
	}

}