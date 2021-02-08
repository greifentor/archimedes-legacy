/*
 * DefaultSchemaChangeStatementGenerator.java
 *
 * 24.02.2012
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;


import corent.db.*;

import static corentx.util.Checks.*;


/**
 * Eine Default-Implementierung zur Nutzung in der Archimedes-Anwendung.
 *
 * @author ollie
 *
 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
 */

public class DefaultSchemaChangeStatementGenerator implements SchemaChangeStatementGenerator {

    /**
     * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
     */
    @Override
    public String create(String schemaName, DBExecMode dbms) throws IllegalArgumentException,
            UnsupportedOperationException {
        ensure(dbms != null, "database management system identifier cannot be null.");
        if ((schemaName == null) || schemaName.isEmpty()) {
            return null;
        }
        if (dbms == DBExecMode.POSTGRESQL) {
            return "SET search_path TO " + schemaName + ";";
        }
        throw new UnsupportedOperationException("change schema statement generation not "
                + "supported for DBMS: " + dbms);
    }

}