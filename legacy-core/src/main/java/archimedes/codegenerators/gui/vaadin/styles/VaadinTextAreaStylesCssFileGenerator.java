package archimedes.codegenerators.gui.vaadin.styles;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.model.DataModel;

public class VaadinTextAreaStylesCssFileGenerator extends AbstractStylesFileGenerator {

	public VaadinTextAreaStylesCssFileGenerator(AbstractCodeFactory codeFactory) {
		super("styles/VaadinTextAreaCssFile.vm", codeFactory);
	}

	@Override
	public String getClassName(DataModel model, DataModel t) {
		return "vaadin-text-area-styles";
	}

}