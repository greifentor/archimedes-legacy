/*
 * StandardSQLScriptGenerator.java
 *
 * 14.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.sql.generator;

import static corentx.util.Checks.*;

import java.util.*;

import corent.db.*;
import corentx.util.*;
import archimedes.meta.*;
import archimedes.meta.chops.*;
import archimedes.meta.chops.addscripts.*;
import archimedes.meta.chops.columns.*;
import archimedes.meta.chops.foreignkeys.*;
import archimedes.meta.chops.indices.*;
import archimedes.meta.chops.primarykeys.*;
import archimedes.meta.chops.sequences.*;
import archimedes.meta.chops.tables.*;
import archimedes.meta.chops.uniques.*;
import archimedes.util.*;


/**
 * A SQL script generator for "Standard" SQL. It can be used as base for other SQL script
 * generator implementations.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 14.12.2015 - Added.
 */

public class StandardSQLScriptGenerator implements SQLScriptGenerator {

    private DBExecMode dbMode = null;
    private boolean foreignKeyConstraints = false;
    private MetaUtil metaUtil = null;
    private NameGenerator nameGenerator = null;
    private boolean notNullConstraints = false;
    private String quotes = null;
    private ScriptHeaderBuilder scriptHeaderBuilder = null;
    private boolean useDomains = false;

    /**
     * Creates a new SQL script generator for standard SQL with the passed parameters.
     *
     * @param dbMode The mode of the DBMS which the SQL script is to create for.
     * @param quotes The quotes for object identifiers (like table names).
     * @param useDomains Set this flag if the database should work with domains.
     * @param foreignKeyConstraints Set this flag if the foreign key constraints should be set. 
     * @param notNullConstraints Set this flag if the not null constraints should be set.
     * @param nameGenerator A generator for data base object names.
     * @param scriptHeaderBuilder A builder for the script header.
     * @throws IllegalArgumentException Passing a null pointer. 
     *
     * @changed OLI 14.12.2015 - Added.
     */
    public StandardSQLScriptGenerator(DBExecMode dbMode, String quotes, boolean useDomains,
            boolean foreignKeyConstraints, boolean notNullConstraints,
            NameGenerator nameGenerator, ScriptHeaderBuilder scriptHeaderBuilder)
            throws IllegalArgumentException {
        super();
        ensure(dbMode != null, "DB mode cannot be null.");
        ensure(quotes != null, "quotes cannot be null");
        this.dbMode = dbMode;
        this.foreignKeyConstraints = foreignKeyConstraints;
        this.nameGenerator = nameGenerator;
        this.notNullConstraints = notNullConstraints;
        this.quotes = quotes;
        this.scriptHeaderBuilder = scriptHeaderBuilder;
        this.useDomains = useDomains;
        this.metaUtil = new MetaUtil(this.quotes);
    }

    /**
     * @changed OLI 14.12.2015 - Added.
     */
    @Override public String generate(AbstractChangeOperation[] changesArr)
            throws IllegalArgumentException {
        String sql = this.scriptHeaderBuilder.build();
        List<AbstractChangeOperation> changes = new SortedVector<AbstractChangeOperation>();
        for (AbstractChangeOperation change : changesArr) {
            changes.add(change);
        }
        for (AbstractChangeOperation change : changesArr) {
            this.prepareSQLStatementForChangeOperation(changes, change);
        }
        ScriptSectionType section = null;
        for (AbstractChangeOperation change : changes) {
            String s = this.getSQLStatementForChangeOperation(change);
            if ((s != null) && (s.length() > 0)) {
                if (section != change.getSection()) {
                    section = change.getSection();
                    sql += "\n\n" + Str.pumpUp("/** SECTION: " + section.name() + " ", "*",
                            69, Direction.RIGHT) + "/\n\n";
                }
                sql += s + (!s.endsWith(";") && !s.endsWith("*/") && !s.endsWith("\n")
                        ? ";" : "") + "\n";
            }
        }
        return sql;
    }

