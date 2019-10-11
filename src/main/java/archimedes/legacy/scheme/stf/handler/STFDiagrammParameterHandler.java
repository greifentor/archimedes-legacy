/*
 * STFDiagrammParameterHandler.java
 *
 * 26.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.handler;


import archimedes.legacy.scheme.stf.*;


/**
 * A class with the basic data for STF access of the diagram parameters.
 *
 * @author ollie
 *
 * @changed OLI 26.04.2013 - Added.
 */

public class STFDiagrammParameterHandler extends AbstractSTFHandler {

    public static final String ADDITIONAL_SQL_SCRIPT_LISTENER = "AdditionalSQLScriptListener";
    public static final String AFTER_WRITE = "AfterWrite";
    public static final String APPLICATION_NAME = "Applicationname";
    public static final String AUTHOR = "Autor";
    public static final String BASE_PACKAGE_NAME = "Basepackagename";
    public static final String CODE_BASE_PATH = "Codebasispfad";
    public static final String CODE_FACTORY_CLASS_NAME = "CodeFactoryClassName";
    public static final String COMMENT = "Kommentar";
    public static final String COUNT = "Anzahl";
    public static final String DB_VERSION_DB_VERSION_COLUMN_NAME = "DBVersionDBVersionColumn";
    public static final String DB_VERSION_DESCRIPTION_COLUMN_NAME =
            "DBVersionDescriptionColumn";
    public static final String DB_VERSION_TABLE_NAME = "DBVersionTablename";
    public static final String DISABLE_TECHNICAL_FIELDS = "TechnischeFelderAusgrauen";
    public static final String DISABLE_TRANSIENT_FIELDS = "TransienteFelderAusgrauen";
    public static final String FONT_SIZE = "Schriftgroessen";
    public static final String FONT_SIZE_HEADERS  = "Ueberschriften";
    public static final String FONT_SIZE_SUB_TITLES = "Untertitel";
    public static final String FONT_SIZE_TABLE_CONTENTS = "Tabelleninhalte";
    public static final String HIDE_DEPRECATED = "AufgehobeneAusblenden";
    public static final String HISTORY = "History";
    public static final String MARK_REQUIRED_FIELDS = "PflichtfelderMarkieren";
    public static final String NAME = "Name";
    public static final String PARAMETER = "Parameter";
    public static final String OPTION = "Option";
    public static final String OPTIONS = "Optionen";
    public static final String OWNER = "Owner";
    public static final String RELATION_COLOR_EXTERNAL_TABLES = "RelationColorExternalTables";
    public static final String RELATION_COLOR_REGULAR = "RelationColorRegular";
    public static final String SCHEMA_NAME = "SchemaName";
    public static final String SCRIPTS = "Scripte";
    public static final String SHOW_REFERENCED_COLUMNS = "ReferenzierteSpaltenAnzeigen";
    public static final String USCHEBTI_BASE_CLASS_NAME = "UdschebtiBaseClassName";
    public static final String VERSION = "Version";
    public static final String VERSION_DATE = "Versionsdatum";
    public static final String VERSION_COMMENT = "Versionskommentar";

    /**
     * Creates an array with the strings to identify a field of table i.
     *
     * @param additionalIds The additional id's which are following the id header for list
     *         elements.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    public String[] createPathForOption(String... additionalIds) {
        return this.createPath(concat(new String[] {OPTIONS}, additionalIds));
    }

    /**
     * @changed OLI 26.04.2013 - Added.
     */
    @Override public String getSingleListElementTagId() {
        return PARAMETER;
    }

    /**
     * @changed OLI 26.04.2013 - Added.
     */
    @Override public String getWholeBlockTagId() {
        return PARAMETER;
    }

}