/*
 * ColumnModel.java
 *
 * 25.04.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;


/**
 * This interface describes the methods of a table column.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 25.04.2013 - Added.
 */

public interface ColumnModel extends CommentOwner, Comparable, NamedObject, OptionListProvider {

    /**
     * Checks if the column can be referenced by other fields.
     *
     * @return <CODE>true</CODE> if the column can be referenced by other fields.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean canBeReferenced();

    /**
     * Returns a copy of the column model.
     *
     * @return A copy of the column model.
     *
     * @changed OLI 19.10.2017 - Added.
     */
    abstract public ColumnModel createCopy();

    /**
     * Returns the comment for the column.
     *
     * @return The comment for the column.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public String getComment();

    /**
     * Returns the initial value for the column (from domain if non defined in the column
     * itself).
     *
     * @return The initial value for the column (from domain if non defined in the column
     *         itself).
     */
    abstract public String getDefaultValue();

    /**
     * Returns the domain which defines the type of the column.
     *
     * @return The domain which defines the type of the column.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public DomainModel getDomain();

    /**
     * Returns the default label text for the column which is defined in the model.
     *
     * @return The default label text for the column which is defined in the model.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public String getEditorLabelText();

    /**
     * Returns the maximum number of character which can be input in a component for this field.
     *
     * @return The maximum number of character which can be input in a component for this field.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public int getEditorMaxLength();

    /**
     * Returns the default tool tip text for the column which is defined in the model.
     *
     * @return The default tool tip text for the column which is defined in the model.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public String getEditorMnemonic();

    /**
     * Returns the position of the column which in a GUI which is configured by the model.
     *
     * @return The position of the column which in a GUI which is configured by the model.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public int getEditorPosition();

    /**
     * Returns the reference weight which is used to define the type of selection components for
     * a foreign key in a GUI configured by the model.
     *
     * @return The reference weight which is used to define the type of selection components for
     *         a foreign key in a GUI configured by the model.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public ReferenceWeight getEditorReferenceWeight();

    /**
     * Returns the resource id for the column which is defined in the model for using in the
     * GUI.
     *
     * @return The resource id for the column which is defined in the model for using in the
     *         GUI.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public String getEditorResourceId();

    /**
     * Returns the default tool tip text for the column which is defined in the model.
     *
     * @return The default tool tip text for the column which is defined in the model.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public String getEditorToolTipText();

    /** 
     * Returns the full name of the column (table name and column name dot separated).
     * 
     * @return The full name of the column (table name and column name dot separated).
     *
     * @changed OLI 03.05.2013 - Added.
     */
    abstract public String getFullName();

    /**
     * Returns the change history for the column.
     *
     * @return The change history for the column.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public String getHistory();

    /**
     * Returns the individual default value for the column if this is not the same as defined
     * by the domain.
     *
     * @return The individual default value for the column if this is not the same as defined
     *         by the domain.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public String getIndividualDefaultValue();

    /**
     * Returns the name of the column.
     *
     * @return The name of the column.
     *
     * @changed OLI 25.04.2013 - Added.
     */
    abstract public String getName();

    /**
     * Returns the panel on which the field is to show in a generic editor.
     *
     * @return The panel on which the field is to show in a generic editor.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public PanelModel getPanel();

    /**
     * Returns the parameters of the column. This is mainly used to define generator stuff.
     *
     * @return The parameters of the column.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public String getParameters();

    /**
     * Returns the qualified name of the column (means [SCHEME_NAME].TABLE_NAME.COLUMN_NAME). 
     *
     * @return The qualified name of the column.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public String getQualifiedName();

    /**
     * Returns the referenced column if the column references one.
     *
     * @return The referenced column if the column references one otherwise <CODE>null</CODE>.
     *
     * @changed OLI 13.09.2013 - Added.
     */
    abstract public ColumnModel getReferencedColumn();

    /**
     * Returns the referenced table if the column references one.
     *
     * @return The referenced table if the column references one otherwise <CODE>null</CODE>.
     *
     * @changed OLI 26.08.2013 - Added.
     */
    abstract public TableModel getReferencedTable();

    /**
     * Returns the relation to another column if there is one.
     *
     * @return The relation to another column or <CODE>null</CODE> if there is no relation.
     *
     * @changed OLI 25.04.2013 - Added.
     */
    abstract public RelationModel getRelation();

