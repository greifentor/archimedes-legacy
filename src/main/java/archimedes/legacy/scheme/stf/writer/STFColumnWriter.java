/*
 * STFColumnWriter.java
 *
 * 30.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.writer;

import org.apache.log4j.Logger;

import archimedes.legacy.model.DiagramSaveMode;
import archimedes.legacy.scheme.stf.handler.STFColumnHandler;
import archimedes.model.ColumnModel;
import archimedes.model.SequenceModel;
import archimedes.model.ViewModel;
import corent.files.StructuredTextFile;

/**
 * A writer for table columns to STF.
 * 
 * @author ollie
 * 
 * @changed OLI 30.04.2013 - Added.
 */

public class STFColumnWriter extends STFColumnHandler {

	private static final Logger LOG = Logger.getLogger(STFColumnWriter.class);

	/**
	 * Writes the passed sequences to the STF.
	 * 
	 * @param sequences
	 *            The sequences which are to store in the passed STF.
	 * @param stf
	 *            The STF which is to update with the sequence data.
	 * 
	 * @changed OLI 23.04.2013 - Added.
	 */
	public void write(StructuredTextFile stf, ColumnModel column, int i, int j, ViewModel mvm, DiagramSaveMode dsm) {
		stf.writeStr(this.createPathForColumn(i, j, NAME), toHTML(column.getName()));
		if (dsm == DiagramSaveMode.REGULAR) {
			stf.writeStr(this.createPathForColumn(i, j, COMMENT), toHTML(column.getComment()));
			stf.writeStr(this.createPathForColumn(i, j, HISTORY), toHTML(column.getHistory()));
		}
		stf.writeStr(this.createPathForColumn(i, j, ALTER_IN_BATCH_VIEW),
				new Boolean(column.isEditorAlterInBatchView()).toString());
		stf.writeStr(this.createPathForColumn(i, j, CAN_BE_REFERENCED), new Boolean(column.canBeReferenced())
				.toString());
		stf
				.writeStr(this.createPathForColumn(i, j, CONSISTENCE, REQUIRED), new Boolean(column.isRequired())
						.toString());
		stf.writeStr(this.createPathForColumn(i, j, DEPRECATED), new Boolean(column.isDeprecated()).toString());
		stf.writeStr(this.createPathForColumn(i, j, DISABLED), new Boolean(column.isEditorDisabled()).toString());
		stf.writeStr(this.createPathForColumn(i, j, DOMAIN), toHTML(column.getDomain().getName()));
		stf.writeStr(this.createPathForColumn(i, j, EDITOR_DESCRIPTOR, EDITOR_MEMBER), new Boolean(column
				.isEditorMember()).toString());
		stf
				.writeStr(this.createPathForColumn(i, j, EDITOR_DESCRIPTOR, LABEL_TEXT), toHTML(column
						.getEditorLabelText()));
		stf.writeLong(this.createPathForColumn(i, j, EDITOR_DESCRIPTOR, MAX_LENGTH), column.getEditorMaxLength());
		stf.writeStr(this.createPathForColumn(i, j, EDITOR_DESCRIPTOR, MNEMONIC), toHTML(column.getEditorMnemonic()));
		stf.writeLong(this.createPathForColumn(i, j, EDITOR_DESCRIPTOR, POSITION), column.getEditorPosition());
		stf.writeStr(this.createPathForColumn(i, j, EDITOR_DESCRIPTOR, REFERENCE_WEIGHT), toHTML(column
				.getEditorReferenceWeight().toString()));
		stf.writeStr(this.createPathForColumn(i, j, EDITOR_DESCRIPTOR, RESOURCE_ID), toHTML(column
				.getEditorResourceId()));
		stf.writeStr(this.createPathForColumn(i, j, EDITOR_DESCRIPTOR, TOOL_TIP_TEXT), toHTML(column
				.getEditorToolTipText()));
		stf.writeStr(this.createPathForColumn(i, j, ENCRYPTED), new Boolean(column.isEncrypted()).toString());
		stf.writeStr(this.createPathForColumn(i, j, FOREIGN_KEY), new Boolean(column.getRelation() != null).toString());
		stf.writeStr(this.createPathForColumn(i, j, HAS_INDEX), new Boolean(column.hasIndex()).toString());
		stf
				.writeStr(this.createPathForColumn(i, j, HIDE_REFERENCE), new Boolean(column.isReferenceHidden())
						.toString());
		stf.writeStr(this.createPathForColumn(i, j, INDEX_SEARCH_MEMBER), new Boolean(column.isIndexSearchMember())
				.toString());
		stf.writeStr(this.createPathForColumn(i, j, INDIVIDUAL_DEFAULT_VALUE), toHTML(column
				.getIndividualDefaultValue()));
		stf.writeStr(this.createPathForColumn(i, j, LAST_MODIFICATION_FIELD), new Boolean(column
				.isLastModificationField()).toString());
		stf.writeStr(this.createPathForColumn(i, j, LIST_ITEM_FIELD), new Boolean(column.isListItemField()).toString());
		stf.writeStr(this.createPathForColumn(i, j, NOT_NULL), new Boolean(column.isNotNull()).toString());
		stf.writeLong(this.createPathForColumn(i, j, PANEL_NUMBER), column.getTable().getPanelPosition(
				column.getPanel()));
		stf.writeStr(this.createPathForColumn(i, j, PARAMETER), toHTML(column.getParameters()));
		stf.writeStr(this.createPathForColumn(i, j, PRIMARY_KEY), new Boolean(column.isPrimaryKey()).toString());
		stf.writeStr(this.createPathForColumn(i, j, REMOVED_STATE_FIELD), new Boolean(column.isRemovedStateField())
				.toString());
		SequenceModel sm = column.getSequenceForKeyGeneration();
		stf.writeStr(this.createPathForColumn(i, j, SEQUENCE_FOR_KEY_GENERATION), (sm != null ? sm.getName() : ""));
		stf.writeStr(this.createPathForColumn(i, j, SUPPRESS_FOREIGN_KEY_CONSTRAINTS), new Boolean(column
				.isSuppressForeignKeyConstraint()).toString());
		stf.writeStr(this.createPathForColumn(i, j, SYNCHRONIZED), new Boolean(column.isSynchronized()).toString());
		stf.writeStr(this.createPathForColumn(i, j, SYNCHRONIZE_ID), new Boolean(column.isSynchronizeId()).toString());
		stf
				.writeStr(this.createPathForColumn(i, j, TECHNICAL_FIELD), new Boolean(column.isTechnicalField())
						.toString());
		stf.writeStr(this.createPathForColumn(i, j, TRANSIENT), new Boolean(column.isTransient()).toString());
		stf.writeStr(this.createPathForColumn(i, j, UNIQUE), new Boolean(column.isUnique()).toString());
		if (column.getRelation() != null) {
			new STFRelationWriter().write(stf, column, i, j, mvm, dsm);
		}
		LOG.debug("column written: " + column.getQualifiedName());
	}

}