    protected String getSQLStatementForChangeOperation(AbstractChangeOperation change) {
        if (change instanceof AddColumn) {
            return this.transferAddColumnToSQLStatement((AddColumn) change);
        } else if (change instanceof AddAdditionalScript) {
            return this.transferAddAdditionalScriptToSQLStatement(
                    (AddAdditionalScript) change);
        } else if (change instanceof AddComplexUniqueConstraint) {
            return this.transferAddComplexUniqueConstraintToSQLStatement(
                    (AddComplexUniqueConstraint) change);
        } else if (change instanceof AddForeignKeyConstraint) {
            return this.transferAddForeignKeyConstraintToSQLStatement(
                    (AddForeignKeyConstraint) change);
        } else if (change instanceof AddMultipleForeignKeyConstraint) {
            return this.transferAddMultipleForeignKeyConstraintToSQLStatement(
                    (AddMultipleForeignKeyConstraint) change);
        } else if (change instanceof AddPrimaryKeyConstraint) {
            return this.transferAddPrimaryKeyConstraintToSQLStatement(
                    (AddPrimaryKeyConstraint) change);
        } else if (change instanceof AlterColumnAddConstraint) {
            return this.transferAlterColumnAddConstraintToSQLStatement(
                    (AlterColumnAddConstraint) change);
        } else if (change instanceof AlterColumnDataType) {
            return this.transferAlterColumnDataTypeToSQLStatement((AlterColumnDataType) change);
        } else if (change instanceof AlterColumnDropConstraint) {
            return this.transferAlterColumnDropConstraintToSQLStatement(
                    (AlterColumnDropConstraint) change);
        } else if (change instanceof AlterColumnDropDefault) {
            return this.transferAlterColumnDropDefaultToSQLStatement(
                    (AlterColumnDropDefault) change);
        } else if (change instanceof AlterColumnSetDefaultValue) {
            return this.transferAlterColumnSetDefaultValueToSQLStatement(
                    (AlterColumnSetDefaultValue) change);
        } else if (change instanceof AlterSequence) {
            return this.transferAlterSequenceToSQLStatement((AlterSequence) change);
        } else if (change instanceof CreateIndex) {
            return this.transferCreateIndexToSQLStatement((CreateIndex) change);
        } else if (change instanceof CreateSequence) {
            return this.transferCreateSequenceToSQLStatement((CreateSequence) change);
        } else if (change instanceof CreateTable) {
            return this.transferCreateTableToSQLStatement((CreateTable) change);
        } else if (change instanceof DropColumn) {
            return this.transferDropColumnToSQLStatement((DropColumn) change);
        } else if (change instanceof DropComplexUniqueConstraint) {
            return this.transferDropComplexUniqueConstraintToSQLStatement(
                    (DropComplexUniqueConstraint) change);
        } else if (change instanceof DropForeignKeyConstraint) {
            return this.transferDropForeignKeyConstraintToSQLStatement(
                    (DropForeignKeyConstraint) change);
        } else if (change instanceof DropIndex) {
            return this.transferDropIndexToSQLStatement((DropIndex) change);
        } else if (change instanceof DropMultipleForeignKeyConstraint) {
            return this.transferDropMultipleForeignKeyConstraintToSQLStatement(
                    (DropMultipleForeignKeyConstraint) change);
        } else if (change instanceof DropPrimaryKeyConstraint) {
            return this.transferDropPrimaryKeyConstraintToSQLStatement((DropPrimaryKeyConstraint) change);
        } else if (change instanceof DropSequence) {
            return this.transferDropSequenceToSQLStatement((DropSequence) change);
        } else if (change instanceof DropTable) {
            return this.transferDropTableToSQLStatement((DropTable) change);
        }
        return null;
    }

    /**
     * @changed OLI 15.12.2015 - Added.
     */
    protected boolean isStringTypeColumn(MetaDataColumn column) {
        String type = column.getSQLType(this.dbMode).toUpperCase();
        if (type.startsWith("CHAR") || type.startsWith("VARCHAR") || type.startsWith("TEXT")) {
            return true;
        }
        return false;
    }

    protected void prepareSQLStatementForChangeOperation(
            List<AbstractChangeOperation> changes, AbstractChangeOperation change) {
        if (change instanceof CreateTable) {
            this.prepareCreateTableToSQLStatement(changes, (CreateTable) change);
        }
    }

