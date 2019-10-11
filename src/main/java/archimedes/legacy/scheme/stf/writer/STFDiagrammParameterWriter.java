/*
 * STFDiagrammParameterWriter.java
 *
 * 26.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.writer;

import org.apache.log4j.Logger;

import archimedes.legacy.model.DiagramSaveMode;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.scheme.stf.handler.STFDiagrammParameterHandler;
import archimedes.model.OptionModel;
import corent.files.StructuredTextFile;

/**
 * A writer for diagram parameters to STF.
 * 
 * @author ollie
 * 
 * @changed OLI 26.04.2013 - Added.
 */

public class STFDiagrammParameterWriter extends STFDiagrammParameterHandler {

	private static final Logger LOG = Logger.getLogger(STFDiagrammParameterWriter.class);

	/**
	 * Writes the passed domains to the STF.
	 * 
	 * @param model
	 *            The model whose parameter are to store in a STF.
	 * @param stf
	 *            The STF which is to update with the diagram parameters data.
	 * 
	 * @changed OLI 26.04.2013 - Added.
	 */
	public void write(StructuredTextFile stf, DiagrammModel model, DiagramSaveMode dsm) {
		stf.writeStr(this.createPath(NAME), toHTML(model.getName()));
		stf.writeStr(this.createPath(AUTHOR), toHTML(model.getAuthor()));
		stf.writeStr(this.createPath(OWNER), toHTML(model.getOwner()));
		if (dsm == DiagramSaveMode.REGULAR) {
			stf.writeStr(this.createPath(COMMENT), toHTML(model.getComment()));
			stf.writeStr(this.createPath(HISTORY), toHTML(model.getHistory()));
			stf.writeStr(this.createPath(SCRIPTS, AFTER_WRITE), toHTML(model.getAfterWriteScript()));
		}
		stf.writeStr(this.createPath(VERSION), toHTML(model.getVersion()));
		stf.writeStr(this.createPath(VERSION_DATE), toHTML(model.getDate().toString()));
		stf.writeStr(this.createPath(VERSION_COMMENT), toHTML(model.getVersionComment()));
		if (dsm == DiagramSaveMode.REGULAR) {
			stf.writeLong(this.createPath(FONT_SIZE, FONT_SIZE_TABLE_CONTENTS), model.getFontSizeTableContents());
			stf.writeLong(this.createPath(FONT_SIZE, FONT_SIZE_HEADERS), model.getFontSizeDiagramHeadline());
			stf.writeLong(this.createPath(FONT_SIZE, FONT_SIZE_SUB_TITLES), model.getFontSizeSubtitles());
			stf.writeStr(this.createPath(SHOW_REFERENCED_COLUMNS), new Boolean(model.isShowReferencedColumns())
					.toString());
			stf.writeStr(this.createPath(MARK_REQUIRED_FIELDS), new Boolean(model.markWriteablemembers()).toString());
			stf.writeStr(this.createPath(HIDE_DEPRECATED), new Boolean(model.isAufgehobeneAusblenden()).toString());
			stf.writeStr(this.createPath(DISABLE_TECHNICAL_FIELDS), new Boolean(model.isPaintTechnicalFieldsInGray())
					.toString());
			stf.writeStr(this.createPath(DISABLE_TRANSIENT_FIELDS), new Boolean(model.isPaintTransientFieldsInGray())
					.toString());
			stf.writeStr(this.createPath(RELATION_COLOR_EXTERNAL_TABLES), toHTML((model
					.getRelationColorToExternalTables() != null ? model.getRelationColorToExternalTables().toString()
					: "null")));
			stf.writeStr(this.createPath(RELATION_COLOR_REGULAR),
					toHTML((model.getRelationColorRegular() != null ? model.getRelationColorRegular().toString()
							: "null")));
		}
		stf.writeStr(this.createPath(CODE_BASE_PATH), toHTML(model.getCodePfad()));
		stf.writeStr(this.createPath(CODE_FACTORY_CLASS_NAME), toHTML(model.getCodeFactoryClassName()));
		stf.writeStr(this.createPath(APPLICATION_NAME), toHTML(model.getApplicationName()));
		stf.writeStr(this.createPath(BASE_PACKAGE_NAME), toHTML(model.getBasePackageName()));
		stf.writeStr(this.createPath(USCHEBTI_BASE_CLASS_NAME), toHTML(model.getUdschebtiBaseClassName()));
		stf.writeStr(this.createPath(ADDITIONAL_SQL_SCRIPT_LISTENER), toHTML(model.getAdditionalSQLScriptListener()));
		stf.writeStr(this.createPath(DB_VERSION_TABLE_NAME), toHTML(model.getDBVersionTablename()));
		stf.writeStr(this.createPath(DB_VERSION_DB_VERSION_COLUMN_NAME), toHTML(model.getDBVersionDBVersionColumn()));
		stf
				.writeStr(this.createPath(DB_VERSION_DESCRIPTION_COLUMN_NAME), toHTML(model
						.getDBVersionDescriptionColumn()));
		stf.writeStr(this.createPath(SCHEMA_NAME), toHTML(model.getSchemaName()));
		stf.writeLong(this.createPathForOption(COUNT), model.getOptionCount());
		for (int j = 0, lenj = model.getOptionCount(); j < lenj; j++) {
			OptionModel option = model.getOptionAt(j);
			stf.writeStr(this.createPathForOption(OPTION + j, NAME), fromHTML(option.getName()));
			stf.writeStr(this.createPathForOption(OPTION + j, PARAMETER),
					fromHTML((option.getParameter() != null ? option.getParameter() : "")));
		}
		LOG.debug("diagram parameters written.");
	}

}