    /**
     * Returns the sequence which is set for key generation.
     *
     * @return The sequence which is set for key generation. If there is no sequence generated
     *         key, the method returns <CODE>null</CODE>.
     *
     * @changed OLI 16.10.2013 - Added.
     */
    abstract public SequenceModel getSequenceForKeyGeneration();

    /**
     * Returns the reference to the table which the column is a part from.
     *
     * @return The reference to the table which the column is a part from.
     *
     * @changed OLI 25.04.2013 - Added.
     */
    abstract public TableModel getTable();

    /**
     * Checks if the column has an index.
     *
     * @return <CODE>true</CODE> if the column has an index.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean hasIndex();

    /**
     * Checks if the object is usable with alter batches.
     *
     * @return <CODE>true</CODE> if the object is usable with alter batches.
     *
     * @changed OLI 20.06.2016 - Added.
     */
    abstract public boolean isAlterInBatch();

    /**
     * Checks if the column is deprecated.
     *
     * @return <CODE>true</CODE> if the column is deprecated.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public boolean isDeprecated();

    /**
     * Checks if the column can be altered in a batch view.
     *
     * @return <CODE>true</CODE> if the column can be altered in a batch view.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean isEditorAlterInBatchView();

    /**
     * Checks if the column is disabled in the generated editor view.
     *
     * @return <CODE>true</CODE> if the column is disabled in the generated editor view.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean isEditorDisabled();

    /**
     * Checks if the column is an editor member.
     *
     * @return <CODE>true</CODE> if the column is an editor member.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean isEditorMember();

    /**
     * Checks if the column is an encrypted field.
     *
     * @return <CODE>true</CODE> if the column is an encrypted field.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean isEncrypted();

    /**
     * Checks if the reference is to hide on the display.
     *
     * @return <CODE>true</CODE> if  the reference is to hide on the display.
     *
     * @changed OLI 20.06.2016 - Added.
     */
    abstract public boolean isHideReference();

    /**
     * Checks if the column is a member of an index search method for the table.
     *
     * @return <CODE>true</CODE> if the column is a member of an index search method for the
     *         table.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean isIndexSearchMember();

    /**
     * Checks if the column if the column contains the information about the last modification
     * of the record (may be a timestamp or a counter).
     *
     * @return <CODE>true</CODE> if the column if the column contains the information about the last modification
     *         of the record (may be a timestamp or a counter).
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean isLastModificationField();

    /**
     * Checks if the column is a list item field (for special storage of lists and their header
     * in a single table).
     *
     * @return <CODE>true</CODE> if the column is a list item field.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean isListItemField();

    /**
     * Checks if the column is not null.
     *
     * @return <CODE>true</CODE> if the column is not null.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean isNotNull();

    /**
     * Checks if the column is a primary key member.
     *
     * @return <CODE>true</CODE> if the column is a primary key member.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean isPrimaryKey();

    /**
     * Checks if the column, in case it is a reference, is not shown in the diagram.
     *
     * @return <CODE>true</CODE> if the column, in case it is a reference, is not shown in the
     *         diagram.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean isReferenceHidden();

    /**
     * Checks if the column is a field which contains the informations about the remove state
     * of the record.
     *
     * @return <CODE>true</CODE> if the column is a field which contains the informations about the remove state
     *         of the record.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean isRemovedStateField();

    /**
     * Checks if the column is a required field.
     *
     * @return <CODE>true</CODE> if the column is a required field.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean isRequired();

    /**
     * Checks if foreign key constraints should be suppressed for the column.
     *
     * @return <CODE>true</CODE> if foreign key constraints should be suppressed for the column.
     *
     * @changed OLI 07.12.2015 - Added.
     */
    abstract public boolean isSuppressForeignKeyConstraint();

