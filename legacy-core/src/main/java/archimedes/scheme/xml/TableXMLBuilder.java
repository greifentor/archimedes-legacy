/*
 * TableXMLBuilder.java
 *
 * 19.02.2016
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.scheme.xml;

import java.awt.Point;
import java.util.List;

import archimedes.model.ColumnModel;
import archimedes.model.NReferenceModel;
import archimedes.model.OrderMemberModel;
import archimedes.model.PanelModel;
import archimedes.model.RelationModel;
import archimedes.model.SelectionMemberModel;
import archimedes.model.StereotypeModel;
import archimedes.model.TableModel;
import archimedes.model.ToStringContainerModel;
import archimedes.model.ViewModel;
import archimedes.model.gui.GUIObjectModel;
import archimedes.model.gui.GUIRelationModel;
import archimedes.model.gui.GUIViewModel;
import corentx.xml.XMLNode;
import corentx.xml.XMLNodeFactory;
import logging.Logger;

/**
 * A builder for a tables node of a XML representation of an Archimedes diagram.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 19.02.2016 - Added.
 */

public class TableXMLBuilder extends AbstractXMLBuilder {

	private static final Logger LOG = Logger.getLogger(TableXMLBuilder.class);

	/**
	 * Creates a new tables XML node for the passed Archimedes diagram.
	 *
	 * @param tables         The tables which are to put to the XML node.
	 * @param xmlNodeFactory A factory for XML node generation.
	 * @return A XML node with the options of the diagram object.
	 *
	 * @changed OLI 19.02.2016 - Added.
	 */
	public XMLNode buildNodes(TableModel[] tables, XMLNodeFactory xmlNodeFactory) {
		XMLNode tablesNode = xmlNodeFactory.createXMLRootNode("Tables");
		for (TableModel t : tables) {
			XMLNode tableNode = xmlNodeFactory.createXMLNode(tablesNode, "Table");
			this.addAttribute(tableNode, "activeInApplication", String.valueOf(t.isActiveInApplication()));
			this.addAttribute(tableNode, "additionalCreateConstraint", t.getAdditionalCreateConstraints());
			this.addAttribute(tableNode, "codeGeneratorOptions", t.getGenerateCodeOptions());
			this.addAttribute(tableNode, "codeFolder", t.getCodeFolder());
			this.addAttribute(tableNode, "colorBackground", String.valueOf(t.getBackgroundColor()));
			this.addAttribute(tableNode, "colorForeground", String.valueOf(t.getFontColor()));
			this.addAttribute(tableNode, "comment", t.getComment());
			this.addAttribute(tableNode, "complexForeignKey", t.getComplexForeignKeyDefinition());
			this.addAttribute(tableNode, "contextName", t.getContextName());
			this.addAttribute(tableNode, "deprecated", String.valueOf(t.isDeprecated()));
			this.addAttribute(tableNode, "draft", String.valueOf(t.isDraft()));
			this.addAttribute(tableNode, "dynamicCode", String.valueOf(t.isDynamicCode()));
			this.addAttribute(tableNode, "externalTable", String.valueOf(t.isExternalTable()));
			this.addAttribute(tableNode, "firstGenerationDone", String.valueOf(t.isFirstGenerationDone()));
			this.addAttribute(tableNode, "generateCode", String.valueOf(t.isGenerateCode()));
			this.addAttribute(tableNode, "history", t.getHistory());
			this.addAttribute(tableNode, "inherited", String.valueOf(t.isInherited()));
			this.addAttribute(tableNode, "name", t.getName());
			this.addAttribute(tableNode, "nmRelation", String.valueOf(t.isNMRelation()));
			this.addAttribute(tableNode, "uniqueFormula", t.getComplexUniqueSpecification());
			for (ColumnModel c : t.getColumns()) {
				XMLNode node = xmlNodeFactory.createXMLNode(tableNode, "Column");
				this.addAttribute(node, "canBeReferenced", String.valueOf(c.canBeReferenced()));
				this.addAttribute(node, "comment", c.getComment());
				this.addAttribute(node, "deprecated", String.valueOf(c.isDeprecated()));
				this.addAttribute(node, "domain", c.getDomain().getName());
				this.addAttribute(node, "editorAlterInBatchView", String.valueOf(c.isEditorAlterInBatchView()));
				this.addAttribute(node, "editorDisabled", String.valueOf(c.isEditorDisabled()));
				this.addAttribute(node, "editorLabelText", c.getEditorLabelText());
				this.addAttribute(node, "editorMaxLength", String.valueOf(c.getEditorMaxLength()));
				this.addAttribute(node, "editorMnemonic", c.getEditorMnemonic());
				this.addAttribute(node, "editorMember", String.valueOf(c.isEditorMember()));
				this.addAttribute(node, "editorPosition", String.valueOf(c.getEditorPosition()));
				this.addAttribute(node, "editorReferenceWeight", String.valueOf(c.getEditorReferenceWeight().name()));
				this.addAttribute(node, "editorResourceId", c.getEditorResourceId());
				this.addAttribute(node, "editorToolTipText", c.getEditorToolTipText());
				this.addAttribute(node, "encrypted", String.valueOf(c.isEncrypted()));
				this.addAttribute(node, "global", String.valueOf(c.isSynchronized()));
				this.addAttribute(node, "globalId", String.valueOf(c.isSynchronizeId()));
				this.addAttribute(node, "hasIndex", String.valueOf(c.hasIndex()));
				this.addAttribute(node, "hideReference", String.valueOf(c.isHideReference()));
				this.addAttribute(node, "history", c.getHistory());
				this.addAttribute(node, "indexSearchMember", String.valueOf(c.isIndexSearchMember()));
				this.addAttribute(node, "individualDefaultValue", c.getIndividualDefaultValue());
				this.addAttribute(node, "lastModificationField", String.valueOf(c.isLastModificationField()));
				this.addAttribute(node, "listItemField", String.valueOf(c.isListItemField()));
				this.addAttribute(node, "name", c.getName());
				this.addAttribute(node, "notNull", String.valueOf(c.isNotNull()));
				int panelNumber = t.getPanelPosition(c.getPanel());
				if (panelNumber < 0) {
					panelNumber = 0;
					LOG.warn("corrected panel number to 0 for " + c.getFullName());
				}
				this.addAttribute(node, "panelNumber", String.valueOf(panelNumber));
				this.addAttribute(node, "parameter", c.getParameters());
				this.addAttribute(node, "primaryKey", String.valueOf(c.isPrimaryKey()));
				if (c.getRelation() != null) {
					XMLNode relation = xmlNodeFactory.createXMLNode(node, "Relation");
					RelationModel r = (RelationModel) c.getRelation();
					this.addAttribute(relation, "referenceColumnName", r.getReferenced().getName());
					this.addAttribute(relation, "referenceTableName", r.getReferenced().getTable().getName());
					for (GUIViewModel gv : ((GUIRelationModel) r).getViews()) {
						XMLNode view = xmlNodeFactory.createXMLNode(relation, "View");
						ViewModel v = (ViewModel) gv;
						this
								.addAttribute(
										view,
										"direction0",
										String
												.valueOf(
														((GUIRelationModel) r)
																.getDirection((GUIViewModel) v, 0)
																.toString()));
						this
								.addAttribute(
										view,
										"direction1",
										String
												.valueOf(
														((GUIRelationModel) r)
																.getDirection((GUIViewModel) v, 1)
																.toString()));
						this.addAttribute(view, "name", v.getName());
						this
								.addAttribute(
										view,
										"offset0",
										String.valueOf(((GUIRelationModel) r).getOffset((GUIViewModel) v, 0)));
						this
								.addAttribute(
										view,
										"offset1",
										String.valueOf(((GUIRelationModel) r).getOffset((GUIViewModel) v, 1)));
						List<Point> pts = ((GUIRelationModel) r).getPointsForView((GUIViewModel) v);
						for (Point p : pts) {
							if ((p != pts.get(0)) && (p != pts.get(pts.size() - 1))) {
								XMLNode point = xmlNodeFactory.createXMLNode(view, "Point");
								this.addAttribute(point, "x", String.valueOf((int) p.getX()));
								this.addAttribute(point, "y", String.valueOf((int) p.getY()));
							}
						}
					}
				}
				this.addAttribute(node, "removedStateField", String.valueOf(c.isRemovedStateField()));
				this.addAttribute(node, "required", String.valueOf(c.isRequired()));
				this
						.addAttribute(
								node,
								"sequenceForKeyGeneration",
								(c.getSequenceForKeyGeneration() != null
										? c.getSequenceForKeyGeneration().getName()
										: ""));
				this
						.addAttribute(
								node,
								"suppressForeignKeyConstraints",
								String.valueOf(c.isSuppressForeignKeyConstraint()));
				this.addAttribute(node, "technicalField", String.valueOf(c.isTechnicalField()));
				this.addAttribute(node, "transient", String.valueOf(c.isTransient()));
				this.addAttribute(node, "unique", String.valueOf(c.isUnique()));
			}
			for (ToStringContainerModel tsc : t.getComboStringMembers()) {
				XMLNode node = xmlNodeFactory.createXMLNode(tableNode, "ComboStringMember");
				this.addAttribute(node, "columnName", tsc.getColumn().getName());
				this.addAttribute(node, "prefix", tsc.getPrefix());
				this.addAttribute(node, "suffix", tsc.getSuffix());
				this.addAttribute(node, "tableName", tsc.getColumn().getTable().getName());
			}
			for (ColumnModel c : t.getCompareMembers()) {
				XMLNode compareToNode = xmlNodeFactory.createXMLNode(tableNode, "CompareToMember");
				this.addAttribute(compareToNode, "columnName", c.getName());
				this.addAttribute(compareToNode, "tableName", c.getTable().getName());
			}
			for (ColumnModel c : t.getEqualsMembers()) {
				XMLNode compareToNode = xmlNodeFactory.createXMLNode(tableNode, "EqualsMember");
				this.addAttribute(compareToNode, "columnName", c.getName());
				this.addAttribute(compareToNode, "tableName", c.getTable().getName());
			}
			for (ColumnModel c : t.getHashCodeMembers()) {
				XMLNode compareToNode = xmlNodeFactory.createXMLNode(tableNode, "HashCodeMember");
				this.addAttribute(compareToNode, "columnName", c.getName());
				this.addAttribute(compareToNode, "tableName", c.getTable().getName());
			}
			for (NReferenceModel nr : t.getNReferences()) {
				XMLNode compareToNode = xmlNodeFactory.createXMLNode(tableNode, "NReference");
				this.addAttribute(compareToNode, "alterable", String.valueOf(nr.isAlterable()));
				this.addAttribute(compareToNode, "columnName", nr.getColumn().getName());
				this
						.addAttribute(
								compareToNode,
								"deleteConfirmationRequired",
								String.valueOf(nr.isDeleteConfirmationRequired()));
				this.addAttribute(compareToNode, "extensible", String.valueOf(nr.isExtensible()));
				this.addAttribute(compareToNode, "id", String.valueOf(nr.getId()));
				this
						.addAttribute(
								compareToNode,
								"nReferencePanelType",
								String.valueOf(nr.getNReferencePanelType().name()));
				int panelNumber = nr.getPanel().getPanelNumber();
				if (panelNumber < 0) {
					panelNumber = 0;
					LOG.warn("corrected panel number to 0 for " + t.getName() + " - NReference -> " + nr);
				}
				this.addAttribute(compareToNode, "panelNumber", String.valueOf(panelNumber));
				this.addAttribute(compareToNode, "permitCreate", String.valueOf(nr.isPermitCreate()));
				this.addAttribute(compareToNode, "tableName", nr.getColumn().getTable().getName());
			}
			tableNode.add(new OptionXMLBuilder().buildNodes(t.getOptions(), xmlNodeFactory));
			/*
			 * for (OptionModel o : t.getOptions()) { XMLNode node = xmlNodeFactory.createXMLNode(tableNode, "Option");
			 * this.addAttribute(node, "name", o.getName()); this.addAttribute(node, "parameter", o.getParameter()); }
			 */
			for (PanelModel p : t.getPanels()) {
				XMLNode panelNode = xmlNodeFactory.createXMLNode(tableNode, "Panel");
				this.addAttribute(panelNode, "panelClass", p.getPanelClass());
				this.addAttribute(panelNode, "panelNumber", String.valueOf(p.getPanelNumber()));
				this.addAttribute(panelNode, "tabMnemonic", p.getTabMnemonic());
				this.addAttribute(panelNode, "tabTitle", p.getTabTitle());
				this.addAttribute(panelNode, "tabToolTipText", p.getTabToolTipText());
			}
			for (SelectionMemberModel smm : t.getSelectableColumns()) {
				XMLNode selectionMemberNode = xmlNodeFactory.createXMLNode(tableNode, "SelectionMember");
				this.addAttribute(selectionMemberNode, "printExpression", String.valueOf(smm.getPrintExpression()));
				this.addAttribute(selectionMemberNode, "selectionAttributeName", String.valueOf(smm.getAttribute()));
				this.addAttribute(selectionMemberNode, "columnName", smm.getColumn().getName());
				this.addAttribute(selectionMemberNode, "tableName", smm.getColumn().getTable().getName());
			}
			for (OrderMemberModel om : t.getSelectionViewOrderMembers()) {
				XMLNode node = xmlNodeFactory.createXMLNode(tableNode, "SelectionOrderMember");
				this.addAttribute(node, "columnName", om.getOrderColumn().getName());
				this.addAttribute(node, "direction", String.valueOf(om.getOrderDirection().name()));
				this.addAttribute(node, "tableName", om.getOrderColumn().getTable().getName());
			}
			for (StereotypeModel stm : t.getStereotypes()) {
				XMLNode node = xmlNodeFactory.createXMLNode(tableNode, "Stereotype");
				this.addAttribute(node, "name", stm.getName());
			}
			for (ToStringContainerModel tsc : t.getToStringMembers()) {
				XMLNode node = xmlNodeFactory.createXMLNode(tableNode, "ToStringMember");
				this.addAttribute(node, "columnName", tsc.getColumn().getName());
				this.addAttribute(node, "prefix", tsc.getPrefix());
				this.addAttribute(node, "suffix", tsc.getSuffix());
				this.addAttribute(node, "tableName", tsc.getColumn().getTable().getName());
			}
			for (ViewModel v : t.getViews()) {
				XMLNode node = xmlNodeFactory.createXMLNode(tableNode, "View");
				this.addAttribute(node, "name", v.getName());
				this.addAttribute(node, "x", String.valueOf(((GUIObjectModel) t).getX((GUIViewModel) v)));
				this.addAttribute(node, "y", String.valueOf(((GUIObjectModel) t).getY((GUIViewModel) v)));
			}
		}
		return tablesNode;
	}

}