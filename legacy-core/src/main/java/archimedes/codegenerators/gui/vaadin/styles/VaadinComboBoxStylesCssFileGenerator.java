package archimedes.codegenerators.gui.vaadin.styles;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.model.DataModel;

public class VaadinComboBoxStylesCssFileGenerator extends AbstractStylesFileGenerator {

	public VaadinComboBoxStylesCssFileGenerator(AbstractCodeFactory codeFactory) {
		super("styles/VaadinComboBoxCssFile.vm", codeFactory);
	}

	@Override
	public String getClassName(DataModel model, DataModel t) {
		return "vaadin-combo-box-styles";
	}

}