    /**
     * Checks if the column is a synchronized field from another data base.
     *
     * @return <CODE>true</CODE> if the column is a synchronized field from another data base.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean isSynchronized();

    /**
     * Checks if the column is a field which is taken as a id to synchronize records of the
     * table or parts there from with other data bases.
     *
     * @return <CODE>true</CODE> if the column is a field which is taken as a id to synchronize records of the
     *         table or parts there from with other data bases.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean isSynchronizeId();

    /**
     * Checks if the column is marked as a transient field. This may be shown in the diagram in
     * a different color.
     *
     * @return <CODE>true</CODE> if the column is marked as a transient field.
     *
     * @changed OLI 11.06.2015 - Added.
     */
    abstract public boolean isTransient();

    /**
     * Checks if the column is marked as a technical field. This may be shown in the diagram in
     * a different color.
     *
     * @return <CODE>true</CODE> if the column is marked as a technical field.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean isTechnicalField();

    /**
     * Checks if the column is marked as unique (complex unique constraints with more than one
     * column have to be defined in the table unique field.
     *
     * @return <CODE>true</CODE> if the column is marked as unique.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public boolean isUnique();

    /**
     * Sets the alter-in-batch flag for the object.
     *
     * @param alterInBatch The new value for the alter in batch flag.
     *
     * @changed OLI 20.06.2016 - Added.
     */
    abstract public void setAlterInBatch(boolean alterInBatch);

    /**
     * Sets a new value for the can be referenced flag of the column.
     *
     * @param canBeReferenced The new value for the can be referenced flag of the column.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setCanBeReferenced(boolean canBeReferenced);

    /**
     * Sets the passed string as new comment for the column.
     *
     * @param comment The new comment for the column. <CODE>null</CODE> values will be changed
     *         to an empty string.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public void setComment(String comment);

    /**
     * Sets a new value for the deprecated flag of the column.
     *
     * @param deprecated The new value for the deprecated flag of the column.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public void setDeprecated(boolean deprecated);

    /**
     * Sets the passed domain as new domain for the column.
     *
     * @param domain The new domain for the column.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setDomain(DomainModel domain);

    /**
     * Sets a new value for the alter in batch view flag of the column.
     *
     * @param alterInBatchView The new value for the alter in batch view flag of the column.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setEditorAlterInBatchView(boolean alterInBatchView);

    /**
     * Sets a new value for the disabled flag of the column in the editor.
     *
     * @param disabled The new value for the disabled flag of the column in the editor.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public void setEditorDisabled(boolean disabled);

    /**
     * Sets the passed label text as the new default label text for the field.
     *
     * @param labelText The new default label text for the field.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setEditorLabelText(String labelText);

    /**
     * Sets the passed maximum length as the new maximum length for the field.
     *
     * @param maxLength The new maximum length for the field.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setEditorMaxLength(int maxLength);

    /**
     * Sets the passed value as the new value for the editor member flag.
     *
     * @param editorMember The new value for the editor member flag.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setEditorMember(boolean editorMember);

    /**
     * Sets the passed mnemonic as the new default mnemonic for the field.
     *
     * @param mnemonic The new default mnemonic for the field.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setEditorMnemonic(String mnemonic);

    /**
     * Sets the passed position as the position in a generated editor panel.
     *
     * @param position The new position in a generated editor panel.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setEditorPosition(int position);

    /**
     * Sets the passed reference weight as the new reference weight for the field (if it is a
     * reference).
     *
     * @param referenceWeight The new reference weight for the field in a generated editor
     *         panel.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setEditorReferenceWeight(ReferenceWeight referenceWeight);

    /**
     * Sets a new value for the resource identifier of the column.
     *
     * @param resourceId The new resource identifier of the column.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setEditorResourceId(String resourceId);

    /**
     * Sets the passed tool tip text as the new default tool tip text for the field.
     *
     * @param toolTipText The new default tool tip text for the field.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setEditorToolTipText(String toolTipText);

    /**
     * Sets a new value for the encrypted flag of the column.
     *
     * @param encrypted The new value for the encrypted flag of the column.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public void setEncrypted(boolean encrypted);

    /**
     * Sets a new value for the hide reference flag of the column.
     *
     * @param hideReference The new value for the hide reference flag of the column.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setHideReference(boolean hideReference);

    /**
     * Sets the passed string as new history for the column.
     *
     * @param history The new history for the column. <CODE>null</CODE> values will be changed
     *         to an empty string.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public void setHistory(String history);

    /**
     * Sets a new value for the index flag of the column.
     *
     * @param index The new value for the index flag of the column.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public void setIndex(boolean index);

    /**
     * Sets a new value for the index search member flag of the column.
     *
     * @param indexSearchMember The new value for the index search member flag of the column.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setIndexSearchMember(boolean indexSearchMember);

    /**
     * Sets the passed value as new individual default value for the column. To set a null value
     * pass the String "NULL" (no quotes in the string).
     *
     * @param value The new individual value for the column.
     * @throws Exception Passing a null pointer.
     *
     * @changed OLI 14.06.2011 - Approved.
     */
    abstract public void setIndividualDefaultValue(String value)
            throws IllegalArgumentException;

