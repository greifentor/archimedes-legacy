package archimedes.legacy.checkers;

import static corentx.util.Checks.ensure;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import archimedes.acf.checker.ModelChecker;
import archimedes.acf.checker.ModelCheckerMessage;
import archimedes.acf.checker.ModelCheckerMessage.Level;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import baccara.gui.GUIBundle;

/**
 * A model checker for a potential foreign key column which lacks a constraint.
 *
 * @author ollie (27.04.2021)
 */
public class ModelCheckerPotentialForeignKeyNotSet implements ModelChecker {

	public static final String SUPPRESS_POTENTIAL_FK_WARNING = "SUPPRESS_POTENTIAL_FK_WARNING";
	public static final String POTENTIAL_FK_WARNING_MODE = "POTENTIAL_FK_WARNING_MODE";
	public static final String POTENTIAL_FK_WARNING_MODE_STRICT = "STRICT";
	public static final String POTENTIAL_FK_WARNING_MODE_WEAK = "WEAK";

	static final String RES_MODEL_CHECKER_POTENTIAL_FOREIGN_KEY = "ModelChecker.PotentialForeignKey.label";

	private GUIBundle guiBundle = null;

	public ModelCheckerPotentialForeignKeyNotSet(GUIBundle guiBundle) {
		super();
		ensure(guiBundle != null, "GUI bundle cannot be null.");
		this.guiBundle = guiBundle;
	}

	@Override
	public ModelCheckerMessage[] check(DataModel model) {
		ensure(model != null, "data model cannot be null.");
		return Arrays
		        .asList(model.getAllColumns())
		        .stream()
		        .filter(column -> isPotentialForeignKey(model, column))
		        .map(column -> getMessage(column, getPotentialTableReference(model, column.getName()).get()))
		        .collect(Collectors.toList())
		        .toArray(new ModelCheckerMessage[0]);
	}

	private boolean isPotentialForeignKey(DataModel model, ColumnModel column) {
		if (column.isOptionSet(SUPPRESS_POTENTIAL_FK_WARNING)) {
			return false;
		}
		return getPotentialTableReference(model, column.getName())
		        .map(table -> isAPotentialProblem(model, table, column))
		        .orElse(false);
	}

	private boolean isAPotentialProblem(DataModel model, TableModel table, ColumnModel column) {
		return (column.getReferencedTable() == null)
		        || (isModeStrictSet(model, column.getTable()) && (column.getReferencedTable() != table));
	}

	private Optional<TableModel> getPotentialTableReference(DataModel model, String s) {
		return Optional
		        .ofNullable(getWithTableNamePrefix(model, s).orElse(getWithTableNameSuffix(model, s).orElse(null)));
	}

	private Optional<TableModel> getWithTableNamePrefix(DataModel model, String s) {
		return !s.toLowerCase().endsWith("id")
		        ? Optional.empty()
		        : Arrays
		                .asList(model.getTables())
		                .stream()
		                .filter(table -> s.toLowerCase().startsWith(table.getName().toLowerCase()))
		                .findFirst();
	}

	private Optional<TableModel> getWithTableNameSuffix(DataModel model, String s) {
		return !s.toLowerCase().startsWith("id")
		        ? Optional.empty()
		        : Arrays
		                .asList(model.getTables())
		                .stream()
		                .filter(table -> s.toLowerCase().endsWith(table.getName().toLowerCase()))
		                .findFirst();
	}

	private ModelCheckerMessage getMessage(ColumnModel column, TableModel table) {
		return new ModelCheckerMessage(
		        Level.WARNING,
		        guiBundle
		                .getResourceText(
		                        RES_MODEL_CHECKER_POTENTIAL_FOREIGN_KEY,
		                        column.getFullName(),
		                        table.getName()),
		        column.getTable());
	}

	private boolean isModeStrictSet(DataModel dataModel, TableModel tableModel) {
		String mode = dataModel.isOptionSet(POTENTIAL_FK_WARNING_MODE)
		        ? dataModel.getOptionByName(POTENTIAL_FK_WARNING_MODE).getParameter()
		        : POTENTIAL_FK_WARNING_MODE_WEAK;
		mode = tableModel.isOptionSet(POTENTIAL_FK_WARNING_MODE)
		        ? tableModel.getOptionByName(POTENTIAL_FK_WARNING_MODE).getParameter()
		        : mode;
		return POTENTIAL_FK_WARNING_MODE_STRICT.equalsIgnoreCase(mode);
	}

}