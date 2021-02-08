/*
 * ModelXMLReader.java
 *
 * 14.02.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.scheme.xml;

import static corentx.util.Checks.ensure;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import archimedes.connections.ArchimedesImportJDBCDataSourceRecord;
import archimedes.connections.DatabaseConnection;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.IndexMetaData;
import archimedes.model.NReferenceModel;
import archimedes.model.NReferencePanelType;
import archimedes.model.OptionListProvider;
import archimedes.model.OrderMemberModel;
import archimedes.model.PanelModel;
import archimedes.model.ReferenceWeight;
import archimedes.model.SelectionAttribute;
import archimedes.model.SelectionMemberModel;
import archimedes.model.SequenceModel;
import archimedes.model.StereotypeModel;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import archimedes.model.gui.GUIObjectModel;
import archimedes.model.gui.GUIRelationModel;
import archimedes.model.gui.GUIViewModel;
import archimedes.scheme.Option;
import archimedes.scheme.SelectionMember;
import corent.dates.PDate;
import corent.db.DBExecMode;
import corent.db.OrderClauseDirection;
import corent.gui.ExtendedColor;
import corentx.util.Str;
import gengen.metadata.ClassMetaData;

/**
 * This class reads a XML file with Archimedes data model information and creates a new diagram from this information.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 14.02.2016 - Added.
 */

public class ModelXMLReader {

	private ObjectFactory objectFactory = null;

	/**
	 * Creates a new model XML reader with the passed parameters.
	 *
	 * @param objectFactory An object factory for instance generation.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 15.06.2016 - Added.
	 */
	public ModelXMLReader(ObjectFactory objectFactory) {
		super();
		ensure(objectFactory != null, "object factory cannot be null");
		this.objectFactory = objectFactory;
	}