    /**
     * Sets a new value for the last modification field flag of the column.
     *
     * @param lastModificationField The new value for the last modification field flag of the
     *         column.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setLastModificationField(boolean lastModificationField);

    /**
     * Sets a new value for the list item field flag of the column.
     *
     * @param listItemField The new value for the list item field flag of the column.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setListItemField(boolean listItemField);

    /**
     * Sets the passed name as the new name for the field.
     *
     * @param name The new name for the field.
     *
     * @changed OLI 03.05.2013 - Added.
     */
    abstract public void setName(String name);

    /**
     * Sets a new value for the not null flag of the column.
     *
     * @param notNull The new value for the not null flag of the column.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public void setNotNull(boolean notNull);

    /**
     * Sets the passed panel as new panel where the field is to show in a generic editor.
     *
     * @param panel The new panel where the field is to show in a generic editor.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setPanel(PanelModel panel);

    /**
     * Sets the passed parameters as the new parameters for the field.
     *
     * @param parameters The new parameters for the field.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setParameters(String parameters);

    /**
     * Sets a new value for the primary key flag of the column.
     *
     * @param primaryKey The new value for the primary key flag of the column.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public void setPrimaryKey(boolean primaryKey);

    /**
     * Sets the passed relation for the column.
     *
     * @param relation The relation which is to set as reference for the column.
     *
     * @changed OLI 29.04.2013 - Added.
     */
    abstract public void setRelation(RelationModel relation);

    /**
     * Sets a new value for the removed state field flag of the column.
     *
     * @param removedStateField The new value for the removed state field flag of the column.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setRemovedStateField(boolean removedStateField);

    /**
     * Sets a new value for the required field flag of the column.
     *
     * @param required  The new value for the required field flag of the column.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setRequired(boolean required);

    /**
     * Sets a sequence which is set for key generation.
     *
     * @param sequence A sequence which is used for the key geenration or <CODE>null</CODE> if
     *         the key is not generated by a sequence.
     *
     * @changed OLI 16.10.2013 - Added.
     */
    abstract public void setSequenceForKeyGeneration(SequenceModel sequence);

    /**
     * Sets a new value for the suppress foreign key constraint flag.
     *
     * @param The new value for the suppress foreign key constraint flag.
     *
     * @changed OLI 07.12.2015 - Added.
     */
    abstract public void setSuppressForeignKeyConstraint(boolean suppressed);

    /**
     * Sets a new value for the synchronized flag of the column.
     *
     * @param synchrnzd The new value for the synchronized flag of the column.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public void setSynchronized(boolean synchrnzd);

    /**
     * Sets a new value for the synchronize id flag of the column.
     *
     * @param synchronizeId The new value for the synchronize id flag of the column.
     *
     * @changed OLI 30.04.2013 - Added.
     */
    abstract public void setSynchronizeId(boolean synchronizeId);

    /**
     * Sets the passed table as new table where the column belongs to.
     *
     * @param table The new table for the column.
     *
     * @changed OLI 25.04.2013 - Added.
     */
    abstract public void setTable(TableModel table) throws IllegalArgumentException;

    /**
     * Sets a new value for the technical field flag of the column.
     *
     * @param technicalField The new value for the technical field flag of the column.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setTechnicalField(boolean technicalField);

    /**
     * Sets a new value for the transient field flag of the column.
     *
     * @param transientField The new value for the transient field flag of the column.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setTransient(boolean transientField);

    /**
     * Sets a new value for the unique flag of the column.
     *
     * @param unique The new value for the unique flag of the column.
     *
     * @changed OLI 02.05.2013 - Added.
     */
    abstract public void setUnique(boolean unique);

}