    /**
     * @changed OLI 16.12.2015 - Added.
     */
    protected void prepareCreateTableToSQLStatement(List<AbstractChangeOperation> changes,
            CreateTable change) {
        MetaDataTable t = change.getTable();
        List<MetaDataColumn> uniques = new SortedVector<MetaDataColumn>();
        MetaDataColumn[] columns = t.getColumns();
        for (MetaDataColumn c : columns) {
            if (c.isUnique()) {
                uniques.add(c);
            }
        }
        if (uniques.size() > 0) {
            for (MetaDataColumn u : uniques) {
                changes.add(new AlterColumnAddConstraint(t, u, ColumnConstraintType.UNIQUE,
                        ScriptSectionType.UPDATE_CONSTRAINTS));
            }
        }
    }

    /**
     * @changed OLI 14.12.2015 - Added.
     */
    protected String transferAddColumnToSQLStatement(AddColumn change) {
        MetaDataColumn column = change.getColumn(); 
        String s = "ALTER TABLE " + this.metaUtil.quote(change.getTable());
        s += " ADD " + this.metaUtil.quote(column) + " ";
        s += column.getSQLType(this.dbMode);
        // s += (this.notNullConstraints && column.isNotNull() ? " NOT NULL" : "");
        if (column.getDefaultValue() != null) {
            s += " DEFAULT " + column.getDefaultValue();
        }
        return s;
    }

    /**
     * @changed OLI 15.12.2015 - Added.
     */
    protected String transferAddForeignKeyConstraintToSQLStatement(
            AddForeignKeyConstraint change) {
        if (!this.foreignKeyConstraints) {
            return null;
        }
        String fkName = change.getForeignKeyConstraint().getName();
        String s = null;
        if (change.getColumns().length == 1) {
            MetaDataColumn rc = change.getForeignKeyConstraint().getReferencedColumn();
            MetaDataColumn c = change.getColumns()[0];
            s = "ALTER TABLE " + this.metaUtil.quote(c.getTable()) + " ADD CONSTRAINT "
                    + this.metaUtil.quote(fkName) + " FOREIGN KEY (" + this.metaUtil.quote(c)
                    + ") REFERENCES " + this.metaUtil.quote(rc.getTable()) + " ("
                    + this.metaUtil.quote(rc) + ")";
        }
        return s;
    }

    /**
     * @changed OLI 15.12.2015 - Added.
     */
    protected String transferAddMultipleForeignKeyConstraintToSQLStatement(
            AddMultipleForeignKeyConstraint change) {
        if (!this.foreignKeyConstraints) {
            return null;
        }
        String fkName = change.getComplexForeignKey().getName();
        String s = null;
        MetaDataColumn[] rcs = change.getComplexForeignKey().getTargetColumns();
        MetaDataColumn[] c = change.getComplexForeignKey().getSourceColumns();
        s = "ALTER TABLE " + this.metaUtil.quote(c[0].getTable()) + " ADD CONSTRAINT "
                + this.metaUtil.quote(fkName) + " FOREIGN KEY ("
                + this.getQuotedCommaSeparatedColumnNames(c)
                + ") REFERENCES " + this.metaUtil.quote(rcs[0].getTable()) + " ("
                + this.getQuotedCommaSeparatedColumnNames(rcs) + ")";
        return s;
    }

    private String getQuotedCommaSeparatedColumnNames(MetaDataColumn[] columns) {
        return this.metaUtil.getCommaSeparated(this.metaUtil.getNames(columns), true);
    }

    /**
     * @changed OLI 21.12.2015 - Added.
     */
    protected String transferAddPrimaryKeyConstraintToSQLStatement(
            AddPrimaryKeyConstraint change) {
        MetaDataTable t = change.getTable();
        return "ALTER TABLE " + this.metaUtil.quote(t) + " ADD CONSTRAINT "
                + this.metaUtil.quote(this.nameGenerator.getPrimaryKeyName(t.getName(),
                this.metaUtil.getNames(t.getPrimaryKeyMembers()))) + " PRIMARY KEY ("
                + this.metaUtil.getCommaSeparated(this.metaUtil.getNames(
                t.getPrimaryKeyMembers()), true) + ")";
    }

    /**
     * @changed OLI 18.12.2015 - Added.
     */
    protected String transferAddAdditionalScriptToSQLStatement(AddAdditionalScript change) {
        return change.getScript();
    }