	/**
	 * Reads a XML data model from the file with the passed name.
	 *
	 * @param fileName The name of the XML whose information are to read.
	 * @return The data model which is read from the XML file.
	 *
	 * @changed OLI 14.02.2016 - Added.
	 */
	public DataModel read(String fileName) {
		try {
			File f = new File(fileName);
			if (!f.exists()) {
				throw new FileNotFoundException("model file not found: " + f.getAbsolutePath());
			}
			return this.read(new FileInputStream(f));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.objectFactory.createDataModel();
	}

	/**
	 * Reads a XML data model from the file with the passed name.
	 *
	 * @param inputStream The input stream which provides the XML data.
	 * @return The data model which is read from the input stream.
	 *
	 * @changed OLI 14.02.2016 - Added.
	 */
	public DataModel read(InputStream inputStream) {
		DataModel dataModel = this.objectFactory.createDataModel();
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");
			Document doc = builder.parse(is);
			this.readAdditionalSQLStatements(dataModel, doc.getElementsByTagName("AdditionalSQLStatements"));
			this.readColors((NodeList) xpath.evaluate("/Diagram/Colors/*", doc, XPathConstants.NODESET));
			this.readImportDataSources(dataModel,
					(NodeList) xpath.evaluate("/Diagram/DataSources/Import", doc, XPathConstants.NODESET));
			this.readDatabaseConnections(dataModel,
					(NodeList) xpath.evaluate("/Diagram/DataSources/DatabaseConnection", doc, XPathConstants.NODESET));
			this.readDiagrammParameters(dataModel, doc.getElementsByTagName("Parameters"));
			this.readDomains(dataModel,
					(NodeList) xpath.evaluate("/Diagram/Domains/Domain", doc, XPathConstants.NODESET));
			this.readOptions(dataModel,
					(NodeList) xpath.evaluate("/Diagram/Options/Option", doc, XPathConstants.NODESET));
			this.readSequences(dataModel,
					(NodeList) xpath.evaluate("/Diagram/Sequences/Sequence", doc, XPathConstants.NODESET));
			this.readStereotypes(dataModel,
					(NodeList) xpath.evaluate("/Diagram/Stereotypes/Stereotype", doc, XPathConstants.NODESET));
			this.readViews(dataModel, (NodeList) xpath.evaluate("/Diagram/Views/View", doc, XPathConstants.NODESET));
			this.readTables(dataModel, (NodeList) xpath.evaluate("/Diagram/Tables/Table", doc, XPathConstants.NODESET));
			this.readComplexIndices(dataModel,
					(NodeList) xpath.evaluate("/Diagram/ComplexIndices/Index", doc, XPathConstants.NODESET));
			this.addTablesToViews(dataModel,
					(NodeList) xpath.evaluate("/Diagram/Views/View", doc, XPathConstants.NODESET));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataModel;
	}

	private void readAdditionalSQLStatements(DataModel dataModel, NodeList nodes) {
		Node node = nodes.item(0);
		String s = this.getString(node, "postChanging");
		if (s != null) {
			dataModel.setAdditionalSQLCodePostChangingCode(s);
		}
		s = this.getString(node, "postReducing");
		if (s != null) {
			dataModel.setAdditionalSQLCodePostReducingCode(s);
		}
		s = this.getString(node, "preChanging");
		if (s != null) {
			dataModel.setAdditionalSQLCodePreChangingCode(s);
		}
		s = this.getString(node, "preExtending");
		if (s != null) {
			dataModel.setAdditionalSQLCodePreExtendingCode(s);
		}
	}

	private void readColors(NodeList nodes) {
		for (int i = 0, leni = nodes.getLength(); i < leni; i++) {
			Node node = nodes.item(i);
			String name = this.getString(node, "name");
			int b = this.getInteger(node, "b");
			int g = this.getInteger(node, "g");
			int r = this.getInteger(node, "r");
			this.objectFactory.getPalette().set(name, new ExtendedColor(name, r, g, b));
		}
	}

	private void readDatabaseConnections(DataModel dataModel, NodeList nodes) {
		for (int i = 0, leni = nodes.getLength(); i < leni; i++) {
			Node node = nodes.item(i);
			String name = this.getString(node, "name");
			String dbExecMode = this.getString(node, "dbExecMode");
			String driver = this.getString(node, "driver");
			String quote = this.getString(node, "quote");
			boolean domains = this.getBoolean(node, "setDomains");
			boolean notNull = this.getBoolean(node, "setNotNull");
			boolean references = this.getBoolean(node, "setReferences");
			String url = this.getString(node, "url");
			String userName = this.getString(node, "userName");
			dataModel.addDatabaseConnection(new DatabaseConnection(name, driver, url, userName,
					DBExecMode.valueOf(dbExecMode), domains, notNull, references, quote));
		}
	}

	private void readDiagrammParameters(DataModel dataModel, NodeList nodes) {
		Node node = nodes.item(0);
		dataModel.setAdditionalDiagramInfo(this.getString(node, "additionalDiagramInfo", false));
		dataModel.setAdditionalSQLScriptListener(this.getString(node, "additionalSQLScriptListener"));
		dataModel.setApplicationName(this.getString(node, "applicationName"));
		dataModel.setAuthor(this.getString(node, "author"));
		dataModel.setBasePackageName(this.getString(node, "basePackageName"));
		dataModel.setCodeFactoryClassName(this.getString(node, "codeFactoryClassName"));
		dataModel.setBasicCodePath(this.getString(node, "codeBasePath"));
		dataModel.setComment(this.getString(node, "comment"));
		dataModel.setDBVersionDescriptionColumnName(this.getString(node, "dbVersionColumnDescriptionName"));
		dataModel.setDBVersionTableName(this.getString(node, "dbVersionTableName"));
		dataModel.setDBVersionVersionColumnName(this.getString(node, "dbVersionColumnVersionName"));
		dataModel.setDeprecatedTablesHidden(this.getBoolean(node, "deprecatedTablesHidden"));
		dataModel.setPaintTechnicalFieldsInGray(this.getBoolean(node, "disableTechnicalFields"));
		dataModel.setPaintTransientFieldsInGray(this.getBoolean(node, "disableTransientFields"));
		dataModel.setFontSizeDiagramHeadline(this.getInteger(node, "fontSizeDiagramHeadlines"));
		dataModel.setFontSizeSubtitles(this.getInteger(node, "fontSizeDiagramSubtitles"));
		dataModel.setFontSizeTableContents(this.getInteger(node, "fontSizeTableContents"));
		dataModel.setHistory(this.getString(node, "history"));
		dataModel.setName(this.getString(node, "name"));
		dataModel.setMarkUpRequiredFieldNames(this.getBoolean(node, "markUpRequiredFieldNames"));
		dataModel.setOwner(this.getString(node, "owner", false));
		dataModel.setRelationColorToExternalTables(this.getColor(node, "relationColorExternalTables"));
		dataModel.setRelationColorRegular(this.getColor(node, "relationColorRegular"));
		dataModel.setSchemaName(this.getString(node, "schemeName"));
		dataModel.setAfterWriteScript(this.getString(node, "scriptAfterWrite"));
		dataModel.setShowReferencedColumns(this.getBoolean(node, "showReferencedColumns"));
		dataModel.setUdschebtiBaseClassName(this.getString(node, "uscheptiClassName"));
		dataModel.setVersion(this.getString(node, "version"));
		dataModel.setVersionComment(this.getString(node, "versionComment"));
		dataModel.setVersionDate(this.getPDate(node, "versionDate"));
	}

	private void readImportDataSources(DataModel dataModel, NodeList nodes) {
		for (int i = 0, leni = nodes.getLength(); i < leni; i++) {
			Node node = nodes.item(i);
			String name = this.getString(node, "name");
			String dbName = this.getString(node, "dbName");
			String driver = this.getString(node, "driver");
			String description = this.getString(node, "description");
			boolean domains = this.getBoolean(node, "domains");
			boolean references = this.getBoolean(node, "references");
			String user = this.getString(node, "user");
			ArchimedesImportJDBCDataSourceRecord dsr = new ArchimedesImportJDBCDataSourceRecord(driver, dbName, user,
					"", domains, references);
			dsr.setName(name);
			dsr.setDescription(description);
			dataModel.setImportDataSourceRecord(dsr);
		}
	}

	private void readDomains(DataModel dataModel, NodeList nodes) {
		for (int i = 0, leni = nodes.getLength(); i < leni; i++) {
			Node node = nodes.item(i);
			String name = this.getString(node, "name");
			String comment = this.getString(node, "comment");
			String history = this.getString(node, "history");
			String initialValue = this.getString(node, "initialValue");
			String parameters = this.getString(node, "parameters");
			int dataType = this.getInteger(node, "dataType");
			int decimalPlace = this.getInteger(node, "decimalPlace");
			int length = this.getInteger(node, "length");
			DomainModel dm = this.objectFactory.createDomain(name, dataType, length, decimalPlace);
			dm.setComment(comment);
			dm.setHistory(history);
			dm.setInitialValue(initialValue);
			dm.setParameters(parameters);
			dataModel.addDomain(dm);
		}
	}

	/*
	 * private void readModelOptions(DataModel dataModel, NodeList nodes) { for (int i = 0, leni = nodes.getLength(); i
	 * < leni; i++) { Node node = nodes.item(i); String name = this.getString(node, "name"); String parameter =
	 * this.getString(node, "parameter"); dataModel.addOption(new Option(name, parameter)); } }
	 */

	private void readOptions(OptionListProvider olp, NodeList nodes) {
		for (int i = 0, leni = nodes.getLength(); i < leni; i++) {
			Node node = nodes.item(i);
			if ("Option".equals(node.getNodeName())) {
				String name = this.getString(node, "name");
				String parameter = this.getString(node, "parameter");
				olp.addOption(new Option(name, parameter));
			}
		}
	}

	private void readSequences(DataModel dataModel, NodeList nodes) {
		for (int i = 0, leni = nodes.getLength(); i < leni; i++) {
			Node node = nodes.item(i);
			String name = this.getString(node, "name");
			String comment = this.getString(node, "comment");
			String history = this.getString(node, "history");
			int increment = this.getInteger(node, "increment");
			int startValue = this.getInteger(node, "startValue");
			SequenceModel sm = this.objectFactory.createSequence(name, startValue, increment);
			sm.setComment(comment);
			sm.setHistory(history);
			dataModel.addSequence(sm);
		}
	}

	private void readStereotypes(DataModel dataModel, NodeList nodes) {
		for (int i = 0, leni = nodes.getLength(); i < leni; i++) {
			Node node = nodes.item(i);
			String name = this.getString(node, "name");
			String comment = this.getString(node, "comment");
			String history = this.getString(node, "history");
			boolean doNotPrint = this.getBoolean(node, "doNotPrint");
			boolean hideTable = this.getBoolean(node, "hideTable");
			StereotypeModel stereotype = this.objectFactory.createStereotype(name, comment);
			stereotype.setDoNotPrint(doNotPrint);
			stereotype.setHideTable(hideTable);
			stereotype.setHistory(history);
			dataModel.addStereotype(stereotype);
		}
	}

	private void readViews(DataModel dataModel, NodeList nodes) {
		for (int i = 0, leni = nodes.getLength(); i < leni; i++) {
			Node node = nodes.item(i);
			String name = this.getString(node, "name");
			String description = this.getString(node, "description");
			boolean hideTechnicalFields = this.getBoolean(node, "hideTechnicalFields");
			boolean hideTransientFields = this.getBoolean(node, "hideTransientFields");
			boolean mainView = this.getBoolean(node, "mainView");
			boolean showReferencedColumns = this.getBoolean(node, "showReferencedColumns");
			ViewModel vm = null;
			if (mainView) {
				vm = this.objectFactory.createMainView(name, description, showReferencedColumns);
			} else {
				vm = this.objectFactory.createView();
				vm.setComment(description);
				vm.setName(name);
				vm.setShowReferencedColumns(showReferencedColumns);
			}
			vm.setHideTechnicalFields(hideTechnicalFields);
			vm.setHideTransientFields(hideTransientFields);
			dataModel.addView((GUIViewModel) vm);
		}
	}

	/**
	 * Reads tables from the passed node list and add it to the passed data model.
	 *
	 * @param dataModel The data model which the tables from the nodes are to add to.
	 * @param nodes     The nodes which the tables should be read from.
	 * @return The read and new created tables.
	 *
	 * @changed OLI 08.12.2016 - Added.
	 */
	public TableModel[] readTables(DataModel dataModel, NodeList nodes) {
		List<TableModel> tms = new LinkedList<TableModel>();
		ViewModel vm = (ViewModel) dataModel.getMainView();
		for (int i = 0, leni = nodes.getLength(); i < leni; i++) {
			Node node = nodes.item(i);
			String name = this.getString(node, "name");
			NodeList children = node.getChildNodes();
			TableModel t = this.objectFactory.createTable(dataModel, vm);
			t.setActiveInApplication(this.getBoolean(node, "activeInApplication"));
			t.setAdditionalCreateConstraints(this.getString(node, "additionalCreateConstraint"));
			t.setCodeFolder(this.getString(node, "codeFolder"));
			if (node.getAttributes().getNamedItem("complexForeignKey") != null) {
				t.setComplexForeignKeyDefinition(this.getString(node, "complexForeignKey"));
			}
			t.setContextName(this.getString(node, "contextName"));
			t.setDeprecated(this.getBoolean(node, "deprecated"));
			t.setDraft(this.getBoolean(node, "draft"));
			t.setDynamicCode(this.getBoolean(node, "dynamicCode"));
			t.setExternalTable(this.getBoolean(node, "externalTable"));
			t.setFirstGenerationDone(this.getBoolean(node, "firstGenerationDone"));
			t.setGenerateCode(this.getBoolean(node, "generateCode"));
			t.setGenerateCodeOptions(this.getString(node, "codeGeneratorOptions"));
			t.setName(name);
			t.setBackgroundColor(this.getColor(node, "colorBackground"));
			t.setFontColor(this.getColor(node, "colorForeground"));
			t.setComment(this.getString(node, "comment"));
			t.setHistory(this.getString(node, "history"));
			t.setInherited(this.getBoolean(node, "inherited"));
			t.setNMRelation(this.getBoolean(node, "nmRelation"));
			t.setComplexUniqueSpecification(this.getString(node, "uniqueFormula"));
			for (int j = 0, lenj = children.getLength(); j < lenj; j++) {
				Node child = children.item(j);
				if ("Column".equals(child.getNodeName())) {
					String columnName = this.getString(child, "name");
					String domainName = this.getString(child, "domain");
					ColumnModel c = this.objectFactory.createColumn(columnName, dataModel.getDomainByName(domainName));
					c.setCanBeReferenced(this.getBoolean(child, "canBeReferenced"));
					c.setComment(this.getString(child, "comment"));
					c.setDeprecated(this.getBoolean(child, "deprecated"));
					c.setEditorAlterInBatchView(this.getBoolean(child, "editorAlterInBatchView"));
					c.setEditorDisabled(this.getBoolean(child, "editorDisabled"));
					c.setEditorLabelText(this.getString(child, "editorLabelText"));
					c.setEditorMaxLength(this.getInteger(child, "editorMaxLength"));
					c.setEditorMnemonic(this.getString(child, "editorMnemonic"));
					c.setEditorMember(this.getBoolean(child, "editorMember"));
					c.setEditorPosition(this.getInteger(child, "editorPosition"));
					c.setEditorReferenceWeight(ReferenceWeight.valueOf(this.getString(child, "editorReferenceWeight")));
					c.setEditorResourceId(this.getString(child, "editorResourceId"));
					c.setEditorToolTipText(this.getString(child, "editorToolTipText"));
					c.setEncrypted(this.getBoolean(child, "encrypted"));
					c.setSynchronized(this.getBoolean(child, "global"));
					c.setSynchronizeId(this.getBoolean(child, "globalId"));
					c.setIndex(this.getBoolean(child, "hasIndex"));
					c.setHideReference(this.getBoolean(child, "hideReference"));
					c.setHistory(this.getString(child, "history"));
					c.setIndexSearchMember(this.getBoolean(child, "indexSearchMember"));
					c.setIndividualDefaultValue(this.getString(child, "individualDefaultValue"));
					c.setLastModificationField(this.getBoolean(child, "lastModificationField"));
					c.setListItemField(this.getBoolean(child, "listItemField"));
					c.setPrimaryKey(this.getBoolean(child, "primaryKey"));
					c.setNotNull(this.getBoolean(child, "notNull"));
					c.setParameters(this.getString(child, "parameter"));
					c.setRemovedStateField(this.getBoolean(child, "removedStateField"));
					c.setRequired(this.getBoolean(child, "required"));
					c.setSequenceForKeyGeneration(
							dataModel.getSequenceByName(this.getString(child, "sequenceForKeyGeneration")));
					c.setSuppressForeignKeyConstraint(this.getBoolean(child, "suppressForeignKeyConstraints"));
					c.setTechnicalField(this.getBoolean(child, "technicalField"));
					c.setTransient(this.getBoolean(child, "transient"));
					c.setUnique(this.getBoolean(child, "unique"));
					c.setTable(t);
					t.addColumn(c);
				} else if ("Options".equals(child.getNodeName())) {
					this.readOptions(t, child.getChildNodes());
				} else if ("Panel".equals(child.getNodeName())) {
					PanelModel pm = this.objectFactory.createPanel();
					pm.setPanelClass(this.getString(child, "panelClass"));
					pm.setPanelNumber(this.getInteger(child, "panelNumber"));
					pm.setTabMnemonic(this.getString(child, "tabMnemonic"));
					pm.setTabTitle(this.getString(child, "tabTitle"));
					pm.setTabToolTipText(this.getString(child, "tabToolTipText"));
					t.addPanel(pm);
				} else if ("Stereotype".equals(child.getNodeName())) {
					StereotypeModel st = dataModel.getStereotypeByName(this.getString(child, "name"));
					t.addStereotype(st);
				} else if ("View".equals(child.getNodeName())) {
					String viewName = this.getString(child, "name");
					GUIViewModel v = (GUIViewModel) dataModel.getViewByName(viewName);
					((GUIObjectModel) t).setXY(v, this.getInteger(child, "x"), this.getInteger(child, "y"));
				}
			}
			tms.add(t);
			dataModel.addTable(t);
		}
		for (int i = 0, leni = nodes.getLength(); i < leni; i++) {
			Node node = nodes.item(i);
			NodeList children = node.getChildNodes();
			TableModel table = dataModel.getTableByName(this.getString(node, "name"));
			for (int j = 0, lenj = children.getLength(); j < lenj; j++) {
				Node child = children.item(j);
				if ("Column".equals(child.getNodeName())) {
					String columnName = this.getString(child, "name");
					ColumnModel c = table.getColumnByName(columnName);
					c.setPanel(table.getPanelAt(this.getInteger(child, "panelNumber")));
					NodeList children0 = child.getChildNodes();
					for (int k = 0, lenk = children0.getLength(); k < lenk; k++) {
						Node child0 = children0.item(k);
						if ("Relation".equals(child0.getNodeName())) {
							String rtn = this.getString(child0, "referenceTableName");
							String rcn = this.getString(child0, "referenceColumnName");
							TableModel rt = dataModel.getTableByName(rtn);
							if (rt != null) {
								ColumnModel rc = rt.getColumnByName(rcn);
								NodeList children1 = child0.getChildNodes();
								for (int l = 0, lenl = children1.getLength(); l < lenl; l++) {
									Node child1 = children1.item(l);
									if ("View".equals(child1.getNodeName())) {
										String vn = this.getString(child1, "name");
										corent.base.Direction d0 = corent.base.Direction
												.CreateDirection(this.getString(child1, "direction0"));
										corent.base.Direction d1 = corent.base.Direction
												.CreateDirection(this.getString(child1, "direction1"));
										int o0 = this.getInteger(child1, "offset0");
										int o1 = this.getInteger(child1, "offset1");
										ViewModel v = dataModel.getViewByName(vn);
										if (c.getRelation() == null) {
											c.setRelation(this.objectFactory.createRelation(v, c, d0, o0, rc, d1, o1));
										} else {
											((GUIRelationModel) c.getRelation()).setDirection((GUIViewModel) v, 0, d0);
											((GUIRelationModel) c.getRelation()).setDirection((GUIViewModel) v, 1, d1);
											((GUIRelationModel) c.getRelation()).setOffset((GUIViewModel) v, 0, o0);
											((GUIRelationModel) c.getRelation()).setOffset((GUIViewModel) v, 1, o1);
										}
										NodeList children2 = child1.getChildNodes();
										for (int m = 0, lenm = children2.getLength(); m < lenm; m++) {
											Node child2 = children2.item(m);
											if ("Point".equals(child2.getNodeName())) {
												int x = this.getInteger(child2, "x");
												int y = this.getInteger(child2, "y");
												c.getRelation().addPoint(v, x, y);
											}
										}
									}
								}
							}
							table.removeColumn(c);
							table.addColumn(c);
						}
					}
				} else if ("ComboStringMember".equals(child.getNodeName())) {
					String columnName = this.getString(child, "columnName");
					String tableName = this.getString(child, "tableName");
					TableModel t = dataModel.getTableByName(tableName);
					ColumnModel c = t.getColumnByName(columnName);
					String prefix = this.getString(child, "prefix");
					String suffix = this.getString(child, "suffix");
					table.addComboStringMember(this.objectFactory.createToStringContainer(c, prefix, suffix));
				} else if ("CompareToMember".equals(child.getNodeName())) {
					String columnName = this.getString(child, "columnName");
					String tableName = this.getString(child, "tableName");
					TableModel t = dataModel.getTableByName(tableName);
					ColumnModel c = t.getColumnByName(columnName);
					table.addCompareMember(c);
				} else if ("EqualsMember".equals(child.getNodeName())) {
					String columnName = this.getString(child, "columnName");
					String tableName = this.getString(child, "tableName");
					TableModel t = dataModel.getTableByName(tableName);
					ColumnModel c = t.getColumnByName(columnName);
					table.addEqualsMember(c);
				} else if ("HashCodeMember".equals(child.getNodeName())) {
					String columnName = this.getString(child, "columnName");
					String tableName = this.getString(child, "tableName");
					TableModel t = dataModel.getTableByName(tableName);
					ColumnModel c = t.getColumnByName(columnName);
					table.addHashCodeMember(c);
				} else if ("NReference".equals(child.getNodeName())) {
					String columnName = this.getString(child, "columnName");
					String tableName = this.getString(child, "tableName");
					TableModel t = dataModel.getTableByName(tableName);
					ColumnModel c = t.getColumnByName(columnName);
					NReferenceModel nr = this.objectFactory.createNReference(table);
					nr.setAlterable(this.getBoolean(child, "alterable"));
					nr.setColumn(c);
					nr.setDeleteConfirmationRequired(this.getBoolean(child, "deleteConfirmationRequired"));
					nr.setExtensible(this.getBoolean(child, "extensible"));
					nr.setId(this.getInteger(child, "id"));
					nr.setNReferencePanelType(
							NReferencePanelType.valueOf(this.getString(child, "nReferencePanelType")));
					nr.setPanel(table.getPanelByNumber(this.getInteger(child, "panelNumber")));
					nr.setPermitCreate(this.getBoolean(child, "permitCreate"));
					table.addNReference(nr);
				} else if ("SelectionOrderMember".equals(child.getNodeName())) {
					String columnName = this.getString(child, "columnName");
					String tableName = this.getString(child, "tableName");
					TableModel t = dataModel.getTableByName(tableName);
					ColumnModel c = t.getColumnByName(columnName);
					OrderMemberModel om = this.objectFactory.createOrderMember(c);
					om.setOrderDirection(OrderClauseDirection.valueOf(this.getString(child, "direction")));
					table.addSelectionViewOrderMember(om);
				} else if ("SelectionMember".equals(child.getNodeName())) {
					String columnName = this.getString(child, "columnName");
					String tableName = this.getString(child, "tableName");
					String printExpression = this.getString(child, "printExpression");
					TableModel t = dataModel.getTableByName(tableName);
					SelectionAttribute selectionAttribute = SelectionAttribute
							.valueOf(this.getString(child, "selectionAttributeName"));
					ColumnModel c = t.getColumnByName(columnName);
					SelectionMemberModel smm = new SelectionMember(c, selectionAttribute,
							(printExpression == null ? "" : printExpression));
					table.addSelectableColumn(smm);
				} else if ("ToStringMember".equals(child.getNodeName())) {
					String columnName = this.getString(child, "columnName");
					String tableName = this.getString(child, "tableName");
					TableModel t = dataModel.getTableByName(tableName);
					ColumnModel c = t.getColumnByName(columnName);
					String prefix = this.getString(child, "prefix");
					String suffix = this.getString(child, "suffix");
					table.addToStringMember(this.objectFactory.createToStringContainer(c, prefix, suffix));
				}
			}
		}
		return tms.toArray(new TableModel[0]);
	}

	private void readComplexIndices(DataModel dataModel, NodeList nodes) {
		for (int i = 0, leni = nodes.getLength(); i < leni; i++) {
			Node node = nodes.item(i);
			String name = this.getString(node, "name");
			String tableName = this.getString(node, "tableName");
			ClassMetaData cmd = (ClassMetaData) dataModel.getTableByName(tableName);
			IndexMetaData imd = this.objectFactory.createIndexMetaData(name, cmd);
			NodeList children = node.getChildNodes();
			for (int j = 0, lenj = children.getLength(); j < lenj; j++) {
				Node child = children.item(j);
				if ("Column".equals(child.getNodeName())) {
					String columnName = this.getString(child, "name");
					imd.addColumn(cmd.getAttribute(columnName));
				}
			}
			dataModel.addComplexIndex(imd);
		}
	}

	private void addTablesToViews(DataModel dataModel, NodeList nodes) {
		for (int i = 0, leni = nodes.getLength(); i < leni; i++) {
			Node node = nodes.item(i);
			String name = this.getString(node, "name");
			ViewModel vm = (ViewModel) dataModel.getViewByName(name);
			NodeList children = node.getChildNodes();
			for (int j = 0, lenj = children.getLength(); j < lenj; j++) {
				Node child = children.item(j);
				if ("Table".equals(child.getNodeName())) {
					String tableName = this.getString(child, "name");
					vm.addTable(dataModel.getTableByName(tableName));
				}
			}
		}
	}

	private boolean getBoolean(Node node, String name) {
		String s = this.getString(node, name);
		return "true".equalsIgnoreCase(s);
	}

	private Color getColor(Node node, String name) {
		String s = this.getString(node, name);
		return this.objectFactory.getColor(s, Color.lightGray);
	}

	private int getInteger(Node node, String name) {
		String s = this.getString(node, name);
		try {
			return Integer.valueOf(s);
		} catch (NumberFormatException e) {
			throw new NumberFormatException(
					"attribute value of '" + this.getPath(node) + "' " + "is not a number: " + s);
		}
	}

	private String getPath(Node node) {
		String n = this.getString(node, "name");
		String s = "";
		while ((node != null) && !node.getNodeName().startsWith("#document")) {
			s = "/" + node.getNodeName() + s;
			node = node.getParentNode();
		}
		return s + (n != null ? ":" + n : "");
	}

	private PDate getPDate(Node node, String name) {
		String s = this.getString(node, name);
		return PDate.valueOf(s);
	}

	private String getString(Node node, String name) {
		return this.getString(node, name, true);
	}

	private String getString(Node node, String name, boolean emptyToNull) {
		if ((node.getAttributes() != null) && (node.getAttributes().getNamedItem(name) != null)) {
			String s = node.getAttributes().getNamedItem(name).getNodeValue();
			s = s.replace("$BR$", "\n");
			s = Str.fromHTML(s);
			return s;
		}
		return (emptyToNull ? null : "");
	}

}