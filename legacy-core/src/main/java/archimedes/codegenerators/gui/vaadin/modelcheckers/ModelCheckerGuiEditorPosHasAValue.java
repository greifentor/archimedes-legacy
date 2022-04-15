package archimedes.codegenerators.gui.vaadin.modelcheckers;

import static corentx.util.Checks.ensure;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import archimedes.acf.checker.ModelChecker;
import archimedes.acf.checker.ModelCheckerMessage;
import archimedes.acf.checker.ModelCheckerMessage.Level;
import archimedes.codegenerators.gui.vaadin.AbstractGUIVaadinClassCodeGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.util.Numbers;
import baccara.gui.GUIBundle;

public class ModelCheckerGuiEditorPosHasAValue implements ModelChecker {

	public static final String RES_MODEL_CHECKER_GUI_VAADIN_GUI_EDITOR_POS_HAS_NO_NUMERIC_VALUE =
			"ModelChecker.GUIVaadin.GUIEditorPosHasNoNumericValue.label";

	private static final String OPTION_NAME_GUI_EDITOR_POS = AbstractGUIVaadinClassCodeGenerator.GUI_EDITOR_POS;

	private GUIBundle guiBundle = null;

	public ModelCheckerGuiEditorPosHasAValue(GUIBundle guiBundle) {
		super();
		ensure(guiBundle != null, "GUI bundle cannot be null.");
		this.guiBundle = guiBundle;
	}

	@Override
	public ModelCheckerMessage[] check(DataModel model) {
		return forAllColumns(model)
				.filter(this::isAGUIEditorPos)
				.filter(this::hasANonIntegerParameter)
				.map(this::getModelCheckerMessage)
				.collect(Collectors.toList())
				.toArray(new ModelCheckerMessage[0]);
	}

	private Stream<ColumnModel> forAllColumns(DataModel model) {
		return List.of(model.getAllColumns()).stream();
	}

	private boolean isAGUIEditorPos(ColumnModel column) {
		return column.isOptionSet(OPTION_NAME_GUI_EDITOR_POS);
	}

	private boolean hasANonIntegerParameter(ColumnModel column) {
		return !Numbers.isAnInteger(column.getOptionByName(OPTION_NAME_GUI_EDITOR_POS).getParameter());
	}

	private ModelCheckerMessage getModelCheckerMessage(ColumnModel column) {
		String parameter = column.getOptionByName(OPTION_NAME_GUI_EDITOR_POS).getParameter();
		return new ModelCheckerMessage(
				Level.ERROR,
				guiBundle.getResourceText(RES_MODEL_CHECKER_GUI_VAADIN_GUI_EDITOR_POS_HAS_NO_NUMERIC_VALUE, parameter),
				column);

	}

}