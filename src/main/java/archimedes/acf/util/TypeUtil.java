/*
 * TypeUtil.java
 *
 * 29.05.2015
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.acf.util;

import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import archimedes.acf.EnumInfo;
import archimedes.acf.param.DomainParamIds;
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DomainModel;
import archimedes.legacy.model.TableModel;

/**
 * A collection of utility method for working with types.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 29.05.2015 - Added.
 */

public class TypeUtil {

	/**
	 * Returns a matching Java type for the passed column.
	 *
	 * @param column               The column which the Java type is to return for.
	 * @param referencesToKeyClass Set this flag, if references should be represented by keys only (if unset references
	 *                             are represented by Java pointers to referenced object.
	 * @param timestampsWrapped    Set this flag if fields of timestamp domains are to wrap by timestamp classes of the
	 *                             "corentx.dates" package.
	 * @param entityGenerator      Set this flag if the generator which calls the method is an entity code generator.
	 * @param parameterUtil        The parameter utilities.
	 * @param generatorUtil        A utility class for generators.
	 * @param imports              The import list of the current code generator process.
	 * @return The name of the Java type.
	 *
	 * @changed OLI 06.04.2016 - Added.
	 */
	public String getJavaType(ColumnModel column, boolean referencesToKeyClass, boolean timestampsWrapped,
			boolean entityGenerator, ParameterUtil parameterUtil, CodeGeneratorUtil generatorUtil, ImportList imports) {
		String type = generatorUtil.getJavaType(column);
		if (column.getReferencedTable() != null) {
			return this.createTypeForReference(type, column, referencesToKeyClass, timestampsWrapped, entityGenerator,
					parameterUtil, generatorUtil, imports);
		} else if (type.equals("String")) {
			EnumInfo ei = this.getEnumInfo(column.getDomain(), parameterUtil);
			if (ei != null) {
				return ei.getEnumTypeName();
			}
		} else if (column.getDomain().getDataType() == Types.BLOB) {
			type = "byte[]";
		} else if (column.getDomain().getDataType() == Types.TIMESTAMP) {
			type = "Timestamp";
		}
		if (timestampsWrapped && this.isTimestampDomain(column.getDomain())) {
			type = this.getTimestampWrapper(column.getDomain());
		}
		while (type.contains(".")) {
			type = type.substring(type.indexOf(".") + 1);
		}
		if (!column.isNotNull() && !column.isPrimaryKey()) {
			type = this.getWrapperClass(type);
		}
		return type.trim();
	}

	/**
	 * Returns a matching Java type for the passed reference column.
	 *
	 * @param type                 The type specified on the plain column domain.
	 * @param column               The column which the Java type is to return for.
	 * @param referencesToKeyClass Set this flag, if references should be represented by keys only (if unset references
	 *                             are represented by Java pointers to referenced object.
	 * @param timestampsWrapped    Set this flag if fields of timestamp domains are to wrap by timestamp classes of the
	 *                             "corentx.dates" package.
	 * @param entityGenerator      Set this flag if the generator which calls the method is an entity code generator.
	 * @param parameterUtil        The parameter utilities.
	 * @param generatorUtil        A utility class for generators.
	 * @param imports              The import list of the current code generator process.
	 * @return The name of the Java type for the passed reference column.
	 *
	 * @changed OLI 06.04.2016 - Added.
	 */
	protected String createTypeForReference(String type, ColumnModel column, boolean referencesToKeyClass,
			boolean timestampsWrapped, boolean entityGenerator, ParameterUtil parameterUtil,
			CodeGeneratorUtil generatorUtil, ImportList imports) {
		return type;
	}

	/**
	 * Returns the enum information for a domain (or <CODE>null</CODE> if the domain doesn't provide those infos).
	 *
	 * @param domain        The domain which the enum information is to create for.
	 * @param parameterUtil Utilities for parameter access.
	 * @return The enum infos for the domain or <CODE>null</CODE> if the domain is not an enum.
	 *
	 * @changed OLI 06.04.2016 - Added.
	 */
	protected EnumInfo getEnumInfo(DomainModel domain, ParameterUtil parameterUtil) {
		String param = parameterUtil.getParameterStartsWith(DomainParamIds.ENUM, domain);
		if (param != null) {
			String pn = param.substring(param.indexOf(":"));
			String s = param.substring(param.indexOf(":") + 1);
			while (s.contains(".")) {
				s = s.substring(s.indexOf(".") + 1);
			}
			return new EnumInfo(s.trim(), pn.trim());
		}
		return null;
	}

