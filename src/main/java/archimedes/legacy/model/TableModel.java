/*
 * TableModel.java
 *
 * 25.04.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;


/**
 * An interface which describes the methods of a table of the data model.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 25.04.2013 - Added.
 */

public interface TableModel extends CommentOwner, Comparable, Deprecateable,
        GenericApplicationSupport, HistoryOwner, NamedObject, OptionListProvider,
        PanelListProvider, StereotypeListProvider, TableGeneratorSupport, TableGUISupport {

    /**
     * Adds the passed column model to the table model.
     *
     * @param column The new column to add to the table model.
     * @throws IllegalArgumentException Passing a null pointer as column.
     *
     * @changed OLI 25.04.2013 - Added.
     */
    abstract public void addColumn(ColumnModel column) throws IllegalArgumentException;

    /**
     * Adds the passed to string container to the combo string members to the table model. 
     *
     * @param tsc The to string container which is to add to the combo string members table
     *         model.
     *
     * @changed OLI 01.03.2016 - Added.
     */
    abstract public void addComboStringMember(ToStringContainerModel tsc);

    /**
     * Adds the passed column model to the compareTo members of the table model.
     *
     * @param column The new column to add to the compareTo members of the table model.
     * @throws IllegalArgumentException Passing a null pointer as column.
     *
     * @changed OLI 26.02.2016 - Added.
     */
    abstract public void addCompareMember(ColumnModel column) throws IllegalArgumentException;

    /**
     * Adds the passed column model to the equals members of the table model.
     *
     * @param column The new column to add to the equals members of the table model.
     * @throws IllegalArgumentException Passing a null pointer as column.
     *
     * @changed OLI 29.02.2016 - Added.
     */
    abstract public void addEqualsMember(ColumnModel column) throws IllegalArgumentException;

    /**
     * Adds the passed column model to the hash code members of the table model.
     *
     * @param column The new column to add to the hash code members of the table model.
     * @throws IllegalArgumentException Passing a null pointer as column.
     *
     * @changed OLI 29.02.2016 - Added.
     */
    abstract public void addHashCodeMember(ColumnModel column) throws IllegalArgumentException;

    /**
     * Adds the passed nReference to the table model. 
     *
     * @param nrm The nReference which is to add to the table model.
     */
    abstract public void addNReference(NReferenceModel nrm);

    /**
     * Adds the passed selection member to the table model. 
     *
     * @param ssm The selection member which is to add to the table model.
     */
    abstract public void addSelectableColumn(SelectionMemberModel ssm);

    /**
     * Adds the passed order member to the table model. 
     *
     * @param om The order member which is to add to the table model.
     *
     * @changed OLI 01.03.2016 - Added.
     */
    abstract public void addSelectionViewOrderMember(OrderMemberModel om);

    /**
     * Adds the passed to string container to the to string members to the table model. 
     *
     * @param tsc The to string container which is to add to the to string members table model.
     *
     * @changed OLI 01.03.2016 - Added.
     */
    abstract public void addToStringMember(ToStringContainerModel tsc);

    /**
     * Removes all combo string members from the table.
     *
     * @changed OLI 01.03.2016 - Added.
     */
    abstract public void clearComboStringMembers();

    /**
     * Removes all compare members from the table.
     *
     * @changed OLI 26.02.2016 - Added.
     */
    abstract public void clearCompareToMembers();

    /**
     * Removes all equals members from the table.
     *
     * @changed OLI 29.02.2016 - Added.
     */
    abstract public void clearEqualsMembers();

    /**
     * Removes all hash code members from the table.
     *
     * @changed OLI 29.02.2016 - Added.
     */
    abstract public void clearHashCodeMembers();

    /**
     * Removes all n-references from the table.
     *
     * @changed OLI 01.03.2016 - Added.
     */
    abstract public void clearNReferences();

    /**
     * Removes all panels from the table.
     *
     * @changed OLI 03.05.2013 - Added.
     */
    abstract public void clearPanels();

    /**
     * Removes all order members from the table.
     *
     * @changed OLI 01.03.2016 - Added.
     */
    abstract public void clearSelectionViewOrderMembers();

    /**
     * Removes all to string members from the table.
     *
     * @changed OLI 01.03.2016 - Added.
     */
    abstract public void clearToStringMembers();

    /**
     * Returns the additional constraints for the tables create statement.
     *
     * @return The additional constraints for the tables create statement.
     *
     * @changed OLI 08.11.2013 - Added.
     */
    abstract public String getAdditionalCreateConstraints();

    /**
     * Returns the code folder of the table for using in code factories.
     *
     * @return The code folder of the table for using in code factories.
     *
     * @changed OLI 15.10.2013 - Added.
     */
    abstract public String getCodeFolder();

    /**
     * Returns the column model with the passed name or <CODE>null</CODE> if there is no column
     * with the passed name.
     *
     * @param name The name of the column which is to return.
     * @return The column with the passed name or <CODE>null</CODE> if there is no column with
     *         the passed name.
     *
     * @changed OLI 25.04.2013 - Added.
     */
    abstract public ColumnModel getColumnByName(String name);

    /**
     * Returns an array with all columns of the table.
     *
     * @return An array with all columns of the table.
     *
     * @changed OLI 25.04.2013 - Added.
     */
    abstract public ColumnModel[] getColumns();

    /**
     * Returns an array with all columns of the table which owns the option with the passed
     * name.
     *
     * @param optionName The name of the option which must by owned by the returned columns.
     * @return An array with all columns of the table which owns the option with the passed
     *         name.
     *
     * @changed OLI 25.05.2016 - Added.
     */
    abstract public ColumnModel[] getColumnsWithOption(String optionName);

    /**
     * Returns a list of attributes which should be a part of the string which is shown in combo
     * boxes.
     *
     * @return A list of attributes which should be a part of the string which is shown in combo
     *         boxes.
     *
     * @changed OLI 15.10.2013 - Added.
     */
    abstract public ToStringContainerModel[] getComboStringMembers();

    /**
     * Returns the comment for the table.
     *
     * @return The comment for the table.
     *
     * @changed OLI 24.10.2013 - Added.
     * /
    abstract public String getComment();

    /**
     * Returns an array of the attributes which are involved to the compareTo method.
     *
     * @return An array of the attributes which are involved to the compareTo method.
     *
     * @changed OLI 26.02.2016 - Added (from "TabellenModel").
     */
    public ColumnModel[] getCompareMembers();

    /**
     * Returns a complex foreign key definition for the table model. 
     *
     * @return The complex foreign key definition for the table model.
     *
     * @changed OLI 22.09.2017 - Added.
     */
    abstract public String getComplexForeignKeyDefinition();

    /**
     * Returns the complex unique specification if the table has one.
     *
     * @return The complex unique specification if the table has one. Otherwise an empty string
     *         will be returned.
     *
     * @changed OLI 05.08.2013 - Added.
     */
    abstract public String getComplexUniqueSpecification();

    /**
     * Returns a context name for the table.
     *
     * @return A context name for the table (e. g. for code generation affairs).
     *
     * @changed OLI 20.08.2013 - Added.
     */
    abstract public String getContextName();

    /**
     * Returns the data model which the table belongs to.
     *
     * @return The data model which the table belongs to.
     *
     * @changed OLI 03.05.2013 - Added.
     */
    abstract public DataModel getDataModel();

    /**
     * Returns an array of the attributes which are involved to the equals method.
     *
     * @return An array of the attributes which are involved to the equals method.
     *
     * @changed OLI 29.02.2016 - Added (from "TabellenModel").
     */
    public ColumnModel[] getEqualsMembers();

    /**
     * Returns the options for the code generator provided by the table.
     *
     * @return The options for the code generator provided by the table.
     *
     * @changed OLI 23.08.2013 - Added.
     */
    abstract public String getGenerateCodeOptions();

    /**
     * Returns an array of the attributes which are involved to the hash code method.
     *
     * @return An array of the attributes which are involved to the hash code method.
     *
     * @changed OLI 29.02.2016 - Added (from "TabellenModel").
     */
    public ColumnModel[] getHashCodeMembers();

    /**
     * Returns the name of the table.
     *
     * @return The name of the table.
     *
     * @changed OLI 25.04.2013 - Added.
     * /
    @Override abstract public String getName();

    /**
     * Returns a NReferenceModel which references to the passed PanelModel if there is one.
     *
     * @param pm The PanelModel whose NReferenceModel is to find.
     * @return A NReferenceModel which references to the passed PanelModel if there is one.
     *         Otherwise <CODE>null</CODE> will be returned.
     *
     * @changed OLI 30.09.2013 - Added.
     */
    abstract public NReferenceModel getNReferenceForPanel(PanelModel pm);

    /**
     * Returns an array with all nReferences from the table.
     *
     * @return An array with the nReference models of the table.
     *
     * @changed OLI 06.05.2013 - Added.
     */
    abstract public NReferenceModel[] getNReferences();

    /**
     * Returns a reference to the panel with the passed index.
     *
     * @param i The index whose panel should be returned.
     * @return A reference to the panel with the passed index.
     * /
    @Override abstract public PanelModel getPanelAt(int i);

    /**
     * Returns a reference to the panel with the passed number.
     *
     * @param no The number of the panel.
     * @return A reference to the panel with the passed number or <CODE>null</CODE> if no panel
     *         is existing with the passed panel number.
     *
     * @changed OLI 12.08.2016 - Added.
     */
    abstract public PanelModel getPanelByNumber(int no);

    /**
     * Returns the primary key columns of the table.
     *
     * @return The primary key columns of the table if there are some, an empty array otherwise.
     *
     * @changed OLI 28.10.2013 - Added.
     */
    abstract public ColumnModel[] getPrimaryKeyColumns();

    /**
     * Returns an array with the columns which are marked as selectable (e g. in a selection
     * view).
     *
     * @return An array with the columns which are marked as selectable (e g. in a selection
     *         view).
     *
     * @changed OLI 09.08.2013 - Added.
     */
    abstract public SelectionMemberModel[] getSelectableColumns();

    /**
     * Returns the order information for the selection view of the table.
     *
     * @return The order information for the selection view of the table.
     *
     * @changed OLI 12.01.2016 - Added.
     */
    abstract public OrderMemberModel[] getSelectionViewOrderMembers();

    /**
     * Returns an array with the stereotypes which the table belongs to.
     *
     * @return An array with the stereotypes which the table belongs to.
     *
     * @changed OLI 03.05.2013 - Added.
     * /
    abstract public StereotypeModel[] getStereotypes();

    /**
     * Returns a list of attributes which should be a part of the string which is shown in
     * string representations.
     *
     * @return A list of attributes which should be a part of the string which is shown in
     *         string representations.
     *
     * @changed OLI 11.06.2014 - Added.
     */
    abstract public ToStringContainerModel[] getToStringMembers();

    /**
     * Returns the list of views which the table is linked to.
     *
     * @return The list of views which the table is linked to. 
     */
    public java.util.List<ViewModel> getViews();

    /**
     * Checks if the table has the passed column in the compare to members.
     *
     * @param c The column which is to check for compare to  member.
     * @return <CODE>true</CODE> if the passed column is in the compare to  members of the
     *         table.
     *
     * @changed OLI 29.02.2016 - Added.
     */
    abstract public boolean isCompareToMember(ColumnModel c);

    /**
     * Checks if the table is deprecated.
     *
     * @return <CODE>true</CODE> if the table is deprecated.
     *
     * @changed OLI 25.04.2013 - Added.
     * /
    abstract public boolean isDeprecated();

    /**
     * Checks if the table is a draft which should not be used in e. g. code or update script
     * generation.
     *
     * @return <CODE>true</CODE> if the table is only a draft.
     *
     * @changed OLI 24.10.2013 - Added.
     */
    abstract public boolean isDraft();

    /**
     * Checks if the table has the passed column in the equals members.
     *
     * @param c The column which is to check for equals member.
     * @return <CODE>true</CODE> if the passed column is in the equals members of the table.
     *
     * @changed OLI 29.02.2016 - Added.
     */
    abstract public boolean isEqualsMember(ColumnModel c);

    /**
     * Checks if the table is an external table from another model. Those tables will be ignored
     * for SQL script and code generation but could be used to make those references visible and
     * generate code for references to those tables.
     *
     * @return <CODE>true</CODE> if the table is an external one, <CODE>false</CODE> otherwise.
     *
     * @changed OLI 07.01.2015 - Added.
     */
    abstract public boolean isExternalTable();

    /**
     * Checks if a first generator run has been done for the table. Using this flag it is
     * possible keep out parts of the generation process.
     *
     * @return <CODE>true</CODE> if the first generation has been done for the table.
     *
     * @changed OLI 23.08.2013 - Added (from <CODE>TabellenModel</CODE> interface.
     */
    abstract public boolean isFirstGenerationDone();

    /**
     * Checks if the generate flag is set for the table.
     *
     * @return <CODE>true</CODE> if the generate flag is set for the table.
     *
     * @changed OLI 23.08.2013 - Added.
     */
    abstract public boolean isGenerateCode();

    /**
     * Checks if the table has the passed column in the hash code members.
     *
     * @param c The column which is to check for hash code member.
     * @return <CODE>true</CODE> if the passed column is in the hash code members of the table.
     *
     * @changed OLI 29.02.2016 - Added.
     */
    abstract public boolean isHashCodeMember(ColumnModel c);

    /** 
     * Checks if the table a many-to-many relation. 
     *
     * @return <CODE>true</CODE> if the table a many-to-many relation, <CODE>false</CODE>
     *         otherwise.
     *
     * @deprecated OLI 13.10.2017 - Use "isManyToManyRelation" instead of.
     */
    @Deprecated abstract public boolean isNMRelation();

    /** 
     * Checks if the table a many-to-many relation. 
     *
     * @return <CODE>true</CODE> if the table a many-to-many relation, <CODE>false</CODE>
     *         otherwise.
     *
     * @changed OLI 13.10.2017 - Added.
     */
    abstract public boolean isManyToManyRelation();

    /**
     * Checks if the table is of the stereotype with the passed name.
     *
     * @param name The name of the stereotype which is to check.
     * @return <CODE>true</CODE> if the table is of the passed stereotype.
     *
     * @changed OLI 03.05.2013 - Approved.
     */
    abstract public boolean isStereotype(String name);

    /**
     * Removes the passed column model from the table model.
     *
     * @param column The column which is to remove from the table model.
     * @throws IllegalArgumentException Passing a null pointer as column.
     *
     * @changed OLI 29.04.2013 - Added.
     */
    abstract public void removeColumn(ColumnModel column) throws IllegalArgumentException;

    /**
     * Removes the passed combo string member from the list of the combo string members of the
     * table. 
     *
     * @param tsc The combo string member which is to remove from the list of the combo string
     *         members which belongs to the table.
     *
     * @changed OLI 01.03.2016 - Added.
     */
    abstract public void removeComboStringMember(ToStringContainerModel tsc);

    /**
     * Removes the passed column model from the compare members of the table model.
     *
     * @param column The column which is to remove from the compare members of the table model.
     * @throws IllegalArgumentException Passing a null pointer as column.
     *
     * @changed OLI 26.02.2016 - Added.
     */
    abstract public void removeCompareToMember(ColumnModel column)
            throws IllegalArgumentException;

    /**
     * Removes the passed column model from the equals members of the table model.
     *
     * @param column The column which is to remove from the equals members of the table model.
     * @throws IllegalArgumentException Passing a null pointer as column.
     *
     * @changed OLI 29.02.2016 - Added.
     */
    abstract public void removeEqualsMember(ColumnModel column) throws IllegalArgumentException;

    /**
     * Removes the table from the passed view.
     *
     * @param view The view which the table is to remove from.
     *
     * @changed OLI 18.10.2017 - Added (from "TabellenModel").
     */
    abstract public void removeFromView(ViewModel view);
    
    /**
     * Removes the passed column model from the hash code members of the table model.
     *
     * @param column The column which is to remove from the hash code members of the table
     *         model.
     * @throws IllegalArgumentException Passing a null pointer as column.
     *
     * @changed OLI 29.02.2016 - Added.
     */
    abstract public void removeHashCodeMember(ColumnModel column)
            throws IllegalArgumentException;

    /**
     * Removes the passed <CODE>NReferenceModel</CODE> form the list of models in the table
     * model.
     *
     * @param nrm The n-references model which is to remove.
     */
    abstract public void removeNReference(NReferenceModel nrm);

    /**
     * Removes the passed selection member from the list of selection members of the table.
     *
     * @param ssm The selection member which is to remove from the table model.
     *
     * @changed OLI 10.03.2016 - Added.
     */
    abstract public void removeSelectableColumn(SelectionMemberModel ssm);

    /**
     * Removes the passed selection view order member from the list of the selection view order
     * members of the table. 
     *
     * @param selectionViewOrderMember The selection view order member which is to remove from
     *         the list of the selection view order members which the table belongs to.
     *
     * @changed OLI 01.03.2016 - Added.
     */
    abstract public void removeSelectionViewOrderMember(
            OrderMemberModel selectionViewOrderMember);

    /**
     * Removes the passed stereotype from the list of the stereotypes of the table. 
     *
     * @param stereotype The stereotype which is to remove from the list of the stereotypes
     *         which the table belongs to.
     *
     * @changed OLI 03.05.2013 - Approved.
     * /
    abstract public void removeStereotype(StereotypeModel stereotype);

    /**
     * Removes the passed to string member from the list of the to string members of the table. 
     *
     * @param tsc The to string member which is to remove from the list of the to string members
     *         which belongs to the table.
     *
     * @changed OLI 01.03.2016 - Added.
     */
    abstract public void removeToStringMember(ToStringContainerModel tsc);

    /**
     * Sets the passed string as new additional constraints for the tables create statement.
     *
     * @param constraints The new additional constraints code for the tables create statement.
     *
     * @changed OLI 08.11.2013 - Added.
     */
    abstract public void setAdditionalCreateConstraints(String constraints);

    /**
     * Sets a new value for code folder of the table .
     *
     * @param codeFolder The new value for the code folder.
     *
     * @changed OLI 15.10.2013 - Added.
     */
    abstract public void setCodeFolder(String codeFolder);

    /**
     * Sets the passed value as new complex foreign key definition for the table.
     *
     * @param cfk The new complex foreign key definition for the table or an empty string to
     *         reset the specification. <CODE>null</CODE> values will be changed to an empty
     *         string.
     *
     * @changed OLI 22.09.2017 - Added.
     */
    abstract public void setComplexForeignKeyDefinition(String cfk);

    /**
     * Sets the passed value as new complex unique specification for the table.
     *
     * @param specification The new complex unique specification for the table or an empty
     *         string to reset the specification. <CODE>null</CODE> values will be changed to an
     *         empty string.
     *
     * @changed OLI 05.08.2013 - Added.
     */
    abstract public void setComplexUniqueSpecification(String specification);

    /**
     * Sets the passed string as new context name for the table.
     *
     * @param contextName The new context name for the table.
     *
     * @changed OLI 23.08.2013 - Added.
     */
    abstract public void setContextName(String contextName);

    /**
     * Sets the passed data model as new data model for the table.
     *
     * @param dataModel The new data model for the table.
     *
     * @changed OLI 25.10.2013 - Added.
     */
    abstract public void setDataModel(DataModel dataModel);

    /**
     * Sets or unsets the deprecated flag of the table.
     *
     * @param b The new value for the deprecated flag of the table.
     *
     * @changed OLI 23.08.2013 - Added.
     * /
    abstract public void setDeprecated(boolean b);

    /**
     * Sets or unsets the draft flag of the table.
     *
     * @param b The new value for the draft flag of the table.
     *
     * @changed OLI 02.03.2016 - Added.
     */
    abstract public void setDraft(boolean b);

    /**
     * Sets or removes the flag which marks the table as an external one. 
     *
     * @param externalTable Set this flag to mark the table as an external one.
     *
     * @changed OLI 07.01.2015 - Added.
     */
    abstract public void setExternalTable(boolean externalTable);

    /**
     * Sets or unsets the flag which marks a first generator run as done for the table.
     *
     * @param b The new state of the first generation done flag for the table.
     *
     * @changed OLI 23.08.2013 - Added (from <CODE>TabellenModel</CODE> interface.
     */
    abstract public void setFirstGenerationDone(boolean b);

    /**
     * Sets or unsets the generate flag of the table.
     *
     * @param b The new value for the generate flag of the table.
     *
     * @changed OLI 23.08.2013 - Added.
     */
    abstract public void setGenerateCode(boolean b);

    /**
     * Sets the passed string as new code generator options for the table.
     *
     * @param options The new code generator options for the table.
     *
     * @changed OLI 23.08.2013 - Added.
     */
    abstract public void setGenerateCodeOptions(String options);

    /**
     * Sets a new name for the table.
     *
     * @param name The new name for the table.
     *
     * @changed OLI 02.03.2016 - Added.
     */
    abstract public void setName(String name);

    /**
     * Sets a new value for the many-to-many relation flag.
     *
     * @param manyToMany The new value for the many-to-many relation flag.
     *
     * @deprecated OLI 13.10.2017 - Use "setManyToManyRelation(boolean)" instead of.
     */
    @Deprecated abstract public void setNMRelation(boolean manyToMany);

    /**
     * Sets a new value for the many-to-many relation flag.
     *
     * @param manyToMany The new value for the many-to-many relation flag.
     *
     * @changed OLI 13.10.2017 - Added.
     */
    abstract public void setManyToManyRelation(boolean manyToMany);

}