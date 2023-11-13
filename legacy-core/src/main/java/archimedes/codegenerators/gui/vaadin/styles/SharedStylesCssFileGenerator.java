package archimedes.codegenerators.gui.vaadin.styles;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.model.DataModel;

public class SharedStylesCssFileGenerator extends AbstractStylesFileGenerator {

	public SharedStylesCssFileGenerator(AbstractCodeFactory codeFactory) {
		super("styles/SharedStylesCssFile.vm", codeFactory);
	}

	@Override
	public String getClassName(DataModel model, DataModel t) {
		return "shared-styles";
	}

}