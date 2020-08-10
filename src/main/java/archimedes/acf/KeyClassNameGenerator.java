/*
 * KeyClassNameGenerator.java
 *
 * 29.04.2016
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.acf;

import archimedes.legacy.model.TableModel;

/**
 * An interface for key class name generators.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 29.04.2016 - Added.
 */

public interface KeyClassNameGenerator {

	/**
	 * Returns the name for the key class of the passed table.
	 *
	 * @param table The table which the key class name is to generate for.
	 * @return The name for the key class of the passed table.
	 *
	 * @changed OLI 29.04.2016 - Added.
	 */
	abstract public String getName(TableModel tm);

}