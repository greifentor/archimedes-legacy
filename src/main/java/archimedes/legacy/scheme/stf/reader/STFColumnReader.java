/*
 * STFColumnReader.java
 *
 * 30.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.reader;

import archimedes.legacy.Archimedes;
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.PanelModel;
import archimedes.legacy.model.ReferenceWeight;
import archimedes.legacy.model.SequenceModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.scheme.Domain;
import archimedes.legacy.scheme.stf.handler.STFColumnHandler;
import corent.files.StructuredTextFile;

/**
 * A reader for columns from a STF.
 * 
 * @author ollie
 * 
 * @changed OLI 30.04.2013 - Added.
 */

public class STFColumnReader extends STFColumnHandler {

	/**
	 * Updates the columns in the passed data model by the information stored in
	 * the STF.
	 * 
	 * @param stf
	 *            The STF whose columns should be read to the diagram.
	 * @param model
	 *            The diagram model which is to fill with the columns.
	 * 
	 * @changed OLI 30.04.2013 - Added.
	 */
	public void read(StructuredTextFile stf, DiagrammModel model, TableModel table, int i, int j) {
		String name = fromHTML(stf.readStr(this.createPathForColumn(i, j, NAME), null));
		String domainName = fromHTML(stf.readStr(this.createPathForColumn(i, j, DOMAIN), null));
		boolean primaryKey = new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j, PRIMARY_KEY), null)));
		ColumnModel column = Archimedes.Factory.createTabellenspalte(name, (Domain) model.getDomain(domainName),
				primaryKey);
		column.setEditorAlterInBatchView(new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j,
				ALTER_IN_BATCH_VIEW), "FALSE"))).booleanValue());
		column.setCanBeReferenced(new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j, CAN_BE_REFERENCED),
				"FALSE"))).booleanValue());
		column.setComment(fromHTML(stf.readStr(this.createPathForColumn(i, j, COMMENT), "")));
		column.setDeprecated(new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j, DEPRECATED), "FALSE")))
				.booleanValue());
		column.setEditorDisabled(new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j, DISABLED), "FALSE")))
				.booleanValue());
		column.setEditorLabelText(fromHTML(stf.readStr(this.createPathForColumn(i, j, EDITOR_DESCRIPTOR, LABEL_TEXT),
				"")));
		column.setEditorMaxLength((int) stf.readLong(this.createPathForColumn(i, j, EDITOR_DESCRIPTOR, MAX_LENGTH), 0));
		column.setEditorMember(new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j, EDITOR_DESCRIPTOR,
				EDITOR_MEMBER), "FALSE"))).booleanValue());
		column
				.setEditorMnemonic(fromHTML(stf
						.readStr(this.createPathForColumn(i, j, EDITOR_DESCRIPTOR, MNEMONIC), "")));
		column.setEditorPosition((int) stf.readLong(this.createPathForColumn(i, j, EDITOR_DESCRIPTOR, POSITION), 0));
		column.setEditorReferenceWeight(ReferenceWeight.valueOf2(fromHTML(stf.readStr(this.createPathForColumn(i, j,
				EDITOR_DESCRIPTOR, REFERENCE_WEIGHT), "keine"))));
		column.setEditorResourceId(fromHTML(stf.readStr(this.createPathForColumn(i, j, EDITOR_DESCRIPTOR, RESOURCE_ID),
				"")));
		column.setEditorToolTipText(fromHTML(stf.readStr(this.createPathForColumn(i, j, EDITOR_DESCRIPTOR,
				TOOL_TIP_TEXT), "")));
		column.setEncrypted(new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j, ENCRYPTED), "FALSE")))
				.booleanValue());
		column.setHideReference(new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j, HIDE_REFERENCE),
				"FALSE"))).booleanValue());
		column.setHistory(fromHTML(stf.readStr(this.createPathForColumn(i, j, HISTORY), "")));
		column.setIndex(new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j, HAS_INDEX), "FALSE")))
				.booleanValue());
		column.setIndexSearchMember(new Boolean(fromHTML(stf.readStr(this
				.createPathForColumn(i, j, INDEX_SEARCH_MEMBER), "FALSE"))).booleanValue());
		column.setIndividualDefaultValue(fromHTML(stf.readStr(this.createPathForColumn(i, j, INDIVIDUAL_DEFAULT_VALUE),
				"")));
		column.setLastModificationField(new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j,
				LAST_MODIFICATION_FIELD), "FALSE"))).booleanValue());
		column.setListItemField(new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j, LIST_ITEM_FIELD),
				"FALSE"))).booleanValue());
		column.setNotNull(new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j, NOT_NULL), "FALSE")))
				.booleanValue());
		column.setParameters(fromHTML(stf.readStr(this.createPathForColumn(i, j, PARAMETER), "")));
		column.setRemovedStateField(new Boolean(fromHTML(stf.readStr(this
				.createPathForColumn(i, j, REMOVED_STATE_FIELD), "FALSE"))).booleanValue());
		column.setRequired(new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j, CONSISTENCE, REQUIRED),
				"FALSE"))).booleanValue());
		String seq = stf.readStr(this.createPathForColumn(i, j, SEQUENCE_FOR_KEY_GENERATION), "");
		for (SequenceModel sm : model.getSequences()) {
			if (seq.equals(sm.getName())) {
				column.setSequenceForKeyGeneration(sm);
				break;
			}
		}
		column.setSuppressForeignKeyConstraint(new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j,
				SUPPRESS_FOREIGN_KEY_CONSTRAINTS), "FALSE"))).booleanValue());
		column
				.setSynchronized(new Boolean(fromHTML(stf
						.readStr(this.createPathForColumn(i, j, SYNCHRONIZED), "FALSE"))).booleanValue());
		column.setSynchronizeId(new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j, SYNCHRONIZE_ID),
				"FALSE"))).booleanValue());
		column.setTechnicalField(new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j, TECHNICAL_FIELD),
				"FALSE"))).booleanValue());
		column.setTransient(new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j, TRANSIENT), "FALSE")))
				.booleanValue());
		column.setUnique(new Boolean(fromHTML(stf.readStr(this.createPathForColumn(i, j, UNIQUE), "FALSE")))
				.booleanValue());
		int pn = (int) stf.readLong(this.createPathForColumn(i, j, PANEL_NUMBER), 0);
		pn = (pn < 0 ? 0 : pn);
		column.setPanel((PanelModel) table.getPanels()[pn]);
		column.setTable(table);
		table.addColumn(column);
	}

}