    /**
     * @changed OLI 17.12.2015 - Added.
     */
    protected String transferAddComplexUniqueConstraintToSQLStatement(
            AddComplexUniqueConstraint change) {
        MetaDataUniqueConstraint uc = change.getUniqueConstraint();
        if (uc instanceof MetaDataUniqueWithNullableConstraint) {
            MetaDataUniqueWithNullableConstraint ucn
                    = (MetaDataUniqueWithNullableConstraint) uc;
            return "CREATE UNIQUE INDEX " + this.metaUtil.quote(uc.getName())
                    + " ON " + this.metaUtil.quote(uc.getTable()) + " ("
                    + this.metaUtil.getCommaSeparated(this.metaUtil.getNames(uc.getColumns()),
                    true) + ") WHERE " + this.metaUtil.quote(ucn.getNullable())
                    + " IS" + (uc.getName().endsWith("_nullable") ? "" : " NOT") + " NULL";
        }
        return "ALTER TABLE " + this.metaUtil.quote(uc.getTable()) + " ADD CONSTRAINT "
                + this.metaUtil.quote(uc.getName()) + " UNIQUE ("
                + this.metaUtil.getCommaSeparated(this.metaUtil.getNames(uc.getColumns()), true)
                + ")";
    }

    /**
     * @changed OLI 15.12.2015 - Added.
     */
    protected String transferAlterColumnAddConstraintToSQLStatement(
            AlterColumnAddConstraint change) {
        MetaDataColumn c = change.getColumn();
        MetaDataTable t = change.getTable();
        ColumnConstraintType type = change.getConstraintType();
        String s = null;
        if (type == ColumnConstraintType.NOT_NULL) {
            s = "ALTER TABLE " + this.metaUtil.quote(t) + " ALTER COLUMN "
                    + this.metaUtil.quote(c) + " SET NOT NULL";
        } else if (type == ColumnConstraintType.PRIMARY_KEY) {
            s = "ALTER TABLE " + this.metaUtil.quote(t) + " ADD CONSTRAINT "
                    + this.metaUtil.quote(this.nameGenerator.getPrimaryKeyName(t.getName(),
                    this.metaUtil.getNames(t.getPrimaryKeyMembers()))) + " PRIMARY KEY ("
                    + this.metaUtil.getCommaSeparated(this.metaUtil.getNames(
                    t.getPrimaryKeyMembers()), true) + ")";
        } else if (type == ColumnConstraintType.UNIQUE) {
            s = "ALTER TABLE " + this.metaUtil.quote(t) + " ADD CONSTRAINT "
                    + this.metaUtil.quote(this.nameGenerator.getUniqueName(t.getName(),
                    this.metaUtil.getNames(new MetaDataColumn[] {c}))) + " UNIQUE ("
                    + this.metaUtil.getCommaSeparated(this.metaUtil.getNames(
                    new MetaDataColumn[] {c}), true)+ ")";
        }
        return s;
    }

    /**
     * @changed OLI 15.12.2015 - Added.
     */
    protected String transferAlterColumnDataTypeToSQLStatement(AlterColumnDataType change) {
        MetaDataColumn c = change.getColumn();
        MetaDataTable t = change.getTable();
        String type = c.getSQLType(this.dbMode);
        String s = "ALTER TABLE " + this.metaUtil.quote(t) + " ALTER COLUMN "
                + this.metaUtil.quote(c) + " SET DATA TYPE " + type;
        return s;
    }

