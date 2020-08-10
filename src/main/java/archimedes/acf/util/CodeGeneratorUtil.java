/*
 * CodeGeneratorUtil.java
 *
 * 06.08.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.util;

import static corentx.util.Checks.ensure;

import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DomainModel;
import archimedes.legacy.model.TableModel;
import gengen.util.Converter;

/**
 * Some utility methods for code generation tasks.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 06.08.2013 - Added.
 */

public class CodeGeneratorUtil {

	/**
	 * Creates a getter name for the passed column model.
	 *
	 * @param column The column model which the getter name is to create for.
	 * @return The getter name for the column model.
	 * @throws IllegalArgumentException In case of passing a null pointer.
	 *
	 * @changed OLI 09.08.2013 - Added.
	 */
	public String createGetterName(ColumnModel column) throws IllegalArgumentException {
		ensure(column != null, "column cannot be null.");
		return (this.getJavaType(column).equals("boolean") ? "is" : "get") + column.getName();
	}

	/**
	 * Creates an attribute name from the passed column model.
	 *
	 * @param column The column model which the attribute is to create for.
	 * @throws IllegalArgumentException In case of passing a null pointer.
	 *
	 * @changed OLI 06.08.2013 - Added.
	 */
	public String getAttributeName(ColumnModel column) throws IllegalArgumentException {
		ensure(column != null, "column cannot be null.");
		return this.getAttributeName(column.getName());
	}

	/**
	 * Creates an attribute name from the passed domain model.
	 *
	 * @param domain The domain model which the attribute is to create for.
	 * @throws IllegalArgumentException In case of passing a null pointer.
	 *
	 * @changed OLI 30.09.2013 - Added.
	 */
	public String getAttributeName(DomainModel domain) throws IllegalArgumentException {
		ensure(domain != null, "domain cannot be null.");
		return this.getAttributeName(domain.getName());
	}

	/**
	 * Creates an attribute name from the passed string.
	 *
	 * @param s The string which the attribute is to create for.
	 * @throws IllegalArgumentException In case of passing a null pointer.
	 *
	 * @changed OLI 30.09.2013 - Added.
	 */
	public String getAttributeName(String s) throws IllegalArgumentException {
		ensure(s != null, "table cannot be null.");
		ensure(!s.isEmpty(), "string cannot be empty.");
		s = s.replace(".", "");
		return s.substring(0, 1).toLowerCase().concat(s.substring(1));
	}

	/**
	 * Creates an attribute name from the passed table model.
	 *
	 * @param table The table model which the attribute is to create for.
	 * @throws IllegalArgumentException In case of passing a null pointer.
	 *
	 * @changed OLI 06.08.2013 - Added.
	 */
	public String getAttributeName(TableModel table) throws IllegalArgumentException {
		ensure(table != null, "table cannot be null.");
		return this.getAttributeName(table.getName());
	}

	/**
	 * Returns the java type for the passed column model.
	 *
	 * @param column The column model which the java type is to return for.
	 * @return The java type for the passed column model.
	 * @throws IllegalArgumentException In case of passing a null pointer.
	 *
	 * @changed OLI 09.08.2013 - Added.
	 */
	public String getJavaType(ColumnModel column) throws IllegalArgumentException {
		ensure(column != null, "column cannot be null.");
		return Converter.toJavaType(column.getDomain().getDataType(), column.getDomain().getName(), "corentx.dates");
	}

}