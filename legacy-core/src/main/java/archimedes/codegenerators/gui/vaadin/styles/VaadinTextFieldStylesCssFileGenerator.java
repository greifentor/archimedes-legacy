package archimedes.codegenerators.gui.vaadin.styles;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.model.DataModel;

public class VaadinTextFieldStylesCssFileGenerator extends AbstractStylesFileGenerator {

	public VaadinTextFieldStylesCssFileGenerator(AbstractCodeFactory codeFactory) {
		super("styles/VaadinTextFieldCssFile.vm", codeFactory);
	}

	@Override
	public String getClassName(DataModel model, DataModel t) {
		return "vaadin-text-field-styles";
	}

}