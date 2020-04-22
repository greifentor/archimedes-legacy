/*
 * ObjectFactory.java
 *
 * 15.06.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.scheme.xml;

import java.awt.*;

import archimedes.model.*;
import corent.base.*;
import corent.gui.*;
import gengen.metadata.*;

/**
 * An interface for an object factory which creates implementations of the model interfaces.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 15.06.2016 - Added.
 */

public interface ObjectFactory {

    /**
     * Creates a column with the passed parameters.
     * 
     * @param name The name of the column.
     * @param domain The domain of the column.
     * @return A sequence with the passed parameters.
     *
     * @changed OLI 15.06.2016 - Added.
     */
    abstract public ColumnModel createColumn(String name, DomainModel domain);

    /**
     * Creates a new data model.
     * 
     * @return A new data model.
     *
     * @changed OLI 15.06.2016 - Added.
     */
    abstract public DataModel createDataModel();

    /**
     * Creates a new domain.
     * 
     * @param name The name of the domain.
     * @param dataType The data type of the domain.
     * @param length The length.
     * @param decimalPlace The decimal place.
     * @return The new domain.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public DomainModel createDomain(String name, int dataType, int length,
            int decimalPlace);

    /**
     * Creates a new index meta data with passed parameters.
     *
     * @param name The name for the index meta data.
     * @param cmd The class meta data model which the index meta data is to create for.
     * @return The new index meta data.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public IndexMetaData createIndexMetaData(String name, ClassMetaData cmd);

    /**
     * Returns a new main view for the data model.
     *
     * @param name The name of the main view.
     * @param description A description for the main view.
     * @param showReferencedColumns Set this flag if the reference information should be shown.
     * @return A new main view for the data model.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public ViewModel createMainView(String name, String description,
            boolean showReferencedColumns); 

    /**
     * Creates a new n-reference for the table.
     * 
     * @param table The table which the n-reference is to create for.
     * @return A new n-reference for the table.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public NReferenceModel createNReference(TableModel table);

    /**
     * Creates a new order member for the column.
     * 
     * @param column The column which the order member is to create for.
     * @return A new order member for the column.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public OrderMemberModel createOrderMember(ColumnModel column);

    /**
     * Creates a new panel for a data view.
     * 
     * @return A new panel for a data view.
     *
     * @changed OLI 15.06.2016 - Added.
     */
    abstract public PanelModel createPanel();

    /**
     * Generates a relation with the passed parameters.
     *
     * @param view The view where the reference is created for.
     * @param t1 The column which is referencing column t2.
     * @param direction1 The direction which the relation meets the referencing table.
     * @param offset1 The offset of the position of the relation line at the referencing table.
     * @param t2 The column of the referenced table.
     * @param direction2 The direction which the relation meets the referenced table.
     * @param offset2 The offset of the position of the relation line at the referenced table.
     * @return The new relation.
     *
     * @changed OLI 16.06.2016 - Added (from class "Relation").
     */ 
    abstract public RelationModel createRelation(ViewModel view, ColumnModel t1,
            Direction direction1, int offset1, ColumnModel t2, Direction direction2, int offset2
            );

    /**
     * Creates a sequence with the passed parameters.
     * 
     * @param name The name of the sequence.
     * @param increment An increment value for the sequence.
     * @param startValue A start value for the sequence.
     * @return A sequence with the passed parameters.
     *
     * @changed OLI 15.06.2016 - Added.
     */
    abstract public SequenceModel createSequence(String name, int increment, int startValue);

    /**
     * Creates a stereotype with the passed parameters.
     * 
     * @param name The name of the stereotype.
     * @param comment A comment for the stereotype.
     * @return A stereotype with the passed parameters.
     *
     * @changed OLI 15.06.2016 - Added.
     */
    abstract public StereotypeModel createStereotype(String name, String comment);

    /**
     * Creates a table with the passed parameters.
     * 
     * @param dataModel The data model which the table is a part of.
     * @param view The view which the table belongs to.
     * @return A table with the passed parameters.
     *
     * @changed OLI 15.06.2016 - Added.
     */
    abstract public TableModel createTable(DataModel dataModel, ViewModel view);

    /**
     * Creates a to string container with the passed parameters.
     * 
     * @param column The column which the to string container is a part of.
     * @param prefix The prefix of the string.
     * @param suffix The suffix of the string.
     * @return A table with the passed parameters.
     *
     * @changed OLI 15.06.2016 - Added.
     */
    abstract public ToStringContainerModel createToStringContainer(ColumnModel column,
            String prefix, String suffix);

    /**
     * Returns a new view for the data model.
     *
     * @return A new view for the data model.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public ViewModel createView(); 

    /**
     * Returns a color for the passed name.
     *
     * @param name The name of the color which should be returned.
     * @param defaultColor A default value which will be returned in case of no color is found
     *         for the passed name.
     * @return The color for the passed name or the default color if no color is found for the
     *         passed name.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public Color getColor(String name, Color defaultColor);

    /**
     * Returns an access to the palette of the data model.
     *
     * @return An access to the palette of the data model.
     *
     * @changed OLI 16.06.2016 - Added.
     */
    abstract public AbstractExtendedColorPalette getPalette();

}