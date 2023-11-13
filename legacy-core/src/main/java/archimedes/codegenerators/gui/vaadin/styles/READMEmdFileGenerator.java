package archimedes.codegenerators.gui.vaadin.styles;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.model.DataModel;

/**
 * A class code generator for the session id classes.
 *
 * @author ollie (02.09.2022)
 */
public class READMEmdFileGenerator extends AbstractStylesFileGenerator {

	public READMEmdFileGenerator(AbstractCodeFactory codeFactory) {
		super("styles/READMEmdFile.vm", ".md", codeFactory);
	}

	@Override
	public String getClassName(DataModel model, DataModel t) {
		return "README";
	}

}