	/**
	 * Returns the timestamp columns of the table model or an empty array if there are none.
	 *
	 * @param tm The table model whose timestamp columns are to return.
	 * @return The timestamp columns of the table model or an empty array if there are none.
	 *
	 * @changed OLI 29.05.2015 - Added comment.
	 */
	public ColumnModel[] getTimestampColumns(TableModel tm) {
		List<ColumnModel> l = new LinkedList<ColumnModel>();
		for (ColumnModel c : tm.getColumns()) {
			if (this.getTimestampWrapper(c.getDomain()) != null) {
				l.add(c);
			}
		}
		return l.toArray(new ColumnModel[0]);
	}

	/**
	 * Returns a java class for the passed timestamp domain.
	 * 
	 * @param domain The domain which a timestamp class is to find for.
	 * @return The matching timestamp class or <CODE>null</CODE> of the domain is not matching a timestamp class.
	 *
	 * @changed OLI 29.05.2015 - Added comment.
	 */
	public String getTimestampWrapper(DomainModel domain) {
		if (domain.getName().equals("LongPTimestamp")) {
			return "Long";
		} else if (domain.getName().equals("PDate")) {
			return "Integer";
		} else if (domain.getName().equals("PTime")) {
			return "Integer";
		} else if (domain.getName().equals("PTimestamp")) {
			return "Long";
		}
		return null;
	}

	/**
	 * Returns a wrapper to simple type method call with a leading dot.
	 *
	 * @param wn The name of teh wrapper class.
	 * @return The wrapper to simple type method call with a leading dot.
	 *
	 * @changed OLI 22.01.2014 - Added.
	 */
	public String getWrapper2SimpleTypeCall(String cn) {
		if (cn.equals("Boolean")) {
			return ".booleanValue()";
		} else if (cn.equals("Byte")) {
			return ".byteValue()";
		} else if (cn.equals("Double")) {
			return ".doubleValue()";
		} else if (cn.equals("Float")) {
			return ".floatValue()";
		} else if (cn.equals("Integer")) {
			return ".intValue()";
		} else if (cn.equals("Long")) {
			return ".longValue()";
		} else if (cn.equals("Short")) {
			return ".shortValue()";
		}
		return cn;
	}

	/**
	 * Returns the wrapper class name if there is only.
	 *
	 * @param cn The class name whose wrapper class is to return.
	 * @return The wrapper class name if there is only.
	 *
	 * @changed OLI 20.11.2013 - Added.
	 */
	public String getWrapperClass(String cn) {
		if (cn.equals("boolean")) {
			return "Boolean";
		} else if (cn.equals("byte")) {
			return "Byte";
		} else if (cn.equals("double")) {
			return "Double";
		} else if (cn.equals("float")) {
			return "Float";
		} else if (cn.equals("int")) {
			return "Integer";
		} else if (cn.equals("long")) {
			return "Long";
		} else if (cn.equals("short")) {
			return "Short";
		}
		return cn;
	}

	/**
	 * Checks if the columns include at least one timestamp field.
	 *
	 * @return <CODE>true</CODE> if the table has at least one timestamp field.
	 *
	 * @changed OLI 11.10.2013 - Added.
	 */
	public boolean hasTimestampField(ColumnModel[] columns) {
		for (ColumnModel c : columns) {
			if (this.isTimestampDomain(c.getDomain())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks id the domain is a string domain.
	 *
	 * @return <CODE>true</CODE> if the domain is a string domain.
	 *
	 * @changed OLI 13.10.2015 - Added.
	 */
	public boolean isStringDomain(DomainModel dm) {
		String[] types = new String[] { "longvarchar", "varchar" };
		for (String type : types) {
			if (dm.getType().startsWith(type)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks id the domain is a timestamp domain.
	 *
	 * @return <CODE>true</CODE> if the domain is a timestamp domain.
	 *
	 * @changed OLI 11.10.2013 - Added.
	 */
	public boolean isTimestampDomain(DomainModel dm) {
		String[] domainNames = new String[] { "LongPTimestamp", "PDate", "PTime", "PTimestamp" };
		for (String domainName : domainNames) {
			if (domainName.equals(dm.getName())) {
				return true;
			}
		}
		return false;
	}

}