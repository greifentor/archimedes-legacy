/*
 * STFDiagrammParameterReader.java
 *
 * 26.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.reader;

import java.awt.Color;
import java.io.File;

import archimedes.legacy.Archimedes;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.scheme.Option;
import archimedes.legacy.scheme.stf.handler.STFDiagrammParameterHandler;
import corent.dates.DateFormatException;
import corent.dates.PDate;
import corent.files.StructuredTextFile;

/**
 * A reader for diagram parameters from a STF.
 * 
 * @author ollie
 * 
 * @changed OLI 26.04.2013 - Added.
 */

public class STFDiagrammParameterReader extends STFDiagrammParameterHandler {

	/**
	 * Updates the diagram parameters in the passed data model by the
	 * information stored in the STF.
	 * 
	 * @param stf
	 *            The STF whose diagram parameters should be read to the
	 *            diagram.
	 * @param model
	 *            The diagram model which is to fill with the diagram
	 *            parameters.
	 * 
	 * @changed OLI 26.04.2013 - Added.
	 */
	public void read(StructuredTextFile stf, DiagrammModel model) {
		model.setName(fromHTML(stf.readStr(this.createPath(NAME), "<null>")));
		model.setAuthor(fromHTML(stf.readStr(this.createPath(AUTHOR), "<null>")));
		model.setComment(fromHTML(stf.readStr(this.createPath(COMMENT), "<null>")));
		model.setAfterWriteScript(fromHTML(stf.readStr(this.createPath(SCRIPTS, AFTER_WRITE), "<null>")));
		model.setVersion(fromHTML(stf.readStr(this.createPath(VERSION), "0")));
		model.setFontSizeTableContents((int) stf.readLong(this.createPath(FONT_SIZE, FONT_SIZE_TABLE_CONTENTS), 10));
		model.setFontSizeDiagramHeadline((int) stf.readLong(this.createPath(FONT_SIZE, FONT_SIZE_HEADERS), 24));
		model.setFontSizeSubtitles((int) stf.readLong(this.createPath(FONT_SIZE, FONT_SIZE_SUB_TITLES), 10));
		model.setAufgehobeneAusblenden(new Boolean(stf.readStr(this.createPath(HIDE_DEPRECATED), "FALSE"))
				.booleanValue());
		model
				.setPaintTechnicalFieldsInGray(new Boolean(stf.readStr(this.createPath(DISABLE_TECHNICAL_FIELDS),
						"FALSE")).booleanValue());
		model
				.setPaintTransientFieldsInGray(new Boolean(stf.readStr(this.createPath(DISABLE_TRANSIENT_FIELDS),
						"FALSE")).booleanValue());
		model.setMarkUpRequiredFieldNames(new Boolean(stf.readStr(this.createPath(MARK_REQUIRED_FIELDS), "FALSE"))
				.booleanValue());
		model.setCodePfad(fromHTML(stf.readStr(this.createPath(CODE_BASE_PATH), "." + File.separator)));
		model.setCodeFactoryClassName(fromHTML(stf.readStr(this.createPath(CODE_FACTORY_CLASS_NAME), "")));
		model.setApplicationName(fromHTML(stf.readStr(this.createPath(APPLICATION_NAME), "")));
		model.setBasePackageName(fromHTML(stf.readStr(this.createPath(BASE_PACKAGE_NAME), "")));
		model.setUdschebtiBaseClassName(fromHTML(stf.readStr(this.createPath(USCHEBTI_BASE_CLASS_NAME), "")));
		model
				.setAdditionalSQLScriptListener(fromHTML(stf.readStr(this.createPath(ADDITIONAL_SQL_SCRIPT_LISTENER),
						"")));
		model.setDBVersionTableName(fromHTML(stf.readStr(this.createPath(DB_VERSION_TABLE_NAME), "")));
		model.setDBVersionVersionColumnName(fromHTML(stf
				.readStr(this.createPath(DB_VERSION_DB_VERSION_COLUMN_NAME), "")));
		model.setDBVersionDescriptionColumnName(fromHTML(stf.readStr(this
				.createPath(DB_VERSION_DESCRIPTION_COLUMN_NAME), "")));
		model.setHistory(fromHTML(stf.readStr(this.createPath(HISTORY), "")));
		model.setSchemaName(fromHTML(stf.readStr(this.createPath(SCHEMA_NAME), "")));
		String col = fromHTML(stf.readStr(this.createPath(RELATION_COLOR_EXTERNAL_TABLES), fromHTML("lightGray")));
		model.setRelationColorExternalTables(Archimedes.PALETTE.get(col, Color.lightGray));
		model.setOwner(fromHTML(stf.readStr(this.createPath(OWNER), "<null>")));
		col = fromHTML(stf.readStr(this.createPath(RELATION_COLOR_REGULAR), fromHTML("black")));
		model.setRelationColorRegular(Archimedes.PALETTE.get(col, Color.black));
		try {
			model.setDate(PDate.valueOf(fromHTML(stf.readStr(this.createPath(VERSION_DATE), new PDate().toString()))));
		} catch (DateFormatException dfe) {
			dfe.printStackTrace();
		}
		model.setVersionComment(fromHTML(stf.readStr(this.createPath(VERSION_COMMENT), "<null>")));
		long count = stf.readLong(this.createPathForOption(COUNT), 0);
		for (int option = 0; option < count; option++) {
			String name = fromHTML(stf.readStr(this.createPathForOption(OPTION + option, NAME), null));
			String parameter = fromHTML(stf.readStr(this.createPathForOption(OPTION + option, PARAMETER), ""));
			Option o = new Option(name, parameter);
			model.addOption(o);
		}
	}

}