    /**
     * @changed OLI 15.12.2015 - Added.
     */
    protected String transferAlterColumnDropConstraintToSQLStatement(
            AlterColumnDropConstraint change) {
        MetaDataColumn c = change.getColumn();
        MetaDataTable t = change.getTable();
        if (change.getConstraintType() == ColumnConstraintType.NOT_NULL) {
            return "ALTER TABLE " + this.metaUtil.quote(t) + " ALTER COLUMN "
                    + this.metaUtil.quote(c) + " DROP NOT NULL";
        } else if (change.getConstraintType() == ColumnConstraintType.PRIMARY_KEY) {
            return "ALTER TABLE " + this.metaUtil.quote(t) + " DROP CONSTRAINT "
                    + this.metaUtil.quote(this.nameGenerator.getPrimaryKeyName(t.getName(),
                    this.metaUtil.getNames(t.getPrimaryKeyMembers())));
        } else if (change.getConstraintType() == ColumnConstraintType.UNIQUE) {
            return "ALTER TABLE " + this.metaUtil.quote(t.getName()) + " DROP CONSTRAINT "
                    + this.metaUtil.quote(this.nameGenerator.getUniqueName(t.getName(),
                    this.metaUtil.getNames(new MetaDataColumn[] {c})));
        }
        return "/* WARNING: CONSTRAINT TYPE NOT PROCESSED FOR TABLE: " + t.getName()
                + ", TYPE="+ change.getConstraintType() + "*/";
    }

    /**
     * @changed OLI 15.12.2015 - Added.
     */
    protected String transferAlterColumnDropDefaultToSQLStatement(AlterColumnDropDefault change)
            {
        MetaDataColumn c = change.getColumn();
        MetaDataTable t = change.getTable();
        String s = "ALTER TABLE " + this.metaUtil.quote(t) + " ALTER COLUMN "
                + this.metaUtil.quote(c) + " DROP DEFAULT";
        return s;
    }

    /**
     * @changed OLI 15.12.2015 - Added.
     */
    protected String transferAlterColumnSetDefaultValueToSQLStatement(
            AlterColumnSetDefaultValue change) {
        MetaDataColumn c = change.getColumn();
        String defaultValue = c.getDefaultValue();
        if (defaultValue == null) {
            return "/* WARNING: DEFAULT SET WITH NULL VALUE IS INVALID FOR FIELD: "
                    + c.getFullName() + " */";
        }
        MetaDataTable t = change.getTable();
        return "ALTER TABLE " + this.metaUtil.quote(t) + " ALTER COLUMN "
                + this.metaUtil.quote(c) + " SET DEFAULT " + this.getDefaultValue(c);
    }

    private String getDefaultValue(MetaDataColumn c) {
        ensure(c != null, "column cannot be null.");
        String defaultValue = c.getDefaultValue();
        if (defaultValue != null) {
            if (this.isStringTypeColumn(c)) {
                if (defaultValue.startsWith("\"")) {
                    defaultValue = defaultValue.substring(1);
                }
                if (defaultValue.endsWith("\"")) {
                    defaultValue = defaultValue.substring(0, defaultValue.length()-1);
                }
                if (!defaultValue.startsWith("'")) {
                    defaultValue = "'" + defaultValue;
                }
                if (!defaultValue.endsWith("'") || (defaultValue.length() == 1)) {
                    defaultValue = defaultValue + "'";
                }
            }
        }
        return defaultValue;
    }

    /**
     * @changed OLI 16.12.2015 - Added.
     */
    protected String transferAlterSequenceToSQLStatement(AlterSequence change) {
        MetaDataSequence s = change.getSequence();
        return "/** WARNING: VALUE OF SEQUENCE CHANGED (ALTE VALUES NOT POSSIBLE): "
                + s.getName() + ", StartValue: " + s.getStartValue() + ", IncrementBy: "
                + s.getIncrementBy() + " */";
    }

    /**
     * @changed OLI 17.12.2015 - Added.
     */
    protected String transferCreateIndexToSQLStatement(CreateIndex change) {
        MetaDataIndex i = change.getIndex();
        return "CREATE INDEX " + this.metaUtil.quote(i) + " ON " + this.metaUtil.quote(
                i.getTable()) + " (" + this.metaUtil.getCommaSeparated(i.getColumnNames(), true)
                + ")";
    }

    /**
     * @changed OLI 16.12.2015 - Added.
     */
    protected String transferCreateSequenceToSQLStatement(CreateSequence change) {
        MetaDataSequence s = change.getSequence();
        return "CREATE SEQUENCE " + this.metaUtil.quote(s) + " INCREMENT BY "
                + s.getIncrementBy() + " START WITH " + s.getStartValue();
    }

