/*
 * DomainDataChecker.java
 *
 * 18.11.2014
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import archimedes.model.DomainModel;
import corent.db.DBExecMode;
import corent.db.DBType;

/**
 * Checks and changes domains dependent to the DB mode.
 * 
 * @author ollie
 * 
 * @changed OLI 18.11.2014 - Added.
 */

public class DomainDataChecker {

	/**
	 * Returns the SQL type name for the passed domain.
	 * 
	 * @param domain
	 *            The domain which the SQL type is to return for.
	 * @param dbmode
	 *            The DB mode for which is to check for.
	 * @return The SQL type for the domain.
	 * 
	 * @changed OLI 18.11.2014 - Added.
	 */
	public String getSQLType(DomainModel domain, DBExecMode dbmode) {
		if ((domain.getDataType() == -1) && (dbmode == DBExecMode.POSTGRESQL)) {
			return "VARCHAR";
		}
		return DBType.GetSQLType(DBType.Convert(domain.getDataType()), dbmode);
	}

	/**
	 * Returns the length of fields for the passed domain.
	 * 
	 * @param domain
	 *            The domain which the field length is to return for.
	 * @param dbmode
	 *            The DB mode for which is to check for.
	 * @return The field length for the domain.
	 * 
	 * @changed OLI 18.11.2014 - Added.
	 */
	public int getLength(DomainModel domain, DBExecMode dbmode) {
		if ((domain.getDataType() == -1) && (dbmode == DBExecMode.POSTGRESQL)) {
			return Integer.MAX_VALUE;
		}
		return domain.getLength();
	}

}