/*
 * PhantomTable.java
 *
 * 30.10.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.NReferenceModel;
import archimedes.legacy.model.OptionModel;
import archimedes.legacy.model.OrderMemberModel;
import archimedes.legacy.model.PanelModel;
import archimedes.legacy.model.SelectionMemberModel;
import archimedes.legacy.model.StereotypeModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.ToStringContainerModel;
import archimedes.legacy.model.ViewModel;

/**
 * A representation of a phantom table.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 30.10.2013 - Added.
 */

public class PhantomTable implements TableModel {

	private String codeFolder = null;
	private List<ColumnModel> columns = new Vector<ColumnModel>();
	private boolean deprecated = false;
	private DataModel dm = null;
	private String name = null;
	private List<OptionModel> options = new Vector<OptionModel>();

	/**
	 * Creates a new phantom table with the passed parameters.
	 *
	 * @param name The name of the table.
	 *
	 * @changed OLI 30.10.2013 - Added.
	 */
	public PhantomTable(String name, DataModel dm) {
		super();
		this.dm = dm;
		this.name = name;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public void addColumn(ColumnModel c) throws IllegalArgumentException {
		this.columns.add(c);
		c.setTable(this);
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public void addNReference(NReferenceModel arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public void addOption(OptionModel option) {
		if (!this.options.contains(option)) {
			this.options.add(option);
		}
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public String getCodeFolder() {
		return this.codeFolder;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public ColumnModel getColumnByName(String name) {
		for (ColumnModel c : this.columns) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public ColumnModel[] getColumns() {
		return new ColumnModel[0];
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public ToStringContainerModel[] getComboStringMembers() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public String getComplexUniqueSpecification() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public String getContextName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public DataModel getDataModel() {
		return this.dm;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public String getGenerateCodeOptions() {
		// TODO Auto-generated method stub
		return "";
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public NReferenceModel getNReferenceForPanel(PanelModel arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public NReferenceModel[] getNReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public OptionModel getOptionAt(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public int getOptionCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public OptionModel[] getOptions() {
		return this.options.toArray(new OptionModel[0]);
	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Override
	public OptionModel[] getOptionsByName(String name) {
		List<OptionModel> l = new LinkedList<OptionModel>();
		for (OptionModel o : this.options) {
			if (o.getName().equals(name)) {
				l.add(o);
			}
		}
		return l.toArray(new OptionModel[0]);
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public PanelModel getPanelAt(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public PanelModel[] getPanels() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public SelectionMemberModel[] getSelectableColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public StereotypeModel[] getStereotypes() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 11.06.2014 - Added.
	 */
	@Override
	public ToStringContainerModel[] getToStringMembers() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public boolean isDeprecated() {
		return this.deprecated;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public boolean isFirstGenerationDone() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public boolean isGenerateCode() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public boolean isStereotype(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public void removeColumn(ColumnModel arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public void removeOption(OptionModel option) {
		this.options.remove(option);
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public void removeStereotype(StereotypeModel arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public void setCodeFolder(String cf) {
		this.codeFolder = cf;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public void setComplexUniqueSpecification(String arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public void setContextName(String arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public void setFirstGenerationDone(boolean arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public void setGenerateCode(boolean arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 30.10.2013 - Added.
	 */
	@Override
	public void setGenerateCodeOptions(String arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 18.11.2013 - Added.
	 */
	@Override
	public String getAdditionalCreateConstraints() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 18.11.2013 - Added.
	 */
	@Override
	public String getComment() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 18.11.2013 - Added.
	 */
	@Override
	public ColumnModel[] getPrimaryKeyColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 18.11.2013 - Added.
	 */
	@Override
	public boolean isDraft() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @changed OLI 18.11.2013 - Added.
	 */
	@Override
	public void setAdditionalCreateConstraints(String arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 18.11.2013 - Added.
	 */
	@Override
	public void setDataModel(DataModel dm) {
		this.dm = dm;
	}

	/**
	 * @changed OLI 21.11.2014 - Added.
	 */
	@Override
	public boolean isNMRelation() {
		return false;
	}

	/**
	 * @changed OLI 21.11.2014 - Added.
	 */
	@Override
	public void removeNReference(NReferenceModel arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 21.11.2014 - Added.
	 */
	@Override
	public void setNMRelation(boolean arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 08.01.2015 - Added.
	 */
	@Override
	public boolean isExternalTable() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @changed OLI 08.01.2015 - Added.
	 */
	@Override
	public void setExternalTable(boolean arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 12.01.2016 - Added.
	 */
	@Override
	public OrderMemberModel[] getSelectionViewOrderMembers() {
		// TODO Auto-generated method stub
		return new OrderMemberModel[0];
	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public OptionModel getOptionByName(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void addComboStringMember(ToStringContainerModel arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void addCompareMember(ColumnModel arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void addEqualsMember(ColumnModel arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void addHashCodeMember(ColumnModel arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void addSelectableColumn(SelectionMemberModel arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void addSelectionViewOrderMember(OrderMemberModel arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void addToStringMember(ToStringContainerModel arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void clearComboStringMembers() {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void clearCompareToMembers() {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void clearEqualsMembers() {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void clearHashCodeMembers() {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void clearNReferences() {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void clearPanels() {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void clearSelectionViewOrderMembers() {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void clearToStringMembers() {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public ColumnModel[] getCompareMembers() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public ColumnModel[] getEqualsMembers() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public ColumnModel[] getHashCodeMembers() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public boolean isCompareToMember(ColumnModel arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public boolean isEqualsMember(ColumnModel arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public boolean isHashCodeMember(ColumnModel arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void removeComboStringMember(ToStringContainerModel arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void removeCompareToMember(ColumnModel arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void removeEqualsMember(ColumnModel arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void removeHashCodeMember(ColumnModel arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void removeSelectionViewOrderMember(OrderMemberModel arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void removeToStringMember(ToStringContainerModel arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void setDraft(boolean arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 10.03.2016 - Added.
	 */
	@Override
	public void setName(String arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 21.03.2016 - Added.
	 */
	@Override
	public void setComment(String arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 21.03.2016 - Added.
	 */
	@Override
	public void removeSelectableColumn(SelectionMemberModel arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 25.05.2016 - Added.
	 */
	@Override
	public ColumnModel[] getColumnsWithOption(String optionName) {
		List<ColumnModel> l = new LinkedList<ColumnModel>();
		for (ColumnModel c : this.getColumns()) {
			for (OptionModel o : c.getOptions()) {
				if (o.getName().equals(optionName)) {
					l.add(c);
				}
			}
		}
		return l.toArray(new ColumnModel[0]);
	}

	/**
	 * @changed OLI 26.05.2016 - Added.
	 */
	@Override
	public boolean isOptionSet(String optionName) {
		return this.getOptionByName(optionName) != null;
	}

	/**
	 * @changed OLI 16.06.2016 - Added.
	 */
	@Override
	public void addPanel(PanelModel panel) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 16.06.2016 - Added.
	 */
	@Override
	public PanelModel getPanelByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 16.06.2016 - Added.
	 */
	@Override
	public int getPanelCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @changed OLI 16.06.2016 - Added.
	 */
	@Override
	public PanelModel[] getPanelsByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 16.06.2016 - Added.
	 */
	@Override
	public boolean isPanelSet(String panelName) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @changed OLI 16.06.2016 - Added.
	 */
	@Override
	public void removePanel(PanelModel panel) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 16.06.2016 - Added.
	 */
	@Override
	public void addStereotype(StereotypeModel stereotype) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 16.06.2016 - Added.
	 */
	@Override
	public StereotypeModel getStereotypeAt(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 16.06.2016 - Added.
	 */
	@Override
	public StereotypeModel getStereotypeByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 16.06.2016 - Added.
	 */
	@Override
	public int getStereotypeCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @changed OLI 16.06.2016 - Added.
	 */
	@Override
	public StereotypeModel[] getStereotypesByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 16.06.2016 - Added.
	 */
	@Override
	public boolean isStereotypeSet(String stereotypeName) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public List<ViewModel> getViews() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public boolean isActiveInApplication() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public void setActiveInApplication(boolean activeInApplication) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public boolean isDynamicCode() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public void setDynamicCode(boolean dynamicCode) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public Color getBackgroundColor() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public Color getFontColor() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public void setBackgroundColor(Color backgroundColor) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public void setFontColor(Color fontColor) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public String getHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public void setHistory(String newHistory) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public boolean isInherited() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public void setInherited(boolean inherited) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 20.06.2016 - Added.
	 */
	@Override
	public int getPanelPosition(PanelModel panel) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @changed OLI 12.08.2016 - Added.
	 */
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @changed OLI 12.08.2016 - Added.
	 */
	@Override
	public PanelModel getPanelByNumber(int no) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 22.09.2017 - Added.
	 */
	@Override
	public String getComplexForeignKeyDefinition() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 22.09.2017 - Added.
	 */
	@Override
	public void setComplexForeignKeyDefinition(String cfk) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 13.10.2017 - Added.
	 */
	@Override
	public boolean isManyToManyRelation() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @changed OLI 13.10.2017 - Added.
	 */
	@Override
	public void setManyToManyRelation(boolean manyToMany) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 18.10.2017 - Added.
	 */
	@Override
	public void removeFromView(ViewModel view) {
		// TODO Auto-generated method stub

	}

}