    /**
     * @changed OLI 16.12.2015 - Added.
     */
    protected String transferCreateTableToSQLStatement(CreateTable change) {
        MetaDataTable t = change.getTable();
        String s = "CREATE TABLE " + this.metaUtil.quote(t) + " (\n";
        MetaDataColumn[] columns = t.getColumns();
        for (MetaDataColumn c : columns) {
            String type = c.getSQLType(this.dbMode);
            s += "    " + this.metaUtil.quote(c) + " " + type;
            if (c.getDefaultValue() != null) {
                s += " DEFAULT " + this.getDefaultValue(c);
            }
            /*
            if (this.notNullConstraints && (c.isNotNull() || c.isPrimaryKey())) {
                s += " NOT NULL";
            }
            */
            if (c != columns[columns.length-1]) {
                s += ",\n";
            }
        }
        s += ")";
        return s;
    }

    /**
     * @changed OLI 16.12.2015 - Added.
     */
    protected String transferDropColumnToSQLStatement(DropColumn change) {
        MetaDataColumn c = change.getColumn();
        MetaDataTable t = change.getTable();
        return "ALTER TABLE " + this.metaUtil.quote(t) + " DROP COLUMN " + this.metaUtil.quote(c
                );
    }

    /**
     * @changed OLI 17.12.2015 - Added.
     */
    protected String transferDropComplexUniqueConstraintToSQLStatement(
            DropComplexUniqueConstraint change) {
        MetaDataUniqueConstraint uc = change.getUniqueConstraint();
        if (uc instanceof MetaDataUniqueWithNullableConstraint) {
            return "DROP INDEX IF EXISTS " + this.metaUtil.quote(uc.getName());
        }
        return "ALTER TABLE " + this.metaUtil.quote(uc.getTable()) + " DROP CONSTRAINT "
                + this.metaUtil.quote(uc.getName());
    }

    /**
     * @changed OLI 16.12.2015 - Added.
     */
    protected String transferDropForeignKeyConstraintToSQLStatement(
            DropForeignKeyConstraint change) {
        if (!this.foreignKeyConstraints) {
            return null;
        }
        MetaDataColumn c = change.getColumns()[0];
        return "ALTER TABLE " + this.metaUtil.quote(c.getTable()) + " DROP CONSTRAINT "
                + this.metaUtil.quote(change.getForeignKeyConstraint());
    }

    /**
     * @changed OLI 29.09.2017 - Added.
     */
    protected String transferDropMultipleForeignKeyConstraintToSQLStatement(
            DropMultipleForeignKeyConstraint change) {
        if (!this.foreignKeyConstraints) {
            return null;
        }
        MetaDataTable t = change.getComplexForeignKey().getSourceTable();
        return "ALTER TABLE " + this.metaUtil.quote(t) + " DROP CONSTRAINT "
                + this.metaUtil.quote(change.getComplexForeignKey().getName());
    }

    /**
     * @changed OLI 17.12.2015 - Added.
     */
    protected String transferDropIndexToSQLStatement(DropIndex change) {
        MetaDataIndex i = change.getIndex();
        return "DROP INDEX IF EXISTS " + this.metaUtil.quote(i);
    }

    /**
     * @changed OLI 21.12.2015 - Added.
     */
    protected String transferDropPrimaryKeyConstraintToSQLStatement(
            DropPrimaryKeyConstraint change) {
        MetaDataTable t = change.getTable();
        if (t.getPrimaryKeyMembers().length == 0) {
            System.out.println("WARNING: PRIMARY KEY REQUIRED ALTHOUGH NO PRIMARY KEY IS "
                    + "DEFINED FOR TABLE: " + t.getName());
            return "";
        }
        return "ALTER TABLE " + this.metaUtil.quote(t) + " DROP CONSTRAINT "
                + this.metaUtil.quote(this.nameGenerator.getPrimaryKeyName(t.getName(),
                this.metaUtil.getNames(t.getPrimaryKeyMembers())));
    }

    /**
     * @changed OLI 16.12.2015 - Added.
     */
    protected String transferDropSequenceToSQLStatement(DropSequence change) {
        return "DROP SEQUENCE IF EXISTS " + this.metaUtil.quote(change.getSequence());
    }

    /**
     * @changed OLI 16.12.2015 - Added.
     */
    protected String transferDropTableToSQLStatement(DropTable change) {
        return "DROP TABLE " + this.metaUtil.quote(change.getTable()) + " CASCADE";
    }

}