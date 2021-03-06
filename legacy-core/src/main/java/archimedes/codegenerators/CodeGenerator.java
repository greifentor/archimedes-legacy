package archimedes.codegenerators;

import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * An interface for the code generator.
 *
 * @author ollie (14.03.2021)
 */
public interface CodeGenerator {

	String getSourceFileName(String basePackageName, DataModel model, TableModel table);

	String generate(String basePackageName, DataModel model, TableModel table);

	String getClassName(TableModel table);

	String getPackageName(DataModel model, TableModel table);

	default String getName() {
		return getClass().getSimpleName();
	}

}