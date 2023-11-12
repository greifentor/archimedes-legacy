package archimedes.codegenerators.gui.vaadin.styles;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.model.DataModel;

public class VaadinCheckboxStylesCssFileGenerator extends AbstractStylesFileGenerator {

	public VaadinCheckboxStylesCssFileGenerator(AbstractCodeFactory codeFactory) {
		super("styles/VaadinCheckboxCssFile.vm", codeFactory);
	}

	@Override
	public String getClassName(DataModel model, DataModel t) {
		return "vaadin-checkbox-styles";
	}

}