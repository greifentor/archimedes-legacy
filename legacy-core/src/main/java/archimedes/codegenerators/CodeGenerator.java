package archimedes.codegenerators;

import archimedes.model.DataModel;
import archimedes.model.NamedObject;

/**
 * An interface for the code generator.
 *
 * @author ollie (14.03.2021)
 */
public interface CodeGenerator<T extends NamedObject> {

	public enum Type {
		DOMAIN,
		MODEL,
		TABLE;
	}

	String getSourceFileName(String basePackageName, DataModel model, T t);

	String generate(String basePackageName, DataModel model, T t);

	String getClassName(DataModel model, T t);

	String getPackageName(DataModel model, T t);

	default String getName() {
		return getClass().getSimpleName();
	}

	default Type getType() {
		return Type.TABLE;
	}

	default boolean isDeprecated() {
		return false;
	}

}